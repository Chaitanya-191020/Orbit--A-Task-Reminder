# Fix Gradle Sync Error: 'prepareKotlinBuildScriptModel' not found

The error `Task 'prepareKotlinBuildScriptModel' not found in project ':app'` indicates that the Kotlin DSL is not being correctly initialized for the `:app` module. This is primarily caused by the missing `org.jetbrains.kotlin.android` plugin in the build configuration, which is required for Kotlin support in Android projects.

## Proposed Changes

### Build Configuration

#### [MODIFY] [build.gradle.kts (root)](file:///C:/Projects/ALARM/android/build.gradle.kts)
- Add the `org.jetbrains.kotlin.android` plugin declaration to the `plugins` block.
- Use version `2.2.10` to match the Compose plugin version.

#### [MODIFY] [build.gradle.kts (:app)](file:///C:/Projects/ALARM/android/app/build.gradle.kts)
- Apply the `org.jetbrains.kotlin.android` plugin.
- Add `kotlinOptions` to the `android` block to specify the JVM target.

## Verification Plan

### Manual Verification
- The user should trigger a Gradle sync in Android Studio (**File > Sync Project with Gradle Files**).
- Verify that the sync completes without the `prepareKotlinBuildScriptModel` error.
- Verify that Kotlin code in the `:app` module can be compiled.
