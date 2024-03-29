package com.example.logintest.dataaccess

import android.util.Log
import com.example.logintest.utils.Secret
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.logintest.utils.Strings.TAG

//Auth
data class LoginModel(
    val UserName: String,
    val password: String
)

data class LoginResponse(
    val expiration: String,
    val token: String
)

interface AuthAPI{
    @POST("Account/login")
    suspend fun login(@Body loginModel:LoginModel): LoginResponse
}

class AuthAPIService(){

    suspend fun login(): LoginResponse{
        return api.login(LoginModel(
            UserName = Secret.USERNAME,
            password = Secret.PASSWORD
        ))
    }

    private val api: AuthAPI by lazy{
        createAPI()
    }

    private fun createAPI(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl("http://fixitmanmike2.ddns.net:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthAPI::class.java)
    }

}






//Reminders
data class Reminder(
    val id: Int,
    val recurrence: Int,
    val recurrenceData: String,
    val reminderText: String,
    val reminderTime: String
)

class AuthInterceptor: Interceptor {

    private fun getToken(): String{
        return runBlocking {
            val t = AuthAPIService().login().token
            Log.d(TAG, "getToken: $t")
            t
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
        currentRequest.addHeader("Authorization", "Bearer ${getToken()}")
        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}

interface ReminderAPI{
    @GET("/reminders")
    suspend fun getAllReminders(): List<Reminder>
}

class ReminderAPIService(){

    private val api : ReminderAPI by lazy {
        createAPI()
    }

    private fun createAPI(): ReminderAPI{
        return Retrofit.Builder()
            .baseUrl("http://fixitmanmike2.ddns.net:80/")
            .client(OkHttpClient().newBuilder()
                .addInterceptor(AuthInterceptor())
                .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReminderAPI::class.java)
    }

    suspend fun getAllReminders() : List<Reminder> = api.getAllReminders()



}

