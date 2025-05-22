package org.example.pizza

class Pizza(private var dough: Dough, private var side: Side, private val size: Sizes) : PriceManage, Element() {
    override var name: String = "No name"
    override var price: Double = 0.0
    private var fullOrNot: Boolean = true
    private var pieces: MutableList<PizzaPiece> = mutableListOf()
    private var forbiddenSide: Side? = null;

    init {
        for (i in 0 until size.pieceCount) {
            pieces.add(PizzaPiece())
        }
        recalculatePrice()
    }

    constructor(
        dough: Dough,
        side: Side,
        toppings: List<Topping>,
        name: String,
        forbiddenSide: Side,
        size: Sizes = Sizes.MED
    ) : this(dough, side, size) {
        this.name = name;
        this.forbiddenSide = forbiddenSide;
        for (piece in pieces) {
            for (topping in toppings) {
                piece.addTopping(topping);
            }
        }
        recalculatePrice()
    }

    fun doubleAllToppings() {
        pieces.map { it.duplicateToppings() }
        recalculatePrice()
    }

    fun toppingHere(name: String): Boolean {
        pieces.forEach { piece ->
            if (piece.toppingHere(name)) {
                return true
            }
        }
        return false
    }

    fun changeDough(pizzaDough: Dough) {
        dough = pizzaDough
        recalculatePrice()
    }

    fun changeSide(pizzaSide: Side) {
        var flag = true
        /*for(side in pieces){
            if()
        }*/
        side = pizzaSide
        recalculatePrice()
    }

    fun setAllPizza(topping: Topping) {
        if (topping in side.getToppings()) {
            for (piece in pieces) {
                piece.addTopping(topping)
            }
            recalculatePrice()
        } else {
            println("Запрещенная начинка для этой пиццы")
        }
    }

    fun setLeftPartOfPizza(topping: Topping) {
        fullOrNot = false
        if (topping in side.getToppings()) {
            for (pieceIndex in 0 until size.pieceCount / 2) {
                pieces[pieceIndex].addTopping(topping)
            }
            recalculatePrice()
        }
        else{
            println("Запрещенная начинка для этой пиццы")
        }

    }

    fun setRightPartOfPizza(topping: Topping) {
        fullOrNot = false
        if (topping in side.getToppings()) {
            for (pieceIndex in size.pieceCount / 2 until size.pieceCount) {
                pieces[pieceIndex].addTopping(topping)
            }
            recalculatePrice()
        }
        else{
            println("Запрещенная начинка для этой пиццы")
        }
    }

    fun deleteFromAllPizza(topping: Topping) {
        for (piece in pieces) {
            piece.deleteTopping(topping)
        }
        recalculatePrice()
    }

    fun deleteFromLeftPartOfPizza(topping: Topping) {
        fullOrNot = false
        for (pieceIndex in 0 until size.pieceCount / 2) {
            pieces[pieceIndex].deleteTopping(topping)
        }
        recalculatePrice()
    }

    fun deleteFromRightPartOfPizza(topping: Topping) {
        fullOrNot = false
        for (pieceIndex in size.pieceCount / 2 until size.pieceCount) {
            pieces[pieceIndex].deleteTopping(topping)
        }
        recalculatePrice()
    }

    fun printPizza() {
        println("\nПицца: $name")
        println("Размер: ${size.russianName}")
        println("Тесто: ${dough.name} (${dough.price})")
        println("Бортик: ${side.name} (${side.price})")
        if (!fullOrNot) {
            println("\nЛевая половина:")
            pieces[0].getToppings().forEach { println("- ${it.name} (${it.price})") }
            println("\nПравая половина:")
            pieces[pieces.size - 1].getToppings().forEach { println("- ${it.name} (${it.price})") }
        } else {
            println("\nНачинки:")
            pieces[0].getToppings().forEach { println("- ${it.name} (${it.price})") }
        }
        println("\nСтоимость пиццы: $price")
    }

    override fun recalculatePrice() {
        price = 0.0
        for (piece in pieces) {
            price += piece.getPrice()
        }
        price += dough.price
        price += side.price

        price = when (size) {
            Sizes.SMALL -> (price) * 0.8
            Sizes.MED -> price
            Sizes.BIG -> (price) * 1.2
        }
    }

    constructor(other: Pizza, newSize: Sizes) : this(other.dough, other.side, newSize) {
        this.name = other.name
        this.fullOrNot = other.fullOrNot

        if (other.fullOrNot) {

            other.pieces[0].getToppings().forEach { setAllPizza(it) }
        } else {

            val oldLeftCount = other.size.pieceCount / 2
            val newLeftCount = this.size.pieceCount / 2

            if (other.pieces.isNotEmpty() && other.pieces[0].getToppings().isNotEmpty()) {
                for (i in 0 until newLeftCount) {
                    other.pieces[0].getToppings().forEach { topping ->
                        pieces[i].addTopping(topping)
                    }
                }
            }

            if (other.pieces.size > oldLeftCount &&
                other.pieces[oldLeftCount].getToppings().isNotEmpty()
            ) {
                for (i in newLeftCount until size.pieceCount) {
                    other.pieces[oldLeftCount].getToppings().forEach { topping ->
                        pieces[i].addTopping(topping)
                    }
                }
            }
        }
        recalculatePrice()
    }

    fun isWhole(): Boolean {
        return fullOrNot
    }

    fun getToppings(): List<Topping> {
        return if (pieces.isNotEmpty()) pieces[0].getToppings() else emptyList()
    }
}