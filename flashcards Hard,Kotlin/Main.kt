package flashcards

import java.io.Console
import java.util.*
import java.io.File
import kotlin.Exception

data class Card(val term: String, val definition: String,var mistake:Int)
fun MutableMap<String,Card>.findTermByDef(value : String) : String{
    for((key,card) in this){
        if(card.definition==value){return card.term}
    }
    return ""
}
fun MutableMap<String,Card>.containsDef(value : String) : Boolean{
    for((key,card) in this){
        if(card.definition==value){return true}
    }
    return false
}
val log = mutableListOf<String>()
fun log(line: String): String {
    log.add(line)
    return line
}

fun main(args: Array<String>) {

    val scanner = Scanner(System.`in`)
    val cardMap = mutableMapOf<String, Card>()
    try {
        if (args[0] == "-import") {
            importEx(args[1], cardMap)
        }
        if (args[2] == "-import") {
            importEx(args[3], cardMap)
        }

    }
    catch (e : Exception){}

    question@ while (true) {
        println(log("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"))
        when (log(scanner.nextLine())) {
            "add" -> add(scanner, cardMap)
            "remove" -> remove(scanner, cardMap)
            "exit" -> {
                println(log("Bye bye!"))
                break@question
            }
            "import" -> import(scanner, cardMap)
            "export" -> export(scanner, cardMap)
            "ask" -> ask(scanner, cardMap)
            "hardest card" -> hardestcard(cardMap)
            "reset stats" -> resetStats(cardMap)
            "log" -> log(scanner)
        }
    }
    try {
        if (args[0] == "-export") {
            exportEx(args[1], cardMap)
        }
        if (args[2] == "-export") {
            exportEx(args[3], cardMap)
        }
    }
    catch (e : Exception){}



}

private fun ask(scanner: Scanner, cardMap: MutableMap<String, Card>) {
    println(log("How many times to ask?"))
    val question = log(scanner.nextLine()).toInt()
    for (i in 1..question) {
        var rightTerm = ""
        val random = Random()
        val (term, card) = cardMap.entries.elementAt(random.nextInt(cardMap.size))
        println(log("Print the definition of \"$term\":"))
        val answer = log(scanner.nextLine())

        if (card.definition == answer) {
            println(log("Correct answer."))
        } else {
            rightTerm = cardMap.findTermByDef(answer)
            if (rightTerm != "") println(log("Wrong answer. The correct one is \"${card.definition}\", you've just written the definition of \"$rightTerm\""))
            else println(log("Wrong answer. The correct one is \"${card.definition}\"."))
            card.mistake++
        }
    }
}

private fun export(scanner: Scanner, cardMap: MutableMap<String, Card>) {
    println(log("File name:"))
    val myFile = File(log(scanner.nextLine()))
    var text = ""

    for (card in cardMap) {
        text += "${card.key}\n${card.value.definition}\n${card.value.mistake}\n"
    }
    myFile.writeText(text)
    println(log("${cardMap.size} cards have been saved."))
}
private fun exportEx(string : String, cardMap: MutableMap<String, Card>) {

    val myFile = File(log(string))
    var text = ""

    for (card in cardMap) {
        text += "${card.key}\n${card.value.definition}\n${card.value.mistake}\n"
    }
    myFile.writeText(text)
    println(log("${cardMap.size} cards have been saved."))
}

private fun import(scanner: Scanner, cardMap: MutableMap<String, Card>) {
    println(log("File name:"))
    try {
        val file = File(log(scanner.nextLine()))
        val count = add(file, cardMap)
        println(log("$count cards have been loaded."))
    } catch (e: Exception) {
        println(log("File not found."))

    }
}
private fun importEx(string : String, cardMap: MutableMap<String, Card>) {

    try {
        val file = File(log(string))
        val count = add(file, cardMap)
        println(log("$count cards have been loaded."))
    } catch (e: Exception) {
        println(log("File not found."))

    }
}

private fun remove(scanner: Scanner, cardMap: MutableMap<String, Card>) {
    println(log("The card:"))
    val term = log(scanner.nextLine())
    if (cardMap.containsKey(term)) {
        println(log("The card has been removed."))
        cardMap.remove(term)
    } else println(log("Can't remove \"$term\": there is no such card."))
}

private fun add(scanner: Scanner, cardMap: MutableMap<String, Card>) {
    println(log("The card:"))
    val term = log(scanner.nextLine())
    if (cardMap.containsKey(term)) {
        println(log("The card \"$term\" already exists."))

    } else {
        println(log("The definition of the card:"))
        val definition = log(scanner.nextLine())
        if (cardMap.containsDef(definition)) {
            println(log("The definition \"$definition\" already exists."))
        } else {
            cardMap[term] = Card(term, definition,0)
            println(log("The pair (\"$term\":\"$definition\") has been added."))
        }
    }
}

private fun add(file: File, cardMap: MutableMap<String, Card>): Int {
    var count = 0
    val scanner = Scanner(file)
    while (scanner.hasNextLine()) {
        val term = scanner.nextLine()
        if (term != "") {
            val definition = scanner.nextLine()
            val mistake = scanner.nextLine().toInt()
            cardMap[term] = Card(term, definition,mistake)
            count++
        }

    }
    return count
}
fun hardestcard(cardMap: MutableMap<String, Card>) {
    var hardest = mutableListOf<Card>()
    var rank = 1
    for (card in cardMap.values) {
        if (card.mistake > rank) {
            hardest.clear()
            rank = card.mistake
            hardest.add(card)
        }
        else if (rank == card.mistake){
            hardest.add(card)
        }
    }
    if (hardest.isNotEmpty()) {
        var string : String = ""
        for (card in hardest){
            if(hardest.last()==card){
                string+="\"${card.term}\"."
            }
            else{string+="\"${card.term}\", "}
        }
        if(hardest.size==1){
            println(log("The hardest card is \"${hardest.first().term}\" You have $rank errors answering it"))}
        else println(log("The hardest cards are $string You have $rank errors answering them."))}
    else{
        println(log("There are no cards with errors."))
    }
}
fun resetStats(cardMap: MutableMap<String, Card>){
    for(card in cardMap.values){
        card.mistake=0
    }
    println(log("Card statistics has been reset."))
}
fun log(scanner : Scanner){
    println(log("File name:"))
    val myFile = File(log(scanner.nextLine()))
    var text = ""
    for(string in log){
        text=text + string +"\n"
    }
    myFile.writeText(text)
    println(log("The log has been saved."))
}
