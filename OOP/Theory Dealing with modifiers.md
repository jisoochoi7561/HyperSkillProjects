## Theory: Dealing with modifiers

우리는 지금까지 어떻게 생성자,메소드,필드를 얻는지를 알아봤습니다. private한거까지 알아냈죠. 근데 이걸 왜알아낸거죠? 만약에 우리가 이걸 어떻게 쓸줄 모른다면. 사실, 이 클래스들은 더 많은걸 할 수 있습니다. 이걸 배울겁니다.

우선은 modifiers를 다루는 법을 공부합니다. 짐작하셨겠지만 리플렉션은 어떤 modifier를 갖고 있는지 알아낼 수 있습니다.

## Checking modifiers

이 클래스를 보세요:

```java
class Item {
    public static final int maxItems = 100; //파이널이넹
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

    protected double getMarkUp() {
        double markUp = 0.1;
        // ... connecting to the remote server
        return 1 + markUp;
    }
}
```

보시듯이, 이 클래스의 필드의 타입은 다양합니다 :  `private`, `protected`, `public`, `static` and `final`.

`Modifier` 는 이런것들을 다루기 위해 만들어졌습니다.

 `getModifiers()`메소드를 a `Field`, `Method` or `Constructor`에 쓰면 , modifer들을 전부 얻어낼 수 있습니다.

사실, 이 메소드는 `int` 를 리턴합니다. 근데 그 인트가 modifier를 표현합니다 . 무슨 말인지 알려면 , 아래의 표를 보세요 32비트짜리 숫자입니다. 각 비트의 위치가 모디파이어에 관여합니다. 그 부분의 모디파이어가  `true` 인지 `false` 인지 알려줍니다. 가장 오른쪽 비트는 `public`을 뜻합니다 그 왼쪽은 `private`을 뜻하고요.오른쪽에서 4번째는 `static `이고요 뭐 그런식입니다. 이걸 외울필요는 전혀 없습니다:

| **Modifiers in source code** | **Modifiers as an integer** |
| ---------------------------- | --------------------------- |
| public                       | 00000000 ... 00000001 = 1   |
| private                      | 00000000 ... 00000010 = 2   |
| protected                    | 00000000 ... 00000100 = 4   |
| static                       | 00000000 ... 00001000 = 8   |
| final                        | 00000000 ... 00010000 = 16  |
| other modifiers              | other bits                  |

이것들을 결합하면 결합한 modifier를 체크할 수 있습니다.아래 표를 보세요:

| **Modifiers in source code** | **Modifiers as an integer** |
| ---------------------------- | --------------------------- |
| public static                | 00000000 ... 00001001 = 9   |
| private final                | 00000000 ... 00010010 = 18  |
| protected static final       | 00000000 ... 00011100 = 28  |

이 비트들을 다룰 필요는 전혀없죠.`Modifier` 는 특별한 스태틱 메소드가 있습니다.`isPublic` 이나 `isStatic`같은거요. 이걸로 모디파이어를 체크하면 되요.

우리가 지금까지 배운 access modifies(접근 제한자.퍼블릭,프라이빗등)말고도 많은 modifier가 있답니다 : `synchronized`, `volatile`, `transient`, `native`, `interface`, `abstract`, `strictfp`.  이 중 몇개는 거의 볼일이 없습니다. 이 토픽에서는 위의 표에 있던 모디파이어들만 다루겠습니다.

## Code example

다음 코드를 보세요:

```java
Item item = new Item("apples", 500);

Class itemClass = item.getClass();
Field[] fields = itemClass.getDeclaredFields();

for (Field field : fields) {
    int modifiers = field.getModifiers();
    if (Modifier.isPublic(modifiers)) {
        System.out.print("public ");
    }
    if (Modifier.isProtected(modifiers)) {
        System.out.print("protected ");
    }
    if (Modifier.isPrivate(modifiers)) {
        System.out.print("private ");
    }
    if (Modifier.isStatic(modifiers)) {
        System.out.print("static ");
    }
    if (Modifier.isFinal(modifiers)) {
        System.out.print("final ");
    }

    System.out.print(field.getType() + " ");
    System.out.println(field.getName());
}
```

위 코드는 다음 결과를 출력합니다:

```no-highlight
public static final int maxItems
public static int inStock
private class java.lang.String name
protected int basePrice
```

결과는 예상대로네요. 메소드와 생성자에도 같은 일을 해보세요.

이 토픽에서 모디파이어 정보를 얻는 법을 배웠습니다.