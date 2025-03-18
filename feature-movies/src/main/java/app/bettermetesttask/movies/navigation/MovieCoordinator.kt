package app.bettermetesttask.movies.navigation

import javax.inject.Inject

interface MovieCoordinator {
    fun showDetails(movieId: Int)
}

class MovieCoordinatorImpl @Inject constructor(
    private val navigator: MovieNavigator
) : MovieCoordinator {

    override fun showDetails(movieId: Int) {
        navigator.navigateToDetails(movieId)
    }

}