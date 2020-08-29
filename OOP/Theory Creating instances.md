## Theory: Creating instances

저번 토픽에서 우리는 어떻게 리플렉션으로 필드를 컨트롤 하고 메소드를 실행하는지 배웠다. 리플렉션은 인스턴스를 만드는데에도 쓸 수 있다.

## Acquiring a constructor

다음 코드를 보세요:

```java
class Item {

    private String name;
    protected int basePrice;

    public Item() {
        this("default");
    }

    public Item(String name) {
        this(name, 0);
    }

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
    
    protected double getMarkUp() {
        double markUp = 0.1;
        // ... connecting to the remote server
        return 1 + markUp;
    }
}
```

3개의 생성자가 있고 그 중 하나는 비어있습니다(파라미터가 없는 생성자를 말하는듯).

자바에서 모든 객체는 생성자로 생성됩니다. 리플렉션역시 마찬가지고요 :인스턴스를 만드려면 생성자가 필요합니다.

다행히도 `Constructor` 클래스가 그 역할을 합니다.

클래스는 여러개의 생성자를 가질 수 있으므로 우선 그 중 하나를 뽑아야 합니다. 두 가지방법으로 적절한 생성자를 고를 수 있습니다:

\1.  `getDeclaredConstructors()` 메소드를 `Class`객체에서 실행한후 배열에서 적절한걸 고르세요.
\2. 만약에 생성자에 어떤 인자가 들어가는지 알거나 인자가 필요없는 생성자를 쓸거라면`getDeclaredConstructor(... )` 메소드를 쓰고 `Class` 타입의 객체를 건네줘서 파라미터의 타입을 알려주세요. 만약 인자가 없는 생성자를 쓸거면 아무것도 안건네주면 됩니다.

생성자를 찾은 후엔 `newInstance(... )`를 써서 인스턴스를 만드세요.

또 다른 방법으로는 `Class` 인스턴스에서 `newInstance()` 메소드를 쓰면 됩니다. 예를 들어 `Item` 클래스의 새로운 인스턴스를 만들려면 `Item.class.newInstance()`를 쓰면 됩니다. 현재는 이 메소드는 deprecated됬으므로 위의 다른방법들을 쓰세요.

## Getting constructor parameters

위의 메소드들로 인스턴스를 만들어 봅시다. 우선 모든 생성자와 인자들을 프린트해보죠:

```java
Class itemClass = Item.class;
Constructor[] constructors = itemClass.getDeclaredConstructors();

for (Constructor constructor : constructors) {
    Class[] params = constructor.getParameterTypes();
    System.out.println("Constructor:");
    if (params.length == 0) {
        System.out.println("No params");
        continue;
    }
    for (Class param : params) {
        System.out.println(param);
    }
    System.out.println();
}
```

`getParameterTypes()`메소드를 콜해서 인자들의 타입을 알아낼 수 있습니다.

결과:

```no-highlight
Constructor:
class java.lang.String
int

Constructor:
class java.lang.String

Constructor:
No params
```

예상대로네요. 3개의 생성자가 있습니다: 인자 0개,1개,2개

## Creating an instance

인자 타입들을 알고 있으면 , `getDeclaredConstructor(... )`메소드를 쓰기 편해집니다. 아래는 2개인자를 요구하는 생성자로 인스턴스를 만드는 예시입니다:

```java
Constructor constructor = itemClass.getDeclaredConstructor(String.class, int.class); 
Object instance = constructor.newInstance("orange", 990);

System.out.println(instance.getClass().getSimpleName());
```

이 생성자는 `Object`를 리턴하고 있지만 ,사실은 `Item` 객체를 리턴한 겁니다. 위의 코드(마지막줄)를 실행하면 확실히 알 수 있습니다:

```no-highlight
Item
```

메소드도 사용할 수 있습니다.`getName()`을 사용해 보겠습니다.

```java
Method getName = instance.getClass().getDeclaredMethod("getName");
System.out.println(getName.invoke(instance));
```

결과:

```no-highlight
orange
```

명백히 이 인스턴스는 `Item` 인스턴스 입니다.