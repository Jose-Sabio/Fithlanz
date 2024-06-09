import { useState } from 'react';
import Diario from '../components/cards/Diario.jsx';
import Actividad from '../components/interfaz/Actividad.jsx';
import { useAuthContext } from '../context/authContext.jsx';

const HomePage = () => {
  const { userHFit } = useAuthContext();


  if (!userHFit) {
    return <div>Loading...</div>;
  }

  const dias = userHFit.dias?.slice().reverse() || [];
  const semana = userHFit.semana?.slice().reverse() || [];

  const [isDias, setIsDias] = useState(true);

  function handleClickActividad() {
    setIsDias(prevIsDias => !prevIsDias);
  }

  const actividad = isDias ? semana : dias;


  return (
    <div className='flex flex-col flex-grow items-center justify-center'>
      <Diario actividad={actividad} />
      
      <div className='flex justify-between w-full mt-8 pl-4 pr-5 items-center'>
        <label className='text-xl md:text-3xl font-medium'>Diario {isDias ? 'Mensual' : 'Semanal'}</label>  
        <button onClick={handleClickActividad} className='text-xs md:text-base'>Cambiar a {isDias ? 'semanal' : 'mensual'}</button>
      </div>
      <Actividad actividad={actividad} turno={userHFit.turno} />
    </div>
  );
}

export default HomePage;