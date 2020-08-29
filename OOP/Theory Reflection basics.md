## Theory: Reflection basics



## Introduction

**Reflection**은 자바에서 가장 강력한 기능중 하나입니다. 리플렉션은 런타임에 프로그램에 접근하고 수정하는 과정입니다. 이것은 클래스와 멤버들 (생성자,필드,메소드)들을 런타임에 접근하고 수정할 수 있다는 것입니다.



## java.lang.reflect package

자바 리플렉션은 `java.lang.reflect` package로 구현한다. 비록 이 패키지가 엄청 많은 것들을 포함하고 있지만 ,지금은 딱 4개의 클래스만 알면 된다 : 

- **Field**: 이것을 써서 변수의 이름,값,타입,접근제한자를 읽고 수정할 수 있다.
- **Method**: 이것을 써서 메소드의 파라미터타입,리턴타입,접근제한자,예외타입을 읽고 수정할 수 있다.
- **Constructor**: 생성자의 이름과 파라미터타입, 접근제한자를 읽고 수정할 수 있다.
- **Modifier**: 접근제한자에 대한 정보를 읽을 수 있다.

## java.lang.Class

또 중요한 점이 있다. 방금 위에말한 리플렉트 패키지만으로 리플렉션을 구현할 수는 없다. 리플렉트 패키지는 필드,메소드,생성자 등에 대한 정보를 주지만 , 먼저 필드리스트 메소드리스트 생성자리스트를 받아야한다(즉 어떻게 메소드나 변수를 참조하지?라는 뜻이다.).

이것은 `java.lang.Class`클래스에 있는 `forName()`스태틱 메소드로 가능하다. 이 메소드에 클래스의 이름을 주면 , 이것은 **Class**를 리턴한다.**Class**는 뭐 string int 이런것처럼 타입이다 여기서. **Class**클래스는 클래스에 대한 정보를 담고 있다.

`java.lang.Class`는 다른 메소드들로 **attributes** (fields, methods, constructors)들도 얻게 해준다. 메소드 목록들:

- `forName(String ClassName)`
- `getConstructors()`
- `getDeclaredConstructors()`
- `getFields()`
- `getDeclaredFields()`
- `getMethods()`
- `getDeclaredMethods()`

두가지 유의점이 있다.

우선,  `forName()`을 제외한 나머지 메소드들은,`java.lang.reflect`클래스 객체의 배열을 리턴한다는 것이다. 예를 들면  `getFields()`은 `java.lang.reflect.Field` 클래스 객체들의 배열을 리턴한다. 그 후, `java.lang.reflect` 패키지의 메소드를 써서  **constructors**, **fields**, and **method**에 관한 더많은 정보를 얻으면 된다.

두번째로, `getConstructors()`, `getFields()` 그리고 `getMethods()` 은 퍼블릭한 것들만 **Class** 객체로 리턴한다.이 메소드들은 또 사속받은 것들도 리턴한다.

비슷하게,`getDeclaredConstructors()`, `getDeclaredFields()`, `getDeclaredMethods()` 은 모든 선언된 것들을 리턴하지만 , 상속받은 것들은 리턴하지 않는다.

보통,  프로그래머는 declared를 사용한다. 예시를 보면 더 알아보자.

## Coding examples

`Student`클래스를 예로 들자. 이것은 3개의 퍼블릭 필드와, 하나의 protected필드, 하나의 프라이빗 필드를 갖고 있다. 이것은 또한 기본 생성자와 두개의 인자를 가지는 퍼블릭생성자를 가진다. 그리고 이 클래스는 프라이빗 메소드와 퍼블릭 메소드를 하나씩 갖고 있다.

```java
public class Student {
    public String firstName;
    public String lastName;
    public int age;
    protected String phoneNumber;
    private String accountNumber;
    
    Student(){
        System.out.println("This is default Constructor");
    }
    
    public Student(String firstName, String lastName){
        this.firstName= firstName;
        this.lastName= lastName;
        System.out.println("This is public Constructor");
    }
    
    private String sanitizeAccountNumber(String accountNumber){
        System.out.println("This is a private method to sanitize account number");
        //code to sanitize accountNumber goes here. 
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber){
        accountNumber = sanitizeAccountNumber(accountNumber);
        this.accountNumber = accountNumber;
    }
}
```

리플렉션은 보통 3단계로 이루어진다:

\1.  `java.lang.Class` 객체를  `forName()`을 사용해서 얻는다.이 예시에서, 우리가 reflect하고 싶은 클래스는 `Student`이다

```java
Class student = Class.forName("Student"); //Class student는 현재 Student클래스 이다.
```

\2. 속성을 배열로 얻는다. 여기서 우리는 필드, 생성자, 메소드를 얻었다.

```java
Constructor[] declaredConstructors = student.getDeclaredConstructors();
Constructor[] constructors = student.getConstructors();
Field[] declaredFields = student.getDeclaredFields();
Field[] fields = student.getFields();
Method[] declaredMethods = student.getDeclaredMethods();
Method[] methods = student.getMethods(); // 여기서부터 리플렉트가 쓰인다
```

\3. 클래스 속성에 대한 정보를 얻고 쓰자. 이 예시에서 우리는 생성자, 필드, 메소드의 이름을 얻어서 프린트 할 것이다.

```java
for(Constructor dc : declaredConstructors) {
    System.out.println("Declared Constructor " + dc.getName());
}
for (Constructor c : constructors) {
    System.out.println("Constructor " + c.getName());
}
for (Field df : declaredFields) {
    System.out.println("Declared Field " + df.getName());
}
for (Field f : fields) {
    System.out.println("Field " + f.getName());
}
for (Method dm : declaredMethods) {
    System.out.println("Declared Method " + dm.getName());
}
for (Method m : methods) {
    System.out.println("Method " + m.getName());
}
```

## Explaining the output

실행결과:

```java
Declared Constructor Student
Declared Constructor Student
Constructor Student
Declared Field firstName
Declared Field lastName
Declared Field age
Declared Field phoneNumber
Declared Field accountNumber
Field firstName
Field lastName
Field age
Declared Method sanitizeAccountNumber
Declared Method setAccountNumber
Method setAccountNumber
Method wait
Method wait
Method wait
Method equals
Method toString
Method hashCode
Method getClass
Method notify
Method notifyAll
```

 `getDeclaredConstructor()`  가 생성자 두개를 모두 반환하고 있는 것을 보라.

`getConstructors()`는 public인 것 하나만 리턴했다.

비슷하게 , `getDeclaredFields()`는 `Student` 에서 선언된 모든 필드를 리턴했지만 `getFields()`는 퍼블릭만 리턴했다.

그리고, 메소드를 보라. 우리의 예상처럼 `getDeclaredMethods()`는 퍼블릭과 프라이빗 모두 리턴했다. 재밌는 것은 `getMethods()`가 우리의 예상대로 `setAccountNumber()`퍼블릭 메소드만 리턴한게 아니라, 다른 이상한 것들도 리턴했다는 것이다. 만약 기억한다면 , 우리는 `java.lang.Object` 클래스가 모든 클래스의 **superclass** 임을 알 것이다. `Object` 는 9개의 퍼블릭 메소드를 갖고 있고, 자연스럽게 우리가 만든 것도 그것들을 상속한다. 그래서 우리가 예상치 못한 9개의 메소드가 더 튀어나온 것이다.

## **Summary**

**Reflection** 은 클래스에 접근하고 수정하는 방법이다. `java.lang.reflect` 패키지와 `java.lang.Class` 클래스가 중요하다.

3단계로 리플렉션을 한다:

- 원하는 클래스의 Class 오브젝트를 얻는다
- `java.lang.Class`의 메소드를 사용해서 원하는 속성을 얻는다.
- `java.lang.reflect`를 사용해서 원하는 정보를 뽑아낸다.
