# Worklog - BuscaPet

Este archivo centraliza el historial de cambios, mejoras y flujo de trabajo del proyecto, unificando los registros de optimización y nuevas funcionalidades.

---

## 📋 Iteración: Corrección de Login con Google (Google Sign-In)

### 🐞 Correcciones (Bug Fixes)
- **Login Silencioso Fallido (Google Sign-In)**:
    - Se solucionó un problema crítico donde el inicio de sesión fallaba silenciosamente tras seleccionar la cuenta de Google, ocultando el indicador de carga sin realizar ninguna acción.
    - Se agregó manejo explícito de errores cuando `Task<GoogleSignInAccount>` no es exitoso en `SignInViewModel`.
    - Se agregó validación para casos donde `account.idToken` es `null` en `SignInUseCase`, devolviendo un error claro al usuario en lugar de colgar el proceso.
    - Se implementó el envío de mensajes de error de inicio de sesión a través del canal de eventos para que la UI los muestre en un `Toast` (ej. si falla por no tener el SHA-1 configurado o por cancelación del usuario).

### 📂 Archivos Clave Modificados
- `auth/presentation/SignInViewModel.kt`: Manejo de errores de inicio de sesión y propagación a la UI.
- `auth/domain/use_case/SignInUseCase.kt`: Prevención de cuelgues por `idToken` nulo y captura adecuada de excepciones (incluyendo `ApiException`).

---

## 📋 Iteración: Ficha Médica, UI y Optimizaciones

### 🚀 Nuevas Funcionalidades
- **Ficha Médica (Detalle de Mascota)**:
    - Se reemplazó la sección estática de "Edad" en `DetailReportScreen` por un botón de **Ficha Médica**.
    - Implementación de `MedicalRecordDialog` con foto circular, edad calculada, fecha de nacimiento formateada (dd-mm-yyyy), peso, tipo de animal y raza.
    - Acceso directo desde `DetailReportScreen`.
- **Edición de Mascotas**:
    - Reutilización de `AddPetScreen` para edición mediante `petId` opcional en la navegación.
    - Soporte para cambio de imagen en tiempo real tocando la foto en el formulario.
- **Tratamientos Médicos**:
    - Estado vacío con mensaje informativo: "No current treatment exists. Add a new treatment using the + button".
    - Implementación de **Swipe-to-Delete** con `SwipeToDismissBox` de Material 3.
    - Sensibilidad ajustada (50%) para prevenir borrados accidentales.
    - Feedback visual con `Snackbar` y animaciones de salida (`AnimatedVisibility`).

### 🎨 Mejoras de UI/UX
- **TopAppBar**:
    - Actualización de tipografía a `titleLarge` en `AppBarWithBack`.
    - Alineación del título al inicio (izquierda) en `CommonTopAppBar`.
    - Ajuste de tamaño de título a `titleMedium`.
- **Home Screen**: Integración del email del usuario en el estado y la barra superior para personalización.
- **CommonCardView**: 
    - Rediseño para que la imagen ocupe todo el alto izquierdo con bordes redondeados y sin padding.
    - Inclusión de información de contacto (email e icono) del dueño o reportante.
- **Pull-to-Refresh**: Retraso intencional de 1.5s en ViewModels (`LastReports`, `MyReports`, `MyPets`) para mejorar la visibilidad del indicador.

### ⚙️ Cambios Técnicos y Arquitectura
- **Capa de Datos (Data)**:
    - `PetsRepository`: Uso de `Flow` con `addSnapshotListener` de Firestore para actualizaciones reactivas en tiempo real.
    - `PetsDatabase`: Migración a la **Versión 14** para incluir la columna `contact_email` en la entidad local.
- **Capa de Dominio (Domain)**:
    - `Pet.kt`: Asegurada compatibilidad con `weight` (Double) y `animalType` (String).
    - `NavDestinations.kt`: Migración de `AddPet` a `data class` con parámetro `petId`.
    - `AddPetEvent.kt`: Nuevos eventos para peso y tipo de animal.
- **Capa de Presentación (UI)**:
    - `AddPetViewModel`: Lógica de carga/guardado inteligente y manejo de imágenes (Base64 vs URI).
    - `DetailReportViewModel`: Migración a recolección de `Flow` para mantener sincronización constante.
- **Configuración**:
    - Soporte para **Android 15+ (16 KB Page Size)** mediante la actualización de `tensorflow-lite` a `2.16.1`.

### 🐞 Correcciones (Bug Fixes)
- Corrección de parámetros `modifier` duplicados en dropdowns (`OutlinedTextField`).
- Solución a crash de serialización en navegación con parámetros opcionales (paréntesis faltantes).
- Ajuste en la persistencia de imágenes para evitar pérdidas durante la edición sin cambios de foto.
- Sincronización reactiva: Se eliminó el lag de actualización entre diálogos y Firestore.

---

## 📂 Archivos Clave Modificados
- `core/presentation/CommonTopAppBar.kt`, `CommonCardView.kt`
- `core/domain/model/Pet.kt`, `NavDestinations.kt`
- `core/data/local/PetsDataBase.kt`, `repository/PetsRepository.kt`
- `home/presentation/HomeState.kt`, `HomeViewModel.kt`, `HomeScreen.kt`
- `last_resports/presentation/LastReportsScreen.kt`, `LastReportsViewModel.kt`
- `my_reports/presentation/MyReportsScreen.kt`, `MyReportsViewModel.kt`
- `my_pets/presentation/MyPetsScreen.kt`, `MyPetsViewModel.kt`, `AddPetViewModel.kt`
- `medical_treatment/presentation/MedicalTreatmentScreen.kt`
- `report/presentation/ReportViewModel.kt`
- `gradle/libs.versions.toml`

---
*Este documento centraliza el estado actual y flujo de trabajo del proyecto.*