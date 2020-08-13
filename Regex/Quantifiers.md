## Theory: Quantifiers 정량자

정량자는 개수를 표현함

특별한 문자에도 붙일 수 있음

짱 중요

## The list of quantifiers 정량자 목록

꼭 기억해야할 정량자들:

- `+` 한개 이상의 앞문자;
- `*` 0개 이상의 앞문자;
- `{n}` 정확히 n개의 앞문자;
- `{n,m}` n이상 m이하의 앞문자;
- `{n,}` n이상의 앞문자;
- `{0,m}` m이하의 앞문자.

**중요,**또다른 정량자 `?` 앞에거 있거나없거나.  `{0,1}`.이미 알고있음.

## The plus quantifier 플러스 정량자

1개이상에 매칭되는 `+`:

```java
String regex = "ca+b";

"cab".matches(regex); // true
"caaaaab".matches(regex); // true
"cb".matches(regex); // false, because it does not have at least one repetition of 'a'
```

보면알듯 1개이상에 매치됨.

## The star quantifier 별 정량자

0개 이상에 매칭되는 `*`:

```java
String regex = "A[0-3]*";

"A".matches(regex);  // true, because the pattern matches zero or more repetitions
"A0".matches(regex); // true
"A000111222333".matches(regex); // true
```

없어도 된다는 점이 `+`와 다르다.

다음 패턴은 John앞뒤로 몇개가 있어도 되는 패턴임.

```java
String johnRegex = ".*John.*"; // it matches all strings containing the substring "John"

String textWithJohn = "My friend John is a computer programmer";

textWithJohn.matches(johnRegex); // true

String john = "John";

john.matches(johnRegex); // true

String textWithoutJohn = "My friend is a computer programmer";

textWithoutJohn.matches(johnRegex); // false
```

`*`로 문자의 일부가 매칭되는지 아주 쉽게 찾을 수 있음.

## Specifying the number of repetitions 반복되는 숫자 정해주기

앞에것들은 매우 넓은 범위를 가지고 있고 특별한 수를 지정해주진 않음. 수를 지정해주는 정량자는 **중괄호**와 함께 쓰임: `{n}`, `{n,m}`, `{n,}`.



중요: 중괄호 안에 공백 쓰지 말것. 숫자 한두개, 필요하면, 쉼표만 사용가능.중괄호에 공백을 넣는것은 정량자를 "비활성" 시키고, 완전히 다른 표현으로 만듬. 예를 들면, `"a{1, 2}"` 는 정확히 문자 `"a{1, 2}"`에 매칭되고,  `"a"` 나 `"aa"`와는 안됨.



다음 예시를 보시라:

```java
String regex = "[0-9]{4}"; // four digits

"6342".matches(regex);  // true
"9034".matches(regex);  // true

"182".matches(regex);   // false
"54312".matches(regex); // false
```

`n` 이상 `m` 이하는`{n,m}` 정량자로 구현가능.시작과 끝이 모두 **포함**됨을 명심: `m` 개도 허용됨! 이건 regex 공식이고, 자바의 range는 끝을 포함안하므로 좀 헷갈릴수도 있음.

```java
String regex = "1{2,3}";

"1".matches(regex);    // false
"11".matches(regex);   // true
"111".matches(regex);  // true
"1111".matches(regex); // false
```

마지막은 최소 `n`개를 `{n,}` 정량자로 구현함.

```java
String regex = "ab{4,}";

"abb".matches(regex); // false, not enough 'b'
"abbbb".matches(regex); // true
"abbbbbbb".matches(regex); // true
```

 `m` 이하도 비슷하게 됨.

## Conclusions 결론

중요한점:

- 정량자로 여러 길이의 문자열을 매치할 수 있다.
- 별은 0개이상.
- 플러스는 1개이상.
- 중괄호는 더 정확한 범위 지정 가능.