const express = require('express');
const admin = require('firebase-admin');
const axios = require('axios');
const app = express();
const port = 3000;

// Inicializar Firebase Admin SDK
const serviceAccount = require('./config/serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

app.use(express.json());

// Rutas de la API
app.get('/api/data', async (req, res) => {
  try {
    const snapshot = await db.collection('Centros').get();
    const data = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    res.status(200).json(data);
  } catch (error) {
    res.status(500).send('Error al obtener datos');
  }
});

app.post('/api/users', async (req, res) => {
  const newUser = req.body;

  // Guardar y eliminar el nombre del centro del nuevo usuario
  const centroNombre = newUser.centro;
  delete newUser.centro;

  // Validar los datos de entrada
  if (!newUser || !newUser.email||!newUser.username || !newUser.id || !centroNombre) {
    return res.status(400).send('Datos de usuario inválidos');
  }

  try {
    const centrosRef = db.collection('Centros');
    
    // Realizar una consulta para encontrar el documento con el nombre del centro
    const querySnapshot = await centrosRef.where('Nombre', '==', centroNombre).get();
    
    if (querySnapshot.empty) {
      return res.status(404).send('Centro no encontrado');
    }
    // Iterar sobre los resultados de la consulta (debería haber solo uno)
    querySnapshot.forEach(async (doc) => {
      const centroRef = doc.ref;
      const centroData = doc.data();

      // Agregar el nuevo usuario al array 'Usuarios'
      if (!centroData.Usuarios) {
        centroData.Usuarios = [];
      }
      centroData.Usuarios.push(newUser);
      // Actualizar el documento del centro con el nuevo usuario
      await centroRef.update({ Usuarios: centroData.Usuarios });
      return res.status(200).json({ message: 'RefreshToken actualizado con éxito' });
    });
  } catch (error) {
    console.error('Error al crear usuario: ', error);
    res.status(500).send('Error al crear usuario');
  }
});


app.get('/api/logros', async (req, res) => {
  try {
    const logrosSnapshot = await db.collection('Logros').get();
    if (logrosSnapshot.empty) {
      return res.status(404).send('No se encontraron logros');
    }
    const logros = [];
    logrosSnapshot.forEach(doc => {
      logros.push({
        id: doc.id,
        ...doc.data()
      });
    });
    res.status(200).json(logros);
  } catch (error) {
    console.error('Error al obtener los logros:', error);
    res.status(500).send('Error al obtener los logros');
  }
});

app.get('/api/usuarios/con-mas-acumulado', async (req, res) => {
  try {
    // Obtener todos los usuarios
    const usuariosSnapshot = await db.collection('Centros').get();
    if (usuariosSnapshot.empty) {
      return res.status(404).send('No se encontraron usuarios');
    }
    // Inicializar un array para almacenar todos los usuarios con su acumulado
    let usuariosConAcumulado = [];
    // Iterar sobre cada usuario para obtener su acumulado, username, y profileImg
    usuariosSnapshot.forEach(doc => {
      const userData = doc.data();
      userData.Usuarios.forEach(usuario => {
        usuariosConAcumulado.push({ username: usuario.username, acumulado: usuario.acumulado, profileImg: usuario.profileImg });
      });
    });
    // Ordenar los usuarios por su acumulado de forma descendente
    usuariosConAcumulado.sort((a, b) => b.acumulado - a.acumulado);
    // Seleccionar los primeros 10 usuarios con el mayor acumulado
    const top10Usuarios = usuariosConAcumulado.slice(0, 10);
    res.status(200).json(top10Usuarios);
  } catch (error) {
    console.error('Error al obtener los 10 usuarios con más acumulado:', error);
    res.status(500).send('Error al obtener los 10 usuarios con más acumulado');
  }
});



// Maneja las solicitudes POST en la ruta /exchangeAuthCode
app.post('/exchangeAuthCode', async (req, res) => {
    // Extrae el authCode del cuerpo de la solicitud
    const { authCode } = req.body;
    // Verifica si se proporcionó un authCode
    if (!authCode) {
        return res.status(400).send('Se necesita el auth code para hacer la solicitud');
    }
    try {
        // Realiza una solicitud POST a la API de Google OAuth2 para intercambiar el authCode por un token de acceso y un refresh token
        const response = await axios.post('https://oauth2.googleapis.com/token', {
            client_id: '965860797920-09l4gfarabtpqfvmkhq8oc7g0m5l3e3i.apps.googleusercontent.com',
            client_secret: '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',
            code: authCode,
            grant_type: 'authorization_code',
            redirect_uri: 'http://localhost:3000/auth/callback'
        });
        // Imprime la respuesta de la solicitud POST
        console.log(response.data);
        //obtenerNuevoAccessToken(response.data.refresh_token)
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

app.get('/usuario/:id', async (req, res) => {
  const userId = req.params.id;
  console.log(`Buscando usuario con ID: ${userId}`);
  try {
    const centrosRef = db.collection('Centros');
    const snapshot = await centrosRef.get();

    if (snapshot.empty) {
      console.log('No se encontraron centros');
      return res.status(404).send('No se encontraron centros');
    }

    let userData = null;

    // Recorrer los documentos de la colección Centros
    for (const doc of snapshot.docs) {
      const centroData = doc.data();
      const usuarios = centroData.Usuarios || [];

      console.log(`Centro ID: ${doc.id}, Usuarios: ${JSON.stringify(usuarios)}`);

      // Buscar el usuario en la lista de usuarios del centro actual
      const user = usuarios.find(usuario => usuario.id.trim() === userId.trim());
      if (user) {
        userData = user;
        console.log(`Usuario encontrado: ${JSON.stringify(userData)}`);
        break; // Salir del bucle si se encuentra el usuario
      }
    }

    if (!userData) {
      console.log('Usuario no encontrado');
      return res.status(404).send('Usuario no encontrado');
    }

    if (!userData.refreshToken) {
      console.log('El refreshToken está vacío');
      return res.status(215).send('El refreshToken está vacío');
    }

    res.status(200).json(userData);

  } catch (error) {
    console.error('Error al obtener el usuario:', error);
    res.status(500).send('Error al obtener el usuario');
  }
});






app.post('/api/centros', async (req, res) => {
  const { nombre, logo } = req.body;
  // Validar los datos de entrada
  if (!nombre || !logo) {
    return res.status(400).send('Datos de centro inválidos');
  }
  try {
    // Obtener referencia a la colección 'Centros' en la base de datos
    const centrosRef = db.collection('Centros');
    // Crear un nuevo documento para el centro con el nombre como ID
    await centrosRef.doc(nombre).set({
      Nombre: nombre,
      Logo: logo,
    });
    return res.status(201).send('Centro creado con éxito');
  } catch (error) {
    console.error('Error al crear centro: ', error);
    res.status(500).send('Error al crear centro');
  }
});

app.post('/api/crearlogros', async (req, res) => {
  const { titulo, goal, descripcion, tipo,nivel } = req.body;
  // Validar los datos de entrada
  if (!titulo || !goal || !nivel  || !descripcion || !tipo) {
    return res.status(400).send('Datos de logro inválidos');
  }
  try {
    // Obtener referencia a la colección 'Logros' en la base de datos
    const logrosRef = db.collection('Logros');
    // Crear un nuevo documento para el logro con el título como ID
    await logrosRef.doc(titulo).set({
      Titulo: titulo,
      Goal: goal,
      Nivel:nivel,
      Descripcion: descripcion,
      Tipo: tipo
    });

    return res.status(201).send('Logro creado con éxito');
  } catch (error) {
    console.error('Error al crear logro: ', error);
    res.status(500).send('Error al crear logro');
  }
});

app.post('/api/eliminarlogro/', async (req, res) => {
  const {titulo} = req.body;
  try {
    const logroRef = db.collection('Logros').doc(titulo);
    const doc = await logroRef.get();
    if (!doc.exists) {
      return res.status(404).send('Logro no encontrado');
    }
    await logroRef.delete();
    return res.status(200).send('Logro eliminado con éxito');
  } catch (error) {
    console.error('Error al eliminar logro: ', error);
    return res.status(500).send('Error al eliminar logro');
  }
});


app.get('/api/leercentros', async (req, res) => {
  try {
    const snapshot = await db.collection('Centros').get();
    if (snapshot.empty) {
      return res.status(404).send('No se encontraron documentos.');
    }
    const nombres = [];
    snapshot.forEach(doc => {
      const data = doc.data();
      if (data.Nombre) {
        nombres.push(data.Nombre);
      }
    });
    res.status(200).json(nombres);
  } catch (error) {
    console.error('Error obteniendo documentos: ', error);
    res.status(500).send('Error al obtener documentos');
  }
});

app.post('/api/updateRefreshToken', async (req, res) => {
  const { id, refreshToken } = req.body;

  // Validar los datos de entrada
  if (!id || !refreshToken) {
    return res.status(400).send('Datos de entrada inválidos');
  }

  try {
    const centrosRef = db.collection('Centros');
    const querySnapshot = await centrosRef.get();

    let userUpdated = false;

    // Iterar sobre todos los documentos en la colección Centros
    for (const doc of querySnapshot.docs) {
      const centroRef = doc.ref;
      const centroData = doc.data();

      if (centroData.Usuarios) {
        // Buscar el usuario por id
        const userIndex = centroData.Usuarios.findIndex(user => user.id === id);

        if (userIndex !== -1) {
          // Usuario encontrado, actualizar el refreshToken
          centroData.Usuarios[userIndex].refreshToken = refreshToken;

          // Actualizar el documento del centro con la lista de usuarios modificada
          await centroRef.update({ Usuarios: centroData.Usuarios });
          userUpdated = true;
          break; // Salir del bucle una vez que se ha encontrado y actualizado el usuario
        }
      }
    }

    if (!userUpdated) {
      return res.status(404).send('Usuario no encontrado en ningún centro');
    }

    res.status(200).json({ message: 'RefreshToken actualizado con éxito' });
  } catch (error) {
    console.error('Error al actualizar refreshToken: ', error);
    res.status(500).send('Error al actualizar refreshToken');
  }
});

app.get('/api/getUserIdByEmail', async (req, res) => {
  const { email } = req.query; // Obtener el email de los parámetros de consulta

  try {
    if (!email || typeof email !== 'string' || email.trim() === '') {
      return res.status(400).send('Correo electrónico inválido');
    }

    const centrosRef = db.collection('Centros');
    const querySnapshot = await centrosRef.get();

    let userId = null;

    // Iterar sobre todos los documentos en la colección Centros
    for (const doc of querySnapshot.docs) {
      const centroData = doc.data();

      if (centroData.Usuarios && Array.isArray(centroData.Usuarios)) {
        // Buscar el usuario por correo electrónico
        const user = centroData.Usuarios.find(user => user.email === email);

        if (user) {
          // Usuario encontrado, obtener su ID
          userId = user.id;
          break; // Salir del bucle una vez que se ha encontrado el usuario
        }
      }
    }

    if (!userId) {
      return res.status(404).send('Usuario no encontrado en ningún centro');
    }

    res.status(200).json({ userId });
  } catch (error) {
    console.error('Error al obtener el ID del usuario por correo electrónico: ', error);
    res.status(500).send('Error al obtener el ID del usuario por correo electrónico');
  }
});




// Iniciar servidor
app.listen(port, () => {
  console.log(`API funcionando en http://localhost:${port}`);
});
