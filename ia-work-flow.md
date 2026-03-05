# Work Flow - Optimization & Features

## UI Enhancements
- **TopAppBar Typography Update**:
    - Modified `AppBarWithBack` in `core/presentation/CommonTopAppBar.kt` to use `titleLarge` (was `headlineSmall`).
    - Updated `CommonTopAppBar` to align the title to the **Start** (Left).
    - Reduced `CommonTopAppBar` title size to `titleMedium` (was `titleLarge`).
- **Home Screen**:
    - Integrated `userEmail` into `HomeState` and `HomeViewModel`.
    - Passed user email to the top bar in `HomeScreen`.

## Functional Improvements
- **Pull-to-Refresh**:
    - Implemented a 1.5s delay in `LastReportsViewModel`, `MyReportsViewModel`, and `MyPetsViewModel` to ensure the refresh loading indicator is visible to the user.

## Medical Treatment Feature
- **Empty State**:
    - Implemented a check in `MedicalTreatmentScreen`. If the list is empty, a centered text "No current treatment exists. Add a new treatment using the + button" is displayed.
- **Swipe-to-Delete**:
    - Implemented `SwipeToDeleteContainer` using Material 3 `SwipeToDismissBox`.
    - **Sensitivity**: Increased swipe threshold to 50% (`positionalThreshold = { it * 0.5f }`) to prevent accidental deletes.
    - **Visual Polish**: Wrapped the swipeable background in a `Box` with `clip(RoundedCornerShape(12.dp))` to match the `TreatmentCard` corners.
    - **Success Feedback**: Added a `Snackbar` that displays "The treatment was deleted successfully" upon deletion.
    - Integrated `AnimatedVisibility` for a smooth removal animation before the actual deletion in the ViewModel.

## Architecture & Configuration
- **Android 15+ 16 KB Page Size Support**:
    - Updated `tensorflow-lite` dependency from `2.14.0` to `2.16.1` to ensure `libtensorflowlite_jni.so` is aligned to 16 KB boundaries, resolving the APK compatibility warning.
    - Updated `gradle/libs.versions.toml`.

## File Changes
- `app/src/main/java/com/example/buscapet/core/presentation/CommonTopAppBar.kt`
- `app/src/main/java/com/example/buscapet/home/presentation/HomeState.kt`
- `app/src/main/java/com/example/buscapet/home/presentation/HomeViewModel.kt`
- `app/src/main/java/com/example/buscapet/home/presentation/HomeScreen.kt`
- `app/src/main/java/com/example/buscapet/last_resports/presentation/LastReportsViewModel.kt`
- `app/src/main/java/com/example/buscapet/my_reports/presentation/MyReportsViewModel.kt`
- `app/src/main/java/com/example/buscapet/my_pets/presentation/MyPetsViewModel.kt`
- `app/src/main/java/com/example/buscapet/medical_treatment/presentation/MedicalTreatmentScreen.kt`
- `gradle/libs.versions.toml`