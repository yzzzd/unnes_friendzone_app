package com.friend.zone.api

import com.friend.zone.App
import com.friend.zone.BuildConfig
import com.friend.zone.data.Const
import com.nuryazid.core.data.CoreSession
import com.nuryazid.core.helper.StringHelper
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface ApiService {

    @POST("auth/request-token")
    suspend fun requestToken(): String

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("regid") regid: String?
    ): String

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): String

    @GET("list-friend")
    suspend fun listFriend(): String

    @Multipart
    @POST("update-profile")
    suspend fun updateProfile(
        @Query("name") name: String?,
        @Query("about") about: String?,
        @Query("password") password: String?,
        @Part photo: MultipartBody.Part?
    ): String

    companion object {
        private val client: OkHttpClient by lazy {
            val interceptors = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient().newBuilder()
                .addInterceptor(interceptors)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()

                    App.context?.let { context ->
                        val token = CoreSession(context).getString(Const.SESSION.TOKEN)

                        if (token.isNotEmpty()) {
                            requestBuilder.header("Authorization", StringHelper.generateBearerAuth(token))
                        } else {
                            requestBuilder.header("Authorization", StringHelper.generateBasicAuth(BuildConfig.AUTH_USER, BuildConfig.AUTH_PASSWORD))
                        }
                    }

                    chain.proceed(requestBuilder.build())
                }
                .build()
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("${BuildConfig.BASE_URL}api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build()
        }

        fun getInstance() = retrofit.create(ApiService::class.java)
    }
}