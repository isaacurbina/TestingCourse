package com.plcoding.testingcourse.part11.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.plcoding.testingcourse.ui.theme.TestingCourseTheme
import org.junit.Rule
import org.junit.Test

internal class ProfileScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testProfileScreenUi_profileLoaded() {
        composeRule.setContent {
            TestingCourseTheme {
                ProfileScreen(
                    state = previewProfileState()
                )
            }
        }

        composeRule.onNodeWithText("Test username").assertIsDisplayed()
        composeRule.onNodeWithText("Title1").assertIsDisplayed()
        composeRule.onNodeWithText("Body1").assertIsDisplayed()
    }
}
