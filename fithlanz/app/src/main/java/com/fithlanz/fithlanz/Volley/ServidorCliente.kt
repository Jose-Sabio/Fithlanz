import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.fithlanz.fithlanz.models.Logro
import com.fithlanz.fithlanz.models.Usuario
import com.fithlanz.fithlanz.navigation.AppScreens
import com.fithlanz.fithlanz.parseadores.parsearDias
import com.fithlanz.fithlanz.parseadores.parsearSemana
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun obtenerLogros(context: Context, callback: (List<Logro>) -> Unit) {
    val url = "https://cliente-kxx4dhwdza-no.a.run.app/api/logros"

    // Crear una nueva cola de solicitudes utilizando el contexto
    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    val logrosList = mutableListOf<Logro>()

    // Crear una solicitud de arreglo JSON
    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET, url, null,
        { response ->
            // Iterar sobre el arreglo JSON y extraer los datos de cada objeto
            for (i in 0 until response.length()) {
                val logroJson: JSONObject = response.getJSONObject(i)
                val logro = Logro(
                    descr = logroJson.getString("Descripcion"),
                    goal = logroJson.getInt("Goal"),
                    type = logroJson.getString("Tipo"),
                    title = logroJson.getString("Titulo"),
                    nivel=logroJson.getString("Nivel")
                )
                logrosList.add(logro)
            }
            Log.d("obtenerLogros", "Logros obtenidos: $logrosList")
            callback(logrosList)
        },
        { error ->
            Log.e("obtenerLogros", "Error al obtener los logros: ${error.message}")
        }
    )
    requestQueue.add(jsonArrayRequest)
}

fun obtenerclasificacion(context: Context,callback: (List<Usuario>) -> Unit){
    val url = "https://cliente-kxx4dhwdza-no.a.run.app/api/usuarios/con-mas-acumulado"
    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    val Usuarios = mutableListOf<Usuario>()
    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET, url, null,
        { response ->
            for (i in 0 until response.length()) {
                val usuariojson: JSONObject = response.getJSONObject(i)
                val usuario = Usuario(
                    id ="",
                    username = usuariojson.getString("username"),
                    acumulado = usuariojson.getInt("acumulado"),
                    profileImg = usuariojson.getString("profileImg"),
                    refreshtoken = "",
                    diario = listOf(),
                    semana = listOf()
                )
                Usuarios.add(usuario)
            }
            Log.d("obtenerLogros", "Logros obtenidos: $Usuarios")
            callback(Usuarios)
        },
        { error ->
            Log.e("obtenerLogros", "Error al obtener la clasificacion: ${error.message}")
        }
    )
    requestQueue.add(jsonArrayRequest)
}

fun createUser(
    context: Context?,
    correo: String,
    id: String?,
    username: String?,
    centro: String,
    acumulado: Long,
    turno: String?,
    profileImg: String?,
    acumuladoSemana: Int,
    token: String?,
    refreshToken: String?,
    navHostController: NavController
) {
    val requestQueue = Volley.newRequestQueue(context)
    val newUser = JSONObject()
    try {
        newUser.put("id", id)
        newUser.put("username", username)
        newUser.put("email", correo)
        newUser.put("centro", centro)
        newUser.put("acumulado", acumulado)
        newUser.put("semana", JSONArray()) // Lista vacía
        newUser.put("dias", JSONArray()) // Lista vacía
        newUser.put("turno", turno)
        newUser.put("profileImg", profileImg)
        newUser.put("acumuladoSemana", acumuladoSemana)
        newUser.put("token", token)
        newUser.put("refreshToken", refreshToken)
    } catch (e: JSONException) {
        e.printStackTrace()
        return
    }
    val jsonObjectRequest = object : JsonObjectRequest(
        Request.Method.POST,
        "https://cliente-kxx4dhwdza-no.a.run.app/api/users",
        newUser,
        Response.Listener { response ->
            println("a")
            navHostController.navigate(AppScreens.SecondScreen.route)
        },
        Response.ErrorListener { error ->
            println("a")
            error.printStackTrace()
        }
    ) {
        override fun getRetryPolicy(): RetryPolicy {
            return DefaultRetryPolicy(
                15000, // 15 segundos
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        }
    }
    requestQueue.add(jsonObjectRequest)
}


fun fetchCentros(context: Context, callback: (List<String>) -> Unit) {
    val requestQueue = Volley.newRequestQueue(context)
    val url = "https://cliente-kxx4dhwdza-no.a.run.app/api/leercentros"
    val jsonArrayRequest = object : JsonArrayRequest(
        Method.GET,
        url,
        null,
        Response.Listener { response ->
            val centros = ArrayList<String>()
            try {
                for (i in 0 until response.length()) {
                    centros.add(response.getString(i))
                }
                callback(centros)
            } catch (e: JSONException) {
                Log.e("Error", "Error parsing JSON response", e)
                callback(emptyList())
            }
        },
        Response.ErrorListener { error ->
            Log.e("Error", "Error fetching centros: ${error.networkResponse?.statusCode} - ${error.message}", error)
            callback(emptyList())
        }
    ) {
        override fun getRetryPolicy(): RetryPolicy {
            return DefaultRetryPolicy(
                15000, // 15 segundos
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        }
    }
    requestQueue.add(jsonArrayRequest)
}

fun obtenerUsuario(context: Context, userId: String, listener: (Usuario?) -> Unit, errorListener: (Exception) -> Unit) {
    val url = "https://cliente-kxx4dhwdza-no.a.run.app/usuario/$userId"
    println("Sending request to URL: $url")
    val queue = Volley.newRequestQueue(context)

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                println("Response from server: $response")
                val usuario = if (response.has("id")) {
                    println("Usuario encontrado en la respuesta")
                    val semanaJsonArray = response.getJSONArray("semana")
                    val semana = parsearSemana(semanaJsonArray)
                    val diasJsonArray = response.getJSONArray("dias")
                    val dias = parsearDias(diasJsonArray)
                    Usuario(
                        response.getString("id"),
                        response.getString("username"),
                        response.getString("email"),
                        response.getInt("acumulado"),
                        response.getString("refreshToken"),
                        semana,
                        dias
                    )
                } else {
                    println("No se encontró el ID de usuario en la respuesta")
                    null
                }
                listener(usuario)
            } catch (e: Exception) {
                println("Exception during JSON processing: ${e.message}")
                e.printStackTrace()
                errorListener(e)
            }
        },
        { error ->
            println("Network error: ${error.message}")
            error.printStackTrace()
            errorListener(error)
        }
    )
    queue.add(jsonObjectRequest)
}




fun updateRefreshToken(
    context: Context,
    userId: String,
    refreshToken: String,
    onSuccess: (response: JSONObject) -> Unit,
    onError: (error: VolleyError) -> Unit,
    navHostController: NavController
) {
    val url = "https://cliente-kxx4dhwdza-no.a.run.app/api/updateRefreshToken"
    val jsonBody = JSONObject()
    try {
        jsonBody.put("id", userId)
        jsonBody.put("refreshToken", refreshToken)
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.POST, url, jsonBody,
        { response ->
            navHostController.navigate(AppScreens.SecondScreen.route)
            onSuccess(response)
        },
        { error ->
            println(error)
            onError(error)
        }
    )
    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    requestQueue.add(jsonObjectRequest)
}




@Composable
fun getUserIdByEmail(email: String, context: Context, callback:  (String?) -> Unit,) {
    val baseUrl = "https://cliente-kxx4dhwdza-no.a.run.app/api/getUserIdByEmail"
    val url = "$baseUrl?email=$email"
    val request = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            val userId = response.optString("userId")
            callback(userId)
        },
        { error ->
            val errorMessage = error.toString() // Obtener toda la excepción como String
            Log.e("Volley", "Error getting user ID by email: $errorMessage")
            callback(null)
        }
    )
    Volley.newRequestQueue(context).add(request)
}






