package org.example.pizza.constructor

import org.example.pizza.component.Dough
import org.example.pizza.component.Side

interface PizzaMake {
    fun createPizza()
    fun chooseDough() : Dough
    fun chooseSide() : Side

    fun chooseTopping(pizzaIndex: Int){
        var continueChoice = true
        while (continueChoice) {
            println("\nВыберите действие:")
            println("1 - Добавить начинку на всю пиццу")
            println("2 - Добавить начинку на половины")
            println("3 - Добавить начинку на диапазон")
            println("4 - Добавить начинку на кусок")
            println("0 - Завершить добавление начинок")

            when (readlnOrNull()?.toIntOrNull() ?: -1) {
                0 -> continueChoice = false
                1 -> addToppingToWhole(pizzaIndex)
                2 -> addToppingToHalves(pizzaIndex)
                3 -> addToppingToRange(pizzaIndex)
                4 -> addToppingToPiece(pizzaIndex)
                else -> println("Неверный ввод")
            }
        }
    }
    fun addToppingToWhole(pizzaIndex: Int)
    fun addToppingToHalves(pizzaIndex : Int)
    fun addToppingToRange(pizzaIndex: Int)
    fun addToppingToPiece(pizzaIndex: Int)

}

