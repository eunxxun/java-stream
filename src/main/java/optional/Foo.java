package optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Foo {
    private String dataString;
    private Integer dataInteger;
    private Specific specific;
}
