# MindSteps Android prototype

MindSteps from Mnemosyne is currently an early, local Android prototype. It contains two activities: matching picture pairs and continuing a simple picture pattern. Development and testing target Android phones and tablets. The initial phone target is a TCL 502 (T432J) running Android 13; the app can also be run in an Android emulator. Keep future iPhone support in mind when shaping activity rules and data formats, while Android remains the current build target.

## Open and run

If you use Android Studio, open the `android/` folder and use **Run** to install the debug app on a connected phone or emulator. The current machine setup uses the command-line SDK and JDK; Android Studio itself is not installed.

The development tools for this machine are installed under `~/Android/BuildTools` and `~/Android/Sdk`. From this `android/` folder, set them up for the current terminal session and build the locally installable debug APK:

```sh
export JAVA_HOME="$HOME/Android/BuildTools"
export ANDROID_SDK_ROOT="$HOME/Android/Sdk"
export PATH="$JAVA_HOME/bin:$ANDROID_SDK_ROOT/platform-tools:$PATH"
./gradlew assembleDebug
```

The APK is created at `android/app/build/outputs/apk/debug/app-debug.apk` (or `app/build/outputs/apk/debug/app-debug.apk` when your terminal is in this folder). On the phone, enable **USB debugging** in Developer options, connect it to the computer, and accept its “Allow USB debugging” prompt. Confirm it appears in `adb devices -l`, then install or update the app with:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

The app uses a debug signing key for local development. It is not a Play Store release.

## Local game history

Game sessions and task outcomes are stored locally in a Room database backed by SQLite. Each session records its game identifier, start/end time, duration, completion status, and counts of attempted, successful, and missed tasks. The app does not yet display this history or synchronize it. Records do not currently include a resident profile or personal name. Android app data is excluded from backup, so uninstalling the app or clearing its storage removes this history.

The Room schema snapshot is kept in `app/schemas/org.mnemosyne.app.data.MnemosyneDatabase/`. When changing entities or queries, preserve existing records with an explicit schema migration and update the exported schema; do not reset the database to make a change work.

## Environment notes

The project uses Kotlin, Jetpack Compose, Android Gradle Plugin 8.13, and JDK 17 or newer. The Gradle wrapper is checked into this folder so the same Gradle version is used across development machines. `local.properties` points Android Studio at this machine's SDK and is intentionally excluded from Git.
