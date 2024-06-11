import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fithlanz.fithlanz.navigation.AppScreens

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>
) {
    val screens = listOf(
        AppScreens.Comunidad, AppScreens.Diario, AppScreens.Retos, AppScreens.Perfil,
    )

    NavigationBar(
        containerColor =  MaterialTheme.colorScheme.surface // Utiliza el color de superficie del tema
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(
                        text = screen.title!!,
                        color = if (currentRoute == screen.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface // Utiliza el color de texto adecuado según la selección del elemento
                    )
                },
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = "",
                        tint = if (currentRoute == screen.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface // Utiliza el color de icono adecuado según la selección del elemento
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.primary
                ),
            )
        }
    }
}
