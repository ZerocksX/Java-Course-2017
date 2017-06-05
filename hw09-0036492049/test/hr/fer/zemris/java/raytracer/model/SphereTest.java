package hr.fer.zemris.java.raytracer.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavao Jerebić
 */
public class SphereTest {
    @Test
    public void findClosestRayIntersection() throws Exception {
        Sphere sphere = new Sphere(new Point3D(), 1, 1, 1, 1, 1, 1, 1, 1);
        Ray ray = new Ray(new Point3D(10, 0, 0), new Point3D(-1, 0, 0));
        RayIntersection intersection = sphere.findClosestRayIntersection(ray);
        assertEquals(9, intersection.getDistance(), 0.00001);
        assertEquals(true, intersection.isOuter());
        assertEquals(1, intersection.getPoint().x, 0.0001);
        assertEquals(0, intersection.getPoint().y, 0.0001);
        assertEquals(0, intersection.getPoint().z, 0.0001);
    }

}