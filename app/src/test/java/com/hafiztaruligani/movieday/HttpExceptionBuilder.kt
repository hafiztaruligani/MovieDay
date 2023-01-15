package com.hafiztaruligani.movieday

import com.hafiztaruligani.movieday.util.StatusCode
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

internal sealed class HttpExceptionBuilder {
    class NotFoundException (
        val exception: HttpException =
            HttpException(
                Response.error<ResponseBody>(
                    StatusCode.NotFound.code ,
                    "exception".toResponseBody("plain/text".toMediaType())

                )
            )
    ): HttpExceptionBuilder()
}