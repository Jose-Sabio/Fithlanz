import { useAuthContext } from "../../context/authContext.jsx"
import { outWithGoogle } from "../../firebase/funciones.js";

const SignOutButton = () => {

    const { singOutFirebase } = useAuthContext();

    const handleSingOut = async () => {
        try {
            const cerradoCorrectamente = await outWithGoogle();

            if(cerradoCorrectamente) {
                singOutFirebase();
            }
        } catch (error) {
            console.log(error)
        }
    }

  return (
    <button onClick={handleSingOut} className="bg-orange-600 hover:bg-orange-700 text-white font-bold w-full p-3 rounded-full">Cerrar sesion</button>
  )
}

export default SignOutButton