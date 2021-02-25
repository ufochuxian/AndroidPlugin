package com.eric.androidplugin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidplugin.proxy.CardProxyPerson
import com.eric.androidplugin.proxy.ICard
import com.eric.plugin.LoadUtil
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy


class MainActivity : AppCompatActivity() {


    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btn).setOnClickListener {
            LoadUtil.loadClass(this)
            getPluginClass()
        }

        proxyTest()
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

    private fun proxyTest() {
        var cardProxyPerson = CardProxyPerson()
        val newProxyInstance = Proxy.newProxyInstance(CardProxyPerson::class.java.classLoader,
            arrayOf(ICard::class.java),
            InvocationHandler { proxy, method, args ->
                Log.v(TAG,"这是动态代理执行的方法，可以在这里加上一些自定义的逻辑")
//                method.invoke(cardProxyPerson,args)
                args[0] = "小红"
                //这里使用反射的方式调用方法，需要将参数的对应上,这里的方法就是"代理的接口对象"的方法，比如这里指的就是makeCard
                method.invoke(cardProxyPerson,args[0])
            }) as ICard
        //这里使用jdk提供的Proxy对象可以实现动态代理方法，进行拦截做一些事情
        newProxyInstance.makeCard("小明")
    }
}