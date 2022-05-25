package com.srm.srmapp

import android.content.Context
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.bookings.BookingInterface
import com.srm.srmapp.repository.orders.OrdersInterface
import com.srm.srmapp.repository.predictions.PredictionsInterface
import com.srm.srmapp.repository.recipes.RecipeInterface
import com.srm.srmapp.repository.stock.StockInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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
    const val BASE_URL = "https://smart-restaurant-manager.herokuapp.com" // Test url
    const val datePattern = "dd-MM-yyyy"
    const val timePattern = "HH:mm"
    const val dateTimePattern = "$timePattern $datePattern"
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(timePattern)!!
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(datePattern)!!
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern)!!

    val httpInterceptor: (UserSession) -> Interceptor = { session ->
        Interceptor {
            val re = it.request()
                .newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            if (session.getBearerToken().isNotBlank())
                re.addHeader("Authorization", session.getBearerToken())
            else
                Timber.w("No token!")

            val req = re.build()
            it.proceed(req)
        }
    }

    @Provides
    @Singleton
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @Provides
    @Singleton
    fun provideHttpClient(userSession: UserSession) = OkHttpClient.Builder()
        .addInterceptor(httpInterceptor(userSession))
        .addInterceptor(HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE))
        .followRedirects(false)
        .followSslRedirects(false)
        .build()


    @Provides
    @Singleton
    fun provideGsonConverter(): Gson {
        val gson = GsonBuilder()
            .setDateFormat("dd-MM-yyyy'T'HH:mm:ssz")

        // LocalDate adapter
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


        // LocalDateTime adapter
        gson.registerTypeAdapter(object : TypeToken<LocalDateTime>() {}.rawType,
            object : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {
                private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                private val FORMATTER2: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
                    return JsonPrimitive(FORMATTER2.format(src))
                }

                override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime? {
                    val s = json.asString.substringBefore(".").replace(" ", "T")
                    return LocalDateTime.from(FORMATTER.parse(s))
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

    @Provides
    @Singleton
    fun provideOrderInterface(retrofit: Retrofit): OrdersInterface {
        return retrofit.create(OrdersInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingInterface(retrofit: Retrofit): BookingInterface {
        return retrofit.create(BookingInterface::class.java)
    }


    @Provides
    @Singleton
    fun providePredictionInterface(retrofit: Retrofit): PredictionsInterface {
        return retrofit.create(PredictionsInterface::class.java)
    }
}