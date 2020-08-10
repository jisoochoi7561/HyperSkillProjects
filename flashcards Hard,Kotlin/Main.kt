package flashcards

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val term = scanner.nextLine()
    val definition = scanner.nextLine()
    val answer = scanner.nextLine()
    if (definition == answer) {
        println("Your answer is right!")
    } else {
        println("Your answer is wrong..")
    }
}
