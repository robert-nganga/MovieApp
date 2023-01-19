package com.robert.mymovies.di

import com.robert.mymovies.repositories.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(moviesRepository: MoviesRepository): RepositoryFilm

    @Binds
    @Singleton
    abstract fun bindSeriesRepository(seriesRepository: SeriesRepository): RepositorySeries
}