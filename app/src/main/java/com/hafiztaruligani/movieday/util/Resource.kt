package com.hafiztaruligani.cryptoday.util


sealed class Resource <T> (){
    companion object{
        const val NETWORK_UNAVAILABLE = "Your network is unavailable"
        const val DATA_NOT_FOUND = "Sorry, the data is not found"
        const val COMMON_ERROR = "Sorry, Somethings wrong"
    }

    class Success<T>(val data: T): Resource<T>()
    class Error<T>(val message: String = COMMON_ERROR, val data: T?=null): Resource<T>()
    class Loading<T>(): Resource<T>()

}
