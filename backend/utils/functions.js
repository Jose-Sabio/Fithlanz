require('dotenv').config();

const dataTypes = {
  pasos: "com.google.step_count.delta", // Cantidad de pasos
  actividad: "com.google.activity.segment", // Tipo de actividad, tiempo empleado y seciones realizadas
  tiempo: "com.google.active_minutes", // Tiempo de actividad en minutos
  distancia: "com.google.distance.delta", // Distancia recorrida
}


// Obtener los datos desde las 12 am hasta las 23:59 pm (Cambiar por los tiempos de actividad pedidos)
const startTime = new Date();
startTime.setHours(0, 0, 0, 0);
const endTime = new Date();
endTime.setHours(23, 59, 59, 999); 

// Periodos de actividad donde se optienen valores válidos para la aplicación
const horas = {
  mañana1: ["7:30:00", "8:40:00"],
  mañana2: ["14:45:00", "15:30:00"],
  tarde1: ["15:30:00", "16:20:00"],
  tarde2: ["22:00:00", "22:40:00"]
}

const horasFecha = {};

// Convertir las horas de cada intervalo a objetos Date
Object.keys(horas).forEach(intervalo => {
  const inicioHora = new Date();
  const inicioTiempo = horas[intervalo][0].split(':');
  inicioHora.setHours(...inicioTiempo);

  const finHora = new Date();
  const finTiempo = horas[intervalo][1].split(':');
  finHora.setHours(...finTiempo);

  // Obtener el valor numérico en era UNIX de las fechas utilizando getTime()
  horasFecha[intervalo] = [inicioHora.getTime(), finHora.getTime()];
});


// Función que obtiene los datos de la primera actividad de los usuarios
async function dataPrimeraActividad(token, dataType, turno) {
  try {

    const intervalo = turno === "M" ? "mañana1" : "tarde1";
    const startTimeMillis = horasFecha[intervalo][0];
    const endTimeMillis = horasFecha[intervalo][1];

    const response = await fetch("https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate", {
      cache: 'no-cache',
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        "aggregateBy": [{
          "dataTypeName": dataType,
        }],
        "bucketByTime": { "durationMillis": 86400000 },
        "startTimeMillis": startTimeMillis.toString(), // '1715724000000' || startTimeMillis.toString()
        "endTimeMillis": endTimeMillis.toString(), // '1715810399999' || endTimeMillis.toString()
      })
    });

    if (!response.ok) {
      throw new Error("Error al traer datos");
    }

    const result = await response.json();

    if (result.data) {
      return result.data;
    } else {
      return result;
    }
  } catch (err) {
    console.log("El user no tiene los tokens actualizados");
  }
}

// Función que obtiene los datos de la segunda actividad de los usuarios
async function dataSegundaActividad(token, dataType, turno) {
  try {

    const intervalo = turno === "M" ? "mañana2" : "tarde2";
    const startTimeMillis = horasFecha[intervalo][0];
    const endTimeMillis = horasFecha[intervalo][1];

    const response = await fetch("https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate", {
      cache: 'no-cache',
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        "aggregateBy": [{
          "dataTypeName": dataType,
        }],
        "bucketByTime": { "durationMillis": 86400000 },
        "startTimeMillis": startTimeMillis.toString(),
        "endTimeMillis": endTimeMillis.toString(),
      })
    });

    if (!response.ok) {
      throw new Error("Error al traer datos");
    }

    const result = await response.json();

    if (result.data) {
      return result.data;
    } else {
      return result;
    }
  } catch (err) {
    console.log("El user no tiene los tokens actualizados");
  }
}




// Deprecado
async function getUsers() {
  const users = []
  try {
    const docRef = await getDocs(collection(db, "Usuarios"));
    docRef.docs.map((doc) => {
      users.push({ id: doc.id, ...doc.data() });
    });
    return users
  } catch (error) {
    console.log(error)
  }
}


// Función para obtener nuevos tokens válidos para realizar las peticiones, utilizando un refreshToken
async function getNewToken(refreshToken) {
  try {
    const response = await fetch("https://oauth2.googleapis.com/token", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        client_id: process.env.GOOGLE_CLIENT_ID,
        client_secret: process.env.GOOGLE_CLIENT_SECRET,
        refresh_token: refreshToken,
        grant_type: 'refresh_token',
      }),
    });

    console.log(response)
    const data = response.json()
    return data
  } catch (error) {
    console.log(error)
  }
}


// Función que obtiene todas las actividades y conviertes los datos obtenidos en un objeto actividad
async function getDia(token, turno) {


  const turno1 = await dataPrimeraActividad(token, dataTypes.actividad, turno); // user.turno "Mañana" || "Tarde"
  const turno2 = await dataSegundaActividad(token, dataTypes.actividad, turno);
  const pasos1 = await dataPrimeraActividad(token, dataTypes.pasos, turno);
  const pasos2 = await dataSegundaActividad(token, dataTypes.pasos, turno);
  const distancia1 = await dataPrimeraActividad(token, dataTypes.distancia, turno);
  const distancia2 = await dataSegundaActividad(token, dataTypes.distancia, turno);
  const actividades1 = [];
  const actividades2 = []


// Transformación de los datos de la primera actividad
if (turno1?.bucket?.[0]?.dataset?.[0]?.point) {
  turno1.bucket[0].dataset[0].point.forEach((actividad, index) => {
    const tipoActividad = actividad.value[0].intVal;
    if (tipoActividad === 7 || tipoActividad === 1) {
      const actividadObj = {
        tipo: tipoActividad === 7 ? "A pie" : "Bicicleta",
        tiempo: (actividad.value[1].intVal / 60000).toFixed(2),
        pasos: pasos1?.bucket?.[0]?.dataset?.[0]?.point?.[0]?.value?.[0]?.intVal || 0,
        distancia: distancia1.bucket[0].dataset[0].point[0].value[0].fpVal || 0,
        // hora: user.turno === "M" ? horas.mañana1 : horas.tarde1,
        segmentos: actividad.value[2].intVal
      };

      actividades1.push(actividadObj);
    }
  });
}


if (turno2?.bucket?.[0]?.dataset?.[0]?.point) {
  turno2.bucket[0].dataset[0].point.forEach((actividad, index) => {
    const tipoActividad = actividad.value[0].intVal;
    if (tipoActividad === 7 || tipoActividad === 1) {
      const actividadObj = {
        tipo: tipoActividad === 7 ? "A pie" : "Bicicleta",
        tiempo: (actividad.value[1].intVal / 60000).toFixed(2),
        pasos: pasos2?.bucket?.[0]?.dataset?.[0]?.point?.[0]?.value?.[0]?.intVal || 0,
        distancia: distancia2.bucket[0].dataset[0].point[0].value[0].fpVal || 0,
        // hora: user.turno === "M" ? horas.mañana2 : horas.tarde2,
        segmentos: actividad.value[2].intVal
      };

      actividades2.push(actividadObj );
    }
  });
}

  
  const actividadDia = {"Turno1": actividades1, "Turno2": actividades2}

  return actividadDia
}



function obtenerFechaActual() {
  const fecha = new Date();
  const yyyy = fecha.getFullYear();
  let mm = fecha.getMonth() + 1; // Los meses van de 0 a 11
  let dd = fecha.getDate();
  if (mm < 10) {
    mm = '0' + mm;
  }
  if (dd < 10) {
    dd = '0' + dd;
  }
  return `${yyyy}/${mm}/${dd}`;
}


module.exports = {
    dataPrimeraActividad,
    dataSegundaActividad,
    getUsers,
    getNewToken,
    getDia,
    obtenerFechaActual
}