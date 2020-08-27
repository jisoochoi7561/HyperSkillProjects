## Theory: Concurrent queues

 concurrent collection중 가장 사랑받는 것은 **concurrent queue**이다. 이것은 데이터들(메세지,task등등)을 교환함으로서 여러 스레드사이의 커뮤니케이션을 저장하는데 쓰인다.이렇게 하려면 스레드들은 같은 큐를 공유하고 그 메소드들을 써야한다.

이미 큐가 콜렉션이고  **first-in-first-out principle** (FIFO)이란건 알고 있을 것이다.

## Thread-safety of ConcurrentLinkedQueue

가장간단한 concurrent queue는 `ConcurrentLinkedQueue` 이다. 이것은 스탠다드 큐와 매우 비슷하지만 **thread-safe**이다. 이 것은 `add` 와 `offer`메소드로 큐의 끝부분에 원소를 삽입한다.

다음 코드가 이 큐의 thread-safety를 보여주고 있다. 이 프로그램은 두개의 스레드가 각각 새 원소들을 추가해서 최종적으로 큐에 몇개의 원소가 있는지를 출력한다:

```java
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        // assigning thread-safe implementation
        Queue<Integer> numbers = new ConcurrentLinkedQueue<>();

        Thread writer = new Thread(() -> addNumbers(numbers));
        writer.start();

        addNumbers(numbers); // add number from the main thread

        writer.join(); // wait for writer thread

        System.out.println(numbers.size()); // it prints 200000
    }

    private static void addNumbers(Queue<Integer> target) {
        for (int i = 0; i < 100_000; i++) {
            target.add(i);
        }
    }
}
```

놀랍지도 않다. 이 프로그램은 언제나 2십만을 제대로 출력한다. 원소의 누락이 없다. 즉, `ConcurrentLinkedQueue` 은 ㄹㅇ루다가  thread-safe이다. 이것을 iterate할 때 `ConcurrentModificationException` 또한 발생하지 않는다.

주의, 이 큐의 각각의 연산이 thread-safe이다. 하지만 , 우리가 만약 그 연산들을 하나의 메소드나 문장묶음으로 묶는다면 , 그 그룹도 thread-safe인지는 보장할 수 없다.

또한 `ConcurrentLinkedQueue` 의 벌크연산들, 즉 여러 원소를 다루는`addAll`, `removeIf`, `forEach`등은 atomic하다고 보장할 수 없다.



## Communication between threads

다음그림은 큐를 사용해서 스레드들이 어떻게 커뮤니케이션을 형성하는지를 보여준다. 스레드 하나가 큐에다 원소를 삽입하고, 다른 스레드는 같은 큐에서 원소를 빼낸다.

![img](https://ucarecdn.com/afdf8010-3627-4423-99ed-6c321acee910/)

 `Queue`가 thread-safe라고 하자.안그러면 이게 성립이 안된다.

스레드가 두개이상이어도 문제 없다.

![img](https://ucarecdn.com/f7c072f0-2263-4b21-80c0-701d83d0052b/)

스레드의 숫자는 중요하지 않다.

우리가 두개의 스레드끼리 정보를 교환하고 싶어서 concurrent queue를 쓴다고 하자. 첫 스레드는 3개의 숫자를 만들거고 다른 스레드는 이 숫자들을 받아서 프린트 할 것이다. `poll` 메소드로 queue에서 첫원소를 얻을 수 있다(빈 큐라면 null).

여기 코드는 추가로 `sleep` 을 사용해서 더 정확한 출력을 만들고 있다.  `generator` 와 `poller` 는 concurrent queue를 사용해 소통하고 있따. 그리고 큐가 완벽히 thread-safe하기 때문에 정보가 유실되지 않는다.

```java
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class GeneratorDemo {

    public static void main(String[] args) {
        Queue<Integer> queue = new ConcurrentLinkedQueue<>(); //스레드세이프한 큐

        Thread generator = new Thread(() -> { // 제너레이터
            try {
                queue.add(10);
                TimeUnit.MILLISECONDS.sleep(10);
                queue.add(20);
                TimeUnit.MILLISECONDS.sleep(10);
                queue.add(30); //3개를 넣는다
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread poller = new Thread(() -> {
            int countRead = 0;
            while (countRead != 3) {
                Integer next = queue.poll();
                if (next != null) {
                    countRead++;
                }
                System.out.println(next);
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        generator.start();
        poller.start();
    }
}
```

Here is an example of an output:

```java
null
10
20
null
30
```

좀 다를 수 있지만 ,어쨌든 모든 숫자가 출력된다.

## Non-atomic composite operations

concurrent queue의 모든 스탠다드 메소드는 thread-safety이다. 하지만 , 여러 메소드를 결합한다면 그런 보장은 없다.

당신이 두개의 원소를 넣고싶다고 하자:

```java
public static void addTwoElements(ConcurrentLinkedQueue<Integer> queue, int e1, int e2) {
    queue.add(e1); // (1)
    queue.add(e2); // (2)
}
```

메소드는 두개의 원소를 하나씩 넣을 것이다.두개는 서로 옆에 있을것이다 - 스레드 단 하나일때만-. 스레드 여러개가 작성한다면, 스레드 하나가 (1)을 하고, 다른 스레드가 (1)을 하고, 이럴 수 있다. 그 다음에 첫 스레드가 (2)를 하는것이다. 이러면 순서가 깨진다. 이 문제는 이 메소드가 **atomic**하지 않아서 발생한다.

이 문제를 피하려면 이렇게 결합해서 만들어진 메소드들은 따로 synchronization 을 해주거나 스탠다드 메소드를 사용하는 걸 택해야한다.`addAll` 메소드를 사용해서 이걸 해결해보자:

```java
queue.addAll(List.of(e1, e2));
```

이 메소드는 atomic하게 리스트의 원소들을 큐에 넣기 때문에 순서가 원하는대로 된다.