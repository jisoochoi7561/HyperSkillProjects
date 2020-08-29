## Theory: Runtime type checking



## Runtime type checking

상위 타입은 언제나 하위타입을 표현할 수 있다 (ex Object obj = "hi"). 우리는 런타임에 실제 타입을 정해줄 수 있다.

이걸 위해 자바는 몇가지 방법을 제공한다:

- `instanceof` 오버레이터가 오브젝트가 특정타입인지 검사할 수 있다;
- **java reflection** 은 클래스를 객체를 얻을 수 있다.

이걸 이용해서 런타임에 타입체크를 해보자.

우리가 예시에서 쓸 클래스 계층구조:

```java
class Shape {...}

class Circle extends Shape {...}

class Rectangle extends Shape {...}
```

구조는 매우 심플하다. 필드와 메소드들은 생략했다. 

## The keyword instanceof

`instanceof` 는 객체가 특정 클래스나 그것의 하위클래스이면 `true` 를 리턴한다.

기본 문법:

```java
obj instanceof Class
```

위의 클래스로부터 객체를 몇가지 만들었다:

```java
Shape circle = new Circle();  // the reference is Shape, the object is Circle
Shape rect = new Rectangle(); // the reference is Shape, the object is Rectangle
```

타입체크를 해보자:

```java
boolean circleIsCircle = circle instanceof Circle; // true
boolean circleIsRectangle = circle instanceof Rectangle; // false
boolean circleIsShape = circle instanceof Shape; // true

boolean rectIsRectangle = rect instanceof Rectangle; // true
boolean rectIsCircle = rect instanceof Circle; // false
boolean rectIsShape = rect instanceof Shape; // true
```

즉 `instanceof` 는 그것이 상위클래스로 표현됫더라도 실제 타입을 체크하게 해준다.

보이듯이, 이 연산은 하위객체를 상위클래스의 인스턴스라고 인식한다:

```java
boolean circleIsShape = circle instanceof Shape; // true
```

**주목,** 물어볼 타입(원입니까?)은 객체(shape)의 subtype 이어야 한다. 그렇지 않다면 컴파일되지 않는다.

이 코드는 컴파일 되지 않는다:

```java
Circle c = new Circle();
boolean circleIsRect = c instanceof Rectangle; // Inconvertible types rectangle은 circle의 subtype이 아니다
```

두번째 라인에서 컴파일 에러: **Inconvertible types**.

## Use reflection

각각의 오브젝트는 `getClass` 메소드를 갖고 있다. 이걸로 클래스를 얻을 수 있다. 우리는  **java reflection**을 사용해 런타임에 곧바로 클래스들을 비교할 수 있다.

다음 예를 보자.`Circle`인스턴스가 있다:

```java
Shape circle = new Circle();
```

리플렉션을 사용하자:

```java
boolean equalsCircle = circle.getClass() == Circle.class; // true
boolean equalsShape = circle.getClass() == Shape.class;   // false
boolean rectangle = circle.getClass() == Rectangle.class; // false
```

`instanceof` 와 달리 이 방식에는 아까처럼 subtype이어야 하는 제한이 없다.

또 다른 방법이 하나 있다. 클래스 오브젝트는 `isInstance` 라는 `instanceof` 와 비슷한 메소드를 갖고 있다.

```java
boolean isIntanceOfCircle = Circle.class.isInstance(circle); // true
boolean isInstanceOfShape = Shape.class.isInstance(circle); // true
boolean isInstanceOfRectangle = Rectangle.class.isInstance(circle); // false
```

이것도 subtype instance인지를 검사한다.하지만 이번 것은 컴파일된다:

```java
Circle c = new Circle();
boolean circleIsRect = Rectangle.class.isInstance(c); // false
```

뭘 쓰든 좋다.

## When to use it

만약 상위타입객체를 하위타입에 캐스트한다면 `ClassCastException` 를 받을지도모른다. 캐스팅 이전에 실제 타입을 체크하라.우리가 방금 배운 방식으로,

다음 예시가 그걸 잘 보여준다.

```java
Shape shape = new Circle();

if (shape.getClass() == Circle.class) {
    Circle circle = (Circle) shape;

    // now we can process it as a circle
}
```



명심할 것은 프로그램에서 런타임체크를 많이 한다는 것은 코드가 못생겨졌단 뜻이다. **polymorphism** 을 사용해 그걸 줄여라.