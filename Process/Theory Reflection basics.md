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

비슷하게,`getDeclaredConstructors()`, `getDeclaredFields()`, `getDeclaredMethods()` 도 이런걸 리턴하지만 , 상속받은 것들은 리턴하지 않는다.

보통,  프로그래머는 declared를 사용한다. 예시를 보면 더 알아보자.

## Coding examples

Suppose that you have a class called `Student`. It has three public fields, one protected field, and a private field. It also has a default constructor and a public constructor. `Student` class also has a private method and a public method.

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

Reflection process usually has three steps:

\1. Get a `java.lang.Class` object of the class using the `forName()` method. In this case, the class we want to reflect is `Student`.

```java
Class student = Class.forName("Student");
```

\2. Get the class attributes as an array. In this case, we are interested in fields, constructors, and methods.

```java
Constructor[] declaredConstructors = student.getDeclaredConstructors();
Constructor[] constructors = student.getConstructors();
Field[] declaredFields = student.getDeclaredFields();
Field[] fields = student.getFields();
Method[] declaredMethods = student.getDeclaredMethods();
Method[] methods = student.getMethods();
```

\3. Get the information about class attributes and use it. In this case, we are going to retrieve names of constructors, fields, and methods and print them.

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

You can write these three sections inside the `main()` method and run this code.

## Explaining the output

When you run the code above you will get a list of constructors, fields, and methods:

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

You can see that `getDeclaredConstructor()` has returned both constructors of the `Student` class while `getConstructors()` has returned only the public constructor. Likewise, `getDeclaredFields()` has returned all fields of the `Student` class while `getFields()` has returned only public fields.

Finally, we print the methods of the `Student` class. As expected, `getDeclaredMethods()` has returned both methods. Now the interesting part is that `getMethods()` has returned some other methods other than `setAccountNumber()` we've expected. If you remember, in one of our previous topics, we've mentioned that `java.lang.Object` class is the **superclass** of all the classes we create. `Object` class has **nine** public methods and all classes we create inherit those methods. That's why you can see nine extra methods in the output.

## **Summary**

**Reflection** is a way to get information about or modify fields, methods, and constructors of a class. `java.lang.reflect` package and `java.lang.Class` class are essential in Java reflection.

There are three steps in the Java reflection process:

- Get the Class object of the class that you want to reflect on.
- Get the attributes of the class you want to reflect on as a list or array using `java.lang.Class` methods.
- Get information about the particular attribute you got in the second step using the `java.lang.reflect` package.

Reflection is a complicated concept that requires some knowledge of **JVM** and **Java internal processes**. Anyhow, we believe the information we've provided in this topic will help you start using reflection in your projects.