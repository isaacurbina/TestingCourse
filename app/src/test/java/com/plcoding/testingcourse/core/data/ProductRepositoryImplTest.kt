package com.plcoding.testingcourse.core.data

import assertk.assertThat
import assertk.assertions.isTrue
import com.plcoding.testingcourse.core.domain.AnalyticsLogger
import com.plcoding.testingcourse.core.domain.LogParam
import com.plcoding.testingcourse.core.domain.Product
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException

internal class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private lateinit var productApi: ProductApi
    private lateinit var analyticsLogger: AnalyticsLogger

    @BeforeEach
    fun setUp() {
        productApi = mockk(relaxed = true)
        analyticsLogger = mockk()
        repository = ProductRepositoryImpl(productApi, analyticsLogger)
    }

    @Test
    fun `Response error, exception is logged`() = runBlocking {
        every { analyticsLogger.logEvent(any(), any(), any()) } answers {
            println("This is a log event")
        }
        mockkConstructor(Product::class)
        every { anyConstructed<Product>().name } returns "Mocked ice cream"
        coEvery { productApi.purchaseProducts(any()) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "Test message"
        }

        val result = repository.purchaseProducts(emptyList())

        assertThat(result.isFailure).isTrue()
        verify {
            analyticsLogger.logEvent(
                key = "http_error",
                LogParam("code", 404),
                LogParam("message", "Test message")
            )
        }
    }
}
