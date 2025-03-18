package app.bettermetesttask.movies.sections.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.interactors.GetMovieDetailsUseCase
import app.bettermetesttask.movies.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {
    private val moviesMutableFlow: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState.Loading)

    val moviesStateFlow: StateFlow<MovieDetailState>
        get() = moviesMutableFlow.asStateFlow()

    fun loadDetails(movieId: Int?) {
        viewModelScope.launch {
            if (movieId == null) {
                moviesMutableFlow.update { MovieDetailState.Error(R.string.error_unknown) }
            }
            else {
                val result = getMovieDetailsUseCase(movieId)
                moviesMutableFlow.update {
                    when (result) {
                        is Result.Success -> MovieDetailState.Loaded(result.data)
                        is Result.Error -> MovieDetailState.Error(R.string.error_unknown)
                    }
                }
            }
        }
    }
}
