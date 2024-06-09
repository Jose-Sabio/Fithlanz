import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';

const Diario = ({ actividad }) => {
  const ultDia = actividad[0] || null;
  let totalPasos = 0
  let puntuacion = 0
  let tiempoTotal = 0

  if (ultDia) {

    if (ultDia.actividad.Turno1 && ultDia.actividad.Turno1.length > 0) {
      ultDia.actividad.Turno1.forEach(turno => {
        totalPasos += turno.pasos
        puntuacion += turno.pasos
        tiempoTotal += parseFloat(turno.tiempo)
      });
    }


    if (ultDia.actividad.Turno2 && ultDia.actividad.Turno2.length > 0) {
      ultDia.actividad.Turno2.forEach(turno => {
        totalPasos += turno.pasos
        puntuacion += turno.pasos
        tiempoTotal += parseFloat(turno.tiempo)
      });
    }
  }

  // Calcular la distancia total
  const distanciaTotal = (totalPasos * 0.00075).toFixed(1); // Convertir pasos a km


  const horas = Math.floor(tiempoTotal / 60)
  const mins = Math.floor(tiempoTotal % 60)
  const segs = Math.floor((tiempoTotal - Math.floor(tiempoTotal)) * 60)

  const formatTime = horas > 0 
    ? `${horas.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${segs.toString().padStart(2, '0')} horas` 
    : `${mins.toString().padStart(2, '0')}:${segs.toString().padStart(2, '0')} mins`

  return (
    <div className='my-8 h-100'>
      <div>
        <CircularProgressbar 
          value={puntuacion} 
          maxValue={10000} 
          text={`${totalPasos}`} 
          styles={buildStyles({ pathColor: '#DC5727', textColor: '#DC5727' })} 
        />
        <div className='flex justify-between mt-8 font-medium text-xl'>
          <div className='flex flex-col justify-center items-center'>
            <label>Distancia: </label>
            <label>{distanciaTotal} km</label>
          </div>
          <div className='flex flex-col justify-center items-center'>
            <label>Duración: </label>
            <label>{formatTime}</label>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Diario;
