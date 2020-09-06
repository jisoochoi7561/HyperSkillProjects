## Theory: The NULL value

노벨상데이터를 저장하기 위한 table을 만들었다고 합시다.

다음 column들을 정의했습니다:  년도, 분야, 수상자이름, 수상자의 출생년도.

근데 어떤 수상자들은 출생년도가 없습니다.

예를들면 2012년 노벨평화상 수상자인 "유럽연합"

00같은 값을 대충 넣어줘도 되겠지요.

하지만 언젠가는 그게 문제가 될지도 모릅니다.

이런 경우를 위해 SQL은 특별한 값: NULL을 지원합니다.



## NULL value

NULL은 SQL에서 어떤 값이 존재하지 않거나 모른다는 것을 뜻합니다. 이것은 빈문자열""이나 0과 헷갈려서는 안됩니다.NULL을 사용한 연산의 값은 NULL입니다. 예를 들면 , 2+NULL 은 NULL입니다.

NULL값은 모든 type에 가능합니다. 하지만, 소프트웨어 엔지니어들은 **NOT NULL** **constraint(강제)**를 표를 만들때 column에 사용해줘서 그 속성의 값이 NULL이 될 수 없다는걸 명시할 수 있습니다. 예를 들면 다음코드에서는 "winner_birth_year"만이 NULL을 사용할 수 있습니다:

```sql
CREATE TABLE winners ( 
    year INTEGER NOT NULL,
    field VARCHAR(20) NOT NULL,  //(최대)20글자짜리 string타입입니다.
    winner_name VARCHAR(100) NOT NULL, 
    winner_birth_year INTEGER);
```

## Comparisons with NULL

NULL 값은 기본적으로 "아무거나 다됨"이란 뜻입니다. 따라서 NULL이 들어간 비교연산은 TRUE나 FALSE가아닌,3번째 논리값 **UNKNOWN**을 리턴합니다. 이것은 사실은, boolean 타입에서 NULL의 값입니다. 그리고 어떤 DBMS들은 NULL과 UNKNOWN을 구별하지 않습니다. 다음 연산의 결과들은 모두 UNKNOWN입니다:

- NULL = 1
- NULL <> 1
- NULL > 1
- NULL = '1'
- NULL = NULL



아무것도 NULL과 equal하지 않습니다. 심지어 NULL조차도 NULL과 equal이 아닙니다!

우리가 어떤 표현이 null인지 아닌지 어떻게 알 수 있을까요? SQL은 **IS NULL** and **IS NOT NULL**을 지원합니다. 예를들면 다음 쿼리들은 TRUE를 리턴합니다:

```sql
SELECT 0+NULL IS NULL;
SELECT '' IS NOT NULL;
```

얘네는 FALSE:

```sql
SELECT NULL IS NOT NULL;
SELECT 1-1 IS NULL;
```

## TRUE, FALSE, and UNKNOWN

UNKNOWN이 들어간 논리식에서 UNKNOWN의 값이 뭐든지에 상관없이 값이 나올 때가 있습니다. 따라서 계산연산과는 다르게 논리식에서는 UNKNOWN을 쓰고도 TRUE나 FALSE가 나올 수 있습니다.예를들면:

- (NULL = 1) AND TRUE evaluates to UNKNOWN (the result would be TRUE only if both operands were TRUE),
- (NULL = 1) OR TRUE equals to TRUE (the result is TRUE because at least one operand is TRUE).