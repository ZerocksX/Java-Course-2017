package hr.fer.zemris.bf.demo;

import hr.fer.zemris.bf.utils.Util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * {@link Util#forEach(List, Consumer)} demo
 */
public class ForEachDemo1 {

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Util.forEach(
                Arrays.asList("A", "B", "C"),
                values ->
                        System.out.println(
                                Arrays.toString(values)
                                        .replaceAll("true", "1")
                                        .replaceAll("false", "0")
                        )
        );
    }

}
