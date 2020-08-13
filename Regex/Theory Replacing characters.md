## Theory: Replacing characters

가끔 문자열의 일부를 바꾸고 싶을 때가 있다. 자바는 유용한 메소드들을 제공하고, regex는 이 과정에서 중요한 역할을 한다.

## The methods replaceFirst and replaceAll of a string

문자열 교체에 쓰이는 두가지 string method:

- `String **replaceFirst**(String regex, String replacement)`첫 `regex` 를 `replacement`로 바꾼다;
- `String **replaceAll**(String regex, String replacement)`모든 `regex` 를 `replacement`로 바꾼다;

참고

- `regex`는 교체할부분과 일치하는 정규표현식이다;
- `replacement` 교체할 부분을 대신할 문자열이다(이것은 정규식이 아니라 그냥 문자열이다!).

string은 수정할 수 없는 객체이므로 이것들은 새로운 string을 리턴한다.

주의할점은 `replace` 는 비슷한 함수이지만 regex를**지원하지 않는다** .

아래 예시를 보자.

```java
String digitRegex = "\\d"; // 숫자를 가리키는 regex

String str = "ab73c80abc9"; // 영숫자로 이루어진 string

String result1 = str.replaceAll(digitRegex, "#"); // 숫자들을 #으로 바꾼다

System.out.println(result1); // "ab##c##abc#"

String result2 = str.replaceFirst(digitRegex, "#"); // 첫숫자를 #으로

System.out.println(result2); // "ab#3c80abc9"
```

regex를 첫 인자로 주면 된다. 다음 예시는 대문자 알파벳을 -기호로 바꾼다.

```java
String regex = "[A-Z]+";

String str = "aBoeQNmDFEFu";

String result = str.replaceAll(regex, "-"); // "a-oe-m-u"
```

Matcher 클래스 또한 같은 역할을 할 수 있다.살펴보자.

## The methods replaceFirst and replaceAll of a matcher

 `Matcher` 객체 또한 regex로 문자열을 수정하는 메소드를 가진다:

- `Matcher **replaceFirst**(String replacement)`;
- `Matcher **replaceAll**(String replacement)`.

string의 메소드와의 차이는 `Matcher`의 메소드는 regex를 인자로 가지지 않는다:  `Matcher`객체는 생성될 때 regex를 받고 시작했으니까. 아래예시를보자.

```java
Pattern pattern = Pattern.compile("\\d"); // 숫자 regex

String str = "ab73c80abc9"; // 바꿀 string

Matcher matcher = pattern.matcher(str); regex로 컴파일한 pattern으로 만든 matcher

System.out.println(matcher.replaceAll("#"));   // ab##c##abc#
System.out.println(matcher.replaceFirst("#")); // ab#3c80abc9
```

수정은 무척 간단하니, regex나 열심히 작성하시라.

## Conclusions

배운것:

- `replaceFirst` 와 `replaceAll` 메소드로 문자열을 바꿀 수있다.`String` 과 `Matcher` 가 이 두 메소드를 각각 갖고있다;
- string 메소드는 두 인자를 받는다: regex와 대신넣을 문자;
- `Matcher` 메소드는 대신넣을 문자만을 요구한다.