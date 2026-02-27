package com.deyvieat.shoptogether.core.di

import com.deyvieat.shoptogether.core.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL    = "http://10.0.2.2:8080/"
    const val WS_BASE_URL = "ws://10.0.2.2:8080/ws/rooms/"

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
    // Interceptor con Token
    // ─────────────────────────────────────────────
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionManager: SessionManager
    ): Interceptor {
        return Interceptor { chain ->

            val token = runBlocking {
                sessionManager.token.first()
            }

            val newRequest = chain.request().newBuilder().apply {
                if (!token.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()

            chain.proceed(newRequest)
        }
    }

    // ─────────────────────────────────────────────
    // OkHttp REST
    // ─────────────────────────────────────────────
    @Provides
    @Singleton
    @AuthOkHttp
    fun provideAuthOkHttp(
        logging: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
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
        logging: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
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