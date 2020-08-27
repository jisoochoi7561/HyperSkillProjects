## Theory: Executors

우리는 이미 어떻게 스레드를 만드는지 배웠다 : `Thread` 와 `Runnable` 로.

두 방법 모두 스레드 객체를 만들어서 각각 코드를 실행할 수 있게 해준다. 여러개의 스레드를 만들어서 실행시키는 건 쉽지만 , 동시에 수백 수천개의 스레드가 실행된다면 곤란하다.

덧붙여, `Thread` 는 매우 로우레벨 클래스라서 하이레벨코드들과 섞이면 알아보기 어렵고 이상한 구조가 될 수 있다. 또 잘알려진 에러들을 일으킬수도 있는데 예를 들면 `start()`대신 `run()`을 깨운다거나.

## Tasks and executors

멀티스레드 애플리케이션 개발을 간단히 하기 위해 자바는 `ExecutorService` (or simply **executor**).라는 추상화를 제공한다. 이것은 하나 이상의 스레드들을 캡슐화 시켜서 한 묶음에 넣고 task들을 내부의 큐에 넣어서 스레드로 실행시킨다.

![img](https://ucarecdn.com/f77a4b54-a7b0-43f1-bd0a-d8693a72a4bd/)

이 방식은 깔끔하게 태스크와 스레드를 분리 시키고 우리가 테스크에 집중하게 해준다. 우리는 스레드를 만들고 관리할 필요가 없어진다 - executor 가 해주니까.

## Creating executors

모든 종류의 executor들은  `java.util.concurrent` 패키지에 들어있다. 우선 이것을 import하라. 이 패키지는 또한 편리한 유틸리티 클래스인 `Executors` 를 포함해서 `ExecutorService`의 다양한 타입들을 만들게 해준다.

우선, 4개의 스레드묶음을 가지는 executor를 만들어보자:

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
```

이것은 여러개의 테스크를 동시에 처리하고 병렬컴퓨팅을 통해 프로그램의 스피드를 향상시켜준다. 스레드중하나가 죽으면 executor가 새로운 스레드를 만든다. 우리는 정확히 몇개의 스레드가 필요한지 결정하는 법을 나중에 배울것이다.

## Submitting tasks

executor는 `submit` 메소드를 가지는데 실행할 `Runnable` task를 인자로 받는다.`Runnable` 이 함수형인터페이스이기 때문에 task는 람다형식으로 표현될 수 있다.

예를 들면 여기서 우리는 standard output에  **"Hello!"**를 출력하는 테스크를 submit하고 있다.

```java
executor.submit(() -> System.out.println("Hello!"));
```

물론 우리는 `Runnable` 을 implement하는 클래스를 만들어서 객체를 만든 후 task에 넣어줄 수도 있다. 하지만 람다식을 **executors** 와 쓰면 짧은 task들을 매우 편리하게 처리할 수 있다.

`submit`의 실행 이후, 현재의 스레드는 task가 끝나는걸 기다리지 않는다. 이것은 그냥 executor의 pool에 있는 thread가 실행항 task를 executor의 내부 큐에 추가했을 뿐이다.

이 메소드는 여러 오버로드 버전을 가지고 있다. 우리는 곧 이에 대해 다룰 것이다.

## Stopping executors

executor는 task를 끝마친 이후에도 계속 실행된다 : pool에 있는 thread들이 새로운 task를 기다리고 있다.

executor가 실행되고 있으므로 프로그램 역시 종료되지 않는다.

두가지 방법으로 executor를 종료하자:

- `void shutdown()` 은 모든 task가 끝나길 기다리고 새로운 task들을 더이상 받지 않는다;
- `List<Runnable> shutdownNow()` 은 즉시 모든 task를 중단하고 실행안된 task들을 리스트로 리턴한다.

**Note**  `shutdown()` 은 현재 스레드를 block하지 않는다  (`Thread`의 `join()`과는 다르다!). 만약에 실행이 완료되기까지 기다리고 싶으면 `awaitTermination(...)`을 (시간과함께) 실행하면 된다. 

```java
ExecutorService executor = Executors.newFixedThreadPool(4); //4개스레드짜리 executor를 만든다.

// submitting tasks  실행할거 넘겨줘서 실행중..

executor.shutdown(); //여기서 갑자기 셧다운.task들은 마저 실행된다.. 이 executor의 스레드들과 달리 현재의 main스레드는 갈길을 간다..

boolean terminated = executor.awaitTermination(60, TimeUnit.MILLISECONDS); //main스레드가 갈길을 못간다.60ms동안 executor가 종료되기를 기다린다.

if (terminated) { //종료됬다면
    System.out.println("The executor was successfully stopped");
} else { //종료안됬다면 executor는 열심히 일하고 있지만 main스레드는 빠꾸없이 갈길을 간다.
    System.out.println("Timeout elapsed before termination");
}
```



## An example: names of threads and tasks

다음 예시코드에서 우리는 4개 스레드짜리 executor를 하나 만든다. 우리는 10개의 task를 submit한다. 그리고 결과를 정리한다. 각 task들은 어떤스레드가 그걸 실행했는지 이름을 프린트하고 자기의 task이름도 프린트한다.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorDemo {
    private final static int POOL_SIZE = 4;
    private final static int NUMBER_OF_TASKS = 10;
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            int taskNumber = i;
            executor.submit(() -> {
                String taskName = "task-" + taskNumber; 
                String threadName = Thread.currentThread().getName();
                System.out.printf("%s executes %s\n", threadName, taskName);
            });
        }

        executor.shutdown();
    }
}
```

이 프로그램을 실행할 때마다 결과는 다를 수 있다. 이런 출력이 나올 수 있다:

```java
pool-1-thread-1 executes task-0
pool-1-thread-2 executes task-1
pool-1-thread-4 executes task-3
pool-1-thread-3 executes task-2
pool-1-thread-3 executes task-7
pool-1-thread-3 executes task-8
pool-1-thread-3 executes task-9
pool-1-thread-1 executes task-6
pool-1-thread-4 executes task-5
pool-1-thread-2 executes task-4
```

이것은 명백히 executor가 4개의 스레드를 전부 사용한다는 것을 보여주고 있다. 스레드들이 몇개의 task를 각각 푸는지는 다를 수 있다.우리는 어떤결과를 받을지 알 수 없다.

만약에 몇개의 스레드를 pool에 넣어놔야 할지 모르겠다면 풀 사이즈로 number of available processors를 넣을 수 있다.

```java
int poolSize = Runtime.getRuntime().availableProcessors();
ExecutorService executor = Executors.newFixedThreadPool(poolSize);
```



## Types of executors

우리는 정해진 크기의 pool을 갖는 executor -가장 자주 사용된다- 를 알아보았다. 다른 타입들도 있다:

- **An executor with a single thread**

가장 간단한 executor는 단 하나의 스레드를 플에 가진다. 조그만 작업에는 충분할 지도 모른다.

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

**Important:** 하나의 스레드는 모든 테스크를 처리하기엔 시간이 부족할지도 모른다. 그러면 queue가 task로 꽉차서 어마어마한 메모리를 잡아먹을 수 있다.

- **An executor with the growing pool**

자동으로 스레드를 늘려가는 executor도 있다. 

```java
ExecutorService executor = Executors.newCachedThreadPool();
```

이것은 짧은 비동기 task들을 여러개 처리할 때 보통 좋다. 하지만 스레드의 개수가 지나치게 많아지는 문제점이 생길수도 있다. 가능하면 스레드의 개수를 정해주는게 선호된다.

- **An executor that schedules a task**

만약에 같은 테스크를 주기적으로 해야하거나 주어진 시간에 맞춰 한번 해야한다면 다음 executor를 쓰라 : 

```java
ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
```

메소드 `scheduleAtFixedRate` 는periodic `Runnable` task를 제출하는데 이 테스크는 주어진 `initDelay`이후에 처음 실행되고 주어진 `period`이후에 주기적으로 실행된다.

예시:

```java
ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
executor.scheduleAtFixedRate(() -> 
        System.out.println(LocalTime.now() + ": Hello!"), 1000, 1000, TimeUnit.MILLISECONDS);
```

출력:

```java
02:30:06.375392: Hello!
02:30:07.375356: Hello!
02:30:08.375376: Hello!
...and even more...
```

우리가 했던 방식으로 멈추면 멈춘다.

이 종류의 executor는 `schedule` 메소드도 가지고 있다. 이것은 테스크를 주어진 딜레이 이후에 한번만 한다.`scheduleWithFixedDelay` 메소드도 가지고 있다. 앞에것이 끝난 후 주어진 시간동안 기다린 후 실행한다.

## Exception handling

지금까지의 예시에서 우리는 에러는 그냥 무시하고 작성했다. 여기서 에러(즉, unchecked exception)를 처리하는 방식에 관련된 걸 하나 보자.

다음 코드가 뭘 프린트할거라고 생각합니까?

```java
ExecutorService executor = Executors.newSingleThreadExecutor(); //싱글스레드 executor이다.
executor.submit(() -> System.out.println(2 / 0)); //여기서 exception이 발생할 것이다.
//내 예상은 아무것도 프린트하지 않을 것이다. Main스레드와는 관계없는 에러니까.
```

이것은 아무것도 프린트하지않는다. 에러메시지조차 말이다! 이래서 우리가 보통 `try-catch`블럭으로 task를 감싸서 exception의 발생여부를 챙기는 것이다.

```java
ExecutorService executor = Executors.newSingleThreadExecutor(); // 싱글스레드executor이다.
executor.submit(() -> { //람다식을 넣는다
    try {
        System.out.println(2 / 0); //람다를 줄 때 try로 준다.
    } catch (Exception e) {
        e.printStackTrace(); //에러일경우 메시지를 프린트하라고 한다.
    }
});
```

이러면 우리는 에러가 발생하면 알아챌 수 있다. 실제 프로그램에서는 로그를 사용하는 것이 낫다. executor는 에러가 발생해도 계속 동작할 거란걸 명심하자. executor는 동적으로 새 스레드를 만들 수 있기 때문이다