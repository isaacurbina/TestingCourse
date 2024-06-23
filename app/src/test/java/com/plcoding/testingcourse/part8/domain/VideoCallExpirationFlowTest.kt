@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.testingcourse.part8.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.plcoding.testingcourse.util.MutableClock
import com.plcoding.testingcourse.util.advanceTimeBy
import com.plcoding.testingcourse.util.scheduledVideoCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes

internal class VideoCallExpirationFlowTest {

    @Test
    fun `Test call expiration`() = runTest {
        val clock = MutableClock(Clock.systemDefaultZone())
        val now = LocalDateTime.now()
        val inFiveMinutes = now.plusMinutes(5)
        val inTenMinutes = now.plusMinutes(10)

        val scheduledCalls = listOf(
            scheduledVideoCall(inFiveMinutes),
            scheduledVideoCall(inTenMinutes)
        )

        VideoCallExpirationFlow(scheduledCalls, clock).test {
            awaitItem() // ignore empty emission

            advanceTimeBy(6.minutes, clock) // expire first call

            val emission2 = awaitItem()
            assertThat(emission2).contains(scheduledCalls[0])
            assertThat(emission2).doesNotContain(scheduledCalls[1])

            advanceTimeBy(5.minutes, clock) // expire second call

            val emission3 = awaitItem()
            assertThat(emission3).contains(scheduledCalls[0])
            assertThat(emission3).contains(scheduledCalls[1])
        }
    }
}
