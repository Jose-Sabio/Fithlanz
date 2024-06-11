package com.fithlanz.fithlanz.Auth

import android.content.Context
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


//Esta clase representa el resultado de la Api
sealed class AuthRes<out T> {
    // La clase tiene dos subclases (osea que heredan de AuthRes)
    //Succes , que es la clase que representa
    data class Success<out T>(val data: T) : AuthRes<T>()
    data class Error(val message: String) : AuthRes<Nothing>()

}

class AuthManager(context: Context, navController: NavController) {

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private val signInClient= Identity.getSignInClient(context)

    fun Usuario(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun isUserSignedIn(context: Context): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null
    }

    fun obtenerCliente(): SignInClient { return  signInClient }

     fun obtenerautenticacion(): FirebaseAuth { return auth }

    val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(
                Scope("https://www.googleapis.com/auth/fitness.activity.write"),
                Scope("https://www.googleapis.com/auth/fitness.activity.read"),
                Scope("https://www.googleapis.com/auth/fitness.location.read")
            )
            .requestIdToken("965860797920-09l4gfarabtpqfvmkhq8oc7g0m5l3e3i.apps.googleusercontent.com")
            .requestServerAuthCode("965860797920-09l4gfarabtpqfvmkhq8oc7g0m5l3e3i.apps.googleusercontent.com", true) // true indica que se solicita el refresh token
            .requestEmail()
            .requestProfile()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (ex: ApiException) {
            AuthRes.Error(ex.message ?: "Google Sign-in failed")
        }
    }

    suspend fun signInWithGoogleCredential(credential: AuthCredential):AuthRes<FirebaseUser>?{
       return try {
           val firebaseUser=auth.signInWithCredential(credential).await()
           firebaseUser.user?.let {
               AuthRes.Success(it)
           }?:throw Exception("Sign In with google failed")
       }catch (e:Exception){
           AuthRes.Error(e.message?:"Sign in with Google failed")
       }
    }

    fun getFitnessOptions(): FitnessOptions {
        return FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

}

