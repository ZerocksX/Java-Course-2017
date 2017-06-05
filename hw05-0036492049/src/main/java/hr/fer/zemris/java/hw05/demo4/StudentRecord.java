package hr.fer.zemris.java.hw05.demo4;

/**
 * Representation for student
 * @author Pavao Jerebić
 */
public class StudentRecord implements Comparable<StudentRecord>{
    /**
     * JMBAG
     */
    private String jmbag;
    /**
     * Last name
     */
    private String lastName;
    /**
     * First Name
     */
    private String firstName;
    /**
     * Score on midterm exam
     */
    private Double scoreMidterm;
    /**
     * Score on final exam
     */
    private Double scoreFinal;
    /**
     * Score on laboratory exercise
     */
    private Double scoreExercise;
    /**
     * Final grade
     */
    private Integer grade;

    /**
     * Constructor for all fields
     *
     * @param jmbag         jmbag
     * @param lastName      last name
     * @param firstName     first name
     * @param scoreMidterm  score on midterm exam
     * @param scoreFinal    score on final exam
     * @param scoreExercise score on laboratory exercise
     * @param grade         final grade
     * @throws IllegalArgumentException if any argument is null
     */
    public StudentRecord(String jmbag, String lastName, String firstName, Double scoreMidterm, Double scoreFinal, Double scoreExercise, Integer grade) {

        if (jmbag == null
                || lastName == null
                || firstName == null
                || scoreMidterm == null
                || scoreFinal == null
                || scoreExercise == null
                || grade == null) {
            throw new IllegalArgumentException("All arguments must not be null");
        }

        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.scoreMidterm = scoreMidterm;
        this.scoreFinal = scoreFinal;
        this.scoreExercise = scoreExercise;
        this.grade = grade;
    }

    /**
     * Getter for jmbag
     *
     * @return jmbag
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Getter for last name
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for first name
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for midterm score
     *
     * @return midterm score
     */
    public Double getScoreMidterm() {
        return scoreMidterm;
    }

    /**
     * Getter for final score
     *
     * @return final score
     */
    public Double getScoreFinal() {
        return scoreFinal;
    }

    /**
     * Getter for exercise score
     *
     * @return exercise score
     */
    public Double getScoreExercise() {
        return scoreExercise;
    }

    /**
     * Getter for final grade
     *
     * @return final grade
     */
    public Integer getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentRecord)) return false;

        StudentRecord that = (StudentRecord) o;

        return jmbag.equals(that.jmbag);
    }

    @Override
    public int hashCode() {
        return jmbag.hashCode();
    }


    @Override
    public int compareTo(StudentRecord studentRecord) {
        return Long.valueOf(this.getJmbag()).compareTo(Long.valueOf(studentRecord.getJmbag()));
    }

    @Override
    public String toString() {
        return "StudentRecord{" +
                "jmbag='" + jmbag + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", scoreMidterm=" + scoreMidterm +
                ", scoreFinal=" + scoreFinal +
                ", scoreExercise=" + scoreExercise +
                ", grade=" + grade +
                '}';
    }
}

