## Theory: Facade

많은 양의 서로다른 서브시스템으로 이루어진 복잡한 시스템을 가지고 있다고 합시다. 이 시스템을 쓰려면 당신은 모든 서브시스템을 매일매일 활성화 해줘야합니다. 만약에 이 알고리즘을 대신해줄 커다란 빨강버튼이 있는 컨트롤러가 있다면 멋지겠죠? 이것이 바로 **Facade** structural pattern 입니다.

## **The Facade pattern**

**Facade** 는 서브시스템 인터페이스들을 통합한 인터페이스를 클라이언트인 당신에게 제공합니다. 이러면 메인 시스템을 쓰는게 편해지죠. 차를 생각해보세요 : 당신이 키를 꽂고 시동을 걸면 나머지 서브시스템은 자동으로 활성화되야합니다 - 엔진,에어컨,라디오.당신이 목적지에 도착해서 차를 끄면 엔진도 꺼지고 라디오도 꺼지고 에어컨도 꺼져야겠죠.

다음 그림이 Facade를 사용할 때 요소들의 관계를 보여줍니다:

![img](https://ucarecdn.com/6744e76c-798e-40cb-b9a5-1ff6c93b51db/)

facade가 커다랗게 묶어주네용;;

## **Example: CarFacade**

이 패턴은 너무 쉬워서 자기도 모르는 사이에 써왔을 겁니다.

맨처음에 우리는 서브시스템먼저 정의해야 겠지요 : *Engine, StereoSystem,* and *HeatedSeats*.

```
class Engine {
    private String description;

    Engine() {
        this.description = "Engine";
    }

    void on() {
        System.out.println(description + " is on");
    }

    void off() {
        System.out.println(description + " is off");
    }
}
```

엔진은 키고 끌 수 있씁니다.

```
class HeatedSeats {
    private String description;
    private int heatLevel;

    HeatedSeats() {
        this.description = "HeatedSeats System";
    }

    void on() {
        heatLevel = 1;
        System.out.println(description + " is on");
    }

    void off() {
        heatLevel = 0;
        System.out.println(description + " is off");
    }

    void increaseHeatLevel() {
        if (heatLevel == 0) {
            on();
        } else if (heatLevel == 1 || heatLevel == 2) {
            heatLevel++;
        } else {
            off();
        }
    }
}
```

에어컨이라고 했지만 사실 좌석난방시스템이었습니다;; 이 시스템은 난방레벨을 키면 1 끄면 0이고요 *increaseHeatLevel()*를 통해 올릴 수 있습니다.너무 올라가면 꺼집니다:

```
public class StereoSystem {
    private String description;
    private String trackTitle;
    private int volume;

    StereoSystem() {
        this.description = "Stereo system";
        this.volume = 50;
    }

    void on() { System.out.println(description + " is turning on"); }

    void off() { System.out.println(description + " is turning off"); }

    void playTrack(String title) {
        this.trackTitle = title;
        System.out.println(title + " is playing");
    }

    public void pause() {
        System.out.println("Track: \"" + trackTitle + "\" were paused");
    }

    public String getTrackTitle() {
        return ("The current track is: \"" + trackTitle + "\"");
    }

    public void setVolume(int volume) {
        if (volume > 100) {
            this.volume = 100;
        } else {
            this.volume = volume;
        }
    }

    public int getVolume() {
        return volume;
    }
}
```

스테레오시스템은 우리가 CarFacade에 추가할 마지막 서브시스템입니다.  이것도 키고 끌 수 있죠. 이 시스템은 원하는 트랙을  *‘*playTrack()’ 메소드로 틀어줍니다.‘pause()’로 멈출수 있어요.볼륨도 조절가능합니다.

CarFacade를 만듭시다. Facade는 위에 말한 모든 서브시스템을 포함해야하고 원하는 알고리즘을 구현해야해요. 우리의 예시에서 우리는 엔진을 키고 난방을 키고 기본트랙을 틀어야 겠지요.

```
class CarFacade {
    private Engine engine;
    private HeatedSeats heatedSeats;
    private StereoSystem stereoSystem;

    CarFacade(Engine engine, HeatedSeats heatedSeats, StereoSystem stereoSystem) {
        this.engine = engine;
        this.heatedSeats = heatedSeats;
        this.stereoSystem = stereoSystem;
    }

    public void turnOnCar() {
        engine.on();
        heatedSeats.on();
        stereoSystem.on();
        stereoSystem.playTrack("Queen - I'm in love with my car"); //한번에 하고있습니다
    }

    public void turnOffCar() {
        engine.off();
        heatedSeats.off();
        stereoSystem.off(); //한번에 하고있습니다
    }

    public void increaseHeatedSeats() {
        heatedSeats.increaseHeatLevel();
    }

    public void playTrack(String title) {
        stereoSystem.playTrack(title);
    }
}
```

composition(결합)으로 Facade design pattern 을 구현했습니다. 컴포지션은 상속과는 다르게 당신의 프로그램의 구조를 더 유연하게 해줍니다. 아 근데 이주제는 따로 토픽이 필요하겠네요.

어쩄든 메인시스템과 서브시스템을 결합한 후에 우리는 시동을 걸어볼 수 있겠네요

```
Engine engine = new Engine();
StereoSystem stereoSystem = new StereoSystem();
HeatedSeats heatedSeats = new HeatedSeats();

CarFacade carFacade = new CarFacade(engine, heatedSeats, stereoSystem);

carFacade.turnOnCar();
System.out.println();
   for (int i = 0; i < 5; i++) {
      Thread.sleep(1500);
      System.out.println("Driving to work... " + i + "km");
      switch (i) {
         case 1:
            Thread.sleep(500);
            carFacade.playTrack("Queen - Bohemian Rhapsody");
            break;
         case 2:
            Thread.sleep(500);
            carFacade.playTrack("Queen - I want to break free");
            break;
         case 3:
            Thread.sleep(500);
            carFacade.playTrack("Queen - Another one bites the dust");
            break;
         case 4:
            Thread.sleep(500);
            carFacade.playTrack("Queen - Scandal");
            break;
      }
   }
System.out.println("\nWe have arrived");
Thread.sleep(1000);
carFacade.turnOffCar();
```

결과는 좋습니다:

```
Engine is on
HeatedSeats System is on
Stereo system is turning on
Queen - I'm in love with my car is playing

Driving to work... 0km
Driving to work... 1km
Queen - Bohemian Rhapsody is playing
Driving to work... 2km
Queen - I want to break free is playing
Driving to work... 3km
Queen - Who wants to live forever? is playing
Driving to work... 4km
Queen - Scandal is playing

We have arrived
Engine is off
HeatedSeats System is off
Stereo system is turning off
```

