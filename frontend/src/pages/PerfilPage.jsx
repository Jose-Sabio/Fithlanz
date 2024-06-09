import SignOutButton from "../components/interfaz/SignOutButton";
import { useAuthContext } from "../context/authContext";
import { useEffect, useState } from "react";
import { updateUserTurno, updateUserName } from "../firebase/crud";
import { FaRegEdit } from "react-icons/fa";

const dominio = import.meta.env.VITE_DOMINIO_URL || "http://localhost"

const PerfilPage = () => {
  const { userHFit, setUserHFit } = useAuthContext(); // Asegúrate de tener una forma de actualizar el estado del usuario
  const [selectedTurn, setSelectedTurn] = useState(userHFit?.turno || 'M');
  const [isEditingName, setIsEditingName] = useState(false);
  const [isEditingTurn, setIsEditingTurn] = useState(false);
  const [name, setName] = useState(userHFit?.username);
  const [data, setData] = useState(null);

console.log(userHFit)
  const handleSelectChange = (e) => {
    setSelectedTurn(e.target.value);
  };

  const handleNameChange = (e) => {
    setName(e.target.value);
  };

  const handleConfirmName = async () => {
    if (name.trim() === "") {
      alert("Por favor ingresa un nombre válido.");
      return;
    }
    try {
      await updateUserName(userHFit, name); // Actualiza el nombre del usuario
      setUserHFit(prev => ({ ...prev, username: name })); // Actualiza el estado del usuario con el nuevo nombre
    } catch (error) {
      console.error("Error actualizando el nombre del usuario:", error);
    }
    setIsEditingName(false);
  };

  const handleConfirmTurn = async () => {
    try {
      await updateUserTurno(userHFit, selectedTurn); // Actualiza el turno del usuario
      setUserHFit(prev => ({ ...prev, turno: selectedTurn })); // Actualiza el estado del usuario con el nuevo turno
    } catch (error) {
      console.error("Error actualizando el turno del usuario:", error);
    }
    setIsEditingTurn(false);
  };

  const handleCancelName = () => {
    setIsEditingName(false);
    setName(userHFit.username);
  };

  const handleCancelTurn = () => {
    setIsEditingTurn(false);
    setSelectedTurn(userHFit.turno);
  };

  function navigate(url) {
    window.location.href = url;
  }

  const getTokens = async () => {
    try {
      navigate(`https://fitbackend-kxx4dhwdza-no.a.run.app/auth/login`);
    } catch (error) {
      console.log("No es posible comunicar con el servidor");
    }
  };

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const encodedData = params.get("data");

    if (encodedData) {
      const decodedData = JSON.parse(atob(encodedData));
      setData(decodedData);
    }
  }, []);

  return (
  <div className="flex flex-col flex-grow justify-center items-center py-2 bg-gray-50 rounded-lg shadow-md h-full">
  <div className="flex flex-col justify-center items-center w-full">
    <img src={userHFit?.profileImg} alt="logo user" className="w-60 h-60 rounded-full border-4 border-blue-500 shadow-lg" />
    <div className="flex items-center justify-center">
      {isEditingName ? (
        <div className="flex items-center justify-center">
          <input type="text" value={name} onChange={handleNameChange} className="text-xl md:text-3xl my-5 text-gray-700 border-b-2 border-gray-400" />
          <button onClick={handleConfirmName}><svg xmlns="http://www.w3.org/2000/svg" height="40px" viewBox="0 -960 960 960" width="40px" fill="#75FB4C"><path d="M379.33-244 154-469.33 201.67-517l177.66 177.67 378.34-378.34L805.33-670l-426 426Z"/></svg></button>
          <button onClick={handleCancelName}><svg xmlns="http://www.w3.org/2000/svg" height="40px" viewBox="0 -960 960 960" width="40px" fill="#EA3323"><path d="m251.33-204.67-46.66-46.66L433.33-480 204.67-708.67l46.66-46.66L480-526.67l228.67-228.66 46.66 46.66L526.67-480l228.66 228.67-46.66 46.66L480-433.33 251.33-204.67Z"/></svg></button>
        </div>
      ) : (
        <div className="flex items-center justify-center">
          <label className="font-bold text-xl md:text-3xl my-5 text-gray-700">{userHFit?.username}</label>
          <FaRegEdit className="cursor-pointer mx-2 text-3xl" onClick={() => setIsEditingName(true)} />
        </div>
      )}
    </div>

    <div className="flex items-center justify-center text-base">
      <label className="font-bold mr-2">Turno:</label>
      {isEditingTurn ? (
        <div className="flex items-center justify-center">
          <select value={selectedTurn} onChange={handleSelectChange} className="" >
            <option value="M">Mañana</option>
            <option value="T">Tarde</option>
          </select>
          <button onClick={handleConfirmTurn}><svg xmlns="http://www.w3.org/2000/svg" height="30px" viewBox="0 -960 960 960" width="40px" fill="#75FB4C"><path d="M379.33-244 154-469.33 201.67-517l177.66 177.67 378.34-378.34L805.33-670l-426 426Z"/></svg></button>
          <button onClick={handleCancelTurn}><svg xmlns="http://www.w3.org/2000/svg" height="30px" viewBox="0 -960 960 960" width="40px" fill="#EA3323"><path d="m251.33-204.67-46.66-46.66L433.33-480 204.67-708.67l46.66-46.66L480-526.67l228.67-228.66 46.66 46.66L526.67-480l228.66 228.67-46.66 46.66L480-433.33 251.33-204.67Z"/></svg></button>
        </div>
      ) : (
        <div className="flex items-center justify-center">
          <p className="mx-2">{userHFit.turno === "M" ? "Mañana" : "Tarde"}</p>
          <FaRegEdit
            className="cursor-pointer mx-2"
            onClick={() => setIsEditingTurn(true)}
          />
        </div>
      )}
    </div>

    {!userHFit.refreshToken && <div className="flex flex-col items-center text-center mt-4">
      <p className="text-gray-600 mb-2 text-xs md:text-base text-justify w-1/2">
        Para poder ofrecerle una experiencia personalizada y completa en nuestra plataforma,
        es necesario que nos preste su autorización para acceder a sus datos de Google Fit
      </p>
      <button onClick={getTokens} className="bg-gray-700 text-white font-bold text-sm p-3 rounded-full border border-gray-400 mt-2 hover:bg-gray-500 hover:border-gray-600">Sync With Fit</button>
    </div>}
  </div>
  <div className="mt-2 w-3/5 md:w-1/4 p-4">
    <SignOutButton />
  </div>
</div>
  );
};

export default PerfilPage;