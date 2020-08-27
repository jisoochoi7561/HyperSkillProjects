## Theory: Thread management

우리는 이미 스레드를 실행하는법 -`start`-를 알았다. 하지만 가끔 스레드의 라이프사이클을 컨트롤 할 필요가 있다.그냥 실행하고 내버려 두는게 아니라.

우리는 두 개의 중요한 메소드를 배울 것이다 : `sleep()` and `join()`.

두 메소드 모두 checkedExcpetion인 `InterruptedException` 을 throw할 수 있다.우리는 그건 그냥 생략할 것이다. 알아서 exception처리 하도록.

## Sleeping

스태틱 메소드인 `Thread.sleep()` 은 현재 실행되고 있는 스레드를 입력되는 millisecond만큼 실행을 지연하게 한다. 이것은 다른 스레드가 실행될 수 있게 여유를 만들어주는 좋은 방법이다.

우리는 이것을 자주 쓸 것이다.  - 어렵고 자원을 많이 쓰는 과제대신에 설명용으로 쓰려고-

```java
System.out.println("Started");

Thread.sleep(2000L); // suspend current thread for 2000 millis 무려 2000초정도 걸리는 어려운 과제를 했다고 생각하자:앞으로의 코드에서는
         
System.out.println("Finished");
```

코드를 잘 살펴보자. 이 코드는 **"Started"**를 프린트한다. 그리고 스레드는 2000 millisecond(이상일 수도 있다.컴퓨터도 시간에 완벽할 순 없다. 하지만 최소한 더 적진 않다)동안 지연된다. 그리고 깨어난 다음 **"Finished"**를 출력한다.

스레드를 재우는 또 다른 방법은 특별한 클래스 `TimeUnit` 을 `java.util.concurrent:`패키지에서 가져다 쓰는 것이다.

- `TimeUnit.MILLISECONDS.sleep(2000)` performs `Thread.sleep` for 2000 milliseconds;
- `TimeUnit.SECONDS.sleep(2)` performs `Thread.sleep` for 2 seconds;

다른 방식들: `NANOSECONDS`, `MICROSECONDS`, `MILLISECONDS`, `SECONDS`, `MINUTES`, `HOURS`, `DAYS`.

## Joining

 `join` 메소드는 현재의 스레드를 메소드를 콜한 다른 스레드가 완료될 때 까지 기다리게 한다. 다음 코드에서, **"do something else"**는 `mythread`가 종료 될 때까지 출력되지 않는다.

```java
Thread mythread = ...
thread.start(); // start thread

System.out.println("Do something useful");

mythread.join();  // waiting for thread to die

System.out.println("Do something else");
```

이 메소드는 대기시간을 millisecond로 설정해준 오버로드 버전이 있다:

```java
thread.join(2000L);
```

이것을 쓰면 스레드가 끝나기를 너무 오래-어쩌면 영원히-기다리는 걸 막아준다.	

다른 예를 보자.`Worker` 클래스는 "어려운 문제"를 "sleep"이라고 치고 구현했다:

```java
class Worker extends Thread {
    
    @Override
    public void run() {
        try {
            System.out.println("Starting a task");
            Thread.sleep(2000L); // it solves a difficult task
        } catch (Exception ignored) {
        }
    }
}
```



여기 `main` 메소드 (**main**스레드)에서 `worker`가 끝나기를 기다리고 있는걸 보라.

```java
public class JoiningExample {
    public static void main(String []args) throws InterruptedException {
        Thread worker = new Worker(); //worker생성
        worker.start(); // start the worker
       
        Thread.sleep(100L); //잠깐 쉬고
        System.out.println("Do something useful"); //뭔가를 한다
        
        worker.join(3000L);  // waiting for the worker 3000까지만 worker를 기다린다.
        System.out.println("The program stopped");
    }
}
```

메인 스레드는 `worker` 를 기다리고 있고,  **"The program stopped"** 메시지를 worker가 끝날 때 까지 출력할 수 없다.

**"Starting a task"** 와 **"Do something useful"**은 순서를 확정할 수 없다는 것은 당연하다.

경우 1:

```java
Starting a task
Do something useful
The program stopped
```

경우 2:

```java
Do something useful
Starting a task
The program stopped
```

확실한 것은 The program stopped가 마지막이란 것이다.