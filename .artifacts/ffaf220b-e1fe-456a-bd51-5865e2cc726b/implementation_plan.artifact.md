# Implementation Plan - Stabilize AGP 9.1.0 and Built-in Kotlin Migration

The project is still experiencing the `prepareKotlinBuildScriptModel` error. This is common during the transition to **AGP 9.1.0** and **Built-in Kotlin** if there are leftover legacy DSL elements or version mismatches in secondary plugins like Hilt.

## User Review Required

> [!IMPORTANT]
> I am removing the deprecated `kotlinOptions` block and upgrading **Dagger Hilt** to version **2.52** to ensure full compatibility with Kotlin 2.2.10 (which is bundled with AGP 9.1.0).

> [!WARNING]
> I've detected a redundant `gradle` wrapper folder inside the `app` module. I have removed it to ensure the project uses the root Gradle 9.6.1 wrapper consistently.

## Proposed Changes

### Build Configuration

#### [MODIFY] [build.gradle.kts](file:///C:/Projects/ALARM/android/build.gradle.kts) (Top-level)
- Update `com.google.dagger.hilt.android` version to `2.52`.

#### [MODIFY] [build.gradle.kts](file:///C:/Projects/ALARM/android/app/build.gradle.kts)
- **DELETE** the `kotlinOptions` block. In AGP 9.0+, Kotlin compilation options are derived from `compileOptions` by default, and the legacy block is incompatible with the new DSL implementations.
- Update `com.google.dagger:hilt-android` and its compiler to `2.52`.

#### [DELETE] `android/app/gradle/`
- Remove the nested Gradle wrapper that was recently added, as it conflicts with the root wrapper.

## Verification Plan

### Manual Verification
1.  **Sync Project:** Trigger a Gradle sync in Android Studio.
2.  **Verify Model:** Ensure that the "Task 'prepareKotlinBuildScriptModel' not found" error is resolved and IDE features (syntax highlighting) are restored in build files.
3.  **Clean Build:** Run `./gradlew clean assembleDebug` (if Java is available) or use the IDE's "Rebuild Project" to verify the full compilation flow.
