Esta aplicacion no requiere de crear la base de dato aparte se puede simplemente correr el proyecto y
el mismo creara la base de datos.

Backend: descargar y correrlo:
El servidor esta corriendo en el puerto 8000
https://github.com/PR0C3S/certificate_backend
comando de ejecucion:
./gradlew bootRun

Frontend: descargar y frontend:
El servidor esta corriendo en el puerto 5173
https://github.com/PR0C3S/certificate_frontend
commando de ejecucion:
npm install -> npm run dev

Rutas frontend:
**PRIMERO SE DEBE CORRER EL BACKEND ANTES DE CONSUMIRLA**
http://localhost:5173/clientes
http://localhost:5173/certificados
Desde esta app se puede realizar todas las acciones de la api

Para acceder a swagger:
localhost:8000/documentation

Se utilizo postman para probar los endpoints, esta invitacion contiene data de prueba para la misma:
https://app.getpostman.com/join-team?invite_code=c9515fba1d23a0d8d3a6b95662fd1a62&target_code=4112fa92084c8599cb49cc79bfc7a49e
