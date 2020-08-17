## Theory: Concurrency and parallelism         병행과 병렬

 아시다시피 workflow(컴퓨터가 일하는 절차)에는 3종류가 있습니다 : synchronous, asynchronous, and parallel.synchronous workflow는 일을 차례대로 처리하고, async와 parallel은 동시에 여러일을 합니다. async와 parallel방식으로 일하는 걸 각각 concurrency와 parallelism이라고 합니다. 피자집의 예시를 보며 공부합시다.

![img](https://ucarecdn.com/6e675251-e62e-4223-b467-f1fdbf07a640/)

보통 피잣집은 커다란 전용오븐을 가지고 있습니다. 점원들은 여러피자를 동시에 굽기위해 그걸 공유합니다. 큰 오븐들은 비싸지만 생산적이죠. 이 오븐들은 시간이겹치는 여러 손님을 대응하게 해줍니다.

## Critical section and synchronization

오븐을 봅시다. 오븐은 크지만, 무한하진 않습니다. 가끔은 빈공간이 없어서 새 피자를 넣을 수 없습니다. 따라서 점원들은 줄을 서서 빈공간이 나오기를 기다려야하죠. 이것이 오븐에 대한 synchronizing상황입니다. 이 오븐은  이 상황에서 critical section(임계구역)입니다.

**Critical section**은 일을 하는데 필요한 한정된 자원입니다. 이걸 쓰지 않으면 일을 할 수가 없다는점에서 *critical*입니다. 따라서 점원들은 일을 마치려면 *반드시*  이 리소스에 대한 접근허가를 받아야합니다.

critical section에 대한 접근을 제어하는 일을 **synchronization**(동기화) 이라 합니다. 

## Concurrency

각각의 점원은 주문을 받고 오븐에 피자를 넣습니다. 그리고, 피자가 구워지는 동안, 점원은 다른 주문을 받거나 준비된 피자를 내놓습니다. 각각의 점원들은 몇명의 손님을 상대하며 여러역할(돈받기,빵굽기,빵주기)를 각각 다른시점에 합니다. 이런 접근을**time-slicing**이라 하는데,이것이 concurrency 또는 multitasking with time-slicing입니다.

**Concurrency** 는 한정된 공유자원을 가지고 정해진 계획없이 여러역할을 동시에 할 수 있는 것입니다. 

Concurrency는 한정된 공유자원(critical section)과 거기에 접근하려 경쟁하는 worker들이 있을 때 성립합니다.concurrency는 이걸 가지는게 중요합니다:

- 할 일이 하나이상
- critical section이 하나이상

하나의 일을 위한 concurrency는 없습니다. 반드시 두개 이상의 일을 해야하고, 따라서 concurrency의 또 다른 이름은 **multitasking입니다**.

## Parallelism

피자집 점원들은 비슷한일을 하지만, 담당하는 손님들은 다릅니다. 그들은 독립적으로 일을 하고, 이것이 Parallelism의 예입니다.

**Parallelism**은 서로다른 실행자들이 동시에 임무를 진행하는 겁니다. 여러가지 독립적인 임무일 수도 있고 거대한 임무일 수도 있습니다. 거대한 임무는 독립적인 임무들로 나뉘어서 실행자들에게 분배됩니다.

Parallelism은 어떤면에선critical section없는 concurrency입니다. 만약 공유되는 자원이 충분해서 모든 사용자가 쓸 수 있다면,프로세스는 parallel하게 진행됩니다. 공유자원에 접근하려고 순서를 기다리지 않아도 되죠.만약에 우리가 여러 실행자들에게 분배해서 모든 실행자가 단 하나의 할 일만 있고, critical section은 없으니, concurrency는 사라지고, parallelism이 등장합니다. 임무들은 동시에 실행되고, 실행자들은 리소스에 접근하려 경쟁하지 않아도 되죠.

실행자가 여럿인 것은 parallelism에 매우 중요한 조건입니다. 많은 실행자들이 실행을 병행하기 때문에, parallelism의 또 다른 이름은 **multiprocessing**입니다.

## Concurrency and parallelism: together and apart

Concurrency는 한명의 실행자나 여러명의 실행자에게 나타날 수 있습니다. 반대로 parallelism은 반드시 여러명의 실행자가 있어야 합니다. 병행과 병렬은 동시에 일어날 수 있습니다. 서로 바뀔 수도 있습니다. 실제로, concurrency와 parallelism의 온갖 결합이 가능합니다.

예를 들면, 여러 parallel한 일의 결과를 하나로 합치려면, critical section이 등장합니다. parallel task 결과를 하나씩 접근하는 순서가 그것입니다. 각각의 task들이 끝나면, concurrent한 방식으로 그 결과를 제공합니다.

## Conclusion

병렬과 병행은 분명 다릅니다

- Concurrency은 여러일을 공유되는 자원으로 한번에 하는 것입니다.
- Parallelism은 여러 실행자가 많은 일을 병행하는 것입니다.
- Concurrency 와 parallelism 은 자주 엮여있습니다. 서로 변환될 수 있습니다.
- 어느방식의 결합이든 등장 가능 합니다.