## Theory: JUnit and Mockito



## Java Unit Testing

자바는 객체 지향 언어 입니다. 자바의 핵심개념중 하나는 클래스 입니다. 모든 자바프로그램은 클래스들의 조합으로 볼 수 있습니다. 그것들이 서로 메소드를 사용해서 상호작용하죠. 자바에서, 클래스는 한 단위이고, 메소드들은 단위 테스팅의 대상입니다.

손수 테스트를 작성하는 건 지루한 일입니다. 다행히도 유닛테스팅프레임워크들이 이것을 쉽게 해줍니다. 이 토픽에서는 이 프레임 워크들을 공부하겠습니다.

## JUnit

**JUnit** 은 아마도 가장 유명한 유닛테스팅프레임워크 일 겁니다. 이것은 매우 활성화된 커뮤니티를 갖고있어서 포럼에 뭘 물어보면 꼬박꼬박 대답해 줍니다. **JUnit**의 기본개념을 알아봅시다.

우선은 당신의 프로젝트에 **JUnit**라이브러리를 포함시켜야 합니다. 만약 **maven** or **gradle** 을 쓰고 있다면,다음 코드를 복사 붙여넣기 한 후  *VERSION* 을 실제라이브러리의 버전으로 바꾸세요.:

**maven**

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>VERSION</version>
    <scope>test</scope>
</dependency>
```

**gradle**

```json
testCompile group: 'junit', name: 'junit', version: 'VERSION'
```



메소드는 *@Test* annotation으로 태그되면 테스트라고 해석됩니다:

```java
@Test
public void testMethod() {
    ...
}
```

보통, 모든 유닛테스트들은 따로 클래스를 만들어서 거기다 몰아넣습니다. 메이븐이나 그레들 같은 빌드시스템은 빌드과정의 마지막에 유닛테스팅을 포함합니다. 이것은 버그를 발견하는데 도움을줍니다. 유닛테스트가 실패하면 빌드가 실패해버립니다.

유닛테스트의 두드러지는 특징은 각각의 테스트들이 고립되어 있다는 것입니다. 이걸 실현하고 코드의 반복을 막기위해,  `@Before`*,* `@BeforeClass`, `@After`, and `@AfterClass` 어노테이션을 쓸 수 있습니다.

```java
@BeforeClass
public static void setUpClass() throws Exception {
    // Code executed before the first test method
}

@Before
public void setUp() throws Exception {
    // Code executed before each test
}

@After
public void tearDown() throws Exception {
    // Code executed after each test 
}
 
@AfterClass
public static void tearDownClass() throws Exception {
    // Code executed after the last test method 
}
```



JUnit은 특별한 API인 **Assertions**을 검사용(acceptance criteria)으로 씁니다. 이것은 결과가 예상과 맞는지 체크해서 틀리면 에러를 던지는 메소드들의 집합입니다. 자주 쓰이는 메소드들은 : 

- assertEquals
- assertTrue
- assertNotNull

간단한 예시를 봅시다. 우리가 두개의 메소드를 가지는 `Calculator` 클래스를 갖고 있습니다.

```java
public class Calculator {

    public int add(int x, int y) { //메소드1 : 덧셈
        ...
    }

    public int subtract(int x, int y) {//메소드2: 뺄셈
        ...
    }
}
```

**JUnit으로 테스팅하는 예시:**

```java
import org.junit.Assert;

public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 2);

        Assert.assertEquals(4, result);
    }

    @Test
    public void testSubtract() {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(4, 2);

        Assert.assertEquals(0, result);
    }
}
```

assertion이 실패하면 짧은 설명과 함께 에러가납니다 `AssertionError` .

```java
java.lang.AssertionError: 
Expected :0
Actual   :2
```

## Mockito

복잡한 클래스에서 유닛테스트는 많은 객체를 구별하고 초기화해야 할 수도 있습니다. 이 준비는 시간을 잡아먹고 테스트는 코드이상으로 복잡해 질 수도 있습니다. 이런 경우 **mockito** 프레임워크가 무척 유용합니다.이것은 당신에게 **mock** 라는 특별한 객체를 생성하게 해주고 실행하는동안 그것의 행동을 지정하게 해줍니다.

**mockito** 라이브러리를 **maven** or **gradle** 로 당신의 프로젝트에 추가하려면 다음 코드를 복사해서 붙여넣기하세요.*VERSION* 을 실제 버전으로 바꾸세요:

**maven**

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>VERSION</version>
    <scope>test</scope>
</dependency>
```

**gradle**

```json
testCompile group: 'org.mockito', name: 'mockito-core', version: 'VERSION'
```

참고 : *mockito-all*은 이제 쓰이지 않습니다.(그게뭔데;;)

예시를 들어 봅시다.`UsdConverter` 클래스는 현지통화를 미국달러로 바꾸는 클래스입니다. 테스트는 테스트할 유닛의 행동을 의존성에 관계없이 체크할 수 있어야 합니다. 환율은 매시간마다 바뀌기 때문에 `UsdConverter` 은 `ExchangeRateService` 를 통해 최근의 환율을 받아옵니다. 그리고,`ExchangeRateService` 의 `getUsd` 메소드는 실제 환율을 알기위해 **HTTP** 로 요청을 할 수 있습니다. 이 경우 테스트 환경을 만들기가 무척 어렵습니다.**Mockito** 는 우리에게 이 어려움을 피하게 해주고 행동을 제어할 빈 객체를 만들 API를 제공합니다.

주의: 우리는 돈에관해선 철저하기 떄문에 `BigDecimal` 을 쓰고 있습니다:

```java
public class UsdConverter {

    private ExchangeRateService service;

    public UsdConverter(ExchangeRateService service) {
        this.service = service;
    }

    public BigDecimal convertToUsd(BigDecimal converted) {
        return converted.multiply(service.getUsd());
    }
}
```

**mockito**를 써서 , 가짜 `ExchangeRateService` 객체를 만들고 `getUsd` 함수를 썼을 때 행동을 정해줄 수 있습니다:

```java
import org.junit.Assert;
import org.mockito.Mock;

public class UsdConverterTest {

    @Mock
    private ExchangeRateService service = Mockito.mock(ExchangeRateService.class);

    private UsdConverter converter = new UsdConverter(service);

    @Test
    public void testConvertToUsd() {
        Mockito.when(service.getUsd()).thenReturn(BigDecimal.valueOf(5));

        BigDecimal result = converter.convertToUsd(BigDecimal.valueOf(2));
        BigDecimal expected = BigDecimal.valueOf(10);

        Assert.assertEquals(expected, result);
    }
}
```

메소드가 예외를 던지게해서 예외처리를 테스트해봐도 됩니다.  또 유용한 부분은 메소드가 테스트 메소드 안에서 일어났는지 보는 것입니다.

모키토에 대해 더 알고싶다면 [full list of features](https://site.mockito.org/)

## Conclusion

Usually, Java class is considered a unit and hence methods are subject to testing. Java community can offer a wide variety of tools to make unit testing easier; we have covered the two most common. **JUnit** framework allows you to prepare and clear the context of tests by annotations and provides API for checking acceptance criteria. **Mockito** framework helps with code isolation if you're testing complex classes and helps manage dependencies.