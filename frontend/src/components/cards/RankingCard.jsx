import ProgressBar from "@ramonak/react-progress-bar";

const RankingCard = ({ user, index, max }) => {
  const { username, acumulado, profileImg } = user;
  const conversion = ((acumulado * 0.75) / 1000).toFixed(1);

  return (
    <div className="rounded-xl bg-gray-700 flex justify-between items-center text-white p-3 m-2 my-3 border-2 border-gray-500">
      <div className="flex items-center w-1/4 md:w-1/6"> 
        <label className="text-sm md:text-xl font-bold">{index + 1}</label>
        <label className="ml-4 text-xs md:text-xl font-bold truncate">{username}</label> 
      </div>
      <div className="flex-grow mx-4">
        <div className="w-full"> 
          <ProgressBar completed={conversion} maxCompleted={max} labelAlignment="right" bgColor="#DC5727" labelClassName="mx-2 text-xs md:text-base font-bold" barContainerClassName="rounded-full bg-gray-400 ml-8 md:ml-0" />
        </div>
      </div>
      <img src={profileImg} className="rounded-full w-10 h-10 md:w-12 md:h-12" />
    </div>
  );
};

export default RankingCard;