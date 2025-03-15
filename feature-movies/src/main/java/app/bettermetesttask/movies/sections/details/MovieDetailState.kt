package app.bettermetesttask.movies.sections.details

import app.bettermetesttask.domainmovies.entries.MovieDetails

sealed class MovieDetailState {

    data object Loading : MovieDetailState()

    data class Error(val message: Int): MovieDetailState()

    data class Loaded(val movie: MovieDetails) : MovieDetailState()
}