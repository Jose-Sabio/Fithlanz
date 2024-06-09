import { useEffect, useState } from "react";
import { getData } from "../../firebase/crud";
// import ProgressBar from "@ramonak/react-progress-bar";
import RankingCard from "../cards/RankingCard";

const ComunidadPanel = () => {
  const [users, setUsers] = useState([]);
  const [max, setMax] = useState(0);

  const fetchDataUsers = async () => {
    try {
      const centroData = await getData("Centros");

      const fetchedUsers = centroData[0].Usuarios;

      const sortedUsers = fetchedUsers.sort((a, b) => b.acumulado - a.acumulado);

      const maxAcumulado = sortedUsers.reduce((max, user) => Math.max(max, user.acumulado), 0);
      const maxConverted = ((maxAcumulado * 0.75) / 1000).toFixed(1);

      // Update state
      setUsers(sortedUsers);
      setMax(maxConverted);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchDataUsers();
  }, []);

  return (
    <div className="flex flex-col flex-grow m-3">
      {users.map((user, index) => (
        <RankingCard key={index} user={user} index={index} max={max} />
      ))}
    </div>
  );
};

export default ComunidadPanel;
