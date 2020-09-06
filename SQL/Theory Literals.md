## Theory: Literals

 리터럴(상수)

## Introduction

거의 모든 프로그램이나 스크립트에서 **literals**이라 불리는 상수를 다뤄야 한다. 예를 들어, 인구 데이터를 분석중이고 특정 기준에 따라 어떤 row들을 출력해야 한다면 (1920년과 2000년도 사이에 태어난 Jessie들) 리터럴을 사용해야한다 (Jessie, 1920, and 2000).

이 토픽에서 3개의 리터럴 타입을 다룬다 : numeric, string, and boolean. 배운 것을 바로 활용하기 위해 Hello world프로그램도 작성할 것이다.

## String literals

SQL에서 string 상수는 연속된 문자들이 `'`나`"`로 쌓여있는 모양이다. 예를 들면 ,  'Hyperskill', 'Hello world!', and "SQL (Structured Query Language)". `'`를 문자열에 넣으려면 `""`사이에 넣고 그 반대경우도 그렇게 하면 된다. 예를 들면 "Sophie's choice"



## Numeric literals

숫자 리터럴은 integer,decimal,real이다. 부호를 표시하지 않는다면 양수로 처리된다.

숫자 리터럴은 INTEGER, REAL, and DECIMAL이 될 수 있다; 그리고 데이터 관리 시스템은 문맥에 맞춰서 타입을 추론해준다. 예를 들어 소수점을 표기하지않았고 INTEGER이 범위안에 들어가는 수라면 그것은 INTEGER라고 추론될 것이다. 아니면 DECIAML로 추론할 것이다. e를 사용하는 형식으로 표기된 숫자는 REAL 로 추론할 것이다.

 **CAST(\*value\* AS \*type\*) function**으로 리터럴의 타입을 정해줄 수있다. 

You can directly specify the type of a literal using the **CAST(\*value\* AS \*type\*) function**. 예를 들어, CAST(1 AS DECIMAL(20,3)) 은 1을 DECIMAL(20,3)으로 다룰 것이다. 물론 가능한 경우에만 캐스팅이 된다 : 예를 들면, CAST(123.4 AS INTEGER) 는 에러이다.

## Boolean literals

Boolean literals은 논리값 boolean이다:TRUE (true) and FALSE (false). 어떤식으로 선언하든 (TRUE or true),  둘다 같은 true이다. (false도 물론 같다).

## Hello, World

이제 전통적인  "Hello, World!" program을 만들어보자. SQL코드 : 

```sql
SELECT 'Hello, World!';
```

이 쿼리의 결과는:

Hello, World!

사실 쿼리는 우리가 string을 결과로 원한다는걸 선언하고 있다. 이 선언은 3가지 부분으로 나누어진다 : **keyword SELECT** (case insensitive),우리가 받고싶은 literal, 그리고 쿼리가 끝난다는걸 말해주는 세미콜론.

정리하자면 , 어떤 리터럴을 뽑아내는 간단한 SQL쿼리는 다음형식이다:

```sql
SELECT literal;
```

# How to use SQL

위의 프로그램 SQL쿼리를 어디다가 적어야 하는가?

이걸 아무도 안알려주었다..

이클립스에다 적진 않을거아냐..

우선 SQL을 다운받아야 한다.

SQL은 프로그램이다. 데이터베이스를 다루는 Data base management program.

자바 파이썬 이클립스 등을 다운받듯이 인터넷에서 열심히 다운을 받는다.[링크](https://dog-developers.tistory.com/20)

그리고 commandline client에다가 쿼리를 써주면 된다.

