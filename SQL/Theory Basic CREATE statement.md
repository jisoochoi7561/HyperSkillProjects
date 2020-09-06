## Theory: Basic CREATE statement

SQL은 테이블과 데이터베이스와 데이터를 다루는 언어이다. 물론 이미 알고있는 사실이다; 하지만 모르고 있는것은 :어떻게 데이터베이스와 테이블을 만들고 지우는지. 이 토픽에서 그걸 공부한다.

## CREATE statement

데이터베이스를 만들려면 **CREATE DATABASE statement**를 사용하라.예를들면 대학교 학생들의 정보를 저장하는 데이터베이스를 만들고 그것의 이름을 students라고 하는 쿼리:

```sql
CREATE DATABASE students;
```

이것으로 데이터베이스를 만들긴 했지만 데이터를 조직하려면 테이블을 몇개 만들어야한다.

## Creating a new table

테이블을 만들려면 **CREATE TABLE**을 쓰라.

*students_info* 테이블을 만들자 : *student_id*, *name*, *surname* and *age*. 이렇게 column4개를 갖고 있다.

*student_id*는 INT타입의 아이디를 저장한다.*name* and *surname*은 VARCHAR(30) 이다. *age* 는INT 이다.

```sql
CREATE TABLE students_info ( 
 student_id INT, 
 name VARCHAR(30), 
 surname VARCHAR(30), 
 age INT
);
```

이제 빈 테이블*students_info*을 얻었다:

![img](https://ucarecdn.com/6f75c5a8-e344-4c05-bcfd-10ace9b470c9/)

위의 쿼리가 CREATE의 중요사용법을 보여줬다. 이런식으로 만든 테이블은 무척 간단하고, 나중에 더 복잡한 테이블을 만드는 법을 배울것이다.

## Drop a database

이제 어떻게 데이터베이스와 테이블을 만드는지 알았으니 지우는법도 알아보자.**DROP DATABASE statement**.을 쓰라:

```sql
DROP DATABASE students;
```

데이터베이스를 지운다면 그 안의 모든테이블도 지워진다.

## Drop a table

DROP TABLE 을 쓰라:

```sql
DROP TABLE students_info;
```

테이블을 지우면 그 안의 정보도 모두 지워진다.

