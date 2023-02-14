package com.robert.mymovies.di

import android.content.Context
import androidx.room.Room
import com.robert.mymovies.data.local.FilmDatabase
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.utils.Constants.BASE_URL
import com.robert.mymovies.utils.Constants.FILM_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesApi(): MoviesAPI {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MoviesAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideFilmDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        FilmDatabase::class.java,
        FILM_DB
    ).build()

}