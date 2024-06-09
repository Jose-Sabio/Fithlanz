// Para crear un contexto -> createContext, Provider, useContext

import { createContext, useContext, useState } from "react";
import { addUser, getData, getUserInCentro, addUserInCentro, updateUserToken } from "../firebase/crud";

const AuthContext = createContext();
export const AuthProvider = ({children}) => {

    const [userFirebase, setUserFirebase] = useState(null);

    const [userHFit, setUserHFit] = useState(null)

    const [users, setUsers] = useState([])


// Funcion que obtiene todos los usuarios del centro
    const getUsersFirebase = async () => {
        try {

            const centroData = await getData("Centros")

            console.log(centroData[0].Usuarios)
            setUsers(centroData[0].Usuarios)


            return centroData[0].Usuarios
        } catch (error) {
            console.log(error)
        }
    }


// Función para obtener los datos del usuario conectado mediante Google en el caso de existir en la BD o registrarlo en el caso de ser su primera conexión 
    const singInFirebase = async (userData, token, navigate) => {
        setUserFirebase(userData);

        const arrUsers = await getUsersFirebase()

        // Comprueba que el usuario existe en el array de Usuarios
        function coincide() {
            let res = false
            for(let user of arrUsers) {
                if(user?.id == userData.reloadUserInfo.localId) {
                    console.log(user.id, userData.reloadUserInfo.localId)
                    res = true
                }
            }
            return res
        }
        const comp = coincide()
        const userInfo = await getUserInCentro(userData.reloadUserInfo.localId)   
        console.log(userInfo)
        console.log(comp)
        if(comp && userInfo) {
            // updateUserToken(userInfo, token)
            setUserHFit(userInfo)

            navigate("/")
        }else {
            addUserInCentro(userData, token)
            const userInfo = await getUserInCentro(userData.reloadUserInfo.localId)   
            setUserHFit(userInfo)

            navigate("/perfil")
        }

    }

    
// Desconecta al usuario
    const singOutFirebase = () => {
        setUserFirebase(null);
    }

    return (
        <AuthContext.Provider value={{userFirebase, users, userHFit, singInFirebase, singOutFirebase}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuthContext = () => {
    return useContext(AuthContext);
}
    
