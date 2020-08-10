fun main() {
    val productType = readLine()!!
    val price = readLine()!!.toInt()
    val product = when(productType){
        "headphones" -> headphones(price)
        "smartphone" -> smartphone(price)
        "tv" -> tv(price)
        "laptop" -> laptop(price)
        else->Product(price)
    }
    println(totalPrice(product))
}
fun totalPrice(product : Product) : Int{return product.price + product.price * product.tax}
open class Product(val price: Int)
class headphones(val price: Int,val tax : Double = 0.11) : Product(price)
class smartphone(val price: Int,val tax : Double = 0.15) : Product(price)
class tv(val price: Int,val tax : Double = 0.17) : Product(price)
class laptop(val price: Int,val tax : Double = 0.19) : Product(price)
