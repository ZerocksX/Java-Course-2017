package hr.fer.zemris.java.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class describing a blog entry<br/>
 * Stored in table 'blog_entries'
 */
@Entity
@Table(name = "blog_entries")
@Cacheable
public class BlogEntry {

    /**
     * Id
     */
    private Long id;
    /**
     * Comments
     */
    private List<BlogComment> comments = new ArrayList<>();
    /**
     * Created at
     */
    private Date createdAt;
    /**
     * Last modified at
     */
    private Date lastModifiedAt;
    /**
     * Title
     */
    private String title;
    /**
     * Text
     */
    private String text;
    /**
     * Creator, owner of this blog entry
     */
    private BlogUser creator;

    /**
     * Getter for ID
     *
     * @return ID
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Setter for Id
     *
     * @param id Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for comments
     *
     * @return comments
     */
    @OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("postedOn")
    public List<BlogComment> getComments() {
        return comments;
    }

    /**
     * Setter for comments
     *
     * @param comments comments
     */
    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    /**
     * Getter for created at
     *
     * @return created at
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for created at
     *
     * @param createdAt created at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for last modified at
     *
     * @return last modified at
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    /**
     * Setter for last modified at
     *
     * @param lastModifiedAt last modified at
     */
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    /**
     * Getter for title
     *
     * @return title
     */
    @Column(length = 200, nullable = false)
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for text
     *
     * @return text
     */
    @Column(length = 4096, nullable = false)
    public String getText() {
        return text;
    }

    /**
     * Setter for text
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * Getter for creator
     * @return creator
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogUser getCreator() {
        return creator;
    }

    /**
     * Setter for creator
     * @param creator creator
     */
    public void setCreator(BlogUser creator) {
        this.creator = creator;
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
        BlogEntry other = (BlogEntry) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}