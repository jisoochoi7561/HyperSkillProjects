## Theory: Regexes in programs 프로그램에서 정규표현식

정규표현식은 활용여지가 많음. 문서 편집, 프로그래밍언어 구현, 파싱과 문법 하이라이팅, 파일과 웹사이트의 유용한 정보 추출. 이 레슨에서 정규표현식으로 문자열을 다루는 간단하고 강력한 프로그램을 소개할 것임.

## Program with a regular expression 정규표현식을 쓰는 프로그램

넣은 문자가 유효한 로그인인지 확인하는 프로그램을 생각해보자. 우리는 로그인이 영어문자와 숫자 ,`_`,`$`로 이루어져 있다고 가정한다.길이는 5이상 12이하이다.

하나더: 시작과 끝의 공백은 무시한다.

```java
import java.util.Scanner;

class CheckLoginProblem {

    public static void main(String[] args) {

       /* The scanner object to read data from the standard input */
       Scanner scanner = new Scanner(System.in);
       
       /* The common pattern for valid logins */
       String loginRegex = "\\s*[a-zA-Z0-9_$]{5,12}\\s*";

       /* The read string which may be a login */
       String mayBeLogin = scanner.nextLine();

       boolean isLogin = mayBeLogin.matches(loginRegex);

       System.out.println(isLogin);
    }
}
```

기억하라,  `"\\s*"`는 공백을 다룰 때 매우 좋다. `" "`을 쓰지 말고 저걸 쓰시오.

테스트해봅시다.

**The input-output pair 1:**

```java
  testuser7
true
```

**The input-output pair 2:**

```java
 test  
false
```

**The input-output pair 3:**

```java
      user!!!
false
```

**The input-output pair 4:**

```java
$test_user
true
```

간단한 정규표현식으로 강력한 프로그램을 만들었다. 원한다면 변형해서 사용해보시오.

## Conclusions 결론

문자열에서 어떻게 정규식을 쓰는지를 배웠다. 더 정교한 로그인을 위해 regex를 변형해볼 수도 있을 것이다.