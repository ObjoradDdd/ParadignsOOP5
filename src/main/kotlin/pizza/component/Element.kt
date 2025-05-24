package org.example.pizza.component

abstract class Element : Comparable<Element> {
    abstract var price: Double
    abstract var name: String

    fun setElementPrice(priceCur : Double){
        price = priceCur
    }

    fun setElementName(nameCur : String){
        name = nameCur
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Element) return false

        return this.name == other.name
    }

    override fun compareTo(other : Element): Int = name.compareTo(name)
}