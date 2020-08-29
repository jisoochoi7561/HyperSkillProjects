## Theory: Retrieving Class instances

 `java.lang.Class`가 리플렉션에서 중요한 역할을 한다. 이것은 자바의 타입을 대표한다. 다른 말로 하자면 , 어떤 타입의 인스턴스를 갖고 있다면 , 이걸 사용해서 타입에 관한 모든 걸 알아낼 수 있다. 이래서  `java.lang.Class`가 중요한 것이다. `java.lang.Class`의 인스턴스를 얻는방법은 여러가지가 있는데, 인스턴스로부터,클래스이름으로부터,아니면타입으로부터,또 이미 존재하는 `Class`로부터. 자세히 알아보자.

## The .class Syntax

`Class` 인스턴스를 타입으로부터 뽑아내려면  `.class` 를 쓴다:

```java
Class stringClass = String.class;
```

`.class`를 타입뒤에 붙이기만 하면된다. 이런 식으로 `Class` 인스턴스를 얻는것은 우리가 다른 정보가 없을 때 유용하다.

원시타입과 void에도 적용가능하다:

```java
Class intClass = int.class;
Class voidClass = void.class;
```

## Retrieve Class from an object instance

`Object`클래스는(모든 클래스는 이걸 상속받고 있다)  [getClass()](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--) 메소드를 내장하고 있다. 주어진 객체의 `Class` 를 얻으려면 이 함수를 부르기만 하면된다:

```java
Class instanceClass = "abc".getClass();
```

이것은 `Object`를 상속받는 레퍼런스 타입에만 먹힌다. 원시타입에는? 쓸 수 없다.

## Retrieve Class with a given name

우리가 이름을 완벽히 알고 있다면  [Class.forName()](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#forName-java.lang.String-)스태틱 메소드를 사용해서 `Class` 를 얻어낼 수 있다.이것은 원시타입에는 사용할 수 없다!

```java
Class forName = Class.forName("java.lang.String");
```

이 메소드는 또한 array클래스에서도 `Class` 를 뽑아낼 수 있다. 이 경우, [ 가 차원을 나타내고 타입을나타내는 문자가 따로 있다.

타입문자는:

- boolean – Z
- byte – B
- char – C
- class or interface – L*classname;*
- double – D
- float – F
- int – I
- long – J
- short – S

```java
Class floatArrayClass = Class.forName("[F"); //1차원 플롯 배열
Class objectArrayClass = Class.forName("[[Ljava.lang.Object;"); //2차원 오브젝트 배열
```

## Methods that Return Classes

우리가 위에서 말한 메소드 말고도 클래스를 얻기위해  Reflection API를 쓸 수 있다. 하지만 이것들은 `Class` 객체가 존재하면 거기다 쓰는 거란걸 잊지마라.

예시:

```java
// Returns the super class for the given class
String.class.getSuperclass();//클래스의 슈퍼클래스를얻는다

// Returns all the public classes, interfaces, and enums that are members of the class
String.class.getClasses();//클래스의 클래스,인터페이스,이넘(내부)를 얻는다

// Returns all of the classes, interfaces, and enums that are explicitly declared in this class.
String.class.getDeclaredClasses();//클래스의 거기에 직접 선언한 클래스,인터페이스,이넘(내부)를 얻는다.
```

