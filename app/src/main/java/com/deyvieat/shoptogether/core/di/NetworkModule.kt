package com.deyvieat.shoptogether.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL = "https://zuri.space/api/"
    const val WS_BASE_URL = "wss://zuri.space"

    // ─────────────────────────────────────────────
    // Logging
    // ─────────────────────────────────────────────
    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    // ─────────────────────────────────────────────
    // OkHttp REST
    // ─────────────────────────────────────────────
    @Provides
    @Singleton
    @AuthOkHttp
    fun provideAuthOkHttp(
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // ─────────────────────────────────────────────
    // OkHttp WebSocket
    // ─────────────────────────────────────────────
    @Provides
    @Singleton
    @WsOkHttp
    fun provideWsOkHttp(
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .pingInterval(20, TimeUnit.SECONDS)
            .build()
    }

    // ─────────────────────────────────────────────
    // Retrofit
    // ─────────────────────────────────────────────
    @Provides
    @Singleton
    @AuthRetrofit
    fun provideRetrofit(
        @AuthOkHttp okHttp: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
