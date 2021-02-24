package com.eric.androidplugin

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.lang.reflect.Array.getLength
import java.lang.reflect.Array.newInstance

/**

 * @Author: chen

 * @datetime: 2021/2/24

 * @desc:

 */
object LoadUtil {

    private const val TAG = "LoadUtil"

    private val apkPath = "/sdcard/app-debug.apk"

    // 1. 获取到宿主的dexElements
    fun loadClass(ctx: Context) {
        //获取到当前类加载器的PathClassloader
        val classLoader = ctx.classLoader as PathClassLoader
        //获取BaseDexClassloader
        val baseDexClassloaderClazz = Class.forName("dalvik.system.BaseDexClassLoader")
        //这里其实就是BaseDexClassLoader类的成员变量pathList（DexPathList）
        val pathListField = baseDexClassloaderClazz.getDeclaredField("pathList")
        pathListField.isAccessible = true
        //获取当前pathList在当前类加载器中的值 (换句话说就是采用参设的方式获取baseDeclassloader中的private字段)
        val pathListValue = pathListField.get(classLoader)

        val dexElementsField = pathListValue.javaClass.getDeclaredField("dexElements")
        dexElementsField.isAccessible = true

        //dexElements数组是在pathList中，随意这里需要从宿主变量中获取"属性值"，这里获取到的就是当前宿主的dexElements
        val dexElementsValue = dexElementsField.get(pathListValue)
        Log.v(TAG, "[loadClass] patList:${pathListValue}")
        Log.v(TAG, "[loadClass] dexElements:${dexElementsValue}") //获取到elements数组对象

        //使用DexClassloader加载我们自己的插件代码，获取插件类加载器中，dexElements数组的对象数字
        val dexClassLoader =
            DexClassLoader(apkPath, ctx.cacheDir.absolutePath, null, ctx.classLoader)

        //使用反射，获取DexClassloader插件的pathList
        val dexPthListValue = pathListField.get(dexClassLoader)
        var pluginPathDexElementsField = dexPthListValue.javaClass.getDeclaredField("dexElements")
        pluginPathDexElementsField.isAccessible = true
        //2. 获取到插件的dexElements
        val pluginPathDexElementsValue = pluginPathDexElementsField.get(dexPthListValue)

        //合并数组dexElements + 插件apk的dexElements
        var myLength = getLength(dexElementsValue)
        var pluginLength = getLength(pluginPathDexElementsValue)

        var newLength = myLength + pluginLength

        //获取到数组的类型
        val componentType = dexElementsValue.javaClass.componentType

        val newArray = newInstance(componentType, newLength)
        System.arraycopy(dexElementsValue,0,newArray,0,myLength)
        System.arraycopy(pluginPathDexElementsValue,0,newArray,myLength,pluginLength)

        //将新数组 赋值给宿主的dexElements
        dexElementsField.set(pathListValue,newArray)

    }

}