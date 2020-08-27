## Theory: Exceptions in threads

만약 프로그램에 있는 스레드 중 하나가 예외를 던졌다면 , 그리고 제대로 처리되지 않았다면 , 스레드는 끝난다. 프로그램이 싱글스레드 프로그램이었다면 프로그램 자체가 끝난다 - 왜냐면 JVM은 **non-daemon**스레드가 남아있지 않으면 프로그램을 끄기 때문이다.

조그만 예를 보자:

```java
public class SingleThreadProgram {
    public static void main(String[] args) {
        System.out.println(2 / 0);
    }
}
```

실행결과:

```java
Exception in thread "main" java.lang.ArithmeticException: / by zero
  at org.example.multithreading.exceptions.SingleThreadProgram.main(SingleThreadProgram.java:6)

Process finished with exit code 1
```

code `1`은 프로그램이 에러로 끝난다는 걸 뜻한다.

우리가 새로 만든 스레드에서 에러가 난다면 프로그램전체가 멈추진 않는다:

```java
public class ExceptionInThreadExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new CustomThread(); // 새스레드
        thread.start(); // 스레드 시작
        thread.join(); // wait for thread with exception to terminate //스레드 기다림.에러로 인해 스레드 종료됨.
        System.out.println("I am printed!"); // this line will be printed
    }
}

class CustomThread extends Thread {

    @Override
    public void run() {
        System.out.println(2 / 0); //exception 일어남
    }
}
```

예외처리를 하지 않았어도 프로그램은 제대로 완료된다.

```java
Exception in thread "Thread-0" java.lang.ArithmeticException: / by zero  at org.example.multithreading.exceptions.CustomThread.run(ExceptionInThreadExample.java:15)
I am printed!

Process finished with exit code 0
```

code `0`은 프로그램이 성공적으로 끝낫음을 뜻한다.

여러 스레드의 예외들을 다루는 것은 어렵다. 프로세스에 살아있는 non-daemon 스레드가 있다면, 스레드에서 에러가 났더라도 프로그램의 실행은 된다. 그래도 스레드의 예외들은 처리해주는 게 좋다.