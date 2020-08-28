## Theory: Garbage Collector

우리의 프로그램이 새로운 객체를 만들 때마다 우리는 물리적인 메모리(RAM)을 많이 필요로 한다.거기다 객체를 저장해야 되니까. 우리가 `new`를 쓸 때 마다 메모리가 JVM의 heap에 할당된다. 하지만, 우리의 프로그램이 이 객체를 다 사용한 후에, 메모리를 다른 일에 쓸 수 있다.

**Garbage Collector** (**GC**) 는 JVM의 부분이다. 이것인 런타임에 메모리를 자유롭게 해줘서 다른 일에 다시 재활용할 수 있게 해준다. 이 자동화된 메모리 관리 시스템은 자바프로그래머들을 에러와 메모리누수로부터 보호해준다. 예를 들면 C나 C++에서는 메모리 할당과 해방을 손으로 직접 해줘야 하기 때문에,  몇몇 메모리는 해방되지 못하고(프로그래머가 눈치못챔 ㅠ) 남겨지기도 한다.

## Ways for requesting JVM to run GC

가비지 콜렉션은 프로그램이 실행되는동안 자동으로 실행된다. JVM이 모든 더러운일을 해주는 것이다. 대부분의 경우에 자바 개발자는 GC에 대해 생각하지 않아도 된다. 하지만 최근의 고성능 프로그램에서는 이 지식도 필요하다.

2가지 방법으로 GC에게 일을하라고 요구할 수 있다:

- calling `System.gc()`
- calling `Runtime.getRuntime().gc()`

개발자보고 하라고 GC를 만들어 논게 아니라서, 사실 이 메소드들이 ㄹㅇ루다가 GC를 실행시킬지는 알 수가 없다. 그러니 이 메소드들은 테스트할 때만 써보자.

더 알아보기 위해 첫번째 메소드를 실행해 보겠다.

## Garbage collection example: a horde of hamsters

여러개의 hamster 객체를 만들어서 컴퓨터의 메모리를 낭비해보자.

그리고 GC한테 메모리를 정리해달라고 해보자.

```java
class Hamster {
    private int id;
    
    public Hamster(int id) {
        this.id = id;
    }
}

public class GCExample {

    private static void printUsedMemory() {
        Runtime r = Runtime.getRuntime(); // it allows to get memory information for JVM 
        long usedMemory = r.totalMemory() - r.freeMemory();
        System.out.println("Used memory (bytes): " + usedMemory);
    }
    
    public static void main(String[] args) {
        printUsedMemory();
        
        for (int i = 0; i < 1_000_000; i++) {
            new Hamster(i); //햄스터를 많이 만들엇다
        }
        
        printUsedMemory();
        
        System.gc(); // Requesting JVM for running GC
        
        printUsedMemory();
    }
}
```

출력:

```java
Used memory (bytes): 1197456
Used memory (bytes): 4489984
Used memory (bytes): 712360
```

숫자는 물론 다를 것이다. 코드를 더 파헤쳐보자.

우리의 `main`메소드에서 우리는 우선 프로그램이 시작할 때 얼마나 메모리를 쓰는지를 확인했다.

이경우에는 **1197456** 이다.

그리고 우리는 햄스터를 떼로 만들어서 메모리를 열심히 낭비했다. 우리가 `new`로  객체를 만들때마다 우리는 메모리를 소모하는 것이다. 따라서 우리가 쓰는 메모리는 **4489984** 까지 올랐다. 하지만 , 우리가 main에서 `System.gc()`를 쓰면 , 우리는 메모리 사용량이 **712360**까지 내려갔다는걸 알게된다. 자바 가비지 콜렉터가 헴스터를 더이상 쓰지 않는다는 것을 깨닫고, 관련된 메모리를 해방시켜주는 것이다.

만약 `System.gc()`를 하지않는다면, 결과는:

```java
Used memory (bytes): 1197448
Used memory (bytes): 4783616
Used memory (bytes): 4783616
```

메모리가 해방되지 않았다는걸 알 수있다. 하지만 이것에 대해 걱정할 필요는 없다. 자바 런타임이 자동으로 `System.gc()`를 알아서 실행해서 메모리를 해방시켜줄 것이다.

## Conclusion

우리는 가비지컬렉터가 메모리를 해방한다는 것을 배웠다. 우리는 이것을 직접 invoke할 수 있지만 그 후 GC가 알아서 할 것이다. 이것은 자동으로 안쓰이는 메모리를 해방시켜준다. GC를 위의 두 메소드로 직접 invoke하는것은 권장되지 않는다. 두 메소드 모두 GC가 실제로 일을 할지는 보장하지 않기 때문이다.