#!/usr/bin/env python3
import os
import re
import shutil
import sys
import subprocess
from pathlib import Path
import fnmatch


def user_input(prompt: str) -> str:
    return input(prompt).strip()


def copy_baseline(baseline_dir: Path, new_project_dir: Path):
    """
    Copy only the non‑ignored files (per .gitignore) from *baseline_dir* to
    *new_project_dir*, but skip helper / meta files that should not ship with
    the generated project.
    """
    # ---- filenames / patterns we do NOT want in a new project --------------
    SKIP_EXACT = {
        "create_baselines.py",   # generator script
        "README.md", "README",   # read‑me’s
        ".LICENSE", "LICENSE",   # license stubs
    }
    SKIP_PATTERNS = ("README.*", "LICENSE.*")  # catch README.txt etc.

    if new_project_dir.exists():
        print(f"Error: The directory '{new_project_dir.name}' already exists.")
        sys.exit(1)

    new_project_dir.mkdir(parents=True, exist_ok=True)

    # --- gather list of files respecting .gitignore -------------------------
    original_cwd = os.getcwd()
    os.chdir(baseline_dir)
    try:
        result = subprocess.run(
            ["git", "ls-files", "--cached", "--others", "--exclude-standard"],
            capture_output=True, text=True, check=True
        )
    except subprocess.CalledProcessError as e:
        print(f"Error running git ls-files: {e}")
        sys.exit(1)
    finally:
        os.chdir(original_cwd)

    files_to_copy = result.stdout.splitlines()

    # --- copy while skipping unwanted files ---------------------------------
    for rel_path in files_to_copy:
        fname = os.path.basename(rel_path)

        # exact‑name exclusions
        if fname in SKIP_EXACT:
            continue
        # pattern exclusions
        if any(fnmatch.fnmatch(fname, pat) for pat in SKIP_PATTERNS):
            continue

        src = baseline_dir / rel_path
        dst = new_project_dir / rel_path
        dst.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(src, dst)


def replace_in_file(file_path: Path, old_str: str, new_str: str):
    try:
        text = file_path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        return  # binary / non‑utf8 – skip

    replaced_text = text.replace(old_str, new_str)
    if replaced_text != text:
        file_path.write_text(replaced_text, encoding="utf-8")


def replace_package_substring_in_file(
    file_path: Path, old_package_root: str, new_package_root: str
):
    try:
        text = file_path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        return

    replaced = text.replace(old_package_root + ".", new_package_root + ".")
    replaced = replaced.replace(old_package_root, new_package_root)

    if replaced != text:
        file_path.write_text(replaced, encoding="utf-8")


def rename_files_dirs(root: Path, old_name: str, new_name: str):
    for path in sorted(root.rglob("*"), reverse=True):
        if old_name in path.name:
            new_path = path.parent / path.name.replace(old_name, new_name)
            path.rename(new_path)


def fix_package_structure(
    project_dir: Path, old_package_root: str, new_package_root: str
):
    source_dirs = [
        project_dir / "app" / "android" / "src" / "main" / "kotlin",
        project_dir / "app" / "compose" / "src" / "androidMain" / "kotlin",
        project_dir / "app" / "compose" / "src" / "commonMain" / "kotlin",
        project_dir / "app" / "compose" / "src" / "iosMain" / "kotlin",
        project_dir / "domain" / "src" / "commonMain" / "kotlin",
        project_dir / "ui" / "design-system" / "src" / "commonMain" / "kotlin",
        project_dir / "ui" / "navigation" / "src" / "commonMain" / "kotlin",
        project_dir / "ui" / "home" / "src" / "commonMain" / "kotlin",
    ]

    old_pkg_dirs = old_package_root.split(".")
    new_pkg_dirs = new_package_root.split(".")

    for source_dir in source_dirs:
        if not source_dir.is_dir():
            print(f"Warning: '{source_dir}' not found — skipping.")
            continue

        old_package_path = source_dir.joinpath(*old_pkg_dirs)
        new_package_path = source_dir.joinpath(*new_pkg_dirs)

        if old_package_path.exists():
            new_package_path.parent.mkdir(parents=True, exist_ok=True)
            old_package_path.rename(new_package_path)
            prune_empty_dirs(old_package_path.parent, source_dir)
        else:
            print(
                f"Warning: old package path '{old_package_path}' not found — skipping."
            )


def prune_empty_dirs(start: Path, stop: Path):
    current = start.resolve()
    stop = stop.resolve()

    while True:
        if current == stop or current == current.root:
            break

        if is_effectively_empty_dir(current):
            try:
                remove_ignored_hidden_files(current)
                current.rmdir()
                current = current.parent
            except OSError as e:
                print(f"Could not remove {current}: {e}")
                break
        else:
            break


IGNORED_FILES = {".DS_Store", ".gitkeep"}


def is_effectively_empty_dir(folder: Path) -> bool:
    if not folder.is_dir():
        return False

    for item in folder.iterdir():
        if item.is_dir():
            return False
        if item.name not in IGNORED_FILES and not item.name.startswith("."):
            return False

    return True


def remove_ignored_hidden_files(folder: Path):
    for item in folder.iterdir():
        if item.is_file() and (
            item.name in IGNORED_FILES or item.name.startswith(".")
        ):
            try:
                item.unlink()
            except OSError as e:
                print(f"Error removing {item}: {e}")


def main():
    baseline_dir = Path(__file__).parent.resolve()
    current_dir = Path.cwd().resolve()

    print("=== Provide new project details ===")
    new_package_name = user_input("1) New package name (e.g. com.example): ")
    new_app_name = user_input("2) New app name (no spaces, e.g. MyNewApp): ")
    new_app_display_name = user_input("3) New app display name (e.g. My New App): ")
    new_org_name = user_input("4) New org name (e.g. myOrg): ")

    new_project_dir = current_dir / new_app_name
    print(f"Copying baseline to '{new_project_dir}' …")
    copy_baseline(baseline_dir, new_project_dir)

    print("Replacing package name in files …")
    for f in new_project_dir.rglob("*"):
        if f.is_file():
            replace_package_substring_in_file(
                f, "io.baselines.sample", new_package_name
            )

    print("Renaming files & dirs …")
    rename_files_dirs(new_project_dir, "BaselinesSample", new_app_name)

    print("Fixing identifiers inside files …")
    for f in new_project_dir.rglob("*"):
        if f.is_file():
            replace_in_file(f, "BaselinesSample", new_app_name)
            replace_in_file(f, "Baselines Sample", new_app_display_name)
            replace_in_file(f, "baselines-sample", new_org_name)

    print("Adjusting package structure …")
    fix_package_structure(new_project_dir, "io.baselines.sample", new_package_name)

    print("\n=== All done! ===")
    print(f"New project created at: {new_project_dir}")
    print("Please verify everything looks correct.")


if __name__ == "__main__":
    main()
