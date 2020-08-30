## Theory: Builder



## The design problem

상상해보세요 - 멋진 레스토랑.. 당신은 멋지게 주문을 합니다..

레스토랑은 멋진요리를 팔죠 : 각각은 재료들로 이루어져 있죠.

이런일이 일어날 수 있습니다. 손님이 요리를 수정해달라고 하는거죠 : 예를들면 , 양파를 편식한다던가, 땅콩에 알러지가 있다거나.

쉐프는 요리의 속성을 다이나믹하게 바꿔야 합니다.

이런 일이 일어날 때 , **Builder** 디자인패턴이 시간을 많이 아껴줍니다. 자세히 알아 봅시다.

당신은 GUI 라이브러리를 만들고 있습니다. 개발자로서, 당신은 나중에 이걸 쓸 개발자들을 위해 편리한 라이브러리를 만들려 최선을 다합니다.라이브러리는 알림창(AlertDialog)을 포함합니다. 이 알림창은 title, text, apply and cancel buttons, footer, and picture을 갖고 있습니다. 당신은 이걸 표현하는 클래스를 만들기로 결정합니다:

```java
class AlertDialog {
    private String title;
    private String text;
    private String applyButton;
    private String cancelButton;
    private String footer;
    private String picture;

    AlertDialog(String title, String text, String applyButton, String cancelButton, String footer, String picture) {
        this.title = title;
        this.text = text;
        this.applyButton = applyButton;
        this.cancelButton = cancelButton;
        this.footer = footer;
        this.picture = picture;
    }
}
```

생성자를 통해 인스턴스를 만들면 되죠:

```java
AlertDialog alertDialog = new AlertDialog("title", "text", "applyButton", "cancelButton", "footer", "pathToPicture");
```

나중에 개발자가 타이틀과 캔슬버튼만 있는 알람창이 필요하다고 합시다. 이 경우, 당신의 생성자는 별로 좋아보이지 않네요:

```java
AlertDialog alertNotification = new AlertDialog("Completed successfully", null, "Ok", null, null, null);
```

이런 못생긴 생성자를 피하기 위해 다음과 같은 생성자를 만듭니다:

```java
class AlertDialog {
    ...

    AlertDialog(String title, String applyButton) {
        this(title, null, applyButton, null, null, null);

    ...
}
```

문제는 해결됬죠. 하지만 다른 개발자가 다른조합의 알림창을 원한다면요? 이경우 또 새로운 생성자를 만들겠네요? 그러면 생성자는 계속 늘어나겠네요? 이 상황을 constructor pollution이라고 부릅니다.

다른방법이 있습니다. 하나의 디펄트 생성자 `AlertDialog() {...}` 를 추가하고 각 필드에 대한 setter를 만드세요:

```java
AlertDialog alertDialog = new AlertDialog();
alertDialog.setTitle("Completed successfully");
alertDialog.setApplyButton("Ok");
```

문제는 해결됬습니다. 하지만 만약 알림창이 immutable이어야 한다면 , 이 해결책을 적용할 수 없습니다.(set이 안된다)

## The Builder pattern

**Builder** 패턴은 **creational(만드는)** 디자인 패턴입니다. 복잡한 생성자를 분리하기 위해서 쓰입니다. 이것은 원하는 구조의 오브젝트를 만들게 해줍니다. 이 패턴의 중요 장점은 constructor pollution을 피할 수 있단 겁니다.

![img](https://ucarecdn.com/3cde3c0f-45f0-429d-9600-7157e3b40d94/)

> UML은 다이어그램 같은 거 입니다.

빌더 패턴은 다음과 같은 요소로 구성됩니다:

- **Builder** 인터페이스는 생성을 하는 단계를 표현합니다. 각각의 복잡한 객체는 만들기위해 빌더 클래스의 도움을 요구합니다.

- **ConcreteBuilder** 은 **Builder**를 implement합니다. 이것은 요구되는 형태의 객체를 만들기 위해서입니다. 이것은 최종결과물의 부분을 만들고 결합하고 그걸 보여줄 인터페이스를 제공합니다. 이것이 핵심 요소입니다.

- **Director** 는 **Builder** 를 사용해서 객체를 만드는 과정을 관리합니다. 이것은 최종 객체를 직접 만들진 않습니다.

- **Product**는 콘크리트빌더를 통해 만들어진 복잡한 최종 객체입니다.유저가 원한 구조를 하고 있습니다.

우리의 예시에서 우리는 간단한 *AlertDialog* 를 정해진 구조로 만들겁니다 : *Title, Text, ApplyButton,* and *CancelButton.*

빌더 패턴이 우리가 정해진 프로퍼티를 피하게 해주지만 추가하게는 못해주는걸 보세요. 곧 볼겁니다.

## Example

두 단계면 됩니다만 , 정신 똑바로 차리세요. 첫단계가 무척 강력하거든요. 빌더를 포함한 AlertDialog의 구현입니다 :

```java
class AlertDialog {
    private String title;
    private String text;
    private String applyButton;
    private String cancelButton;

    private AlertDialog(String title, String text, String applyButton, String cancelButton) {
        this.title = title;
        this.text = text;
        this.applyButton = applyButton;
        this.cancelButton = cancelButton;
    }

    @Override
    public String toString() {
        String str = "";
        if (title != null) {
            str += "The title is: \"" + title + "\"\n";
        }
        if (text != null) {
            str += "The text is: \"" + text + "\"\n";
        }
        if (applyButton != null) {
            str += "The applyButton is: \"" + applyButton + "\"\n";
        }
        if (cancelButton != null) {
            str += "The cancelButton is: \"" + cancelButton + "\"\n";
        }

        return str;
    }

    static class Builder {
        private String title;
        private String text;
        private String applyButton;
        private String cancelButton;

        Builder() {}

        Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        Builder setText(String text) {
            this.text = text;
            return this;
        }

        Builder setApplyButton(String applyButton) {
            this.applyButton = applyButton;
            return this;
        }

        Builder setCancelButton(String cancelButton) {
            this.cancelButton = cancelButton;
            return this;
        }

        AlertDialog build() {
            return new AlertDialog(title, text, applyButton, cancelButton);
        }
    }
}
```

간략하게 표현한 거라서 , 위의 예시는 **Builder** and **Director**가 없습니다.여기의`Builder` 는 **ConcreteBuilder** 입니다.`AlertDialog` 는 **Product**입니다.생성자를 바로 쓰는걸 막기위해 `private` 으로 설정한걸 보세요.

마지막 단계는 *TestDrive*입니다. 위에서 봤듯이 *build()*메소드가 우리가 원하는 객체를 리턴합니다.즉 이것이 **Director**역할을 합니다.

```java
class TestDrive {
    public static void main(String[] args) {

        AlertDialog twoButtonsDialog = new AlertDialog.Builder()
                .setTitle("Two buttons dialog")
                .setText("You can use either `Okay` or `Cancel`")
                .setApplyButton("Okay")
                .setCancelButton("Cancel")
                .build(); //이게 디렉터역할을 하고 있습니다.빌더에게 어떻게어떻게 하라고 하고 있습니다.

        System.out.println(twoButtonsDialog);

        AlertDialog oneButtonsDialog = new AlertDialog.Builder()
                .setTitle("One button dialog")
                .setText("You can use `Close` only")
                .setCancelButton("Close")
                .build();
//한번 빌드가 끝나면 이후에는 빌더로 뭐 추가할 수가 없습니다.
        System.out.println(oneButtonsDialog);
    }
}
```

끝났습니다. 위의 코드는 UML과는 좀 다르지만 , 이런식으로 많이들 씁니다. 이 패턴은 작고 쉬워 보이지만 다른 패턴들(*Composite* *pattern,*Fabric pattern)과 결합하면 매우 강력합니다.

출력은:

```java
The title is: "Two buttons dialog"
The text is: "You can use either `Okay` or `Cancel`"
The applyButton is: "Okay"
The cancelButton is: "Cancel"

The title is: "One button dialog"
The text is: "You can use `Close` only"
The cancelButton is: "Close"
```

