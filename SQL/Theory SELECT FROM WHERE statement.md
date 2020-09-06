## Theory: SELECT FROM WHERE statement

서점의 데이터베이스를 관리한다고 합시다. 당신은 이미 books라는 table에서 모든책들의 정보를 얻을 수 있습니다. 하지만 그것들은 수많은 row들이 있고, 어떤 기준에 따라 그것들을 필터링해야할겁니다. 예를 들자면, 작가로. 또는 언어로. 아니면 고객이 요청하는 속성이나 표현으로. 이렇게 몇몇 row들만 골르는걸 **filtering**이라고 합니다. SQL에서 하는법을 알아봅시다.

## Example

당신의 고객이 Charles Dickens가 쓴 책중 좋은평가를 받고 영어와 프랑스어로 된걸 원한다고 합시다. 이걸 위한 쿼리를 작성해봅시다:

```sql
SELECT
    id, 
    title,
    author,
    language,
    rating,
    price,
    amount
FROM 
    books
WHERE
    author = 'Charles Dickens'
    AND (language = 'EN' OR language = 'FR')
    AND rating > 4.0
    AND amount > 0
;
```

SELECT문장의 마지막 부분에  **keyword WHERE** 를 추가했고 뒤에 필터역할을 하는 논리식을 써주었습니다. 쿼리는  `author = 'Charles Dickens' AND (language = 'EN' OR language = 'FR') AND rating > 4.0 AND amount > 0`가 true인 row들만을 리턴할겁니다.

## WHERE clause

SELECT 와 WHERE 필터링을 쓰는 기본형식입니다 : SELECT,원하는 것들, FROM,테이블이름,WHERE,필터,세미콜론:

```sql
SELECT
    expr1 [AS alias1], ..., exprN [AS aliasN]  
FROM
    table_name
WHERE
    logical_expression
;
```

SELECT 는 분명 SQL에서 가장 복잡하고 멋진 문장일겁니다.**clauses**(절) 라고 불리는 옵션들 -SELECT, FROM, WHERE, ORDER BY, GROUP BY, HAVING.-과 함께라면요. 이미 3가지를 배웠내요.

형식은 다음 3가지로 정리할 수 있습니다:

- **SELECT clause**(select절)은 결과에 넣을 것들
- **FROM clause** 은 어느 테이블에서 가져올지
- **WHERE clause** 은 필터링



SELECT clause 는 WHERE절에 있는 것들을 포함하지 않아도 됩니다. 즉 원하는 것과 필터링 기준은 관련이 없어도 됩니다.