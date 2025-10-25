package com.example.hw_4_1.di

import com.example.hw_4_1.data.network.AccountsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.annotation.Inherited

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun loggingInterceptor():HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun httpClient(loggingInterceptor : HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor ( loggingInterceptor ).build()

    @Provides
    fun retrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://68e9fe5cf1eeb3f856e5b27a.mockapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun accountsApi(retrofit: Retrofit): AccountsApi = retrofit.create(AccountsApi::class.java)
}