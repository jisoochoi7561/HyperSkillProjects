## Theory: First steps to coroutines

당신은 이미 여러 task를 처리하는 법을 알고 있다. 그중 하나가 coroutine이다. 여기서 officaill kotlin coroutines 라이브러리를 공부한다.

왜 이것이 언어가 아니라 라이브러리인지, 그리고 어떻게 사용하는지.

## What is `kotlinx.coroutines`

코루틴은 제트브레인이 만든 코틀린라이브러리이다. 이것은 **multiplatform** 이고 코틀린의 주된 사용처 : VM, JS, Native, Android.에서 기능한다.

라이브러리는 동시에 여러일을 하게 해주는 여러 메소들을 제공한다. 구체적인 내용은 나중에 알아보겠다. 메소드를 활용하는 법을 배우기 전에 코루틴의 개념을 이해해야한다.

이 라이브러리는 오픈소스다.마치 코틀린처럼. 이 라이브러리 코드를 [GitHub repository](https://github.com/Kotlin/kotlinx.coroutines)에서 볼 수 있다. 

또 볼만 한 곳은  [official documentation web page](https://kotlinlang.org/docs/reference/coroutines-overview.html) 이다. 여기서 가이드와 예시를 볼 수 있다. 이걸 읽으면 코루틴을 더 이해할 수 있다.



## `kotlinx.coroutines` as a library

중요한 점은 코루틴이 코틀린 스탠다드 라이브러리에 묶여있는게 아니라는 거다 - 이것은 외부 라이브러리 이다. 외부 라이브러리를 써본적 없더라도 겁내지 마라 : 어렵지 않다. 왜 코루틴이 따로 분리됬는가에 대한 이유가 몇가지 있다.그 중 가장 큰 이유 둘:

- 모든 프로그램이 internal concurrency를 가질 필요는 없다.만약 코루틴이 내장되어 있다면 모든 코틀린 프로그램에는 코드가 들어갈 것이고 이건 필요가 없는경우에도 메모리를 잡아먹을 수 있다.
- 어떤 개발자들은 공식적인 코루틴을 쓰고싶어하지 않는다. 코루틴이 plugable이면 , 쉽게 원하는 종류의 코루틴을 적합한 상황에 쓸 수 있다.

다시 말하자면 , 코루틴을 외부라이브러리로 돌리면 우리는 더 자유로워진다. 이러면 아무도 강제로 이것을 쓰지 않아도 된다.

## Maven dependency

`kotlinx.coroutines`라이브러리를 프로젝트에 넣는 가장 쉬운 방법중 하나는 Maven dependency이다. 여기서 우리가 보여주는 코드는 Gradle에서의 코드지만 , 다른 시스템들은 나름대로의 방법을 갖고 있을 것이다. 깃허브에서 이것들을 알아 볼 수 있다.

해야할 것은 간단하다 .다음 코드를 `build.gradle(.kts)`파일에 추가하라:

```kotlin
repository {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
}
```

우선 레포지터리를 알려준다.`jcenter()`대신에 `mavenCentral()`를 쓸 수도 있다 : 코루틴라이브러리는 둘다에 들어가있다.

또한 라이브러리 자체도 선언해줘야 한다. 이 글은 `1.3.8`버전에서 작성되었다.

## Validation

확실히 `kotlinx.coroutines` 이 인스톨됬는지 확인하라. 다음방식으로:

```kotlin
import kotlinx.coroutines.delay

fun main() {
    println(::delay.name)
}
```

모든게 정상이라면 컴파일이 성공하고 `delay`메시지가 출력된다.