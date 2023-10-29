package com.inksy.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inksy.Interfaces.NetworkStateChangeListener
import com.inksy.UI.Constants

class MyReceiverForInternet(var _listener: NetworkStateChangeListener) : BroadcastReceiver() {

    var listener: NetworkStateChangeListener? = _listener


    fun MyReceiverForInternets() {}
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (listener != null) {
            if (Constants.isNetworkConnected(p0!!)) {
                listener!!.NetworkStateChange(true)
            } else {
                listener!!.NetworkStateChange(false)
            }
        }
    }


}