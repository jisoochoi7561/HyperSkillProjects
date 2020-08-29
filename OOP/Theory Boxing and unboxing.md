## Theory: Boxing and unboxing

 

## Wrapper classes

각각의 원시타입은 그걸 위한 클래스가 있다. 그 클래스들을 **wrappers** 라고 하며 그것들은 **immutable** 이다(string처럼). 래퍼 클래스들은 여러 상황에서 사용된다:

- 변수가 `null`이 될 수 있을 때 (absence of a value);
- 제네릭 콜렉션이 값을 넣어야 할 때 (다음 강좌에서 배움);
- 클래스들이 제공하는 함수를 쓰고 싶을 때.

다음 표에 원시타입과 래퍼클래스를 정리해 두었다.

**![img](https://ucarecdn.com/547298e4-3df8-4e70-9577-41663f4c6c0f/)**

**The table with primitive types and the corresponding wrappers**

보시듯이 자바는 8개의 래퍼클래스를 제공한다. 3번째 열은 래퍼클래스를 생성할 때 필요한 인자의 타입을 말한다.

## Boxing and unboxing

**Boxing** 은 원시타입을 래퍼클래스로 바꾸는 걸 뜻한다.**Unboxing** 은 반대과정이다. 다음 코드에 두 과정이 모드 있다:

```java
int primitive = 100;
Integer reference = Integer.valueOf(primitive); // boxing
int anotherPrimitive = reference.intValue();    // unboxing
```

**Autoboxing** 과 **auto-unboxing** 은 자바 컴파일러에 의해 자동으로 일어나는 변환이다.

```java
double primitiveDouble = 10.8;
Double wrapperDouble = primitiveDouble; // autoboxing
double anotherPrimitiveDouble = wrapperDouble; // auto-unboxing
```

오토와 수동을 섞어써도 된다.

오토박싱은 왼쪽과 오른쪽이 똑같은 타입일 때만 일어난다. 다른경우에는 컴파일 에러.



```java
Long n1 = 10L; // OK, assigning long to Long
Integer n2 = 10; // OK, assigning int to Integer 

Long n3 = 10; // WRONG, assigning int to Long
Integer n4 = 10L; // WRONG assigning long to Integer
```

## Constructing wrappers based on other types

래퍼클래스는 다른 타입으로부터 만들어낼 수 있는 생성자를 갖고 있다.예를들어 래퍼클래스 인스턴스는 문자열로부터 탄생하곤 한다(Character빼고 나머지는).

```java
Integer number = new Integer("10012"); // an Integer from "10012"
Float f = new Float("0.01");           // a Float from "0.01"
Long longNumber = new Long("100000000"); // a Long from "100000000"
Boolean boolVal = new Boolean("true");   // a Boolean from "true"
```

래퍼 클래스를 만드는 특별한 메소드들도 있다:

```java
Long longVal = Long.parseLong("1000");      // a Long from "1000"
Long anotherLongVal = Long.valueOf("2000"); // a Long from "2000"
```

만약 내부의 문자열이 잘못된 문자열이라면  (예를 들면, `"1d0o3"`)`NumberFormatException`



자바9 이후엔 생성자들은 deprecated. 특별한 메소드들을 써서 만드세요.





## Comparing wrappers

다른 레퍼런스 타입들처럼 `==`연산은 주소값을 비교한다.`equals` 함수는 우리가 생각하는 값이 같은지를 비교한다.

```java
Long i1 = Long.valueOf("2000");
Long i2 = Long.valueOf("2000");
System.out.println(i1 == i2);      // false
System.out.println(i1.equals(i2)); // true
```

이것을 절대로 잊지 마라. 래퍼클래스의 값이 원시타입이지만 ,어쩃든 이것들은 레퍼런스 타입이다.

## NPE when unboxing

언박싱을 할 때 문제가 발생할 수 있따. 만약 래퍼 인스턴스가 null이면 `NullPointerException`이 발생한다

```java
Long longVal = null;
long primitiveLong = longVal; // It throws NPE
```

이걸 고치려면 조건문을 추가해야한다

```java
long unboxed = val != null ? val : 0; // No NPE here
```

이 코드는 예외를 던지지 않는다.

또 다른 예외는 수학적인 계산을 할 때 생긴다. 이 과정에 오토 언박싱이 있기 떄문에 **NPE** 가 생길 수 있다.

```java
Integer n1 = 50;
Integer n2 = null;
Integer result = n1 / n2; // It throws NPE
```