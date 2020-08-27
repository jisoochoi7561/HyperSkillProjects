## Theory: Callable and Future

Callable과 Future

## The Callable interface

가끔 executor에서 task를 실행할 뿐만 아니라 이 task의 결과 또한 리턴해야 할 때가 있다. 이것은 `Runnable`로 가능하긴 하지만 하기 불편하다.

이것을 간단하게 하기 위해 executor는 다른 클래스의 task를 지원한다.그것이`Callable` 이다. 이것은 결과를 리턴하고 exception을 throw할 수 있다. 이 인터페이스는 ` java.util.concurrent` package에 포함되어있다. 자세히 살펴보자.

```java
@FunctionalInterface   //함수형 인터페이스이다.
public interface Callable<V> {
    V call() throws Exception;
}
```

보시듯이 이것은 제네릭 인터페이스다. 타입 파라미터 `V`가 결과의 타입을 결정한다. 이것이 **함수형인터페이스**이기 때문에 우리는 람다식과 함께 쓸 수있고 물론 메소드 리퍼런스와 클래스에implement도 가능하다.

이 `Callable` 이 긴시간이 걸리는 task라고 치고, "계산된"결과를 리턴한다고 치자.

```java
Callable<Integer> generator = () -> {
    TimeUnit.SECONDS.sleep(5);
    return 700000;
};
```

같은 코드는 implement한 클래스로도 만들 수 있다. 람다가 훨씬 낫지만 말이다.

## Submitting a Callable and obtaining a Future

우리가 `Callable` 을 executor에 submit하면, `submit` 메소드가 task의 완료를 기다리지 않기 때문에 결과를 직접 리턴할수는 없다.

대신에, executor는`Future` 라는 특별한 객체를 리턴합니다. Future은-아직 존재하지도 않더라도- 결과를 싸고 있습니다. 이 객체가 task의 결과를 표현합니다.

```java
ExecutorService executor = Executors.newSingleThreadExecutor(); //executor

Future<Integer> future = executor.submit(() -> { //future는 결과입니다..언제 확정될지는 모르겠지만요.
    TimeUnit.SECONDS.sleep(5);
    return 700000;
};
```

task가 끝나기 전까지 실제 결과는 `future`에 없습니다. 확인하려면 , `isDone()`메소드를 쓰면 됩니다. future로 저렇게 wrap을 하고 곧바로 isDone()을 쓰면 보통 `false` 가 나올 겁니다.

```java
System.out.println(future.isDone()); // most likely it is false
```

## Getting the actual result of a task

결과는 *future*에서 `get`메소드를 써서 얻어야 합니다.

```java
int result = future.get();
```

이것은 계산이 완료된 이후의 결과를 리턴한다. 완료가 안됬다면 현재스레드를 막고 완료가 될때까지 기다린다. 이것은 두개의 checked exception을 throw할 지도 모른다 : `ExecutionException` 와 `InterruptedException`여기서는 생략하겠다.

만약 submit한 task의 실행이 무한루프거나 외부리소스를 너무오래 기다리거나 하면 , `get` 이 실행되고 있는 스레드는 하루종일 막혀있을 것이다. 이것을 막으려면 , `get` 에 대기시간을 추가해 오버로드한 버전을 쓰면 된다.

```java
int result = future.get(10, TimeUnit.SECONDS); // it blocks the current thread
```

이 경우 스레드는 10초를 기다려준다. 만약 10초후에도 완료되지 않으면 메소드는 `TimeoutException`을 던진다.

## Cancelling a task

`Future` 클래스는 인스턴스 메소드`cancel` 을 제공한다. 이 메소드는 task의 실행을 cancel한다. 이 메소드는 보기보다는 복잡한 메소드이다.

cancel하려는 시도는 실패할 수도 있다. task가 이미 완료되있거나 이미 캔슬되있거나 아니면 다른이유로 캔슬에 실패할 수 있다. 만약 task실행전에 cancel한다면 실행자체가 아예 안될 것이다.

이 메소드는 `boolean` 파라미터를 받는데 이것이 지금 task를 하고 있는 스레드를 interrupt해서 cancel을 시도할지 말지를 결정한다.즉, 이미 실행되고 있는 task를 멈출지 말지를 결정한다.

```java
future1.cancel(true);  // try to cancel even if the task is executing now
future2.cancel(false); // try to cancel only if the task is not executing
```

`true` 인자를 넘기는것은 interruption을 함축하기 때문에 cancel은 `InterruptedException` 을 적절히 제어하고 `Thread.currentThread().isInterrupted()`플러그로 실행을 제어할 때만 가능하다.

만약에 누군가가 `future.get()`을 cancel성공한 task에다가 요구한다면  메소드는  unchecked `CancellationException`을 throw할 것이다. 이것과 씨름하기 싫다면 , `isCancelled()`를 써서 캔슬됫는지를 확인하라.

## The advantage of using Callable and Future

우리가 배우고 있는 이 방식은 `Future` 를 얻고 실제 값을 얻는 사이에서 뭔가를 할 수있게 해준다. 이 사이의 시간에, 우리는 여러개의 task를 executor에 submit하고 result들이 나오기를 기다리면 된다.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);  //executor 만듬

Future<Integer> future1 = executor.submit(() -> { //submit하고 result를 future로 wrap함.
    TimeUnit.SECONDS.sleep(5);
    return 700000;
});

Future<Integer> future2 = executor.submit(() -> { //하나 더함.
    TimeUnit.SECONDS.sleep(5);
    return 900000;
});

int result = future1.get() + future2.get(); // waiting for both results get메소드는 기다림.

System.out.println(result); // 1600000
```

현대 컴퓨터에서 이 과정은 병렬적으로 수행된다.

## Methods invokeAll and invokeAny

위의 특징들 말고도, 또 두개의 유용한 메소드가 있다. 그것들은 Callable을 한꺼번에 executor에 submit하게 해준다.

- `invokeAll`  callable의 컬렉션을 받아서 future컬렉션을 리턴한다
- `invokeAny` 는 callable의 컬렉션을 받아서 성공한 결과(future가 아니다!)중 하나를 리턴한다

두 메소드 모두 오버로드된 버전이 있어서 시간제한을 걸 수 있다.현실에서 유용하다.

여러개의 숫자를 분리된 task들로 계산하고 그것들을 `main`스레드에서 합친다고 하자. 이것은 `invokeAll` 을 쓰면 무척 간단하다.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);  //executor를 만듬
List<Callable<Integer>> callables =
        List.of(() -> 1000, () -> 2000, () -> 1500); // three "difficult" tasks  callable 컬렉션이다.

List<Future<Integer>> futures = executor.invokeAll(callables); future컬렉션이다.
int sum = 0;
for (Future<Integer> future : futures) {
   sum += future.get(); // blocks on each future to get a result  //main스레드에서 계산중..
}
System.out.println(sum);
```

자바의 버전이 8이하라면 `List.of`를 쓸 수없으니 `Arrays.asList`를 쓰라.Stream을 적용할 수 있다면 그것도 쓸 수 있다.



## Summary

 `Callable` 과 `Future` 정리.

비동기적인 task들이 `ExecutorService` 에의해 실행될 때의 result를 받으려면  3가지 단계를 거쳐야 한다:

1. `Callable` 로 task객체 만들기
2. `ExecutorService` 에 task를 submit해서 `Future` 얻기
3.  `get` 을 써서 실제 결과 값을 필요할 때 받기.

 `Future` 쓰면 우리는 현재 스레드를 멈추지 않을 수 있다. 우리가 결과가 필요할 때 까지. 또 여러개의 task들을 실행해서 현재스레드에 결과들을 모을 수 있다. 더하자면 프로그램을 더 반응적으로 만들고 병렬컴퓨팅을 이용한다면 더 빠르게 만들어준다.

여러 메소드 `isDone,cancel,isCancelled  `도 유용할 것이다. 하지만 예외처리에 늘 유의하라. 불행하게도 우리가 모든 케이스를 여기서 설명해줄 순 없다. 하지만 경험을 쌓아간다면 극복할 수 있을것이다.

가장 중요한 것은 - 특히나 멀티쓰레드에서- doc을 읽어라

