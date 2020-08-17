# Collections

콜렉션이란 : 자료구조 모음.

자바는 자바에서 편리하게 쓰라고 Java Standard 라이브러리에서 콜렉션을 제공한다.

자바뿐 아니라 많은 언어가 사용자를 위해 콜렉션을 만들어서 제공하고 있다.

자료구조의 모음이기 때문에, 우리가 직접 구현해서 쓸 수도 있다.

자바의 경우엔 standard라이브러리가 마음에 들지 않으면 Google이 제공하는 Guava 라이브러리를 쓸 수도 있다.

코틀린도 자바의 콜렉션을 공유하지만 실제사용에서 약간의 다른점이 있다.



# 제네릭

현대 콜렉션은 보통 제네릭으로 구현되어 있다.

제네릭은 쓸 수 있는 타입을 넓게 정해둔 것이다.

예를 들면 List는 Integer도, String도, 사용자가 직접 만든 Person타입의 객체에도 모두 적용할 수 있다.

다만 원시타입 int,double등은 쓰는데 좀 문제가 있다.

따라서 int를 Wrap한 Integer 클래스를 사용하는 것이다.

자바는 그냥 쓰면 Wrap을 자연스럽게 해주고, 코틀린은 애초에 코드를 int가 아닌 Int로 한다. 그러니 크게 신경쓸 이유는 없다.

# ArrayList

```
ArrayList<String> list = new ArrayList<>(50);  //타입은 String
```

자바에서 어레이리스트의 선언. 50은 어레이리스트의 초기 사이즈다. 쓰지 않는다면 default로는 10이 들어가 있다.

```
ArrayList<String> list = new ArrayList<>(anotherList);
```

다른 리스트를 사용해서 만들 수도 있다. 이 방식뿐 아니라, 리스트의 함수중에는 다른 자료구조로 옮겨주는 함수들이 많이 있다.

arraylist가 array보다 좋은점은, 여러 메소드보다도, 배열의 크기를 늘릴 수 있단 것이다. 리스트가 꽉차면, 리스트가 1.5배나 두배가 커진다.

만약 array가 꽉차면 어떻게 해야하는가? 더 큰 array를 만들어서 거기다 원소들을 넣고, 이후의 원소들도 넣어야 한다.

arraylist도 사실 그런 방식으로 리스트가 커진다.

# 컬렉션 프레임워크

자바에서 다양한 컬렉션들 - arraylist,linkedlist,set,map등은 그저 그 구조만 구현해둔게 아니라,

인터페이스와 상속을 통해 통합이고 유기적으로 작동할 수 있게 되어있다.

이것을 컬렉션 프레임워크라고한다.

예를 들면, 위의 컬렉션들은 Iterable 인터페이스를 implement하기 때문에, 모두 Iterator를 써서 내부를 순환할 수 있고, 따라서 for each문도 쓸 수 있다.

또 정렬도 배열에선 직접 손으로 해줘야 했지만, 여기에선 한번에 할 수 있다. 서로서로 변환도 쉽게 가능하다.

콜렉션 프레임워크는 Collection 인터페이스와 Map인터페이스로 나뉜다.

아마도 Map은 순서쌍을 원소로 가진다는 점에서 나머지 자료구조들과 근본적인 차이가 있기 때문에 나뉜것인 것 같다.

Collection과 Map은 직접적인 상속관계가 없다는 것에 유의한다.

**![img](https://ucarecdn.com/7592c5a5-1adb-4f91-8a83-15e0c6dfbe4b/)**

여기에 나온 것들은 인터페이스 이기 때문에 이것들로 자료구조를 만들 수는 없다.

```
Deque deque = new Deque<Integer>() ; //이것은 틀린 코드이다. 인터페이스에는 껍데기밖에 없다.
```

이 인터페이스들을 구현한 실제 클래스들로 만들어야한다.

```
ArrayList 는 List인터페이스를 실제로 구현한 클래스이다.
List list = new ArrayList<String>(); //String Arraylist는 object List이기 때문에 이 코드는 작동한다. 다만 이경우 List는 인덱스로 접근할 수 없다. 하지만 ArrayList구조 자체는 유지되고 있으니 나중에 활용할 수 있다.
List<String> list = new ArrayList(); //String List인 list가 되었다. 앞에 String을 써주면 뒤의 ArrayList의 제네릭위치에 String인 것은 컴파일러가 추론가능하기 때문에 생략가능하다.
```

- `int size()` returns the number of elements in this collection;
- `boolean isEmpty()` returns `true` if this collection contains no elements;
- `boolean contains(Object o)` returns `true` if this collection contains the specified element;
- `boolean add(E e)` adds an element to the collection. Returns `true`, if the element was added, else returns `false`;
- `boolean remove(Object o)` removes a single instance of the specified element;
- `boolean removeAll(Collection<?> collection)` removes elements from this collection that are also contained in the specified collection;
- `void clear()` removes all elements from this collection.

Collection 인터페이스의 메소드 목록이다.

removeAll(Collection<?> collection)이 눈에 띈다.

equals 함수에 대해서도 유의해야 한다. 이 함수는 콜렉션의 타입이 서로 같고, 원소들이 모두 같다면 true를 리턴한다. 이 때 "원소들이 모두 같다"는 그 원소타입의 equals를 따르는 걸 명심해야 한다. 객체의 equals가 hashcode equal을 원하는지 data equal을 원하는지 유의하자.

# List

List에는 ArrayLIst, LinkedList,Immutable list 가있다.

이것들은 List인터페이스로 다루는 것이 좋다고 한다.

```
List<String> list = new ArrayList(); // 이 것이 더 낫다는 주장이다.
// 내부구조에 관계없이 프로그램을 작동하게 해준다는데 사실 잘 모르겠다.
ArrayList<String> list = new ArrayList(); 
```

## Immutable lists

`List`의 `of`를 사용해서 immutable list(내부를 수정할 수 없음)을 만들 수 있다.

```java
List<String> emptyList = List.of(); // 0 elements
List<String> names = List.of("Larry", "Kenny", "Sabrina"); // 3 elements
List<Integer> numbers = List.of(0, 1, 1, 2, 3, 5, 8, 13);  // 8 elements
```

이것은 뭐랄까, 매우매우 이상하다.

ArrayList는 클래스이다. 그리고 new 로 만들어서 List라고 불렀다. 이것엔 아무문제가 없다.

ImmutableLIst의 생성방식은, 인터페이스의 of함수를 사용해서 immutablelist를 만드는데, 이것도 이상하고, 애초에 immutablelist 클래스조차 없다.

그리고 List의 of함수는 Java의 doc에 없었다. 매우 황당했는데, 그 이유는 저 함수가 Java9에 추가됬기 때문이다.[최신버전의 Doc을 보자..](https://docs.oracle.com/javase/9/docs/api/overview-summary.html)사실 자바는 14?까지 나왔기 때문에 저것도 최신버전이 아니다;;

```
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5); // Java9이전까진 이런방식을 썼다.
```

Array를 List에 박고있는 모습이다. 배열과 관련이 있나?

# More about Immutable

## Immutable List Static Factory Methods

[`List.of()`](https://docs.oracle.com/javase/9/docs/api/java/util/List.html#of-E...-)는Immutable list를 만들게 해주는 static factory method이다.

> ###### static factory mehod 란?
>
> 1.생성하는 메소드다
>
> Integer.valueof(10)을 보면, new Integer이 포함되어 있다는 걸 추측할 수 있다.
>
> 2.static 이다.
>
> Instance가 아니라 Class에서 메소드를 호출하는걸 알 수 있다.



이 메소드로 만들어진 `List`인스턴스는 다음과 같은 특징을 가진다:

- *구조적으로 수정불가함*. 원소추가불가 교체불가 제거불가. 수정하는 함수 부르면 `UnsupportedOperationException` 일어날 것임. 근데 내용물 자체가 바뀌면야 바뀔 수는 있겠지.
- `null` 원소 허용하지않음. 시도하면 `NullPointerException`.
- 원소가 serializable이면 List도 그렇다.
- 넣은 순서가 유지된다.
- [value-based](https://docs.oracle.com/javase/9/docs/api/java/lang/doc-files/ValueBased.html)하다. 이 리스트가 아까 그 리스트인지 아무도 알 수 없다. factory들은 새걸 만들거나 있던걸 다시 쓰거나 할 수 있다. 그러니 정체성에 관련된 연산 (reference equality (`==`), identity hash code, and synchronization)은 신뢰할 수 없고 안쓰는쪽으로 해라.
- [Serialized Form](https://docs.oracle.com/javase/9/docs/api/serialized-form.html#java.util.CollSer) page에 쓰인대로 serialization된다.

[Java Collections Framework](https://docs.oracle.com/javase/9/docs/api/java/util/package-summary.html#CollectionsFramework)의 일부이다.

별 도움이 안된다.

소스코드를 보러 가봤다.

```
static <E> List<E> of(E e1) {//최신Java에서 인터페이스도 구체적인 함수를 가질 수 있게 추가가 되었다.default는 override가 가능하고,static은 불가능하다.
        return new ImmutableCollections.List12<>(e1); //ImmutableCollection.List12을 리턴한다.
    }
/**
     * Returns an unmodifiable list containing one element.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the single element
     * @return a {@code List} containing the specified element
     * @throws NullPointerException if the element is {@code null}
     *
     * @since 9
     */
```

ImmutableCollection을 리턴한다. 그럼 이게 Collection의 일부이고, List를 implement하면 아무문제가 없을텐데..

이건 콜렉션과 별개의 구조이다.

```
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5); // Java9이전까진 이런방식을 썼다.
```

이 코드는 매우 놀라운 코드인데, 보면 asList를 해서 List를 만들었는데, Immutable이 되버린 코드이다.

계속 의문점을 조사해본결과, 재밌는 사실을 알아냈는데, asList는 뭔가 이상한걸 만든단 것이다.

이건 Arrays$ArrayList 를 리턴한다.그리고 저 ArrayList는 우리가 지금까지 콜렉션에서 다룬 ArrayList와 이름만 같고 다른 ArrayList이다.

즉, '뭔가 이상한 것'을 넣으면 List인터페이스쪽인지 저 이상한 ArrayList쪽인지에서 뭔가의 기묘한 로직으로 final설정등등을 해서 Immutable한 List구조를 만들어 List로 쓴단 얘기다. 그걸 구현한게 자바9이전에는  Arrays$ArrayList를 박아서 구현하다가 자바9부터는 ImmutableCollection도 도입을 한 것이다.

실제로 Guava라이브러리는 좀더 체계적인 ImmutableCollection이 있으며, Kotlin은[![collections diagram](https://codechacha.com/static/a8a4ee897d3ad2694f914c10d6aad695/b9e4f/collections-diagram.png)](https://codechacha.com/static/a8a4ee897d3ad2694f914c10d6aad695/0c99c/collections-diagram.png)(출처: kotlinlang.com)

라는, 훨씬 납득이 가는 구조를 갖고 있다.

이 주제는 여기까지 하겠다. 다만 코틀린의 구조에서도 볼 수 있듯이, Mutable과 Immutable은 생각이상으로 차이가 있다는 것을 유추할 수 있다.

[추가 : 여전히 잘 납득이 안가서 질문을 올렸고, 답변의 geeksforgeeks링크에서 많은 의문점이 풀렸다.](https://stackoverflow.com/questions/63434294/what-is-the-relationship-between-collections-and-immutablecollections)

