# Implementation Plan - Fix Black Screen Issue

The user reports a black screen after the latest changes. Logcat indicates that the app is running and `HomeScreen` is composed, but the visual output is pitch black (except for the status bar). This typically points to a UI hierarchy issue, a transparency conflict between nested Scaffolds, or a theme-level color misconfiguration.

## User Review Required

> [!IMPORTANT]
> I will be simplifying the UI hierarchy by consolidating the `Scaffold` usage and ensuring a solid background color is applied at the root of the navigation graph.

## Proposed Changes

### UI Layer

#### [MODIFY] [MainActivity.kt](file:///C:/Projects/ALARM/android/app/src/main/kotlin/com/example/orbit/ui/MainActivity.kt)
- Ensure `Surface` uses `Color.White` explicitly as a fallback to verify if the theme is the issue.

#### [MODIFY] [OrbitNavGraph.kt](file:///C:/Projects/ALARM/android/app/src/main/kotlin/com/example/orbit/ui/navigation/OrbitNavGraph.kt)
- Set `containerColor = Color.White` (or `OrbitBackground`) on the root `Scaffold`.
- Add a solid background to the `NavHost` or its parent to ensure visibility.

#### [MODIFY] [HomeScreen.kt](file:///C:/Projects/ALARM/android/app/src/main/kotlin/com/example/orbit/ui/screens/home/HomeScreen.kt)
- Remove `OrbitGradientBackground` temporarily to see if it's the cause of the black screen.
- Ensure the `Scaffold` has a solid `containerColor`.

### Theme Layer

#### [MODIFY] [Theme.kt](file:///C:/Projects/ALARM/android/app/src/main/kotlin/com/example/orbit/ui/theme/Theme.kt)
- Verify `Typography()` usage and ensure `onBackground` and `background` are contrasting correctly.

## Verification Plan

### Automated Tests
- Build and deploy the app to the emulator.
- Take a screenshot to verify the "black screen" is gone.

### Manual Verification
- Check if the "Alarm" header and list items are visible.
