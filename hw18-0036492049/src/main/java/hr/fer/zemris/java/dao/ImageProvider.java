package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.model.Image;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Provider for images
 *
 * @author Pavao Jerebić
 */
public class ImageProvider {
    /**
     * images
     */
    private List<Image> images;
    /**
     * tags
     */
    private Set<String> tags;

    /**
     * Constructor that initializes images and tags
     *
     * @param images images
     */
    public ImageProvider(List<Image> images) {
        this.images = images;
        this.tags = new TreeSet<>();
        images.forEach(image -> tags.addAll(image.getTags()));
    }

    /**
     * Returns all images
     *
     * @return images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * Returns all images with a tag
     *
     * @param tag tag
     * @return image with a tag
     */
    public List<Image> getImagesWithTag(String tag) {
        return images.stream().filter(image -> image.getTags().contains(tag)).collect(Collectors.toList());
    }

    /**
     * Returns all tahs
     *
     * @return tags
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * Retunrs image with the given path
     *
     * @param path paht
     * @return image or null if it does not exist
     */
    public Image getImage(String path) {
        for (Image i : images) {
            if (i.getPath().equals(path)) {
                return i;
            }
        }
        return null;
    }
}
