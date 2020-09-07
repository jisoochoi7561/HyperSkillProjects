## Theory: Consistency constraints

테이블의 각각의 column들은 특정 데이터타입을 가지고 있다. 따라서 다른 데이터타입을 가진 데이터를 넣을 수 없다. 데이터타입의 제한은 우리의 실수를 막아주지만 더 구체적인 제한방법도 있다. 예를 들면 id들이 모두 달라야한다거나, 19세 이상만 손님이 될 수 있다던가. 이런 특정한 값에 대한 제한을 **constraints**라고 한다.

## Example

가장 흔한 constraints (제약) 들은 **NOT NULL**, **UNIQUE**, **CHECK, DEFAULT, PRIMARY KEY** and **FOREIGN KEY.** 이다. 이 토픽에서 우리는 앞의 4개를 다룬다.

간단한 예를 들어보자. 우리가 *employees* 테이블에 *personal_id*, *first_name*, *last_name* and *age* 컬럼을 만들었다고 하자:

```sql
CREATE TABLE employees (
 personal_id INT,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 age INT
);
```

이 테이블에 몇몇 제약을 추가해보자.

이 토픽은 MySQL기준이다.

## NOT NULL constraint

**NOT NULL**은  **null** 값을 허용하지 않는다. 우리의 테이블에서 *age*를 not null로 만들어보자. 

 `ALTER TABLE MODIFY` 문장을 쓰자:

```sql
ALTER TABLE employees
MODIFY age INT NOT NULL;
```

이 쿼리를 실행하면 나이가 없는 직원은 추가할 수 없다. 만약 age에 이미 NULL이들어가 잇다면 에러가 발생한다.

제약을 없애려면 NOT NULL을 안쓴 문장을 그대로 쓰면된다:

```sql
ALTER TABLE employees
MODIFY age INT;
```

`CREATE TABLE`에서도 제약을 추가할 수 있다:

```sql
CREATE TABLE employees (
 personal_id INT,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 age INT NOT NULL
);
```

## UNIQUE constraint

**UNIQUE**  제약은 모든 값이 달라야 한다는 제약이다. 우리는 ID가 모두 달라야한다고 확신하므로 *personal_id*를 UNIQUE하게 만들면된다.

다음과 같이:

```sql
CREATE TABLE employees (
 personal_id INT UNIQUE,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 age INT 
);
```

여기서는 ADD를 써야한다:

```sql
ALTER TABLE employees
ADD UNIQUE (personal_id);
```

쿼리가 실행되면 id들은 유니크하다. 만약 유니크하지않은 id가 존재한다면 에러가 난다.

여러개를 유니크하게 만들려면 이름있는제약(**named constraint**)을 선언해줄 수 있다:

```sql
CREATE TABLE employees (
 personal_id INT,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 age INT, 
 CONSTRAINT uq_id_last_name UNIQUE (personal_id, last_name) 
);
```

drop으로 named constraint를 없앨 수 있다.:

```sql
ALTER TABLE employees
DROP INDEX uq_id_last_name;
```

## **CHECK constraint**

**CHECK** 제약은 논리적인 제약을 추가하게 해준다. 예를 들어 나이가 16살 이상인 직원만 허용할 수 있다.  `CHECK` 를 `CREATE TABLE`에서:

```sql
CREATE TABLE employees (
 personal_id INT,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 age INT CHECK (age>16)
);
```

존재하는 테이블에 check제약을 추가하려면 **ALTER TABLE ADD CHECK**를 쓰라. unique와 같은 문법이다.

 이름없는 `CHECK`를 지우려면 , **ALTER TABLE DROP CHECK**:

```sql
ALTER TABLE employees
DROP CHECK age;
```

`CHECK` 제약은 이름을 가지고 여러개의 column에 적용될 수 있다. unique와 같은 문법이다.  **ALTER TABLE ADD CONSTRAINT** 를 써서 이름을 가진`CHECK` 를 추가할 수도 있다:

```sql
ALTER TABLE employees 
ADD CONSTRAINT chk_employee CHECK (age>16 AND personal_id>0);
```

`ALTER TABLE ADD CONSTRAINT` 은 check 와 unique 모두 쓸 수 있다.

이름있는 `CHECK` 를 지우려면 `ALTER TABLE DROP CONSTRAINT`를 쓰라.

## **DEFAULT constraint**

**DEFAULT** 제약은 초기값을 설정해준다:

```sql
CREATE TABLE employees (
 personal_id INT,
 first_name VARCHAR(30) DEFAULT 'John',
 last_name VARCHAR(30) DEFAULT 'Doe',
 age INT DEFAULT 17
);
```

이제 성과 이름 나이가 없는 직원을 추가해도 기본값을 가진 직원으로 추가될 것이다.

`DEFAULT` 제약을 추가하려면 **ALTER TABLE ALTER SET DEFAULT** :

```sql
ALTER TABLE employees
ALTER first_name SET DEFAULT 'John';
```

지우려면 **ALTER TABLE ALTER DROP DEFAULT** :

```sql
ALTER TABLE employees 
ALTER first_name DROP DEFAULT;
```

## Combining constraints

당연히 여러개의 제약을 동시에 사용할 수 있다:

```sql
CREATE TABLE employees (
 personal_id INT NOT NULL UNIQUE,
 first_name VARCHAR(30) NOT NULL DEFAULT 'John',
 last_name VARCHAR(30) NOT NULL DEFAULT 'Doe',
 age INT DEFAULT 17, 
 CHECK (age>16) 
);
```

