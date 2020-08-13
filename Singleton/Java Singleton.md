## Theory: Singleton

종종 우리는 애플리케이션 전체에서 단 하나의 인스턴스만을 쓰는 클래스가 필요할 때가 있다.Singleton은 클래스의 인스턴스 생성을 단 하나로 제한시키는 디자인패턴이다. 이 패턴은 클래스의 인스턴스가 단 하나인것을 보장하고, 바깥쪽에서도 사용할 수 있게 해준다.![img](https://ucarecdn.com/c4c50aab-5b46-4d4d-81c4-4699808d8ffa/)

여러 client들이 같은 인스턴스를 사용한다. (classes, methods)

싱글톤은 보통 데이터베이스나 소켓같은 리소스에 대한 접근을 컨트롤한다.

예를 들면,  데이터베이스에 대한 연결을 담당하는 클래스는 싱글톤으로 사용할만하다: 연결하는 객체를 매번매번 만드는건 메모리를 많이 잡아먹을 것이다.만약 그것이 싱글톤이라면, 애플리케이션은 더 빠르고 더 좋을 것이다.

또 다른 예는 애플리케이션 전체에 대한 로거를 제공하는 클래스이다. 그 클래스는 공유되는 하나의 로그파일을 리소스로 가진다.

이 토픽에선 싱글톤의 기본적 구현만을 다룬다.



## Java에서의 Basic Singleton

싱글톤패턴으로 클래스를 작성하는 기본적 방법:

- private한 생성자 - new를 통해 새로운 인스턴스를 생성하는 것을 막는다.
- 클래스의 유일한 인스턴스인 클래스의 private static 변수
- 같은 인스턴스를 리턴하는 public static 메소드; 이것으로 인스턴스에 접근한다.

아래코드에서 구현하고 있다.

```
class Singleton  

    private static Singleton instance = new Singleton(); //단 하나의 인스턴스를 변수로 가지고 있다.

    private Singleton() { }     //private 생성자

    public static Singleton getInstance() {
        return instance;    //인스턴스에 접근하게 해주는 public static 메소드
    }
}
```

간단한 구조이지만 더 자세히 살펴보자.

 `Singleton` 클래스의 모양이 singleton패턴의 기본적 구현이다. `instance` 변수는 클래스의 단 하나의 인스턴스를 저장하고 있다.생성자는 또다른 인스턴스를 만드는 걸 막기위해  `private` 으로 선언되어 있다. `static` 함수 `getInstance` 는 단 하나 바로 그 인스턴스를 리턴한다.

3개의 변수를 만들고, 각각 인스턴스를 할당해 주소를 비교해보자:

```ingleton s1 = Singleton.getInstance();
Singleton s1 = Singleton.getInstance(); //instance를 리턴받았다
Singleton s2 = Singleton.getInstance(); //동일한 인스턴스라고 기대할 수 있다.
Singleton s3 = Singleton.getInstance(); //동일한 인스턴스라고 기대할 수 있다.
    
System.out.println(s1 == s2); // s1과 s2가 같은 객체이므로 true이다
System.out.println(s2 == s3); // s2와 s3가 같은 객체이므로 true이다
```

보면 알듯,  `s1`,`s2`, `s3` 는 같은 객체(`Singleton`의 static field에 저장된)를 가리키고 있다 

보통 싱글톤은 추가적인 `instance field`를 가지고 있다.애플리케이션 전체가 사용하는 값(전역변수)과 함수들을 저장하기 위해.위의 간단한 `Singleton` 클래스는 구체적인 쓸모가 없으니 다른 변수와 메소드를 가지고 있단 뜻이다.

[local variable , instance field,input paremeter,class field 란?](https://stackoverflow.com/questions/20671008/what-is-the-difference-between-a-local-variable-an-instance-field-an-input-par)

>  자바 프로그램에서 local variable, instance field,input parameter, class field 의 차이는 무엇입니까?

>**local variable** 은 블록범위안에서 정의되어 있습니다.블록 밖에선 사용할 수 없습니다.
>
>예시:
>
>```java
>if(x > 10) { // 블록 시작
>    String local = "Local value";  //local variable
>} // 블록 끝
>System.out.println(local) // 이 코드는 오류입니다.
>```
>
>`local` 을 이 `if` block밖에서 쓸수는 없습니다.
>
>**instance field**는 그냥 **field**라고 하기도 하는데,객체에 묶여있는 변수를 말합니다. 객체안에서 그냥 쓸 수도 있고,객체의 메소드(함수)들도 쓸 수 있습니다..
>
>`public`이 아닐 때 객체 밖에서 그걸 쓰려면  getter setter를 써야합니다.
>
>Example:
>
>```java
>public class Point { 
>    private int xValue; // xValue가 field입니다.
>
>    public void showX() {
>        System.out.println("X is: " + xValue); // 함수가 그냥 변수를 쓰고 있습니다.
>    }
>}
>public class Main { 
>    System.out.println("X is: " + xValue); // xvalue는 private이기 때문에 이 코드는 오류입니다.
>    Point point = new Point();
>    System.out.println("X is: " + point.getxValue()); //이런식으로 쓰려면 Point클래스에 getxValue함수를 구현해줘야 합니다.이 코드자체는 오류입니다.
>}
>```
>
> **input parameter**는 **parameter**나 **argument**라고 불리기도 하는데  메소드나 생성자에 건네주는 것입니다. 그 메소드나 생성자 안에서만 의미를 갖습니다.
>
>Example:
>
>```java
>public class Point {
>    private int xValue;
>    public Point(int x) {
>        xValue = x; //이 x는 이제 끝났습니다.
>   }
>
>    public void setX(int x) {
>        xValue = x;//아까의 x와는 다른 x입니다.
>    }
>}
>```
>
>두 `x` parameter는 다른범위에 묶여있습니다.
>
>**class field** 즉 **static field**는 instance field(field) 와 비슷합니다만 차이점은 인스턴스를 만들지 않고 쓸 수 있단겁니다.
>
>Example:
>
>```java
>System.out.println(Integer.MAX_VALUE);//Integer 클래스가 인스턴스 없이 그냥 사용되고 있습니다.이 코드는 Integer의 최대값을 출력합니다.
>```
>
>`Integer` 의 인스턴스가 필요 없습니다.



의문점 : singleton은 그냥 instance를 하나만 쓰는 것과 뭐가 다른가?static선언과 혹시 모를 추가생성에 대한 제한이 이정도의 가치가 있는가?

//해결필요

## Lazy initialization (초기화 지연)

위의 singleton은 프로그램이 로드될 때 같이 로드된다(static).하지만 싱글톤의 초기화에 시간이 필요할 때가 있다: 예를들면, 파일이나 데이터베이스에서 값을 가져와야 할 때.

다음 구현은 client(`getInstance`를 사용하는)가 필요할 때만 인스턴스를 로드한다 :

```
class Singleton { // 싱글톤 클래스
    
    private static Singleton instance; //싱글톤용 인스턴스 단 하나
    
    private Singleton () { } // private 생성자

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton(); //getInstace가 call(사용)될 때 초기화를 한다.
        }
        return instance;
    }
}
```

싱글톤 구현에 코드를 좀 더 쓰긴 했지만,중요한 특징이 있다: lazy initialization(초기화 지연).이것은 `getInstance` method가 처음 사용되기 전까진  singleton instance 가 초기화 되지 않는다. 이 테크닉은 필요할 때 초기화가 된단걸 보장해준다.

이 싱글톤 클래스는 멀티쓰레딩문제에 치명적이기 때문에 단일 쓰레드 환경에서만 써야한단걸 명심하라. 다른 토픽에서 어떻게 thread-safe(쓰레드에서 안전)한 싱글톤을 구현하는지 설명할 것이다.

## Java library의Singleton pattern 

자바 라이브러리에 싱글톤패턴이 많이 있다:

- [`java.lang.Runtime#getRuntime()`](http://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html#getRuntime--)
- [`java.awt.Desktop#getDesktop()`](http://docs.oracle.com/javase/8/docs/api/java/awt/Desktop.html#getDesktop--)
- `java.lang.System#getSecurityManager()`

싱글톤을 확인하려면,늘 동일한 인스턴스를 리턴하는 클래스의 static한 method를 보자.

유명한 프레임워크 (such as Spring, Java EE)들도 singleton을 쓴다.

# Kotlin에서의 Singleton

코틀린에서의 싱글톤은 이런 문법적인 요소들이 간단하게 되어 더 간편하고 직관적으로 쓸 수 있다.

[kotlin object](Kotlin Object.md)

## 비판

singleton pattern은 anti-pattern이라고 욕먹을 때도 있다: 필요 없는데 쓰이고,쓸데없는 제한을 부과하거나 프로그램전체를 관통하는 상태(위험하다)를 유도한다.꿀팁: 진짜로 singleton이 필요할 때 현명하게 쓰자.