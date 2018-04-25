package hr.fer.zemris.java.model;

import java.util.List;

/**
 * Representation of an image
 *
 * @author Pavao Jerebić
 */
public class Image {
    /**
     * Path relative to the 'WEB-INF/slike' folder
     */
    private String path;
    /**
     * Thumbnail paht
     */
    private String thumbnail;
    /**
     * Name of the image
     */
    private String name;
    /**
     * Tags
     */
    private List<String> tags;

    /**
     * Basic constructor
     *
     * @param path path
     * @param name name
     * @param tags tags
     */
    public Image(String path, String name, List<String> tags) {
        this.path = path;
        this.name = name;
        this.tags = tags;
    }

    /**
     * getter fro path
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for tags
     *
     * @return tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Getter for thumbnail path
     *
     * @return thumbnail path
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Setter for thumbnail path
     *
     * @param thumbnail thumbnail path
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
