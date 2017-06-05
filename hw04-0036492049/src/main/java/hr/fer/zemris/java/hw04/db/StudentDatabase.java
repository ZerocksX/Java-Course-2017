package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple database model that takes data as lines of entries and inputs {@link StudentRecord} in a list<br/>
 * Functions are find by jmbag with O(1) complexity and filter that takes {@link QueryFilter} and filters student list<br/>
 *
 * @author Pavao Jerebić
 */
public class StudentDatabase {
    /**
     * Data
     */
    private List<String> data;
    /**
     * List of students
     */
    private List<StudentRecord> students;
    /**
     * {@link StudentRecord#jmbag},Index hash table for O(1) complexity for finding student by jmbag
     */
    private SimpleHashtable<String, Integer> studentIndexes;

    /**
     * Constructor that takes data as list of lines with format jmbag\tlastName\tfirstName\tfinalGrade
     *
     * @param data given data
     * @throws IllegalArgumentException if data is null or wrong format
     */
    public StudentDatabase(List<String> data) {
        if (data == null) {
            throw new IllegalArgumentException("data can not be null");
        }
        this.data = data;
        students = new ArrayList<>();
        studentIndexes = new SimpleHashtable<>();
        int i = 0;
        for (String s : data) {

            int cnt = 0;
            char[] string = s.toCharArray();

            try {

                String jmbag = nextString(string, cnt);
                cnt += jmbag.length() + 1;

                String surname = nextString(string, cnt);
                cnt += surname.length() + 1;

                String name = nextString(string, cnt);
                cnt += name.length() + 1;

                String gradeAsString = nextString(string, cnt);

                Integer grade = Integer.parseInt(gradeAsString);

                students.add(new StudentRecord(jmbag, name, surname, grade));
                studentIndexes.put(jmbag, i);
                i++;

            } catch (Exception e) {
                throw new IllegalArgumentException("Data is not properly formatted\n" + e.getMessage());
            }
        }
    }

    /**
     * Helping method to process given database.txt file
     *
     * @param data lina as char array
     * @param cnt  current position
     * @return next string
     */
    private String nextString(char[] data, int cnt) {
        StringBuilder sb = new StringBuilder();
        while (cnt < data.length && data[cnt] != '\t') {
            sb.append(data[cnt++]);
        }
        return sb.toString();
    }

    /**
     * Returns Student for given jmbag<br/>
     * O(1) complexity
     *
     * @param jmbag jmbag
     * @return student with given jmbag
     */
    public StudentRecord forJMBAG(String jmbag) {
        Integer index = studentIndexes.get(jmbag);
        if (index == null) {
            throw new IllegalArgumentException("Can not find given jmbag");
        }
        return students.get(index);
    }

    /**
     * Filters student list using {@link QueryFilter}
     *
     * @param filter filter
     * @return list of students where queryFilter is accepted
     */
    public List<StudentRecord> filter(IFilter filter) {
        return students.stream()
                .filter(filter::accepts)
                .collect(Collectors.toList());
    }
}
