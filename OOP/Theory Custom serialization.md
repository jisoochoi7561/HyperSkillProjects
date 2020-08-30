## Theory: Custom serialization

우리는 객체의 기본적인 serialization 을 배웠다. 필드의 시리얼화를 막기 위해 `transient` 키워드를 쓸 수 있다. 하지만 가끔 이것만으론 부족하다. 프로젝트를 deserialize할 때 뭔가 판별용 필드가 필요할 수 있다. 이걸 원한다면 당신은 **custom serialization**을 써야한다. 여기에는 디자인적인 요소도 있다 : 클래스는 나중에 바뀔수도 있다 - 그러면 그 전에 처리한 정보가 깨져버린다.

## How to customize serialization?

자바는 두가지 메소드를 통해 시리얼라이즈를 커스텀하게 해준다. 그 메소드는 :

- `writeObject()`
- `readObject()`

이제 조금 이상할 수도 있다. 이것은 방금 본 자바 시리얼라이즈의 내장함수가 아닌가? 이 함수들은 상속받지도,오버라이드되지도 오버로드 되지도않았다. 당신은 그저 이걸 당신만의 논리와 함께 커스텀 시리얼라이즈 클래스에 구현하면 된다. 이런식으로:

```java
public class ClassName implements Serializable {
    
    // transient and non-transient fields

    private void writeObject(ObjectOutputStream oos) throws Exception {
       // write the custom serialization code here
    } // 그냥 깡으로 써버린다. 오버로드도 오버라이드도 아니다..

    private void readObject(ObjectInputStream ois) throws Exception {
       // write the custom deserialization code here
    }
}
```

`oos.writeObject()`메소드를 실행하면 **JVM**이 우선 당신이 `writeObject()` 를 구현했는지를 본다. 만약 그렇다면 JVM은 그 코드를 기본 시리얼라이즈 함수 대신에 쓴다. 비슷하게 JVM은 `readObject()`도 `ois.readObject()` 을 쓸 때 대신 쓸 것이다. 

## Initialize transient variables

이제 저번 예시로 돌아가보자. `oos.writeObject()` 가 패스워드를 시리얼라이즈 하지 않는걸 알고 있다.

> 저번예시가 어딨는지 모르겠다. 뭐 PASSWORD가 transient니 일반적인 시리얼라이즈화는 먹히지 않는단 얘기같다.

우리는 이 문제를 오브젝트를 재구성할 때 패스워드를 초기화해줌으로서 해결할 수 있다. 이런식으로



```java
public class User implements Serializable {
    String userName = "admin";
    transient String password = "password"; //이건 시리얼화 안된다.
  
    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        password = new String(" ");
    } 
}
```

여기서 우리는 `writeObject()`메소드를 안써도 된다 : 우리는 시리얼라이즈 하는 과정에는 수정할 것이 없다. 우리는 `readObject()` 만 구현하면 된다. 첫번째 줄의 `ois.defaultReadObject()`는 기본적인 디시리얼라이즈를 할 것이다. 그 말은 저 코드 실행이후 non-transient필드는 제대로된 값을 갖고 transient 필드는 null값을 갖는단 것이다.

그리고 우리는  `password = new String(“ ”)`로 비밀번호를 초기화해준다.

실행결과:
`Before serialization : Username : admin, Password : passwordAfter serialization and deserialization : Username : admin, Password :`

패스워드가 null이 아니라 빈 칸이 되었다.

> 여기의 실행결과는 뜬금없다. 내생각엔 바로 전 토픽의 강의인 Citizen과 passport가 사실은 User와 password였던 것 같다. 업데이트가 따로 되다보니 이상한 흐름이 되버린 것 같다.

## More examples of custom serialization

커스텀 시리얼라이즈를 하는 이유는 더 있다:

- 클래스의 중요부분을 **암호화**
- 더 압축된 시리얼라이즈

클래스의 필드를 암호화하는 법을 알아보자. 우리는 두개의 함수 `encrypt` and `decrypt`가 있고, 이걸 암호화에 쓸 것이다. 이것들의 구현은 여기서 중요하진 않으니까 그냥 그런 함수가 있다고 치자.

이 코드는 `writeObject()` 와 `readObject()` 를 둘다 쓴다:

```java
public class User implements Serializable {
    String userName = "admin";
    transient String password = "password"; //얘는 시리얼라이즈 안된다

    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject(); //시리얼라이즈 된다
        String encryptPassword = encrypt(password); //암호화한 패스워드
        oos.writeObject(encryptPassword);//암호화한패스워드를 시리얼라이즈
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        String password = decrypt((String)ois.readObject());
    }
}
```

우선,  `writeObject()` 안의 `oos.defaultWriteObject()` 가  기본적인 시리얼라이징을 한다. 그리고 패스워드를 암호화한다. 그 다음 암호화한 패스워드를 시리얼라이즈 한다. 그런 식으로 읽기도 똑같이 한다.

> 이건 어렵다..나중에 또 보자.