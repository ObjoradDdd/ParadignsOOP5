package org.example.pizza.component

class Side(override var name: String, override var price: Double, private var isUniversal: Boolean) : Element(){
    private val availableList : MutableList<Topping> = mutableListOf()

    fun addAvailableTopping(topping: Topping){
        this.availableList.add(topping)
    }

    fun getToppings(): MutableList<Topping> {
        return availableList
    }

    fun isUniversalSize(): Boolean {
        return isUniversal
    }

    fun madeSideUniversal(){
        isUniversal = true
    }
}

