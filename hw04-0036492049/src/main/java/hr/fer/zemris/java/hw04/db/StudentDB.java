package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.db.parser.QueryParser;
import hr.fer.zemris.java.hw04.db.parser.QueryParserException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Program inputs database from src/main/resources/database.txt and then reads queries from standard input and writes results on standard output<br/>
 * Query format is {query} {fieldName} {operator} {string literal} ["and"  and then another query can follow]. Also described in {@link QueryParser}<br/>
 * If query is malformed program will write appropriate message and continue reading <br/>
 * Program ends when user inputs "exit"
 *
 * @author Pavao Jerebić
 */
public class StudentDB {

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Scanner sc = null;
        try {
            sc = new Scanner(Paths.get("src/main/resources/database.txt"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        StudentDatabase db = new StudentDatabase(lines);
        sc.close();
        sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (sc.hasNext()) {
                String value = sc.next();
                if (value.equals("exit")) {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                } else if (value.equals("query")) {
                    String query = sc.nextLine();
                    QueryParser parser;
                    try {
                        parser = new QueryParser(query);
                        if (parser.getQuery().size() == 0) {
                            System.out.println("Please input a full query in one line");
                            continue;
                        }
                    } catch (QueryParserException ex) {
                        System.out.format("Query not formed properly%n%s%n", ex.getMessage());
                        continue;
                    }
                    if (parser.isDirectQuery()) {
                        StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
                        prettyPrintStudentList(new ArrayList<StudentRecord>() {{
                            add(r);
                        }});
                    } else {
                        List<StudentRecord> list = db.filter(new QueryFilter(parser.getQuery()));
                        prettyPrintStudentList(list);
                    }
                } else {
                    System.out.println("Keyword not supported");
                }
            } else {
                System.out.println("Nothing to read");
                sc.close();
                return;
            }
        }
    }

    /**
     * Used to output in given format
     *
     * @param list list of students to print
     */
    private static void prettyPrintStudentList(List<StudentRecord> list) {

        int maxJMBAG = 0, maxLastName = 0, maxFirstName = 0, maxFinalGrade = 0;
        for (StudentRecord r : list) {
            maxJMBAG = Math.max(maxJMBAG, r.getJmbag().length());
            maxLastName = Math.max(maxLastName, r.getLastName().length());
            maxFirstName = Math.max(maxFirstName, r.getFirstName().length());
            int k = 1, g = r.getFinalGrade();
            while (g / 10 > 0) k++;
            maxFinalGrade = Math.max(maxFinalGrade, k);
        }
        if (list.size() > 0) {
            System.out.format("+%s+%s+%s+%s+%n",
                    addNCharacters(maxJMBAG + 2, '='),
                    addNCharacters(maxLastName + 2, '='),
                    addNCharacters(maxFirstName + 2, '='),
                    addNCharacters(maxFinalGrade + 2, '='));

            for (StudentRecord r : list) {
                System.out.format("| %s%s | %s%s | %s%s | %s%s |%n",
                        r.getJmbag(), addNCharacters(maxJMBAG - r.getJmbag().length(), ' '),
                        r.getLastName(), addNCharacters(maxLastName - r.getLastName().length(), ' '),
                        r.getFirstName(), addNCharacters(maxFirstName - r.getFirstName().length(), ' '),
                        r.getFinalGrade().toString(), addNCharacters(maxFinalGrade - r.getFinalGrade().toString().length(), ' ')
                );
            }

            System.out.format("+%s+%s+%s+%s+%n",
                    addNCharacters(maxJMBAG + 2, '='),
                    addNCharacters(maxLastName + 2, '='),
                    addNCharacters(maxFirstName + 2, '='),
                    addNCharacters(maxFinalGrade + 2, '='));
        }
        System.out.printf("Records selected: %d%n", list.size());
    }

    /**
     * Crates a string of n c characters
     *
     * @param n number of characters
     * @param c character to repeat n times
     * @return string of n time c char
     */
    private static String addNCharacters(int n, char c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
