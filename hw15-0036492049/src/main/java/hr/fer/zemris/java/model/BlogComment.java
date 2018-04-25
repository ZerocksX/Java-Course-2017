package hr.fer.zemris.java.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity describing blog comment<br/>
 * Stored in table 'blog_comments'
 */
@Entity
@Table(name = "blog_comments")
@NamedQueries({
        @NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
public class BlogComment {

    /**
     * ID
     */
    private Long id;
    /**
     * Blog entry to which this class belongs
     */
    private BlogEntry blogEntry;
    /**
     * users e-mail
     */
    private String usersEMail;
    /**
     * message
     */
    private String message;
    /**
     * posted on
     */
    private Date postedOn;

    /**
     * Getter for id
     *
     * @return id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for blog entry
     *
     * @return blog entry
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    /**
     * Setter for blog entry
     *
     * @param blogEntry blog entry
     */
    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    /**
     * Getter for users e-mail
     *
     * @return users e-mail
     */
    @Column(length = 100, nullable = false)
    public String getUsersEMail() {
        return usersEMail;
    }

    /**
     * Setter for users e-mail
     *
     * @param usersEMail users e-mail
     */
    public void setUsersEMail(String usersEMail) {
        this.usersEMail = usersEMail;
    }

    /**
     * Getter for message
     * @return message
     */
    @Column(length = 4096, nullable = false)
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for posted on
     * @return posted on
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getPostedOn() {
        return postedOn;
    }

    /**
     * Setter for posted on
     * @param postedOn posted on
     */
    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlogComment other = (BlogComment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}