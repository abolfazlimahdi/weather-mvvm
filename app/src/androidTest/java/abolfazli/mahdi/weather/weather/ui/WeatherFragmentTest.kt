package abolfazli.mahdi.weather.weather.ui


import abolfazli.mahdi.weather.R
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherFragmentTest {

    @Test
    fun testNavigateToProfile() {

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()).apply {
            setGraph(R.navigation.main_nav)
            setCurrentDestination(R.id.weatherFragment)
        }

        val fragmentScenario = launchFragmentInContainer<WeatherFragment>(themeResId = R.style.AppTheme)


        fragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.iv_search)).perform(ViewActions.click())

        val backStack = navController.backStack
        val currentDestination = backStack.last()
        assertThat(currentDestination.destination.id).isEqualTo(R.id.searchFragment)
    }


}