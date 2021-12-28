package example;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ex {
    public static void main(String[] args) {
        // List<V> to Map<K, V>
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("짱구", 23, "010-1234-1234"));
        personList.add(new Person("유리", 24, "010-4444-1111"));
        personList.add(new Person("철수", 29, "010-3333-1234"));
        personList.add(new Person("맹구", 25, null));

        // Function.identity는 t -> t, 항상 입력된 인자(자신)를 반환
        Map<String, Person> personMap = personList.stream()
                .filter(p -> p.getAge() > 24)
                .collect(Collectors.toMap(Person::getName, Function.identity()));

        personMap.forEach((k,v) -> System.out.println("name: " + k + " age: " + v.getAge()));

        // Collectors.toMap 수행시 하나의 키에 매핑되는 값이 2개 이상인 경우 IllegalStateException 예외 발생
        Map<Integer, Person> personMap2 = personList.stream()
                .collect(Collectors.toMap(
                        o -> o.getAge(),
                        Function.identity(),
                        (oldValue, newValue) -> newValue)); // 중복되는 경우 새 값으로 넣는다.

        personMap2.entrySet().stream()
                .forEach(entry -> System.out.println("age: " + entry.getKey()+" name: " + entry.getValue().getName()));

        // 중복 키를 허용하고 싶을 때 Collectors.groupingBy 사용
        Map<Integer, List<Person>> duplicatedMap = personList.stream()
                .collect(Collectors.groupingBy(Person::getAge));

        // null 제외하기
        Stream<String> stream = Stream.of("철수", "훈이", null, "유리", null);
        List<String> filteredList = stream.filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 철수 훈이 유리
        filteredList.forEach(e -> System.out.print(e + " "));
        System.out.println();

        // 단일 컬렉션 만들기
        String[][] names = new String[][]{
                {"짱구", "철수"}, {"훈이", "맹구"}
        };

        // to List 짱구 철수 훈이 맹구
        List<String> list = Arrays.stream(names)
                .flatMap(Stream::of)
                .collect(Collectors.toList());

        list.forEach(e -> System.out.print(e +" "));
        System.out.println();

        // to array 짱구 철수 훈이 맹구
        String[] flattedNames = Arrays.stream(names)
                .flatMap(Stream::of).toArray(String[]::new);

        Arrays.stream(flattedNames).forEach(e -> System.out.print(e +" "));
    }
}
