# GraalVM Native Image Demos

This repository demonstrates building and running native images with GraalVM for two sample apps: `oracle.App` and `serialization.App`.

## Quickstart — Local (GraalVM installed)

1. Verify GraalVM is on your PATH and `native-image` is available:

   ```bash
   java -version
   native-image --version
   ```

2. Build the project and shaded jar:

   ```bash
   mvn -DskipTests package
   ```

3. Build native images (uses the `native` profile):

   ```bash
   mvn -Pnative -DskipTests package
   ```

   This produces:
   - `target/oracle-app`
   - `target/serialization-app`

4. Run the native binaries to verify:

   ```bash
   ./target/oracle-app
   ./target/serialization-app .
   ```

   `serialization.App` will output `file-stats.json` in the project root.

## Quickstart — Docker / CI

The project includes a GitHub Actions workflow (`.github/workflows/native-image.yml`) that:
- sets up GraalVM 21
- installs `native-image` via `gu`
- runs `mvn -Pnative -DskipTests package`
- verifies the produced binaries and uploads them as artifacts

To run CI locally, push your branch to GitHub and open a PR or run the workflow via the Actions tab.

## Notes & Troubleshooting

- `serialization.App` requires Jackson to serialize `serialization.FileCount` — a reflection config is provided in `src/main/resources/META-INF/native-image/reflect-config.json` so it works in native images.
- If you need smaller or faster images, consider enabling PGO and additional native-image flags. See the `native` profile in `pom.xml` for current flags (`--no-fallback`, `-H:+ReportExceptionStackTraces`).
- The CI workflow targets GraalVM Java 21; adjust `.github/workflows/native-image.yml` if you require a different version.

## Commands summary

- Build JAR: `mvn -DskipTests package`
- Build native images locally: `mvn -Pnative -DskipTests package`
- Run native images: `./target/oracle-app`, `./target/serialization-app .`

---

If you want, I can also add a release workflow to publish built native images as GitHub release artifacts. 👍