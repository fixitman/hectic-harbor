package com.example.logintest.dataaccess

import android.content.Context
import java.time.LocalDateTime
import java.time.ZonedDateTime

class CredentialManager(
    private val ctx: Context,
    private val askForCreds : () -> LoginModel
) {
    companion object{
        private const val CREDS: String = "CREDENTIALS"
        private const val PASS: String = "PASS"
        private const val USER:  String = "USER"
        private const val JWT: String = "JWT"
        private const val TOKEN: String = "TOKEN"
        private const val EXPIRATION: String = "EXPIRATION"
    }

    fun getToken() : String?{
        try {// try to use saved token
            var token = getSavedToken()
            if(!token.isNullOrBlank()){
                return token
            }
            // no saved token (or expired) get new token with saved creds
            token = getSavedCreds()?.let{creds ->
               getNewToken(creds)
            }
            if(!token.isNullOrBlank()){
                return token
            }
             //saved creds are no good
            while(token.isNullOrBlank() ){
                val creds = getNewCredsFromUser()
                token = getNewToken(creds)
                if(!token.isNullOrBlank()){
                    if(creds.remember){
                        saveCreds(creds)
                    }else {
                        saveCreds(LoginModel(UserName = "", password = ""))
                    }
                }
            }
            return token
        } catch (e: Exception) {
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

    private fun getNewCredsFromUser(): LoginModel {
        return askForCreds()
    }

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

}
