#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)"
TARGET_DIR="$(pwd -P)"
TEMPLATE_DIR=""
FROM_TEMPLATE_DIR=""
FROM_REV=""
FROM_VERSION=""
TO_REV=""
OUTPUT_DIR=""
APP_PACKAGE=""
APP_NAME=""
APP_DISPLAY_NAME=""
INIT_METADATA=false
APPLY_PATCH=false
ALLOW_DIRTY=false
KEEP_WORK=false
NO_PROGRESS=false
DEBUG_REPORT=false

WORK_ROOT=""
REPORT_DIR=""
APPLY_STATUS="not-requested"
METADATA_STATUS="not-requested"
CURRENT_STEP=""
PROGRESS_STEP=0
PROGRESS_TOTAL=0

usage() {
    cat <<'EOF'
Usage:
  baselines_migrate.sh [options]

Default mode:
  Generates a migration report for an existing project by comparing:
    1. the old branded Baselines template state
    2. the new branded Baselines template state
    3. the current target project

Options:
  -t, --target <path>           Existing project to migrate. Defaults to current directory.
      --template <path>         New Baselines template checkout. Defaults to this script's
                                directory when baselines_create.sh is present.
      --from-rev <rev>          Previous Baselines template revision. Defaults to metadata.
      --to-rev <rev>            New Baselines template revision. Defaults to the template
                                checkout's current working tree.
      --from-template <path>    Previous Baselines template checkout. Use this when the
                                previous revision is not reachable from --template.
  -p, --package <package>       Project package/application id. Defaults to metadata.
  -n, --name <name>             Project app identifier. Defaults to metadata.
  -d, --display-name <name>     Project display name. Defaults to metadata.
  -o, --output <path>           Report directory. Defaults to .baselines/migrations/<timestamp>.
      --apply                   Apply apply.patch content only if git apply --check passes.
      --allow-dirty             Allow --apply with existing target worktree changes.
      --init-metadata           Write .baselines/template.properties for older generated projects.
      --keep-work               Keep temporary generated projects for debugging.
      --no-progress             Suppress progress step output.
      --debug-report            Keep detailed logs, status files, and raw diffs under debug/.
  -h, --help                    Show this help.

Examples:
  ../baselines-kmp/baselines_migrate.sh --target . --template ../baselines-kmp --to-rev v1.4.0
  ./baselines_migrate.sh --template ../baselines-kmp --to-rev v1.4.0 --apply
  ../baselines-kmp/baselines_migrate.sh --target . --template ../baselines-kmp \
      --init-metadata --from-rev v1.3.0 -p com.example.myapp -n MyApp -d "My App"
EOF
}

fail() {
    printf 'Error: %s\n' "$*" >&2
    exit 1
}

warn() {
    printf 'Warning: %s\n' "$*" >&2
}

progress_total_for_run() {
    if [ "$APPLY_PATCH" = true ]; then
        printf '11\n'
    else
        printf '10\n'
    fi
}

progress() {
    PROGRESS_STEP=$((PROGRESS_STEP + 1))
    CURRENT_STEP="$*"

    [ "$NO_PROGRESS" = false ] || return 0

    printf '[%d/%d] %s\n' "$PROGRESS_STEP" "$PROGRESS_TOTAL" "$CURRENT_STEP" >&2
}

cleanup() {
    local status=$?

    if [ "$status" -ne 0 ] && [ -n "$REPORT_DIR" ] && [ -d "$REPORT_DIR" ] && [ -n "$OUTPUT_DIR" ] && [ ! -e "$OUTPUT_DIR" ]; then
        mkdir -p -- "$(dirname -- "$OUTPUT_DIR")"
        if [ ! -f "$REPORT_DIR/README.md" ]; then
            {
                printf '# Baselines Migration Report\n\n'
                printf 'Migration failed before the full report could be generated.\n\n'
                printf 'Target project:\n`%s`\n\n' "$TARGET_DIR"
                printf 'Template:\n`%s`\n\n' "${TEMPLATE_DIR:-<not set>}"
                if [ -n "$CURRENT_STEP" ]; then
                    printf 'Last migration step:\n`%s`\n\n' "$CURRENT_STEP"
                fi
                printf 'Check any generated `*.log` files in this directory for the failing step.\n'
            } > "$REPORT_DIR/README.md"
        fi
        mv -- "$REPORT_DIR" "$OUTPUT_DIR"
        REPORT_DIR=""
        printf 'Partial migration report written to: %s\n' "$OUTPUT_DIR" >&2
    fi

    if [ "$KEEP_WORK" = false ] && [ -n "$WORK_ROOT" ] && [ -d "$WORK_ROOT" ]; then
        rm -rf -- "$WORK_ROOT"
    elif [ "$KEEP_WORK" = true ] && [ -n "$WORK_ROOT" ] && [ -d "$WORK_ROOT" ]; then
        printf 'Temporary work directory kept at: %s\n' "$WORK_ROOT" >&2
    fi

    return "$status"
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
    require_command mkdir
    require_command cp
    require_command mv
    require_command rm
    require_command date
    require_command sed
    require_command tar
}

parse_args() {
    while [ "$#" -gt 0 ]; do
        case "$1" in
            -t|--target)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                TARGET_DIR="$2"
                shift 2
                ;;
            --template)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                TEMPLATE_DIR="$2"
                shift 2
                ;;
            --from-template)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                FROM_TEMPLATE_DIR="$2"
                shift 2
                ;;
            --from-rev)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                FROM_REV="$2"
                shift 2
                ;;
            --to-rev)
                [ "$#" -ge 2 ] || fail "$1 requires a value."
                TO_REV="$2"
                shift 2
                ;;
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
            --apply)
                APPLY_PATCH=true
                shift
                ;;
            --allow-dirty)
                ALLOW_DIRTY=true
                shift
                ;;
            --init-metadata)
                INIT_METADATA=true
                shift
                ;;
            --keep-work)
                KEEP_WORK=true
                shift
                ;;
            --no-progress)
                NO_PROGRESS=true
                shift
                ;;
            --debug-report)
                DEBUG_REPORT=true
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

existing_dir() {
    local path="$1"

    [ -d "$path" ] || fail "Directory does not exist: $path"
    (cd -- "$path" && pwd -P)
}

resolve_paths() {
    TARGET_DIR="$(existing_dir "$TARGET_DIR")"

    if [ -z "$TEMPLATE_DIR" ] && [ -f "$SCRIPT_DIR/baselines_create.sh" ]; then
        TEMPLATE_DIR="$SCRIPT_DIR"
    fi

    if [ -n "$TEMPLATE_DIR" ]; then
        TEMPLATE_DIR="$(existing_dir "$TEMPLATE_DIR")"
    fi

    if [ -n "$FROM_TEMPLATE_DIR" ]; then
        FROM_TEMPLATE_DIR="$(existing_dir "$FROM_TEMPLATE_DIR")"
    fi

    if [ -n "$OUTPUT_DIR" ]; then
        case "$OUTPUT_DIR" in
            /*) ;;
            *) OUTPUT_DIR="$TARGET_DIR/$OUTPUT_DIR" ;;
        esac
    fi
}

load_metadata() {
    local metadata_file="$TARGET_DIR/.baselines/template.properties"
    local line
    local key
    local value

    [ -f "$metadata_file" ] || return 0

    while IFS= read -r line || [ -n "$line" ]; do
        case "$line" in
            ''|\#*) continue ;;
        esac

        key="${line%%=*}"
        value="${line#*=}"

        case "$key" in
            BASELINES_PROJECT_PACKAGE)
                [ -z "$APP_PACKAGE" ] && APP_PACKAGE="$value"
                ;;
            BASELINES_PROJECT_NAME)
                [ -z "$APP_NAME" ] && APP_NAME="$value"
                ;;
            BASELINES_PROJECT_DISPLAY_NAME)
                [ -z "$APP_DISPLAY_NAME" ] && APP_DISPLAY_NAME="$value"
                ;;
            BASELINES_TEMPLATE_REVISION)
                [ -z "$FROM_REV" ] && FROM_REV="$value"
                ;;
            BASELINES_TEMPLATE_VERSION)
                [ -z "$FROM_VERSION" ] && FROM_VERSION="$value"
                ;;
        esac
    done < "$metadata_file"
}

warn_if_from_template_was_dirty() {
    case "$FROM_VERSION" in
        *-dirty*)
            warn "Target metadata says it was generated from a dirty template version: $FROM_VERSION"
            warn "The recorded revision can only recreate committed template files, not the uncommitted changes that were copied into this project."
            warn "Automatic apply may fail with already-applied or mismatched hunks. For reliable migration tests, commit the template before generating the target project."
            ;;
    esac
}

validate_project_values() {
    [ -n "$APP_PACKAGE" ] || fail "Project package is required. Provide --package or commit .baselines/template.properties."
    [ -n "$APP_NAME" ] || fail "Project app identifier is required. Provide --name or commit .baselines/template.properties."
    [ -n "$APP_DISPLAY_NAME" ] || fail "Project display name is required. Provide --display-name or commit .baselines/template.properties."

    [[ "$APP_PACKAGE" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]] ||
        fail "Package must use lowercase Java/Kotlin-style segments, for example com.example.myapp."

    [[ "$APP_NAME" =~ ^[A-Za-z][A-Za-z0-9_]*$ ]] ||
        fail "App identifier must be a Swift/Kotlin-safe identifier, for example MyApp."

    case "$APP_DISPLAY_NAME" in
        *$'\n'*|*$'\r'*)
            fail "Display name must be a single line."
            ;;
    esac
}

template_git_value() {
    local template_dir="$1"
    local kind="$2"
    local revision="${3:-HEAD}"
    local value=""

    case "$kind" in
        repo)
            value="$(git -C "$template_dir" remote get-url origin 2>/dev/null || true)"
            ;;
        revision)
            value="$(git -C "$template_dir" rev-parse "$revision^{commit}" 2>/dev/null || true)"
            ;;
        version)
            value="$(git -C "$template_dir" describe --tags --always --dirty "$revision" 2>/dev/null || true)"
            ;;
    esac

    printf '%s\n' "$value"
}

template_revision_for() {
    local template_dir="$1"
    local revision="$2"
    local resolved=""

    if git -C "$template_dir" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
        if [ -n "$revision" ]; then
            resolved="$(git -C "$template_dir" rev-parse "$revision^{commit}" 2>/dev/null || true)"
        fi

        if [ -z "$resolved" ]; then
            resolved="$(git -C "$template_dir" rev-parse HEAD 2>/dev/null || true)"
        fi
    else
        resolved="$revision"
    fi

    printf '%s\n' "$resolved"
}

template_version_for() {
    local template_dir="$1"
    local revision="$2"
    local version=""

    if git -C "$template_dir" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
        if [ -n "$revision" ]; then
            version="$(git -C "$template_dir" describe --tags --always --dirty "$revision" 2>/dev/null || true)"
        fi

        if [ -z "$version" ]; then
            version="$(git -C "$template_dir" describe --tags --always --dirty 2>/dev/null || true)"
        fi
    fi

    printf '%s\n' "$version"
}

write_metadata_var() {
    printf '%s=%s\n' "$1" "$2"
}

write_metadata_content() {
    local template_repo="$1"
    local template_revision="$2"
    local template_version="$3"
    local generated_at="$4"

    [ -n "$generated_at" ] || generated_at="$(date -u '+%Y-%m-%dT%H:%M:%SZ')"

    printf '# Baselines template provenance. Keep this file committed.\n'
    write_metadata_var "BASELINES_METADATA_VERSION" "1"
    write_metadata_var "BASELINES_TEMPLATE_PACKAGE" "io.baselines.sample"
    write_metadata_var "BASELINES_TEMPLATE_APP_NAME" "BaselinesSample"
    write_metadata_var "BASELINES_TEMPLATE_DISPLAY_NAME" "Baselines Sample"
    write_metadata_var "BASELINES_TEMPLATE_REPOSITORY" "$template_repo"
    write_metadata_var "BASELINES_TEMPLATE_REVISION" "$template_revision"
    write_metadata_var "BASELINES_TEMPLATE_VERSION" "$template_version"
    write_metadata_var "BASELINES_PROJECT_PACKAGE" "$APP_PACKAGE"
    write_metadata_var "BASELINES_PROJECT_NAME" "$APP_NAME"
    write_metadata_var "BASELINES_PROJECT_DISPLAY_NAME" "$APP_DISPLAY_NAME"
    write_metadata_var "BASELINES_GENERATED_AT" "$generated_at"
}

write_metadata_file() {
    local metadata_dir="$TARGET_DIR/.baselines"
    local metadata_file="$metadata_dir/template.properties"
    local template_repo=""
    local template_revision="$FROM_REV"
    local template_version=""

    if [ -n "$TEMPLATE_DIR" ] && git -C "$TEMPLATE_DIR" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
        template_repo="$(template_git_value "$TEMPLATE_DIR" repo)"
        [ -n "$template_revision" ] || template_revision="$(template_git_value "$TEMPLATE_DIR" revision HEAD)"
        template_version="$(template_git_value "$TEMPLATE_DIR" version "${FROM_REV:-HEAD}")"
    fi

    [ -n "$template_revision" ] || template_revision="unknown"

    mkdir -p -- "$metadata_dir"

    write_metadata_content "$template_repo" "$template_revision" "$template_version" "" > "$metadata_file"

    printf 'Metadata written to %s\n' "$metadata_file"
}

write_next_metadata_file() {
    local template_repo="$1"
    local template_revision="$2"
    local template_version="$3"

    write_metadata_content "$template_repo" "$template_revision" "$template_version" "" \
        > "$REPORT_DIR/template.properties.next"
}

apply_next_metadata_file() {
    local metadata_dir="$TARGET_DIR/.baselines"
    local metadata_file="$metadata_dir/template.properties"
    local metadata_log="$REPORT_DIR/metadata.log"

    mkdir -p -- "$metadata_dir"
    cp -p -- "$REPORT_DIR/template.properties.next" "$metadata_file"
    METADATA_STATUS="updated"
    printf 'Updated %s from template.properties.next.\n' "$metadata_file" > "$metadata_log"
}

ensure_migration_inputs() {
    [ -n "$TEMPLATE_DIR" ] || fail "New template checkout is required. Pass --template /path/to/baselines-kmp."
    [ -f "$TEMPLATE_DIR/baselines_create.sh" ] || fail "Template checkout does not contain baselines_create.sh: $TEMPLATE_DIR"

    if [ -z "$FROM_TEMPLATE_DIR" ] && [ -z "$FROM_REV" ]; then
        fail "Previous template revision is required. Commit .baselines/template.properties or pass --from-rev/--from-template."
    fi
}

prepare_output_dir() {
    local timestamp

    if [ -z "$OUTPUT_DIR" ]; then
        timestamp="$(date -u '+%Y%m%dT%H%M%SZ')"
        OUTPUT_DIR="$TARGET_DIR/.baselines/migrations/$timestamp"
    fi

    [ ! -e "$OUTPUT_DIR" ] || fail "Report directory already exists: $OUTPUT_DIR"
}

prepare_work_root() {
    WORK_ROOT="$(mktemp -d "${TMPDIR:-/tmp}/baselines-migrate.XXXXXX")"
    REPORT_DIR="$WORK_ROOT/report"
    mkdir -p -- "$REPORT_DIR"
}

prepare_template_checkout() {
    local source_dir="$1"
    local revision="$2"
    local output_dir="$3"
    local label="$4"

    if [ -z "$revision" ]; then
        printf '%s\n' "$source_dir"
        return
    fi

    git -C "$source_dir" rev-parse --is-inside-work-tree >/dev/null 2>&1 ||
        fail "$label template is not a git checkout: $source_dir"

    mkdir -p -- "$output_dir"
    git -C "$source_dir" archive "$revision" | tar -x -C "$output_dir" ||
        fail "Could not archive $label template revision '$revision'."

    printf '%s\n' "$output_dir"
}

generate_project_from_template() {
    local template_dir="$1"
    local output_dir="$2"
    local log_file="$3"
    local template_repo="$4"
    local template_revision="$5"
    local template_version="$6"
    local generator="$template_dir/baselines_create.sh"
    local generator_template_dir="$template_dir"

    if [ ! -f "$generator" ]; then
        generator="$TEMPLATE_DIR/baselines_create.sh"
        [ -f "$generator" ] || fail "Generator not found in template checkout or current template: $template_dir/baselines_create.sh"
    fi

    BASELINES_GENERATED_AT="2000-01-01T00:00:00Z" \
        BASELINES_TEMPLATE_DIR_OVERRIDE="$generator_template_dir" \
        BASELINES_TEMPLATE_REPOSITORY_OVERRIDE="$template_repo" \
        BASELINES_TEMPLATE_REVISION_OVERRIDE="$template_revision" \
        BASELINES_TEMPLATE_VERSION_OVERRIDE="$template_version" \
        bash "$generator" \
            --package "$APP_PACKAGE" \
            --name "$APP_NAME" \
            --display-name "$APP_DISPLAY_NAME" \
            --output "$output_dir" \
            > "$log_file" 2>&1 ||
        {
            printf 'Generator log:\n' >&2
            sed -n '1,160p' "$log_file" >&2
            fail "Failed to generate project from template: $template_dir"
        }
}

should_snapshot_file() {
    local rel_path="$1"
    local name

    name="$(basename -- "$rel_path")"

    case "$rel_path" in
        .git/*|*/.git/*|.gradle/*|*/.gradle/*|.idea/*|*/.idea/*|.kotlin/*|*/.kotlin/*|.fleet/*|*/.fleet/*|.run/*|*/.run/*|build/*|*/build/*|captures/*|*/captures/*|.externalNativeBuild/*|*/.externalNativeBuild/*|.cxx/*|*/.cxx/*|.baselines/migrations/*)
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

is_template_support_file() {
    local rel_path="$1"

    case "$rel_path" in
        baselines_create.sh|baselines_migrate.sh|*_baselines.py)
            return 0
            ;;
        README|README.*|LICENSE|LICENSE.*)
            return 0
            ;;
    esac

    return 1
}

is_generated_metadata_file() {
    case "$1" in
        .baselines/template.properties)
            return 0
            ;;
    esac

    return 1
}

should_keep_managed_snapshot_file() {
    local rel_path="$1"

    is_template_support_file "$rel_path" && return 1
    is_generated_metadata_file "$rel_path" && return 1

    return 0
}

should_keep_auto_apply_file() {
    local rel_path="$1"

    should_keep_managed_snapshot_file "$rel_path"
}

prune_snapshot_by_policy() {
    local root_dir="$1"
    local policy="$2"
    local abs_path
    local rel_path
    local keep=false

    [ -d "$root_dir" ] || return 0

    while IFS= read -r -d '' abs_path; do
        rel_path="${abs_path#"$root_dir"/}"
        keep=false

        case "$policy" in
            managed)
                should_keep_managed_snapshot_file "$rel_path" && keep=true
                ;;
            auto-apply)
                should_keep_auto_apply_file "$rel_path" && keep=true
                ;;
            *)
                fail "Unknown snapshot policy: $policy"
                ;;
        esac

        [ "$keep" = true ] || rm -f -- "$abs_path"
    done < <(find "$root_dir" \( -type f -o -type l \) -print0)
}

copy_snapshot_tree() {
    local source_dir="$1"
    local target_dir="$2"

    mkdir -p -- "$target_dir"
    cp -pR -- "$source_dir/." "$target_dir"
}

collect_project_files() {
    local project_dir="$1"
    local git_root=""
    local abs_path

    git_root="$(git -C "$project_dir" rev-parse --show-toplevel 2>/dev/null || true)"

    if [ -n "$git_root" ] && [ "$(cd -- "$git_root" && pwd -P)" = "$project_dir" ]; then
        git -C "$project_dir" ls-files -z --cached --others --exclude-standard
        return
    fi

    find "$project_dir" -type f -print0 | while IFS= read -r -d '' abs_path; do
        printf '%s\0' "${abs_path#"$project_dir"/}"
    done
}

copy_project_snapshot() {
    local source_dir="$1"
    local target_dir="$2"
    local file_list="$WORK_ROOT/project-files.list"
    local rel_path
    local src
    local dst

    mkdir -p -- "$target_dir"
    collect_project_files "$source_dir" > "$file_list"

    while IFS= read -r -d '' rel_path; do
        [ -n "$rel_path" ] || continue
        should_snapshot_file "$rel_path" || continue

        src="$source_dir/$rel_path"
        dst="$target_dir/$rel_path"

        [ -f "$src" ] || continue
        mkdir -p -- "$(dirname -- "$dst")"
        cp -p -- "$src" "$dst"
    done < "$file_list"
}

normalize_snapshot_metadata() {
    local project_dir="$1"
    local metadata_file="$project_dir/.baselines/template.properties"

    [ -f "$metadata_file" ] || return 0

    perl -0pi -e 's/^BASELINES_GENERATED_AT=.*$/BASELINES_GENERATED_AT=2000-01-01T00:00:00Z/m' "$metadata_file"
}

run_diff() {
    local output_file="$1"
    shift
    local status

    set +e
    git diff --no-index "$@" > "$output_file"
    status=$?
    set -e

    [ "$status" -eq 0 ] || [ "$status" -eq 1 ] ||
        fail "git diff --no-index failed with status $status"
}

strip_patch_paths() {
    local input_file="$1"
    local output_file="$2"
    local old_prefix="$3"
    local new_prefix="$4"

    BASELINES_DIFF_OLD_PREFIX="$old_prefix" \
        BASELINES_DIFF_NEW_PREFIX="$new_prefix" \
        perl -pe '
        BEGIN {
            $old = quotemeta($ENV{"BASELINES_DIFF_OLD_PREFIX"});
            $new = quotemeta($ENV{"BASELINES_DIFF_NEW_PREFIX"});
        }
        s#^(diff --git a/)$old/#$1#;
        s#( b/)$new/#$1#;
        s#^(--- a/)$old/#$1#;
        s#^(\+\+\+ b/)$new/#$1#;
        s#^(rename from )$old/#$1#;
        s#^(rename to )$new/#$1#;
        s#^(copy from )$old/#$1#;
        s#^(copy to )$new/#$1#;
    ' "$input_file" > "$output_file"
}

strip_name_status_paths() {
    local input_file="$1"
    local output_file="$2"
    shift 2

    BASELINES_DIFF_PREFIXES="$(printf '%s\n' "$@")" \
        perl -pe '
        BEGIN {
            @prefixes = map { quotemeta($_) } grep { length($_) } split /\n/, $ENV{"BASELINES_DIFF_PREFIXES"};
        }
        for $prefix (@prefixes) {
            s#(^|[\t ])$prefix/#$1#g;
        }
    ' "$input_file" > "$output_file"
}

create_diffs() {
    local raw_patch="$REPORT_DIR/template-delta.raw.patch"
    local raw_status="$REPORT_DIR/template-delta.raw.name-status"
    local raw_apply_patch="$REPORT_DIR/template-delta.apply.raw.patch"
    local raw_apply_status="$REPORT_DIR/template-delta.apply.raw.name-status"
    local raw_target_patch="$REPORT_DIR/target-vs-new-template.raw.patch"
    local raw_target_status="$REPORT_DIR/target-vs-new-template.raw.name-status"
    local auto_old="$WORK_ROOT/auto-apply-old-template"
    local auto_new="$WORK_ROOT/auto-apply-new-template"

    prune_snapshot_by_policy "$WORK_ROOT/old-template" managed
    prune_snapshot_by_policy "$WORK_ROOT/new-template" managed
    prune_snapshot_by_policy "$WORK_ROOT/target-current" managed

    copy_snapshot_tree "$WORK_ROOT/old-template" "$auto_old"
    copy_snapshot_tree "$WORK_ROOT/new-template" "$auto_new"
    prune_snapshot_by_policy "$auto_old" auto-apply
    prune_snapshot_by_policy "$auto_new" auto-apply

    (
        cd -- "$WORK_ROOT"
        run_diff "$raw_patch" --binary -- old-template new-template
        run_diff "$raw_status" --name-status -- old-template new-template
        run_diff "$raw_apply_patch" --binary -- auto-apply-old-template auto-apply-new-template
        run_diff "$raw_apply_status" --name-status -- auto-apply-old-template auto-apply-new-template
        run_diff "$raw_target_patch" --binary -- target-current new-template
        run_diff "$raw_target_status" --name-status -- target-current new-template
    )

    strip_patch_paths "$raw_apply_patch" "$REPORT_DIR/template-delta.apply.patch" "auto-apply-old-template" "auto-apply-new-template"
    strip_name_status_paths "$raw_status" "$REPORT_DIR/template-delta.name-status" old-template new-template
    strip_name_status_paths "$raw_apply_status" "$REPORT_DIR/template-delta.apply.name-status" auto-apply-old-template auto-apply-new-template
    strip_name_status_paths "$raw_target_status" "$REPORT_DIR/target-vs-new-template.name-status" target-current new-template

    mv -- "$raw_patch" "$REPORT_DIR/template-delta.patch"
    mv -- "$raw_target_patch" "$REPORT_DIR/target-vs-new-template.patch"
    rm -f -- "$raw_status" "$raw_apply_patch" "$raw_apply_status" "$raw_target_status"
}

target_is_clean() {
    [ -z "$(git -C "$TARGET_DIR" status --porcelain 2>/dev/null || true)" ]
}

maybe_apply_patch() {
    local patch_file="$REPORT_DIR/template-delta.apply.patch"
    local apply_log="$REPORT_DIR/apply.log"
    local status

    [ "$APPLY_PATCH" = true ] || return 0

    if [ ! -s "$patch_file" ]; then
        APPLY_STATUS="no-op"
        printf 'No template changes to apply.\n' > "$apply_log"
        return 0
    fi

    if ! git -C "$TARGET_DIR" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
        APPLY_STATUS="blocked-not-git-repository"
        {
            printf 'Automatic apply was skipped because the target is not a Git repository.\n'
            printf 'Run git init in the target project, or rerun without --apply and apply apply.patch manually after reviewing it.\n'
            printf 'Target: %s\n' "$TARGET_DIR"
        } > "$apply_log"
        {
            printf 'Automatic apply skipped: target is not a Git repository.\n'
            printf 'Run git init in the target project, or rerun without --apply and apply apply.patch manually.\n'
        } >&2
        return 1
    fi

    if [ "$ALLOW_DIRTY" = false ] && ! target_is_clean; then
        APPLY_STATUS="blocked-dirty-worktree"
        {
            printf 'Automatic apply was skipped because the target worktree is not clean.\n'
            printf 'Commit or stash local changes, then rerun with --apply.\n'
            printf 'If you intentionally want to apply on top of the current changes, rerun with --apply --allow-dirty.\n'
        } > "$apply_log"
        {
            printf 'Automatic apply skipped: target worktree has uncommitted changes.\n'
            printf 'Commit or stash local changes, or rerun with --apply --allow-dirty to apply anyway.\n'
        } >&2
        return 1
    fi

    set +e
    git -C "$TARGET_DIR" apply --check "$patch_file" > "$apply_log" 2>&1
    status=$?
    set -e

    if [ "$status" -ne 0 ]; then
        APPLY_STATUS="blocked-patch-check"
        {
            printf '\nAutomatic apply was skipped because git apply --check failed.\n'
            printf 'No files were modified by --apply.\n'
            printf 'Review the errors above, then port the relevant hunks manually from apply.patch.\n'
            printf 'Common fixes: run from the project root, resolve conflicting local project changes, or apply selected hunks manually.\n'
        } >> "$apply_log"
        {
            printf 'Automatic apply skipped: git apply --check failed.\n'
            printf 'No files were modified. Review apply.log, then port relevant hunks from apply.patch manually.\n'
        } >&2
        return 1
    fi

    set +e
    git -C "$TARGET_DIR" apply --whitespace=fix "$patch_file" >> "$apply_log" 2>&1
    status=$?
    set -e

    if [ "$status" -ne 0 ]; then
        APPLY_STATUS="blocked-apply-failed"
        {
            printf '\nAutomatic apply failed after git apply --check succeeded.\n'
            printf 'Inspect the target with git status, review apply.log, and port remaining hunks from apply.patch manually.\n'
        } >> "$apply_log"
        {
            printf 'Automatic apply failed after the patch check passed.\n'
            printf 'Inspect git status and apply.log before retrying.\n'
        } >&2
        return 1
    fi

    APPLY_STATUS="applied"
}

write_status_list() {
    local status_file="$1"
    local empty_text="$2"
    local line

    if [ ! -s "$status_file" ]; then
        printf '%s\n' "$empty_text"
        return
    fi

    while IFS= read -r line || [ -n "$line" ]; do
        [ -n "$line" ] || continue
        printf -- '- `%s`\n' "$line"
    done < "$status_file"
}

write_report_readme() {
    local readme="$REPORT_DIR/README.md"

    {
        cat <<EOF
# Baselines Migration Report

Target: \`$TARGET_DIR\`

Project:
- Package: \`$APP_PACKAGE\`
- App identifier: \`$APP_NAME\`
- Display name: \`$APP_DISPLAY_NAME\`

Template:
- Previous revision: \`${FROM_REV:-from --from-template}\`
- New template: \`$TEMPLATE_DIR\`
- New revision: \`${TO_REV:-current working tree}\`

Automatic apply status: \`$APPLY_STATUS\`
Metadata status: \`$METADATA_STATUS\`

## Files To Use

- \`apply.patch\`: safe template changes. Present only when there is something to apply.
- \`template.properties.next\`: replacement metadata to commit after the migration is adopted.
- \`apply.log\`: apply failure details. Present only when \`--apply\` could not apply the safe patch.

## Safe Patch

EOF
        write_status_list "$REPORT_DIR/template-delta.apply.name-status" "No safe patch changes."

        cat <<EOF

## Recommended Process

1. Create a branch in the target project.
2. If \`apply.patch\` exists, run \`git apply --check "$OUTPUT_DIR/apply.patch"\`.
3. If the check passes, apply it with \`git apply --whitespace=fix "$OUTPUT_DIR/apply.patch"\`.
4. If the check fails, manually port the relevant hunks from \`apply.patch\`.
5. Replace \`.baselines/template.properties\` with \`template.properties.next\` once the migration is adopted.
6. Run the project validation suite.

EOF
        if [ "$DEBUG_REPORT" = true ]; then
            printf '\nDebug details are available under `debug/`.\n'
        else
            printf '\nRun again with `--debug-report` to keep raw diffs, status files, and generation logs.\n'
        fi
    } > "$readme"
}

compact_report_files() {
    local debug_dir="$REPORT_DIR/debug"
    local file

    if [ -s "$REPORT_DIR/template-delta.apply.patch" ]; then
        mv -- "$REPORT_DIR/template-delta.apply.patch" "$REPORT_DIR/apply.patch"
    else
        rm -f -- "$REPORT_DIR/template-delta.apply.patch"
    fi

    [ -s "$REPORT_DIR/apply.log" ] || rm -f -- "$REPORT_DIR/apply.log"

    rm -f -- "$REPORT_DIR/metadata.log"

    if [ "$DEBUG_REPORT" = true ]; then
        mkdir -p -- "$debug_dir"
        for file in "$REPORT_DIR"/generate-*.log "$REPORT_DIR"/template-delta.* "$REPORT_DIR"/target-vs-new-template.*; do
            [ -e "$file" ] || continue
            mv -- "$file" "$debug_dir/"
        done
    else
        rm -f -- "$REPORT_DIR"/generate-*.log "$REPORT_DIR"/template-delta.* "$REPORT_DIR"/target-vs-new-template.*
    fi
}

move_report_into_place() {
    mkdir -p -- "$(dirname -- "$OUTPUT_DIR")"
    mv -- "$REPORT_DIR" "$OUTPUT_DIR"
    REPORT_DIR=""
}

print_plan() {
    printf '=== Baselines migration helper ===\n'
    printf 'Target:        %s\n' "$TARGET_DIR"
    printf 'Template:      %s\n' "${TEMPLATE_DIR:-<not set>}"
    printf 'From revision: %s\n' "${FROM_REV:-<from-template>}"
    printf 'To revision:   %s\n' "${TO_REV:-<working tree>}"
    printf 'Package:       %s\n' "$APP_PACKAGE"
    printf 'App name:      %s\n' "$APP_NAME"
    printf 'Display name:  %s\n' "$APP_DISPLAY_NAME"
    printf 'Report:        %s\n' "$OUTPUT_DIR"
}

main() {
    local from_template
    local from_metadata_template
    local to_template
    local from_repo
    local from_revision
    local from_version
    local to_repo
    local to_revision
    local to_version
    local apply_failed=false

    require_commands
    parse_args "$@"
    resolve_paths
    load_metadata
    validate_project_values

    if [ "$INIT_METADATA" = true ]; then
        [ "$APPLY_PATCH" = false ] || fail "--init-metadata cannot be combined with --apply."
        write_metadata_file
        exit 0
    fi

    ensure_migration_inputs
    prepare_output_dir
    print_plan
    warn_if_from_template_was_dirty
    PROGRESS_TOTAL="$(progress_total_for_run)"
    [ "$NO_PROGRESS" = true ] || printf '\n' >&2

    progress "Preparing temporary workspace"
    prepare_work_root

    if [ -n "$FROM_TEMPLATE_DIR" ]; then
        progress "Using previous template checkout"
        from_template="$FROM_TEMPLATE_DIR"
        from_metadata_template="$FROM_TEMPLATE_DIR"
    else
        progress "Checking out previous template revision"
        from_template="$(prepare_template_checkout "$TEMPLATE_DIR" "$FROM_REV" "$WORK_ROOT/from-template-checkout" "previous")"
        from_metadata_template="$TEMPLATE_DIR"
    fi

    progress "Preparing new template revision"
    to_template="$(prepare_template_checkout "$TEMPLATE_DIR" "$TO_REV" "$WORK_ROOT/to-template-checkout" "new")"

    progress "Resolving template provenance"
    from_repo="$(template_git_value "$from_metadata_template" repo)"
    from_revision="$(template_revision_for "$from_metadata_template" "$FROM_REV")"
    from_version="$(template_version_for "$from_metadata_template" "$FROM_REV")"
    to_repo="$(template_git_value "$TEMPLATE_DIR" repo)"
    to_revision="$(template_revision_for "$TEMPLATE_DIR" "${TO_REV:-HEAD}")"
    to_version="$(template_version_for "$TEMPLATE_DIR" "${TO_REV:-HEAD}")"

    progress "Generating previous branded template snapshot"
    generate_project_from_template "$from_template" "$WORK_ROOT/old-template" "$REPORT_DIR/generate-old.log" "$from_repo" "$from_revision" "$from_version"
    progress "Generating new branded template snapshot"
    generate_project_from_template "$to_template" "$WORK_ROOT/new-template" "$REPORT_DIR/generate-new.log" "$to_repo" "$to_revision" "$to_version"
    progress "Snapshotting target project"
    copy_project_snapshot "$TARGET_DIR" "$WORK_ROOT/target-current"
    progress "Normalizing generated metadata"
    normalize_snapshot_metadata "$WORK_ROOT/target-current"
    normalize_snapshot_metadata "$WORK_ROOT/old-template"
    normalize_snapshot_metadata "$WORK_ROOT/new-template"
    progress "Creating migration diffs"
    create_diffs
    write_next_metadata_file "$to_repo" "$to_revision" "$to_version"

    if [ "$APPLY_PATCH" = true ]; then
        progress "Checking and applying template patch"
        METADATA_STATUS="not-updated"
        maybe_apply_patch || apply_failed=true
        if [ "$apply_failed" = false ]; then
            apply_next_metadata_file
        else
            METADATA_STATUS="not-updated-apply-failed"
        fi
    fi

    progress "Writing migration report"
    write_report_readme
    compact_report_files
    move_report_into_place

    printf '\nMigration report written to:\n  %s\n' "$OUTPUT_DIR"

    if [ "$APPLY_PATCH" = true ]; then
        printf 'Automatic apply status: %s\n' "$APPLY_STATUS"
        printf 'Metadata status: %s\n' "$METADATA_STATUS"
    fi

    [ "$apply_failed" = false ] || exit 1
}

main "$@"
