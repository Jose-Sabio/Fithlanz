import { Link, useNavigate } from "react-router-dom"
import { useAuthContext } from "../../context/authContext.jsx"

import { useState } from "react"
import { singWithGoogle, singMobile } from "../../firebase/funciones.js"


const Header = () => {

    const { userFirebase, singInFirebase } = useAuthContext()
    // const [error, setError] = useState()
    // const navigate = useNavigate()


const handleSingIn = async () => {
  // await singWithGoogle(singInFirebase, setError, navigate)
  await singMobile()
}


  return (
    <header className="flex justify-between items-center p-2 bg-slate-800 text-white sticky top-0 z-50">
        <div className="flex items-center mx-4">
        <Link to="/"><div className="flex items-center">
            <img src="logotipo3.png" alt="logo" className="w-10 md:w-14 mr-2" />
            <span className="text-3xl md:text-5xl font-bold mx-2">FitLanz</span>
        </div></Link>
        </div>
        { !userFirebase ? 
          <button onClick={handleSingIn} className="px-4 py-2 border flex gap-2 bg-blue-800 border-slate-200 dark:border-slate-700 rounded-lg text-slate-700 dark:text-slate-200 hover:border-slate-400 dark:hover:border-slate-500 hover:text-slate-900 dark:hover:text-slate-300 hover:shadow transition duration-150">
          <img className="w-6 h-6" src="https://www.svgrepo.com/show/475656/google-color.svg" loading="lazy" alt="google logo" />
          <span className="hidden md:block font-bold text-white">Login with Google</span>
        </button> : null }


        { userFirebase ? 
        <div className="hidden md:flex">
          <div className="flex justify-center items-center flex-grow text-xl font-bold mx-8 uppercase">
          {userFirebase && (
            <ul className="flex space-x-4">
              <li className="">
                <Link to="/">Actividad</Link>
              </li>
              <li className="">
                <Link to="/comunidad">Comunidad</Link>
              </li>
              <li className="">
                <Link to="/logros">Logros</Link>
              </li>
              <li className="">
                <a href="https://www.ieshlanz.es/blog/">Noticias</a>
              </li>
            </ul>
          )}
          </div>
            <Link to="/perfil"><img src={userFirebase.photoURL} alt="logo user" className="w-12 md:w-16 mx-4 rounded-full" /></Link>
        </div> : null }

    </header>
  )
}

export default Header