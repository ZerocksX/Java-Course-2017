package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.db.parser.QueryParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class StudentDatabaseTest {
    StudentDatabase db;

    @Before
    public void setUp() throws Exception {
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
        db = new StudentDatabase(lines);
        sc.close();
    }

    @Test
    public void testForJMBAG() throws Exception {
        StudentRecord r = db.forJMBAG("0000000001");
        assertEquals("Marin",r.getFirstName());
        assertEquals("Akšamović",r.getLastName());
        assertEquals(2L,r.getFinalGrade().longValue());
    }

    @Test
    public void testFilter() throws Exception {
        QueryParser parser = new QueryParser("lastName LIKE \"Bo*\"");
        List<StudentRecord> list = db.filter(new QueryFilter(parser.getQuery()));
        assertEquals(2,list.size());
        assertEquals("0000000003",list.get(0).getJmbag());
        assertEquals("0000000004",list.get(1).getJmbag());
    }

}