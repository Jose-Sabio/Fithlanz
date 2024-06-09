import { useEffect, useState } from "react"
import { getData } from "../../firebase/crud"
import { useAuthContext } from "../../context/authContext"
import ProgressBar from "@ramonak/react-progress-bar";

const RetosCard = () => {

    const [logros, setLogros] = useState([])
    const { userHFit } = useAuthContext()
    const [km, setKm] = useState()

    console.log(logros)

    const fetchDataLogros = async () => {
      try {
        const logrosData = await getData("Logros")
        setLogros(logrosData)
      } catch(error) {
        console.log(error)
      }
    }
    
    useEffect(() => {
      fetchDataLogros()

      setKm(((userHFit.acumulado * 0.75)/1000).toFixed(1))
    }, [])
  return (
    <div className='flex flex-wrap flex-grow justify-around items-center m-5'>
    {logros.map(logro => (
      <div key={logro.id} className='flex bg-gray-700 text-white rounded-xl border-2 border-gray-500 h-40 m-2 w-full md:w-5/12 p-2'>
      <div className="flex items-center m-2">
          {logro.Tipo === "Contador" ? 
            <img src={logro.Nivel === "Principiante" ? "/kmBronce.svg" : logro.Nivel === "Intermedio" ? "/kmPlata.svg" : "/kmOro.svg"} className="w-20 "/>
            :
            <img src={logro.Nivel === "Principiante" ? "/rachaBronce.svg" : logro.Nivel === "Intermedio" ? "/rachaPlata.svg" : "/rachaOro.svg"} className="w-20 "/>}
      </div>
      <div className="flex flex-col w-full">
        <label className="text-2xl">{logro.Titulo}</label>
        <label className="text-justify m-3">{logro.Descripcion}</label>
        {logro.type === "Contador" ? 
        <ProgressBar completed={logro.Goal > km ? km : ""+logro.Goal} maxCompleted={logro.Goal} labelAlignment="right" bgColor="#DC5727" barContainerClassName="rounded-full bg-gray-300 text-xs md:text-base" /> 
        : <ProgressBar completed={logro.Goal > userHFit.dias.length ? ""+userHFit?.dias.length : ""+logro.Goal} maxCompleted={logro.Goal} labelAlignment="right" bgColor="#DC5727" barContainerClassName="rounded-full bg-gray-300 text-xs md:text-base" />}
        
      </div>
      </div>
  ))}
    </div>
  )
}

export default RetosCard
