## Theory: Connecting to a database with JDBC

JDBC로 데이터베이스를 가져오기

## What is JDBC

Java DataBase Connectivity (**JDBC**) 은 자바의 API이다. 이것은 relational database에서 정보를 가져오기 위해 만들어졌다.JDBC를 사용하면 데이터베이스에 관계를 틀 수 있고 SQL문장을 실행하거나 쿼리에 대해 받은 대답을 활용할 수 있게 된다.

JDBC API가 자바프로그램과 관계형 데이터베이스를 연결하는 인터페이스를 제공해준다면 , JDBC driver는 이 인터페이스들을 구체적인 database management system (DBMS)에 적용한다. JDBC드라이버는 데이터베이스에 연결하고 애플리케이션과 데이터베이스 사이에 프로토콜을 적용해 쿼리와 대답들이 오고가게 해준다. 이런 것들이 여러 DBMS들에 대해 같은 방식으로 JDBC APU를 쓰게해준다(여러가지 driver로 적용하면 되니까)

## Establishing a connection

프로그램과 데이터베이스의 연결을 만들기 위해선 `DataSource` 인터페이스를 구현한 클래스의 인스턴스가 필요하다. 말했듯이 JDBC driver가 JDBC API의 구현을 담당하고, `DataSource` 인터페이스는 특정한 DBMS를 표현한다.

연결이 만들어지기 전에 데이터베이스의 url을 특정해야 한다.**database URL**은 데이터베이스에 대한 정보를 담은 문자열이다 : 데이터베이스의 이름,경로,특성 등.

각각의 DBMS는 나름의 규칙과 문법으로 URL을 만든다. 대부분의 DBMS는 데이터베이스이름,호스트이름,포트넘버,유저권한을 요구한다.

마침내, 우리는 `DataSource.getConnection`메소드를 실행한다. 이것은 `Connection` 객체를 리턴하는데 이것이 바로 데이터베이스와의 연결이다. 커넥션이 만들어지면 데이터베이스를 다룰 수 있다.

실제로 해보자. 우리의 프로그렘을 **SQLite** DBMS와 연결해보자. 우선, SQLite를 다운로드하고 설치해라. 이것은 매우 컴팩트한 DBMS라서 실제로 자주 쓰이진 않지만 학습용으론 훌륭하다.

다음 단계는 SQLite JDBC driver를 추가해주는 것이다. 그러려면 Gradle을 이용해야 한다. build.gradle 파일에 다음 디펜던시를 추가해주라.

```json
dependencies {
    compile group:'org.xerial', name:'sqlite-jdbc', version:'3.30.1'
}
```



SQLite database URL스트링을 먼저 만들자:

```java
public class CoolJDBC {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:path-to-database";
    }
}
```



`path-to-database` 은 데이터베이스의 절대주소나 상대주소이다. 예를들어 당신이 SQLite를 C:\sqlite folder에 설치했고 fruits.db파일을 쓴다면 URL은 `"jdbc:sqlite:C:/sqlite/fruits.db"`. 만약 데이터베이스파일이 프로젝트폴더에 있다면 URL은`"jdbc:sqlite:fruits.db"`.

이 이후로는 다음링크가 더 훌륭하다고 생각하고, mySql을 다루고있으므로 링크로 대체한다[링크](https://krksap.tistory.com/471?category=690878)

