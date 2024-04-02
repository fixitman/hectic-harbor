package com.example.logintest.dataaccess

import android.util.Log
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
import retrofit2.HttpException
import java.time.LocalDateTime

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
    suspend fun login(@Body loginModel:LoginModel): LoginResponse?
}

class AuthAPIService(){

    suspend fun login(creds: LoginModel): LoginResponse?{

        try{
            val res = api.login(creds)
            return res

        }catch(e : HttpException){
            return null
        }

    }

    private val api: AuthAPI by lazy{
        createAPI()
    }

    private fun createAPI(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl("http://fixitmanmike2.ddns.net:80/")
            .addConverterFactory(GsonConverterFactory.create())
//            .client(OkHttpClient.Builder()
//                .addInterceptor(AuthErrorInterceptor())
//                .build()
//            )
            .build()
            .create(AuthAPI::class.java)
    }

}


class AuthErrorInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val resp = chain.proceed(req)
        when(resp.code()){
            401 -> {


            }
        }
        return resp
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

class AuthInterceptor(
    val credMgr: CredentialManager
): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
        currentRequest.addHeader("Authorization", "Bearer ${credMgr.getToken()}")
        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}

interface ReminderAPI{
    @GET("/reminders")
    suspend fun getAllReminders(): List<Reminder>
}

class ReminderAPIService(credMgr: CredentialManager) {

    private val api : ReminderAPI by lazy {
        createAPI(credMgr)
    }

    private fun createAPI(credMgr: CredentialManager): ReminderAPI{
        return Retrofit.Builder()
            .baseUrl("http://fixitmanmike2.ddns.net:80/")
            .client(OkHttpClient().newBuilder()
                .addInterceptor(AuthInterceptor(credMgr))
                .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReminderAPI::class.java)
    }

    suspend fun getAllReminders() : List<Reminder> {
        return try {
            api.getAllReminders()
        }catch(e: HttpException){
            Log.e(TAG, "getAllReminders: ${e.message()}" )
            listOf<Reminder>()
        }
    }



}

