## Theory: Strategy 전략 패턴



## The design problem 디자인 문제

다음과 같은 상황을 상상해보세요 : 거대한 조건문이나 스위치문을 통해 실행할 알고리즘을 고르는 상황입니다. 가끔 이렇게 이미 완성된 구조에 새로운 알고리즘을 추가해야할 때가 있죠. 여기서 문제는 코드가 상황마다 복제되간단 겁니다. 쉽게 알고리즘을 추가하고 간결하게 코드를 작성하려면 어떡해야 할까요?

**Strategy**라는 특별한 디자인 패턴이 있습니다. 객체지향에서 가장 자주 쓰이는 패턴중 하나입니다. 알고리즘 묶음을 정의하고, 각각의 클래스에 캡슐화시켜 묶음안에서 교환가능 하게 해줍니다.

## Introducing the strategy pattern 전략 패턴 소개

**Strategy** 패턴에서, 각각의 상황에서 실행되는 알고리즘은 각각 전략이라고 불리는 자신만의 클래스에 들어갑니다. 전략 클래스는 공통의 인터페이스를 공유합니다. 전략은 행동에 관한 의미이지 내용물에 대한 얘기는 아닙니다. 

- 런타임에 알고리즘을 고르게 해줍니다;
- 알고리즘 부분을 다른 클래스와 분리해서 알고리즘의 추가를 쉽게해줍니다.;
- 알고리즘을 사용자에게서 독립적이 되게 분리 해줍니다.

이 패턴을 쓰려면 다음과 같이 하세요:

1.  `Strategy` interface(abstract strategy) 를 알고리즘 묶음용으로 선언하세요.1개 이상의 추상 메소드를 포함해야합니다..
2. 알고리즘을 뽑아내서 각각의 클래스(전략 클래스.concrete strategy)에 넣으세요. 그 클래스들은  `Strategy` interface 를 implement해야 합니다.
3.  `Context` 라는 특별한 클래스를 선언해 전략을 참조하세요. `Context`는 인터페이스를 통해 전략클래스의 인스턴스에게 실행을 맡깁니다.  동작을 스스로 하는게 아닙니다.

**Note**여기서 인터페이스가 정말로 java의 interface일 필요는 없습니다. 클래스나 추상클래스를 써도 됩니다. 가장 중요한점은 전략 클래스들이 공통으로 해당할 추상적 전략이 있어야 한단 겁니다.

아래 다이어그램이 전략 디자인패턴의 구성과 관계를 설명해줍니다

![img](https://ucarecdn.com/4d19108f-6789-4d7c-967a-5be842df103f/)

**The Strategy pattern** 전략패턴

The client code should create an instance of사용자는 `Context`의 인스턴스를 만들고,  전략을 고르고 특별한 메소드를 통해 실행시킵니다. 실행은 `Context`의 프로퍼티인 `strategy`에서 언급된 전략이 할 것입니다.

가끔은 `execute` 가 모든 알고리즘을 실행할 때가 있습니다. 보통은 일부분을 담당할 것이고, `Context`의 `invoke`에 기본적인 부분이 있을 겁니다. 전략 인터페이스가 알고리즘들의 공통된부분이 아니라 특별하고 독립적인 부분을 제공할 때 유용하겠네요.

좀 어렵네요, 예시를 봅시다.

## An example: sending messages 

고객에게 메시지를 보내는 프로그램을 생각합시다. 메시지는 여러방식으로 보낼 수 있죠 : sms나 e-mail. 덧붙이자면 미래에는 새로운방식도 추가될겁니다(예를들면 푸시메시지). 새로운 전략을 추가할 때 코드를 최대한 조금 수정하는게 바람직하겠죠.

전략패턴에 따르면, 우리는 알고리즘 묶음(메시지를 보내는 함수)을 만들어야 합니다. 각각의 알고리즘은 메시지를 보내는 구체적방법 (SMS/email)의 논리를 캡슐화시켜야 합니다

보내는 함수들의 구조:

```java
interface SendingMethod {

    void send(String from, String to, String msg);       //send알고리즘 묶음
}

class SmsSendingMethod implements SendingMethod {

    @Override
    public void send(String from, String to, String msg) {
        System.out.println(String.format("send SMS from '%s' to '%s'", from, to));  //구체화된 sms send
    }
}

class EmailSendingMethod implements SendingMethod {

    @Override
    public void send(String from, String to, String msg) {
        System.out.println(String.format("Email from '%s' to '%s'", from, to));  //구체화된 email send
    }
}
```

 `Context` 용으로 `MessageSender` 를 만듭시다. 보내는 함수를 프로퍼티로 갖고 있죠. 이 부분을 고치면 사용할 함수를 고칠 수 있습니다:

```java
class MessageSender { // 사용자는 이 클래스와 상호작용합니다

    private SendingMethod method; // 보내는함수를 프로퍼티로 가집니다
    
    // it may contain additional fields as well

    public void setMethod(SendingMethod method) { //여기에 쓰이는 method 를 사용자가 고를 수 있습니다.
        this.method = method;
    }

    public void send(String from, String to, String msg) {//실행을 프로퍼티의 sendingmethod에게 맡기고 있습니다.
        this.method.send(from, to, msg);
    }
}
```

사용자는 , `MessageSender`의 인스턴스를 만들고, 함수를 고르고,  *send* 를 실행합니다. 언제라도 함수를 바꿀 수 있죠.

```java
MessageSender sender = new MessageSender(); // message sender 를 만듭니다

sender.setMethod(new EmailSendingMethod()); // 구체적인 함수를 고릅니다

sender.send("alice@gmail.com", "bob@gmail.com", "Hello!"); //send를 실행합니다

sender.setMethod(new SmsSendingMethod()); // 함수를 바꿉니다

sender.send("1-541-444-3333", "1-541-555-2222", "Hello!");//send를 실행합니다
```

결과:

```java
Email from 'alice@gmail.com' to 'bob@gmail.com'
send SMS from '1-541-444-3333' to '1-541-555-2222'
```

물론 이 예제는 매우 간단합니다. 실제로는 더 복잡한 논리가 필요하죠. 하지만 전략패턴의 핵심은 이것입니다.

## Complicating the strategy pattern 복잡한 전략패턴

여러가지 이유로 전략패턴을 복잡하게 써야할 겁니다.

**1) 필드와 생성자.** 구체적인 전략클래스들은 자신들만의 세팅을 가질 필드가 필요할겁니다. `PushSendingMethod`클래스는 자신만의 필드를 갖고있고 우리가 생성자로 채워줘야 합니다:

```java
class PushSendingMethod implements SendingMethod {
     
    private final boolean magicFlag; //자신만의 구체적 필드가 있습니다
    
    public PushSendingMethod(boolean magicFlag) {
        this.magicFlag = magicFlag; //우리가 직접 채워줘야 합니다
    }

    @Override
    public void send(String from, String to, String msg) {
        System.out.println(String.format("Send push from '%s' to '%s'", from, to)); 
        //이것은 SendingMethod의 send를 구현한것입니다
    }
}
```

전략클래스의 인스턴스를 만들 때 값을 채워줘야 합니다:

```java
sender.setMethod(new PushSendingMethod(true));
```

**2) 다르거나 안쓰이는 인자들.** 서로다른 전략들은 서로다른 인자들을 필요로하거나 공통메소드가 받은 인자중 필요없는게 있을 수도 있습니다. 심지어 새로운 전략은 근본적 구조를 바꿔야되는 인자를 요구할 수도 있습니다.

이걸 해결할 가장 간단한 방법은 중요한 인자들을 담은 집합체(set이라던가 dataclass라던가)를 사용하는 것입니다. 전략들은 이 집합체에서 필요한 것들을 골라 쓰겠지요.

 `SendingMethod`클래스를 수정해보았습니다.  *send* 메소드는 여러 필드를 갖고있는 Message인스턴스를 인자로 받죠. 필드들은 설명을 위해 public으로 했습니다.

```java
interface SendingMethod {

    void send(Message message);
}

class Message {

    public String from;
    public String to;
    public String title;
    public String text;
    public byte[] attachment; // 그냥 필요한 이미지데이터 같은거..
}
```

**3) 여러 메소드.** 전략들은 당연히 여러개의 메소드를 가질 수 있습니다. 예를들면 `PaymentStrategy` 는 돈을 내고 돌려받는 메소드들을 포함할 수 있겟죠. 전략을 독립적인 두 전략으로 나누는 것도 가능하죠. 하지만 한 전략이 여러 메소드를 가지고 있는 것이 나을 때도 있습니다.

## Conclusion

**Strategy** 패턴은 우리가 여러 알고리즘을 가지고 있고 런타임에 실행할 알고리즘을 고르고 싶을 때 유용합니다. 알고리즘의 숫자가늘어날수록 이 패턴은 유용한데 알고리즘을 사용자에게서 분리해주기 때문입니다. 하지만 이 패턴은 추가적인 클래스들을 사용하므로 시스템이 복잡해질 수 있습니다.