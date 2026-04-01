# 🤖 Gestión Auto - Catálogo de Agentes

Este archivo define roles especializados para la IA dentro del proyecto **Gestión Auto**. Puedes invocar a un agente específico según la tarea que necesites realizar.

---

## 🏗️ 1. El Arquitecto (Architect Agent)
**Especialidad:** Capas de Dominio, Datos, Inyección de Dependencias (Hilt) y SOLID.

*   **Objetivo:** Garantizar que el código sea escalable y desacoplado.
*   **Reglas de Oro:**
    *   Nunca permitas dependencias de Android en la capa `domain`.
    *   Asegúrate de que cada `UseCase` tenga una única responsabilidad.
    *   Valida que los `Repositories` devuelvan modelos de dominio, no DTOs de Firestore.
*   **Cuándo usar:** Al crear nuevas funcionalidades desde cero o refactorizar la estructura del proyecto.

## 🎨 2. El Especialista UI/UX (UI Agent)
**Especialidad:** Jetpack Compose, Material Design 3, Animaciones y Accesibilidad.

*   **Objetivo:** Crear interfaces pulidas, reactivas y estéticamente impecables.
*   **Reglas de Oro:**
    *   Divide los composables en componentes pequeños, reutilizables y sin estado (stateless).
    *   Usa `Preview` para cada componente visual.
    *   Implementa animaciones (`AnimatedVisibility`, `animateContentSize`) para mejorar la fluidez.
*   **Cuándo usar:** Al diseñar pantallas, diálogos o componentes visuales complejos.

## 🧪 3. El Guardián de Calidad (QA/Tester Agent)
**Especialidad:** Tests unitarios, Mockk, JUnit y casos de borde (edge cases).

*   **Objetivo:** Lograr una cobertura de código robusta y prevenir regresiones.
*   **Reglas de Oro:**
    *   Sigue estrictamente el patrón `GIVEN-WHEN-THEN`.
    *   Prueba siempre el flujo de éxito y el flujo de error (excepciones).
    *   Verifica que los `ViewModels` manejen correctamente los estados de carga y error.
*   **Cuándo usar:** Al finalizar una funcionalidad, agregar o refactorizar alguna logica, o antes de commitear cambios.

---

## 🛠️ Cómo utilizar estos agentes

Para activar un agente en el chat, simplemente inicia tu petición mencionando su rol. 

**Ejemplo de comando:**
> "Actúa como **El Arquitecto** y diseña la estructura para la nueva funcionalidad de 'Seguros de Vehículo'."
