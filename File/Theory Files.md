## Theory: Files

프로그램이 바깥에 데이터를 저장해야 할 수도 있다 : 로그라던가.

가장 간단한 방법은 파일이다. 파일 안쓰는 운영체제가 있나?

파일은 다른 곳에 저장된 데이터집합이다.

한 단위로 생각될 수 있다.

파일들은 폴더(디렉토리)로 정리할 수 있다.

자바에서 파일을 쓰는 법을 배운다.

## The File class

`java.io` 패키지에 `File` 클래스가 있다. 이 클래스의 객체는 파일/폴더이다. 이 클래스는 파일과 폴더를 다루는데 쓰인다.

객체를 만드는 가장간단한 방법은 생성자에 string을건네는 것이다. string의 형식은 운영체제마다 다르다:

- Windows uses backslashes for paths (`'\'`),
- Linux, OS X, Android and other UNIX-like systems use the forward slash (`'/'`).

이 차이점을 언제나 명심하라

윈도우라면 반드시 `'\'`를 쓰라

만들어 보았다:

```java
File fileOnUnix = new File("/home/username/Documents");    // a directory on a UNIX-like system
File fileOnWin = new File("D:\\Materials\\java-materials.pdf"); // a file on Windows
```

파일이 존재하지 않더라도 이 코드는 작동한다. 이것이 새로운 파일을 만드는건 아니다. 이것은 "가상"파일이라고 치고 작동한다.

백슬래시인지 그냥슬래시인지 알려면 다음코드를 쓰라:

```java
System.out.println(File.separator); // '/' - for Linux
```

`File` 의 객체는 immutable이다. 

## Absolute and relative path

방금 본 파일주소는 **absolute path**이다. 이것은 시스템의 root(위의 경우는 D드라이브)에서부터 시작한다. 이것은 파일의 위치에 대해 완전한 정보를 갖고 있다.

하지만 프로그램에 절대주소를 사용하는것은 좋지 않다고 여겨진다. 왜냐면 다른 곳에가면 프로그램이 망가지기 때문이다. 

**relative path**는 시스템의 root부터 시작하지 않는다. 이것은 **working directory(현재위치)**부터 시작한다. 현재위치는 `.`으로 표현된다.상대위치는 파일에 다다르기 위해 거기에 몇개를 더 덧붙여야한다.

예시:

```java
File fileOnUnix = new File("./images/picture.jpg");
File fileOnWin = new File("./images/picture.jpg");
```

두 경로는 정확히 같다. 윈도우든 유닉스든, 플랫폼에 관계없이 동작하는 프로그램을 작성했다. 재밌는 것은 `.`은 생략가능하므로 `images/picture.jpg`도 역시 맞다는 것이다.

플랫폼에 독립적인 프로그램을 만들기위해 최대한 상대위치로 작성하라. 

상위폴더에 접근하려면  `..` (double dot)을 써라. `../picture.jpg` 은 현재폴더의 상위폴더에 있는 파일이다. `images\..\picture.jpg` 는 `picture.jpg`와 같다.

## Basic methods

`File` 인스턴스의 메소드들:

- `String **getPath()**` returns the string path to this file or directory;
- `String **getName()**` returns the name of this file or directory (just the last name of the path)
- `boolean **isDirectory()**` returns `true` if it is a directory and exists, otherwise, `false`;
- `boolean **isFile()**` returns `true` if it is a file that exists (not a directory), otherwise, `false`;
- `boolean **exists()**` returns `true` if this file or directory actually exists in your file system, otherwise, `false`;
- `String **getParent()**` returns the string path to the parent directory that contains this file or directory.

더 많은 메소드를 원한다면, [see here](https://docs.oracle.com/javase/7/docs/api/java/io/File.html).

존재하는 파일을 가르키는 객체를 만들고 그거에 관한 정보를 출력해보자:

```java
File file = new File("/home/username/Documents/javamaterials.pdf");

System.out.println("File name: " + file.getName());
System.out.println("File path: " + file.getPath());
System.out.println("Is file: " + file.isFile());
System.out.println("Is directory: " + file.isDirectory());
System.out.println("Exists: " + file.exists());
System.out.println("Parent path:" + file.getParent());
```

결과:

```java
File name: javamaterials.pdf
File path: /home/username/Documents/javamaterials.pdf
Is file: true
Is directory: false
Exists: true
Parent path: /home/username/Documents
```

존재하지 않는 파일을 가르키는 객체에 대해선:

```java
File name: javamaterials1.pdf
File path: /home/art/Documents/javamaterials1.pdf
Is file: false
Is directory: false
Exists: false
Parent path:/home/art/Documents
```

`canRead()`, `canWrite()`, `canExecute()` 는 파일을 **read/modify/execute** 할 수 있는지 알려준다. 이 것을 적절히 써서 검사하지 않으면 파일을 다룰 때 '권한없음' 에러를 마주칠 지도 모른다.