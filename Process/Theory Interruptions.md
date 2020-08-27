## Theory: Interruptions

스레드는 `run` 메소드가 멈출 때 종료된다. 가끔 실행중인 작업을 종료시키고 싶을 수 있다.자바는 **interruptions** 이라는 메커니즘을 제공해서 스레드를 멈추게하거나 다른일을 하게 할 수 있다. 인터렵션은 스레드를 즉시반응하도록 강제하진 않지만 이런 요구가 존재한단 것을 알려준다.

## Interrupt() and isInterrupted()

 `interrupt()` 메소드를  `Thread`인스턴스에서 실행해서 그것의 interrupted flag를 `true`로 만들자

```java
Thread thread = ...
thread.interrupt(); // interrupt this thread
```

이 이벤트에 대한 반응은 인터럽메소드를 콜한 스레드 자체에 의해 결정된다. 가장 흔한 반응은 실행을 멈추는 것이다. 아니면 스레드는 그냥 이 요청을 무시할 수도 있다.

스레드의 현재상태에 따라서 인터럽션은 다르게 다루어진다. `thread.interrupt()`을 쓰면 `InterruptedException` 이 일어날 수도 있다:만약 스레드가 sleep중이거나 다른스레드에 join중이라면. 아니라면 , interrupted flag가 `true`가 될 것이다.

여기 스레드가 인터럽션을 다루는 예시를 보라:

```java
public class CustomThread extends Thread {

    @Override
    public void run() { 
        while (!isInterrupted()) {  
            try {
                doAction();
                Thread.sleep(1000); // it may throw InterruptedException
            } catch (InterruptedException e) {
                System.out.println("sleeping was interrupted");
                break; // stop the loop
            }
        }
        System.out.printf("%s finished", getName());
    }

    private void doAction() {
        // something useful
    }
}
```

이 스레드가 run되는 동안 , 인터럽션은 런 메소드의 모든 문장에서 일어날 가능성이 있다. 루프의 조건체크, doAction실행중,심지어 sleep중.만약 스레드가 슬립중이라면 `Thread.sleep(1000)`이`InterruptedException` 을 throw할것이고 그걸 우리가 다룰 것이다. 그 외엔 , 플래그가 true가 됨에 따라 루프를 탈출하게 될 것이다.
만약에 `Runnable` 을 임플리먼트 하는걸 `Thread` 를 extend 하는 것 보다 선호한다면 스태틱메소드 `Thread.interrupted()`를 런메소드안에서 실행할 수 도 있다.이것과 방금 예시의 차이점은 스태틱메소드 `Thread.interrupted()`은 인터럽션 플래그를 `false`로 리셋한단 것이다.

## An example: counting with interruption

우리는 스레드가 interrupt받지 않으면 숫자를 세나가는 일을 해볼 것이다.

```java
class CountingTask implements Runnable {

    @Override
    public void run() {
        System.out.println("Start counting");
        int i = 1; // the first number to print

        try {
            while (!Thread.interrupted()) {
                System.out.println(i);
                i++;
                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            System.out.println("Sleeping was interrupted");
        }
        System.out.println("Finishing");
    }
}
```

이 구현에서 `sleep` 이 대부분의 시간을 차지하므로 인터럽션은 보통 슬립중에 일어날 것이다. 만약 프로그램이  **"Sleeping was interrupted"** 를 출력하지 않는다면 이것은 스레드가 슬립중이 아닌 동작중에 인터럽션이 일어났다는걸 말한다.

`main` 메소드에서 우리는 일을 할 스레드를 만들고 스레드를 인터럽트 한다.

```java
public class InterruptionDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread counter = new Thread(new CountingTask());
        counter.start();
        Thread.sleep(5000L);
        counter.interrupt();
        counter.join();
    }
}
```

**Note**  `main` 메소드에서  `sleep` 과 `join` 도 `InterruptedException` 을 발생시킬 수 있다.그것의 처리는 생략했다.

출력:

```java
Start counting
1
2
3
4
Sleeping was interrupted
Finishing
```