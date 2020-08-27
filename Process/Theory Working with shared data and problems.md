## Theory: Working with shared data and problems

공유되는 데이터를 가지고 일할 때 발생하는 문제들.

## Sharing data between threads

같은 프로세스에 속해있는 스레드들은 같은 메모리공간을 공유한다 (이 공간을 **Heap**이라고 한다) 스레드들은 메모리에 있는 데이터를 서로 공유 할 수 있다. 같은 데이터를 여러 스레드가 접근하려면 , 각각의 스레드는 이 데이터에 대한 참조를 알아야한다(객체로 알면 된다). 아래 그림을 보라.

**![img](https://ucarecdn.com/b2ad2d24-f178-40de-94df-2d65f09f4215/)**

**여러개의 스레드(같은 프로세스)가 heap에 잇는 object들을 참조하고 있다.**

 `Counter`클래스로 예를 들어보자.

```java
class Counter {

    private int value = 0;

    public void increment() {
        value++;
    }

    public int getValue() {
        return value;
    }
}
```

이 클래스는 두개의 메소드: `increment` 와 `getValue`가 있다.`increment` 를 실행할 때 마다 `value`에 1이 더해진다. 그리고 `getValue` 는 현재 value를 리턴한다.

그리고 `Thread`를 상속받은 MyThread클래스가 있다:

```java
class MyThread extends Thread {

    private final Counter counter;  // 카운터 단 하나를 final로 놨다.

    public MyThread(Counter counter) {
        this.counter = counter; //생성자
    }

    @Override
    public void run() {
        counter.increment();
    }
}
```

 `MyThread` 의 생성자는  `Counter` 인스턴스를 받아서 필드에 저장해놓는다. `run`을 하면 저장해둔 `counter`인스턴스의 `increment`를 실행한다.

`Counter`인스턴스를 만들고 `MyThread`인스턴스 두개를 만들자.  `MyThread` 의 인스턴스 두개는 같은 `counter` object를 참조하고 있다.

```java
Counter counter = new Counter();

MyThread thread1 = new MyThread(counter);
MyThread thread2 = new MyThread(counter);
```

스레드를 하나씩 실행하고 `counter.getValue()`를 써보자

```java
thread1.start(); // start the first thread
thread1.join();  // wait for the first thread

thread2.start(); // start the second thread
thread2.join();  // wait for the second thread

System.out.println(counter.getValue()); // it prints 2
```

답은 2 이다. 왜냐면 각각의 스레드가 같은 객체를 다루기 때문에 각각 한번씩 총 두번 `increment`된 것이다.

이 예에서, 우리는 첫 스레드를 실행하고 끝나기를 기다리고(이 때 첫 increment가 실행완료됬다)그리고 두번째 스레드를 시작하고(여기서 두번쨰 increment)기다렸다. 그리고 getvalue를 출력했다. 결과는 완벽히 예상대로 이다.

같은 데이터를 동시에 다루는 여러 스레드들을 다룰 때 몇가지 기억해야할 것:



- 어떤 동작들은 non-atomic이다;
- 한 스레드가 실행한 것은 다른 스레드가 모를 수 있다;
- 만약 실행했단 것은 알 수 있어도, 순서는 모를 수도 있다(순서 뒤바뀜).



더 자세히 알아보자



## **Thread interference**

non-atomic operation은 여러 단계로 이루어진 operation이다. 스레드는 다른 스레드의 non-atomic operation도중의 중간값을 가져다 쓸 지도 모른다. 이것은 **thread interference**라는 문제를 발생시킨다 : 스레드들의 non-atomic 실행이 겹칠 수 있다.

**increment** 가 non-atomic operation이란 것 먼저 설명하자. 다시 `Counter` 클래스를 보자:

```java
class Counter {

    private int value = 0;

    public void increment() {
        value++; // the same thing as value = value + 1   
    }

    public int getValue() {
        return value;
    }
}
```



주의: 이전에 든 예에서는 스레드들은 데이터를 동시에 다루지 않았다(끝나기를 join으로 기다렸다).두번째 스레드가 실행될 때 첫번째 스레드는 완전히 끝나있었다.

 `value++` 는 3가지 단계로 이루어져 있다:

1. 현재값을 읽는다;
2. 거기에 1을 더한다;
3. 그 값을 필드에 다시 할당한다;

increment가 non-atomic이고 3단계로 이루어져 있기 때문에 **thread interference**가 발생할 수 있다.



당연히  `value--` 도 3단계이다.



 `Counter` class의 instance가 있다고 치자:

```java
Counter counter = new Counter();
```

필드의 초기값은 0이다.

이제 `Thread A `가 `increment` 를 이 instance에서 실행하고 `Thread B `도 똑같이 한다고하면, 이런 일이 발생한다:

1. **Thread A:** 변수에서 값을 읽음 (0읽음)
2. **Thread** **A:** 그 값에다 1더함(1인지중)
3. **Thread B:** 변수에서 값을 읽음 (여전히 0임).
4. **Thread A:** 값을 변수에 씀(변수는이제 1).
5. **Thread B:** 읽은 값에 1을 더함(0에다 1더해서 1임)
6. **Thread B:** 변수에 값을 씀 (변수는 이제 1).

이 경우 `increment` 를 두개의 스레드가 실행했는데 우리의 예상인 2 가 아니라 1이 나와버린다.이 것은 `Thread A`가 한 일은 증발해 버리고 `Thread B`에 의해 덧씌워졌단 뜻이다. 가끔은 값이 맞을 수도 있겠지만, 그것은 그냥 우연일 뿐이다.

값을 증가시키고 감소시키는게 왜 non-atomic operation인지 보았다.

이 토픽에서, 우리는 이걸 해결하는법은 배우지 않을 것이다. 그냥 이런 문제가 있단것만 기억해두라.

또 다른 경우를 보자: 64-bit value의 할당. 놀랍게도, 어떤 플랫폼에서는 `double` 과 `long` type (64-bits)을 읽고 쓰는 것조차 non-atomic일 수 있다.

```java
class MyClass {

    long longVal; // reading and writing may be not atomic

    double doubleVal; // reading and writing may be not atomic
}
```

이것이 뜻하는 것은 , 어떤 스레드가 변수에다 값을 쓸 때, 다른 스레드가 그 중간값에 접근(예를 들면 , 64비트중 32비트만 아직 작성되있는경우!)할 수 있다는 것이다. 이 실행을 atomic하게 만들려면, 필드를 `volatile` keyword로 선언해야한다.

```java
class MyClass {

    volatile long longVal; // reading and writing is atomic now

    volatile double doubleVal; // reading and writing is atomic now
}
```

다른 원시타입들 (boolean, byte, short, int, char, float) 은 **atomic**인 것이 보장된다.

큰 프로그램에서는 **thread interference** 는 찾아서 고치기 어려운 버그가 될 수도 있다.

## Visibility between threads

가끔, 스레드가 공유되는 데이터를 바꿀 때, 다른 스레드가 바뀌었음을 인지 못하거나 순서를 착각할 수 있다. 이것은 다른 스레드들은 같은 데이터지만 다른 관점을 가질 수 있다는 것이다.

이유는 맨날 다르다 - caching values for threads, compiler optimization등등. 다행히,보통 프로그래머들이 이 이유들을 다 이해할 필요는 없다. 필요한 것은 이것을 피할 전략뿐이다.

**Example.**  `int` 필드를 정의하고 초기화 했다:

```java
int number = 0;
```

두 스레드가 이것을 공유한다:` Thread A` 와 `Thread B`.

`Thread A` 가 `number` 에 5를 더한다.

```java
number += 5;
```

그 직후, `Thread B` 가 `number` 를 standard output에 출력한다:

```java
System.out.println(number);
```

결과는? 0 아니면 5

`Thread A`가 한 변화가  `Thread B`의 관점에 있는지 보장할 수가 없다.

우리가 말했던 keyword `volatile`이 **visibility**에 관여한다. 한 스레드가 한 변화를 다른 스레드에 보여주려면, 필드를 `volatile`로 선언해야 한다

```java
volatile int number = 0;
```

필드가 **volatile** 이라면 이 필드에 행해진 모든 변화가 다른 스레드들에게도 보인다는 것이 보장된다.

 `volatile`키워드는 인스턴스나 스태틱 필드에서 쓰일 수 있다.

## Other cases of visibility

가끔 `volatile` 을 안써도 된다.다음 방식들도 visibility를 보장한다:

- 다른 스레드가 start 되기전에 변화가 완료된 경우;
-  `join` 으로 순서가 제어된 경우(우리가 여기서 했다)

모든 방법을 다루진 않을 것이다.방법들은 **"Happens-before"**이라는 특별한 이름의 관계로 정리되어 있다. 지금 당장은 `volatile` 과 위의 두 방법을 기억해두자.

## More on volatile keyword

 `volatile`키워드는 우리가 한 스레드의 실행결과를 다른 스레드가 볼 수 있게 해준다. 이것은 `double`과 `long`을 atomic하게 만들어준다.하지만 increment를 atomic하게 만들어 줄 수는 없다.

 `volatile`을 사실 더 복잡한 키워드지만 , 지금은 그냥 넘어가자.