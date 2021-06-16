package com.heycode.freshnewzz.util

import android.content.Context
import android.content.SharedPreferences

class Constants {
    companion object {
        const val API_KEY = "7b244417ae0f48b7838b7ae4ff104b79"
        const val BASE_URL = "https://newsapi.org"
        const val COUNTRY_CODE = "in"
        const val DELAY_TIME = 600L
        const val QUERY_PAGE_SIZE = 20
        const val SHARED_PREFERENCES_NAME = "mySharedPref"

        val sharedPreferences: SharedPreferences = MyClass.getContext().getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }


    abstract class MyClass {

        companion object {
            private lateinit var context: Context

            fun setContext(con: Context) {
                context = con
            }

            fun getContext(): Context {
                return context
            }
        }
    }
}