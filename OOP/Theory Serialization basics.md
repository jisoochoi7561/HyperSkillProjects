## Theory: Serialization basics

객체의 생존시간은 런타임으로 제한된다. 우리가 프로그램을 껐다 키면 저번 실행에서의 정보들은 다 날아가 버린다. 컴퓨터 게임에 이런일이 생긴다고 생각해보자: 이 쓰레기게임을 누가하겠는가! 다행히 프로그램은 객체들을 하드드라이브 같은 안전한 저장소에 저장해서 다음에 프로그램이 시작하면 불러올 수 있다.

## **Serialization and deserialization**

객체의 상태를 저장하고 불러오기 위한 두가지 과정이 있다 : **serialization** and **deserialization**.

**Serialization** 은 객체의 상태를 바이트 스트림으로 바꾸는 과정이다. 객체는 안전한 저장소에 저장되어 나중에 다시 구현될 수 있다.

**Deserialization** 은 반대과정으로 시리얼화된 바이트로 다시 객체를 만드는 것이다.

자바객체를 바이너리/텍스트 형태로 시리얼라이즈하는 방법은 여러가지가 있다. 예를 들면 XML과 JSON. 자바 플랫폼은 시리얼라이즈에 특별히 주의를 기울이고 default protocol을 제공한다.

다음 그림이 객체가 바이트스트림으로 시리얼화 되어 파일이나 데이터베이스에 저장되고 다시 재구현되는 과정을 보여준다.

![img](https://ucarecdn.com/f727023c-5e01-452d-90a3-33f95fa1455d/)

시리얼라이즈의 또 다른 특징은 시리얼화된 객체를 네트워크를 통해 보내 다른 자바프로그램이 deserialize할 수 있단 것이다. 따라서 시리얼라이즈는 연결된 시스템이 객체를 쉽게 사용하게 해준다.

## Making a class serializable

클래스를 시리얼가능하게 만드려면 그것은 반드시 `Serializable` interface를 implement해야 한다. 이것은 메소드가 없는 표시용 인터페이스이다. 이것은 컴파일러에게 이걸 쓰는 클래스는 특별한 행동이 있다고 알려준다.

```java
class SomeClass implements Serializable {
    // fields and methods
}
```

시리얼화된 클래스는 그 안에 여러 객체나 원시타입을 포함할 수 있다. 관련된 모든 것들도 물론 시리얼화된다. 필드가 시리얼화 되는걸 막기위해선 `transient` 키워드를 쓰면 된다:

```java
private transient String nonSerialziedField;
```

권장사항으로 `serialVersionUID` 라는 특별한 필드를 이 인터페이스를 implement한 클래스마다 붙여주는 걸 추천한다. 이 필드는 반드시 `static`, `final`이고 `long` type이어야 한다:



```java
private static final long serialVersionUID = 7L;
```

`serialVersionUID`필드는 시리얼화된 오브젝트를 보내는 프로그램과 받는 프로그램이 서로 소통할 수 있고 제대로 같은 클래스를 로드했음을 확인하기 위해 사용된다. 만약 센더와 리시버의 버전 넘버가 다르면 런타임에러`InvalidClassException` 가 일어난다. 이 숫자의 비교는 우리가 모르게 시리얼화와 디시리얼화 중에 일어난다. 값이 안맞을 때에만 에러가 발생한다.

필수는 아니지만 , 시리얼가능한 클래스는 `serialVersionUID`를 명시적으로 선언하도록 추천된다. 이 숫자를 선언하고 사용하면 여러 서로다른 자바 컴파일러 사이에서 이 숫자가 유지됨이 보장된다. 또한 , 클래스들마다 이 숫자가 달라야 하는건 [아니다.](https://stackoverflow.com/questions/21182037/should-serialversionuid-be-unique-for-different-classes)



## Streams for objects

자바에서 시리얼라이즈와 디시리얼라이즈 메커니즘은 스탠다드 I/O system과 바이트스트림에 기초하고 있다. `ObjectOuputStream` 과 `ObjectInputStream` 클래스를 사용했다.

첫 클래스는 `void writeObject(Object object)`라는 메소드를 제공하는데 이것은 객체의 상태를 스트림에 작성한다. 두번째 클래스는  `Object readObject()`라는 비슷한 메소드를 제공하는데 이건 객체를 다시 만든다. 두 메소드 모두 뭔가 잘못되면 예외를 던진다.

`아래의 SerializationUtils` 안에 시리얼라이즈/디시리얼라이즈용 함수 두개를 완전하게 구현해 놓았다.

```java
class SerializationUtils { //그냥 클래스
    /**
     * Serialize the given object to the file
     */
    public static void serialize(Object obj, String fileName) throws IOException { //예외를 던져주고
        FileOutputStream fos = new FileOutputStream(fileName); //파일아웃풋스트림
        BufferedOutputStream bos = new BufferedOutputStream(fos); //의 버퍼드 버전
        ObjectOutputStream oos = new ObjectOutputStream(bos); // 에다가 오브젝트아웃풋스트림을 적용
        oos.writeObject(obj); // 오브젝트를 저장
        oos.close();//스트림닫음
    }

    /**
     * Deserialize to an object from the file
     */
    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}
```

메소드 두개 모두 다른 타입의 스트림들을 사용하나 : file,buffer,object. 다른 스트림을 기반으로 새로운 스트림 오브젝트를 만드는건 자바에서 흔한 일이다. 그것들은 서로를 wrap해서 새로운 기능을 추가한다. [the decorator pattern](https://hyperskill.org/learn/step/5216)을 보시길. 이 코드를 복사해다가 좀 고쳐보면서 실험해보면 좋다.

여기 짧은 설명이 있다.

- `FileOutputStream` and `FileInputStream` 은 파일을 다루는 스트림이다;
- `BufferedInputStream` and `BufferedOutputStream` 은 옵션이다. 하지만 버퍼를 쓰므로 속도를 더 빠르게 해줄 수 있다;
- `ObjectOutputStream` and `ObjectInputStream`은 시리얼라이즈와 디시리얼라이즈를 한다.또 스트림들을 파일에 결합시킨다.
- 두 메소드 모두 스트림을 close해서 메모리누수를 막고 있다.

## An example: citizens

가정해보자 당신이 정보시스템을 개발해야하는데 그 시스템은 계속해서 시민들의 목록을 저장해야 한다고. 여기 이 시스템에 관한 두가지 클래스가 있다:`Citizen` and `Address`. 두 클래스 모두 `Serializable` 이고 `serialVersionUID` 를 지정해두었다.

`Citizen` 클래스는 시민을 뜻한다. 이름 주소 그리고 저장되면 안되는 정보( non-serializable)`passport`가 있다.

```java
public class Citizen implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Address address;
    private transient String passport;

    // getters and setters

    @Override
    public String toString() {
        return "Citizen{" +
                "name='" + name + '\'' +
                ", passport='" + passport + '\'' +
                ", address=" + address +
                '}';
    }
}
```

`Address`클래스는 주소를 뜻한다. 이것은 세가지 문자열 필드 `state`, `city` and `street`를 가지고 있다.

```java
class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private String state;
    private String city;
    private String street;

    // getters and setters

    @Override
    public String toString() {
        return "Address{" +
                "state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
```

getter와 setter는 생략했다(귀찮음)

`serialVersionUID` 의 값은 당신이 필드를 바꿀 때마다(수정,추가,제거)증가해야한다. 아니면 디시리얼라이즈 중에 바꾸기전의 객체와 충돌이 일어나 에러가 발생할 수 있다.



## Serializing and deserializing objects

마침내 이제 시리얼라이즈를 할 차례다. 여기 시티즌 배열을 만드는 함수가 있다.

```java
public static Citizen[] initCitizens() {
    Citizen mark = new Citizen();
    mark.setName("Mark Olson");
    mark.setPassport("503143798"); // the passport was set

    Address markAddress = new Address();
    markAddress.setState("Arkansas");
    markAddress.setCity("Conway");
    markAddress.setStreet("1661  Dawson Drive");

    mark.setAddress(markAddress);

    Citizen anna = new Citizen();
    anna.setName("Anna Flores");
    anna.setPassport("605143321"); // the passport was set

    Address annaAddress = new Address();
    annaAddress.setState("Georgia");
    annaAddress.setCity("Atlanta");
    annaAddress.setStreet("4353  Flint Street");

    anna.setAddress(annaAddress);

    return new Citizen[]{ mark, anna };
} //mark와 anna는 열심히 살아가는 시민이다.
```

사실 2명의 시민만 있다는건 말이 안되긴한다. 하지만 예시일 뿐이니까.. 우리가 두 시민에게 모두 여권을 증정했다는 걸 기억해두라.

여기 `main`메소드가 있다. 이것이 모든 일을 할 것이고 `SerializationUtils` 클래스(위에서 우리가 만들었다)를 사용할 것이다.

```java
public static void main(String[] args) {
    String filename = "citizens.data";
    try {
        SerializationUtils.serialize(initCitizens(), filename);
        Citizen[] citizens = (Citizen[]) SerializationUtils.deserialize(filename);
        System.out.println(Arrays.toString(citizens));
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

이것은 시민목록을 `citizens.data`파일안에 시리얼라이즈하고 그걸 다시 로드해서 프린트한다. 여기서 우리는 간단한 예외처리도 만들었다.현실에서는 더 완벽히 해야한다.

예상대로 프로그램은 두명의 시민을 출력한다.

```java
[Citizen{name='Mark Olson', passport='null', address=Address{state='Arkansas', city='Conway', street='1661  Dawson Drive'}}, 
Citizen{name='Anna Flores', passport='null', address=Address{state='Georgia', city='Atlanta', street='4353  Flint Street'}}]
```

두 시민 모두 파일로부터 잘 불러와졌다. 여권항목은 null인데 왜냐면 그건`transient`이기 떄문이다.

파일안의 시리얼된 데이터가 어떻게 생겼나 궁금할 수 있다. 이렇게 생겼다:

```no-highlight
�� ur "[Lorg.hyperskill.problems.Citizen;i� �����  xp   sr  org.hyperskill.problems.Citizen        L addresst !Lorg/hyperskill/problems/Address;L namet Ljava/lang/String;xpsr  org.hyperskill.problems.Address        L cityq ~ L stateq ~ L streetq ~ xpt Conwayt Arkansast 1661  Dawson Drivet 
Mark Olsonsq ~ sq ~ t Atlantat Georgiat 4353  Flint Streett  Anna Flores
```

몇몇 반가운 단어가 보이긴 하지만 사람이 읽을 만한 파일은 아니다.

최종 코드는  [available on GitHub](https://github.com/hyperskill/hs-java-samples/tree/master/src/main/java/org/hyperskill/samples/serialization/basics). 이것은 실제 프로젝트와 비슷하게 좀 다른구조로 되어있다.