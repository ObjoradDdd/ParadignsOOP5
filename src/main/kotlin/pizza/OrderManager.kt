package org.example.pizza

import org.example.pizza.Manager.printAllToppings
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

object OrderManager : PizzaMake {
    private val orders: MutableList<Order> = mutableListOf()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    fun start() {
        var running = true
        while (running) {
            println("\nУправление заказами:")
            println("1 - Создать новый заказ")
            println("2 - Просмотреть все заказы")
            println("3 - Найти заказ по номеру")
            println("4 - Найти заказы по дате")
            println("0 - Вернуться в главное меню")

            when (readlnOrNull()?.toIntOrNull() ?: -1) {
                0 -> running = false
                1 -> makeNewOrder()
                2 -> showAllOrders()
                3 -> findOrderById()
                4 -> findOrdersByDate()
                else -> println("Неверный ввод")
            }
        }
    }

    private fun makeNewOrder() {
        println("\nСоздание нового заказа")
        val order = Order()
        orders.add(order)

        while (true) {
            println("\n1 - Добавить существующую пиццу")
            println("2 - Создать новую пиццу")
            println("3 - Завершить формирование заказа")
            println("0 - Отменить заказ")

            when (readlnOrNull()?.toIntOrNull() ?: -1) {
                0 -> return
                1 -> addExistingPizza(order)
                2 -> {
                    createPizza()
                }
                3 -> {
                    if (order.getPizzas().isEmpty()) {
                        println("Заказ должен содержать хотя бы одну пиццу")
                        continue
                    }
                    finalizeOrder(order)
                    return
                }
                else -> println("Неверный ввод")
            }
        }
    }

    private fun finalizeOrder(order: Order) {
        println("\nВведите комментарий к заказу (или нажмите Enter для пропуска):")
        order.setComment(readlnOrNull() ?: "")

        println("Указать время доставки? (да/нет)")
        val setDeliveryTime = readlnOrNull()?.equals("да", ignoreCase = true) ?: false


        if (setDeliveryTime) {
            println("Введите дату и время доставки (формат: YYYY-MM-DD HH:MM:SS):")

            val currentTime = LocalDateTime.now(ZoneOffset.UTC).plusHours(7)

            try {
                val deliveryTime = LocalDateTime.parse(readlnOrNull() ?: "", dateFormatter)
                if (deliveryTime.isBefore(currentTime.plusMinutes(30L * order.getPizzas().size))) {
                    println("Время доставки должно быть не менее ${30 * order.getPizzas().size} минут от текущего времени")
                    order.setScheduledTime(currentTime.plusMinutes(30L * order.getPizzas().size))
                } else {
                    order.setScheduledTime(deliveryTime)
                }
            } catch (e: DateTimeParseException) {
                println("Неверный формат времени. Установлено автоматическое время доставки")
                order.setScheduledTime(currentTime.plusMinutes(30L * order.getPizzas().size))
            }
        }

        println("\nЗаказ успешно создан:")
        order.printOrder()
    }

    private fun addExistingPizza(order: Order) {
        val readySets = Manager.getReadySets()
        if (readySets.isEmpty()) {
            println("Нет доступных готовых пицц")
            return
        }

        println("\nВыберите пиццу:")
        readySets.forEachIndexed { index, pizza ->
            println("${index + 1} - ${pizza.name}")
        }


        val pizzaIndex = readlnOrNull()?.toIntOrNull()?.minus(1) ?: return
        if (pizzaIndex in readySets.indices) {
            val size = chooseSize()

            println("\n2 - в 2 раза больше начинки")
            println("1 - оставить все как есть")
            do{
                val toDoubleState : Int = (readlnOrNull()?:"-1").toInt()
                if (toDoubleState == 2){
                    val pizza = Pizza(Manager.getReadySets()[pizzaIndex], size)
                    pizza.doubleAllToppings()
                    order.addPizza(pizza)
                    break
                }
                else if (toDoubleState == 1){
                    order.addPizza(Pizza(Manager.getReadySets()[pizzaIndex], size))
                    break
                }
                else{
                    println("Неверрный ввод, введите 1 или 2")
                }
            }while(true)

            println("Пицца добавлена в заказ")
        } else {
            println("Неверный номер пиццы")
        }
    }

    private fun chooseSize(): Sizes {
        var sizeChoice: Int

        do {
            println("\nВыберите размер пиццы:")
            println("1 - Маленькая (${Sizes.SMALL.pieceCount})")
            println("2 - Средняя (${Sizes.MED.pieceCount})")
            println("3 - Большая (${Sizes.BIG.pieceCount})")

            sizeChoice = readlnOrNull()?.toIntOrNull() ?: 0

            when (sizeChoice) {
                1 -> return Sizes.SMALL
                2 -> return Sizes.MED
                3 -> return Sizes.BIG
                else -> println("Неверный выбор. Пожалуйста, выберите 1, 2 или 3")
            }
        } while (true)
    }

    private fun showAllOrders() {
        if (orders.isEmpty()) {
            println("\nНет активных заказов")
            return
        }

        println("\nСписок всех заказов:")
        orders.sortedBy { it.getScheduledTime() }.forEach { it.printOrder() }
    }

    private fun findOrderById() {
        println("\nВведите номер заказа:")
        val orderIdStr = readlnOrNull() ?: return

        try {
            val orderId = UUID.fromString(orderIdStr)
            val order = orders.find { it.getId() == orderId }
            if (order != null) {
                order.printOrder()
            } else {
                println("Заказ не найден")
            }
        } catch (e: IllegalArgumentException) {
            println("Неверный формат номера заказа")
        }
    }

    private fun findOrdersByDate() {
        println("\nВведите дату (формат: YYYY-MM-DD):")
        val dateStr = readlnOrNull() ?: return

        try {
            val searchDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val foundOrders = orders.filter {
                it.getScheduledTime()?.toLocalDate() == searchDate
            }

            if (foundOrders.isEmpty()) {
                println("Заказов на эту дату не найдено")
            } else {
                println("\nЗаказы на $dateStr:")
                foundOrders.sortedBy { it.getScheduledTime() }.forEach { it.printOrder() }
            }
        } catch (e: DateTimeParseException) {
            println("Неверный формат даты")
        }
    }

    override fun createPizza() {
        println("\nСоздание пиццы для заказа:")
        val size = chooseSize()
        val newPizza = Pizza(chooseDough(), chooseSide(), size)
        orders.last().addPizza(newPizza)
        chooseTopping(orders.size - 1)
        println("\nДобавленная пицца:")
        orders.last().printOrder()
    }

    override fun chooseSide(): Side {
        var sideIndex: Int = -1

        println("\nВыберите бортик:")
        val sides = Manager.getSides()
        for (d in 0 until sides.size) {
            println("${d + 1} - ${sides[d].name} (${sides[d].price} руб.)")
        }
        while (sideIndex < 1 || sideIndex > sides.size) {
            sideIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (sideIndex < 1 || sideIndex > sides.size) {
                println("Неверный ввод")
            }
        }
        return sides[sideIndex - 1]
    }

    override fun addToppingToWhole(pizzaIndex: Int) {
        printAllToppings()
        var toppingIndex: Int
        val toppings = Manager.getToppings()
        do {
            toppingIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (toppingIndex in 1..toppings.size) {
                orders.last().getPizzas().last().setAllPizza(toppings[toppingIndex - 1])
                println("Начинка добавлена на всю пиццу")
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)
    }

    override fun addToppingToHalves(pizzaIndex: Int) {
        println("\nДобавление начинок на левую половину:")
        println("1 - Выбрать отдельные начинки")
        println("2 - Скопировать начинки с существующей пиццы")
        println("0 - Пропустить")

        when (readlnOrNull()?.toIntOrNull() ?: -1) {
            1 -> addIndividualToppingsToLeft(orders.last().getPizzas().last())
            2 -> copyToppingsFromExistingPizza(orders.last().getPizzas().last(), true)
            0 -> {}
            else -> println("Неверный ввод")
        }

        println("\nДобавление начинок на правую половину:")
        println("1 - Выбрать отдельные начинки")
        println("2 - Скопировать начинки с существующей пиццы")
        println("0 - Пропустить")

        when (readlnOrNull()?.toIntOrNull() ?: -1) {
            1 -> addIndividualToppingsToRight(orders.last().getPizzas().last())
            2 -> copyToppingsFromExistingPizza(orders.last().getPizzas().last(), false)
            0 -> {}
            else -> println("Неверный ввод")
        }
    }

    override fun chooseDough(): Dough {
        var doughIndex: Int = -1

        println("\nВыберите тесто:")
        val doughs = Manager.getDoughs()
        for (d in 0 until doughs.size) {
            println("${d + 1} - ${doughs[d].name} (${doughs[d].price} руб.)")
        }
        while (doughIndex < 1 || doughIndex > doughs.size) {
            doughIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (doughIndex < 1 || doughIndex > doughs.size) {
                println("Неверный ввод")
            }
        }
        return doughs[doughIndex - 1]
    }

    private fun addIndividualToppingsToLeft(pizza: Pizza) {
        printAllToppings()
        var toppingIndex: Int
        val toppings = Manager.getToppings()
        do {
            toppingIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (toppingIndex in 1..toppings.size) {
                pizza.setLeftPartOfPizza(toppings[toppingIndex - 1])
                println("Начинка добавлена на левую половину")
                pizza.printPizza()
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)
    }

    private fun addIndividualToppingsToRight(pizza: Pizza) {
        printAllToppings()
        var toppingIndex: Int
        val toppings = Manager.getToppings()
        do {
            toppingIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (toppingIndex in 1..toppings.size) {
                pizza.setRightPartOfPizza(toppings[toppingIndex - 1])
                println("Начинка добавлена на правую половину")
                pizza.printPizza()
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)
    }

    private fun copyToppingsFromExistingPizza(targetPizza: Pizza, isLeftHalf: Boolean) {
        val readySets = Manager.getReadySets()
        val wholePizzas = readySets.filter { it.isWhole() }

        if (wholePizzas.isEmpty()) {
            println("\nНет доступных пицц с полной начинкой для копирования")
            return
        }

        println("\nВыберите пиццу, с которой хотите скопировать начинки:")
        wholePizzas.forEachIndexed { index, pizza ->
            println("${index + 1} - ${pizza.name}")
            println("Начинки: ${pizza.getToppings().joinToString { it.name }}")
        }

        val pizzaIndex = readlnOrNull()?.toIntOrNull()?.minus(1) ?: return
        if (pizzaIndex in wholePizzas.indices) {
            val sourcePizza = wholePizzas[pizzaIndex]
            sourcePizza.getToppings().forEach { topping ->
                if (isLeftHalf) {
                    targetPizza.setLeftPartOfPizza(topping)
                } else {
                    targetPizza.setRightPartOfPizza(topping)
                }
            }
            println("Начинки успешно скопированы на ${if (isLeftHalf) "левую" else "правую"} половину")
            targetPizza.printPizza()
        } else {
            println("Неверный номер пиццы")
        }
    }
}