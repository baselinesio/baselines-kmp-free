## 🚀 Quick Start

```
> cd ~/projects && git clone baselines-kmp
> ./baselines-kmp/baselines_create.sh
> open -a "Android Studio" ./MyApp
> open ./MyApp/app/ios/MyApp/MyApp.xcodeproj
```

Non-interactive generation:

```sh
./baselines-kmp/baselines_create.sh \
  --package com.example.myapp \
  --name MyApp \
  --display-name "My App" \
  --output ./MyApp
```

## 🔄 Template Updates

Generated projects include `.baselines/template.properties`. Use it with the
migration helper when adopting a newer Baselines release:

```sh
../baselines-kmp/baselines_migrate.sh \
  --target . \
  --template ../baselines-kmp \
  --to-rev v1.4.0
```

The migrator writes a report under `.baselines/migrations/<timestamp>` by
default. The report separates changes into:

- `apply.patch`: safer changes that can be checked with
  `git apply --check` or applied with `--apply`.
- `template.properties.next`: the metadata file to commit after the migration
  is adopted.

Template helper scripts and generated metadata are intentionally excluded from
the patch files. When `--apply` succeeds, the migrator updates
`.baselines/template.properties` automatically.
Without `--apply`, `template.properties.next` is only a report artifact.
Use `--debug-report` to keep raw diffs, status files, and generation logs under
the report's `debug/` directory.

See [Adopt Template Update](https://baselines.io/docs/project-generation/2.%20Adopt%20Template%20Updates) for the
full migration process.

## 💬 Discussions

For Q&A, Announcements and Polls, please visit our GitHub Discussions page:

➡️  [github.com/baselinesio/baselines-kmp-free/discussions](https://github.com/baselinesio/baselines-kmp-free/discussions)

## 📖 Documentation

For detailed guides, overviews, samples, visit docs:

➡️  [baselines.io/docs](https://baselines.io/docs/Overview)

---

Licensed under the baselines.io Free Tier License. See the [LICENSE](LICENSE) file for details.
