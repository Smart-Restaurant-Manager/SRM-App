package com.srm.srmapp

import android.content.Context
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.recipes.RecipeInterface
import com.srm.srmapp.repository.stock.StockInterface
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
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://smart-restaurant-manager.herokuapp.com" // Test url
    private const val DATE_PATTERM = "dd-MM-yyyy'T'HH:mm:ssz"

    @Provides
    @Singleton
    fun provideUserSession(@ApplicationContext context: Context, gson: Gson): UserSession {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        val api = retrofit.create(AuthInterface::class.java)
        return UserSession(context, api)
    }

    @Provides
    @Singleton
    fun provideHttpClient(userSession: UserSession) = OkHttpClient.Builder()
        .addInterceptor {
            val req = it.request()
            Timber.i("request url ${req.url.encodedPathSegments}")
            if (req.url.encodedPathSegments[1] == "fake")
                it.proceed(req)
            else
                it.proceed(req
                    .newBuilder()
                    .addHeader("Authorization", userSession.getBearerToken())
                    .build())
        }
        .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
        .build()


    @Provides
    @Singleton
    fun provideGsonConverter(): Gson {
        val gson = GsonBuilder()
            .setDateFormat(DATE_PATTERM)
        gson.registerTypeAdapter(object : TypeToken<LocalDate>() {}.rawType, object : JsonSerializer<LocalDate?>, JsonDeserializer<LocalDate?> {
            private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            private val FORMATTER2: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
                return JsonPrimitive(FORMATTER2.format(src?.atStartOfDay()))
            }

            override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
                val s = json.asString.substringBefore(".").replace(" ", "T")
                val datetime = LocalDateTime.from(FORMATTER.parse(s))
                return datetime.toLocalDate()
            }

        })
        return gson.create()
    }

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
        return retrofit.create(AuthInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideStockInterface(retrofit: Retrofit): StockInterface {
        return retrofit.create(StockInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeInterface(retrofit: Retrofit): RecipeInterface {
        return retrofit.create(RecipeInterface::class.java)
    }
}