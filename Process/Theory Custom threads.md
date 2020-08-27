## Theory: Custom threads

**main**스레드가 바로 시작점이다. 거기서부터 새로운 스레드들을 생성한다. 그러려면 각 스레드들에 쓰일 코드를 쓰고 스레드를 실행시켜야 한다.

## Create custom threads

자바는 기본적으로 두가지 방법으로 새로운 스레드를 생성할 수 있다.

- `Thread` 클래스를 extend하고 `run` 메소드를 오버라이드 해서.

```java
class HelloThread extends Thread {  //Thread를 상속받는 스레드를 만든다

    @Override
    public void run() { //run을 오버라이드
        String helloMsg = String.format("Hello, i'm %s", getName());
        System.out.println(helloMsg);
    }
}
```

- `Runnable` 인터페이스를 implement해서 만든 클래스를 `Thread` class의 생성자에 넘겨준다.

```java
class HelloRunnable implements Runnable {

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        String helloMsg = String.format("Hello, i'm %s", threadName);
        System.out.println(helloMsg);
    }
}
```

어느 경우든, 반드시 `run` 메소드를 오버라이드 해야한다. 이 메소드가 일을 하는 부분이다. 어느방식을 택할지는 당신의 선택이다. 만약에 `Thread` 클래스를 extend하기로 했다면 , `Thread` 클래스의 필드와 메소드를 활용할 수 있겠지만, 다른 클래스를 상속받을 수는 없다 - 자바는 다중상속을 지원하지 않기 때문에.

두가지방식으로 만든 스레드 객체가 각각 있다:

```java
Thread t1 = new HelloThread(); // a subclass of Thread

Thread t2 = new Thread(new HelloRunnable()); // passing runnable
```

이름을 지어주는 방법이다.:

```java
Thread myThread = new Thread(new HelloRunnable(), "my-thread");
```

람다식에 익숙하다면 한번에 처리할 수 있다:

```java
Thread t3 = new Thread(() -> {
    System.out.println(String.format("Hello, i'm %s", Thread.currentThread().getName())); //하나의 run메소드기 때문에 그냥 람다로 써준다.
});
```

스레드 객체를 만들었지만 ,이제 시작일 뿐이다. 하고싶은 실행을 하려면 , 스레드를 시동하는 법을 알아야한다.

## Starting threads

 `Thread` 클래스는  `start()` 라는 메소드로 스레드를 실행한다. 이 메소드를 실행하면 곧 `run` 메소드가 자동으로 시작된다. 즉시되는 것은 아니라는 것을 기억하라.

 **main**메소드 안에  `HelloThread` 인 `t` 를 만들고 실행시키자.

```java
Thread t = new HelloThread(); // an object representing a thread
t.start();
```

이것을 이걸 프린트한다:

```no-highlight
Hello, i'm Thread-0 //start에 의해 자동으로 run이 실행된 모습이다.
```

여기 스레드가 어떤식을 start하고, 왜 run이 즉시 실행되지 않는지 설명해놨다.



![img](https://ucarecdn.com/9dfe86a1-1880-4e14-9635-015d0091c70c/)



보시듯이 , start와 run 사이에는 약간의 딜레이가 있다.

기본적으로 새로운 스레드는 **non-daemon**모드이다. 까먹으신 분을 위해:**daemon** 과 **non-daemon**의 차이점은 jvm이 **non-daemon**이 아직 실행중이면 프로그램을 종료하지 않는 반면 **daemon** 은 남아있어도 그냥 종료해버린단 것이다.



 `run` 과 `start`를 혼동하지 마라. 당신은 main에서 `start`를 써야한다. 그러면 그 메소드가 알아서 다른 스레드에서`run`을 해줄 것이다. 만약에 당신이 직접 `run`을 쓴다면,코드는 같은 스레드에서 실행될 것이다.

만약 `start`를 두번이상 쓰면 , `IllegalThreadStateException`이다.

한 스레드내에서 코드가 순차적으로 실행된단 것은 명백하다. 하지만 여러개의 스레드 사이에서는 어떤순서로 실행될지 알 수가 없다. 그 경우를 다루는 법은 이 레슨에선 다루지 않겠다.

다음 코드를 보라:

```java
public class StartingMultipleThreads { // 그냥 클래스

    public static void main(String[] args) { //메인함수(메인 스레드)
        Thread t1 = new HelloThread(); //다른스레드
        Thread t2 = new HelloThread();

        t1.start(); //다른스레드에서 곧 run됨
        t2.start();

        System.out.println("Finished");
    }
}
```

출력의 순서는 알 수 없다. 이렇게 되거나:

```no-highlight
Hello, i'm Thread-1
Finished
Hello, i'm Thread-0
```

이렇게 될수도 있다:

```no-highlight
Finished
Hello, i'm Thread-0
Hello, i'm Thread-1
```

이 것이 뜻하는건 우리가 `start` 를 순서대로 했음에도 불구하고 `run` 의 순서는 알 수 없다는 것을 뜻한다.



여러 스레드를 다룰 때 '순서'로 생각하는 것은 틀리다 - 특별한 테크닉이 있는게 아니라면.





## A simple multithreaded program

간단한 멀티스레드 프로그램 - 두개 스레드-를 생각해보자. 첫 스레드는 stdinput에서 숫자를 읽어서 제곱수를 출력한다. 동시에, `main`스레드는 나름대로 자기만의 메시지를 출력한다. 이 스레드 두개는 함께 실행된다(하나가 일을 다하고 그다음 것이 하는게 아니다)

제곱수 출력 스레드 이다.

```java
class SquareWorkerThread extends Thread { // Thread extend로 구현
    private final Scanner scanner = new Scanner(System.in); //스캐너

    public SquareWorkerThread(String name) {
        super(name); //생성자
    }

    @Override
    public void run() {
        while (true) {
            int number = scanner.nextInt();
            if (number == 0) {
                break;
            }
            System.out.println(number * number);
        }
        System.out.println(String.format("%s finished", getName()));
    }
}
```

 `main`함수에서`SquareWorkerThread` 객체가 실행되고, `main`스레드는 메시지를 출력하고 또다른 스레드는 제곱수를 출력한다.

```java
public class SimpleMultithreadedProgram {

    public static void main(String[] args) {
        Thread worker = new SquareWorkerThread("square-worker");
        worker.start(); // start a worker (not run!)

        for (long i = 0; i < 5_555_555_543L; i++) {
            if (i % 1_000_000_000 == 0) {
                System.out.println("Hello from the main!");
            }
        }
    }
}
```

한 경우이다(순서는 알 수가 없다.):

```java
Hello from the main!   // the program outputs it
2                      // the program reads it
4                      // the program outputs it
Hello from the main!   // outputs it
3                      // reads it
9                      // outputs it
5                      // reads it
Hello from the main!   // outputs it
25                     // outputs it
0                      // reads it
square-worker finished // outputs it
Hello from the main!   // outputs it
Hello from the main!   // outputs it

Process finished with exit code 0
```

보시듯이 프로그램은 두개의 작업을 **동시에**하고 있다 : 하나는 `main`스레드 에서, 하나는 `worker`스레드 에서. 실제로 같은 시간인 **동시**가 아니라 각각 순서없이 열심히 하고 있단 뜻이란걸 굳이 언급해 주겠다.