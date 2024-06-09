import { Link } from "react-router-dom"

const ErrorPage = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <h1 className="text-9xl">Oops!!!</h1>
      <div className="my-20">
        <p className="text-center text-5xl">404 - PAGE NOT FOUND</p>
        <p className=" text-2xl mt-6">La página que has buscado podría haber sido eliminada, cambiado su nombre o haber sido inhabilidata temporalmente</p>
      </div>
      <Link to="/" className="text-4xl text-white rounded-s-full rounded-e-full bg-blue-700 p-5">GO TO HOMEPAGE</Link>
    </div>
  )
}

export default ErrorPage