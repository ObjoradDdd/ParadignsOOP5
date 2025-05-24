package org.example.pizza.order

import org.example.pizza.constructor.Pizza
import org.example.pizza.constructor.PriceManage
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.UUID

class Order(
    private val id: UUID = UUID.randomUUID(),
    private val pizzas: MutableList<Pizza> = mutableListOf(),
    private var comment: String = "",
    private val orderTime: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC).plusHours(7),
    private var scheduledTime: LocalDateTime? = null
) : PriceManage {
    private var price: Double = 0.0

    fun getScheduledTime(): LocalDateTime? {
        return scheduledTime
    }

    fun setScheduledTime(loc : LocalDateTime?){
        scheduledTime = loc
    }

    fun getPizzas(): MutableList<Pizza> {
        return pizzas
    }


    fun getOrderTime(): LocalDateTime {
        return orderTime
    }

    fun getComment(): String {
        return comment
    }

    fun setComment(com : String){
        comment = com
    }

    fun getPrice(): Double {
        return price
    }

    fun getId(): UUID {
        return id
    }

    fun addPizza(pizza: Pizza) {
        pizzas.add(pizza)
        if (scheduledTime == null) {
            scheduledTime = orderTime.plusMinutes(30L * pizzas.size)
        }
        recalculatePrice()
    }

    fun printOrder() {
        recalculatePrice()
        println("\n=== Заказ #${id} ===")
        println("Время заказа: ${formatDateTime(orderTime)}")
        println("Время доставки: ${formatDateTime(scheduledTime!!)}")
        if (comment.isNotEmpty()) {
            println("Комментарий: $comment")
        }
        println("\nСостав заказа:")
        pizzas.forEachIndexed { index, pizza ->
            println("\nПицца #${index + 1}:")
            pizza.printPizza()
        }
        println("\nОбщая стоимость: $price руб.")
        println("==================")
    }

    override fun recalculatePrice() {
        price = pizzas.sumOf { it.price }
    }

    private fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}