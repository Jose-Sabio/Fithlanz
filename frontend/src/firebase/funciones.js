import { addDoc, collection, deleteDoc, doc, getDoc, getDocs, updateDoc } from "firebase/firestore"
import { db } from "./Firebase"
import { GoogleAuthProvider, browserPopupRedirectResolver, browserSessionPersistence, getAuth, getRedirectResult, setPersistence, signInWithPopup, signInWithRedirect, signOut } from "firebase/auth"
// import { google } from "googleapis";





// Iniciar sesion con Google
export const singWithGoogle = async (singInFirebase, setError, navigate) => {
    const auth = getAuth()

    const provider = new GoogleAuthProvider();
    provider.addScope('https://www.googleapis.com/auth/fitness.activity.read');
    provider.addScope('https://www.googleapis.com/auth/fitness.location.read');
    provider.setCustomParameters(
        import.meta.env.VITE_GOOGLE_CLIENT_ID, // ID cliente
        import.meta.env.VITE_GOOGLE_CLIENT_SECRET, // Secreto cliente
        import.meta.env.VITE_AUTH_REDIRECT_URI // URI de redireccionamiento autorizado
      );

    try {
        // await setPersistence(auth, browserSessionPersistence)
        const result = await signInWithPopup(auth, provider);

        // // result contiene toda la informacion de la cuenta seleccionada
    
        const credential = GoogleAuthProvider.credentialFromResult(result);

        console.log(credential)
        const token = credential.accessToken;
        const user = result.user;

        console.log(auth)
        console.log(token)


        // Mandamos al usuario al contexto global utilizando 

        // const dataNode = await getDataNode(token)
        singInFirebase(user, token, navigate)


    } catch (error) {
        const credential = GoogleAuthProvider.credentialFromError(error);
        setError(`Error al iniciar sesion: ${error}`)
    }
}


// Cerrar sesion con Google
export const outWithGoogle = async () => {
    const auth = getAuth();
    try {
        await signOut(auth);
        return true;
    } catch (error) {
        console.log("Error al cerrar sesion",error)
    }
}



export const singMobile = async () => {
    const auth = getAuth()

    const provider = new GoogleAuthProvider();
    provider.addScope('https://www.googleapis.com/auth/fitness.activity.read');
    provider.addScope('https://www.googleapis.com/auth/fitness.location.read');
    provider.setCustomParameters(
        import.meta.env.VITE_GOOGLE_CLIENT_ID, // ID cliente
        import.meta.env.VITE_GOOGLE_CLIENT_SECRET, // Secreto cliente
        import.meta.env.VITE_AUTH_REDIRECT_URI // URI de redireccionamiento autorizado
      );

    try {
        await signInWithRedirect(auth, provider);
    } catch (error) {
        console.log("error")
    }
}

export const getRedirect = async (singInFirebase, navigate) => {
    const auth = getAuth()

    try {
        const result = await getRedirectResult(auth);
        if (result) {
            const credential = GoogleAuthProvider.credentialFromResult(result);
            const token = credential.accessToken;
            const user = result.user;

            // Mandamos al usuario al contexto global utilizando 
            singInFirebase(user, token, navigate);
        }
    } catch (error) {
        const credential = GoogleAuthProvider.credentialFromError(error);
    }
}