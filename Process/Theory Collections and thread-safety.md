## Theory: Collections and thread-safety



## Classic collections and multithreading

이미 알고 있듯이, 여러개의 스레드가 같은 데이터에 동시에 접근하면 여러문제가 발생한다. 우리는 그걸 막으려고 synchronization을 한다.

여러 스레드가 같은 콜렉션에 접근할 때에도 같은 문제가 발생한다:

- 대부분의 고전적 콜렉션들: `ArrayList`, `LinkedList`, `HashMap` 등등은  non-synchronized 이고,따라서, thread-safety를 제공하지않는다;
- 낡은 콜렉션들 `Vector`, `Stack`,  `Hashtable` 등은 완벽히 synchonized되고 thread-safe하지만, 성능이 안좋다.
- 한 스레드가 표준 컬렉션을 순회하고 있고 다른 스레드가 거기에 새로운 원소를 추가하려 한다면 우리는 런타임 예외`ConcurrentModificationException`를 받는다.

다음 프로그램이 두 스레드가 같은 콜렉션에 원소를 추가할 때 일어나는 일을 보여준다:

```java
import java.util.ArrayList;
import java.util.List;

public class NeedOfConcurrentCollectionsDemo {

    public static void main(String[] args) throws InterruptedException { 
        ArrayList<Integer> numbers = new ArrayList<>(); // 공유되는 컬렉션

        Thread writer = new Thread(() -> addNumbers(numbers)); // 함수하나를 스레드에 박아준다.
        writer.start(); // 함수하나 다른 스레드에서 시작.

        addNumbers(numbers); // 메인스레드에서 함수 시작

        writer.join(); // 다른 스레드를 기다린다.addNumbers가 다른스레드에서 완료되겠네.

        System.out.println(numbers.size()); //컬렉션의 사이즈는? 알 수 없다.20만아냐?
    }

    private static void addNumbers(ArrayList<Integer> target) {
        for (int i = 0; i < 100_000; i++) { //리스트에 0에서 10만까지를 넣는 함수다 ㄷㄷ
            target.add(i);
        }
    }
}
```

예상결과는 2십만일 것이다(십만 + 십만).현실은, 이 코드를 실행할 때마다 달라진다. 원소들이 유실된다는 것이다.

세번 해보니 다음결과를 얻었다:

```java
196797
154802
181359
```

직접해보시길.

멀티스레드 환경에서 스탠다드 컬렉션을 쓴다는 것은 매우 안좋은 생각이다. synchonization이 명확하지 않다면.  다시말하지만, 이 synchonization을 하면 퍼포먼스가 낮아질 수 있다. 그니까 쓰지를 말라고

## Concurrent collections

이 문제들을 막으려 자바 클래스 라이브러리는 멀티스레드 환경에 적합한 대안 콜렉션을 제공한다. 이것을 `java.util.concurrent` package에서 찾을 수 있다. 리스트,큐,맵 등등이 있다.

이  concurrent collection들은 우리에게 직접 synchronization을 하지않게 해주고 거의 기본 콜렉션에 비슷한 좋은 퍼포먼스를 제공한다.Concurrent collection은 `synchronized` 키워드를 쓰지않고, 더 복잡한 원시 동기화와 lock-free알고리즘을 쓴다. 따라서 thread-safe와 high performance를 동시에 얻을 수 있따. 하지만 , 만약 정말로 멀티스레딩이 필요한게 아니라면 , 그냥 고전적 콜렉션을 쓰라. 그것들은 여전히 더 좋다.

여러분은 여러 concurrent collection을 나중에 배울 것이다.

