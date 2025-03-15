package app.bettermetesttask.movies.sections.movies

import app.bettermetesttask.domainmovies.entries.Movie

sealed class MoviesState {

    data object Initial : MoviesState()

    data object Loading : MoviesState()

    data class Loaded(val movies: List<Movie>) : MoviesState()
}