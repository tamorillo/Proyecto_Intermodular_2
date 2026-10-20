# 📚 CatchTask

<p align="center">
  <img src="./logo_readme.png" alt="Logo de CatchTask" width="180">
</p>

<p align="center">
  <strong>Organiza tus tareas, asignaturas y exámenes en un mismo lugar.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Estado-En%20desarrollo-orange?style=for-the-badge" alt="En desarrollo">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-Framework-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
</p>

---

## 📌 Descripción

**CatchTask** es una aplicación web desarrollada para ayudar a los estudiantes a organizar sus tareas, asignaturas y exámenes en un mismo lugar.

Su objetivo es facilitar la planificación académica mediante una interfaz sencilla que permita gestionar la información de cada usuario.

El proyecto se desarrolla de forma progresiva mediante sprints, incorporando nuevas funcionalidades y realizando pruebas durante cada etapa.

Actualmente, se han completado los dos primeros sprints, centrados en la gestión de usuarios y asignaturas.

## 🚀 Funcionalidades implementadas

### 🔐 Sprint 1 — Gestión de usuarios

- 📝 **Registro de usuarios:** permite crear una cuenta con nombre, correo electrónico y contraseña.
- 🔑 **Inicio de sesión:** permite acceder a la aplicación utilizando el correo electrónico y la contraseña.
- 🚪 **Cierre de sesión:** permite finalizar la sesión del usuario.
- 🗄️ **Almacenamiento en MySQL:** guarda la información de los usuarios en la base de datos.
- 🛡️ **Protección de contraseñas:** utiliza BCrypt para almacenar las contraseñas mediante hash.
- 🔒 **Control de acceso:** protege las páginas que requieren una sesión iniciada.

### 📚 Sprint 2 — Gestión de asignaturas

- ➕ **Añadir asignaturas:** permite guardar asignaturas indicando su nombre, descripción, curso y profesor.
- 🔎 **Consultar asignaturas:** muestra las asignaturas asociadas al usuario que ha iniciado sesión.
- ✏️ **Modificar asignaturas:** permite actualizar los datos de las asignaturas existentes.
- 🗑️ **Eliminar asignaturas:** permite eliminar las asignaturas que el usuario ya no necesita.
- 🔐 **Control de acceso por usuario:** incorpora comprobaciones para impedir que un usuario modifique o elimine asignaturas pertenecientes a otra cuenta.
- 🎨 **Mejoras de interfaz:** se han ajustado las tarjetas, los títulos y los botones para facilitar el uso de la aplicación.
- 💬 **Mensajes de confirmación:** los mensajes de éxito desaparecen automáticamente después de tres segundos.
- 🔄 **Mejora del cierre de sesión:** se ha añadido una comprobación para recargar páginas recuperadas de la caché del navegador.

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| **Java 21** | Lenguaje de programación. |
| **Spring Boot** | Desarrollo de la aplicación web. |
| **Spring MVC** | Gestión de las peticiones web mediante controladores. |
| **Spring Data JPA** | Acceso y gestión de los datos. |
| **Hibernate** | Mapeo de las entidades Java a la base de datos. |
| **Thymeleaf** | Generación de las páginas web. |
| **HTML y CSS** | Estructura y diseño de la interfaz. |
| **JavaScript** | Mejoras en el comportamiento de la interfaz. |
| **MySQL** | Almacenamiento de usuarios y asignaturas. |
| **Maven** | Gestión de dependencias y construcción del proyecto. |
| **BCrypt** | Hash de las contraseñas. |

## 📂 Estructura del proyecto

La estructura principal del proyecto sigue una organización por paquetes y carpetas:

```text
gestortareas/
├── src/
│   └── main/
│       ├── java/com/proyecto/gestortareas/
│       │   ├── config/
│       │   │   └── PasswordConfig.java
│       │   ├── controller/
│       │   │   ├── UsuarioController.java
│       │   │   └── AsignaturaController.java
│       │   ├── entity/
│       │   │   ├── Usuario.java
│       │   │   └── Asignatura.java
│       │   └── repository/
│       │       ├── UsuarioRepository.java
│       │       └── AsignaturaRepository.java
│       └── resources/
│           ├── static/
│           │   └── css/
│           │       └── estilo.css
│           ├── templates/
│           │   ├── registro.html
│           │   ├── login.html
│           │   ├── inicio.html
│           │   ├── asignaturas.html
│           │   └── formulario-asignatura.html
│           └── application.properties
├── pom.xml
├── logo_readme.png
└── README.md
```

*Nota: esta estructura muestra los archivos principales mencionados en la documentación. Puede variar según la organización final del proyecto.*

## ⚙️ Requisitos previos

Para ejecutar CatchTask en local necesitas:

- **Java 21** instalado.
- **Eclipse** u otro entorno compatible con Maven.
- **MySQL Workbench**, recomendado para gestionar la base de datos.
- El código fuente del proyecto descargado desde GitHub.

## ▶️ Cómo ejecutar la aplicación

### 1. Descargar el proyecto

Clona el repositorio de GitHub o descárgalo en tu ordenador.

```bash
git clone URL_DEL_REPOSITORIO
```

Sustituye `URL_DEL_REPOSITORIO` por la dirección real de tu repositorio.

### 2. Preparar la base de datos

1. Abre MySQL Workbench.
2. Comprueba que existe la base de datos `gestortareas`.
3. Si no existe, créala antes de ejecutar la aplicación.

```sql
CREATE DATABASE gestortareas;
```

Comprueba también que la configuración de conexión de la aplicación coincide con los datos de tu instalación de MySQL.

### 3. Configurar la conexión

Abre el archivo `src/main/resources/application.properties` y revisa la configuración de la conexión a MySQL.

La contraseña debe configurarse mediante la variable de entorno `DB_PASSWORD` si así está definida en el archivo de configuración.

**Importante:** no publiques contraseñas ni credenciales personales en el repositorio.

### 4. Abrir y ejecutar el proyecto

1. Abre el proyecto en Eclipse.
2. Espera a que Maven descargue las dependencias.
3. Localiza la clase principal de Spring Boot.
4. Ejecuta esa clase como una aplicación Java.

### 5. Acceder a CatchTask

Cuando la aplicación se haya iniciado correctamente, abre el navegador y accede a:

**http://localhost:8080/registro**

Desde la página de registro podrás crear una cuenta y, posteriormente, iniciar sesión para acceder a las funcionalidades disponibles.

> CatchTask se ejecuta actualmente en un entorno local. Para acceder desde otro equipo, será necesario configurar un despliegue o un entorno de ejecución accesible.

## 🗄️ Base de datos

CatchTask utiliza MySQL para almacenar la información de los usuarios y las asignaturas.

### Tabla `usuario`

Almacena los datos necesarios para identificar las cuentas registradas.

| Campo | Descripción |
|---|---|
| `id` | Identificador único del usuario. |
| `nombre` | Nombre del usuario. |
| `email` | Correo electrónico. |
| `password` | Hash de la contraseña. |

### Tabla `asignatura`

Almacena la información de las asignaturas creadas por los usuarios.

| Campo | Descripción |
|---|---|
| `id` | Identificador único de la asignatura. |
| `nombre` | Nombre de la asignatura. |
| `descripcion` | Descripción de la asignatura. |
| `curso` | Curso al que pertenece. |
| `profesor` | Profesor de la asignatura. |
| `usuario_id` | Referencia al usuario que ha creado la asignatura. |

La relación entre usuarios y asignaturas permite asociar cada asignatura a su creador y filtrar la información que se muestra a cada usuario.

*Los nombres y tipos de las columnas deben coincidir con la configuración real de las entidades y de la base de datos.*

## 📋 Gestión del proyecto

El desarrollo de CatchTask se organiza mediante sprints y un tablero de Trello, donde se planifican las tareas, se controla su estado y se realiza el seguimiento del proyecto.

🔗 [Acceder al tablero de Trello](https://trello.com/invite/b/6ac6a69fde04ede9b08480c4/ATTI4c76a0b95e121d30f44660756e0d0a01EDB73792/catchtask-proyecto-intermodular-ii)

El código fuente y la evolución del proyecto se gestionan mediante Git y GitHub.

## 📅 Próximas mejoras

En los siguientes sprints se continuará ampliando CatchTask con nuevas funcionalidades académicas, entre ellas:

- 📋 **Gestión de tareas:** añadir, modificar y eliminar tareas.
- ✅ **Seguimiento de tareas:** marcar tareas como terminadas.
- 🗓️ **Gestión de exámenes:** incorporar funcionalidades para organizar los próximos exámenes.
- 🎨 **Mejoras de interfaz:** seguir perfeccionando la presentación y la facilidad de uso.

Estas funcionalidades se incorporarán progresivamente a medida que avance el desarrollo.

## 👨‍💻 Proyecto académico

CatchTask es una aplicación desarrollada como parte del módulo **Proyecto Intermodular II** del ciclo formativo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

<p align="center">
  <strong>CatchTask — Organiza tus tareas, alcanza tus objetivos.</strong>
</p>