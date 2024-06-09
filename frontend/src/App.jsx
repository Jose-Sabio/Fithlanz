import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import HomePage from './pages/HomePage.jsx';
import LogrosPage from './pages/LogrosPage.jsx';
import PerfilPage from './pages/PerfilPage.jsx';
import ComunidadPage from './pages/ComunidadPage.jsx';
import ErrorPage from './pages/ErrorPage.jsx';
import ProtectedRouter from './utils/ProtectedRouter.jsx';
import RootPage from './pages/RootPage.jsx';
import { AuthProvider } from './context/authContext.jsx';

function App() {
const router = createBrowserRouter([
  {
    path: '/', element: <RootPage />, errorElement: <ErrorPage />, children: [
      {
        element: <ProtectedRouter redirectPath="/comunidad" />, children: [
          {index: true, element:<HomePage />},
          {path: "/logros", element:<LogrosPage />},
          {path: "/perfil", element:<PerfilPage />}
        ]
      },
      {
        path: "comunidad", element:<ComunidadPage />
      }
    ]
  }
])
  return (
    (
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
    )
  )
}

export default App