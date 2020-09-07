## Theory: Reading files

스탠다드 자바 클래스 라이브러리는 파일에게서 데이터를 읽는 방법을 제공한다. 몇몇개는 너무 낡았지만 , 몇몇개는 최신이다. 여기서는 그 중 기본적인 두개만 배운다. 

## Reading data using Scanner

`java.util.Scanner` 로 파일을 읽을 수 있다. 이 클래스는 인풋데이터를 읽는데 무척 좋다. 이것은 원시타입이나 문자열(에 정규식을 결합해서)도 다룰 수 있다.

우선, `java.io.File` 의 인스턴스와 `Scanner` 인스턴스를 만들어서 거기에 파일객체를 넣자. 그러면 스캐너를 통해 마치 standard input처럼 파일을 읽을 수 있다.

`pathToFile`이라는 문자열이 있따고 하자. 이것은 공백으로 분리된 여러가지 숫자들을 담고있는 파일로의 경로이다.

파일객체를 만들고 스캐너에 넘겨주자

```java
File file = new File(pathToFile);
Scanner scanner = new Scanner(file); // it throws FileNotFoundException (checked)
```

파일객체로 스캐너를 만들 때 `FileNotFoundException`을 체크해줘야 한다.

이제 스캐너의 메소드를 사용해서 데이터를 읽으면 된다.

한줄 읽어보자

```java
while (scanner.hasNext()) {
    System.out.print(scanner.nextLine());
}
```

이 코드는 파일에서 한줄씩 읽어서 standard output에 출력한다.

스캐너를 쓴 다음엔 close해서 메모리 누수( [leaks](https://en.wikipedia.org/wiki/Resource_leak))를 막아라. 스캐너를 열고 닫는 좋은 방법은  **try-with-resources** 이다. 더 알고싶다면  [the official tutorial](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html):

```java
File file = new File(pathToFile);
 
try (Scanner scanner = new Scanner(file)) {
    while (scanner.hasNext()) {
        System.out.print(scanner.nextLine() + " ");
    }
} catch (FileNotFoundException e) {
    System.out.println("No file found: " + pathToFile);
}
```

파일이 다음과 같다면:

```java
first line
second line
third line
```

콘솔엔 다음과같이 출력된다:

```java
first line second line third line
```

스캐너는 물론 정수 불린 더블 등등도 읽어준다. 이렇게 standardoutput에 출력하지 않고 배열에 저장해둬도 있다.

## Reading all text from a file as a single string

자바 1.7 이후에 파일을 위한 새로운 클래스와 메소드들이 등장했다. 이 토픽에서 텍스트파일 전체를 읽는 법을 공부하자. 이것은 조그만 파일에만 써야한다.조그맣다는 것은 자바가 사용가능한 RAM보다 작다는 것이다.

우선 임포트하라:

```java
import java.nio.file.Files;
import java.nio.file.Paths;
```

`Files` 클래스는 파일에 관한 메소드를 가지고 있고 `Paths` 클래스는 파일로의 경로인 특별한 객체를 리턴하는 메소드를 가지고 있다. 

다음 함수가 파일의 모든 텍스트를 리턴한다:

```java
public static String readFileAsString(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
}
```

`readFileAsString` 을 써서 `HelloWorld.java`의 소스코드를 읽고 출력해보자.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadingFileDemo {
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void main(String[] args) {
        String pathToHelloWorldJava = "/home/username/Projects/hello-world/HelloWorld.java";
        try {
            System.out.println(readFileAsString(pathToHelloWorldJava));
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
    }
}
```

출력은 소스코드모양이다:

```java
package org.hyperskill;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}
```

