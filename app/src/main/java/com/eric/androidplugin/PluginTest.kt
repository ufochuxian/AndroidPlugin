package com.eric.androidplugin

import android.content.Context
import android.util.Log
import android.widget.Toast

/**

 * @Author: chen

 * @datetime: 2021/2/4

 * @desc:

 */
open class PluginTest {


    open fun getToast(ctx: Context) {
        Log.i("PluginTest", "我是插件类中的方法，我被加载并且被调用啦")
        Toast.makeText(ctx, "我是插件类中的方法，我被加载并且被调用啦", Toast.LENGTH_LONG).show()
    }
}