package optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        List<Foo> list = new ArrayList<>();
        list.add(new Foo("foo01", 1, new Specific("제목1", "2021")));
        printFooSpecTitle(list);
        list.get(0).setSpecific(null);
        printFooSpecTitle(list);

        TreeMap<Integer, Score> map = new TreeMap<>();
        map.put(1, new Score("1등"));
        map.put(2, new Score("2등"));
        map.put(4, new Score("4등"));

        //Optinal 아닐 때
        /*Score searchScore = map.get(3); // null 발생
        if(searchScore == null) new Exception("값이 없습니다.");
        else System.out.println("Score : " + searchScore);*/

        IntStream.rangeClosed(1,5).forEach(x -> {
            //orElseGet : null 일때만 불림
            Score aNull = Optional.ofNullable(map.get(x))
                    .orElseGet(() -> {
                        return new Score("비어있는 등수");
                    });

            //orElse : null이 아니여도 불림
            Score aNull2 = Optional.ofNullable(map.get(x))
                    .orElse(new Score("비어있는 등수"));

            System.out.println(aNull.getScore());
        });
    }

    public static void printFooSpecTitle(List<Foo> list){

        //Optional : null이 아닌 값을 포함하거나 포함하지 않을 수 있는 컨테이너 객체
        //if-else로 null 체크를 해도 되는거 아니냐 -> Optional은 주로 "결과 없음"을 나타내야하는 명확한 필요성이 있고 null을 사용하면 오류가 발생할 가능성이있는 메서드 반환 유형으로 사용하기위한 것입니다. 유형이 Optional 인 변수는 자체적으로 null이 아니어야합니다. 항상 Optional 인스턴스를 가리켜야합니다

        list.forEach(x -> {
            System.out.println(Optional.ofNullable(x)
                    .map(Foo::getSpecific) //map : 요소들을 특정 조건에 해당하는 값으로 변환
                    .map(Specific::getTitle)
                    .orElseGet(() -> {
                        System.out.println("null이라 새로 생성");
                        return "";
                    })
            );
        });
    }
}
