package hr.fer.andriod.hw0036492049.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class representing a user with some data<br/>
 * Can be used with {@link com.google.gson.Gson}
 * Created by pavao on 28.06.17..
 */

public class User implements Serializable {
    /**
     * HTTP location of the avatar image
     */
    @SerializedName("avatar_location")
    private String avatarLocation;
    /**
     * First name
     */
    @SerializedName("first_name")
    private String firstName;
    /**
     * Last name
     */
    @SerializedName("last_name")
    private String lastName;
    /**
     * Phone number
     */
    @SerializedName("phone_no")
    private String phoneNumber;
    /**
     * Email
     */
    @SerializedName("email_sknf")
    private String eMail;
    /**
     * Spouse
     */
    @SerializedName("spouse")
    private String spouse;
    /**
     * Age
     */
    @SerializedName("age")
    private String age;

    /**
     * Empty constructor
     */
    public User() {
    }

    /**
     * Constructor that sets all fields
     *
     * @param avatarLocation location of the avatar image
     * @param firstName      first name
     * @param lastName       last name
     * @param phoneNumber    phone number
     * @param eMail          E-Mail
     * @param spouse         spouse
     * @param age            age
     */
    public User(String avatarLocation, String firstName, String lastName, String phoneNumber, String eMail, String spouse, String age) {
        this.avatarLocation = avatarLocation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.spouse = spouse;
        this.age = age;
    }

    /**
     * Getter for avatar location
     *
     * @return avatar location
     */
    public String getAvatarLocation() {
        return avatarLocation;
    }

    /**
     * Setter for avatar location
     *
     * @param avatarLocation avatar location
     */
    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
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
     * Setter for first name
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
     * Setter for last name
     *
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for phone number
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for phone number
     *
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter for e-mail
     *
     * @return e-mail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * Setter for email
     *
     * @param eMail email
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * Getter for spouse
     *
     * @return spouse
     */
    public String getSpouse() {
        return spouse;
    }

    /**
     * Setter for spouse
     *
     * @param spouse spouse
     */
    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    /**
     * Getter for age
     *
     * @return age
     */
    public String getAge() {
        return age;
    }

    /**
     * Setter for age
     *
     * @param age age
     */
    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "avatarLocation='" + avatarLocation + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", eMail='" + eMail + '\'' +
                ", spouse='" + spouse + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
