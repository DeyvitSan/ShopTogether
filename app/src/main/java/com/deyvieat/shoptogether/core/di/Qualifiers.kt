package com.deyvieat.shoptogether.core.di


import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WsOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit