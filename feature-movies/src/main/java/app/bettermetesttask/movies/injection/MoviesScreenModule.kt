package app.bettermetesttask.movies.injection

import app.bettermetesttask.movies.navigation.MovieCoordinator
import app.bettermetesttask.movies.navigation.MovieCoordinatorImpl
import app.bettermetesttask.movies.navigation.MovieNavigator
import app.bettermetesttask.movies.navigation.MovieNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
interface MoviesScreenModule {
    @Binds
    fun bindNavigator(navigatorImpl: MovieNavigatorImpl): MovieNavigator

    @Binds
    fun bindCoordinator(coordinatorImpl: MovieCoordinatorImpl): MovieCoordinator
}