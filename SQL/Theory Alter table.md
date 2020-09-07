## Theory: Alter table

가끔은 테이블을 수정해야할 때가 있다 : 새로운 column을 추가하거나 이미 있던 것을 수정하거나 등등.우리는 전에 있던 테이블을 지우고 그냥 새로운 테이블을 만들수도 있겠지만, 그러다가는 데이터를 잃어버리기 쉽다. 다행히도 SQL은 존재하는 테이블을 수정할 수 있는  **ALTER TABLE statement**를 제공한다. 이것은 column을 만들거나 지우거나 바꿀 떄 쓰인다.

## Adding a new column

새로운 회사가 전세계에 사원들을 갖고 있다고 하자. 그리고 그 정보를 기록하려고 한다고 하자. 여기 employees테이블의 예시가 있다:

![img](https://ucarecdn.com/ba288ae4-d255-4a5d-a364-cc1a3d54c7b8/)

지금 시점에선 테이블은 연락처정보를 포함하지 않지만 , 우리가 연락처를 추가한다면 멋진 일이 될것이다.

새로운 column을 테이블에 추가하려는것은 **ALTER TABLE statement** with **ADD COLUMN**.을 쓰면 된다.

다음 쿼리가 직원들의 이메일 column을 우리의 employees테이블에 추가해준다:

```sql
ALTER TABLE employees 
ADD COLUMN employee_email VARCHAR(10);
```

보시듯이 우리는 쿼리에 만들때처럼 add하고 있다. 이 경우 *employee_email* column의 타입은 VARCHAR(10) 이다.

쿼리의 실행후에 테이블은 빈 column을 갖는다:

![img](https://ucarecdn.com/83f70eb0-014b-480e-a6b3-4b72cb8a7952/)

## Changing the data type

우리는 *employee_email* 을 VARCHAR(10) 타입으로 만들었는데, 어떤 사람들은 매우 긴 이메일 'johntomasyork@emailservice.com'. 을 갖고있다. 이런 이메일을 다루기 위해 데이터타입을 바꿔야만 한다.

데이터타입을 바꾸려면  **ALTER TABLE statement** 와 **MODIFY COLUMN을 사용하라:**

```sql
ALTER TABLE employees 
MODIFY COLUMN employee_email VARCHAR(45);
```

쿼리의 실행결과로 , *employee_email* 은 VARCHAR(45) 타입이 된다. 이제 우리는 긴 이메일도 추가할 수 있다:

![img](https://ucarecdn.com/020d0dc5-16b9-4945-bf7d-9e50f66f43d0/)



타입을 바꾸기로 결심했다면, column를 비워놓거나 새로운데이터타입이 원래있던 것에도 적용가능해야 한다. 안그러면 에러이다.

여기서 우리는 MySQL 의 문법을 보여주었다. 다른 SQL에서는 다를수도 있다.

## Dropping an existing column

우리의 employees테이블에서 우리는 고향에 관한 정보를 가지고 있다. 이 불필요한 정보를 없애보자.

이 column을 없애려면 **ALTER TABLE statement** 와 **DROP COLUMN**을 쓰라:

```sql
ALTER TABLE employees 
DROP COLUMN native_city;
```

실행결과:

![img](https://ucarecdn.com/d5303237-25b6-4adb-b724-94d965bb9ea5/)



이렇게 column을 지우면 그 안의 정보도 모두 사라진다.

## Renaming a column

*employee_email* 의 이름이 그냥 email이면 더 깔끔할 것 같다.

이름을 바꿔보자.**ALTER TABLE statement** 와 **CHANGE**를 쓰라:

```sql
ALTER TABLE employees
CHANGE employee_email email VARCHAR(45);
```

결과:

![img](https://ucarecdn.com/816fccc6-1474-4666-ab14-b36f3d75cd4d/)

보시듯이 쿼리에 타입선언이 포함되어있다 : 즉 이름과 타입을 동시에 바꿀 수도 있다. 이름만 바꾸려면 그냥 그전의 타입을 그대로 써넣으면 된다.

여기서 우리는 MySQL을 기준으로 했다. 다른 SQL은 다를 수 있다.

