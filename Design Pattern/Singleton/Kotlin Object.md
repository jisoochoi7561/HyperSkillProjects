## Object 선언

 22 minutes5 / 8 problems solved

Kotlin에서, 클래스는 여러방식으로 여러번 구현 될 수 있다.가끔 우리는 단 하나의 인스턴스가 필요하다. 코드를 정리하고 함수를 모으는데 유용할 수도 있다.

## Singleton

싱글톤은 클래스가 단 하나의 인스턴스를 가지고 글로벌한 접근이 가능하단걸 보장해주는 디자인패턴이다. 우리가 코드의 어느부분에서건 싱글톤에 접근할 수 있단 말이다. 간단한 비유: 보드게임에서,모든 참가자들은 동일한 단 하나의 보드를 다룬다. 이건 특별한 필드 단 하나 이므로 게임 전체상태를 저장하면 된다. 우선 싱글톤의 특징을 정리하고 시작하자:

- 싱글톤 클래스는 단 하나의 인스턴스를 가진다
- 싱글톤 클래스는 global access를 보장한다

## Object declaration

싱글톤은 진짜로 유용하고,코틀린은 싱글톤 전용 구조를 제공한다: **object declaration**. 이것은 **object** keyword를 가지는 싱글톤 생성 클래스이다. 이 키워드를 쓰면 귀찮은 짓 할필요가 없고, 어떻게 구현할지 끙끙댈 것도 없다: 그냥 object declaration을 사용해라

코드를 보자:

```kotlin
object PlayingField { //class keyword 대신에 object  키워드가 사용되었다.

    fun getAllPlayers(): Array<Player> {
        /* ... */
    }
    
    fun isPlayerInGame(player: Player): Boolean {
        /* ... */
    }

}
```

object declaration을 쓸 때, 생성자를 다룰 수 없다-코틀린이 알아서 한다. 우리의 `게임판`의 인스턴스를 얻으려면,`PlayingField` 를 써라. 어디서나 사용가능하고 언제나 같은 인스턴스이다.

```kotlin
fun startNewGameTurn() {
    val players = PlayingField.getAllPlayers()  //PlayingField 오브젝트가 사용되고 있다
    if (players.length < 2) {
        return println("Game could not be continued without players")
    }
    for (player in players) {
        nextPlayerTurn(player)
    }
}

fun nextPlayerTurn(player: Player) {
    if (!PlayingField.isPlayerInGame(player)) {  //위에서 쓴 오브젝트와 동일한 오브젝트이다
        return println("Current player lost. Next...")
    }
    /* Player actions here */
}
```

## Nested object 

Object declaration은 class declaration안에 들어갈 수 있다. **nested class** 은 또다른 클래스 안쪽에서 만들어진다;바깥 클래스를 선언하기 전엔 접근할 수가 없다.

```kotlin
class OuterClass {  
   //바깥 클래스  
    class NestedClass {  
      //안쪽 클래스
    }  
}
```

nested object 를 자세히보자:

```kotlin
class Player(val id: Int) {

    /* ... */
    object Properties { //클래스안에 object가 있다
        /* Default player speed in playing field - 7 cells per turn */
        val defaultSpeed = 7

        fun calcMovePenalty(cell: Int): Int {
            /* calc move speed penalty */
        }
    }
}

/* prints 7 */
println(Player.Properties.defaultSpeed)
```

object `Properties` 는  `Player` 의 안에 있으므로,  `Player.Properties`로 접근해야한다. 이 제한을 풀려면, object앞에 `companion` 키워드를 추가해주면 된다.

## Companion object

클래스안에 있는 object에  **companion** 을 붙여줄 수 있다:

```kotlin
class Player(val id: Int) {

    /* ... */
    companion object Properties { // objcet앞에 companion을 붙였다
        /* Default player speed in playing field - 7 cells per turn */
        val defaultSpeed = 7

        fun calcMovePenalty(cell: Int): Int {
            /* calc move speed penalty */
        }
    }
}

/* prints 7 */
println(Player.Properties.defaultSpeed)
```

companion object 는 바깥클래스에 붙여진 싱글톤이다.따라서 바깥쪽 클래스에 접근해야한다.현재 object가 바깥쪽 클래스에 연결됬다고 생각하면 된다. 예를 들면 `Player` companion object에 우리는 모든플레이어들의 defaultspeed를 저장할 수 있다. 이건 또 모든 `Player`들이 companion object를 가지고 있고 그것을 리턴한단 뜻이다. 

그 전까지 우린 이름이 있는 companion object를 썼다. Kotlin에선 이름을 생략해도 된다. 예를들면:

```kotlin
class Player(val id: Int) {

    /* ... */
    companion object { //이름이생략됨
        /* Default player speed in playing field - 7 cells per turn */
        val defaultSpeed = 7

        fun calcMovePenalty(cell: Int): Int {
            /* calc move speed penalty */
        }
    }
}

/* prints 7 */
println(Player.defaultSpeed) //Player클래스가 companion object처럼 사용되고 있음
```

보는 바와 같이,이름이 없을땐 바깥쪽 클래스에 대한 접근으로 companion object에 접근 가능하다. 이름을 활용하려면 생략을 안하면 되겠다.이름이 없을 땐, 기본 이름으로 쓰면 된다: `Companion`.

```kotlin
/* prints 7 too */
println(Player.Companion.defaultSpeed)
```

## nested objects의 제한

companion object는 각 클래스당 하나만 가질 수 있다. Kotlin에선 이름이 다르더라도 여러개를 동시에 쓸 수 없다.시도해보면 에러가 날것이다:

```kotlin
class BadClass {

    companion object Properties {
    
    }

    companion object Factory {
    
    }
}

// 컴파일 에러
// 클래스당 companion object는 단 하나
```

생각해보면 위처럼 BadClass.defaultspeed를 할 때 Properties와 Factory중 뭐를 가르키는지 알 수 없으니 당연하다.

하지만 object들은 여러개가 가능하다:

```kotlin
class Player(val id: Int) {

    /* ... */
    companion object Properties {
        /* Default player speed in playing field - 7 cells per turn */
        val defaultSpeed = 7

        fun calcMovePenalty(cell: Int): Int {
            /* calc move speed penalty */
        }
    }

    /* creates a new instance of Player */
    object Factory {
        fun create(playerId: Int): Player {
            return Player(playerId)
        }
    }
    
    /* Allows saving player instance into a file or something else */
    object Serializer {
        fun toBytes(player: Player): ByteArray {
            /* Convert a Player instance to byte array */
        }

        fun fromBytes(bytes: ByteArray): Player {
            /* Creates a Player instance from byte array */
        }
    }
}

/* prints 7 */
println(Player.Properties.defaultSpeed)

/* also prints 7 */
println(Player.defaultSpeed)

/* prints 13 */
println(Player.Factory.create(13).id)
```

또 우리는 다른 object 의 companion object를 만들 수 없는데 , global access 원칙을 위반하기 때문이다.

object안에 object들은 얼마든지 만들 수 있다:

```kotlin
object Game {

    object Properties {
        val maxPlayersCount = 13
        val maxGameDurationInSec = 2400
    }

    object Serializer {
        
    }

}
```

Game이 companion object를 가진다면 Game.*에서 Game이 Game objcet인지 Game이 가진 companion object인지 구별할 방법이 없기 때문에 허용되지않는다.

# 요약

Object declaration은 유용하다. 우리는 보통 `object` 와 `companion` 를 싱글톤에 쓴다. 현명하고 올바르게 쓴다면, 코드를 간단하고 쉽게 만들 수 있다.
