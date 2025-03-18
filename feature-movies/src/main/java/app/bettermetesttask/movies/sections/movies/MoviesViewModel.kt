package app.bettermetesttask.movies.sections.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.navigation.MovieCoordinator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val likeMovieUseCase: AddMovieToFavoritesUseCase,
    private val dislikeMovieUseCase: RemoveMovieFromFavoritesUseCase,
    private val adapter: MoviesAdapter,
    private val coordinator: MovieCoordinator
) : ViewModel() {

    private val triggerFlow: StateFlow<Unit> = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val moviesStateFlow: StateFlow<MoviesState> = triggerFlow.flatMapLatest {
        return@flatMapLatest observeMoviesUseCase()
    }.map { result ->
        if (result is Result.Success) {
            adapter.submitList(result.data)
            MoviesState.Loaded(result.data)
        } else {
            MoviesState.Error(R.string.error_unknown)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MoviesState.Loading
    )

    fun likeMovie(movie: Movie) {
        viewModelScope.launch {
            if (movie.liked) {
                dislikeMovieUseCase(movie.id)
            } else {
                likeMovieUseCase(movie.id)
            }
        }
    }

    fun openMovieDetails(movie: Movie) {
        coordinator.showDetails(movie.id)
    }
}