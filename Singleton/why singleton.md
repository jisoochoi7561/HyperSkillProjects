[싱글톤은 왜 존재함?](https://stackoverflow.com/questions/4979023/why-are-there-java-singleton-classes-when-would-you-need-to-use-one)

>인스턴스 단 하나를 쓸 때 singleton을 쓴단 건 이해했습니다. 근데 이게 왜 쓸모있는건지 모르겠습니다.static 변수와 method들을 선언하고 thread여러개가 관여될 땐 synchronize를 쓰면 되지않나요. 굳이 이런 고생을 할 거 있습니까. 물론 제가 놓친점이 있겠지요?

> 다른 답변들도 일리가 있지만 질문자는 왜 static들보다 singleton을 쓰는질 묻는 것 같습니다.
>
> **왜 싱글톤을 쓰는가?**
>
> 검색해보세요. [JavaWorld](http://www.javaworld.com/javaworld/jw-04-2003/jw-0425-designpatterns.html)에 따르면:
>
> > 가끔 클래스가 단 하나의 인스턴스를 가지는게 좋을 때가 있습니다: window managers, print spoolers, and filesystems 등등. 보통, 이런 종류의 객체는—singleton이라고 부르죠—시스템에서 여러 objects가 접근하고,따라서 글로벌하게 접근할 방법이 필요합니다. 물론, 진짜로 딱 하나가 필요할 때 써야겠지요,마음을 바꿀 때를 고려하는 것도 괜찮겠습니다.
>
> **왜 static method로 이루어진 클래스 대신 singleton을 쓰는가**
>
> 이유들
>
> 1. 상속을 쓸 수도 있음
> 2. interface 사용가능
> 3. singleton의 unit test가 쉬워짐
> 4. singleton을 쓰는 것의 unit test가 쉬워짐
>
> 3번째 이유를 설명하자면,싱글톤이 database connection pool이라면, 단 하나가 있기를 바랄 것이고,동시에 데이터베이스를 건드리지 않으면서 테스트하기를 원하겠죠( package-scope constructor 나 static creational method를 쓸 수 있습니다):
>
> ```java
> public class DatabaseConnectionPool { //database connection pool클래스입니다
>   private static class SingletonHolder { //싱글톤용 클래스입니다
>     public static DatabaseConnectionPool instance = new DatabaseConnectionPool(
>         new MySqlStatementSupplier()); //인스턴스 단 하나
>   }
> 
>   private final Supplier<Statement> statementSupplier;
> 
>   private DatabaseConnectionPool(Supplier<Statement> statementSupplier) {
>     this.statementSupplier = statementSupplier;
>   }
> 
>   /* 테스팅용 */
>   static DatabaseConnectionPool createInstanceForTest(Supplier<Statement> s) {
>     return new DatabaseConnectionPool(s);
>   }
> 
>   public static DatabaseConnectionPool getInstance() {
>     return SingletonHolder.instance;
>   }
> 
>   // more code here
> }
> ```
>
> (notice the use of the [Initialization On Demand Holder](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#dcl) pattern)
>
> You can then do testing of the `DatabaseConnectionPool` by using the package-scope `createInstanceForTest` method.
>
> Note, however, that having a static `getInstance()` method can cause "static cling", where code that depends on your singleton cannot be unit tested. **Static singletons are often not considered a good practice** because of this (see [this blog post](http://misko.hevery.com/2008/08/17/singletons-are-pathological-liars/))
>
> Instead, you could use a dependency injection framework like Spring or Guice to insure that your class has only one instance in production, while still allowing code that uses the class to be testable. Since the methods in the Singleton aren't static, you could use a mocking framework like JMock to mock your singleton in tests.