package com.eric.androidplugin.proxy

import android.util.Log

/**

 * @Author: chen

 * @datetime: 2021/2/25

 * @desc:

 */
class CardProxyPerson : ICard {

    val TAG = "CardProxyPerson"

    override fun makeCard(name: String) {
        Log.v(
            TAG, "${name},通过黄牛办卡"
        )
    }
}