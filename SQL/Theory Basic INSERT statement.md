## Theory: Basic INSERT statement

데이터베이스를 점점 잘하게 되고 있다. 이제 테이블에 새로운 정보를 넣을 차례다.

## Basic INSERT statement

**INSERT INTO statement**로 간단히 새로운 row를 넣을 수 있다.

예를 들어서 새로운 고객정보를 추가해보자 (VARCHAR(20)), *surname* (VARCHAR(20)), *zip_code* (INT) and *city* (VARCHAR(10)).

![img](https://ucarecdn.com/1dc72f30-c3ea-4fd9-a4a0-2f14644ef22e/)

새 고객의 이름은 Bobby이고 surname 은 Ray 이고 ZIP 코드는 60601이고 city는 Chicago이다. 다음 쿼리로 추가한다:

```sql
INSERT INTO customers (name, surname, zip_code, city) VALUES ('Bobby', 'Ray', 60601, 'Chicago')
```

**VALUES**키워드 뒤에 정보를 입력하면 된다.

결과:

![img](https://ucarecdn.com/12108bfa-eeed-4ad1-a186-58be9dace3a6/)

만약 column의 순서를 정확히 알고 있다면 이름을 저렇게 열거하지 않고 그냥 생략해도 된다:

```sql
INSERT INTO customers VALUES ('Bobby', 'Ray', 60601, 'Chicago')
```

## Insert multiple rows

여러개의 row를 넣는것도 한번에 가능하다:

```sql
INSERT INTO customers (name, surname, zip_code, city) 
VALUES ('Mary', 'West', 75201, 'Dallas'), ('Steve', 'Palmer', 33107, 'Miami')
```

쉼표로 두개를 분리해주기만 하면된다.

## Insert into specified columns

정보가 부족하지만 추가해야할 수도 있다. 우리가 cats라는 테이블을 가지고 있다고 하자(VARCHAR(20)), *age* (INT) and *owner* (VARCHAR(40)).

각 column들은 default값이 없다.

![img](https://ucarecdn.com/718d392b-8927-4b4d-9470-ee253faa9fbd/)

나이와 이름은 알지만 주인을 모르는 고양이를 추가해보자:

```sql
INSERT INTO cats (name, age) VALUES ('Felix', 3)
```

결과:

![img](https://ucarecdn.com/beb92c88-6f75-453d-a33a-d1b08fd43ccd/)

디펄트 값이 있다면 디펄트로 채워진다.

