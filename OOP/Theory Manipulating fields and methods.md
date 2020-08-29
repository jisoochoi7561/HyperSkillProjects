## Theory: Manipulating fields and methods

지금이순간, 당신은 이미 리플렉션의 기본 개념을 알고 있습니다. 하지만 우리가 지금까지 한 것은 그냥 맛보기에요. 이 토픽에서 우리는 필드를 얻고 그 값을 얻고 데이터를 거기다 써넣고 메소드를 불러낼겁니다. 리플렉션으로요,

## Getting fields values

우리는 필드의 값을 얻는 법부터 알아야겠지요.다음 클래스를 보세요:

```java
class Item {
    public static final int maxItems = 100; //100이란 값이 있네요
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

`Field` 오브젝트를 쓰면 값을 알 수 있씁니다. 근본적으로 , 이럴려고 `Field` 클래스가 존재하는 겁니다. 이것은 `get` 메소드가 있는데 인자를 하나받습니다(바로 객체본체를 받습니다). 주의할 점은 `Field` 오브젝트는 `Item` 인스턴스와 엮여있지 않다는 것입니다(item1의 instock을 얻었다고 그게 item1과 관련있는 상태가 아님). 이래서 우리가 get메소드에 객체 본체를 넣어줘야 하는겁니다. 만약 스태틱필드의 값을 얻고싶다면(이건 인스턴스와는 관계가 없죠)null을 건네세요.

`Item` 인스턴스 하나의 모든 필드의 값을 빼내봅시다. 우선 인스턴스를 만들고 `Class` 객체를 뽑아냅시다.

```java
Item item = new Item("apples", 500);//새로운 인스턴스네요
Class itemClass = item.getClass(); //이때의 itemClass는 인스턴스와는 관계가 없는 클래스입니다.
Field[] fields = itemClass.getDeclaredFields(); // 이필드도 클래스의 필드지 인스턴스와는 관계가 없어요
```

 `get` 메소드로 모든 필드를 뽑아냅시다:

```java
for (Field field : fields) {
    System.out.println(field.getName() + " " + field.get(item)); // 여기 item을 건내주는게 보이시나요~
}
```

결과물..

```no-highlight
java.lang.IllegalAccessException: cannot access a member with modifiers "private"
```

어우...자바가 맞는 말을 하네요. 프라이빗 필드에는 접근할 수가 없죠. 다행히도 간단한 해결책이 있습니다. 자바는 우리가 접근할 수 있는지를 검사하고 있죠. 우리는 접근가능하게 만들 수 있습니다.`setAccessible(true)`메소드를 쓰세요.

코드수정:

```java
for (Field field : fields) {
    field.setAccessible(true);
    System.out.println(field.getName() + " " + field.get(item));
}
```

결과물:

```no-highlight
maxItems 100
inStock 19
name apples
basePrice 500
```

## Setting values to the fields

`Field`의 `set`메소드도 비슷하게 작동한다. 이것은 두개의 인자를 받는다 : 하나는 객체 그자체이고 나머지 하나는 새로운 값이다.  다시한번 말하지만 static field라면 `null` 을 넘겨주면 된다. 아래 예시를 보라:

```java
for (Field field : fields) {
    field.setAccessible(true);
    field.set(item, field.get(item)); //자기의 값을 그대로 다시 넣어주고 있다. a=a처럼.
    System.out.println(field.getName() + " " + field.get(item));
}
```

또 예외가 발생한다...

```no-highlight
java.lang.IllegalAccessException: Can not set static final int field to java.lang.Integer
```

`final` 필드는 바뀔 수 없다. 이건 맞는 말이다. 하지만 이번엔 해결책이 없다 : 파이널을 바꿀 수 있다는 것이 더 큰 문제가 되지 않겠는가?

이걸 고치려면 , 우리는 필드가 파이널이 아닐 때만 set하도록 해야한다. 모디파이어를 얻어서 `isFinal()`을 쓰자.이번엔 새로운 값으로 0까지 준비했다:

```java
for (Field field : fields) {
    field.setAccessible(true);
    if (field.getType() == int.class && !Modifier.isFinal(field.getModifiers()) {
        field.set(item, 0);
    }
}
```

이것은 완벽히 작동한다.

>  근데..사실 지금까지는 이걸 어디다 쓰는지 모르겠다 ㅠㅠ 언젠간 알겠지.

## Invoking methods

메소드를 실행하는 방법도 비슷하다. 이번에는 `Method` 의 `invoke` 메소드가 사용된다. 이 메소드는 여러개의 인자를 받을 수 있다: 정확히는 실행할 메소드의 인자개수 + 1 개를 받는다. 첫 인자는 인스턴스이다. 아까처럼. 예상했듯이 static메소드라면 `null`을 건네주면 된다:

```java
Method[] methods = itemClass.getDeclaredMethods();
for (Method method : methods) {
    method.setAccessible(true);
    System.out.println(method.invoke(item)); // 메소드는 자신의 이름,가격,이자율을 출력하는 것 총 3개가 있었다.
}
```

3 메소드 모두 인자를 받지 않으므로 `invoke` 는 단 하나의 인자만 받는다. 결과:

```no-highlight
apples
1.1
550
```

순서는 `getName` `getMarkUp` `getPrice` 순서로 실행됬음을 알 수 있다.

## When it works

런타임에 리플렉션으로 정보에 접근하고 수정하는 법을 배웠다. 이걸 읽고나면 의문점이 생긴다 : 런타임에 그걸 왜접근 해야하는데?

컴파일 타임에 다 할 수 있는 일이잖아?(ㄹㅇ루다가)

리플렉션이 해결할 수 있는 전형적인 문제중 하나는 오브젝트의 **serialization** 이다. 만약 어떤 클래스가 `Serializable` 인터페이스를 implement하지 않았다면 reflection을 쓰지않으면 시리얼라이즈 할 수 없다.

> 시리얼라이즈는 다음 토픽에 나온다..

리플렉션이 있다면 , 모든 클래스필드, 프라이빗한것까지,를 외부파일에 작성할 수 있다.

deserialize를 할 때,이 파일을 읽어서 모든 필드를 복구할 수 있다.**절대로 프라이빗 필드의 값을 바꾸지마라-deserialization할 때 빼고**,왜냐면 프라이빗을 잘못바꾸면 프로그램이 높은 확률로 가장나기 때문이다. (디)시리얼라이즈는 다음토픽에 배운다.