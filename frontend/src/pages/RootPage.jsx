import { Outlet } from "react-router-dom"
import Header from "../components/interfaz/Header"
import Footer from "../components/interfaz/Footer"

const RootPage = () => {
  return (
    <div className="bg-[#f0f0f0] flex flex-col min-h-screen">
      <Header />
      <Outlet />
      <Footer />
    </div>
  )
}

export default RootPage
