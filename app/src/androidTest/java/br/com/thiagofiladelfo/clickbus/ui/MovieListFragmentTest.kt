package br.com.thiagofiladelfo.clickbus.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.resources.FakeMovieData
import br.com.thiagofiladelfo.clickbus.data.repository.network.Service
import br.com.thiagofiladelfo.clickbus.data.setBodyWith
import br.com.thiagofiladelfo.clickbus.ui.view.main.MainActivity
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.adapter.MovieHolder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import retrofit2.Retrofit


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class MovieListFragmentTest {

    private val mockWebServer = MockWebServer()
    val LIST_ITEM_IN_TEST = 4
    val MOVIE_IN_TEST = FakeMovieData.movies[LIST_ITEM_IN_TEST]

    lateinit var API: Service

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockWebServer.start(80)
        API = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(Service::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_isListFragmentVisible_onAppLaunch() {

        mockWebServer.apply {
            enqueue(MockResponse().also { it.setBodyWith("") })
        }

        onView(withId(R.id.edittext_search)).check(matches(isDisplayed()))
    }

    @Test
    fun listGoesOverTheFold() {
        //onView(withText(R.string.title_search)).check(matches(isDisplayed()))
        onView(withId(R.id.edittext_search))
            .perform(click())
            .perform(typeText("mulher"))
            .perform(pressImeActionButton())

        onView(withId(R.id.recycle_view_movies))
            .perform(actionOnItemAtPosition<MovieHolder>(LIST_ITEM_IN_TEST, click()))
    }
}