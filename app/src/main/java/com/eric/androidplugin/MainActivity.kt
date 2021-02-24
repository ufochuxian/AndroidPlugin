package com.eric.androidplugin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btn).setOnClickListener {
            LoadUtil.loadClass(this)
            getPluginClass()
        }
    }

    private fun getPluginClass() {
        try {
            val loadClass = classLoader.loadClass("com.eric.androidplugin.PluginTest")
            val getToast = loadClass.getDeclaredMethod("getToast", Context::class.java)
            getToast.isAccessible = true
            getToast.invoke(loadClass.newInstance(), applicationContext)
        } catch (e: Exception) {
            Log.e("", "error:$e")
        }
    }
}