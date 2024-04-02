package com.example.logintest.dataaccess

import android.content.Context
import com.example.logintest.utils.Secret
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class CredentialManager(
    val ctx: Context
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
        var token = getSavedToken()
        if(!token.isNullOrBlank()){
            return token
        }
        var creds = getSavedCreds()
        if(creds != null){
            token = getNewTokenWithCreds(creds)
            if(!token.isNullOrBlank()){
                return token
            }
        }
        //if we're here, creds are no good
        while(token == null){
            creds = getNewCreds()
            token = getNewTokenWithCreds(creds)
        }
        return token

    }

    private fun getNewCreds(): LoginModel {
        //TODO("Not yet implemented")
        //return LoginModel(Secret.USERNAME, Secret.PASSWORD)
        return LoginModel(UserName = "wrong", "password")
    }

    fun getSavedCreds(): LoginModel? {

        val savedCreds = ctx.getSharedPreferences(CREDS, Context.MODE_PRIVATE)
        savedCreds?.let{
            val user = savedCreds.getString(USER,"")
            if(user != ""){
                val pw = savedCreds.getString(PASS, "")
                return LoginModel(user!!,pw!!)
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

    private fun getNewTokenWithCreds(model: LoginModel): String? {
       var r =  runBlocking {
            val m = AuthAPIService().login(model)
            m?.token
        }
        return r
    }
}
