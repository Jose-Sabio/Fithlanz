import { addDoc, collection, deleteDoc, doc, getDoc, getDocs, updateDoc, setDoc, query, where, arrayUnion, arrayRemove } from "firebase/firestore"
import { db } from "./Firebase"
import { GoogleAuthProvider, browserSessionPersistence, getAuth, setPersistence, signInWithPopup, signOut } from "firebase/auth"
import { useAuthContext } from "../context/authContext"
import { get } from "firebase/database"


/// ---------------- DATOS DE LA COLECCION -----------------

const usuariosCollection = collection(db, "Usuarios")
const diasCollection = collection(db, "dias")
const semanasCollection = collection(db, "semanas")
const logrosCollection = collection(db, "Logros")
const centrosCollection = collection(db, "Centros")



/// ---------------- AÑADIR DATOS -------------------

export const addData = async (strColeccion, newData) => {
    let coleccion = ""
    if(strColeccion === "Logros") {
        coleccion = logrosCollection
    }else if(strColeccion === "Usuarios") {
        coleccion = usuariosCollection
    }else if(strColeccion === "dias") {
        coleccion = diasCollection
    }else if(strColeccion === "semanas") {
        coleccion = semanasCollection
    }

    try {
        const docRef = await addDoc(coleccion, newData)
        return docRef.id
    }catch (err) {
        console.log("Error al añadir nuevos datos a la base de datos",err)
        throw err
    }
}
// Deprecado
export const addUser = async (user) => {
    const docRef = await setDoc(doc(db, "Usuarios", user.reloadUserInfo.localId), {
        username: user.reloadUserInfo.displayName,
        profileImg: user.reloadUserInfo.photoUrl,
        email: user.reloadUserInfo.email,
        turno: "M",
        acumuladoSemana: 0,
        acumuladoMensual: 0,
        dias: [],
        token: "",
        refreshToken: "",
    })
    console.log(docRef)
}

// Agrega un nuevo usuario en Firestore (Como estamos trabajando con un único centro, seteamos a la fuerza la id de la coleccion de HLanz)
export const addUserInCentro = async (user, token) => {
    console.log(user)
    const centroRef = doc(db, "Centros", 'FJSfZGk4yZbQRmoHNuB2')

    try {
        await updateDoc(centroRef, {
            Usuarios: arrayUnion({
                id: user.reloadUserInfo.localId,
                username: user.reloadUserInfo.displayName,
                profileImg: user.reloadUserInfo.photoUrl,
                email: user.reloadUserInfo.email,
                turno: "M",
                acumuladoSemana: 0,
                acumulado: 0,
                dias: [],
                semana: [],
                token: token,
                refreshToken: "",
            })
        })
        console.log("usuario añadido")

    } catch (error) {
        console.log(error)
    }
}

// Función para actualizar el token del usuario en el caso de que se haya conectado nuevamente
export const updateUserToken = async (user, token) => {
    const centroRef = doc(db, "Centros", 'FJSfZGk4yZbQRmoHNuB2')

    try {
        await updateDoc(centroRef, {
            Usuarios: arrayRemove(user)
        })

        const updatedUser = { ...user, token}
        await updateDoc(centroRef, {
            Usuarios: arrayUnion(updatedUser)
          });

          console.log("token actualizado")
    } catch (error) {
        console.log(error)
    }
}



export const updateUserName = async (user, username) => {
    const centroDocRef = doc(db, "Centros", 'FJSfZGk4yZbQRmoHNuB2');

    try {
        // Obtener el documento del centro
        const centroSnapshot = await getDoc(centroDocRef);
        if (centroSnapshot.exists()) {
            const centroData = centroSnapshot.data();
            const usuarios = centroData.Usuarios;

            // Buscar y actualizar el usuario existente
            const updatedUsuarios = usuarios.map(u => {
                if (u.id === user.id) {
                    return { ...u, username: username };
                } else {
                    return u;
                }
            });

            // Actualizar el campo 'Usuarios' en el documento del centro
            await updateDoc(centroDocRef, {
                Usuarios: updatedUsuarios
            });

            console.log("Nombre de usuario actualizado");
            window.location.reload();
        } else {
            console.log("El documento del centro no existe");
        }
    } catch (error) {
        console.log("Error al actualizar el nombre de usuario:", error);
    }
};


export const updateUserTurno = async (user, turno) => {
    const centroDocRef = doc(db, "Centros", 'FJSfZGk4yZbQRmoHNuB2');

    try {
        // Obtener el documento del centro
        const centroSnapshot = await getDoc(centroDocRef);
        if (centroSnapshot.exists()) {
            const centroData = centroSnapshot.data();
            const usuarios = centroData.Usuarios;

            // Buscar y actualizar el turno del usuario existente
            const updatedUsuarios = usuarios.map(u => {
                if (u.id === user.id) {
                    return { ...u, turno: turno };
                } else {
                    return u;
                }
            });

            // Actualizar el campo 'Usuarios' en el documento del centro
            await updateDoc(centroDocRef, {
                Usuarios: updatedUsuarios
            });

            console.log("Turno de usuario actualizado");
            window.location.reload();
        } else {
            console.log("El documento del centro no existe");
        }
    } catch (error) {
        console.log("Error al actualizar el turno de usuario:", error);
    }
};

export const updateUserSyncFit = async (user, refreshToken, token) => {
    const centroDocRef = doc(db, "Centros", 'FJSfZGk4yZbQRmoHNuB2');

    try {
        // Obtener el documento del centro
        const centroSnapshot = await getDoc(centroDocRef);
        if (centroSnapshot.exists()) {
            const centroData = centroSnapshot.data();
            const usuarios = centroData.Usuarios;

            // Buscar y eliminar el usuario existente
            const updatedUsuarios = usuarios.filter(u => u.id !== user.id);

            // Agregar el usuario actualizado con el nuevo token
            const updatedUser = { ...user, refreshToken, token };
            updatedUsuarios.push(updatedUser);

            // Actualizar el campo 'Usuarios' en el documento del centro
            await updateDoc(centroDocRef, {
                Usuarios: updatedUsuarios
            });

            console.log("Refresh Tokens actualizados");

            localStorage.removeItem('access_token');
            localStorage.removeItem('refresh_token');
        } else {
            console.log("El documento del centro no existe");
        }
    } catch (error) {
        console.log("Error al actualizar los tokens:", error);
    }
};


/// ---------------- CARGAR DATOS -------------------

export const getData = async (strColeccion) => {
    let coleccion = ""
    if(strColeccion === "Logros") {
        coleccion = logrosCollection
    }else if(strColeccion === "Usuarios") {
        coleccion = usuariosCollection
    }else if(strColeccion === "dias") {
        coleccion = diasCollection
    }else if(strColeccion === "semanas") {
        coleccion = semanasCollection
    }else if(strColeccion === "Centros") {
        coleccion = centrosCollection
    }

    try {
        const data = await getDocs(coleccion)
        return data.docs.map(doc => ({...doc.data(), id:doc.id}))
    }catch (error) {
        console.log("Error al obtener los datos de la colección", error)
        throw error
    }
}


export const getDataById = async (strColeccion, idproduct) => {
    let coleccion = ""
    if(strColeccion === "Logros") {
        coleccion = logrosCollection
    }else if(strColeccion === "Usuarios") {
        coleccion = usuariosCollection
    }else if(strColeccion === "dias") {
        coleccion = diasCollection
    }else if(strColeccion === "semanas") {
        coleccion = semanasCollection
    }else if(strColeccion === "Centros") {
        coleccion = centrosCollection
    }

    try {
        const itemDocRef = doc(coleccion, idproduct)
        const itemDoc = await getDoc(itemDocRef);

        if(itemDoc.exists()){
            return {...itemDoc.data(), id: itemDoc.id}
        }else {
            console.log("No se encuentra referencias con dicha Id")
            return null
        }
    } catch (error) {
        console.log("Error al obtener los datos",error)
        throw error
    }
}


export const getUserInCentro = async (idUser) => {
    const centroRef = doc(db, "Centros", 'FJSfZGk4yZbQRmoHNuB2')
console.log(idUser)
    try {
        const centroDoc = await getDoc(centroRef);
        const data = centroDoc.data()
        const usuarios = data.Usuarios

        const user = usuarios.find(u => u.id === idUser);
console.log(user)
        if(user) {
            return user
        }else{
            return null
        }
    } catch (error) {
        console.log(error)
    }
}
