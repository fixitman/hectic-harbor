package com.example.logintest.dataaccess

import android.content.Context
import android.util.Log
import com.example.logintest.utils.Strings.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialManager
    @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {
    companion object{
        private const val CREDS: String = "CREDENTIALS"
        private const val PASS: String = "PASS"
        private const val USER:  String = "USER"
        private const val JWT: String = "JWT"
        private const val TOKEN: String = "TOKEN"
        private const val EXPIRATION: String = "EXPIRATION"
    }

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun getToken() : String?{
        try {// try to use saved token
            var token = getSavedToken()
            if(!token.isNullOrBlank()){
                _events.emit(Event.TokenChangedEvent(token))
                return token
            }
            // no saved token (or expired) get new token with saved creds
            val creds = getSavedCreds()
            creds?.let{
               token = getNewToken(it)
            }

            if(!token.isNullOrBlank()){
                _events.emit(Event.TokenChangedEvent(token!!))
                return token
            }
             //saved creds are no good
            _events.emit(Event.InvalidCredentialsEvent)



//            while(token.isNullOrBlank() ){
//                val creds = getNewCredsFromUser()
//                token = getNewToken(creds)
//                if(!token.isNullOrBlank()){
//                    if(creds.remember){
//                        saveCreds(creds)
//                    }else {
//                        saveCreds(LoginModel(UserName = "", password = ""))
//                    }
//                }
//            }




            return null
        } catch (e: Exception) {

            Log.d(TAG, "getToken: ${e.message}")
            return null
        }

    }

    private fun saveCreds(creds: LoginModel) {
        ctx.getSharedPreferences(CREDS, Context.MODE_PRIVATE)?.run {
            edit()
                .putString(USER,creds.UserName)
                .putString(PASS, creds.password)
                .apply()
        }
    }

    private fun saveToken(response: LoginResponse) {
        ctx.getSharedPreferences(JWT, Context.MODE_PRIVATE)?.run {
            edit()
                .putString(TOKEN,response.token)
                .putString(EXPIRATION, response.expiration)
                .apply()
        }
    }

//    private fun getNewCredsFromUser(): LoginModel {
//        return askForCreds()
//    }

    private fun getSavedCreds(): LoginModel? {
        ctx.getSharedPreferences(CREDS, Context.MODE_PRIVATE)?.run{
            val user = getString(USER,"")
            if(!user.isNullOrBlank()){
                val pw = getString(PASS, "")
                return LoginModel(user,pw?: "")
            }
        }
        return null
    }

    private fun getSavedToken(): String? {
        val savedToken = ctx.getSharedPreferences(JWT, Context.MODE_PRIVATE)
        savedToken?.let{
            val expString = savedToken.getString(EXPIRATION, ZonedDateTime.now().minusHours(1).toString())
            val exp: ZonedDateTime = ZonedDateTime.parse(expString)
            if(exp.isAfter(ZonedDateTime.now().plusHours(1))){
                val tokenString = savedToken.getString(TOKEN,"")
                if(!tokenString.isNullOrBlank()){
                    return tokenString
                }
            }
        }
        return null
    }

    private fun getNewToken(creds: LoginModel): String? {
       return AuthAPIService().login(creds)?.let { loginResponse ->
           saveToken(loginResponse)
           loginResponse.token
       }
    }

    fun updateCreds(value: LoginModel) {
        saveCreds(value)
    }


    sealed interface Event{
        data class TokenChangedEvent(val token: String): Event
        data object InvalidCredentialsEvent: Event
    }

}
