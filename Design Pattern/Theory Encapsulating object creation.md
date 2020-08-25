## Theory: Encapsulating object creation

캡슐화

## Creating objects in factories     factory로 객체 만들기

가끔 우리는 클래스들의 계층구조가 하나의 base class(또는 인터페이스)와 여러개의 subclass들로  이루어진 걸 볼 수있다. 그리고 우리는 객체를 만들 때 서브클래스의 타입을 고려해야 한다. 실제로 객체가 사용되는 클라이언트 코드에 `new` 생성자를 쓰는 것보다, 생성용 코드를 다른 곳에 캡슐화 해놓고 그걸 클라이언트코드에서 불르는게 더 편리하다. 캡슐화 해놓는 곳을 **factory**라고 부른다. factory는 계층구조를 이루는 클래스들의 인스턴스를 만든다. 이것덕분에 우리는 클라이언트코드를 단순하게 작성하고 계층구조에 클래스가 추가될 때 오류를 안일으키게 할 수 있다.

따라서 **factory** 는 객체를 만드는 방식중의 하나이다 - 프로그램의 한 부분(클래스나 메소드)이 객체를 만들고 한부분은 그걸 쓰는-

factory는 여러 종류가 있다: **static factory**, **simple factory**, **factory method** and **abstract factory**. 이 토픽에서, 우리는 앞의 두개를 공부할 것이다; 그것들은 보통 디자인패턴이라기보단 *관용구*라고 불린다. 뒤의 두개는 진짜로 디자인패턴이고, 저 관용구들과 같은 원리를 가진다.

## Static factory idiom

예시로, 우리는 다음과같은 계층구조의 컴퓨터들을 볼것이다:

```java
class Computer {  //하나의 basic class가 있다
    
    private long ram;
    private long cpu;
    
    // getters and setters
}

class PC extends Computer {  //subclass of computer
    
    // additional members
}

class Laptop extends Computer {  //subclass of computer
    
    // additional members
}
```



**The static factory** 는 우리가 쓸 수 있는 가장 간단한 팩토리이다. 이것은 하나의 스태틱 메소드를 가지고있는데 그 메소드가 객체들을 만든다. 이 메소드는 타입을 스트링이나 이넘인자로 받아서 그것과 관련된 서브클래스 인스턴스를 베이트클래스 타입으로 리턴한다.

새로운 컴퓨터를 만들기 위해 우리는 스태틱팩토리를 써본다:

```java
class ComputerStaticFactory {  //스태틱팩토리
    
    public static Computer newInstance(String type) { //하나의 스태틱 메소드가 타입을 스트링형식으로 입력받고 있다
        if (type.equals("Computer")) { //타입에 따라
            return new Computer(); //서브클래스 인스턴스를 만든다
        } else if (type.equals("PC")) {
            return new PC();//서브클래스 인스턴스를 만든다
        } else if (type.equals("Laptop")) {
            return new Laptop();//서브클래스 인스턴스를 만든다
        }
        return null; // if not a suitable type
    }
    //스태틱 메소드의 리턴형이 Computer인것에 주의
}
```



넘겨받은 타입에 따라서, 적절한 if절이 실행될 것이다. 우리는 또한 `switch`로도 할 수 있다.

다음 클라이언트 코드는 두개의 컴퓨터를 만들고 있다: a laptop and a PC:

```java
public class FactoryClient {
    
    public static void main(String args[]) {
        
        Computer pc = ComputerStaticFactory.newInstance("PC");
        System.out.println(pc instanceof PC); // prints "true"
       
        Computer laptop = ComputerStaticFactory.newInstance("Laptop");
        System.out.println(laptop instanceof Laptop); // prints "true"
    }
}
```

`Computer` 를 상속받는 또다른 컴퓨터타입을 선언하고 인스턴스를 만드는 것은 전혀 어렵지 않다.

이걸 쓰는 여러방법:

-   `ComputerStaticFactory` 의`newInstance` 메소드가 이넘타입을 받아서 서브클래스타입을 결정한다;
- 메소드 `newInstance`가 맞는타입을 못찾으면 `null`을 리턴하지 말고 exception을 throw한다.
- 메소드 `newInstance` 을 베이스클래스: `Computer` 의 메소드로;
- 팩토리는 여러개의 메소드를 쓸 수도 있다.

## Simple factory idiom



The simple factory idioms은 static factory 와 생성하는 메소드가 스태틱이 아니라는 점에서 다르다.

```java
class ComputerFactory {
    
    // it may contain some fields
    
    public Computer newInstance(String type) {
        if (type.equals("Computer")) {
            return new Computer();
        } else if (type.equals("PC")) {
            return new PC();
        } else if (type.equals("Laptop")) {
            return new Laptop();
        }
        return null;
    }
}
```



클라이언트 코드에서 우리는 팩토리의 인스턴스를 만들고 메소드를 실행해야한다(스태틱이었다면 인스턴스를 안만들고 클래스레벨에서 실행이 가능했다.):

```java
ComputerFactory factory = new ComputerFactory();  //팩토리 인스턴스를 만든다
Computer pc = factory.newInstance("PC"); // 팩토리 인스턴스에서 메소드를 실행한다.
```

심플팩토리와 스태틱팩토리는 거의 같아보인다. 하지만 스태틱팩토리와 다르게 이것은 여러종류의 팩토리를 만들어서 생성을 더 제어할 수 있다. 당신은 또 팩토리의 서브클래스를 만들어서 생성메소드를 오버라이드 할 수도 있다.

## Conclusion

두 팩토리 관용구를 배웠다: **static factory** and **simple factory**. 이것들은 사실 디자인패턴이라기보단 관용구이다. 하지만 둘다유용하다. 나머지 팩토리들도 비슷한 원리이다.

우리는 계층구조의 객체를 생성하는 코드들을 모아서 팩토리에 캡슐화한다. 팩토리는 어떤 객체를 만들지 받는 파라미터를 갖고 적절한 인스턴스를 클라이언트 코드에 리턴해준다. 이것은 클라이언트코드를 클래스계층구조와 분리해서 안전하고 심플하게 만든다.

기억할 것은 여러 클라이언트가 팩토리를 쓸 수 있다는 것이다. 그러면 `new` 로 생성하는 코드들을 여러개 복사하는 대신 팩토리를 써서 간편히 할 수 있다.

