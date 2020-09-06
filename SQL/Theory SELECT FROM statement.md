## Theory: SELECT FROM statement

이미 SQL이 table을 다루는 언어라는 걸 알고 있을 것이다. 또 이름을 가진 테이블에서 모든 데이터를 뽑아내는 쿼리도 알고 있다:

```sql
SELECT * FROM table_name;
```

이 토픽에서 SELECT로 table을 다루는 법을 더 알아본다.

## Projection

weather라는 테이블에다가 지난 5일간의 런던날씨를 저장하고 있다고하자.

![img](https://ucarecdn.com/6952a453-b292-442f-8afe-8a82ffe30064/)

보면 알듯이, 각 시간마다 많은 속성들이 있다. 우리는 이 모든게 필요할까? 모바일스크린에 띄울만한 중요한 정보들만 골라내는 쿼리를 작성해보자 : 예를들면 날짜,시간,기온,풍속.

```sql
SELECT
    day, 
    hour,
    phenomena,
    temperature as "temperature in Celsius",
    feels_like as "feels like in Celsius",
    wind_speed as "wind speed in m/s"
FROM
    weather
;
```

 `SELECT` 키워드 다음에 우리가 원하는 column을 고르고 원한다면 alias를 설정해준다. 결과는:

![img](https://ucarecdn.com/9eacb359-755c-490b-a362-22604c09b20d/)

테이블에서 일부분만을 뽑아내는 것을 **projection**(투영)이라고 한다

여기 이런 쿼리들의 일반적인 구조가 있다 : SELECT,(alias를 원한다면 붙인) column들, FROM, 테이블이름, 그리고 세미콜론;

```sql
SELECT
    col1 [AS alias1], ..., colN [AS aliasN]  
FROM
    table_name
;
```

## Expressions

자 이제 동일한 데이터에서 다른 것들을 뽑아내길 원한다고 하자. 예를 들어 장소와 화씨온도와 더 춥게느껴지는지.

![img](https://ucarecdn.com/f0c8aabc-629a-4fe8-b424-316cc22941eb/)

아래쿼리로 하면 된다:

```sql
SELECT
    'London' as place,
    day, 
    hour,
    phenomena,
    temperature*9/5+32 as "temperature in Fahrenheit",
    feels_like < temperature as "feels colder",
    wind_speed as "wind speed in m/s"
FROM
    weather
;
```

우리는 place라는 속성에 London을 부여해서 만들었다.그런식으로 temperature in Fahrenheit와 feels colder도 만들었다.그렇다 , column의 이름을 적는 곳에 표현식을 적을 수 있다! 

## Logical table

데이터 관리 시스템은 **logical table**이라는 추상적 개념을 써서 실제데이터를 우리에게서 분리한다. 우리가 쿼리를 실행하기 위해 알아야할것은 **database schema**뿐이다 - 테이블이름,column이름과 타입-그리고 적절한 접근권한. 

## Conclusion

```sql
SELECT
    exp1 [AS alias1], ..., expN [AS aliasN]  
FROM
    table_name
;
```