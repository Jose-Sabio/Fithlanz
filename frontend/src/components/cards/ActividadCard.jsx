
const ActividadCard = ({dia, turno}) => {

  return (
    <div className="flex flex-col md:flex-row items-center justify-around md:items-stretch w-full">
      <div className="bg-gray-700 w-11/12 md:w-2/5 border-gray-500 border-2 rounded-xl m-2 p-4 flex flex-col text-white">
        <div className="flex justify-between">
          <label className="font-bold text-xl mb-2">{dia.dia}</label>
          <p className="text-lg font-semibold">{turno === "M" ? "07:30 - 08:40" : "15:30 - 16:20"}</p>
        </div>
        <div className="flex flex-col flex-grow justify-center items-center">
          <div>{dia?.actividad.Turno1 ? dia?.actividad.Turno1.map(ac => (
            <div key={ac} className="flex justify-center items-center my-2">
              <img src={ac.tipo === "A pie" ? "run2.png" : "bici2.png"} alt="" className="w-20" />
              <div className="flex flex-col mx-4">
                <label className="text-xl md:text-2xl font-semibold mb-2">{ac.tipo === "A pie" ? "Caminando" : "Bicicleta"}</label>
                <label className="font-semibold">Distancia: {(ac.pasos * 0.00075).toFixed(2)} km</label>
                <label className="font-semibold">Duración: {ac.tiempo} Mins</label>
              </div>
            </div>
          )) : <label>No hay datos registrados</label>}</div>
        </div>
      </div>


      <div className="bg-gray-700 text-white w-11/12 md:w-2/5 border-gray-500 border-2 rounded-xl m-2 p-4 flex flex-col">
        <div className="flex justify-between">
          <label className="font-bold text-xl mb-2">{dia.dia}</label>
          <p className="text-lg font-semibold">{turno === "M" ? "14:45 - 15:30" : "22:00 - 22:40"}</p>
        </div>
        <div className="flex flex-col flex-grow justify-center items-center">
          <div>{dia?.actividad.Turno2 ? dia?.actividad.Turno2.map(ac => (
            <div key={ac} className="flex justify-center items-center my-2">
              <img src={ac.tipo === "A pie" ? "run2.png" : "bici2.png"} alt="" className="w-20" />
              <div className="flex flex-col mx-4 ">
                <label className="text-xl md:text-2xl font-semibold mb-2">{ac.tipo === "A pie" ? "Caminando" : "Bicicleta"}</label>
                <label className="font-semibold">Distancia: {(ac.pasos * 0.00075).toFixed(2)} km</label>
                <label className="font-semibold">Duración: {ac.tiempo} Mins</label>
              </div>
            </div>
          )) : <label>No hay datos registrados</label>}</div>
        </div>
      </div>
  </div>
  )
}

export default ActividadCard
