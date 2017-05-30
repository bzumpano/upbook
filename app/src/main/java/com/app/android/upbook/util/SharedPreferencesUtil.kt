package com.app.android.upbook.util

import android.content.Context
import android.content.SharedPreferences
import com.app.android.upbook.R

/**
 * Created by bzumpano on 29/05/17.
 */
open class SharedPreferencesUtil {

    fun getSharedPreferences(context: Context) : SharedPreferences {
            return context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    fun isAuthenticated(context: Context) : Boolean {
        val sp = getSharedPreferences(context)

        val email = sp.getString(API_EMAIL_KEY, null)
        val token = sp.getString(API_TOKEN_KEY, null)

        return email.isNullOrEmpty() or token.isNullOrEmpty()
    }


    companion object {

        private val API_EMAIL_KEY = "API_EMAIL"

        private val API_TOKEN_KEY = "API_TOKEN"
    }
}