import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fithlanz.fithlanz.Auth.AuthManager
import com.fithlanz.fithlanz.IniciarSesion
import com.fithlanz.fithlanz.InicioSesion2
import com.fithlanz.fithlanz.Screens.CargaScren
import com.fithlanz.fithlanz.Screens.NoInternetScreen
import com.fithlanz.fithlanz.isInternetConnected
import com.fithlanz.fithlanz.models.Usuario
import com.fithlanz.fithlanz.navigation.AppScreens
@Composable
fun AppNavigation(context: Context) {
    var refreshtoken= remember { mutableStateOf("")}
    val navController = rememberNavController()
    val isConnected = remember { isInternetConnected(context) }
    val authManager = remember { AuthManager(context, navController) }
    var startDestination by remember { mutableStateOf<String?>(null) }
    var currentUser by remember { mutableStateOf<Usuario?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var hasActivityPermission by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Permission request launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Refresh permissions status
            hasLocationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED

            hasActivityPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PERMISSION_GRANTED

            // Determine start destination after permissions are checked
            if (hasLocationPermission ) {
                startDestinationChoice(context, isConnected, authManager) { route, usuario ->
                    startDestination = route
                    currentUser = usuario
                    isLoading = false
                }
            }
        }
    }

    // LaunchedEffect to check permissions and start destination
    LaunchedEffect(Unit) {
        // Check permissions
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED

        hasActivityPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PERMISSION_GRANTED

        // Request permissions if not granted
        if (!hasLocationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else if (!hasActivityPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            // Permissions are already granted
            isLoading = false
            startDestinationChoice(context, isConnected, authManager) { route, usuario ->
                startDestination = route
                currentUser = usuario
            }
        }
    }

    // Navigation logic based on startDestination
    if (!isLoading) {
        if (startDestination != null) {
            NavHost(navController = navController, startDestination = startDestination!!) {
                composable(AppScreens.FirstScreen.route) {
                    IniciarSesion(navController, authManager, context, currentUser,refreshtoken)
                }
                composable(AppScreens.SecondScreen.route) {
                        InicioSesion2(navController, authManager, context)

                }
                composable(AppScreens.Registro.route) {
                        Registro(context = context, authManager = authManager, navController = navController, refreshToken = refreshtoken, usuario = currentUser,isLoading)
                }
                composable(AppScreens.Wifi.route) {
                    NoInternetScreen()
                }
            }
        } else {
            // Show loading indicator if startDestination is not determined
            CargaScren()
        }
    }
}


fun startDestinationChoice(
    context: Context,
    isConnected: Boolean,
    authManager: AuthManager,
    navigationCallback: (String, Usuario?) -> Unit
) {
    if (!isConnected) {
        println("No internet connection")
        navigationCallback(AppScreens.Wifi.route, null)
    } else {
        val currentUser = authManager.obtenerautenticacion().currentUser
        if (currentUser == null) {
            navigationCallback(AppScreens.FirstScreen.route, null)
        } else {
            println("Current user ID: ${currentUser.uid}")
            obtenerUsuario(context, currentUser.uid.toString(), { usuario ->
                if (usuario != null) {
                    println("Usuario encontrado: ${usuario.username}")
                    if(usuario.refreshtoken.equals("")){
                        navigationCallback(AppScreens.FirstScreen.route,null)
                    }else{
                        navigationCallback(AppScreens.SecondScreen.route, usuario)
                    }
                } else {
                    println("Usuario no encontrado")
                    navigationCallback(AppScreens.FirstScreen.route, null)
                }
            }, { exception ->
                println("Error al obtener el usuario: ${exception.message}")
                exception.printStackTrace()
                navigationCallback(AppScreens.FirstScreen.route, null)
            })
        }
    }
}


