package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.MovieDetails
import app.bettermetesttask.domainmovies.entries.toMovieDetails
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(id: Int): Result<MovieDetails> {
        return when (val result = repository.getMovie(id)) {
            is Result.Success -> Result.Success(result.data.toMovieDetails())
            is Result.Error -> Result.Error(result.error)
        }
    }

}