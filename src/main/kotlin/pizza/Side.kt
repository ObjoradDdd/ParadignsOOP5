package org.example.pizza

class Side(override var name: String, override var price: Double) : Element(){
    private val availableList : MutableList<Topping> = mutableListOf();

    public fun addAvailableTopping(topping: Topping){
        this.availableList.add(topping)
    }

    public fun getToppings(): MutableList<Topping> {
        return availableList
    }
}

