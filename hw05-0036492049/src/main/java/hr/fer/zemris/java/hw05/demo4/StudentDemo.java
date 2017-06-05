package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Program that takes file in src/main/resources/studenti.txt and reads it and creates a list of {@link StudentRecord}s<br/>
 * Filters and calculates some data from that input and displays it
 *
 * @author Pavao Jerebić
 */
public class StudentDemo {
    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("src/main/resources/studenti.txt"));
        } catch (IOException e) {
            System.out.println("Error opening file at src/main/resources/studenti.txt");
            System.exit(-1);
        }
        List<StudentRecord> records = null;
        try {
            records = convert(lines);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }

        System.out.printf("Broj bodova vise od 25:%n");
        System.out.println(vratiBodovaViseOd25(records));

        System.out.printf("%nBroj odlikasa:%n");
        System.out.println(vratiBrojOdlikasa(records));

        System.out.printf("%nLista odlikasa:%n");
        vratiListuOdlikasa(records).forEach(System.out::println);

        System.out.printf("%nSortirana lista odlikasa:%n");
        vratiSortiranuListuOdlikasa(records).forEach(System.out::println);

        System.out.printf("%nPopis nepolozenih:%n");
        vratiPopisNepolozenih(records).forEach(System.out::println);

        System.out.printf("%nStudenti po ocjenama:%n");
        razvrstajStudentePoOcjenama(records).forEach((grade, students) -> {
            System.out.print(grade + " -> ");
            System.out.println(Arrays.toString(students.toArray()));
        });

        System.out.printf("%nBroj studenata po ocjenama:%n");
        vratiBrojStudenataPoOcjenama(records).forEach((grade, number) -> {
            System.out.print(grade + " -> ");
            System.out.println(number);
        });

        System.out.printf("%nStudenti po prolazu:%n");
        razvrstajProlazPad(records).forEach((grade, students) -> {
            System.out.print((grade ? "prosao" : "pao") + " -> ");
            System.out.println(Arrays.toString(students.toArray()));
        });
    }

    /**
     * Converst list of strings to list of student records
     * @param lines lines
     * @return list of student records
     */
    private static List<StudentRecord> convert(List<String> lines) {
        List<StudentRecord> list = new ArrayList<>();
        for (String s : lines) {

            int cnt = 0;
            char[] string = s.toCharArray();

            try {

                String jmbag = nextString(string, cnt);
                cnt += jmbag.length() + 1;

                String surname = nextString(string, cnt);
                cnt += surname.length() + 1;

                String name = nextString(string, cnt);
                cnt += name.length() + 1;

                String MIAsString = nextString(string, cnt);
                cnt += MIAsString.length() + 1;
                Double MI = Double.parseDouble(MIAsString);

                String ZIAsString = nextString(string, cnt);
                cnt += ZIAsString.length() + 1;
                Double ZI = Double.parseDouble(ZIAsString);

                String LABAsString = nextString(string, cnt);
                cnt += LABAsString.length() + 1;
                Double LAB = Double.parseDouble(LABAsString);

                String gradeAsString = nextString(string, cnt);
                Integer grade = Integer.parseInt(gradeAsString);

                list.add(new StudentRecord(jmbag, surname, name, MI, ZI, LAB, grade));

            } catch (Exception e) {
                throw new IllegalArgumentException("Data is not properly formatted\n" + e.getMessage());
            }
        }
        return list;
    }

    /**
     * Helping method to process given database.txt file
     *
     * @param data lina as char array
     * @param cnt  current position
     * @return next string
     */
    private static String nextString(char[] data, int cnt) {
        StringBuilder sb = new StringBuilder();
        while (cnt < data.length && data[cnt] != '\t') {
            sb.append(data[cnt++]);
        }
        return sb.toString();
    }

    /**
     * Returns number of students which had more than 25 point sum from finals, midterms and lab. exercises
     *
     * @param list list of student records
     * @return number of students with sum of point greater than 25
     */
    private static long vratiBodovaViseOd25(List<StudentRecord> list) {
        return list.stream().filter(studentRecord ->
                studentRecord.getScoreExercise()
                        + studentRecord.getScoreMidterm()
                        + studentRecord.getScoreFinal()
                        > 25)
                .count();
    }

    /**
     * Returns the number of students who had 5 as their final grade
     *
     * @param list list of student records
     * @return number of students who had 5 as their final grade
     */
    private static long vratiBrojOdlikasa(List<StudentRecord> list) {
        return list.stream().filter(studentRecord -> studentRecord.getGrade() == 5)
                .count();
    }

    /**
     * Returns a list of students who had 5 as their final grade
     *
     * @param list list of student records
     * @return a list of students who had 5 as their final grade
     */
    private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> list) {
        return list.stream().filter(studentRecord -> studentRecord.getGrade() == 5)
                .collect(Collectors.toList());
    }

    /**
     * Returns a sorted list of students who had 5 as their final grade
     *
     * @param list list of student records
     * @return a sorted list of students who had 5 as their final grade
     */
    private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> list) {
        return list.stream().filter(studentRecord -> studentRecord.getGrade() == 5)
                .sorted(Collections.reverseOrder(
                        Comparator.comparing(studentRecord -> studentRecord.getScoreExercise()
                            + studentRecord.getScoreMidterm()
                            + studentRecord.getScoreFinal())
                        )
                )
                .collect(Collectors.toList());
    }

    /**
     * Returns a sorted list of students who had 1 as their final grade
     *
     * @param list list of student records
     * @return a sorted list of students who had 1 as their final grade
     */
    private static List<StudentRecord> vratiPopisNepolozenih(List<StudentRecord> list) {
        return list.stream().filter(studentRecord -> studentRecord.getGrade() == 1)
                .sorted(Comparator.comparing(studentRecord -> Long.valueOf(studentRecord.getJmbag())))
                .collect(Collectors.toList());
    }

    /**
     * Returns a map of grades and students
     *
     * @param list list of student records
     * @return map of grades and students
     */
    private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> list) {
        return list.stream().collect(Collectors.groupingBy(
                StudentRecord::getGrade)
        );
    }

    /**
     * Returns a map of grades and number of students who had that grade
     *
     * @param list list of student records
     * @return map of grades and number of students who had that grade
     */
    private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> list) {
        return list.stream().collect(Collectors.toMap(
                StudentRecord::getGrade,
                studentRecord -> 1,
                (i1, i2) -> i1 + 1)
        );
    }

    /**
     * Returns map of positive and negative grades  and students
     * @param list list of student records
     * @return map of positive and negative grades  and students
     */
    private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> list) {
        return list.stream().collect(Collectors.partitioningBy(
                s -> s.getGrade() > 1)
        );

    }


}
