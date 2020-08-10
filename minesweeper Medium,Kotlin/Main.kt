package minesweeper

import kotlin.random.Random
import java.util.*

open class Node(val position: Int) {
    var isConcealed = false
    var marked = false
}
class Mine(position: Int) : Node(position){
    override fun toString(): String {
        if(marked) return "*" else return "."
    }
}

class Fine(position: Int) : Node(position) {
    var state: Int = 0
    override fun toString(): String {
        if (marked) return "*"
        else if (state == 0) {
            return "."
        } else {
            return state.toString()
        }
    }
}
fun MutableMap<Int, Node>.checkgame() : Boolean{
    var doWin = true
    for (entry in this){
        if(entry.value is Fine && entry.value.marked){doWin=false}
        else if (entry.value is Mine && !entry.value.marked){doWin=false}
    }
    return doWin
}
fun MutableMap<Int, Node>.calculate(node: Node) {
    if (node is Fine) {
        if (node.position == 1) {
            if(this[node.position + 1] is Mine){node.state++}
            if(this[node.position + 9] is Mine){node.state++}
            if(this[node.position + 10] is Mine){node.state++}
        } else if (node.position == 9) {
            if(this[node.position - 1] is Mine){node.state++}
            if(this[node.position + 9] is Mine){node.state++}
            if(this[node.position + 8] is Mine){node.state++}
        } else if (node.position == 73) {
            if(this[node.position + 1] is Mine){node.state++}
            if(this[node.position - 9] is Mine){node.state++}
            if(this[node.position - 8] is Mine){node.state++}
        } else if (node.position == 81) {
            if(this[node.position - 1] is Mine){node.state++}
            if(this[node.position - 9] is Mine){node.state++}
            if(this[node.position - 10] is Mine){node.state++}
        } else if (node.position < 10) {
            if(this[node.position - 1] is Mine){node.state++}
            if(this[node.position + 1] is Mine){node.state++}
            if(this[node.position + 9] is Mine){node.state++}
            if(this[node.position + 8] is Mine){node.state++}
            if(this[node.position + 10] is Mine){node.state++}
        } else if (node.position > 72) {
            if(this[node.position - 1] is Mine){node.state++}
            if(this[node.position + 1] is Mine){node.state++}
            if(this[node.position - 9] is Mine){node.state++}
            if(this[node.position - 8] is Mine){node.state++}
            if(this[node.position - 10] is Mine){node.state++}
        } else if (node.position % 9 == 1) {
            if(this[node.position + 1] is Mine){node.state++}
            if(this[node.position - 9] is Mine){node.state++}
            if(this[node.position - 8] is Mine){node.state++}
            if(this[node.position + 9] is Mine){node.state++}
            if(this[node.position + 10] is Mine){node.state++}
        } else if (node.position % 9 == 0) {
            if(this[node.position - 1] is Mine){node.state++}
            if(this[node.position - 9] is Mine){node.state++}
            if(this[node.position + 9] is Mine){node.state++}
            if(this[node.position - 10] is Mine){node.state++}
            if(this[node.position + 8] is Mine){node.state++}
        } else {
            if(this[node.position + 1] is Mine){node.state++}
            if(this[node.position - 1] is Mine){node.state++}
            if(this[node.position + 9] is Mine){node.state++}
            if(this[node.position + 10] is Mine){node.state++}
            if(this[node.position + 8] is Mine){node.state++}
            if(this[node.position - 9] is Mine){node.state++}
            if(this[node.position - 8] is Mine){node.state++}
            if(this[node.position - 10] is Mine){node.state++}
        }

    }
}
fun main() {
    val scanner: Scanner = Scanner(System.`in`)
    val map : MutableMap<Int,Node> = mutableMapOf()
    println("How many mines do you want on the field?")
    val mineAmount = scanner.nextInt()
    val nextValues = List(mineAmount) { 0 }.toMutableList()
    setMine(nextValues)
    makeMap(nextValues, map)
    checkMap(map)
    printMap(map)
    while (true) {
        ask()
        val node = map[(scanner.nextInt() - 1) * 9 + scanner.nextInt()]!!
        if (node is Mine) {
            mark (node)
            printMap(map)
        }
        else if (node is Fine){
            if(node.state == 0){
                mark(node)
                printMap(map)
            }
            else {
                println("There is a number here!")
            }
        }
        if(map.checkgame()){break }
    }
    println("Congratulations! You found all the mines!")
}

private fun makeMap(nextValues: MutableList<Int>, map: MutableMap<Int, Node>) {
    for (i in 1..9) {
        for (j in 1..9) {
            val position = (i - 1) * 9 + j
            if (nextValues.contains(position)) {
                map[position] = Mine(position)
            } else {
                map[position] = Fine(position)
            }
        }
    }
}

private fun setMine(nextValues: MutableList<Int>) {
    for (index in nextValues.indices) {
        var x = Random.nextInt(1, 82)
        while (nextValues.contains(x)) {
            if (x == 81) x = 1 else x += 1
        }
        nextValues[index] = x
    }
}

private fun checkMap(map: MutableMap<Int, Node>) {
    for (i in 1..9) {
        for (j in 1..9) {
            val position = (i - 1) * 9 + j
            map.calculate(map[position]!!)
        }
    }
}

private fun printMap(map: MutableMap<Int, Node>) {
    println(" │123456789│\n" +
            "—│—————————│")
    for (i in 1..9) {
        print("$i│")
        for (j in 1..9) {
            val position = (i - 1) * 9 + j
            print(map[position]!!.toString())
            if (j == 9){
                println("│")}
        }
        if(i==9) println("—│—————————│")
    }
}
fun ask (){
    println("Set/delete mines marks (x and y coordinates): ")
}
fun mark(node : Node){
    node.marked == !node.marked
}
