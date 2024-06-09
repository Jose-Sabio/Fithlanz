import ActividadCard from "../cards/ActividadCard.jsx";

const Actividad = ({actividad, turno}) => {

  const dias = actividad

  return (
    <>
      {dias.map((dia,index) => <ActividadCard key={index} dia={dia} turno={turno} />)}
    </>
  )
}

export default Actividad
