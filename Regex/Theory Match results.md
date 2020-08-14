## Theory: Match results

`Matcher`객체로 할수 있는 것은 문자와 regex가 매치하는지 확인하는 것 이상이다: 이것은 가끔은 중요한 추가적인 정보를 준다.

## Getting match results

알다시피  `Matcher`의 `find` 는 일부분과 regex가 매치하는지 확인한다. 여기 예시코드가 있다.

```java
String javaText = "Java supports regular expressions. LET'S USE JAVA!!!";

Pattern javaPattern = Pattern.compile("java", Pattern.CASE_INSENSITIVE);
Matcher matcher = javaPattern.matcher(javaText);

System.out.println(matcher.find()); // prints "true"
```

 `find()` 메소드가 `true` (match 성공)를 반환할 때 매치된 부분에 대한 정보를 얻을 수 있다. `start()` 와 `end()`는 각각 매치된 부분의 시작과 끝 인덱스를 반환하고, `group()` 은 매치된 문자부분 자체를 반환한다.

```java
System.out.println(matcher.start()); // 0, 첫 인덱스
System.out.println(matcher.end());   // 4(자바에선 마지막인덱스는 포함안한다)즉 3다음
System.out.println(matcher.group()); // "Java", 문자열 자체
```

특별한 클래스`MatchResult` 는 매치에 관한 모든 정보를 가진다:

```java
MatchResult result = matcher.toMatchResult(); // a special object containing match results
        
System.out.println(result.start()); // 0
System.out.println(result.end());   // 4
System.out.println(result.group()); // "Java"
```

주의점은 `start`, `end`, `group` 을`find()` 실행 전에 쓰거나 매치가 안됬는데 쓸경우 `IllegalStateException`가 발생할 수 있다. 이걸 막으려면 이것들을 쓰기전 `find()` 가 true인지 확인하라.

```java
if (matcher.find()) {
    System.out.println(matcher.start());
    System.out.println(matcher.end());
    System.out.println(matcher.group());
} else {
    System.out.println("No matches found");
}
```

이 코드는 `find`값이 `false`면 **"No matches found"**를 출력한다. 또 이 코드는`start()`, `end()`, `group()` 가`find()`이후에 실행됨을 보장한다.

## Iterating over multiple matches 매치가 여러개일 때

가끔 한 부분 이상이 매치될 수 있다. 이전 예시에서 **"Java"** 와 **"JAVA"**가 모두 가능했다(왜냐면 대소문자 구별안함 모드였기 때문에).  `find()` 메소드는 루프를 통해 매치되는 문자부분들을 순환하게 해준다.

```java
String javaText = "Java supports regular expressions. LET'S USE JAVA!!!";

Pattern javaPattern = Pattern.compile("java", Pattern.CASE_INSENSITIVE);
Matcher matcher = javaPattern.matcher(javaText);

while (matcher.find()) {
    System.out.println("group: " + matcher.group() + ", start: " + matcher.start());
}
```

결과:

```java
group: Java, start: 0
group: JAVA, start: 45
```

루프 조건덕분에`start()` 와 `group() `은 `find()` 가 `true`일 때만 실행됨이 보장된다.
즉 처음 find는 첫번째 매치를 하고, 두번째 find는 그 다음 문자열을 검사하고.. 이런식으로 되는 것 같다.
정확히는 find가 아니라 matcher의 작동방식이 "다음부분"으로 넘어가는 식으로 되는 것 같다.
matcher.group역시 find처럼 다음부분에대해 작동하기 때문이다.

## 결론

보면 알듯, `Matcher` 매치에 관한 모든정보를 제공해준다.어디서 시작하고 어디서 끝나는지 어떤 문자열인지. 이 데이터들은 `MatchResult` 객체에 저장할 수 있다.
