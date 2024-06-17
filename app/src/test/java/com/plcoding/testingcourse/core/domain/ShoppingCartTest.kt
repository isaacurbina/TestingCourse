package com.plcoding.testingcourse.core.domain

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.plcoding.testingcourse.core.data.ShoppingCartCacheFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ShoppingCartTest {

    private lateinit var cart: ShoppingCart
    private lateinit var cache: ShoppingCartCacheFake

    @BeforeEach
    fun setUp() {
        cache = ShoppingCartCacheFake()
        cart = ShoppingCart(cache)
    }

    @ParameterizedTest
    @CsvSource(
        "0, 0.0",
        "3, 15.0",
        "6, 30.0",
        "20, 100.0"
    )
    fun `Add multiple products, total price sum is correct`(
        qty: Int,
        expectedPriceSum: Double
    ) {
        val product = Product(
            id = 0,
            name = "Ice cream",
            price = 5.0
        )
        cart.addProduct(product, qty)

        val priceSum = cart.getTotalCost()

        assertThat(priceSum).isEqualTo(expectedPriceSum)
    }

    @RepeatedTest(100)
    fun `Add product with negative quantity, throws Exception`() {
        val product = Product(
            id = 0,
            name = "Ice cream",
            price = 5.0
        )

        assertFailure {
            cart.addProduct(product, -5)
        }
    }

    @Test
    fun `isValidProduct returns invalid for not existing product`() {
        val product = Product(
            id = 1345,
            name = "Ice cream",
            price = 5.0
        )
        assertFailure {
            cart.addProduct(product, 4)
        }
        val totalPriceSum = cart.getTotalCost()
        assertThat(totalPriceSum).isEqualTo(0.0)
    }

    @Test
    fun `Test products are saved in cache`() {
        val product = Product(
            id = 1,
            name = "Ice cream",
            price = 5.0
        )
        
        cart.addProduct(product, 2)
        val productsFromCache = cache.loadCart()

        assertThat(productsFromCache).hasSize(2)
        assertThat(productsFromCache).contains(product)
    }
}
