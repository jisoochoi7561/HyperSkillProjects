package machine

import java.util.*
val sc = Scanner(System.`in`)
interface Stuff {
    val unit: String
    val name: String
    var amount: Int
}
interface Coffee {
    val water: Int
    val milk : Int
    val coffeeBeans: Int
    val cost : Int
    val name : String
}
class espresso : Coffee {
    override val water: Int = 250
    override val milk: Int = 0
    override val coffeeBeans: Int = 16
    override val cost: Int = 4
    override val name: String = "espresso"
}
class latte : Coffee {
    override val water: Int =  350
    override val milk: Int =75
    override val coffeeBeans: Int = 20
    override val cost: Int = 7
    override val name: String = "latte"
}
class cappuccino : Coffee {
    override val water: Int = 200
    override val milk: Int = 100
    override val coffeeBeans: Int =  12
    override val cost: Int = 6
    override val name: String = "cappuccino"
}
object coffeeMachine {
    override fun toString(): String {
        val string = "The coffee machine has:\n" +
                "${water.amount} of ${water.name}\n" +
                "${milk.amount}  of ${milk.name}\n" +
                "${coffeeBeans.amount} of ${coffeeBeans.name}\n" +
                "${disposableCups.amount} of ${disposableCups.name}\n" +
                "${money.amount} of ${money.name}\n"
        return string
    }
    object water : Stuff {
        override val unit: String = "ml"
        override val name: String = "water"
        override var amount: Int = 400
    }
    object milk : Stuff {
        override val unit: String = "ml"
        override val name: String = "milk"
        override var amount: Int = 540
    }
    object coffeeBeans : Stuff {
        override val unit: String = "grams"
        override val name: String = "coffee beans"
        override var amount: Int = 120
    }
    object disposableCups : Stuff {
        override val unit: String = "cups"
        override val name: String = "disposable cups"
        override var amount: Int = 9
    }
    object money : Stuff{
        override val unit: String = "$"
        override val name = "money"
        override var amount = 550
    }

    fun availableCoffee(coffee: Coffee): Boolean {
        return coffee.milk < milk.amount && coffee.water < water.amount && coffee.coffeeBeans < coffeeBeans.amount && disposableCups.amount > 0
    }
}
fun buyCoffee(coffee : Coffee){
    if(coffeeMachine.availableCoffee(coffee)){
    coffeeMachine.milk.amount-=coffee.milk
    coffeeMachine.water.amount-=coffee.water
    coffeeMachine.coffeeBeans.amount-=coffee.coffeeBeans
    coffeeMachine.disposableCups.amount--
    coffeeMachine.money.amount+=coffee.cost
    println("I have enough resources, making you a coffee!")}
    else{
        println("Sorry,no resources")
    }
}
fun buy(){
    println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
    when(sc.next()){
        "1"-> buyCoffee(espresso())
        "2"->buyCoffee(latte())
        "3"->buyCoffee(cappuccino())
        "back"->{}
    }
}
fun fill(){
    coffeeMachine.water.apply{
        println("Write how many ${this.unit} of ${this.name} do you want to add:")
        this.amount+=sc.nextInt()
    }
    coffeeMachine.milk.apply{
        println("Write how many ${this.unit} of ${this.name} do you want to add:")
        this.amount+=sc.nextInt()
    }
    coffeeMachine.coffeeBeans.apply{
        println("Write how many ${this.unit} of ${this.name} do you want to add:")
        this.amount+=sc.nextInt()
    }
    coffeeMachine.disposableCups.apply{
        println("Write how many ${this.unit} of ${this.name} do you want to add:")
        this.amount+=sc.nextInt()
    }
}

fun take() {
    coffeeMachine.money.apply {
        println("I gave you $${this.amount}")
    }.also { it.amount = 0 }

}

fun main() {
    loop@ while (true) {
        println("Write action (buy, fill, take, remaining, exit): ")
        val command = sc.next()
        when (command) {
            "buy" -> {
                buy()
            }
            "fill" -> {
                fill()
            }
            "take" -> {
                take()
            }
            "remaining" -> {
                println(coffeeMachine)
            }
            "exit" -> {
                break@loop
            }
        }

    }

}
