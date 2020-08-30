## Theory: Decorator



## Design problem

가끔은 클래스 전체보다는 인스턴스 하나에 추가적인 책임(responsibility)을 부여해야 할 때가 있습니다. 유저에게 그래픽인터페이스를 보여주기 위한 라이브러리를 생각해보세요. 새로운 속성, 말하자면 , 새창이나 새로운동작(스크롤을한다거나)을 추가할 수 있어야 합니다. 상속을 통해 책임추가가 가능하죠. 하지만 이 방식은 유연하지 않아요.

## Decorator

더 유연한 접근법은 **decorator**라고 불리는 다른 객체에 요소를 집어넣는 거에요. 데코레이터는 객체에 추가적인 책임을 더해주는 구조적 패턴입니다.

> 구조적 패턴이란 겹치는 패턴이란 뜻이다. 예를들어 인터페이스를 두개 겹치는 것이다. 데코레이터를 있던거에 겹친단 뜻이다.

 객체자체의 기능은 그대로 두고요. 이것은 당신이 다이나믹하게 객체의 행동을 런타임에 바꿀 수 있다는 걸 뜻합니다- 데코레이터로 감싸서요-

> 오브젝트 자체의 기능은 그대로이다. 할 수 있는 일은 늘어나지만(데코레이터로 인해).

데코레이터는 몇몇 동작-인터페이스의 핵심동작이 아닌-을 더하는데 쓰입니다. 데코레이터는 다음과 같은 목적에 완벽히 부합해요:

- 결과 캐싱;
- 메소드의 실행시간 측정;
- 유저 접근 제한.

데코레이터 패턴의 요소들:

- *Component* 는 새로운 책임을 얻을 객체를 위한 인터페이스입니다.

  > 즉 우리가 다룰 객체의 타입입니다. 

- *Concrete Component* 는 컴포넌트 인터페이스와 새로운 책임을 받을 객체를 만듭니다 ;

  > 즉 이 친구의 인스턴스가 우리의 객체(본체)입니다. 이것의 상위타입인 Component로 대표되므로 우리는 Component를 타입으로 써줄겁니다.

- *Decorator*는 컴포넌트 레퍼런스를 갖고 오버라이드한 컴포넌트 메소드를 갖습니다.

  > 즉 데코레이터 안에 컴포넌트가 들어있는 꼴이됩니다..사실은 컴포넌트에 데코레이터를 적용한 겁니다만.

- *Concrete Decorator* 는 데코레이터를 extend하고 새로운 함수,속성,상태를 추가합니다.

  > 콘크리트 데코레이터는 구체적인 데코레이터 입니다.

![img](https://ucarecdn.com/8422f83b-451b-442a-8588-86ec6507b72f/)




The decorator pattern in JDK:

- Streams: java.io package;
- Collections: java.util package.

> 정리하면 다음과 같습니다.
>
> 우리가 다룰 객체는 여러 종류가 있겠죠. 그 객체들은 각각 concrete component타입입니다.그 객체들을 묶어주는게 component입니다. 따라서 어떤 객체든 component로 캐스팅할 수 있습니다.
>
> 데코레이터는 컴포넌트를 필드에 가지고, 나름대로의 메소드등을 통해 기능을 확장해 줍니다.
>
> 데코레이터도 어떤 기능을 추가해주느냐에 따라 종류가 있겠죠. 그게 콘크리트 데코레이터입니다.
>
> 결국 타입은 데코레이터타입(컴포넌트 타입)이 되겠죠.
>
> 물론 콘크리트데코레이터(컴포넌트 타입) 등이 될수도 있겠지만 다형성을 쓰려면 적절하진 않겠네요.
>
> 근데 데코레이터 자체가 컴포넌트를 implement합니다.
>
> 그럼 최종타입은 처음처럼 component가 되고, 정말로 기능만 확장된 느낌이 되겠네요.

## Practice example

구체적인 예를 들어 봅시다. 우리의 *Component* 는 일을해야하는 소프트웨어 개발자입니다. Developer 인터페이스를 만듭시다:

```java
public interface Developer { 

    public String makeJob(); //우리의 객체는 Developer입니다. 어떤 구체적인 타입일지는 모르겠지만.
}
```

concrete developer를 만듭시다:

```java
public class JavaDeveloper implements Developer { //자바 디벨로퍼는 디벨로퍼의 일종입니다.이경우는 하나뿐이지만 실전에선 여러 개가 있겠죠

    public String makeJob() { 
        return "Write Java Code"; //구체적으로 구현됬습니다.
    }
}
```

developer decorator를 묘사하고 우리의 developer들에게 기능을 부여해줍시다:

```java
public class DeveloperDecorator implements Developer { //이 데코레이터는 결국 Developer입니다.
    private Developer developer; //안에 Developer를 받아서 건드리지를 않습니다.

    public DeveloperDecorator(Developer developer) {
        this.developer = developer;
    }

    public String makeJob() {
        return developer.makeJob(); //뭔가 데코레이팅을 하려고 하네요.특별한 일을 하진않았습니다. 콘크리트가 아니라서 모양만 만들고 구현은 안한 것 같습니다.
    }
}
```

콘크리트 데코레이터는 시니어 자바 개발자입니다. 코드리뷰라는 새로운 책임이 있습니다:

```java
public class SeniorJavaDeveloper extends DeveloperDecorator { //구체적인 데코레이터 입니다.여전히 Developer로 표현가능합니다.

    public SeniorJavaDeveloper(Developer developer) {
        super(developer);
    }

 
    public String makeCodeReview() {
        return "Make code review"; //새로운 기능이 추가됬습니다.
    }

    public String makeJob() {
        return super.makeJob() + " " + makeCodeReview(); //기존기능도 업그레이드 됬습니다.
    }
}
```

두번째 데코레이터는 팀리더입니다: 여전히 developer이고 새로운 책임-고객에게 주간보고-이 있습니다:

```java
public class JavaTeamLead extends DeveloperDecorator {

    public JavaTeamLead(Developer developer) {
        super(developer);
    }

    public String sendWeekReport() {
        return "Send week report to customers."; //새로운 책임이 추가
    }

    public String makeJob() {
        return super.makeJob() + " " + sendWeekReport(); //기존 책임이 업그레이드
    }
}
```

최종버전:

```java
public class Task {

    public static void main(String[] args) {
        Developer developer = new JavaTeamLead( //또 디벨로퍼를 받아 다시 데코레이터로 장식해 디벨로퍼를 내놓습니다.
                                  new SeniorJavaDeveloper( //데코레이터로 장식합니다. 여전히 Developer죠.물론 구체적타입 써도 됩니다만
                                      new JavaDeveloper())); // 콘크리크 컴포넌트인 자바디벨로퍼로 시작합니다
        
        System.out.println(developer.makeJob());
        //developer는 Developer지만 실제로는 JavaTeamLead입니다.하지만 Developer로 캐스팅되있으니 sendWeekReport를 바로 실행하지는 못할 것입니다.대신 우리는 makeJob에서 관련된 기능을 이미 가능하도록 구현해놓았습니다.
    }
}
```