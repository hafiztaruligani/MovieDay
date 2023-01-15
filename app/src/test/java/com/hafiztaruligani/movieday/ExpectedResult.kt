package com.hafiztaruligani.movieday

import com.hafiztaruligani.cryptoday.util.Resource
import kotlin.reflect.KClass

internal sealed class ExpectedResult(){
    class SuccessResourceClass(
        val result :  List<KClass<out Resource<*>>> =
            listOf(
                Resource.Loading::class,
                Resource.Success::class
            )
    ): ExpectedResult()

    class ErrorResourceClass(
        val result :  List<KClass<out Resource<*>>> =
            listOf(
                Resource.Loading::class,
                Resource.Error::class
            )
    ): ExpectedResult()
}