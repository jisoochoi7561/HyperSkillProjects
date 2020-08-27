## Theory: Thread synchronization 스레드 동기화

공유되는 데이터들을 동시에 작업한다는 것은 기대하지 않은 - 어쩌면 에러인- 결과를 낳을 수 있다.다행히도 자바는 공유자원에 여러 스레드가 접근하는 것을 컨트롤 하는 메커니즘을 제공한다. 이 메커니즘을 **thread synchronization**라고 한다

## Important terms and concepts

우리가 코드에 동기화를 적용하기전에 용어와 컨셉을 정확히 알고 가자.

1) A **critical** **section** 은 코드의 부분이다 : 공유되는 자원에 접근하는. 그리고 하나 이상의 스레드가 동시에 이걸 사용해서는 안된다. 공유되는 자원이란 변수,파일,인풋아웃풋,데이터베이스 등등이다.

예를 보자. 클래스에 스태틱 필드 `counter`가 있다:

```java
public static long counter = 0;
```

두 스레드가 필드를 increment(1증가) 10 000 000번을 동시에 한다고 하자. 마지막 값은 20 000 000이 되어야 겠지만, 우리가 앞에서 봤던 것처럼, 다른 값이 나올 수있다.9 999 843 라던가.

이것이 일어나는 이유는 가끔 스레드가 공유자원에 다른 스레드가 만든 변화를 눈치채지 못하고, 또 non-atomic 실행의 중간값을 가지고 일하기 때문이다. 이것들이 우리가 공유자원을 다룰 때 생각해야 하는 visibility와 atomic problem이다.

이것이 바로 여러 스레드로 값을 증가시키는게 **critical section**인 이유다. 물론, 이  예시는 무척 간단하지만, 임계구역(**critical section**.)은 더 복잡할 수 있다.

2)  **monitor** 는 특별한 메커니즘이다 : 객체에 대해 동시제 접근하는 것을 컨트롤 하는. 자바에서, 각각의 객체와 클래스들은 관련된 모니터를 암시적으로 갖고 있다. 한 스레드가 모니터를 얻을 수 있는데, 그러면 다른 스레드는 모니터를 얻지 못한다. 그러면 주인(모니터를 갖고있는)이 그걸 내놓을 떄까지 기다려야한다.

따라서 스레드는 객체의**monitor** 에 의해 잠겨서 기다리게 될 수 있다. 이 메커니즘이 프로그래머들에게 여러스레드가 동시에 **critical section**에 접근하는 것을 막는 방법을 제공한다.

## The synchronized keyword

"고전적"이고 가장 간단한 코드보호 방법은 바로 **synchronized**키워드를 사용하는 것이다.

이것은 두 방식으로 사용된다:

- synchronized method (a static or an instance method) 싱크로나이즈된 메소드(스태틱이거나 인스턴스 메소드)
- synchronized blocks or statements (inside a static or an instance method) 싱크로나이즈된 블럭이나 문장(스태틱이나 인스턴스 메소드 안의)

싱크로나이즈된 메소드나 블럭은 스레드를 잠그려면 객체가 필요하다. 그 객체의 모니터가 특정 critical section에 대한 동시접근을 관리한다. 한번에 하나의 스레드만이 싱크로나이즈된 블럭이나 메소드에 접근할 수 있다. 다른 스레드들은 그 스레드가 나올 떄 까지 기다려아한다.

## Static synchronized methods

우리가 스태틱 메소드를 싱크로나이즈 하면(**synchronized** 를 써서) 쓰이는 모니터는 클래스의 모니터이다.  한번에 하나의 스레드만이 이 함수를 실행할 수 있다. 정리하자면 "*클래스당 하나의 스레드*"

여기 예시가 있다 - 하나의 싱크로나이즈된 스태틱메소드 `doSomething`을 가지는 클래스:



```java
class SomeClass {

    public static synchronized void doSomething() {  //싱크로나이즈된 스태틱 메소드
        
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s entered the method", threadName));
        System.out.println(String.format("%s leaves the method", threadName));
    }
}
```

메소드 `doSomething` 이 싱크로나이즈 되어있다. 이것은 한번에 하나의 스레드만이 실행할 수 있다. 이 메소드는 스태틱메소드가 포함된 SomeClass로 싱크로나이즈된다(모니터가`SomeClass`의 모니터다 ).

동시에 두개의 스레드에서 메소드를 불러보자.결과는 늘 같다:

```java
Thread-0 entered the method
Thread-0 leaves the method
Thread-1 entered the method
Thread-1 leaves the method
```

두 스레드가 동시에 각각 메소드를 실행할 수는 없다.

## Instance synchronized methods

인스턴스 메소드는 인스턴스(객체)에 의해 싱크로나이즈된다. 모니터는 현재의 객체(`this`)의 것이다. 만약 우리가 클래스의 인스턴스 두개를 갖고 있다면 그것들은 각각의 모니터를 갖고 있다.

단 하나의 스레드만이 특정 객체의 메소드를 실행할 수 있다. 하지만 스레드들은 여러객체의 메소드를 실행할 수 있다.이것은 *"인스턴스당 하나의 스레드"*.

여기 `doSomething`메소드를 가지는 예시가 있다. 인스턴스를 구별하기위해 생성자도 구현했다.

```java
class SomeClass {

    private String name;

    public SomeClass(String name) { // 이름구별용 생성자
        this.name = name;
    }

    public synchronized void doSomething() { //싱크로나이즈된 인스턴스 메소드

        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s entered the method of %s", threadName, name));
        System.out.println(String.format("%s leaves the method of %s", threadName, name));
    }
}
```

두개의 인스턴스를 만들어서 3개의 스레드로 `doSomething`을 해보자. 처음 두개는 같은 인스턴스에서 실행하고 3번째거는 나머지 하나의 인스턴스에서 실행한다.

```java
SomeClass instance1 = new SomeClass("instance-1");
SomeClass instance2 = new SomeClass("instance-2");

MyThread first = new MyThread(instance1);
MyThread second = new MyThread(instance1);
MyThread third = new MyThread(instance2);

first.start();
second.start();
third.start();
```

결과:

```java
Thread-0 entered the method of instance-1
Thread-2 entered the method of instance-2
Thread-0 leaves the method of instance-1
Thread-1 entered the method of instance-1
Thread-2 leaves the method of instance-2
Thread-1 leaves the method of instance-1
```

 `**instance-1**`의`doSomething`은 동시에 실행되지 않는다. 여러번 실험해보라

## Synchronized blocks (statements)

가끔은 메소드의 일부분만을 싱크로나이즈 하고 싶을 수 있다. 이것은 synchronized blocks (statements)를 쓰면 가능하다. 이것들은 스레드를 잠가줄 객체를 특정해야한다.

여기에 스태틱과 인스턴스 메소드를 가지는 클래스가 있다. 두 메소드들은 unsynchronized 지만 synchronize된 부분을 갖고 있다.



```java
class SomeClass {

    public static void staticMethod() {

        // unsynchronized code
                
        synchronized (SomeClass.class) { // synchronization on the class
            // synchronized code
        }
    }

    public void instanceMethod() {

        // unsynchronized code

        synchronized (this) { // synchronization on this instance
            // synchronized code
        }
    }
}
```

 `staticMethod` 안의 블럭이 클래스에 의해 싱크로나이즈 된다 - 하나의 스레드만 이거 실행가능-.

 `instanceMethod` 안의 블럭은 `this` 인스턴스에 의해 싱크로나이즈 된다 - 하나의 스레드에 한 객체 - 

싱크로나이즈 블럭들은 메소드 싱크로나이즈와 비슷하지만 부분만을 싱크로나이즈 하게 해준다.

## Synchronization and visibility of changes

*Java Language Specification*은 만약 같은 모니터로 싱크로나이즈 된다면 스레드들이 서로 한 일을 볼 수 있음을 보장해준다. 더 정확히 말하자면 , 만약 스레드가 공유자원(예를들면, 변수)을 싱크로나이즈된 블럭이나 메소드안에서 바꾸고, 모니터를 내놓으면 , 다른 스레드들은 모니터를 얻고(모니터가 있어야 그걸 쓸 수 있다) 변화를 알게된다.

## Example: a synchronized counter

여기 예시가 있다. 이것은 싱크로나이즈된 카운터 클래스이다. 두개의 싱크로나이즈된 인스턴스 메소드: `increment` 와 `getValue`를 가진다

```java
class SynchronizedCounter {
    
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getValue() {
        return count;
    }
}
```

여러개의 스레드가 같은 인스턴스의 `increment` 를 쓰려해도 아무 문제가 없다.**synchronized** 키워드가 공유되는 필드를 보호하기 떄문이다. 단 하나의 스레드만이 필드에 접근 할 수 있다. 다른 스레드들은 그 스레드가 모니터를 내놓을 때까지 기다려야 한다. 변수 `count` 의 모든 변화들은 visible하다.

메소드 `getValue` 는 필드를 수정하지 않는다. 그냥 값을 읽을 뿐이다. 메소드는 싱크로나이즈 되있고 덕분에 값을 읽는 스레드는 언제나 제대로 된 값을 읽는다(increment이후에 모니터를 받아서 읽으니까); 만약 싱크로나이즈가 아니라면 `**count**`가 변화된 이후의 값인지 보장할 수 없다.

여기에 `Thread`를 extend하는 `Worker` 클래스가 있다. 이 클래스는 `SynchronizedCounter` 의 인스턴스를 받아서 `increment` 를  10 000 000 번 실행한다.

```java
class Worker extends Thread {

    private final SynchronizedCounter counter;

    public Worker(SynchronizedCounter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10_000_000; i++) {
            counter.increment();
        }
    }
}
```

다음 코드가 `SynchronizedCounter`의 인스턴스를 만들어서 스레드로 실행하고 결과를 출력한다.

```java
SynchronizedCounter counter = new SynchronizedCounter(); //인스턴스를 만든다

Worker worker1 = new Worker(counter); //스레드1
Worker worker2 = new Worker(counter); // 같은 카운터를 공유하는 스레드 2

worker1.start(); // 시작
worker2.start(); //시작

worker1.join();//얘네는 이부분의 싱크로나이즈와는 관련없다. 그냥 println이 스레드종료 이후에 되기를 보장해준다.
worker2.join();

System.out.println(counter.getValue()); // the result is 20_000_000 완벽한 값이다.
```

하지만 가끔은 그저 읽기만 하는 메소드(getter라던가)라면 싱크로나이즈 할 필요가 없을 수도 있다:

- 만약에 우리가 `join` 을 사용해서 읽는 것이 쓴 이후가 확실하단걸 보장 한다면 싱크로나이즈할 필요가 없다.

- 공유되는 필드가 **volatile** 라면 우리는 언제나 제대로 된 값을 본다.

싱크로나이즈를 하지 않기로 결정할 땐 신중하라.

## One monitor and multiple synchronized methods and blocks

하나의 모니터에 여러개의 싱크로나이즈 메소드와 블럭들.

**중요해요:**단 하나의 모니터와 단 하나의  스레드를 가지는 객체나 클래스는 같은 모니터에서 싱크로나이즈된 코드를 실행할 수 있다.

이것이 의미하는 것은 만약 클래스가 여러개의 싱크로나이즈된 인스턴스 메소드를 가지고 있고 한 스레드가 그것중 하나를 사용한다면 다른 스레드들은 이 메소드들중 아무것도 사용하지 못한다는 것이다-첫 스레드가 인스턴스의 모니터를 내놓을 때 까지-

예를 들면 : 클래스가 세개의 인스턴스 메소드를 갖고 있고, 그중 두개는 싱크로나이즈고 하나는 내부에 싱크로나이즈 블럭을 갖고 있다. 이 메소드와 블럭들은 모두 `this`인스턴스의 모니터에 의해 싱크로나이즈 된다.

```java
class SomeClass {

    public synchronized void method1() {
        // do something useful
    }

    public synchronized void method2() {
        // do something useful
    }
    
    public void method3() {
        synchronized (this) {
            // do something useful
        }
    }
}
```

만약 스레드가 `method1` 을 실행하면 , 다른 스래드들은 `method2` 나 `method3` 안의 싱크로나이즈된 블럭을 사용할 수 없다.왜냐면 `this` 의 모니터는 이미 사용되고있기 때문이다. 스레드들은 이 모니터가 나오기를 기다릴 것이다.

클래스 모니터의 경우에도 같은 논리가 적용된다.

## Reentrant synchronization   재진입성

스레드는 다른 스레드에 의해 잠긴걸 풀 수없다. 하지만 스레드는 자기가 잠근 걸 풀 수 있다. 이 것을  **reentrant synchronization**라고 한다.

A thread cannot acquire a lock owned by another thread. But a thread can acquire a lock that it already owns. This behavior is called **reentrant synchronization**.

예시:

```java
class SomeClass {

    public static synchronized void method1() {
        method2(); 
        //원래대로라면 메소드1을 쓸 때 모니터를 점유했으니
        //메소드 2가 잠겨서 쓸 수 없어야 한다.
        //하지만 쓸 수 있다.
        // legal invocation because a thread has acquired monitor of SomeClass
        
    }

    public static synchronized void method2() {
        // do something useful
    }
}
```

위의 코드는 작동한다. 만약 스레드가 `method1` 안에 있다면 `method2` 도 실행할 수 있다. 왜냐면 둘다 같은 오브젝트 (`SomeClass`)로 싱크로나이즈 되었기 때문이다.

## Fine-grained synchronization

가끔 클래스는 절대로 동시에 사용되지 않는 필드들을 가질 수 있다. 물론 이것을 모니터를 통해 보호할 수도 있지만, 이경우 우리는 서로 독립적인 필드들임에도 불구하고 한스레드만이 접근할 수 있을 것이다. 더 동시적으로 처리하기 위해 다른 객체의 모니터를 쓰는 지혜를 발휘해보자.

여기 예시가 있다 : 클래스에 두개의 메소드가 있다. 클래스는 각 메소드가 실행된 횟수를 저장하는 필드를 가지고 있다.

```java
class SomeClass {

    private int numberOfCallingMethod1 = 0; //횟수저장용
    private int numberOfCallingMethod2 = 0; //횟수저장용

    final Object lock1 = new Object(); // an object for locking 잠그기 위한 오브젝트
    final Object lock2 = new Object(); // another object for locking 잠그기 위한 오브젝트

    public void method1() {
        System.out.println("method1...");

        synchronized (lock1) {
            numberOfCallingMethod1++;
        }
    }

    public void method2() {
        System.out.println("method2...");
        
        synchronized (lock2) {
            numberOfCallingMethod2++;
        }
    }
}
```

보시듯이 , 클래스는 모니터를 제공하기위한 객체들을 필드에 가지고 있다.

만약에 우리가 클래스의 인스턴스를 만든다면 한 스레드가 method1을 작업하고 다른 한 스레드가 method2를 작업할 수 있을 것이다.

## Synchronization and performance of programs

기억하라, 싱크로나이즈 메커니즘에 의해 보호되는 코드는 한번에 하나의 스레드만이 사용할 수 있다. 이것은 프로그램의 동시성과 반응성을 감소시킨다.

모든 코드들을 싱크로나이즈 하지 마라. 필요할 때에만 쓰라. 코드의 작은 부분들을 싱크로나이즈로 만들어라. 메소드 전체보다는 부분을 싱크로나이즈 하는게 좋을 때가 있다!