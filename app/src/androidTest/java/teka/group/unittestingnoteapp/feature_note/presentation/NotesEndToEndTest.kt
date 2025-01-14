package teka.group.unittestingnoteapp.feature_note.presentation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import teka.group.unittestingnoteapp.core.util.TestTags
import teka.group.unittestingnoteapp.di.AppModule
import teka.group.unittestingnoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import teka.group.unittestingnoteapp.feature_note.presentation.notes.NotesScreen
import teka.group.unittestingnoteapp.feature_note.presentation.util.Screen
import teka.group.unittestingnoteapp.ui.theme.UnitTestingNoteAppTheme


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalAnimationApi::class)
    @Before
    fun setup(){
        hiltRule.inject()
        composeRule.activity.setContent{
            UnitTestingNoteAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route +
                                "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards(){
        //click on FAB to navigate to AddNote Screen
        composeRule.onNodeWithContentDescription("Add note").performClick()

        //adding title and content to a new note
        composeRule.onNodeWithTag(TestTags.ADD_TITLE_TXT_FIELD).performTextInput("PROGRAMMING")
        composeRule.onNodeWithTag(TestTags.ADD_CONTENT_TXT_FIELD).performTextInput("coding")
        composeRule.onNodeWithContentDescription("Save note").performClick()

        //on notes screen ensure our title and content is displayed
        composeRule.onNodeWithText("PROGRAMMING").assertIsDisplayed()
        composeRule.onNodeWithText("coding").assertIsDisplayed()

        //click on created note to navigate to AddNote Screen for editing
        composeRule.onNodeWithText("PROGRAMMING").performClick()

        // Make sure title and content text fields contain note title and content
        composeRule
            .onNodeWithTag(TestTags.ADD_TITLE_TXT_FIELD)
            .assertTextEquals("PROGRAMMING")
        composeRule
            .onNodeWithTag(TestTags.ADD_CONTENT_TXT_FIELD)
            .assertTextEquals("coding")

        //on navigation to AddNote Screen edit title and save
        composeRule.onNodeWithTag(TestTags.ADD_TITLE_TXT_FIELD).performTextInput("title")
        composeRule.onNodeWithContentDescription("Save note").performClick()

        //ensure title and content is updated
        composeRule.onNodeWithText("titlePROGRAMMING").assertIsDisplayed()
        composeRule.onNodeWithText("coding").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending(){
        for(i in 1..3) {
            //click on FAB to navigate to AddNote Screen
            composeRule.onNodeWithContentDescription("Add note").performClick()
            //adding title and content to a new note and save
            composeRule.onNodeWithTag(TestTags.ADD_TITLE_TXT_FIELD).performTextInput("PROGRAMMING$i")
            composeRule.onNodeWithTag(TestTags.ADD_CONTENT_TXT_FIELD).performTextInput("coding$i")
            composeRule.onNodeWithContentDescription("Save note").performClick()
        }

        //on notes screen ensure our title and content is displayed
        composeRule.onNodeWithText("PROGRAMMING1").assertIsDisplayed()
        composeRule.onNodeWithText("coding1").assertIsDisplayed()
        composeRule.onNodeWithText("PROGRAMMING2").assertIsDisplayed()
        composeRule.onNodeWithText("coding2").assertIsDisplayed()
        composeRule.onNodeWithText("PROGRAMMING3").assertIsDisplayed()
        composeRule.onNodeWithText("coding3").assertIsDisplayed()

        //set up order in descending
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0].assertTextContains("PROGRAMMING3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0].assertTextContains("coding3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1].assertTextContains("PROGRAMMING2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1].assertTextContains("coding2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2].assertTextContains("PROGRAMMING1")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2].assertTextContains("coding1")

    }
}