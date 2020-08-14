# Regex in Kotlin

아쉽게도 hyperskill.org에서는 Kotlin의 regex에 대해 자세한 설명을 하지 않는다.

따라서 이 문서는 내 개인적인 조사와 다른 인터넷 레퍼런스 그리고 [공식문서](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/)기반으로 작성되었다.



# 삼중 따옴표

(Apple) 이나 (Media) 처럼 괄호 안의 대문자로 시작하는 단어를 정규 표현식 으로 나타내면 다음과 같습니다.

```
\([A-Z]\w+\)
```

정규 표현식 을 사용하려면 다음과 같이 입력해야 합니다.

```
val regexString : String = "\\([A-Z]\\w+\\)"
```

[출처](https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%97%90%EC%84%9C-%EC%A0%95%EA%B7%9C-%ED%91%9C%ED%98%84%EC%8B%9D-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-2c655ba35c36)

괄호또한 regex에서 사용될수 있기 때문에

>  예 : (ab)+는 ab,abab..를 뜻함

이스케이프 문자 `\`를 넣어줘 `\(`를 넣어줘야한다.

근데 스트링 자체에서 `\`가 이스케이프문자이기 때문에

`\\(`를최종적으로 넣어줘야 하는 것이다.



regex를 처음 배울 때 가장 힘든 점이 저것이었다. hyperskill의 좋은점이 저런 기본적인 것부터 설명해 준단 것이다.  이스케이프 문자를 배우긴 했지만 띄엄띄엄 읽는 수준이다보니.. 사실 지금와서도 이스케이프 문자를 regex에서 사용하는건 부담되는 것이 사실이다.

Kotlin에서는 `삼중 따옴표`를 지원하기 때문에 훨씬 알아보기 편하고, 개인적으론 regex엔 이걸 쓰는게 좋아보인다.

regex뿐만 아니라 일반 문자열에서도 사용가능하다. 



`삼중 따옴표`란 : 이 안에서는 이스케이프문자가 효력을 잃고, 우리가 실제 작성한 문자열이 있는 그대로의 특징을 얻는다.

```
String string = "안녕하세요\nJava입니다."
```

```
val string = """안녕하세요
Kotlin입니다."""
```

로 대체할 수가 있다.



위의 문자열을 regex로 만들려면

```
val normal = "\\(([A-Z])\\w+\\)".toRegex()
val special = """\([A-Z]\w+\)""".toRegex()
```

로 표현할 수있다.



위 예시에서 String을 toRegex함수를 통해 직관적으로 regex로 만드는걸 확인 할 수 있다



# Regex Class

Kotlin은 Regex클래스를 지원한다.

Java 또한 Pattern과 Matcher를 가지고 있지만, regex클래스를 가지고 있지 않다.java.util.regex.Pattern 의 regex는 클래스가 아니고,인터페이스도 아니고, 그저 패키지 이름일 뿐이다.

```
Pattern pattern = Pattern.compile(".*java.*", Pattern.CASE_INSENSITIVE);

String text = "We use Java to write modern applications";

Matcher matcher = pattern.matcher(text);

System.out.println(matcher.matches()); // true
```

자바에서의 Pattern과 Matcher 사용법을 보면, Pattern의 컴파일을 통해 문자열정규식을 패턴으로 바꾸고, 그 패턴을 기반으로 하는 Matcher를 생성한후 Matcher를 통해 매치를 진행한다.



Kotlin에서는 이 것을 간단하게 해주는 Regex클래스를 지원한다.

Pattern의 역할을 해주기 위해 option과 pattern 프로퍼티를 가지는 Regex class는

Matcher의 역할을 해주는 find,replace등의 함수또한 가지고 있다.



Regex 생성법 : 

- Regex 클래스의 생성자를 이용

```
val reg1 = Regex("\\(([A-Z])\\w+\\)")
```

- String 클래스의 확장함수 toRegex() 이용

```
val reg2 = "\\(([A-Z])\\w+\\)".toRegex()
```

- Regex 클래스의 Companion Object 함수 인 fromLiteral() 이용

```
val reg3 = Regex.fromLiteral("\\(([A-Z])\\w+\\)")
```

삼중따옴표를 사용해 더 간단히 할 수 있다.

마지막 fromLiteral은 특이하다. 저것은 입력값을 문자열 리터럴 자체로 본다. 나머지 두개와는 아예 다른 식이다. 즉 \w나 [A-Z]등이 regex식이 아니라 그냥 문자열이라고 인식된다.

```
\(([A-Z])\w+\) 자체가 regex가 된다. "\(([A-Z])\w+\)" 와 매치된단 뜻이다.
```



# Regex의 생성자

Regex의 생성자는 Pattern이 그러하듯 option을 넣어줄 수 있다. 

```
val reg1 = Regex("")
val reg2 = Regex("",RegexOption.LITERAL) 
val reg3 = Regex("", setOf(RegexOption.LITERAL,RegexOption.COMMENTS))// 옵션 집합도 가능하다.
```





# Regex의 RegexOption

RegexOption은 별도의 enum 클래스이다.

참고로 Pattern의 mode는 Pattern 클래스의 property로 int 값을 가지고 있다.enum 짝퉁느낌?

여기에선 간단하게 EnumValues만 다룬다. 이것들은 mode에서도 같은 의미를 가진다.

> IGNORE_CASE

대소문자 구별 없음



> MULTILINE

`^`과 `$`가 단어의 시작과 끝이 아니라 각 줄의 시작과 끝을의미하게 된다.



> LITERAL

위 생성자의 fromLiteral 참고. 문자열리터럴로 간주한다.



> UNIX_LINES

이 모드에서는 `\n`만이 개행문자로 간주된다. 캐리지리턴`\r\`등이 .으로 처리가능해진단 뜻.



> COMMENTS

regex안에서 빈칸이나 comments를 쓰는걸 허용합니다.



> DOT_MATCHES_ALL

`.`이 개행문자까지도 포함합니다.



> CANON_EQ

Enables equivalence by canonical decomposition.

뭔소린지 모르겠음.



# Regex의 match 와 find

Regex클래스의 함수 find 는 Matchresult?를 반환한다.

자바의 Matcher의 find는 boolean 값을 리턴했으므로 헷갈리지 않도록 한다.

#### [containsMatchIn](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/contains-match-in.html)

포함하고 있는지 확인. Java의 find와 비슷?

```
fun containsMatchIn(input: CharSequence): Boolean
```

#### [find](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/find.html)

처음 일치하는 부분의 MatchResult를 반환함,  [startIndex](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/find.html#kotlin.text.Regex$find(kotlin.CharSequence, kotlin.Int)/startIndex)명시 가능.

```
fun find(  input: CharSequence,  startIndex: Int = 0): MatchResult?
```

#### [findAll](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/find-all.html)

find와 비슷하지만 모두 찾아서 Sequence로 반환한다.

```
fun findAll(  input: CharSequence,  startIndex: Int = 0): Sequence<MatchResult>
```

#### [matchEntire](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/match-entire.html)

find가 일부라면 이건 전체다. Java에서 matches함수느낌.

```
fun matchEntire(input: CharSequence): MatchResult?
```

#### [matches](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/matches.html)

이건 진짜로  Java Matcher의 matches 빼다박았다.

```
infix fun matches(input: CharSequence): Boolean
```



# MatchResult 

자바에도 비슷한 matchresult가 있었다.

value : 찾은 결과를 문자열로 담고있다.

range : 처음과 마지막 인덱스를 IntRange로 담고 있다.

next()함수 : 결과가 여러개일 때 다음 결과를 조회할 수 있다!



## 정규 표현식 그룹

정규 표현식 ***a([bc]+)d?\*** 는 첫번째 그룹 ***a[bc]+d?\*** 와 괄호 안의 두번째 그룹 ***[bc]+\*** 로 나눌수 있습니다.

그리고 그 결과는 MatchResult 클래스의 groupValues 프로퍼티 에 List<String> 객체로 저장되어 반환됩니다.

```
val regex = """a([bc]+)d?""".toRegex()val matchResult = regex.find("abcd")val groupValues : List<String> = matchResult!!.groupValues
```

MatchResult 클래스의 groups 프로퍼티는 MatchGroupCollection 의 객체 이므로 get 함수를 통하여 MatchGroup 객체를 반환받을수 있고, 이 MatchGroup 은 value 와 range 의 두개의 프로퍼티를 가집니다.

그러므로 그룹의 각 결과의 인덱스는 다음과 같이 구할수 있습니다.

```
val groupValueIndex : IntRange =
                         matchResult!!.groups!!.get(0)!!.range
```

## 구조 분해 할당 (Destructuring)

정규 표현식 그룹을 이용해서 다음과 같이 구조 분해 할당을 사용할수 있습니다.

```
val regex = """([\w\s]+) is (\d+) years old""".toRegex()
val matchResult = regex.find("Mickey Mouse is 95 years old")
val (name, age) = matchResult!!.destructured
```

MatchResult 클래스의 destructured 프로퍼티는 Destructured 클래스를 이용하여 groupValues 에 담긴 각 그룹의 값들을 지정된 변수에구조 분해 할당 합니다.

예시에서 name 에는 Mickey Mouse 가, age 에는 95 가 할당되게 됩니다.

[출처]([https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%97%90%EC%84%9C-%EC%A0%95%EA%B7%9C-%ED%91%9C%ED%98%84%EC%8B%9D-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-2c655ba35c36](https://medium.com/@limgyumin/코틀린-에서-정규-표현식-사용하기-2c655ba35c36))

# 교체

#### [replace](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/replace.html)

전부바꿈

```
fun replace(input: CharSequence, replacement: String): String
```

함수를 넣어줄수도 있음

```
fun replace(  input: CharSequence,  transform: (MatchResult) -> CharSequence): String
```

#### [replaceFirst](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/replace-first.html)

처음걸 바꿈

```
fun replaceFirst(  input: CharSequence,  replacement: String): String
```



# 스플릿

#### [split](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/split.html)

regex.split(string)으로 쓰면 string을 스플릿가능

```
fun split(input: CharSequence, limit: Int = 0): List<String>
```

`limit` - 나올 수 있는 substring의 최대 갯수. 0이라면 제한이 없는 것임

#### 스플릿에서 문자열과 정규식

자바에서 String.split(".")을 하면 스플릿이 되지않음.

"."을 regex로 처리하기 때문임

코틀린은 직관대로 .을 .으로 처리함

regex로 처리하길원하면 ".".toRegex()나 Regex(".")을 넣어주면 됨.



# toPattern

자바 와의 호환을 위해 toPattern 메소드를 제공함
