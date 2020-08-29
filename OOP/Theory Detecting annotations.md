## Theory: Detecting annotations

우리는 이미 어노테이션을 배웠다(난 그냥 컴파일러를 위한 주석?정도로 배웠다). 몇개를 기억하시는가? 스탠다드 라이브러리에서 가장 자주 쓰이는 어노테이션은  `@Deprecated` and `@Override`이다.하지만 다른 라이브러리들에는 또 많은 어노테이션이 있다. 예를 들면 스프링라이브러리는 어노테이션을 잔뜩 갖고 있고, 자바클래스를 테스트 하는데 아주 유용한 `@Test`어노테이션이 있다. [this page's code examples](https://dzone.com/articles/key-java-annotations-to-build-full-spring-boot-res) 여길 보고 실전에서 이걸 얼마나 많이 쓰는지 확인해보라. 이것이 어떻게 동작하고 어떤일이 발생하는지 공부해보자.



## `getDeclaredAnnotations` method

실제로는, 어노테이션은 그냥 표시이다 : 이것들은 클래스,메소드,필드등이 특별한 속성이 있다는 것을 말해준다. 이 어노테이션들은 리플렉션과 결합해서 컴파일이나 런타임에 더 자세히 분석된다.

다음 클래스를 보라:

```java
class Item {
    @Deprecated
    public static final int maxItems = 100;
    public static int inStock = 19;

    private String name;
    protected int basePrice;

    public Item(String name, int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return (int) (basePrice * getMarkUp());
    }

    public boolean haveSuchAmount(@Deprecated int amount) {
        return inStock >= amount;
    }

    @Deprecated
    protected double getMarkUp() {
        double markUp = 0.1;
        // ... connecting to the remote server
        return 1 + markUp;
    }

    @Override
    public String toString() {
        return getName() + " " + getPrice();
    }
}
```

3개의`@Deprecated` 어노테이션과 하나의 `@Override` 어노테이션이 있다. 리플렉션을 써서 이것을 분석하자.

어노테이션을 다루기 위해  `Class`, `Method`, `Field` and `Constructor` class들은(전부 리플렉션 패키지의 클래스들이다) `getDeclaredAnnotations()`메소드를 가지고 있다. 이것은  `Annotation`객체배열을 리턴한다. 이 메소드는 어레이를 리턴하지, 객체를 리턴하지 않는데 왜냐면 하나의 필드나 메소드가 여러개의 어노테이션을 갖고 있을 수 있기 때문이다. 위의 링크를 아까 봤다면 맨처음에 클래스에 6개의 어노테이션이 달린것을 확인할 수 있다!

## Code example

다음 코드를 실행해보자. 여기서 우리는 메소드와 필드들의 이름을 지나가며 `getDeclaredAnnotations()`를 써서 그것들의 어노테이션을 알아내 이름옆에 출력할 것이다.

```java
Class itemClass = Item.class; //인스턴스를 하나 만듬

for (Field field : itemClass.getDeclaredFields()) { //필드
    for (Annotation annotation : field.getDeclaredAnnotations()) {
        System.out.print(annotation + " - "); //어노테이션
    }
    System.out.println(field.getName()); //이름
}

for (Method method : itemClass.getDeclaredMethods()) {
    for (Annotation annotation : method.getDeclaredAnnotations()) {
        System.out.print(annotation + " - ");
    }
    System.out.println(method.getName());
}
```

Take a look at the output:

```no-highlight
@java.lang.Deprecated(forRemoval=false, since="") - maxItems //이친구는 어노테이션이 있다
inStock
name
basePrice
toString
getName
@java.lang.Deprecated(forRemoval=false, since="") - getMarkUp //이 친구도 있다
getPrice
haveSuchAmount
```

여기서 알 수 있는건 우리는 3개의 어노테이션이 있었는데 두개만 출력됫따는 것이다.@Deprecated는 우리 예상처럼 `maxItems` 와  `getMarkUp`에 잘 붙어 나왔다.그런데 어떠한 이유로 `@Override toString` 이 출력되지 않았다. 우리 스스로 이유를 찾아보자. 이걸 통해 어노테이션과 리플렉션을 더 이해할 수 있을 것이다. 우선 우리는 @Override 가 없는 이유가 뭔가 메소드와 관련 있다고 생각할 수 있다: 사실 `toString` 은 이 클래스에서 선언된게아니라 `Object`에 있는걸 override한 것이니까. 이 가설을 확인하기 위해 declare가 아닌것도 뽑아내는 `getMethods` 메소드를 써보자

```java
for (Method method : itemClass.getMethods()) { //getMethod로 함수가 바뀌었다.
    for (Annotation annotation : method.getDeclaredAnnotations()) {
        System.out.print(annotation + " - ");
    }
    System.out.println(method.getName());
}
```

우리의 예상이 맞다면 이번에는  `@Override` 가 출력될 것이다.

출력:

```no-highlight
toString
getName
getPrice
haveSuchAmount
wait
wait
wait
equals
@jdk.internal.HotSpotIntrinsicCandidate() - hashCode
@jdk.internal.HotSpotIntrinsicCandidate() - getClass
@jdk.internal.HotSpotIntrinsicCandidate() - notify
@jdk.internal.HotSpotIntrinsicCandidate() - notifyAll
```

여전히 뭔가가 잘못됬다! 보시듯이, 이번에는 처음보는 어노테이션을가진 4개의 메소드가 등장하고,`toString` 은 여전히 어노테이션이 없다. 우선 `getMarkUp` 메소드는 protected이기 때문에 출력안됬단걸 짚고 넘어가자.저 4개의 메소드는 그냥 넘어가자. 우리는 지금은 이 이상 깊게 갈 생각은 없다.

여기서 이유를 알려주자면 이것은 어노테이션 자체의 성질과 관련이 있다. 확실히,`@Override`어노테이션은 컴파일타임의 어노테이션이고 프로그램이 실행될 때에는 존재하지 않는다.`@Deprecated` 은 프로그램 실행도중에도 남아있고, 그것은 이게 런타임 어노테이션이란걸 뜻한다.

## Conclusion

리플렉션 패키지는 소스코드에서 어노테이션을 뽑아내는 기능을 제공해준다.`Class`, `Field` and `Method` 클래스들은 `getDeclaredAnnotations` 를 가지고 있어서 런타임어노테이션 리스트를 리턴해준다. 다른 어노테이션들은 런타임에 알 수 없는데,컴파일러가 그것들을 지워버렸기 때문이다.