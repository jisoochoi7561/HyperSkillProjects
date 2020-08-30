## Theory: Command

**behavioral** 패턴을 들어본적 있을 것이다.

>들어본적 없음. 전략패턴이 이거랑 관련있던 것 같기도 하고.

 **behavioral** 패턴은 객체들의 상호작용에 관한 이야기이다. 약 12개의 디자인 패턴이 이 행동패턴에 속하는데,  이중 커맨드 패턴은 특별하다 : 이건 다른 디자인패턴들보다 많이 쓰인다. 커맨드 패턴의 목적은 커맨드와 컨슈머의 로직을 분리하는 것이다.

커맨드 패턴의 정확한 정의는 : 커맨드와 관련된 모든 데이터를 하나의 객체안에 **encapsulating**  하는것. 보통 이 데이터란 것은 메소드들,파라미터들,또 그와 관련된 객체들로 이루어져 있다.우리는 저 하나의 객체를 **Receiver**라고 한다. 즉 분리할 때의 중요한 점은 만약 값을 변경해야 한다면 , 단 하나의 클래스(리시버)만을 변경해야 한단 것이다.

전통적으로 커맨드패턴을 쓰려면 5단계가 필요하다.

> 커맨드는 명령이다. 
>
> 리시버는 그에 필요한 데이터들을 모아놓은 객체이다.

## The classic version

커맨드를 쓰기위한 전통적 요소:

- **Command** 인터페이스는 보통 커맨드를 실행할 단 하나의 메소드를 가진다
- **ConcreteCommand**는 리시버에게 콜을 건네주는 연산이다; 전통적 접근에서, 커맨드는 로직을 실행하지 않고 리시버의 메소드를 몇개 깨울 뿐이다.
- **Receiver**는 행동을 하는 방법을 안다.
- **Invoker** 는 커맨드에게 요청을 하라고 요구한다.
- **Client** 는 **ConcreteCommand** 객체를 만들고 Receiver를 셋한다.

*Command* 인터페이스가 언어에서의 인터페이스일 필요는 없다. 간단한 클래스이거나 추상클래스여도 된다. 중요한 것은 이것이 concrete커맨드에 상속될 추상적 command를 표현해야 한단 거다.

![img](https://ucarecdn.com/2ec87a27-dfe8-4105-8fa6-8705de6f9e53/)

*Client*는 *Receiver* 객체와 *ConcreteCommand* 객체를 만들고 커맨드를 실행할 Invoker 를 세팅합니다.  *ConcreteCommand*의 각 타입 (e.g. *CreateFileCommand*, *RemoveFileCommand*)  은 파라미터들을 표현하는 필드집합이 있습니다. 커맨드는 리시버의 메소드를 몇개 콜해서 구체적인 행동을 하고 프로그램의 상태를 바꾸도록 합니다.

> 하나도 모르겠음. 근데 예시를 보면 확실히 좀 나음.
>
> 그냥 무시하고 예시로 넘어가는게 나을지도.

예시를 들으면 좀 이해가 갈 것입니다.

## Example of the command pattern

집에 자동화 시스템을 설치한다고 합시다. 불을 끄고 켜야 합니다. 여기서 우리는 매우 비슷한 두개의 명령이 있습니다. 그래서 우리는 하나의 인터페이스 `Command` 를 먼저 만들 겁니다. 이것은 단 하나의 메소드 `execute()`를 가집니다.

```java
public interface Command {
    void execute();
}
```

그리고 우리는 두개의 클래스를 만들어서 `Command` 인터페이스를 적용할 것입니다. 이 구체적인 클래스들은 명령에 필요한 데이터들을 담고 있습니다 . 즉 당신은 각 명령에 따라 구체적인 클래스를 만들어야 합니다. 우리는 두 구체적 클래스를 만들겁니다. 왜냐면 우리의 프로그램이 두가지 명령을 하니까요  *Light On* 과 *Light Off*.

우선 `LightOnCommand` 에 `Command` 인터페이스를 적용합니다.

```java
public class LightOnCommand implements Command { //concrete command다.너무 반갑다.command는 그냥 콘크리트커맨드들을 묶어주는 인터페이스 였던 것이다.

    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.lightOn();
    }
}
```

다음으로, `LightOffCommand` 도 해줍시다. `LightOffCommand` 는`LightOnCommand` 와 별 다르지 않습니다.

```java
public class LightOffCommand implements Command {

    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.lightOff();
    }
}
```

우리의 리시버인  `Light` 클래스를 이제 만들겠습니다.

```java
public class Light {

    public void lightOn() {
        System.out.println("Turn on Light");
    }

    public void lightOff() {
        System.out.println("Turn off Light");
    }
}
```

가끔은 간단한 예시로는 디자인패턴을 배우기 어렵습니다...왜냐면 현실과 동떨어져 있으니까요. 예를 들면 , 누군가는 "왜 `Light` 클래스를 만들지? 메소드 두개를 그냥 커맨드에 만들면되는거 아닌가?"할 수 있습니다. 네, 현실에서는 , `Light` 클래스는 커맨드와는 아무상관이 없는 더 많은 필드와 메소드들로 더 복잡할 겁니다.

다음으로, 우리는 **Invoker class**를 만들어야 합니다. 이 클래스는 어떻게 커맨드가 실행되는지 결정합니다. 예를 들어, invoker는 특정순서로 실행되야 하는 커맨드 리스트를 갖고 있을 수 있습니다. 여기서 invoker는 그냥 일반적인용어로, 우리는 이걸 커맨드가 어떻게 실행되는지를 결정하는 클래스를 부를 때 씁니다. 당신이 원하는 이름으로 지으시면 됩니다.

우리의 invoker `Controller` 를 만들었습니다.

```java
public class Controller {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}
```

마침내 클라이언트 `main` 메소드가 인보커를 사용해서 커맨드를 실행합니다.

```java
public class HomeAutomationDemo {

    public static void main(String[] args) {

        Controller controller = new Controller();
        Light light = new Light();

        Command lightsOn = new LightOnCommand(light);
        Command lightsOff = new LightOffCommand(light);

        controller.setCommand(lightsOn);
        controller.executeCommand();

        controller.setCommand(lightsOff);
        controller.executeCommand();
    }
}
```



무슨일이 일어나는지는 명백하죠. 기본적으로 중요한 3단계가 있습니다.

1. invoker 객체(여기선 `Controller` )를 만들기.
2. 우리가 실행할 command 객체 만들기
3. invoker를 사용해 커맨드 실행하기

> 이제 조금 알겠다.
>
> 콘크리트 커맨드가 우리가 하고싶은 일이다.
>
> 커맨드는 그것들을 받아주기 위한 상위 클래스이다. Command command = new concreteCommand
>
> 인보커가 커맨드들을 받아서 어느 로직을 해야할지 결정하고 그 커맨드를 실행하라고 한다.
>
> 실제 일은 리시버가 한다. 

또 다른 단계를 거쳐서 이 과정을 도울 수 있을 것이다. 예를 들면 `main()`는 `Light` 오브젝트가 필요하기 때문에 이걸 만드는 과정을 거치고 있다.이 코드의 결과는:

```java
Turn on Light
Turn off Light
```

## Additional options

커맨드 패턴은 다음 옵션들과 함꼐 사용될 수 있다:

- 나중에 실행할 커맨드들을 큐에 넣기;
- 취소 연산 추가;
- 커맨드 연산 기록 남기기;
- 커맨드를 시리얼라이즈 해서 디스크에 저장하기;
- 커맨드들을 모아넣어서 하나로 만들기:이게 바로 **macros.**

이 옵션들은 필수는 아니지만 자주 쓰인다.

가끔은 커맨드는 리시버에게 시키지 않고 그냥 자기가 일을 직접 한다. 이것도 현실에서 쓰이는 옵션이다.

## Applicability

이 패턴을 적용하는 곳:

- **GUI buttons and menu items.** Swing 프로그래밍에서 *Action*은 커맨드객체 이다. 원하는 행동을 할 수 있는 능력말고도 *Action* 은 관련된 아이콘, 단축키 등등을 가질 수 있다.

- **Networking.** 모든 커맨드 객체를 네트워크에 보네 다른 기계가 실행하게 할 수 있다. 예시 : 온라인게임의 유저의행동

- **Transactional behavior.** 취소 연산과 비슷하게, 데이터베이스 엔진이나 소프트웨어 설치기는 현재까지 일어난/앞으로 일어날 연산들을 기억하고 있을 수 있다. 그중 하나가 실패하면 , 다른 것들이 전부 되돌아갈 수 있도록(이걸 우리는**rollback**이라 부른다. )

- **Asynchronous method invocation.** 멀티스레딩 프로그래밍에서 이 패턴은 커맨드들을 비동기적으로 실행하게 해준다. 이경우 인보커는 메인스레드에서 작동하고 개별 스레드에서 돌아가는 리시버에게 요청을 보낼 것이다. 인보커는 커맨드의 목록을 가지고 있다가 리시버가 일을 끝내면 다음명령을 내릴 것이다.

## Conclusion

이 커맨드 패턴의 가장 중요한 장점은 실행을 하라고 하는 객체와 실행을 하는 객체를 분리하는 것이다. 이 패턴의 수정버전이 요청기록을 남기거나 취소기능을 구현하거나 매크로커맨드를 만들거나 하는 것들이다. 다만 이 패턴은 그냥 메소드를 부르지 않고 추상화를 쌓기 떄문에 프로그램이 더 복잡해질 수 있다.

