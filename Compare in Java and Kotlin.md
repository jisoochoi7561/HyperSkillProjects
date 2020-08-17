# Compare in Java and Kotlin

코틀린과 자바에서의 Compare을 정리한다.

자바에서 comparator와 comparable을 override하는 compare의 사용법을 기준으로

코틀린에서의 사용법을 정리한다.

> 코틀린에서는 >,<등의 연산기호 또한 compareTo로 구현된다.

보통 compare은 Collection에서 Sort를 할 때 사용한다.

물론 다른방식으로도 얼마든지 쓸 수 있다.

## comparable

comparable은 인터페이스다. -계산가능하단 뜻-

왠만한 객체들은 다 comparable을 implement한다고 생각하면 된다.

인터페이스 구조는 간단하다.

```
public interface Comparable<T> {   
	public int compareTo(T o);
}

```

사용법은 x.compareTo(y)이다.

이 때 리턴값이 살짝 헷갈린다.

+(양수)면 :  (x가)더 크다

0이면 : 같다

음수면 : 작다.

이다.

## Integer

```
public final class Integer extends Number
        implements Comparable<Integer>, Constable, ConstantDesc
```

자바의 Integer이 Comparable을  implement하고 있는 모습이다.

```
public int compareTo(Integer anotherInteger) {
    return compare(this.value, anotherInteger.value);
}
```

compare메소드를 새로 만들어서 compareTo를 구현한 모습이다.

```
public static int compare(int x, int y) {
    return (x < y) ? -1 : ((x == y) ? 0 : 1);
}
```

compare 메소드의 내용은 다음과 같다 : x가작으면 -1을, 같으면 0을, 크면 1을 리턴한다.

# comparable로 sort하기

```
Collections.sort(list)
list.sort()
```

두 방식으로 comparable한 원소들을 갖고 있는 collection을 sort할 수 있다.

구현은 작은쪽이 앞에 오게 적용된다.

이걸 바꾸려면, compareTo를 클 때 음수가오게(헷갈린다) 바꾸거나

reverse인자를 넘겨주면 된다.reverse인자를 어떻게 넘겨주느냐?

kotlin에서는 매우 직관적이다.

```
Collections.sort(list, reverseOrder())
list.sortDescending()
```

하지만 사실 원리는 자바와 같다.

reverseorder는 comparator를 넘겨주고 있는 것이다.

sortdescending은 코틀린 나름대로 거꾸로 하고 있는 것이다.

# comparator

comparator 역시 인터페이스다.

이 인터페이스는 compare(x,y)메소드를 가지고 있다.

앞에 Integer이 x.compareTo(y)를 구현할 때 compare(x,y)를 거친 기억이 난다.

이 인터페이스로 만든 것을 sort에 인자로 넘겨주면 sort는 comparator의 compare을 사용한다.

이것이 뜻하는 것은 , comparable하지 않은 타입도 comparator를 넘겨주면 sort할 수 있다는 거다.

comparable한 것도 comparator로 sort될 것이다.

```
java
Collections.sort(arraylist, Collections.reverseOrder());
```

Collections.reverseOrder는 Comparator를 만든다.

특정 방식말고 정말로 원하는 대로 comparator를 만들어보자

```
class myComparator implements Comparator<Integer> 
{ 
    public int compare(Integer a, Integer b) 
    { 
        return a%2 - b%2; 
    } 
} 
```

홀수와 짝수가 마주치면 홀수가 뒤로 가는 comparator이다.

```
java
Collections.sort(arraylist, new myComparator());
```

## comparator를 만드는 법

아주 정석적인 comparator를 만드는법은 방금 했다.

그래도 lambda,kotlin등 요즘 세상에 살다보면 좋은 방법이 더 생긴다.

```
println(listOf("aaa", "bb", "c").sortedWith(compareBy { it.length }))
```

compareBy는 람다로 comparator를 만든다.

람다식의 결과는 comparable이어야 한다.

람다식의 결과로 비교한 것과 같은 결과를 만드는 comparator를 만들어준다.

그 외에도 익명클래스등을 이용해 간단히 만들 수 있겠다.

