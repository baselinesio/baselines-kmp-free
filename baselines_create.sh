#!/usr/bin/env bash

set -euo pipefail

readonly TEMPLATE_PACKAGE="io.baselines.sample"
readonly TEMPLATE_APP_NAME="BaselinesSample"
readonly TEMPLATE_DISPLAY_NAME="Baselines Sample"

APP_PACKAGE=""
APP_NAME=""
APP_DISPLAY_NAME=""
OUTPUT_DIR=""
DRY_RUN=false

if [ -n "${BASELINES_TEMPLATE_DIR_OVERRIDE:-}" ]; then
    SCRIPT_DIR="$(cd -- "$BASELINES_TEMPLATE_DIR_OVERRIDE" && pwd -P)"
else
    SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)"
fi
WORK_DIR="$(pwd -P)"
STAGING_DIR=""
TEMP_FILES=("")

usage() {
    cat <<'EOF'
Usage:
  baselines_create.sh [options]

Options:
  -p, --package <package>       New package/application id, for example com.example.myapp
  -n, --name <name>             New app identifier, for example MyApp
  -d, --display-name <name>     New app display name, for example "My App"
  -o, --output <path>           Destination project directory
      --dry-run                 Validate inputs and print the planned destination
  -h, --help                    Show this help

Examples:
  ./baselines_create.sh
  ./baselines_create.sh --package com.example.myapp --name MyApp --display-name "My App"
  ./baselines_create.sh -p com.company.product -n CompanyProduct -d "Company Product" -o ../CompanyProduct

Notes:
  If --output is omitted, the project is created in the current directory.
  When run from the template repository itself, the default destination is next
  to the repository to avoid nesting generated projects inside the template.
EOF
}

fail() {
    printf 'Error: %s\n' "$*" >&2
    exit 1
}

warn() {
    printf 'Warning: %s\n' "$*" >&2
}

cleanup() {
    local file

    for file in "${TEMP_FILES[@]}"; do
        [ -n "$file" ] && [ -e "$file" ] && rm -f -- "$file"
    done

    if [ -n "$STAGING_DIR" ] && [ -d "$STAGING_DIR" ]; then
        rm -rf -- "$STAGING_DIR"
    fi
}

trap cleanup EXIT INT TERM

require_command() {
    command -v "$1" >/dev/null 2>&1 || fail "Required command '$1' was not found."
}

require_commands() {
    require_command git
    require_command find
    require_command perl
    require_command mktemp
    require_command cp
    require_command mv
    require_command mkdir
    require_command rm
    require_command rmdir
    require_command date
}

template_worktree_is_dirty() {
    git -C "$SCRIPT_DIR" rev-parse --is-inside-work-tree >/dev/null 2>&1 || return 1
    [ -n "$(git -C "$SCRIPT_DIR" status --porcelain 2>/dev/null || true)" ]
}

warn_if_template_dirty() {
    [ -z "${BASELINES_TEMPLATE_DIR_OVERRIDE:-}" ] || return 0

    if template_worktree_is_dirty; then
        warn "Template checkout has uncommitted changes."
        warn "Generated metadata will point to the current HEAD, but future migrations cannot reconstruct uncommitted template changes."
        warn "Commit or stash template changes before generating projects you expect to migrate automatically."
    fi
}

parse_args() {
    while [ "$#" -gt 0 ]; do
        case "$1" in
            -p|--package)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                APP_PACKAGE="$2"
                shift 2
                ;;
            -n|--name)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                APP_NAME="$2"
                shift 2
                ;;
            -d|--display-name)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                APP_DISPLAY_NAME="$2"
                shift 2
                ;;
            -o|--output)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                OUTPUT_DIR="$2"
                shift 2
                ;;
            --dry-run)
                DRY_RUN=true
                shift
                ;;
            -h|--help)
                usage
                exit 0
                ;;
            --)
                shift
                break
                ;;
            -*)
                fail "Unknown option: $1"
                ;;
            *)
                fail "Unexpected argument: $1"
                ;;
        esac
    done

    [ "$#" -eq 0 ] || fail "Unexpected trailing arguments."
}

prompt_if_missing() {
    if [ -n "$APP_PACKAGE" ] && [ -n "$APP_NAME" ] && [ -n "$APP_DISPLAY_NAME" ]; then
        return
    fi

    [ -t 0 ] || fail "Missing required options. Run with --help for non-interactive usage."

    printf '=== Provide new project details ===\n'

    if [ -z "$APP_PACKAGE" ]; then
        printf '1) New package name (e.g. com.example.myapp): '
        IFS= read -r APP_PACKAGE
    fi

    if [ -z "$APP_NAME" ]; then
        printf '2) New app identifier (e.g. MyApp): '
        IFS= read -r APP_NAME
    fi

    if [ -z "$APP_DISPLAY_NAME" ]; then
        printf '3) New app display name (e.g. My App): '
        IFS= read -r APP_DISPLAY_NAME
    fi
}

validate_inputs() {
    [ -n "$APP_PACKAGE" ] || fail "Package name is required."
    [ -n "$APP_NAME" ] || fail "App identifier is required."
    [ -n "$APP_DISPLAY_NAME" ] || fail "App display name is required."

    [[ "$APP_PACKAGE" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]] ||
        fail "Package must use lowercase Java/Kotlin-style segments, for example com.example.myapp."

    case "$APP_PACKAGE" in
        "$TEMPLATE_PACKAGE"|"$TEMPLATE_PACKAGE".*)
            fail "Package must not be '$TEMPLATE_PACKAGE' or nested under it."
            ;;
    esac

    [[ "$APP_NAME" =~ ^[A-Za-z][A-Za-z0-9_]*$ ]] ||
        fail "App identifier must be a Swift/Kotlin-safe identifier, for example MyApp."

    case "$APP_DISPLAY_NAME" in
        *$'\n'*|*$'\r'*)
            fail "Display name must be a single line."
            ;;
    esac
}

absolute_path_for() {
    local path="$1"
    local parent
    local name

    case "$path" in
        /*)
            parent="$(dirname -- "$path")"
            name="$(basename -- "$path")"
            ;;
        *)
            parent="$(dirname -- "$WORK_DIR/$path")"
            name="$(basename -- "$path")"
            ;;
    esac

    [ -d "$parent" ] || fail "Output parent directory does not exist: $parent"
    parent="$(cd -- "$parent" && pwd -P)"
    printf '%s/%s\n' "$parent" "$name"
}

resolve_output_dir() {
    if [ -z "$OUTPUT_DIR" ]; then
        if [ "$WORK_DIR" = "$SCRIPT_DIR" ]; then
            OUTPUT_DIR="$(dirname -- "$SCRIPT_DIR")/$APP_NAME"
        else
            OUTPUT_DIR="$WORK_DIR/$APP_NAME"
        fi
    fi

    OUTPUT_DIR="$(absolute_path_for "$OUTPUT_DIR")"
}

preflight_destination() {
    [ -e "$OUTPUT_DIR" ] && fail "Destination already exists: $OUTPUT_DIR"

    case "$OUTPUT_DIR" in
        "$SCRIPT_DIR"|"$SCRIPT_DIR"/*)
            fail "Destination must not be inside the template repository. Use --output outside $SCRIPT_DIR."
            ;;
    esac
}

should_copy_file() {
    local rel_path="$1"
    local name

    name="$(basename -- "$rel_path")"

    case "$rel_path" in
        .git/*|*/.git/*|.gradle/*|*/.gradle/*|.idea/*|*/.idea/*|.kotlin/*|*/.kotlin/*|.fleet/*|*/.fleet/*|.run/*|*/.run/*|build/*|*/build/*|captures/*|*/captures/*|.externalNativeBuild/*|*/.externalNativeBuild/*|.cxx/*|*/.cxx/*)
            return 1
            ;;
        baselines_create.sh|baselines_migrate.sh|README|README.*|LICENSE|LICENSE.*)
            return 1
            ;;
    esac

    case "$name" in
        .DS_Store|*.iml|local.properties)
            return 1
            ;;
    esac

    return 0
}

collect_template_files() {
    if git -C "$SCRIPT_DIR" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
        git -C "$SCRIPT_DIR" ls-files -z --cached --others --exclude-standard
        return
    fi

    find "$SCRIPT_DIR" -type f -print0 | while IFS= read -r -d '' abs_path; do
        printf '%s\0' "${abs_path#"$SCRIPT_DIR"/}"
    done
}

create_temp_file() {
    local temp_file

    temp_file="$(mktemp "${TMPDIR:-/tmp}/baselines-generator.XXXXXX")"
    TEMP_FILES+=("$temp_file")
    printf '%s\n' "$temp_file"
}

copy_template() {
    local file_list="$1"
    local copied_count=0
    local rel_path
    local src
    local dst

    while IFS= read -r -d '' rel_path; do
        [ -n "$rel_path" ] || continue
        should_copy_file "$rel_path" || continue

        src="$SCRIPT_DIR/$rel_path"
        dst="$STAGING_DIR/$rel_path"

        [ -f "$src" ] || continue

        mkdir -p -- "$(dirname -- "$dst")"
        cp -p -- "$src" "$dst"
        copied_count=$((copied_count + 1))
    done < "$file_list"

    printf '%s\n' "$copied_count"
}

is_text_file() {
    perl -e 'exit(-B $ARGV[0] ? 1 : 0)' "$1"
}

should_preserve_template_tokens() {
    local file_path="$1"

    case "${file_path#"$STAGING_DIR"/}" in
        baselines_migrate.sh|.baselines/template.properties)
            return 0
            ;;
    esac

    return 1
}

replace_template_tokens() {
    local scanned_count=0
    local file_path

    export_template_token_env

    while IFS= read -r -d '' file_path; do
        should_preserve_template_tokens "$file_path" && continue
        is_text_file "$file_path" || continue

        perl -0pi -e '
            BEGIN {
                $old_package = $ENV{"BASELINES_OLD_PACKAGE"};
                $new_package = $ENV{"BASELINES_NEW_PACKAGE"};
                $old_app_name = $ENV{"BASELINES_OLD_APP_NAME"};
                $new_app_name = $ENV{"BASELINES_NEW_APP_NAME"};
                $old_display_name = $ENV{"BASELINES_OLD_DISPLAY_NAME"};
                $new_display_name = $ENV{"BASELINES_NEW_DISPLAY_NAME"};
            }
            s/\Q$old_package\E/$new_package/g;
            s/\Q$old_app_name\E/$new_app_name/g;
            s/\Q$old_display_name\E/$new_display_name/g;
        ' "$file_path"

        scanned_count=$((scanned_count + 1))
    done < <(find "$STAGING_DIR" -type f -print0)

    printf '%s\n' "$scanned_count"
}

export_template_token_env() {
    export BASELINES_OLD_PACKAGE="$TEMPLATE_PACKAGE"
    export BASELINES_NEW_PACKAGE="$APP_PACKAGE"
    export BASELINES_OLD_APP_NAME="$TEMPLATE_APP_NAME"
    export BASELINES_NEW_APP_NAME="$APP_NAME"
    export BASELINES_OLD_DISPLAY_NAME="$TEMPLATE_DISPLAY_NAME"
    export BASELINES_NEW_DISPLAY_NAME="$APP_DISPLAY_NAME"
}

rename_template_paths() {
    local path_list="$1"
    local renamed_count=0
    local path
    local parent
    local name
    local new_name
    local new_path

    find "$STAGING_DIR" -depth -name "*$TEMPLATE_APP_NAME*" -print0 > "$path_list"

    while IFS= read -r -d '' path; do
        [ -e "$path" ] || continue

        parent="$(dirname -- "$path")"
        name="$(basename -- "$path")"
        new_name="${name//$TEMPLATE_APP_NAME/$APP_NAME}"
        new_path="$parent/$new_name"

        [ "$path" != "$new_path" ] || continue
        [ ! -e "$new_path" ] || fail "Cannot rename '$path' to existing path '$new_path'."

        mv -- "$path" "$new_path"
        renamed_count=$((renamed_count + 1))
    done < "$path_list"

    printf '%s\n' "$renamed_count"
}

package_path_for() {
    local slash="/"

    printf '%s\n' "${1//./$slash}"
}

prune_empty_parents() {
    local current="$1"
    local stop="$2"

    while [ "$current" != "$stop" ] && [ "$current" != "/" ]; do
        rmdir -- "$current" 2>/dev/null || break
        current="$(dirname -- "$current")"
    done
}

merge_directory_contents() {
    local source_dir="$1"
    local target_dir="$2"
    local item
    local target

    shopt -s dotglob nullglob
    for item in "$source_dir"/*; do
        target="$target_dir/$(basename -- "$item")"
        [ ! -e "$target" ] || fail "Cannot merge '$item' into existing path '$target'."
        mv -- "$item" "$target_dir/"
    done
    shopt -u dotglob nullglob

    rmdir -- "$source_dir"
}

relocate_package_directories() {
    local package_dir_list="$1"
    local old_package_path
    local new_package_path
    local relocated_count=0
    local old_dir
    local source_root
    local new_dir

    old_package_path="$(package_path_for "$TEMPLATE_PACKAGE")"
    new_package_path="$(package_path_for "$APP_PACKAGE")"

    find "$STAGING_DIR" -type d -path "*/$old_package_path" -print0 > "$package_dir_list"

    while IFS= read -r -d '' old_dir; do
        [ -d "$old_dir" ] || continue

        source_root="${old_dir%/$old_package_path}"
        new_dir="$source_root/$new_package_path"

        [ "$old_dir" != "$new_dir" ] || continue

        case "$new_dir" in
            "$old_dir"/*)
                fail "Package '$APP_PACKAGE' would move package directories inside themselves."
                ;;
        esac

        if [ -e "$new_dir" ]; then
            [ -d "$new_dir" ] || fail "Package destination exists and is not a directory: $new_dir"
            merge_directory_contents "$old_dir" "$new_dir"
        else
            mkdir -p -- "$(dirname -- "$new_dir")"
            mv -- "$old_dir" "$new_dir"
        fi

        prune_empty_parents "$(dirname -- "$old_dir")" "$source_root"
        relocated_count=$((relocated_count + 1))
    done < "$package_dir_list"

    printf '%s\n' "$relocated_count"
}

warn_for_residual_tokens() {
    local has_residual=false
    local file_path

    export_template_token_env

    while IFS= read -r -d '' file_path; do
        should_preserve_template_tokens "$file_path" && continue
        is_text_file "$file_path" || continue

        if perl -0ne '
            BEGIN { $found = 0; }
            if (/\Q$ENV{"BASELINES_OLD_PACKAGE"}\E/
                || /\Q$ENV{"BASELINES_OLD_APP_NAME"}\E/
                || /\Q$ENV{"BASELINES_OLD_DISPLAY_NAME"}\E/) {
                $found = 1;
            }
            END { exit($found ? 0 : 1); }
        ' "$file_path"; then
            if [ "$has_residual" = false ]; then
                printf 'Warning: residual template tokens found in generated files:\n' >&2
                has_residual=true
            fi
            printf '  %s\n' "${file_path#"$STAGING_DIR"/}" >&2
        fi
    done < <(find "$STAGING_DIR" -type f -print0)
}

template_git_value() {
    local override=""
    local value=""

    case "$1" in
        repo)
            override="${BASELINES_TEMPLATE_REPOSITORY_OVERRIDE:-}"
            value="$(git -C "$SCRIPT_DIR" remote get-url origin 2>/dev/null || true)"
            ;;
        revision)
            override="${BASELINES_TEMPLATE_REVISION_OVERRIDE:-}"
            value="$(git -C "$SCRIPT_DIR" rev-parse HEAD 2>/dev/null || true)"
            ;;
        version)
            override="${BASELINES_TEMPLATE_VERSION_OVERRIDE:-}"
            value="$(git -C "$SCRIPT_DIR" describe --tags --always --dirty 2>/dev/null || true)"
            ;;
    esac

    if [ -n "$override" ]; then
        printf '%s\n' "$override"
        return
    fi

    printf '%s\n' "$value"
}

write_metadata_var() {
    local key="$1"
    local value="$2"

    printf '%s=%s\n' "$key" "$value"
}

write_project_metadata() {
    local metadata_dir="$STAGING_DIR/.baselines"
    local metadata_file="$metadata_dir/template.properties"
    local generated_at

    generated_at="${BASELINES_GENERATED_AT:-$(date -u '+%Y-%m-%dT%H:%M:%SZ')}"

    mkdir -p -- "$metadata_dir"

    {
        printf '# Generated by baselines_create.sh. Keep this file committed.\n'
        printf '# It lets baselines_migrate.sh compare this project with future template releases.\n'
        write_metadata_var "BASELINES_METADATA_VERSION" "1"
        write_metadata_var "BASELINES_TEMPLATE_PACKAGE" "$TEMPLATE_PACKAGE"
        write_metadata_var "BASELINES_TEMPLATE_APP_NAME" "$TEMPLATE_APP_NAME"
        write_metadata_var "BASELINES_TEMPLATE_DISPLAY_NAME" "$TEMPLATE_DISPLAY_NAME"
        write_metadata_var "BASELINES_TEMPLATE_REPOSITORY" "$(template_git_value repo)"
        write_metadata_var "BASELINES_TEMPLATE_REVISION" "$(template_git_value revision)"
        write_metadata_var "BASELINES_TEMPLATE_VERSION" "$(template_git_value version)"
        write_metadata_var "BASELINES_PROJECT_PACKAGE" "$APP_PACKAGE"
        write_metadata_var "BASELINES_PROJECT_NAME" "$APP_NAME"
        write_metadata_var "BASELINES_PROJECT_DISPLAY_NAME" "$APP_DISPLAY_NAME"
        write_metadata_var "BASELINES_GENERATED_AT" "$generated_at"
    } > "$metadata_file"
}

print_plan() {
    printf '=== Baselines project generator ===\n'
    printf 'Template:      %s\n' "$SCRIPT_DIR"
    printf 'Destination:   %s\n' "$OUTPUT_DIR"
    printf 'Package:       %s\n' "$APP_PACKAGE"
    printf 'App name:      %s\n' "$APP_NAME"
    printf 'Display name:  %s\n' "$APP_DISPLAY_NAME"
}

main() {
    local file_list
    local path_list
    local package_dir_list
    local destination_parent
    local copied_count
    local scanned_count
    local renamed_count
    local relocated_count

    require_commands
    parse_args "$@"
    prompt_if_missing
    validate_inputs
    resolve_output_dir
    preflight_destination
    print_plan
    warn_if_template_dirty

    if [ "$DRY_RUN" = true ]; then
        printf '\nDry run complete. No files were written.\n'
        exit 0
    fi

    file_list="$(create_temp_file)"
    path_list="$(create_temp_file)"
    package_dir_list="$(create_temp_file)"

    collect_template_files > "$file_list"

    destination_parent="$(dirname -- "$OUTPUT_DIR")"
    STAGING_DIR="$(mktemp -d "$destination_parent/.${APP_NAME}.staging.XXXXXX")"

    printf '\nGenerating project...\n'
    copied_count="$(copy_template "$file_list")"
    printf '[ok] Copied %s files\n' "$copied_count"

    scanned_count="$(replace_template_tokens)"
    printf '[ok] Updated template tokens in %s text files\n' "$scanned_count"

    renamed_count="$(rename_template_paths "$path_list")"
    printf '[ok] Renamed %s paths\n' "$renamed_count"

    relocated_count="$(relocate_package_directories "$package_dir_list")"
    printf '[ok] Relocated %s package roots\n' "$relocated_count"

    write_project_metadata
    printf '[ok] Wrote .baselines/template.properties\n'

    warn_for_residual_tokens

    mv -- "$STAGING_DIR" "$OUTPUT_DIR"
    STAGING_DIR=""

    printf '\nDone. New project created at:\n  %s\n' "$OUTPUT_DIR"
    printf '\nNext steps:\n'
    printf '  cd "%s"\n' "$OUTPUT_DIR"
    printf '  ./gradlew projects\n'
}

main "$@"
