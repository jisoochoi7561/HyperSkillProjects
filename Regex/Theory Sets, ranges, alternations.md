## Theory: Sets, ranges, alternations 집합,범위,대안

`.`은 일반적인 패턴을 만들 수 있게 해줬다. 하지만 점은 거의 모든 문자에 매치되는데, 우리는 특정한 문자를 원할 때가 있다. 이럴 때 Set(집합)이 도움이 된다.

## The set of characters 문자집합

각 집합은 문자 하나에 대응 되는데, 어떤 문자인지는 집합의 내용이 결정한다. 집합은 대괄호 `[]`로 표현된다. 예를들면 `"[abc]"` 는"a","b","c"와 매칭가능하다. 아래 예시를 보자:

```java
String pattern = "ab[abc]"; //  "aba", "abb", "abc", 가능, "abd"는 불가능

"abc".matches(pattern); // true
"abd".matches(pattern); // false
```

즉 집합의 어떤 문자나 매칭가능하다.

다음 예시는 집합이 두개가 있다:

```java
String pattern = "[ab]x[12]"; // a 또는 b + x + 1 또는 2
```

이 패턴은 다음 문자열들과 매칭된다:

```java
"ax1", "ax2", "bx1", "bx2"
```

다음 문자열들과는 매칭되지 않는다:

```java
"xa1", "aax1", "bx"
```

regex에서 집합의 위치는 중요하다. 하지만 집합내에서 문자들의 순서는 중요하지 않다.

## The range of characters 문자의 범위

가끔 집합을 커다랗게 만들고 싶을 수 있다. 이럴 경우 모든 문자를 적어줄 필요는 없다. [ASCII/Unicode encoding](https://hyperskill.org/learn/step/7899) 표에서 연속된다면, 우리는 범위를 사용해 집합을 만들 수 있다.범위는 `-`대시 문자로 만들어진다. 대시문자 앞의 문자는 시작점을 뒤의 문자는 끝점을 뜻한다.예를 들면 우리는 모든 숫자에 매칭되는 집합을 아래처럼 작성할 수 있다:

```java
String anyDigitPattern = "[0-9]"; //  0 부터 9 까지 매칭가능
```

알파벳들에도 가능하다 `"[a-z]"`나 `"[A-Z]"`처럼.  이 범위는 모든 대소문자 라틴문자에 각각 매치된다. 발음기호들은 이 범위에 포함되지 않는단걸 주의해라. 또 대소문자 구별이 존재한다는걸 되새겨라. 저 두 범위는 다르다!

경우를 구별하기 싫다면 다음과 같이 써라:

```java
String anyLetterPattern = "[a-zA-Z]"; // matches any letter "a", "b", ..., "A", "B", ...
```

보는 바와 같이 한 집합에 여러범위를 넣을 수 있다. 어떤 순서로든 넣을 수 있고, 사이사이 개개의 문자를 넣을 수 있다:

```java
String anyLetterPattern = "[a-z!?.A-Z]"; // 모든 라틴문자에다가 "!", "?", "." 까지
```

대시문자 그 자체를 넣고 싶다면 맨 처음이나 마지막에 넣어라: `"[-a-z]"` 이건 소문자와 대시문자이고,  `"[A-Z-]"` 이건 대문자와 대시문자이다.

## Excepting characters 문자 제외

어떤 특정한 문자만 아니면 되는 상황이 있다. 그러면 우리는 어떤문자만 *제외하고*  매칭되는 집합을 만들 수 있다.그럴려면 모자 `^` 를 집합의 맨처음에 넣어라.

```java
String regex = "[^abc]"; //  "a", "b", "c" 빼고 매칭됨

"a".matches(regex); // false
"b".matches(regex); // false
"c".matches(regex); // false
"d".matches(regex); // true
```

범위 역시 같은 방식으로:

```java
String regex = "[^1-6]";

"1".matches(regex); // false
"2".matches(regex); // false
"0".matches(regex); // true
"9".matches(regex); // true
```

모자문자 그 자체를 쓰려면, 처음이 아닌 곳에 넣어라.  `"[^a-z^]"` 는 소문자와 모자문자를 제외하고 매칭되는 집합이다.

## 대안

수직 기호`|` 는 `또는`을 뜻한다. 여러문자들이 있을 땐 괄호와 함께 쓰면 된다. 괄호는 경계를 지정한다: 괄호사이의 것들은 모두 대안블럭에 포함된다.

```java
String pattern = "(abc|def|xyz)"; //  "abc", "def", "xyz"와 매치, "a" 또는 "b"는 매치안됨

"abc".matches(pattern); // true

String pattern = "ab(c|d)"; //  "abc" 또는 "abd"
```

보통, 대안은 집합과 비슷한 용도로 사용된다: 여러 패턴이 가능할 때. 하지만 집합은 하나의 문자에 관한 것이고 대안은 문자열을 다룰 수 있다.

## Conclusions 정리

요약:

- 대괄호는 집합에 사용됨
- 집합안에 대시기호와 함께 범위사용 가능
- 문자 "빼는" 집합 가능. 맨 앞에 모자문자가 있는 집합.
- 수직바와 괄호 결합으로 여러문자 대안 사용 가능