package org.example.pizza

abstract class Element {
    abstract var price: Double
    abstract var name: String

    fun setElementPrice(priceCur : Double){
        price = priceCur
    }

    fun setElementName(nameCur : String){
        name = nameCur
    }
}