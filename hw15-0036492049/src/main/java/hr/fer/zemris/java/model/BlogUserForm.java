package hr.fer.zemris.java.model;

/**
 * Class describing a blog user form used to create {@link BlogUser}
 *
 * @author Pavao Jerebić
 */
public class BlogUserForm {

    /**
     * First name
     */
    private String firstName;
    /**
     * Last name
     */
    private String lastName;
    /**
     * Nick
     */
    private String nick;
    /**
     * Email
     */
    private String email;
    /**
     * Password
     */
    private String password;

    /**
     * Constructor that sets all fields
     *
     * @param firstName first name
     * @param lastName  last name
     * @param email     email
     * @param nick      nick
     * @param password  password
     */
    public BlogUserForm(String firstName, String lastName, String email, String nick, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
        this.email = email;
        this.password = password;
    }

    /**
     * Method that checks all fields and if any field is wrong it sets its flag to true<br/>
     * constrains are ( order of the flags is the same ):<br/>
     * first name -> size < 20, only ordinary letters<br/>
     * last name -> size < 20, only ordinary letters<br/>
     * email -> size < 100<br/>
     * nick -> size < 20, only ordinary letters<br/>
     * password -> size < 20<br/>
     *
     * @return validated blog user form
     * @throws BlogUserException if anything fails
     * @see BlogUserException#getFlags()
     */
    public BlogUserForm validate() {
        boolean firstNameFlag, lastNameFlag, emailFlag, nickFlag, passwordFlag;
        firstNameFlag = invalidString(firstName, 20, true);
        lastNameFlag = invalidString(lastName, 20, true);
        nickFlag = invalidString(nick, 20, true);
        passwordFlag = invalidString(password, 20, false);
        emailFlag = invalidString(email, 100, false);
        if (firstNameFlag || lastNameFlag || nickFlag || passwordFlag || emailFlag) {
            throw new BlogUserException(new boolean[]{firstNameFlag, lastNameFlag, emailFlag, nickFlag, passwordFlag});
        }
        return this;
    }

    /**
     * Helping method that checks if the given string is not null, within the given size and if onlyLetters flag is true, then it should be only letters
     *
     * @param string      string
     * @param size        size
     * @param onlyLetters onlyLetters flag
     * @return true if string is <b>invalid</b>
     */
    private boolean invalidString(String string, int size, boolean onlyLetters) {
        if (string == null || string.trim().isEmpty() || string.length() > size) {
            return true;
        }
        if (onlyLetters) {
            for (char c : string.toCharArray()) {
                if (!Character.isLetter(c)) {
                    return true;
                }
            }
        }

        return false;
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
     * Getter for last name
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for nick
     *
     * @return nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * Getter for email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
