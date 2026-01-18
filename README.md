# BuscaPet 🐾

> **Conectando a la comunidad para reunir mascotas con sus familias.**

## 📖 Acerca del Proyecto

**BuscaPet** no es solo una aplicación; es una iniciativa comunitaria nacida de la necesidad de hacer más eficiente y humana la búsqueda de mascotas perdidas. Sabemos que cada minuto cuenta cuando un miembro de la familia se extravía, y creemos firmemente que la colaboración entre vecinos y la tecnología pueden marcar la diferencia.

Nuestro objetivo es crear una red solidaria donde los usuarios puedan reportar avistamientos, registrar a sus mascotas y colaborar en tiempo real para lograr reencuentros felices. Esta herramienta está diseñada para ser rápida, intuitiva y accesible para todos.

## ✨ Características Principales

*   **Reporte de Mascotas Perdidas:** Crea reportes detallados con fotografías, descripción y ubicación exacta del último avistamiento.
*   **Mapa Interactivo:** Visualiza reportes cercanos en un mapa integrado (Google Maps) para identificar zonas de búsqueda.
*   **Gestión de Mascotas:** Registra el perfil de tus mascotas para tener su información lista en caso de emergencia.
*   **Autenticación Segura:** Inicio de sesión integrado (Firebase Auth/Google Sign-In).
*   **Feed de Reportes:** Listado actualizado de los últimos reportes generados por la comunidad.

## 🛠️ Stack Tecnológico

Este proyecto ha sido desarrollado siguiendo los más altos estándares de la industria moderna de Android, priorizando la escalabilidad, el mantenimiento y el rendimiento.

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
*   **Arquitectura:** Clean Architecture + MVVM (Model-View-ViewModel)
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetbrains/compose) (Material Design 3)
*   **Inyección de Dependencias:** [Hilt](https://dagger.dev/hilt/)
*   **Navegación:** Navigation Compose
*   **Servicios en la Nube (Firebase):**
    *   Firebase Authentication (Gestión de usuarios)
    *   Cloud Firestore (Base de datos NoSQL en tiempo real)
    *   Firebase Storage (Almacenamiento de imágenes)
    *   Firebase Analytics
*   **Persistencia Local:** [Room Database](https://developer.android.com/training/data-storage/room)
*   **Mapas:** Google Maps SDK for Android & Maps Compose
*   **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/)
*   **Concurrencia:** Coroutines & Flow

## 📂 Estructura del Proyecto

El código está organizado siguiendo los principios de **Clean Architecture**, dividiendo la aplicación en capas claras:

*   **Presentation:** Contiene la UI (Composables), ViewModels y estados.
*   **Domain:** Contiene la lógica de negocio pura, casos de uso (UseCases) y modelos del dominio. Independiente del framework.
*   **Data:** Implementación de repositorios, fuentes de datos (DataSources) locales (Room) y remotas (Firebase), y mappers.

## 🚀 Configuración e Instalación

Para ejecutar este proyecto localmente, necesitarás Android Studio y configurar los servicios de Firebase.

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/BuscaPet.git
    ```
2.  **Configuración de Firebase:**
    *   Crea un proyecto en la consola de Firebase.
    *   Descarga el archivo `google-services.json`.
    *   Coloca el archivo en el directorio `app/` del proyecto.
3.  **API Key de Google Maps:**
    *   Asegúrate de tener configurada tu API Key de Google Maps en el `local.properties` o `AndroidManifest.xml` según corresponda para visualizar los mapas correctamente.
4.  **Compilar y Ejecutar:**
    *   Sincroniza el proyecto con Gradle y ejecuta la aplicación en un emulador o dispositivo físico.

## 🤝 Contribución

¡Este es un proyecto hecho para la comunidad y por la comunidad! Cualquier contribución es bienvenida, ya sea reportando errores, sugiriendo nuevas funcionalidades o mejorando el código.

1.  Haz un Fork del proyecto.
2.  Crea tu rama de funcionalidad (`git checkout -b feature/AmazingFeature`).
3.  Haz Commit de tus cambios (`git commit -m 'Add some AmazingFeature'`).
4.  Haz Push a la rama (`git push origin feature/AmazingFeature`).
5.  Abre un Pull Request.

---
*Desarrollado con ❤️ para ayudar a nuestras mascotas a volver a casa.*
