# Introducción
FitLanz es un podómetro propuesto por el centro I.E.S. Politécnico Hermenegildo Lanz. 
Los estudiantes registrados en este servicio, podrán controlar de manera diaria, tanto la distancia como el tiempo que emplean en ir y volver desde el centro educativo hasta sus hogares.


## Manual de instalación
- 	Tener Git instalado en el sistema
-	Clonar el repositorio dentro de una carpeta

    ``git clone https://github.com/Jose-Sabio/Fithlanz.git``

-	Entrar en la carpeta con el repositorio clonado

    ``cd fitlanz``
-	Moverse a las carpetas de backend y frontend e instalar dependencias 

```
cd bakend
npm i
node index

cd frontend
npm i npm 
run dev
```

## Manual de usuario
La aplicación empezará en la interfaz de ranking, donde los usuarios solo podrán ver un panel con el orden de todos los usuarios logueados en la aplicación y un botón en el header para poder loguearse.

Al pulsar el botón, el usuario será redirigido a una página donde Google se encargará de autenticar al usuario y devolverlo a la aplicación. 
En caso de ser un usuario anteriormente logueado, este será devuelto a la página de actividades, donde tendrá la información de todas sus actividades realizadas durante la semana y con la posibilidad de ver las realizadas durante el mes.

En caso de ser su primera conexión, será devuelto a la página de su perfil donde deberá conceder su autorización para realizar las peticiones de obtención de datos utilizando la Api de Fit. Este proceso, supone que deba volver a ser redirigido por Google, esta vez sacándole de la aplicación; suponiendo un tercer logueo, para poder volver a entrar en la misma. Tras todo esto, el usuario debería visitar la pantalla de comunidad para que sus datos sean finalmente guardados de forma satisfactoria.

El motivo de tantos logins en caso de primera conexión, es debido a que las credenciales devueltas por el provider de Google ofrecido por Firebase, proporciona información del usuario y un Access_token, necesarios para la recogida de datos. El problema, no facilita la obtención de un refresh_token con el que hacer futuras peticiones. Obligando a realizar el primer logueo para guardar al usuario en Firebase y el segundo para obtener su refresh.


## Manual de Google Cloud
El proyecto de FitLanz debe ir asociado a un proyecto de la Google Cloud, al ser esta la encargada de habilitar los distintos servicios, necesarios para el correcto funcionamiento de la aplicación.
En primer lugar, se requiere la creación de un nuevo proyecto donde se deberá habilitar API REST Fit y obtener los permisos de autorización de lectura de datos de actividades.

Para ello, selecciona los servicios de Api & Services y pulsa sobre ENABLE APIS AND SERVICES, busca Fitness API y habilítalo. Tras ello, selecciona OAuth consent screen, deberás configurar la app, proporcionando los datos requeridos. En la siguiente pantalla, agrega los permisos que nos interesa:

En un principio, buscamos obtener información demasiada sensible para ser compartida. Siendo necesaria, la verificación de Google o en su defecto introducir una tarjeta de crédito, para poder continuar con el desarrollo. La verificación de Google suele tardar una media de 30 días.

Aun en la pantalla de consentimiento, agrega al menos a un Test users para realizar comprobaciones, estos serán los únicos que podrán ser “suplantados” utilizando las credenciales del cliente. 

Guarda los cambios y selecciona Credentials, crea unas nuevas credenciales para OAuth client ID e introduce los dominios donde este alojado tu página. Es necesario que estos dominios tengan protocolo https para ser aceptado.


## Manual de despliegue

Aprovechando los servicios que ofrece el ecosistema de Google Cloud, el despliegue se ha realizado utilizando el servicio de Cloud Run, esto debido a que se les es asignado un dominio con certificado SSL a los contenedores hosteados por dicho servicio.

Proceso para desplegar en Cloud Run
- Instalar Google Cloud SDK Shell
- Tener Docker instalado
- Habilitar las APIs de Cloud Run, Container Registry y Artifact Registry
- Loguearse en la cuenta del proyecto 
``gcloud auth login``
- Configurar los Dockerfile en cada una de las partes del mismo (backend y frontend)
- Dirigete a la ruta donde se encuentre el Dockerfile y construye la imagen 
```
cd ../fitlanz/frontend
docker build .
```
- Eliminar la imagen de ser necesario

```
docker tag [nombre_imagen] [nombre_repositorio]/[nombre_proyecto]/[nombre_imagen]
```

- Pushea la imagen a Cloud Run

```
docker push [nombre_repositorio]/[nombre_proyecto]/[nombre_imagen]
```

Dirigete al servicio de Artifact Registry, selecciona tu repositorio y configura el contenedor (donde estará alojado, puertos, autorizaciones…). Una vez terminada la configuración y haya sido desplegado tu contenedor, se te habrá asignado un dominio dinámicamente donde accederás a tu aplicación.

- Realiza las mismas acciones con el backend

Una vez desplegados ambos contenedores, copia los dominios de ambos, regresa al código y cambia los fetch para que apunten a los nuevos dominios, en caso contrario no se comunicarán entre ellos.
Vuelve a crear imágenes para ambos y pushealo al mismo repositorio, evitando que se les asignen nuevos dominios.
Para desplegar la nueva imagen, introduce en la Shell de Google Cloud:

```
docker build .
docker tag [nombre_imagen] [nombre_repositorio]/[nombre_proyecto]/[nombre_imagen]
docker push [nombre_repositorio]/[nombre_proyecto]/[nombre_imagen]
gcloud run deploy frontend --image [nombre_repositorio]/[nombre_proyecto]/[nombre_imagen] --region [region_seleccionada]
```