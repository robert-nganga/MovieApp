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
    abstract fun bindFilmRepository(filmRepositoryImpl: FilmRepositoryImpl): FilmRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(searchRepository: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindMovieDetailsRepository(movieDetailsRepositoryImpl: MovieDetailsRepositoryImpl): MovieDetailsRepository

    @Binds
    @Singleton
    abstract fun bindSeriesDetailsRepository(seriesDetailsRepository: SeriesDetailsRepositoryImpl): SeriesDetailsRepository

    @Binds
    @Singleton
    abstract fun bindMoreFilmsRepository(moreFilmsRepositoryImpl: MoreFilmsRepositoryImpl): MoreFilmsRepository

}