## Theory: Factory method



**Factory Method** 패턴은 시작하기 좋다. 특히 다른 팩토리 패턴을 이해하고 싶다면. 이것은 분명 당신이 쓸 수 있는 가장 간단한 패턴이다.

당신이 뭔가(좋아하는걸 고르세요!) 만드는 공장의 사장이라고 생각해보자 - 당신은 운좋게도 훌륭한 엔지니어를 데리고 있다. 그사람은 말하는 타입의 물건을 뭐든지 만들어낸다 : *TYPE_A* or *TYPE_B.* 이것이 팩토리 메소드 디자인 패턴이다.

이 패턴은 객체를 만드는 인터페이스를 정의하지만 정확히 뭘 만들지는 그것의 서브클래스에게 넘긴다. 따라서 기본적으로, 팩토리 메소드는 클래스의 생성을 서브클래스로 위임하게 해준다. 모든 팩토리의 목적은 소비자를 클래스구조로부터 분리하는 것이다. 팩토리 메소드는 템플릿 메소드 패턴의 특별한 형태이다.

## Structure

팩토리 메소드의 요소들:

- Creator;
- Concrete Creator;
- Product;
- Concrete Product.

![img](https://ucarecdn.com/0bd448e0-f5c7-44bf-bf0e-7af4bbaf52c7/)

4 요소는 각각 다른 역할을 한다.바로 전의 decorator를 이해했다면 여긴 그냥 쉽다:

1. **Creator** 은 객체를 만들 가상/추상 메소드를 선언한다. 여기의 factory method를 구현해서 객체를 만든다.
2. **ConcreteCreator** 은 팩토리메소드를 구현해 특정 객체를 만든다
3. **Product** 는 팩토리 메소드가 만든 객체들을 묶어주는 인터페이스다.  (로봇)
4. **ConcreteProduct** 은 생성물의 구체적 타입이다.  (두발로봇,세발로봇,네발로봇, 8발로봇)

The pattern in JDK is available in `java.util`, `java.io` and `javax.persistence`.

## Practice example

우리의 추상적 예시를 더 구체적으로 만들어 보자. 우리는 현재 공장의 사장이다. 공장이 테이블을 만든다고 하자 : 테이블은 꼭 필요한 물건이다. 당신은 멋진직원과 함께하는데, 아까 그 엔지니어, 즉 우리의 팩토리 메소드말이다.

추상클래스 *Table을 선언:*

```java
abstract class Table { //이건 Product네
    private String name;

    Table(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void attachLegs() {
        System.out.println("Attaching Legs");
    }

    void attachTableTop() {
        System.out.println("Attaching tabletop");
    }
}
```

그 다음 우리는 구체적인 테이블 두개를 구현: *TableOffice* 와*TableKitchen* 클래스. 주의점은 추상클래스는 생성자를 갖는데, 초보 자바개발자는 어리둥절  할 수 있다.

```java
class TableOffice extends Table {
    TableOffice(String name) {
        super(name);
    }
}

class TableKitchen extends Table {
    TableKitchen(String name) {
        super(name);
    }
}
```

3번째로는 팩토리를 만든다.이름 *TableStore*이고 추상클래스 *TableFactory*를 구현했다:

```java
abstract class TableFactory { //이게 creator네

    abstract Table createTable(String type); //만드는 함수가 구체화되지 않은 상황

    Table orderTable(String type) throws InterruptedException {//공장들의 공통 로직은 구현해놈
        Table table = createTable(type);
        if (table == null) {
            System.out.println("Sorry, we are not able to create this kind of table\n");
            return null;
        }
        System.out.println("Making " + table.getName());
        table.attachLegs();
        table.attachTableTop();
        Thread.sleep(1500L);
        System.out.println("Created " + table.getName() + "\n");
        return table;
    }
}

class TableStore extends TableFactory { //이게 concrete creator
    @Override
    Table createTable(String type) { //만드는 함수 구체화
        if (type.equals("office")) {
            return new TableOffice("Office Table");
        } else if (type.equals("kitchen")) {
            return new TableKitchen("Kitchen Table");
        } else return null;
    }
}
```

출력:

```java
class TestDrive {
    public static void main(String[] args) throws InterruptedException {
        TableStore tableStore = new TableStore();

        Table strangeTable = tableStore.orderTable("Mysterious table");

        Table officeTable = tableStore.orderTable("office");
        Table kitchenTable = tableStore.orderTable("kitchen");

    }
}
Sorry, we are not able to create this kind of table

Making Office Table
Attaching Legs
Attaching tabletop
Created Office Table

Making Kitchen Table
Attaching Legs
Attaching tabletop
Created Kitchen Table
```