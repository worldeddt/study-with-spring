# call by value , call by reference

원시 타입, 참조 타입의 메커니즘을 구현해 보기로 함.


### immutable

String, Boolean, Integer, Double, Float, Long

### mutable

List, HashMap, ArrayList



## 결론

기본적으로 primitive 계열인 원시 타입인 int, long, float 등의 자료형 있는 반면,
Object 를 최상단에 두고 있는 참조형 타입인 String, Integer, Long, Float 등이 있다

이 둘의 차이 점은 변수명과 밸류를 어디에 배치 하느냐 차이가 있다

원시 타입은 변수와 밸류 모두 stack 영역에 자리 잡고 반대로 참조형은 변수는 stack 영역에 밸류와 타입은
heap 영역에 자리 잡게 된다. 
e.g. ArrayList 타입에 경우 변수명은 그대로 stack 영역에 나머지 순차적인 값들의 배열은 heap 영역에
아래와 같이
Heap 
 
List
|1|
|2|
|3|

형식으로 자리 잡게 된다. 

여기서 call by value, call by reference 를 따지 자면 immutable 인지 mutable 인지로 나뉘게 된다

보통 String을 포함함 Float, int, Integer 등은 immutable 계열로 함수 매개변수로 변수를 넘겨도

본래 변수의 값은 변하지 않고 매개 변수로 넘긴 값대로 별도로 stack, heap 영역에 저장 공간이 생기는 형태이다.

List, HashMap, ArrayList, int[] 등의 컬렉션 계열은 mutable 계열로 매개변수로 변수를 넘기면 본래에 

변수 값까지 변경되어 반영된다.

## 최종 결론

메모리에 어떻게 자리 잡느냐 와 메소드로 매개변수를 넘길때 어떻게 넘겨지느냐로 이 분야가 나뉜다고 이해하면 될 듯.

이유 >  List, ArrayList, HashMap, array 등 컬렉션 및 배열 형태가 아니면 변수를 복사하는 형태라 값이 변경되지 아니 한다.