package com.srm.srmapp

import android.content.Context
import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.repository.authentication.AuthInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://smart-restaurant-manager.herokuapp.com" // Test url

    @Provides
    @Singleton
    fun provideUserSession(@ApplicationContext context: Context, api: AuthInterface): UserSession = UserSession(context, api)

    @Provides
    @Singleton
    fun provideHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
        .build()


    @Provides
    @Singleton
    fun provideGsonConverter(): Gson = GsonBuilder()
        .setDateFormat("dd-MM-yyyy'T'HH:mm:ssz")
        .create()

    @Provides
    @Singleton
    fun provideRetrofitClient(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterface(retrofit: Retrofit): AuthInterface {
        Timber.d("Create api client")
        return retrofit.create(AuthInterface::class.java)
    }
}