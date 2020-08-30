## Theory: Abstract factory

당신이 공장의 사장이라고 합시다. 당신은 현재까지 쓰이는 완벽한 생산알고리즘이 있어요. 근데, 어느날 당신은 사업을 확장하기로 결심하고 다른 도시에 공장을 하나 더 열었습니다. 이것이 뜻하는 것은 공장들에게 적용할 생산알고리즘을 캡슐화해야 한단겁니다. 이것이 *Abstract Factory*입니다.

## **The Abstract Factory pattern**

**Abstract Factory** 는 매우 생산적인 패턴입니다. 이것은 생산과정을 캡슐화합니다. 여기서 가장 흥미로운점은 구체적인 클래스를 정할 필요가 없단겁니다.

![img](https://ucarecdn.com/660ba619-51fe-430e-87c9-b7ec6fb804c0/)*TableFactory*를 만들어 봅시다.우리는 이케아처럼 유명한 회사가 되고 싶고 우리는 조립하기 쉬운걸 만들겁니다. 이런 이유로,  *InstrumentsFactory*를 만드는 것도 중요합니다. *Abstract Factory*의 개념을 먼저 확실히 합시다. 이 패턴은 생산적이고, 일반적으로, 제공된 객체를 변경하면 안됩니다. 그냥 간단히 내놔야합니다.

## **Example: TableFactory**

우선, *Table* 인터페이스를 만들고 *Kitchen* and *Office*로 구현합시다.

```java
interface Table {
    public String getTable(); //테이플 인터페이스
}

public class KitchenTable implements Table {
    @Override
    public String getTable() {
        return "There is a kitchen table"; //테이블 구현
    }
}

public class OfficeTable implements Table {
    @Override
    public String getTable() {
        return  "There is an office table"; //테이블 구현
    }
}
```

두번째로, 풀키트로 제공할 Instrument도 만들어야죠:

```java
interface TableInstruments {
    public String getInstrument(); //Instrument 인터페이스
}

public class KitchenTableInstrument implements TableInstruments {
    @Override
    public String getInstrument() {
        return "These are a kitchen table instruments"; //구현1
    }
}

public class OfficeTableInstrument implements TableInstruments {
    @Override
    public String getInstrument() {
        return "These are an office table instruments"; //구현2
    }
}
```

3번째로, *AbstractFactory*를 만듭시다. 우리는 필요한 조각들은 다 만들어 놨습니다. 예를 들어 *KitchenTableInstruments* 은 *KitchenTable*에 연관되죠. 이것은 *TableFactory* 에 생산 알고리즘을 캡슐화해야한단거고 마침내  **Abstract Factory** pattern을 얻습니다. 

```java
interface TableFactory {
    public Table makeTable();
    public TableInstruments makeTableInstruments(); //알고리즘을 내장한 추상공장
}

public class KitchenTableFactory implements TableFactory {
    @Override
    public Table makeTable() {
        return new KitchenTable(); //공장구현
    }

    @Override
    public TableInstruments makeTableInstruments() {
        return new KitchenTableInstrument(); //공장구현
    }
}

public class OfficeTableFactory implements TableFactory {
    @Override
    public Table makeTable() {
        return new OfficeTable();
    }

    @Override
    public TableInstruments makeTableInstruments() {
        return new OfficeTableInstrument();
    }
}
```

완성했습니다 손님이 두명 온 상황을 가정해보죠:

```java
Table table;
TableInstruments tableInstruments;

TableFactory kitchenTableFactory = new KitchenTableFactory();
TableFactory officeTableFactory = new OfficeTableFactory();

System.out.println("-I work as a cook. I need a kitchen table");
System.out.println("-Got It! Give me a sec,- Calling to the KitchenTableFactory. - Bring me the KitchenTable with KitchenTableInstruments");
Thread.sleep(5000);
        
table = kitchenTableFactory.makeTable();
tableInstruments = kitchenTableFactory.makeTableInstruments();

System.out.println(table.getTable() + "\n" + tableInstruments.getInstrument());
System.out.println("-There they are!\n");
Thread.sleep(5000);

System.out.println("-I am office manager. I need an office table");
System.out.println("-Got It! Give me a sec,- Calling to the OfficeTableFactory. - Bring me the OfficeTable with OfficeTableInstruments");
Thread.sleep(5000);
        
table = officeTableFactory.makeTable();
tableInstruments = officeTableFactory.makeTableInstruments();

System.out.println(table.getTable() + "\n" + tableInstruments.getInstrument());
System.out.println("-There they are!");
```

출력:

```no-highlight
-I work as a cook. I need a kitchen table
-Got It! Give me a sec,- Calling to the KitchenTableFactory. - Bring me the KitchenTable with KitchenTableInstruments
There is a kitchen table
These are a kitchen table instruments
-There they are!

-I am office manager. I need an office table
-Got It! Give me a sec,- Calling to the OfficeTableFactory. - Bring me the OfficeTable with OfficeTableInstruments
There is an office table
These are an office table instruments
-There they are!
```

