package com.plcoding.testingcourse.shopping.domain


class ShoppingCart {

    private val items = mutableListOf<Product>()
    private val validProductIds = (0..5).toList()

    fun addProduct(product: Product, quantity: Int) {
        if (quantity < 0) {
            throw IllegalArgumentException("Quantity can't be negative")
        }
        if (isValidProduct(product)) {
            repeat(quantity) {
                items.add(product)
            }
        } else throw IllegalArgumentException("Product is not valid, check price or ID")
    }

    fun getTotalCost(): Double {
        return items.sumOf { it.price }
    }

    private fun isValidProduct(product: Product): Boolean {
        return product.price >= 0.0 && product.id in validProductIds
    }
}