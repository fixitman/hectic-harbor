package com.example.logintest.dataaccess

import android.content.Context
import com.example.logintest.utils.Secret
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class CredentialManager(
    private val ctx: Context,
    private val getCredsFunction : () -> LoginModel
) {
    companion object{
        private const val CREDS: String = "CREDENTIALS"
        private const val PASS: String = "PASS"
        private const val USER:  String = "USER"
        private const val JWT: String = "JWT"
        private const val TOKEN: String = "TOKEN"
        private const val EXPIRATION: String = "EXPIRATION"
    }

    fun getToken() : String{
        // try to use saved token
        var token = getSavedToken()
        if(!token.isNullOrBlank()){
            return token
        }
        // no saved token (or expired)
        var creds = getSavedCreds()
        if(creds != null){
            token = getNewToken(creds)
            if(!token.isNullOrBlank()){
                return token
            }
        }
        // saved creds are no good
        while(token == null){
            creds = getNewCreds()
            token = getNewToken(creds)
            if(!token.isNullOrBlank() && creds.remember){
                saveCreds(creds)
            }
        }
        return token

    }

    private fun saveCreds(creds: LoginModel) {
        ctx.getSharedPreferences(CREDS, Context.MODE_PRIVATE)?.run {
            edit()
                .putString(USER,creds.UserName)
                .putString(PASS, creds.password)
                .apply()
        }
    }

    private fun getNewCreds(): LoginModel {
        //TODO("Not yet implemented")
        //return LoginModel(Secret.USERNAME, Secret.PASSWORD)
        //return LoginModel(UserName = "wrong", "password")
        return getCredsFunction()
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
            val expString = savedToken.getString(EXPIRATION, LocalDateTime.now().minusHours(1).toString())
            val exp: LocalDateTime = LocalDateTime.parse(expString)
            if(exp.isAfter(LocalDateTime.now())){
                val tokenString = savedToken.getString(TOKEN,"")
                if(!tokenString.isNullOrBlank()){
                    return tokenString
                }
            }
        }
        return null
    }

    private fun getNewToken(model: LoginModel): String? {
       return  AuthAPIService().login(model)?.token

    }



}
