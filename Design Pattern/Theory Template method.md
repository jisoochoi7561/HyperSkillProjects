## Theory: Template method



## Design problem

**Template Method**는 동작패턴이다. 이건 서브클래스들이 알고리즘의 부분을 구현할 때 전체적인 알고리즘을 말해준다. 이 패턴은 서브클래스들이 알고리즘의 부분을 구현하면서도 알고리즘의 구조는 유지되게 한다.

예를 들기 위해, 일하는 과정을 생각해보자. 알고리즘은 3단계이다: go to work, work, go home. 맞죠?

모든 노동자가 일을 갔다가 집에간다고하자.근데 일하는 시간은 노동자의 직업에 따라 다르다. 우리는 다른 직업들을 고를 수 있지만 알고리즘은 여전히 똑같다.

## Template method pattern

추상적인 기초 클래스는 표준 알고리즘 단계들을 구현한다. 그리고 각단계의 커스텀을 위한 기본 구현을 제공한다. 특정한 서브클래스들은 각 단계의 구체적인 구현을 제공한다.

템플릿 메소드는 다음 요소로 이루어진다:

- **Abstract Class** 원시적인 연산을 표현하고 원시적인 연산을 부르는 템플릿 메소드 자체를 표현한다;
- **Concrete Class** 은 원시적 연산을 구체적으로 구현한다.



![img](https://ucarecdn.com/b7fd06ca-19e2-449f-aaa1-8aca2a787ddf/)



이 패턴은 JDK에서 잘 쓰인다.

## Practice example

템플릿 메소드의 작동방식을 알아보기 위해 추상 클래스 `Worker` 를 만들어서 working routine을 표현해보자. 그리고 거기다 템플릿 메서드를 추가하자:

```java
public abstract class Worker {
//요 친구가 템플릿 메소드.연산들을 하는 단위! 팩토리 메소드 , 데코레이터 등에서도 비슷한 사용이 있던게 기억이 난다!
    public void work() {
        goToWork();

        workingProcess();

        goHome();
    }
//이 아래가 기본 알고리즘을 만드는 메소드들!
    public void goToWork() {
        System.out.println("= I'm going to work sadly =");
    }

    public void goHome() {
        System.out.println("= I'm going home happy =");
    }

    public abstract void workingProcess();
}
```

공통되는 알고리즘은 이미 정해졌다. 이제 두 구체적인 클래스를 만들어보자: `Programmer` and `Actor`**:**

```java
public class Programmer extends Worker {
    public void workingProcess() {
        System.out.println(" > I'm a programmer");
        System.out.println(" > I drink coffee");
        System.out.println(" > I write code");
        System.out.println(" > I drink coffee again");
        System.out.println(" > I write code again");
    }
}

public class Actor extends Worker {
    public void workingProcess() {
        System.out.println(" > I'm an actor");
        System.out.println(" > I read a scenario");
        System.out.println(" > I get used to role ");
        System.out.println(" > I play a role");
   }
}
```



`TemplateMethodDemo`클래스에서 프로그래머와 액터 인스턴스를 만들고 템플릿 메소드를 부르자:

```java
public class TemplateMethodDemo {
    public static void main(String[] args) {
        Worker programmer = new Programmer();
        Worker actor = new Actor();
        programmer.work();
        actor.work();
    }
}
```

