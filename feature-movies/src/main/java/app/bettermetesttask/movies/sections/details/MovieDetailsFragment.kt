package app.bettermetesttask.movies.sections.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.bettermetesttask.domainmovies.entries.MovieDetails
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import coil3.compose.AsyncImage
import javax.inject.Inject
import javax.inject.Provider

class MovieDetailsFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelProvider: Provider<MovieDetailViewModel>

    private val viewModel by viewModels<MovieDetailViewModel> {
        SimpleViewModelProviderFactory(viewModelProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                val viewState by viewModel.moviesStateFlow.collectAsStateWithLifecycle()
                MovieDetailsScreen(
                    viewState = viewState,
                    viewLoaded = { viewModel.loadDetails(1) }
                )
            }
        }
    }
}

@Composable
fun MovieDetailsScreen(
    viewState: MovieDetailState,
    viewLoaded: () -> Unit
) {
    viewLoaded()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (viewState) {
            MovieDetailState.Initial -> {}
            is MovieDetailState.Loaded -> {
                MovieDetails(viewState.movie)
            }

            MovieDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MovieDetailState.Error -> TODO()
        }
    }
}

@Composable
fun MovieDetails(movie: MovieDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = movie.title, fontSize = 18.sp, color = Color.Black)
            Text(text = movie.description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}