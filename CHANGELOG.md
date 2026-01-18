## Versión 1.0

### Agregado

- Implementación de nuevos casos de uso para gestión de sesión (`LogoutUseCase`).
- Agregados nuevos componentes de UI reutilizables como `CommonLoadingOverlay` y `MapScreen`.
- Inclusión de nuevos módulos de inyección de dependencias (`AuthModule`).
- Soporte para nuevos tipos de datos en base de datos local mediante `Converter`.
- Pruebas unitarias iniciales para diversos módulos.

### Refactorizado

- Actualización general de dependencias y configuración de versiones en `libs.versions.toml`.
- Optimización de la lógica de negocio en ViewModels (`AddPetViewModel`, `ReportViewModel`, `DetailReportViewModel`, etc.).
- Mejoras en la implementación de repositorios y acceso a datos locales (`PetsRepositoryImpl`, `PetDao`).
- Reestructuración y limpieza de la navegación de la aplicación (`NavDestinations`, `RootNavGraph`).
- Refactorización de componentes de UI y pantallas principales para mayor estabilidad y rendimiento.
