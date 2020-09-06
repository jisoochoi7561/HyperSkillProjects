## Theory: Basic UPDATE statement

보통 우리가 데이터를 저장만 하진 않지요 : 우리는 현재상태를 최대한 반영해야 겠지요. 누군가가 승진하면 , 직함이 바뀌어야 겠지요. 친구에게 선물을 사주면 당신계좌의 잔고는 그에 맞게 줄어들어야 겠지요. 이런 상황에서 SQL은 이미 존재하고 있는 row의 값을 바꾸게 해주는 실행 -UPDATE를 제공합니다.

## **General form**

업데이트 하려면 어떤정보가 필요하지요? 우선 우리가 데이터를 수정하고 싶은 table의 이름이 필요하지요. 데이터가 있는 column의 이름도 필요하고요. 또 새로운 값이 될 expression도 필요하지요:



```
UPDATE table_name SET col1 = expr1, col2 = expr2, …, colN = expr;
```

"Column name-expression" 페어들은 콤마로 구분됩니다. 일반적으로, 모든 SQL식을 쓸 수 있습니다. 리터럴과 연산자와 함수와 레퍼런스들의 결합을 써넣으면 됩니다. 기억해야할 것은 **type consistency**(타입 일관성)뿐입니다; integer column에 text를 넣으려 하면 문제가 발생할 겁니다.

ABC 회사의 개발자로 일하고 있다고합시다. 회사는 데이터를 많이 갖고 있고 SQL로 그걸 다루고 있습니다. 그들의 직원에 관한 정보가 employees라는 테이블에 저장되어 있습니다. 각각의 직원들은 부서 id(integer)와 lastname(text) 연봉(integer)와 연봉상한선(integer)가 있지요:

![img](https://ucarecdn.com/7c9f3449-f324-4ced-919c-99e22be07283/)

어떤 이유로 모든 직원들이 14번 부서로 간다면 다음과같이 씁니다:

```
UPDATE employees SET department_id = 14;
```

결과:

![img](https://ucarecdn.com/945eae79-278c-41dd-b98a-f45e8315a867/)

## **Column references**

말했듯이 새로운 값이 리터럴일 필요는 없습니다. 많은 경우에 이것은 데이터들의 결합이죠. `=`오른쪽에 있는 식에 있는 column 이름들은 각각의 row에 맞는 현재attribute값을 뜻합니다.

우리가 직원들의 월급을 10000씩 올려준다고 합시다. 그러려면 :

```
UPDATE employees SET salary = salary + 10000;
```

![img](https://ucarecdn.com/2df7d313-ca87-42c9-90a5-275364a5fe82/)

주의: UPDATE를 하는 도중에, table의 각각의 row들은 개별적으로 다뤄집니다. 만약에 우리가 이런식으로 접근하면 그 row가 가지고있는 정보만 접근할 수 있습니다.

여러개의 column들을 동시에 업데이트 할 수도 있습니다:

```
UPDATE employees SET department_id = 14, salary = salary + 10000;
```

더 정교하게 해보죠 :  월급을 월급상한선의 80퍼센트로 맞추고 소수점은 생략합시다.소수점을 생략하는 부분에서 floor()함수(real을 Integer로 바꿈)를 쓰겠습니다.

```
UPDATE employees SET salary = floor(0.8 * upper_limit);
```

![img](https://ucarecdn.com/25a409a2-fad0-4ed9-8a7a-88e71805eb2c/)