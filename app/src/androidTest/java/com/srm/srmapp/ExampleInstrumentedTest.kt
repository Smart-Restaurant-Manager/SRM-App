package com.srm.srmapp

import androidx.test.filters.SmallTest
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.authentication.AuthRepository
import com.srm.srmapp.repository.recipes.RecipeRepository
import com.srm.srmapp.repository.stock.StockRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class ExampleInstrumentedTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val testDispatcher = UnconfinedTestDispatcher()

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var authInterface: AuthInterface

    @Inject
    lateinit var stockRepository: StockRepository

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var userSession: UserSession


    @Before
    fun init() {
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun destroy() {
        userSession.logout()
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun test_login() {
        val login = runBlocking {
            authRepository.login("frank@srm.com", "frank.srm")
        }
        assert(login.isSuccess())
        val user = runBlocking {
            authInterface.getUser(userSession.getBearerToken())
        }
        assert(user.isSuccessful && user.body()?.email == "frank@srm.com")
        assert(userSession.isLoggedIn())
    }
}
