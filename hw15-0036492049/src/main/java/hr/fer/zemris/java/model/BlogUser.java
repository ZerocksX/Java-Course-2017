package hr.fer.zemris.java.model;

import hr.fer.zemris.java.encoder.Encoder;

import javax.persistence.*;
import java.util.List;

/**
 * Class describing blog user<br/>
 * Stored in 'blog_users' table
 *
 * @author Pavao Jerebić
 */
@Entity
@Table(name = "blog_users")
@NamedQueries({
        @NamedQuery(name = "BlogUser.findByNick", query = "select u from BlogUser as u where u.nick=:nick"),
        @NamedQuery(name = "BlogUser.findAll", query = "select u from BlogUser as u")
})
@Cacheable
public class BlogUser {
    /**
     * Id
     */
    private long id;
    /**
     * Getter for first name
     */
    private String firstName;
    /**
     * Getter for last name
     */
    private String lastName;
    /**
     * Getter for nick
     */
    private String nick;
    /**
     * Getter for email
     */
    private String email;
    /**
     * Getter for password hash
     */
    private String passwordHash;
    /**
     * Getter for blog entries
     */
    private List<BlogEntry> blogEntries;

    /**
     * Basic constructor
     */
    public BlogUser() {

    }

    /**
     * Getter for first name
     *
     * @return first name
     */
    @Column(length = 20, nullable = false)
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
    @Column(length = 20, nullable = false)
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
     * Getter for nick
     *
     * @return nick
     */
    @Column(length = 20, unique = true, nullable = false)
    public String getNick() {
        return nick;
    }

    /**
     * Setter for nick
     *
     * @param nick nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Getter for email
     *
     * @return email
     */
    @Column(length = 100, unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for password hash
     *
     * @return password hash
     */
    @Column(length = 50, nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Setter for password hash
     *
     * @param passwordHash password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Getter for Id
     *
     * @return Id
     */
    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    /**
     * Setter for Id
     *
     * @param id Id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for blog entries
     *
     * @return blog entries
     */
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("lastModifiedAt desc")
    public List<BlogEntry> getBlogEntries() {
        return blogEntries;
    }

    /**
     * Setter for blog entries
     *
     * @param blogEntries blog entries
     */
    public void setBlogEntries(List<BlogEntry> blogEntries) {
        this.blogEntries = blogEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogUser)) return false;

        BlogUser blogUser = (BlogUser) o;

        return id == blogUser.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    /**
     * Constructor that accepts blog user form
     *
     * @param form blog user form
     * @throws BlogUserException if form is invalid
     * @see BlogUserForm#validate()
     */
    public BlogUser(BlogUserForm form) {
        form = form.validate();
        setFirstName(form.getFirstName());
        setLastName(form.getLastName());
        setEmail(form.getEmail());
        setNick(form.getNick());
        setPasswordHash(Encoder.encode(form.getPassword()));
    }
}
