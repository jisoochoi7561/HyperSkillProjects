## Theory: Inside the JVM



## What is Java Virtual Machine?

**Java Virtual Machine (JVM)**은 물리적인 컴퓨터의 가상시뮬레이션이다. 이것은 컴파일된 자바프로그램(바이트 코드)를 실행한다.JVM은 운영체제에서 실행되는 프로그램이고 자바프로그램을 위한 환경을 제공한다.

이것이 가상머신이기 때문에 , 자바프로그램들은 플랫폼에 독립적이고 여러 하드웨어와 여러 OS에서 사용 될 수 있다 : **WORA** (*Write Once Run Anywhere*)

JVM은 여러 종류가 있다.HotSpot 이 현재 가장 잘나가는 JVM이다. 이것은 오라클자바와 오픈JDK에서 쓰인다.

많은 JVM들(HotSpot포함)이  [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se9/html/index.html)을 준수해서 개발되었다. 이걸 읽을 필요는 없고 이런 규칙이 존재한단것만 알아두라.

## The JVM internals overview

자바 프로그램의 컴파일이 완료되면 **.class**확장자를 가지는 파일이 생긴다. 여기에 자바 바이트코드가 들어있다. 이 코드를 실행하려면 JVM에 로드해야 한다. JVM이 프로그램을 실행하면 바이트코드를 플랫폼 네이티브 코드로 바꾼다.

>플랫폼 네이티브 코드란:
>
>머신코드와 뭐 거의 비슷한 의미로 쓰인다.
>
>기계어라고 보면 될듯.
>
>CPU마다 기계어가 다르다는 것을 이번에 알았다. 제조사가 정하는 것이다.
>
>그래서 바이트코드를 CPU에 맞는 기계어로 바꿔주는 과정이 필요하다.



JVM은 보통 다음과 같은 일을한다:

- loads bytecode; 바이트코드 로드
- verifies bytecode; 바이트코드 검사
- executes bytecode;  바이트코드 실행
- provides the runtime environment. 런타임 환경 제공

보통 JVM의 구조:

![img](https://ucarecdn.com/1e15b25e-8cb4-42d4-9c3f-8a697c9458b4/)

**The common JVM architecture**

세세히 설명들어간다~

## The class loader subsystem

이 부분은 자바바이트코드를 로드한다. 그리고 검사한다. 그리고 바이트코드를 위해 메모리를 할당한다. 이 서브시스템은 나중에 따로 공부할 것이다. 바이트코드를 검사하기 위해 **bytecode verifier**라는 모듈이 존재한다. 이 모듈은 코드가 위험한 일을 요구하지 않는지 - 프라이빗에 접근한다던가- 검사한다.

## The runtime data areas

이 **subsystem** 은 **JVM memory**이다. 이 부분은 프로그램 실행중 다양한 목적으로 쓰인다.

- **PC register** 는 현재 실행중인 부분의 주소를 기억한다;
- **stack area** 는 메소드콜과 지역변수가 저장되는 메모리이다;
- **native method stack** 은 네이티브 메소드 정보를 저장한다;
- **heap** 은 모든 객체를 저장한다;
- **method area** 는 클래스레벨의 모든 정보 - 클래스이름, 부모클래스,메소드,스태틱변수등-를 저장한다.

모든 스레드는 각각의 **PC register**, **stack**,**native method stack**을 가지지만 그것들은 같은 **heap**과 **method area**를 공유한다.



## Execution engine

이것은 프로그램(바이트코드)의 실행을 책임진다. 이것은 JVM의  data영역과 상호작용한다. execution engine은 다음으로 구성된다:

- **bytecode interpreter** 은 바이트코드를 한줄씩 해석해서 실행한다(살짝 느림);
- **just-in-time compiler** (JIT compiler)은 바이트코드를 네이티브 기계어로 바꾼다(프로그램을 실행하는동안) 이것은 인터프리터보다 빠르다.
- **garbage collector**는 heap영역에서 안쓰이는 객체를 지운다.

JVM은 **bytecode interpreter** 와 **just-in-time compiler**를 둘다 가질수도 있고 하나만 가질 수도 있다. javac와 헷갈리지마라.javac는 소스코드를 바이트코드로 컴파일한다. javac는 JVM에 포함되어있지 않다.

## Interfaces and libraries

jVM의 또다른 중요한부분이다:

- **native method interface** 는 자바코드와 네이티브메소드라이브러리사이에 인터페이스를 제공한다.
- **(C/C++파일로 이루어진)native method library**는 네이티브코드를 실행할 때 필요하다.

JVM은 많은 부분으로 이루어져 있다. 우리는 전부 다루진 않을 것이다. 클래스로더와 가비지콜랙션은 따로 배울 것이다.