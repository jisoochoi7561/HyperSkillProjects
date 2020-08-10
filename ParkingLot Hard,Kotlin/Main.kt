package parking

import java.util.*

class Car(val serial: String, val color: String) {

}

class ParkingLot(size: Int) {
    val parkingLot = Array<Car?>(size) { null }
    fun reg_by_color(color: String) : String{
        var message : String = ""
        for (index in parkingLot.indices){
            if(parkingLot[index]?.color?.toLowerCase()==color.toLowerCase()){
                if (message==""){message+="${parkingLot[index]!!.serial}"}else{message+=", ${parkingLot[index]!!.serial}"}
            }
        }
        if(message==""){return "No cars with color $color were found."}else{return message}
    }
    fun spot_by_color(color: String) : String{
        var message : String = ""
        for (index in parkingLot.indices){
            if(parkingLot[index]?.color?.toLowerCase()==color.toLowerCase()){
                if (message==""){message+="${index+1}"}else{message+=", ${index+1}"}
            }
        }
        if(message==""){return "No cars with color $color were found."}else{return message}
    }
    fun spot_by_reg(reg: String) : String{
        var message : String = ""
        for (index in parkingLot.indices){
            if(parkingLot[index]?.serial==reg){
                if (message==""){message+="${index+1}"}else{message+=", ${index+1}"}
            }
        }
        if(message==""){return "No cars with registration number $reg were found."}else{return message}
    }

}

fun main() {
    val scanner = Scanner(System.`in`)
    var myParkingLot: ParkingLot? = null
    while (true) {
        val temp = scanner.nextLine().split(" ")
        if (temp[0] == "exit") {
            break
        }
        if (temp[0] == "create") {
            myParkingLot = create(temp)
        } else if (myParkingLot == null) {
            println("Sorry, a parking lot has not been created.")
        } else {
            if (temp[0] == "park") {
                park(temp, myParkingLot)

            } else if (temp[0] == "leave") {
                leave(temp, myParkingLot)
            } else if (temp[0] == "status") {
                var isEmpty = true
                for (index in myParkingLot.parkingLot.indices) {
                    if (myParkingLot.parkingLot[index] != null) {
                        isEmpty=false
                        println("${index + 1} ${myParkingLot.parkingLot[index]!!.serial} ${myParkingLot.parkingLot[index]!!.color}")
                    }
                }
                if(isEmpty) println("Parking lot is empty.")
            }
            else if (temp[0] == "reg_by_color"){
                println(myParkingLot.reg_by_color(temp[1]))
            }
            else if (temp[0] == "spot_by_color"){
                println(myParkingLot.spot_by_color(temp[1]))
            }
            else if (temp[0] == "spot_by_reg"){
                println(myParkingLot.spot_by_reg(temp[1]))

            }
        }
    }
}

private fun leave(temp: List<String>, myParkingLot: ParkingLot) {
    val position = temp[1].toInt()
    val index = position - 1
    if (myParkingLot.parkingLot[index] != null) {
        println("Spot $position is free.")
        myParkingLot.parkingLot[index] = null
    } else {
        println("There is no car in spot $position.")
    }
}

private fun park(temp: List<String>, myParkingLot: ParkingLot) {
    val car = Car(temp[1], temp[2])
    var isFull = true
    for (lot in myParkingLot.parkingLot.indices) {
        if (myParkingLot.parkingLot[lot] == null) {
            myParkingLot.parkingLot[lot] = car
            println("${myParkingLot.parkingLot[lot]!!.color} car parked in spot ${lot + 1}.")
            isFull = false
            break
        }
    }
    if (isFull) {
        println("Sorry, the parking lot is full.")
    }
}

private fun create(temp: List<String>): ParkingLot? {
    val size = temp[1].toInt()
    var myParkingLot1 = ParkingLot(size)
    println("Created a parking lot with $size spots.")
    return myParkingLot1
}
