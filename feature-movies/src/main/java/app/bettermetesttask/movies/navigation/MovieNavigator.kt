package app.bettermetesttask.movies.navigation

import androidx.navigation.NavController
import app.bettermetesttask.featurecommon.utils.navigation.executeSafeNavAction
import app.bettermetesttask.movies.R
import dagger.Lazy
import javax.inject.Inject

interface MovieNavigator {
    fun navigateToDetails(movieId: Int)
}

class MovieNavigatorImpl @Inject constructor(
    private val navController: Lazy<NavController>,
): MovieNavigator {

    override fun navigateToDetails(movieId: Int) {
        executeSafeNavAction {
            navController.get().navigate(R.id.action_movies_to_details)
        }
    }

}