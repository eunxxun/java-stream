# Java8 Stream Test

### Stream이란?
* 자바8에서 추가된 기능으로 함수형 인터페이스인 람다(lambda)를 활용할 수 있는 기술이다.
* 예전에는 배열이나 컬렉션 반복문을 순회하며 요소를 하나씩 꺼내 여러 코드를 섞어 작성했다면 스트림과 람다를 이용하여 코드의 양을 줄이고 간결하게 작성할 수 있다.
* 스트림을 이용하면 멀티 스레드 환경에 필요한 코드를 작성하지 않고도 데이터를 병렬로 처리할 수 있다. (기존엔 synchronized와 같은 동기화코드를 관리해야 한다.)

### Stream과 Collection의 차이
 - 스트림과 컬렉션 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다. 둘의 차이는 **"언제"** 계산하느냐 이다.
    - Collection : 자료구조가 포함하는 모든 값을 메모리에 저장하는 자료구조라 어떤 계산을 한 값을 저장하기 위해서는 모든 요소들이 컬렉션에 추가되기 전에 미리 계산이 되어야 한다.
      또, 컬렉션 인터페이스를 사용하기 위해서 외부 반복을 사용하여 사용자가 직접 요소를 반복해야한다.(for-each사용)
    - Stream : 이론적으로 요청할때만 요소를 계산하는 고정된 자료구조. 그래서 스트림에 요소를 추가하거나 제거하는 작업은 할 수 없다.
      내부 반복을 사용하기 때문에 추출할 요소만 선언하면 알아서 반복을 처리한다.
    - 컬렉션은 모든 영화 데이터가 저장되어있는 DVD와 같고 스트림은 그때그때 필요한 부분을 불러 재생하는 유튜브와 같다.
    - 스트림 특징 : 데이터를 변경하지 않고 새로운 결과를 스트림에 저장,  필요한 데이터만 메모리에 로드해 처리, Iterator처럼 데이터에 1번만 접근한다.

### 생성작업
- 컬렉션이나 배열 등으로부터 스트림을 생성
1. Collection으로 생성
```aidl
    List<String> list = List.of("test","play");
    Stream<String> stream = list.stream();
```
2. Array로 생성
```aidl
    String[] arr = new String[]{"test", "play"};
    Stream<String> stream = Arrays.stream(arr);
    
    //0번 인덱스만 선택(closed range)
    Stream<String> specificStream = Arrays.stream(arr,0,1);
    
    //"test" 출력
    specificStream.forEach(System.out::println);
```
3. 병렬 스트림 생성
```aidl
    List<String> list = List.of("test","play","...");
    Stream<String> stream = list.parallelStream();
```
4. 기본 타입에 특화된 스트림 생성
    
    - 오토박싱과 언박싱의 비효율적 측면을 줄이기 위해 기본 타입에 특화된 스트림을 사용할 수 있다.
```aidl
    IntStream intStream = IntStream.range(0,3); //0, 1, 2
    IntStream cloxedIntStream = IntStream.rangeClosed(0,3); //0, 1, 2, 3
    LongStream longStream = LongStream.range(0,3); //0, 1, 2
    DoubleStream doubleStream = DoubleStream.of(0,3); // 0.0, 0.3
```
5. 파일(Files)로 생성

    - list 메서드는 path 스트림을, lines 메서드는 파일 내 각 라인을 문자열 스트림으로 생성한다.
```aidl
    Path path = Paths.get("~");
    Stream<Path> list = Files.list(path);
    
    Path filePath = Paths.get("~.txt");
    Stream<String> lines = Files.lines(path);
```
6. Stream.builder()로 생성
```aidl
    Stream<String> stream = Stream.<String>builder()
        .add("test").add("play").build();
```
등등..

### 중간연산
- 스트림을 필터링하거나 요소를 알맞게 변환

1. filter 메서드로 필터링
    
    - filter 메서드의 인자인 Predicate<T> 인터페이스는 test라는 추상 메서드를 정의하는데, 이는 제네릭 형식의 객체를 인수로 받아 boolean 값을 반환한다.
```aidl
    List<String> list = List.of("park","sssuuny");
    list.stream().filter(s -> s.length() > 5);
    // "sssuuny"
    
    // without lambda expression
    list.stream().filter(new Predicate<String>() {
        @Override
        public boolean test(String s){
            return s.length() > 5;
        }
    });
```

2. map 메서드로 특정 형태로 변환
```aidl
    List<String> list = List.of("test", "play");
    list.stream().map(s -> s.toUpperCase());
    // "TEST", "PLAY"
    
    // without lambda expression
    list.stream().map(new Function<String, String>() {
        @Override
        public String apply(String s) {
            return s.toUpperCase();
        }
    });
```
3. flatMap 메서드로 단일 스트림 변환

   - flatMap 메서드는 중첩된 구조를 한단계 없애고 단일 원소 스트림으로 만들어준다.
```aidl
    List<String> list1 = List.of("test","play");
    List<String> list2 = List.of("park","sssunny");
    
    List<String> streamByList = combinedLisst.stream()
        .flatMap(list -> list.stream())
        .collect(Collectors.toList());
        
    // test, play, park, sssunny
    System.out.println(streamByList);
    
    // 2차원배열
    Stream[][] arrs = new String[][]{
        {"test","play"},{"park","sssunny"}
    };
    
    List<String> streamByArrs = Arrays.stream(arrs)
        .flatMap(arr -> Arrays.stream(arr))
        .collect(Collectors.toList());
        
    // test, play, park, sssunny
    System.out.println(streamByArrs); 
    
```

4. distinct 메서드로 중복 제거
```aidl
    IntStream stream = Arrays.stream(new int[]{1,2,2,3,3});
    
    // 1,2,3
    stream.distinct()
        .forEach(System.out::println);
```

5. sorted 메서드로 정렬하기
```aidl
    // 1,2,3
    List.of(2,1,3).stream()
        sorted();
        
    // 3,2,1
    List.of(1,2,3).stream()
        .sorted(Comparator.reverseOrder());
        
    // 2,1,0
    IntStream.range(0,3)
        .boxed() // 기본형특화스트림의 경우 boxed 메서드 이용해 객체 스트림으로 변환 후 사용
        .sorted(Comparator.reverseOrder());
```
### 단말연산
- 가공한 스트림을 결과로 만들어낸다.

1. 순회(iterate)

   - forEach 메서드를 사용해 스트림을 순회할 수 있다.
```aidl
   List<Integer> list = List.of(3, 2, 1, 5, 7);
   list.stream().forEach(System.out::println);
```
   - 병렬 스트림 사용시 순서를 보장할 수 없다. 그래서 forEachOrdered 메서드를 사용
```aidl
   List<Integer> list = List.of(3, 1, 2);

   // 매 실행마다 출력 결과가 동일하지 않다.
   list.parallelStream().forEach(System.out::println);
   
   // 매 실행마다 동일한 출력 결과
   list.parallelStream().forEachOrdered(System.out::println);
```
2. 결과 합치기(reduce)

   - reduce 연산을 이용해 모든 스트림 요소를 처리해 결과를 구할 수 있다.

3. 결과 모으기(Collect)

   - 스트림을 List, Set 그리고 Map과 같은 다른 형태의 결과로 변환해준다.
   - Collectors.toList() : 작업 결과를 리스트로 반환
   - 숫자 값의 합, 평균 등 구하기
   ```aidl
      // name 길이의 합 구하기
      Integer summingName = list.stream()
        .collect(Collectors.summingInt(s -> s.getName().length()));
      
      // mapToInt 메서드로 칼로리(cal) 합 구하기
      int sum = list.stream().mapToInt(Food::getCal).sum();
      
      // 평균 구하기: averagingInt
      Double averageInt = list.stream()
        .collect(Collectors.averagingInt(Food::getCal));
      
      // 평균 구하기: averagingDouble
      Double averageDouble = list.stream()
        .collect(Collectors.averagingDouble(Food::getCal));
   ```
   - Collectors.joining() : 스트림 연산 결과를 문자열로 만들기
   - Collectors.toMap() : Map으로 결과 모으기