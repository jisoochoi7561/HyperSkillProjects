## Theory: Encapsulation  캡슐화

 10 minutes0 / 4 problems solved

프로그램을 만들 때, 다른 동료 프로그래머들과 코드를 공유해야 할겁니다. 가끔, 여러분의 의도와 달리, 그들이 코드를 이상한 방식으로 쓸 수 있습니다. 변수를 고치고 그렇게 쓰라고 만든게 아닌 함수를 쓰면 무척 당황스러울 겁니다. 여기 문제가 있습니다 : 코드가 더 정확해져서 어느부분을 써도 되고 어느부분을 건들면 안되는지 알 수 있을까요? 객체지향언어들은 그렇게 할 수 있는 도구를 제공합니다. 우린 보통 그걸 encapsulation이라고 부릅니다.

## Encapsulation

**Encapsulation** 은 데이터들과 함수들을 묶어서 분리하는 겁니다.

우리가 OOP를 할 때, 우리는 모든걸 컨트롤 합니다. 객체는 스스로 변화하지 않습니다; 언제나 이유가 있죠,보통 여러분이 그러라고 시킵니다. 이걸 더 잘 이해하려면, 벽의 색을 생각해보세요. 어느게 더 효과적일까요, 조용히 이 속성을 바꾸기 vs `PAINT_THE_WALL`메소드 호출하기. 두번째방식이 더 명확하고, 벽의 색깔은 스스로 바뀌지 않을겁니다!

우리는 여러분이 객체의 안쪽 데이터에 매우 신중하게 접근하길 조언합니다. 그건 시간을 맞추기 위해 시계의 기어를 건드리는 거랑 마찬가지에요: 까딱하면 망가집니다. 안쪽을 들여다보는 대신에, 여러분을 위해 만들어진 시계꼭지를 돌리세요. 보시듯이, 중요하고 자세한 정보를 유저에게 분리할 필요가 있습니다.

![img](https://ucarecdn.com/b54bffb4-e5a8-4c7f-b76e-0f2176f14647/-/crop/1874x770/32,317/-/preview/)



시계 장인처럼, 우리도 객체의 내부동작을 숨겨야합니다. 그러면 유저에게 역향을 끼치지 않고 코드를 바꿀 수 있죠. 객체는 상태가 바뀔 때에도 잘 동작할 겁니다.

이렇게 객체를 바꾸는 방식은 어려워보이지만, 간단한 해결책이 있습니다.

## Getters and setters

프로그래머가 된단건 엔지니어가 된단것과 비슷해요. 아무도 수정할 수 없는 클래스를 만들고 - 수정을 막으며 안전한 작동을 제공하는거죠.  바깥쪽에서 절대로 바꾸면 안되는 데이터들을 위해, 절대로 할 수 있는 여지를 주지 맙시다. 유저를 위험하고 예측할 수없는 행동으로부터 분리하는거죠.

이렇게 하기위한 가장 쉬운 방법은-oop에서 자주 쓰입니다- get과 set을 하는 메소드를 만드는 겁니다. 이 메소드들은 **getters** 와 **setters**라고 불립니다. 이게 쓸모없다고 생각할수도 있습니다, 우리는 그냥 가서 객체를 바꾸면 되니까요. 하지만 그렇지 않습니다. Setter를 사용하면 , 우리는 객체를 바꿀 주체가 있어야 한다는걸 선언할 수 있습니다.더욱이, `SET` 메소드로, 우리는 새로운 데이터가 정확하고 객체를 망가트리지 않는지 확인할 수 있습니다. 덕분에, 우리는 위험한 행동들을 막을 수 있죠.

말했듯이, 많은 사람들이 이 방식을 씁니다. 다른 방식은 뭐가 있을까요?

## Domain-specific methods                      특정역할함수

`CLOCK` 클래스를 만든다고 합시다. 유저가 시간을 바꿀 기능을 뭘제공할까요? `SET_TIME`도 좋고, 유저가 시간을 좀더 섬세히 바꾸고 싶다면,`ADD_HOUR`, `ADD_MINUTE`, `ADD_SECONDS`를 제공할 수 있습니다. 잠깐 현실에서 어떻게 되나 봅시다.

유저는 어쩌면 시간을 세팅하지 않고 그냥 시차를 적용하고 싶어할 수 있습니다. 지금까지처럼,  `CHANGE_TIMEZONE`메소드를 만들자고 할 수 있습니다. 이제 유저는 어떻게 해야하는지 압니다 - 시차를 입력하면, 메소드가 나머지는 알아서 하겠죠.

![img](https://ucarecdn.com/e5ade60d-1110-458e-af8e-91ebb71f077c/-/crop/2146x1049/0,294/-/preview/)

이제 함수로 데이터를 제어하는 법을 알았습니다. 그러면 보호는 어떻게 하죠?

## Protecting data

데이터 보호는 프로그래밍 언어마다 다릅니다. 어떤 언어는 접근을 제한하는 키워드를 제공하죠. 다른 언어는 이름을 건드리기도 합니다. 쓰는 언어의 문서를 확인해보세요.

어떤 언어들은 **keywords** 를 사용해 클래스의 필드를 감춥니다. 덕분에, 누구까지 변수에 접근할 수 있나 고를 수 있습니다. 또 다른 방법은 **name conventions**입니다.  예를 들면, 대문자로 시작하는 속성은 공개되고, 소문자로 시작하는 속성은 보호되는 거죠.

공개라는것은 속성이 이름으로 그냥 부를 수 있단 겁니다



**Name mangling** 은 외부접근에게는 다른 이름을 제공하는 방법입니다. 여러분의 클래스에`MINUTES`속성이 있다고 해봅시다, 그리고 외부에는, 이 속성이 `DO_NOT_USE_THESE_MINUTES`라고 표시될겁니다. 이것은 접근 자체는 제한하지 않지만 경고를 함으로서 제한하죠.



만약 필드를 읽을수만 있께하려면, getter를 써야합니다.



## Conclusion

OOP는 객체를 메소드로 다루는걸 암시합니다.  getters, setters, 그리고 domain-specific methods 로 속성에 관한 접근을 제어할 수 있습니다. 속성이 보이는 범위를 언어마다의 방식으로 제한할 수 있습니다.

우리는 이걸 내부작동을 숨기고 더 쉽게 객체를 관리하는데에 필요로합니다.
