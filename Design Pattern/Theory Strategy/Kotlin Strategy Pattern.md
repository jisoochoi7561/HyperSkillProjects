# Kotlin Strategy Pattern

#### 코틀린에서의 전략패턴

자바에서의 전략패턴의 구조는 결국



상황마다 다른 로직을 써야하는 함수가 있음 ->

로직을 각각 클래스로 구현함 ->

그것을 묶는 인터페이스/클래스 가 있음 ->

사용자는 코드에는 그 추상적타입을 넣어두고 ->

사용할 때 그 타입의 구체적 로직을 지정해서 사용함



이다.



여기서 느낀 점은

지나치게 클래스가 많다..라고 느꼈다.

전략에 필드가 들어가긴 하지만

가장 중요한 부분은 로직을 구현한 메소드인데

메소드를 저장하기 위해 클래스를 만들어 놓고

전략을 넘겨줘야 하는 부분에서 클래스의 인스턴스를 new로 만들어서 넘겨주는 것은 지나치게 구조적인 것 같다..

```java
MessageSender sender = new MessageSender(); // message sender 를 만듭니다

sender.setMethod(new EmailSendingMethod()); // 구체적인 함수를 고릅니다  이걸 위해 클래스까지 구현한다라

sender.send("alice@gmail.com", "bob@gmail.com", "Hello!"); //send를 실행합니다

sender.setMethod(new SmsSendingMethod()); // 함수를 바꿉니다   아까 그 객체는 그럼 증발한거임?

sender.send("1-541-444-3333", "1-541-555-2222", "Hello!");//send를 실행합니다
```



물론 인터페이스를 활용해 범용적인 프로그램을 만드는 것은 멋지다고 생각한다.

근데 이게 전략패턴인지는 잘 모르겠다. 원래 그러라고 인터페이스가 있는거 아닌가?



어쨌든 코틀린은 함수가 일급시민이고, 람다등도 잘 되어있기 때문에

더 간결하게 표현할 수 있다. 보자마자 이거 함수를 넘겨주는게 낫지 않나 하는 생각이 들었다.

물론 코틀린도 위와같은 정통 전략 패턴을 사용할 수 있다.

클래스를 구현할지 함수를 넘겨줄지 상황에 따라 적절히 선택하면 좋은 코드를 짤 수 있을 것이다.



## 구현

[코틀린 전략 패턴](https://chercher.tech/kotlin/strategy-design-pattern-kotlin)

전략 패턴에서 클래스의 행동이나 알고리즘은 런타임에 바뀝니다. 전략 패턴은 행동 패턴의 일종입니다.

전략 패턴에서 우리는 다양한 전략을 대표하는 객체를 만들고 전략객체에 따라 행동이 달라지는 컨텍스트 객체를 만듭니다. 전략객체는 컨텍스트 객체가 실행하는 알고리즘을 바꿉니다.

```java
class Printer(val strategy: (String) -> String) {  // 함수를 인자로 받는다
    fun print(string: String): String = strategy(string) //함수에 따라 하는 행동이 달라진다
}
val lowerCaseFormatter: (String) -> String = String::toLowerCase // 소문자리턴함수
val upperCaseFormatter: (String) -> String = String::toUpperCase // 대문자 리턴함수
fun main(args: Array<String>) {
    val lower = Printer(strategy = lowerCaseFormatter) // 프린터에 적절한 전략(함수)를 건내준다
    println(lower.print("O tempora, o mores!"))
    val upper = Printer(strategy = upperCaseFormatter) // 전략을 바꿔준다
    println(upper.print("O tempora, o mores!"))
}
```

결과

```java
o tempora, o mores!
O TEMPORA, O MORES!
```

지나치게 클래스를 많이 만들지 않고 함수를 넘겨주는 방식으로 전략패턴을 구현했다.

또 다른 예

```java
interface BookingStrategy { // 전략용 인터페이스를 만든다. 자바에서 사용했던 구조.
    val fare: Double    // 필드
}

class CarBookingStrategy : BookingStrategy { // 구체적 전략
 
    override val fare: Double = 12.5 // 필드구현

    override fun toString(): String {
        return "CarBookingStrategy" //메소드 구현. toString메소드라서 인터페이스에 없는듯.즉 이 전략패턴은 필드를 사용하기 위한 전략패턴이다.
    }
}

class TrainBookingStrategy : BookingStrategy { //전략 2

    override val fare: Double = 8.5

    override fun toString(): String {
        return "TrainBookingStrategy"
    }
}

class Customer(var bookingStrategy: BookingStrategy) { //Context에 해당

    fun calculateFare(numOfPassangeres: Int): Double { //전략에 따라 하는 행동이 달라지는 함수
        val fare = numOfPassangeres * bookingStrategy.fare
        println("Calculating fares using " + bookingStrategy)
        return fare
    }
}

fun main(args: Array<String>) {

    //CarBooking Strategy
    val cust = Customer(CarBookingStrategy()) //전략선언,객체만듬
    var fare = cust.calculateFare(5) //차 요금 계산
    println(fare)

    //TrainBooking Strategy
    cust.bookingStrategy = TrainBookingStrategy() //전략바꿈
    fare = cust.calculateFare(5) // 기차 요금 계산
    println(fare)
}
```

결과

```java
Calculating fares using CarBookingStrategy
62.5
Calculating fares using TrainBookingStrategy
42.5
```

아무래도 필드를 사용하다 보니 자바처럼 클래스를 만들어 준 모습이다. 음..근데 너무 간단한 예제라서 그런지 굳이 전략패턴을 써야하나 싶긴하다.



필드가 있을 때는 클래스를

하나의 메소드만 있을 때는 함수를 넘겨주는게 좋을 것 같다.

함수를 넘겨줄 때의 단점은 예를 들면 String -> String 함수들중에서도 사용될 수 있는 함수만을 구별할 방법이 없다는 것이다.

클래스를 만들고 거기에 함수들을 저장하는 방식을 선택할 수도 있겠다.