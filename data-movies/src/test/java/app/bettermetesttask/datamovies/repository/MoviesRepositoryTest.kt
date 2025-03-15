package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.database.entities.MovieEntity
import app.bettermetesttask.datamovies.repository.stores.MoviesFactory
import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class MoviesRepositoryTest {

    private val restStore: MoviesRestStore = mock()
    private val localStore: MoviesLocalStore = mock()
    private val mapper: MoviesMapper = MoviesMapper()

    private val systemUnderTest: MoviesRepository = MoviesRepositoryImpl(localStore, mapper, restStore)

    @Test
    fun `test remove movies from favorites`() = runTest {
        val expectedId = 1

        systemUnderTest.removeMovieFromFavorites(expectedId)

        verify(localStore).dislikeMovie(expectedId)
    }

    @Test
    fun `test add movies to favorites`() = runTest {
        val expectedId = 1

        whenever(localStore.likeMovie(any())).thenReturn(Unit)

        systemUnderTest.addMovieToFavorites(expectedId)

        verify(localStore).likeMovie(expectedId)
    }

    @Test
    fun `test observe movies`() = runTest {
        systemUnderTest.observeLikedMovieIds()

        verify(localStore).observeLikedMoviesIds()
    }

    @Test
    fun `test get movie by id`() = runTest {
        val expectedId = 1
        val movieEntity = MovieEntity(1, "title", "description", null)
        val expectedResult = Result.Success(
            Movie(1, "title", "description", null)
        )

        whenever(localStore.getMovie(expectedId)).thenReturn(movieEntity)

        val actualResult = systemUnderTest.getMovie(expectedId)

        assertEquals(expectedResult, actualResult)
        verify(localStore).getMovie(expectedId)
    }

    @Test
    fun `test get remote movies`() = runTest {
        val movies = MoviesFactory.createMoviesList()
        whenever(restStore.getMovies()).thenReturn(movies)
        whenever(localStore.saveMovies(any())).thenReturn(Unit)

        val expectedResult = Result.Success(movies)

        val actualResult = systemUnderTest.getMovies()

        assertEquals(expectedResult, actualResult)

        verify(localStore).saveMovies(any())
        verify(restStore).getMovies()
    }

    @Test
    fun `test get remote movies is empty`() = runTest {
        whenever(restStore.getMovies()).thenReturn(emptyList())

        val movies = MoviesFactory.createMoviesList()
        whenever(localStore.getMovies()).thenReturn(movies.toLocal())

        val expectedResult = Result.Success(movies)

        val actualResult = systemUnderTest.getMovies()

        assertEquals(expectedResult, actualResult)

        verify(localStore, never()).saveMovies(any())
        verify(restStore).getMovies()
    }

    @Test
    fun `test get remote movies throws error`() = runTest {
        whenever(restStore.getMovies()).thenThrow(IllegalStateException::class.java)

        val movies = MoviesFactory.createMoviesList()
        whenever(localStore.getMovies()).thenReturn(movies.toLocal())

        val expectedResult = Result.Success(movies)

        val actualResult = systemUnderTest.getMovies()

        assertEquals(expectedResult, actualResult)

        verify(localStore, never()).saveMovies(any())
        verify(restStore).getMovies()
    }

    private fun List<Movie>.toLocal() = this.map { mapper.mapToLocal(it) }

}