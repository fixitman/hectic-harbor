package com.example.logintest.dataaccess

import android.content.Context

class CredManager(
    val ctx: Context,
    val fnGetCreds: ()->LoginModel?
) {
    private val PASS: String = "PASS"
    private val USER:  String = "USER"
    private val CREDS: String = "CREDENTIALS"

    fun getCreds(): LoginModel? {

        val x = ctx.getSharedPreferences(CREDS, Context.MODE_PRIVATE)
        x?.let{
            val user = x.getString(USER,"")
            if(user != ""){
                val pw = x.getString(PASS, "")
                return LoginModel(user!!,pw!!)
            }
        }
        val creds =  fnGetCreds()
        creds?.let {
            x.edit()
                .putString(USER, it.UserName)
                .putString(PASS, it.password)
                .apply()
            return creds
        }
        return null
    }
}
