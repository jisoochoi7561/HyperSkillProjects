## Theory: Observer



## Design problem

유튜브에 새로운 동영상을 올릴 때, 당신의 프로필정보를 페이스북에서 업데이트 할떄 또 인스타그램에 사진을 올릴 때 이 모든 정보는 당신의 구독자와 친구들에게 자동으로 보내집니다. 이것은 관찰가능한 상태가 바뀌어서 옵저버가 그걸 눈치챘다는 뜻입니다. 

## Observer

**observer pattern** 은 행동패턴중 하나입니다.이것은 생산자와 구독자의 관계를 정의하는데 쓰입니다.그리고 구독메커니즘에도 쓰입니다. 하나의 상태가 바뀌면 이거에 관련된 모든 것들이 알아채고 업데이트 됩니다.

옵저버는 알고싶어하는 사람들(프로그램에선 요소겠죠)전부에게 상태가 바뀌거나 일어난 사건에대해 알려줍니다. 많은 경우에 옵저버 패턴은 로우레벨에서 일어납니다 - 클래스와 그거에 관련된 것들의 의존성을 줄입니다.직접적인 연결을 끊어서요.

옵저버 패턴의 요소들:

- Observable
- Concrete Observable
- Observer
- Concrete Observer

![img](https://ucarecdn.com/dbed6e9b-4e98-44cd-b4ae-f055255bbd82/)

4개 요소는 각각 다른 기능이 있습니다:

1. Observable 은 옵저버들을 승인하고,제거하고,변화에대해 알려줍니다;
2. Concrete Observable 는 Observable 을 implement하고 몇가지 상태를 구체화합니다;
3. Observer 는 Observable 을 구독하고 그것이 알려주는거에 귀를 쫑긋 합니다;
4. Concrete Observer 는 Observer interface를 implement하고 업데이트(Observable notification)에 반응합니다.

The observer pattern in JDK is available in `java.util.Observer` and `java.util.EventListener`.

## Practice example

유튜브는 옵저버 패턴의 좋은 예시입니다. 우리는 유튜브 채널(Observable)이 있고 구독자 (Observers)가 있습니다. 각 구독자는 새로운 비디오가 뜨면 알람받을 것입니다.

Observable 인터페이스는 옵저버를 add remove notify하는 연산이 있습니다(구현은 콘크리트에서 하쇼):

```java
public interface Observable {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObserver();
}
```

유튜브 채널은 옵저버리스트와 연결된 옵저버블 인터페이스의 구체적인 구현(concrete implementaion)입니다.Generic <Observer> 가 채널과 구독자 사이의 연결을 제공합니다:

```java
public class YoutubeChannel implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>(); //이게 연결을 제공함

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer); //구현중
    } 

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);//구현중
    }

    public void releaseNewVideo(String video) {
        System.out.println(“Release new video : “ + video);//새로운 메소드
        notifyObserver();
    }

    @Override
    public void notifyObserver() {
        for(Observer observer: observers) {//구현중
            observer.update();
        }
    }
}
```

Observer 인터페이스는 Observable 이 옵저버에게 notify 하면 할 행동을 업데이트합니다:

```java
public interface Observer {
    public void update();  //구현은 안된 모습
}
```

유튜브 구독자 (Concrete Observer) 들이 채널로부터 새 비디오 notification을 받습니다:

```java
public class YoutubeSubscriber implements Observer {

    private Observable observable;  //콘크리트옵저버블이 들어가겠지
    
    public YoutubeSubscriber(Observable observable) {
        this.observable = observable; //observable세팅
    }

    @Override
    public void update() {
        System.out.println("New video on channel!"); //업데이트함수 구현
    } 
}
```

데모버전입니다:

```java
public class Main {
    public static void main(string[] args) {
        YoutubeChannel youtubeChannel = new YoutubeChannel();
        YoutubeSubscriber subscriberA = new YoutubeSubscriber(youtubeChannel);
        YoutubeSubscriber subscriberB = new YoutubeSubscriber(youtubeChannel);
        YoutubeSubscriber subscriberC = new YoutubeSubscriber(youtubeChannel);
        youtubeChannel.addObserver(subscriberA);
        youtubeChannel.addObserver(subscriberB);
        youtubeChannel.addObserver(subscriberC);
        youtubeChannel.releaseNewVideo("Design Patterns : Factory Method");
        youtubeChannel.releaseNewVideo("Design Patterns : Proxy");
        youtubeChannel.releaseNewVideo("Design Patterns : Visitor");
    }
}
```