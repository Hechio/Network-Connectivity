package com.stevehechio.apps.nectworkconnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

/**
 * Created by stevehechio on 10/14/21
 */
class NetworkStatusHelper(private  val context: Context) : LiveData<NetworkStatus>() {

    var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val validNetworkConnections: ArrayList<Network> = ArrayList()
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    /**
     * onActive and onInactive resume and stop monitoring network status from the system
     */

    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest,connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun getConnectivityManagerCallback() = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            val hasNetworkConnection =
                networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            if (hasNetworkConnection){
                Log.e("network status", "has net")
                validNetworkConnections.add(network)
                updateStatus()
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            validNetworkConnections.remove(network)
            updateStatus()
            Log.e("network status", "no net")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                validNetworkConnections.add(network)
            }else{
                validNetworkConnections.remove(network)
            }
            updateStatus()
        }
    }

    private fun updateStatus() {
       if(validNetworkConnections.isEmpty()){
           postValue(NetworkStatus.Available)
       }else{
           postValue(NetworkStatus.UnAvailable)
       }
    }

}