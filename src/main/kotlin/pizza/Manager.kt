package org.example.pizza

object Manager : PizzaMake {
    private const val DEFAULT_DOUGH_NAME = "классическое"
    private const val DEFAULT_SIDE_NAME = "сырный"
    private const val MAX_DOUGH_PRICE = 100.0

    private val toppings: MutableList<Topping> = mutableListOf(Topping("сыр", 10.0), Topping("ветчина", 30.0))
    private val sides: MutableList<Side> = mutableListOf(Side(DEFAULT_SIDE_NAME, 20.0), Side("кунжут", 20.0))
    private val doughs: MutableList<Dough> = mutableListOf(Dough(DEFAULT_DOUGH_NAME, 100.0))
    private val readySets: MutableList<Pizza> = mutableListOf(
        Pizza(doughs[0], sides[0], listOf(toppings[0], toppings[1]), "СырВетчина", sides[1])
    )

    fun getReadySets(): MutableList<Pizza> {
        return readySets
    }

    fun getSides(): MutableList<Side> {
        return sides
    }


    fun getToppings(): MutableList<Topping> {
        return toppings
    }


    fun getDoughs(): MutableList<Dough> {
        return doughs
    }

    fun start() {
        var running = true
        val orderManager = OrderManager
        while (running) {
            println("\nГлавное меню:")
            println("1 - Создать пиццу")
            println("2 - Добавить новую начинку")
            println("3 - Поменять существующую начинку")
            println("4 - Добавить новое тесто")
            println("5 - Добавить новый бортик")
            println("6 - Редактировать существующую пиццу")
            println("7 - Удалить пиццу")
            println("8 - Просмотреть доступные начинки")
            println("9 - Просмотреть доступные виды теста")
            println("10 - Просмотреть доступные бортики")
            println("11 - Просмотреть все пиццы")
            println("12 - Управление заказами")
            println("0 - Выход")

            when (readlnOrNull()?.toIntOrNull() ?: -1) {
                0 -> running = false
                1 -> createPizza()
                2 -> addNewTopping()
                3 -> changeTopping()
                4 -> addNewDough()
                5 -> addNewSide()
                6 -> selectAndEditPizza()
                7 -> deletePizza()
                8 -> printAvailableToppings()
                9 -> printAvailableDoughs()
                10 -> printAvailableSides()
                11 -> printAllPizzas()
                12 -> orderManager.start()
                else -> println("Неверный ввод")
            }
        }
    }

    override fun chooseDough(): Dough {
        var doughIndex: Int = -1

        println("\nВыберите тесто:")
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

    override fun chooseSide(): Side {
        var sideIndex: Int = -1

        println("\nВыберите бортик:")
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
        do {
            toppingIndex = (readlnOrNull() ?: "-1").toIntOrNull() ?: -1
            if (toppingIndex in 1..toppings.size) {
                readySets[pizzaIndex].setAllPizza(toppings[toppingIndex - 1])
                println("Начинка добавлена на всю пиццу")
                readySets[pizzaIndex].printPizza()
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)
    }

    private fun changeTopping() {
        var toppingIndex: Int
        do {
            printAllToppings()
            toppingIndex = ((readlnOrNull() ?: "-1").toIntOrNull() ?: -1)
            if (toppingIndex in 1..toppings.size) {
                var changeState: Int
                do {
                    println("\n1 - Поменять название")
                    println("2 - Поменять цену")
                    println("0 - Назад")

                    changeState = (readlnOrNull() ?: "-1").toIntOrNull() ?: -1
                    if (changeState in 1..2) {
                        when (changeState) {
                            1 -> {
                                println("\nВведите новое имя:")
                                toppings[toppingIndex - 1].setElementName(readlnOrNull() ?: "no name")
                                println("\nНазвание изменено")
                            }
                            2 -> {
                                println("\n Введите новую цену:")
                                toppings[toppingIndex - 1].setElementPrice(
                                    (readlnOrNull() ?: "-1").toDoubleOrNull() ?: -1.0
                                )
                                println("\nЦена изменена")
                            }
                        }
                    } else if (changeState != 0) {
                        println("Неверный ввод")
                    }
                } while (changeState != 0)
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)
    }

    override fun addToppingToHalves(pizzaIndex: Int) {
        println("\nДобавление начинок на левую половину:")
        printAllToppings()
        var toppingIndex: Int
        do {
            toppingIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (toppingIndex in 1..toppings.size) {
                readySets[pizzaIndex].setLeftPartOfPizza(toppings[toppingIndex - 1])
                println("Начинка добавлена на левую половину")
                readySets[pizzaIndex].printPizza()
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)

        println("\nДобавление начинок на правую половину:")
        printAllToppings()
        do {
            toppingIndex = (readlnOrNull() ?: "0").toIntOrNull() ?: -1
            if (toppingIndex in 1..toppings.size) {
                readySets[pizzaIndex].setRightPartOfPizza(toppings[toppingIndex - 1])
                println("Начинка добавлена на правую половину")
                readySets[pizzaIndex].printPizza()
            } else if (toppingIndex != 0) {
                println("Неверный ввод")
            }
        } while (toppingIndex != 0)
    }

    private fun deleteTopping(pizzaIndex: Int) {
        var continueDeleting = true
        while (continueDeleting) {
            println("\nВыберите действие:")
            println("1 - Удалить начинку со всей пиццы")
            println("2 - Удалить начинку с левой половины")
            println("3 - Удалить начинку с правой половины")
            println("0 - Вернуться назад")

            when (readlnOrNull()?.toIntOrNull() ?: -1) {
                0 -> continueDeleting = false
                1 -> deleteFromWholeByName(pizzaIndex)
                2 -> deleteFromLeftByName(pizzaIndex)
                3 -> deleteFromRightByName(pizzaIndex)
                else -> println("Неверный ввод")
            }
        }
    }

    fun printAllToppings() {
        println("\nДоступные начинки:")
        for (d in 0 until toppings.size) {
            println("${d + 1} - ${toppings[d].name} (${toppings[d].price} руб.)")
        }
        println("0 - Завершить")
    }

    private fun addNewTopping() {
        println("\nВведите название начинки:")
        val name = readlnOrNull()?.trim() ?: return

        if (toppings.any { it.name.equals(name, ignoreCase = true) }) {
            println("Начинка с таким названием уже существует")
            return
        }

        println("Введите цену начинки:")
        val price = readlnOrNull()?.toDoubleOrNull()
        if (price != null && price > 0) {
            toppings.add(Topping(name, price))
            println("Начинка '$name' успешно добавлена")
        } else {
            println("Неверная цена")
        }
    }

    private fun addNewDough() {
        println("\nВведите название теста:")
        val name = readlnOrNull()?.trim() ?: return

        if (doughs.any { it.name.equals(name, ignoreCase = true) }) {
            println("Тесто с таким названием уже существует")
            return
        }

        println("Введите цену теста (максимум $MAX_DOUGH_PRICE руб.):")
        val price = readlnOrNull()?.toDoubleOrNull()

        when {
            price == null -> println("Неверная цена")
            price <= 0 -> println("Цена должна быть больше 0")
            price > MAX_DOUGH_PRICE -> println("Цена теста не может быть больше $MAX_DOUGH_PRICE руб.")
            else -> {
                doughs.add(Dough(name, price))
                println("Тесто '$name' успешно добавлено")
            }
        }
    }

    private fun addNewSide() {
        println("\nВведите название бортика:")
        val name = readlnOrNull()?.trim() ?: return

        if (sides.any { it.name.equals(name, ignoreCase = true) }) {
            println("Бортик с таким названием уже существует")
            return
        }

        println("Введите цену бортика:")
        val price = readlnOrNull()?.toDoubleOrNull()
        if (price != null && price > 0) {
            sides.add(Side(name, price))
            println("Бортик '$name' успешно добавлен")
        } else {
            println("Неверная цена")
        }

        printToppings()
        println("Выберите начинки разрешенные для этой пиццы")

        while(true){
            val numberOfTopping = readlnOrNull()?.toIntOrNull()

            if (numberOfTopping != 0 && numberOfTopping != null) {
                sides.last().addAvailableTopping(toppings[numberOfTopping - 1])
            }
            else if(numberOfTopping == 0){
                break
            }
            else{
                println("Неверный ввод")
            }
        }


    }

    private fun selectAndEditPizza() {
        if (readySets.isEmpty()) {
            println("\nНет доступных пицц для редактирования")
            return
        }

        println("\nВыберите пиццу для редактирования:")
        readySets.forEachIndexed { index, pizza ->
            println("${index + 1} - ${pizza.name}")
        }

        val index = (readlnOrNull()?.toIntOrNull() ?: -1) - 1
        if (index in readySets.indices) {
            editPizza(index)
        } else {
            println("Неверный номер пиццы")
        }
    }

    private fun editPizza(pizzaIndex: Int) {
        var editing = true
        while (editing) {
            println("\nРедактирование пиццы:")
            println("1 - Изменить тесто")
            println("2 - Изменить бортик")
            println("3 - Изменить начинки")
            println("4 - Изменить название")
            println("0 - Вернуться в главное меню")

            when (readlnOrNull()?.toIntOrNull() ?: -1) {
                0 -> editing = false
                1 -> {
                    readySets[pizzaIndex].changeDough(chooseDough())
                    readySets[pizzaIndex].printPizza()
                }

                2 -> {
                    readySets[pizzaIndex].changeSide(chooseSide())
                    readySets[pizzaIndex].printPizza()
                }

                3 -> {
                    var toppingEdit = true
                    while (toppingEdit) {
                        println("\nУправление начинками:")
                        println("1 - Добавить начинку")
                        println("2 - Удалить начинку")
                        println("0 - Назад")

                        when (readlnOrNull()?.toIntOrNull() ?: -1) {
                            0 -> toppingEdit = false
                            1 -> chooseTopping(pizzaIndex)
                            2 -> deleteTopping(pizzaIndex)
                            else -> println("Неверный ввод")
                        }
                    }
                }

                4 -> {
                    println("Введите новое название пиццы:")
                    val newName = readlnOrNull()?.trim() ?: continue
                    if (newName.isNotEmpty()) {
                        readySets[pizzaIndex].name = newName
                        println("Название пиццы изменено на '$newName'")
                    } else {
                        println("Название не может быть пустым")
                    }
                }

                else -> println("Неверный ввод")
            }
        }
    }

    private fun deletePizza() {
        if (readySets.isEmpty()) {
            println("\nНет доступных пицц для удаления")
            return
        }

        println("\nВыберите пиццу для удаления:")
        readySets.forEachIndexed { index, pizza ->
            println("${index + 1} - ${pizza.name}")
        }

        val index = (readlnOrNull()?.toIntOrNull() ?: -1) - 1
        if (index in readySets.indices) {
            val deletedPizza = readySets.removeAt(index)
            println("Пицца '${deletedPizza.name}' удалена")
        } else {
            println("Неверный номер пиццы")
        }
    }

    override fun createPizza() {
        println("\nВведите название для новой пиццы:")
        val pizzaName = readlnOrNull()?.trim() ?: "Без названия"
        val newPizza = Pizza(chooseDough(), chooseSide(), Sizes.MED)
        newPizza.name = pizzaName
        readySets.add(newPizza)
        chooseTopping(readySets.size - 1)
        println("\nСозданная пицца:")
        readySets.last().printPizza()
    }

    private fun deleteFromWholeByName(pizzaIndex: Int) {
        println("\nТекущее состояние пиццы:")
        readySets[pizzaIndex].printPizza()

        println("\nВведите название начинки для удаления:")
        val toppingName = readlnOrNull()?.trim() ?: return
        val topping = findToppingByName(toppingName)

        if (topping != null) {
            readySets[pizzaIndex].deleteFromAllPizza(topping)
            println("Начинка '$toppingName' удалена со всей пиццы")
            println("\nОбновленное состояние пиццы:")
            readySets[pizzaIndex].printPizza()
        } else {
            println("Начинка с названием '$toppingName' не найдена")
        }
    }

    private fun deleteFromLeftByName(pizzaIndex: Int) {
        println("\nТекущее состояние пиццы:")
        readySets[pizzaIndex].printPizza()

        println("\nВведите название начинки для удаления с левой половины:")
        val toppingName = readlnOrNull()?.trim() ?: return

        val topping = findToppingByName(toppingName)
        if (topping != null) {
            readySets[pizzaIndex].deleteFromLeftPartOfPizza(topping)
            println("Начинка '$toppingName' удалена с левой половины пиццы")
            println("\nОбновленное состояние пиццы:")
            readySets[pizzaIndex].printPizza()
        } else {
            println("Начинка с названием '$toppingName' не найдена")
        }
    }

    private fun deleteFromRightByName(pizzaIndex: Int) {
        println("\nТекущее состояние пиццы:")
        readySets[pizzaIndex].printPizza()

        println("\nВведите название начинки для удаления с правой половины:")
        val toppingName = readlnOrNull()?.trim() ?: return

        val topping = findToppingByName(toppingName)
        if (topping != null) {
            readySets[pizzaIndex].deleteFromRightPartOfPizza(topping)
            println("Начинка '$toppingName' удалена с правой половины пиццы")
            println("\nОбновленное состояние пиццы:")
            readySets[pizzaIndex].printPizza()
        } else {
            println("Начинка с названием '$toppingName' не найдена")
        }
    }

    private fun findToppingByName(name: String): Topping? {
        return toppings.find { it.name.equals(name, ignoreCase = true) }
    }

    private fun printAvailableDoughs() {
        if (doughs.isEmpty()) {
            println("\nНет доступных видов теста")
            return
        }
        println("\nДоступные виды теста:")
        doughs.forEach { println("${it.name} (${it.price} руб.)") }
    }

    private fun printAvailableSides() {
        if (sides.isEmpty()) {
            println("\nНет доступных бортиков")
            return
        }
        println("\nДоступные бортики:")
        sides.forEach { println("${it.name} (${it.price} руб.)") }
    }

    private fun printAvailablePizzas(name: String) {
        if (readySets.isEmpty()) {
            println("\nНет пицц c $name")
            return
        }

        println("\nПиццы с $name:")
        readySets.forEach {
            if (it.toppingHere(name)) {
                it.printPizza()
            }
        }
    }

    private fun printAllPizzas() {
        if (readySets.isEmpty()) {
            println("\nНет созданных пицц")
            return
        }

        println("\nВывод пицц:")
        println("1 - Показать все пиццы")
        println("2 - Отфильтровать по начинке")
        println("0 - Назад")

        when (readlnOrNull()?.toIntOrNull() ?: -1) {
            0 -> return
            1 -> {
                println("\nСписок всех пицц:")
                readySets.forEachIndexed { index, pizza ->
                    println("\nПицца #${index + 1}:")
                    pizza.printPizza()
                }
            }

            2 -> {
                println("\nВведите название начинки для фильтрации:")
                val toppingName = readlnOrNull()?.trim() ?: return
                printAvailablePizzas(toppingName)
            }

            else -> println("Неверный ввод")
        }
    }

    private fun printAvailableToppings() {
        if (toppings.isEmpty()) {
            println("\nНет доступных начинок")
            return
        }
        println("\nДоступные начинки:")
        toppings.forEach { println("${it.name} (${it.price} руб.)") }
    }


    private fun printToppings() {
        if (toppings.isEmpty()) {
            println("\nНет доступных начинок")
            return
        }
        println("\nДоступные начинки:")
        for(i in 0 until toppings.size){
            println("${i + 1} - ${toppings[i].name}")
        }
        println("\n0 - выход")
    }

}