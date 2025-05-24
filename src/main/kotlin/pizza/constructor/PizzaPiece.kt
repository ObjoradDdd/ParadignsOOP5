package org.example.pizza.constructor

import org.example.pizza.component.Topping

class PizzaPiece : PriceManage {
    private var price: Int = 0
    private val toppings: MutableList<Topping> = mutableListOf()

    fun addTopping(topping: Topping) {
        toppings.add(topping)
        recalculatePrice()
    }

    fun duplicateToppings(){
        val tops = toppings.toList()
        toppings.addAll(tops)
        recalculatePrice()
    }

    fun deleteTopping(topping: Topping) {
        val removeIndex = toppings.indexOfFirst {
            it.name.lowercase() == topping.name.lowercase()
        }

        if(removeIndex != -1) {
            toppings.removeAt(removeIndex)
            recalculatePrice()
        }
    }

    fun getToppings(): List<Topping> {
        return toppings.toList()
    }

    fun getPrice(): Int {
        return price
    }

    override fun recalculatePrice() {
        price = toppings.sumOf { it.price.toInt() }
    }

    fun toppingHere(name : String): Boolean {
        toppings.forEach {
            if(it.name.lowercase() == name.lowercase()){
                return true
            }
        }
        return false
    }

}