package geektime.spring.faq;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class Demo {
    public static void main(String[] args) {
        Stream.of("Foo", "Bar")
                .filter(s -> s.equalsIgnoreCase("foo"))
                .map(String::toUpperCase)
                .forEach(System.out::println);

        Arrays.stream(new String[] { "s1", "s2", "s3" })
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .forEach(System.out::println);
    }
}
