## Theory: Basic SELECT statement

## Introduction

이미 SQL쿼리를 써보았다:

```sql
SELECT 'Hello, World!';
```

랑

```sql
SELECT 2.3*4+5;
```

이 두 쿼리는 **SELECT statements**의 예시이다. 이 토픽에서 SQL의 첫 블록을 알아보러 가겠다. select.

## Tuple

SELECT 문장에서 하나이상의 값을 콤마로 선택할 수 있다. 예를 들면 아래의 쿼리는 string,숫자,계산식을 select한다.

```sql
SELECT 'Alice', 170, 170*0.393701;
```

이 값들(or attributes, fields)의 묶음을 **tuple**(record, row) 이라고 한다. Hello World를 select한 쿼리의 결과는 하나의 속성을 가진 row라고 할 수 있다.

## Alias

쿼리에서 튜플의 각 속성(attributes)에 이름(**alias.별명**)을 정해줄 수 있다. 방법은 , **AS** 키워드를 쓰면 된다. 만약 alias가 여러개의 단어로 구성되있거나 SQL keyword와 같은 단어라면 , `"`로 감싸줘야 한다. 아래의 쿼리를 보라:

```sql
SELECT 
  'Alice' AS name, 
  170 AS height_in_centimeters, 
  170*0.393701 AS "height in inches"
;
```

쿼리의 결과는 3개의 속성을 가진 Tuple이다:“name”, “height_in_centimeters”, and “height in inches”. 사람이 읽을 수 있는 alias를 쓰는 게 좋다; 아니면 DBMS가 자동으로 만들어 줄 수도 있다.

기억날지모르겠는데, SQL은 표에 있는 데이터를 다루려고 디자인되었따. 사실, 지금까지 쿼리들의 결과는 모두 table이다. 하나의 row(결과가 하나니까)와 이름이 있는 여러개의 column(attribute는 방금예시에서는 3개였다)을 가진.

## Code readability

SQL은 대소문자를 구별하지 않는다. 따라서 SELECT, select, SeLeCt, and seLEct모두 가능하다. 하지만 가독성과 강조를 위해 키워드는 보통 대문자로만 쓴다.

우리는 또 들여쓰기(indent)를 권장한다. 물론 코드가이드는 여러가지가 있다. 당신의 소망과 팀의 요구에 맞춰서 사용하라.

여러 방법으로 같은 쿼리를 작성하였다. 어느 방법이 좋으신지?

```sql
SELECT 'Bob' AS name, 160 AS "height in centimeters", 160*0.393701 AS "height in inches";

SELECT 
  'Bob' AS "name", 
  160 AS "height in centimeters", 
  160*0.393701 AS "height in inches"
;

SELECT 
  'Bob'        AS "name", 
  160          AS "height in centimeters", 
  160*0.393701 AS "height in inches"
;
```

3번째 버전이 가장 좋다: 몇개의 속성이 있는지, 속성의 이름은 뭐고, 값은 뭔지 쉽게 볼 수 있다. 중간에 있는 버전은 덜 정돈되었지만 작성하기에는 더 좋다(3번째 버전에서는 손수 줄을 맞춰줬다.)물론 현실에서는 당신의 소망과 팀의 요구에 맞춰서 사용하라.

## Conclusion

여기 기본적인 SELECT 문장 구조가 있다 : 키워드, 원하는 것들(과 alias),그리고 세미콜론:

```sql
SELECT val1 [AS name1], ..., valN [AS nameN];
```

추카추카!많이 배웠다!