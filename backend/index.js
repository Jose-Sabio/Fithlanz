const express = require('express');
const { google } = require('googleapis');
const fetch = require('node-fetch');
const cors = require('cors');
const router = express.Router()
const cron = require('node-cron')
const { db } = require('./firebase/Firebase')
const { collection, getDocs, doc, updateDoc } = require('firebase/firestore');
const { getDia, obtenerFechaActual, getNewToken } = require('./utils/functions')
require('dotenv').config();
const axios = require('axios');
const bodyParser = require('body-parser');

const dominio = process.env.DOMINIO_URL || "http://localhost"

const app = express();

// Configurar las credenciales de OAuth 2.0 (modo de prueba)
const oauth2Client = new google.auth.OAuth2(
  process.env.GOOGLE_CLIENT_ID,
  process.env.GOOGLE_CLIENT_SECRET,
  process.env.AUTH_REDIRECT_URI
);

// Definir los alcances necesarios
const scopes = [
  'https://www.googleapis.com/auth/fitness.activity.read',
  'https://www.googleapis.com/auth/fitness.activity.write',
  'https://www.googleapis.com/auth/fitness.location.read',
];




/**
 * Tarea programada para actualizar los tokens de todos los usuarios de la Base de Datos, utilizaando su refresh_token.
 * Recoge a todos los usuarios del array de cada Centro y hace las peticiones a la consola de Google, con el refresh_token 
 * almacenado de cada uno.
 */
//  '20 23 * * 1-5' '*/10 * * * * *'
cron.schedule('20 23 * * 1-5', async () => {
    // Obtiene los usuarios de todas las colecciones en Firestore
    const centroSnapshot  = await getDocs(collection(db, "Centros"))

    centroSnapshot.forEach(ce => {
      const centro = ce.data()
      const usuarios = centro.Usuarios

      // Para todos los usuarios obtiene un nuevo token con las autorizaciones anteriormente dadas, con el fin de volver a realizar una petición a la api de Fit
      usuarios.forEach(async (user) => {
        try {

          const token = await getNewToken(user.refreshToken)

          if(!token) {
            user.token = ""
            user.refreshToken = ""
          }else {
            user.token = token.access_token
          }
          console.log("Token obtenido de forma correcta:", token.access_token)
          const centroRef = doc(collection(db, "Centros"), ce.id) 
          await updateDoc(centroRef, { Usuarios: usuarios });
        } catch (error) {
          user.token = ""
          user.refreshToken = ""
          const centroRef = doc(collection(db, "Centros"), ce.id) 
          await updateDoc(centroRef, { Usuarios: usuarios });
          console.log("El usuario ha revocado su autorización o el refresh ha caducado")
        }
        }
      );
    })
})




/**
 * Tarea programada para actualizar el registro de los días (semanal y mensual) de todos los usuarios de la Base de Datos.
 * Recoge a todos los usuarios del array de cada Centro y hace las peticiones a la api de Google Fit, con el token 
 * almacenado de cada uno.
 */
//'45 23 * * 1-5'
cron.schedule('45 23 * * 1-5', async () => {
  // Obtiene los usuarios de todas las colecciones en Firestore
  try {
    const centroSnapshot  = await getDocs(collection(db, "Centros"))

    centroSnapshot.forEach(ce => {
      const centro = ce.data()
      const usuarios = centro.Usuarios
      //Por cada usuario, obtiene el token de acceso, hace las peticiones para crear un objeto actividad del día y actualiza al usuario con los nuevos datos
      usuarios.forEach(async (user) => {

        try {
          const actividad = await getDia(user.token, user.turno)
          const fechaActual = obtenerFechaActual()
          const diaIndex = user.dias.findIndex(dia => dia.dia === fechaActual);
console.log(actividad)
          // const nuevoDia = {
          //   dia: fechaActual,
          //   actividad: actividad
          // };

          // if (diaIndex === -1) {
          //   // Si no existe el registro, añadir uno nuevo
          //   user.dias.push(nuevoDia);
          //   user.semana.push(nuevoDia)
    

          // } else {
          //   // Si existe el registro, sustituir el existente
          //   user.dias[diaIndex] = nuevoDia;
          //   user.semana[diaIndex] = nuevoDia;
          // }


          // const pasosTurno1 = nuevoDia.actividad.Turno1[0].tipo === "A pie" ? nuevoDia.actividad.Turno1[0].pasos : 0.4*(nuevoDia.actividad.Turno1[0].pasos);
          // const pasosTurno2 = nuevoDia.actividad.Turno2[0].tipo === "A pie" ? nuevoDia.actividad.Turno2[0].pasos : 0.4*(nuevoDia.actividad.Turno2[0].pasos);

          // const newAcumulado = user.acumulado + pasosTurno1 + pasosTurno2;
          // const newAcumuladoSemana = user.acumuladoSemana + pasosTurno1 + pasosTurno2;
    
          // user.acumulado = !isNaN(newAcumulado) ? newAcumulado : user.acumulado;
          // user.acumuladoSemana = !isNaN(newAcumuladoSemana) ? newAcumuladoSemana : user.acumuladoSemana;
          // const centroRef = doc(collection(db, "Centros"), ce.id) 
          // await updateDoc(centroRef, { Usuarios: usuarios });
        } catch(err) {
          console.log("Error al recorrer al user")
        }
        }
      );
    })
  } catch (error) {
    console.log("No existe el centro en cuestion")
  }
})


/**
 * Tarea programada para resetear el registro de las actividades realizadas durante la semana
 */
cron.schedule('59 23 * * 7', async () => {
    // Obtiene los usuarios de todas las colecciones en Firestore
    const centroSnapshot  = await getDocs(collection(db, "Centros"))

    centroSnapshot.forEach(ce => {
      const centro = ce.data()
      const usuarios = centro.Usuarios

      // Resetea a 0 la puntuación semanal de todos los usuarios
      usuarios.forEach(async (user) => {
        try {
          user.semana = []
          user.acumuladoSemana = 0
          const centroRef = doc(collection(db, "Centros"), ce.id) 
          await updateDoc(centroRef, { Usuarios: usuarios });

        } catch(err) {
          console.log("Error al resetear la semana")
        }
      });
    })
})


/**
 * Tarea programada para resetear el registro de las actividades realizadas durante el mes
 */
cron.schedule('0 0 1 * *', async () => {
  // Obtiene los usuarios de todas las colecciones en Firestore
  const centroSnapshot  = await getDocs(collection(db, "Centros"))

  centroSnapshot.forEach(ce => {
    const centro = ce.data()
    const usuarios = centro.Usuarios

    // Resetea a 0 la puntuación de todos los usuarios al principio de cada mes
    usuarios.forEach(async (user) => {
        user.dias = []
        user.acumulado = 0
        const centroRef = doc(collection(db, "Centros"), ce.id) 
        await updateDoc(centroRef, { Usuarios: usuarios });
      }
    );
  })
})








// Maneja las solicitudes POST en la ruta /exchangeAuthCode
app.post('/exchangeAuthCode', async (req, res) => {
  // Extrae el authCode del cuerpo de la solicitud
  const { authCode } = req.body;

  // Verifica si se proporcionó un authCode
  if (!authCode) {
      return res.status(400).send('Auth code is required');
  }

  try {
      // Realiza una solicitud POST a la API de Google OAuth2 para intercambiar el authCode por un token de acceso y un refresh token
      const response = await axios.post('https://oauth2.googleapis.com/token', {
          client_id: process.env.GOOGLE_CLIENT_ID,
          client_secret: process.env.GOOGLE_CLIENT_SECRET,
          code: authCode,
          grant_type: 'authorization_code',
          redirect_uri: process.env.AUTH_REDIRECT_URI
      });

      // Imprime la respuesta de la solicitud POST
      console.log(response.data);
      await getNewToken(response.data.refresh_token)
      // Devuelve tanto el token de acceso como el refresh token como respuesta al cliente
      const accessToken = response.data.access_token;
      const refreshToken = response.data.refresh_token;
      res.json({ accessToken, refreshToken });
  } catch (error) {
      // Maneja cualquier error que ocurra durante el intercambio de authCode por accessToken y refreshToken
      console.error('Error exchanging auth code for access token:', error);
      res.status(500).send('Internal Server Error');
  }
});




app.use(cors())
app.get('/auth/login', (req, res) => {
  const authUrl = oauth2Client.generateAuthUrl({
    access_type: 'offline',
    scope: scopes
  });
  res.redirect(authUrl);
});
// Ruta de redireccionamiento después de la autorización
app.get('/auth/callback', async (req, res) => {
  // const token = req.headers.authorization
  const code = req.query.code;
  try {

    const { tokens } = await oauth2Client.getToken(code);
    const encodedData = Buffer.from(JSON.stringify(tokens)).toString('base64');
    console.log(tokens)
    res.redirect(`https://fitfrontend-kxx4dhwdza-no.a.run.app/comunidad?data=${encodedData}`)

  } catch (error) {
    console.error('Error al obtener los tokens:');
    res.status(500).send('Error al obtener los tokens');
  }
});




app.get('/syncData', async (req, res) => {
      // Obtiene los usuarios de todas las colecciones en Firestore
      const centroSnapshot  = await getDocs(collection(db, "Centros"))

      centroSnapshot.forEach(ce => {
        const centro = ce.data()
        const usuarios = centro.Usuarios
  
        // Para todos los usuarios obtiene un nuevo token con las autorizaciones anteriormente dadas, con el fin de volver a realizar una petición a la api de Fit
        usuarios.forEach(async (user) => {
          try {
  
            const token = await getNewToken(user.refreshToken)
  
            if(!token) {
              user.token = ""
              user.refreshToken = ""
            }else {
              user.token = token.access_token
            }
  
            const centroRef = doc(collection(db, "Centros"), ce.id) 
            await updateDoc(centroRef, { Usuarios: usuarios });
          } catch (error) {
            user.token = ""
            user.refreshToken = ""
          }
          }
        );
      })


 // Obtiene los usuarios de todas las colecciones en Firestore
 try {
  const centroSnapshot  = await getDocs(collection(db, "Centros"))

  centroSnapshot.forEach(ce => {
    const centro = ce.data()
    const usuarios = centro.Usuarios
    //Por cada usuario, obtiene el token de acceso, hace las peticiones para crear un objeto actividad del día y actualiza al usuario con los nuevos datos
    usuarios.forEach(async (user) => {

      try {
        const actividad = await getDia(user.token, user.turno)
        const fechaActual = obtenerFechaActual()
        const diaIndex = user.dias.findIndex(dia => dia.dia === fechaActual);

        const nuevoDia = {
          dia: fechaActual,
          actividad: actividad
        };

        if (diaIndex === -1) {
          // Si no existe el registro, añadir uno nuevo
          user.dias.push(nuevoDia);
          user.semana.push(nuevoDia)
  

        } else {
          // Si existe el registro, sustituir el existente
          user.dias[diaIndex] = nuevoDia;
          user.semana[diaIndex] = nuevoDia;
        }


        const pasosTurno1 = nuevoDia.actividad.Turno1[0].tipo === "A pie" ? nuevoDia.actividad.Turno1[0].pasos : 0.4*(nuevoDia.actividad.Turno1[0].pasos);
        const pasosTurno2 = nuevoDia.actividad.Turno2[0].tipo === "A pie" ? nuevoDia.actividad.Turno2[0].pasos : 0.4*(nuevoDia.actividad.Turno2[0].pasos);

        const newAcumulado = user.acumulado + pasosTurno1 + pasosTurno2;
        const newAcumuladoSemana = user.acumuladoSemana + pasosTurno1 + pasosTurno2;
  
        user.acumulado = !isNaN(newAcumulado) ? newAcumulado : user.acumulado;
        user.acumuladoSemana = !isNaN(newAcumuladoSemana) ? newAcumuladoSemana : user.acumuladoSemana;
        const centroRef = doc(collection(db, "Centros"), ce.id) 
        await updateDoc(centroRef, { Usuarios: usuarios });
      } catch(err) {
        console.log("Error al recorrer al user")
      }
      }
    );
  })
} catch (error) {
  console.log("No existe el centro en cuestion")
}

})








// Iniciar el servidor
const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Servidor en funcionamiento en https://localhost:${PORT}`);
});