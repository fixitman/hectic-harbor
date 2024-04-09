package com.example.logintest.dataaccess

import android.util.Log
import com.example.logintest.utils.Strings.TAG
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//Auth
data class LoginModel(
    var UserName: String,
    var password: String,
    var remember: Boolean = false
)

data class LoginResponse(
    var expiration: String,
    var token: String
)

interface AuthAPI{
    @POST("Account/login")
    suspend fun login(@Body loginModel:LoginModel): LoginResponse?

    @POST("Account/login")
    fun loginCall(@Body loginModel:LoginModel): Call<LoginResponse?>
}

class AuthAPIService(){

    private val api: AuthAPI by lazy{
        createAPI()
    }

    fun login(creds: LoginModel): LoginResponse?{
        return try{
            api.loginCall(creds).execute().let{response ->
                if(response.isSuccessful){
                    response.body()
                }else{
                    null
                }
            }
        }catch(e : HttpException){
            null
        }
    }

    private fun createAPI(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl("http://fixitmanmike2.ddns.net:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthAPI::class.java)
    }

}



////////////////////////////////////////////////////////////////////////////////////////////////





//Reminders
data class Reminder(
    val id: Int = -999,
    val recurrence: Int = 0,
    val recurrenceData: String = "",
    val reminderText: String = "",
    val reminderTime: String = ""
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

