# Implementation Plan - Fix "unexpected jvm signature V" Build Error

The project is failing to build with the error `java.lang.IllegalStateException: unexpected jvm signature V` during KSP processing for Room. This is a known compatibility issue between Room 2.6.1 and Kotlin 2.0+ / KSP 2.0+. It specifically affects `suspend` functions in DAOs that return `Unit`.

## Proposed Changes

### [Component: Data Layer]

#### [MODIFY] [Daos.kt](file:///C:/Projects/ALARM/android/app/src/main/kotlin/com/example/orbit/data/local/Daos.kt)
- Change the return type of `insertAlarm` to `Long`.
- Change the return type of `deleteAlarm` to `Int`.
- Change the return type of `insertTask` to `Long`.

Returning `Long` for insertions and `Int` for deletions is a standard Room practice and avoids the `Unit` (void) signature that is causing the KSP failure.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:kspDebugKotlin` to verify the KSP task completes successfully.
- Run `./gradlew assembleDebug` to ensure the full build passes.

### Manual Verification
- None required as this is a build-time fix.
