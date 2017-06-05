package hr.fer.zemris.java.hw04.db;

/**
 * Representation of a student<br/>
 *
 * @author Pavao Jerebić
 */
public class StudentRecord {
    /**
     * JMBAG
     */
    private String jmbag;
    /**
     * First name
     */
    private String firstName;
    /**
     * Last name
     */
    private String lastName;
    /**
     * Final grade
     */
    private Integer finalGrade;

    /**
     * Constructor for all fields
     *
     * @param jmbag      JMBAG
     * @param lastName   Last name
     * @param firstName  First name
     * @param finalGrade Final grade
     * @throws IllegalArgumentException if any field is null
     */
    public StudentRecord(String jmbag, String firstName, String lastName, Integer finalGrade) {
        if (jmbag == null
                || firstName == null
                || lastName == null
                || finalGrade == null) {
            throw new IllegalArgumentException("One or more arguments are null");
        }
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
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
     * Getter for lastName
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for firstName
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for finalGrade
     *
     * @return final grade
     */
    public Integer getFinalGrade() {
        return finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentRecord that = (StudentRecord) o;

        return getJmbag().equals(that.getJmbag());
    }

    @Override
    public int hashCode() {
        return getJmbag().hashCode();
    }

    @Override
    public String toString() {
        return "StudentRecord{" +
                "jmbag='" + jmbag + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", finalGrade=" + finalGrade +
                '}';
    }
}
