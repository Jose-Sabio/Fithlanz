import { useEffect, useState } from "react";
import ComunidadPanel from "../components/interfaz/ComunidadPanel"
import { useAuthContext } from "../context/authContext";
import { updateUserSyncFit } from "../firebase/crud";
import { getRedirect } from "../firebase/funciones";
import { useNavigate } from "react-router-dom";


const ComunidadPage = () => {
  const navigate = useNavigate()
  const { userHFit, singInFirebase } = useAuthContext();
  const [tokens, setTokens] = useState(null);


  useEffect(() =>{
    getRedirect(singInFirebase, navigate)
  })

  useEffect(() => {
    // Obtener los parámetros de la URL
    const params = new URLSearchParams(window.location.search);
    const encodedData = params.get('data');

    if (encodedData) {
      // Decodificar los datos de base64 a JSON
      const decodedData = JSON.parse(atob(encodedData));
      setTokens({
        access_token: decodedData.access_token,
        refresh_token: decodedData.refresh_token
      });

      // Guardar los tokens en el localStorage
      localStorage.setItem('access_token', decodedData.access_token);
      localStorage.setItem('refresh_token', decodedData.refresh_token);
    } else {
      // Recuperar los tokens del localStorage si existen
      const access_token = localStorage.getItem('access_token');
      const refresh_token = localStorage.getItem('refresh_token');
      if (access_token && refresh_token) {
        setTokens({ access_token, refresh_token });
      }
    }
  }, []);

  useEffect(() => {
    if (userHFit && tokens) {
      updateUserSyncFit(userHFit, tokens.refresh_token, tokens.access_token);
    }
  }, [userHFit, tokens]);
  return (
    <ComunidadPanel />
  )
}

export default ComunidadPage