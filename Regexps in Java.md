## Theory: Regexps in Java (자바 정규표현식)



 **regular expression**(정규표현식) 은 문자열집합을 특정해주고 텍스트의 검색,편집,생산에 사용되는문자시퀀스(문자열?) 이다. 다른 프로그래밍 언어들처럼 자바도 regular expression을 지원한다. 우리는 이미 약간 알고 있다. 이 레슨에서 자바는 이것을 어떻게 구현하고 있는지 배울것이다.

## Simple matching 

우선, `String` 으로 regex를 만들 수 있다. 예시를 보자:

```java
String aleRegex = "ale"; // the "ale" regex
```

자바에서, `String` 타입은 regex에 대한 지원이 이미 만들어져 있다. String은 `matches` 라는 함수를 가지고 있는데 regex 패턴을 인자로 가지고 string과 패턴이 일치하는지 확인한다. 이 함수는 string*전체* 가  regexp과 일치할 때에만  `true` 를 리턴한다. regex로 만들어진 패턴은 왼쪽에서 오른쪽으로 적용된다.

아래 예시에서, `aleRegex` 와 여러문자열들을 매치해보고 있다:

```java
"ale".matches(aleRegex);  // true, ale은 aleRegex="ale"과 정확히 일치한다
"pale".matches(aleRegex); // false, "pale"은 p를 더 갖고있다
"ALE".matches(aleRegex);  // false, 대소문자가 다르다
```

 `"pale"` 이 매치되지 않는것을 확인할 수 있다.이유는 Java regex 구현은 string*전체*  가 regex pattern에 맞아 떨어지는지 확인하도록 되어있기 때문이다.일부분이 아니다. 이점에서, 자바는 다른 프로그래밍 언어들과 다르다.

또 다른 예가 있다. `helloRegex` 패턴은 쉼표와 빈칸으로 나뉜 두 단어로 이루어져 있다:

```java
String helloRegex = "Hello, World";

"Hello, World".matches(helloRegex); // true 정확히 일치
"Hello, world".matches(helloRegex); // false w의 대소문자가 다름
"Hello,World".matches(helloRegex); // false 빈칸이 없음
```

예시에서 명백히 확인 가능하듯, 우리의 regex가 그냥 문자열이라면, 정확히 같은 문자열에만 match 된다. 이럴거면 regex를 쓸 필요가 없다.

우린 이미 알고있는데, regex의 진정한 힘은 동시에 여러 문자열과 매치할 수 있는 패턴을 만드는 특별한 문자에게서 나온다. 우리는 이미 친숙한 두개의 특별한 문자를 알아볼 것이다.

## The dot character and the question mark  점과물음표

점 `.` 은 알파벳,숫자,빈칸 등 모든 하나의 문자에 매치된다. 이것이 매치할 수 없는 유일한 문자는 개행문자`\n`이다. 아래의 예시는 친숙할 것이다:

```java
String learnRegex = "Learn.Regex";

"Learn Regex".matches(learnRegex); // true, .과 빈칸이 매치된다
"Learn.Regex".matches(learnRegex); // true, .과 .이 매치된다
"LearnRegex".matches(learnRegex); // false, .과 R이 매치되고 R과 e가 매치에 실패한다
"Learn, Regex".matches(learnRegex); // false, .과 빈칸이 매치되고 R과 빈칸이 매치에 실패한다
"Learn\nRegex".matches(learnRegex); // false, .과 \n이 매치에 실패한다
```

알고 있듯이, 물음표 `?` 는 앞에 문자이거나 '없는문자'이다. 영국영어와 미국영어의 사소한 스펠링 차이가 이 문자의 사용의 오래된 예시이다:

```java
String pattern = "behaviou?r";

"behaviour".matches(pattern); // true, u?가 u와 매치된다
"behavior".matches(pattern);  // true, u?가 '없는'문자와 매치된다
```

점 `.` 과 물음표 `?` 를 하나의 regexpattern에서 써보자. 이 조합은 "아무문자 하나 또는 없는문자"이다:

```java
String pattern = "..?";

"I".matches(pattern);  // true, .과 I가 매치되고 .?와 없는문자가 매치된다
"am".matches(pattern); // true, .과 a가 매치되고 .?와 m가 매치된다
"".matches(pattern);   // false, .과 없는문자가 매치에 실패한다
```

가끔 점과 물음표를 쓰고 싶을 때가 있다(특별한 문자로서가 아니라,순수한 .과 ?의 매치).어떻게 해야하나 알아보자.

## The tricky escape character 요망한 탈출문자

이제 `.` 와 `?`를 평범한 문자로 어떻게 regexp에서 사용할 수 있는지 궁금할 것이다.

이 경우, 우리는 앞에 역슬래시를 붙여서 문자를 보호해줘야 한다 `\` . 역슬래시는 탈출문자라고 불리는데 이것이 문자를 특별한 역할에서 "탈출" 시켜주기 때문이다. 역슬래시 `\` 자체를 평범한 문자로 쓰기위해선, 이것 역시 탈출시켜줘야한다! 이 경우, 역슬래시 두개 `\\`  가 regexp에서 하나의 평범한 역슬래시 문자를 뜻한다.

근데 복잡해진다. 역슬래시 `\` 는 regex뿐 아니라 `String` 에서도 탈출문자이다. 따라서 우리는 진짜진짜 역슬래시를 쓰려면 역슬래시를 하나더 써줘야한다 .다음과 같이:

```java
String endRegex = "The End\\."; // \에 의해 \.문자열이 regexp에 들어간다. \.는 문자열에서 탈출문자+.이므로 순수한 .이다.

"The End.".matches(endRegex); // true, .과 .이 매치된다
"The End?".matches(endRegex); // false, .은 순수한 .이므로 ?과 매치되지 않는다
```

예를들어, .으로 끝나는 5글자단어는 다음의 패턴으로 표현한다:

```java
String pattern = ".....\\.";

"a1b2c.".matches(pattern); // true
"Wrong.".matches(pattern); // true
"Hello!".matches(pattern); // false
```

늘 그렇듯 탈출은 역슬래시와 함께 할때 무척 헷갈려진다.



하나의 `\` 를 자바 string에서 매치하려면 우리는 `\\\\` 를 regex에서 써야한다. 역슬래시 두개 `\\` 로 string에서 역슬래시 하나 `\` 를 만들 수 있고, string에서 그 역슬래시앞에 역슬래시를 붙여 순수한 역슬래시를 만들어줘야 하므로 2*2=4개가 필요하다.

## Boundary Matchers 경계 확인

새로운걸 배워보자! regex는 문자열이 특정 문자열로 시작하거나 끝나는지 확인할 방법을 제공한다. 특별한 문자 `^` 가 "*시작"*을 뜻한다. 아래 예시가 input이  `sa` 에 임의의 문자 하나 더 로 시작하나 확인한다:

```java
String regex = "^sa.";
"sam".matches(regex); // true
"Sam".matches(regex); // false
"jam".matches(regex); // false
```

또 다른 특별한 문자 `$` 는  "반대" 목적으로 이용된다. 이건 끝날 때를 다룬다. 예를 들면 다음 패턴은 !로끝나는 3개의 문자인지 확인한다:

```java
String regex = "..!$";
"ok!".matches(regex); // true
"no!".matches(regex); // true
"ok?".matches(regex); // false
```

이번 레슨 정리!

## Conclusion 결론

regex는 강력하다. 특별문자와 문자들로 패턴을 만들고 확인하게 해준다. 키포인트는 :

- `matches` string 메소드는 문자열과 regex를 비교하는데 사용된다;
- 점 `.` 은 `\n` 를 제외한 모든 문자를 커버한다;
- 물음표 `?` 는 "이전 문자는 없어도됨" 을 뜻한다;
- 모자 `^` 는 시작을 다룬다;
- 달러 `$`는 끝을 다룬다;
- 특별한 문자들은 순수한 문자로 쓰려면 역슬래시`\`를 써야한다.

다음 레슨에서 우리는 다른 기능들을 살펴본다: 특별한 문자들도 더 있고, 여러 함수들도 대기중이고, 문자열을 다루는데 좋은 클래스들도 있다.