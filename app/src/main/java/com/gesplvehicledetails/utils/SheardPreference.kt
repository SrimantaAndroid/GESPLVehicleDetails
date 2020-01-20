package com.gespl.bgv.utils

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE



object SheardPreference {
    private val APP_SETTINGS = "APP_SETTINGS"
    private lateinit var preference: SharedPreferences
    fun init(context: Context){
        preference=context.getSharedPreferences(APP_SETTINGS,Context.MODE_PRIVATE)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getSomeStringValue(context: Context,key :String): String? {
        return preference.getString(key,"")
    }
    fun getSomeStringValue(context: Context,key :String,value:Boolean): Boolean? {
        return preference.getBoolean(key,false)
    }

    fun setSomeStringValue(context: Context,key :String, newValue: String) {
        val prefeditor:SharedPreferences.Editor= preference.edit()
        with(prefeditor){
            putString(key,newValue)
            commit()
        }
    }

    fun setSomeStringValue(context: Context,key :String, newValue: Boolean) {
        val prefeditor:SharedPreferences.Editor= preference.edit()
        with(prefeditor){
            putBoolean(key,newValue)
            commit()
        }
    }

}