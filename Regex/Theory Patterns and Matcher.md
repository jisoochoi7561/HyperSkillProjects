## Theory: Patterns and Matcher

자바 라이브러리는 두개의 강력한 특별한 클래스를 regex를 위해 제공한다`java.util.regex.Pattern` 와 `java.util.regex.Matcher`.  `Matcher` 객체는 regex를 다루는 유용한 함수들을 제공하고, `Pattern` 객체는 regex자체를 표현한다.

## Matching a regex

string 변수를 가지고 있다고 하자:

```java
String text = "We use Java to write modern applications";
```

이 문자열이 **"Java"** 나 **"java"**를 포함하는지 알고 싶다고 하자. `Pattern`과  `Matcher` 로 간단히 해결할 수 있다.

\1. `Pattern` 인스턴스를 `compile` 함수에 regex문자열을 건내주어 만든다:

```java
Pattern pattern = Pattern.compile(".*[Jj]ava.*"); // regex to match "java" or "Java" in a text
```

\2.  `Matcher` 를 `Pattern`의  `matcher` 함수를 통해 만든다:

```java
Matcher matcher = pattern.matcher(text); // it will match the passed text
```

\3. matcher의 `matches` 함수를 사용한다:

```java
boolean matches = matcher.matches(); // true
```

 `Matcher`의 `matches`  는   `String`의`matches`와 정확히 같은 방식으로 동작한다r.

## Advantages of Pattern and Matcher classes Pattern과 Matcher의 장점

위의 예시로는 왜 `Pattern`과`Matcher`를 쓰는지 이해가 안갈 수 있다. 하지만 다음 두 점을 주목하라:

- **퍼포먼스** 사실`String`의 `matches`  내부적으로 `Matcher`의 `matches` 를 사용하는데,  `Pattern.compile(...)` 또한 실행할 때마다 사용한다. 이것은 비효율적이다. 패턴을 여러번쓴다면 `compile`은 한번만 하는게 효율적이다.

- **풍부한 API.**  `Matcher`는 `matches`말고도 여러함수를 가지고 있다 : 문자열을 다루는데 좋고, `Pattern` 은 더 세심한 설정을 하게 해주는데, 예를들면, 대소문자를 무시하게할 수 있다.

따라서 패턴을 여러번 쓰거나 더 정확한 매칭을 원한다면, `Pattern` 과 `Matcher` 가 유리하다.

## Patterns and Modes 

방금 봤듯이 `Pattern` 은 `Matcher`객체를 만드는데 쓰인다. 하지만 패턴을 다시 쓸 생각이 없다면  `Pattern`의 `matches`  함수를 한줄로 실행할 수 있다.

```java
Pattern.matches(".*[Jj]ava.*", "We use Java to write modern applications"); // true
```

  `String`의 `matches` 와 비슷하다. 다만 여전히 패턴재활용이 불가능하다.

이전의 예시를 다시 다루자. 기본적으로 그렇듯 대소문자를 구별하므로**"JAVA"** 와 매칭되지 않는다.다행히 `Pattern.CASE_INSENSITIVE` 모드가  `Pattern` 의 컴파일에 사용될 수 있다.  이것은 대소문자를 무시하게 해준다.

```java
Pattern pattern = Pattern.compile(".*java.*", Pattern.CASE_INSENSITIVE);

String text = "We use Java to write modern applications";

Matcher matcher = pattern.matcher(text);

System.out.println(matcher.matches()); // true
```

 `Pattern.DOTALL` 도 유용하다. `.` 를 개행문자 `\n` 까지 포함하게 해준다.



 `Matcher`없이도 대소문자구별없음 모드를 쓸 수 있다.  `(?i)` 를 regex 시작에 넣으면 된다. DOTALL모드를 쓰려면 `(?s)`를 넣으라. `(?is)`로 동시에 두모드를 쓸 수 있다.



예시:

```java
Pattern.matches("(?is).*java.*", "\n\nJAVA\n\n"); // true
```

다른 모드가 더 있지만, 여기선 다루지 않는다.  [documentation](https://docs.oracle.com/javase/10/docs/api/java/util/regex/Pattern.html) 에서 상세히 볼 수 있다.

## The matches and find methods

 `Matcher` 인스턴스는 재밌는 메소드들을 제공해준다. 우리는 이중 일부를 다룰 것이다.

`String`의 `matches` 처럼, `Matcher`의 `matches`는 패턴과 문자열 전체가 맞을 때만 `true`를 리턴한다. 하지만 그렇게 편한 방식은 아니잖아? 예를 들면,  일부분이 맞는지 확인하려면 `.*` 을 시작과 끝에 붙여줘야 한다.

 `Matcher` 덕분에, 우리는 `find` 메소드를 쓸 수 있다. 그건 `matches` 와 비슷하지만, 패턴에 맞는 일부분과 매치한다. 차이점을 아래 예시를 통해 확인하라:

```java
String text = "Regex is a powerful tool for programmers";

Pattern pattern = Pattern.compile("tool");
Matcher matcher = pattern.matcher(text);

System.out.println(matcher.matches()); // false, the whole string does not match the pattern
System.out.println(matcher.find()); // true, there is a substring that matches the pattern
```

경계문자를 기억하는가?  `find` 메소드와 함께 쓰면 `matches` 와 비슷하게 쓸 수 있다.  `find` 가 시작점부터 시작하는 문자열일부를 찾게 하기위해, 모자문자 `^` 를 regex의 시작부분에 넣어주자. 끝부분의 문자열일부를 찾게 하기위해,달러문자 `$` 를 끝에 넣어주자. 이것들을 결합해 `find`를 `matches`대용으로 쓸 수 있다.:

```java
Pattern pattern = Pattern.compile("^tool$");
Matcher matcher = pattern.matcher(text);

System.out.println(matcher.matches()); // false
System.out.println(matcher.find());   // false
```

기본적으로  `matches` 와 `find` 는 전체문자를 체크한다. 하지만 `range` 메소드를 통해 체크할 문자열부분의 첫부분과 끝부분을 명시해줄 수 있다.

`range`메소드는 아마도 `String`의 `subString`메소드를 말하는 듯.

## Conclusion

regex를 다루는 두가지 방법이 있습니다: `String` 을 쓰거나 `Pattern` 과 `Matcher` 를 쓰세요. 두번째 방법이 더 효율적이고, 유용하고, 정확합니다. 두가지 중요한 메소드가 있는데, `matches` 와 `find`이고, 중요한 차이점이 있습니다.  `matches` 는 전체문자를 매치하고 `find` 는 문자열의 일부를 매치합니다.