package com.stevehechio.apps.nectworkconnectivity

/**
 * Created by stevehechio on 10/14/21
 */
sealed class NetworkStatus {
    object Available : NetworkStatus()
    object UnAvailable : NetworkStatus()
}
