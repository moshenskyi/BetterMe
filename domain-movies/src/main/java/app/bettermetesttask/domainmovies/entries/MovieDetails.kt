package app.bettermetesttask.domainmovies.entries

data class MovieDetails(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
)

fun Movie.toMovieDetails() = MovieDetails(
    id = id,
    title = title,
    description = description,
    posterPath = posterPath
)