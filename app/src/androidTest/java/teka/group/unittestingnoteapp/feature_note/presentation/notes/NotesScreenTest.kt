package teka.group.unittestingnoteapp.feature_note.presentation.notes

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import teka.group.unittestingnoteapp.core.util.TestTags
import teka.group.unittestingnoteapp.di.AppModule
import teka.group.unittestingnoteapp.feature_note.presentation.MainActivity
import teka.group.unittestingnoteapp.feature_note.presentation.util.Screen
import teka.group.unittestingnoteapp.ui.theme.UnitTestingNoteAppTheme


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalAnimationApi::class)
    @Before
    fun setup(){
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            UnitTestingNoteAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ){
                    composable(route = Screen.NotesScreen.route){
                        NotesScreen(navController = navController)
                    }
                }

            }
        }
    }

    @Test
    fun clickToggleOrderSection_isVisible(){
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).isDisplayed()
    }



}