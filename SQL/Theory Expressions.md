## Theory: Expressions

이미 SQL과 리터럴 데이터타입(이 토픽은 생략하였다. 자바의 반대로 id INTEGER,is_parent BOOLEAN 처럼 쓰면 된다. )에 대해 조금 공부했다. 이제 더 깊이 공부해서 논리 연산 문자열을 다룰 것이다. 그리고 간단한 계산기를 만드는 법을 배울 것이다.

## Arithmetic

학교를 다니셨다면 SQL에서 쓰이는 계산표현에 익숙할 것이다 : 2+22+2, 2+2/22+2/2, and (2-2)*2(2−2)∗2.

숫자 계산에 쓰이는 SQL의 기본적 연산자들 : 

- **-** (unary minus that changes the sign of value)
- ***** (multiplication), **/** (division), **%** (modulo that returns remainder of integer division),
- **+** (addition), **-** (subtraction)

SQL 은 연산에 우선순위를 적용한다. 위의 순서가 우선순위이다. 즉 곱하기는 더하기보다 먼저 처리된다는 것이다.이것은 또한 괄호(가장 높은 우선순위)를 지원한다.

괄호가 없어도 되더라도 가독성을 위해 쓸 수도 있다.비교해보라: -2+2*2-2/2 and (-2)+(2*2)-(2/2). 

## String expressions

SQL은 텍스트를 다루는 다양한 함수를 제공한다:

- Function `concat(s_l, s_r)` 은 *s_l* 에 *s_r* 을 붙인걸 결과로 내논다.예를 들어, `concat('Hyper', 'skill')`은 'Hyperskill'.
- Function `char_length(s)`은 몇글자인지를 센다. 예를들어, `char_length('apple')` 은 5.
- 문자를 거꾸로 하려면  – `reverse(s)`. 예를들어, `reverse('Madam, I''m Adam')` 은 'madA m''I ,madaM'.
- Function `replace(s_in, s_what, s_with)` 은  *s_in*을 리턴하는데 그 안의 모든 *s_what* 이 *s_with*으로 바뀌어 있다. 예를 들어, `replace('abracadabra', 'bra', '')` 은 'acada'.
- 문자열의 일부분을 원한다면 `substr(s, i_from, n_char)`i(1부터 시작)에서부터 n개. For example, `substr('Hello, data!', 8, 4)` 은 'data'.

SQL dialects(다른 버전)은 string을 다루는 부분에서 차이가 많이 날 수 있다. 우리는 PostgreSQL의 문법을 알려주었다; 각자의 sql에 맞는 doc을 참고하길 바란다.

## Logic expressions

논리 값을 위해 SQL은 불린대수의 연산자를 지원한다 : **NOT**, **AND**, and **OR** (우선순위순 정렬).

## Comparisons

또 중요한 연산자들 – **comparisons**. SQL은 비교용 연산자들을 제공한다:

- **=** (equality check)
- **<** (less), **>** (greater)
- **<=** (less or equal), **>=** (greater or equal)
- **<>** (not equal).

숫자나 string을 비교할 수 있다(영어라면 알파벳 순서로 비교); 예를 들어 2 < 3,char_length('') = 0 은 TRUE,  'ab' = 'ba', 'a' >= 'aa' 은 FALSE.

비교연산의 결과는 논리값이다. 이것은 논리연산에 바로 쓰일 수 있다. 예를 들어 (char_length('hyperskill') = 5 + 5) AND (concat('hyper','skill') = 'hyperskill').

## Calculator

이 토픽을 읽었다면 계산기 대신에 SQL을 쓸 수 있다. 무척 좋다.

SQL에서 리터럴 뿐 아니라 표현식도 select할 수 있다. 형식은 :

```sql
SELECT expression;
```

이 문장은 3부분으로 이루어졌다 : SELECT키워드, 원하는 표현식, 그리고 세미콜론.

예를 들자면

```sql
SELECT (2+2)*15;
```

출력은 6060.



# Workbench

mySql을 전 토픽에서 설치하였다.

Workbench는 mySql의 GUI이다.

표를 직접 보여주므로 무척 편리하다.