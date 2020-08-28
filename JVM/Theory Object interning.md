## Theory: Object interning

커다란 프로그램은 메모리에 많은 객체를 만드는데 그러면 프로그램이 느려진다. 쓰이는 메모리 양을 줄이기 위해 자바는 몇개의 자주쓰이는 클래스의 객체들을 특별한 풀에 넣어놓고 재활용한다. 이 테크닉은 **object interning**이라고 부른다. 우선 이 개념을 string의 예시로 이해해보자.

## Reuse of strings

`String`클래스는 ㄹㅇ루다가 많이 쓰인다. 또 메모리도 많이 잡아먹는 놈이기도 하다. heap에 string을 저장하는걸 최적화 하기 위해 자바는 string pool을 쓴다. 

아시듯이, `new`로 새로운 string을 만들 수 있다. 물론 `"`을 사용해서도 만들 수 있다. 우선 `new`로 만들어보자:

```java
String greeting1 = new String("hello");
String greeting2 = new String("hello");
```

 `String` 은 레퍼런스타입(일반적인 객체)이니까, 위의 코드는 두 객체 `"hello"`를 만든다.그리고 각각의 변수는 각각의 객체를 참조한다. 따라서 두개를 비교하면(주소비교) `false`가 나온다(두 개는 다른 객체임). 물론 실제값을 비교하면 `true`가 나온다. 

```java
System.out.println(greeting1 == greeting2); // false
System.out.println(greeting1.equals(greeting2)); // true
```

꼭 string이 아니더라도 모든 객체들이 이런식으로 반응할 것이다.

이번엔 `new`를 안쓰고 해보겠다.

```java
String greeting3 = "hello";
String greeting4 = "hello";
```

놀랍게도 이 두 변수가 가르키는 `"hello"`는 같은 헬로이다. 그 헬로는 특별한 풀에 들어가있어서 재사용된다. 덕분에 메모리를 저량ㄱ할 수 있다. 다음 코드를 보면 같은 주소라는걸 알 수 있다:

```java
System.out.println(greeting3 == greeting4); // true 
System.out.println(greeting3.equals(greeting4)); // true
```

`String` 이 레퍼런스 타입임에도 불구하고  `==` 연산자가 `true`를 리턴한다. 왜냐면 자바가 풀에 들어있는 같은 객체를 재활용하고 있기 떄문이다. 다만 ,여전히 string을 비교할 땐 equals를 써야한다, 왜냐면 풀을 쓸 때 안쓸 때 결과가 다르면 곤란하니까.

## Reuse of wrapper class objects

string처럼 어떤 wrapper class들도 interning 메커니즘을 통해 메모리를 아낀다. 하지만 약간 다른 방식으로 작동한다,,

-  `Boolean`은 두 갓을 모두 저장해둔다.
-  `Character` 는  `0` ~ `127` (`\u0000` to `\u007f`)을 저장해둔다.
- `Byte`, `Short`, `Integer`,  `Long` 은 `-128` ~ `127`을 저장해둔다.
-  `Float` 과 `Double` 은 그런거 없다.

따라서, 어떤 wrapper들은 특정 범위의 값을 pool에 저장해두고 재활용한다.이 현상은 **boxing** 이나 **autoboxing**을 통해 객체를 만들 때 일어난다.

예를 들면 `System.out.println(i1 == i2)`은 `i1` 과 `i2`가 같은 값일 때 `-128` 과 `127`사이면  true를 프린트하고 아니면 false일 것이다.

```java
Long i1 = Long.valueOf("127");     // boxing
Long i2 = Long.valueOf("127");     // boxing
System.out.println(i1 == i2);      // true, Java reuses 127
System.out.println(i1.equals(i2)); // true, they have the same value inside
```

autoboxing일 때도 같다:

```java
Long i1 = 127L; // autoboxing
Long i2 = 127L; // autoboxing
System.out.println(i1 == i2);      // true
System.out.println(i1.equals(i2)); // true
```

하지만 이 방식은 `new`를 통해 새로운 객체를 만든다고 명시하면 사용되지 않는다:

```java
Long i1 = new Long("127");
Long i2 = new Long("127");
System.out.println(i1 == i2);      // false, "new" creates different objects
System.out.println(i1.equals(i2)); // true, they have the same value inside
```

## Conclusion

**Object interning**은 메모리를 아끼는 내부적인 요소이다. pool을 사용한다. 이것은 immutable object사이에만 가능하다. 사실;object interning은 JVM마다 쓸지 안쓸지 다르다(우리는 HotSpot기준으로 얘기했다.).우리의 프로그램은 이 최적화 방식에 의존해서는 안된다. 즉 `==`연산자를 객체사이의 비교에 쓰면 특정범위에서 JVM마다 다른 결과가 나올것이니 쓰지마라.설사 pool을 쓰는지 안쓰는지를 완벽히 안다고 하더라도 말이다.