package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.List;

/**
 * Program that calculates colors using Phong's lighting model<br/>
 * It displays
 *
 * @author Pavao Jerebić
 */
public class RayCaster {

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        RayTracerViewer.show(getIRayTracerProducer(),
                new Point3D(10, 0, 0),
                new Point3D(0, 0, 0),
                new Point3D(0, 0, 10),
                20, 20);
    }

    /**
     * Producer for Ray Tracer
     *
     * @return ray tracer producer
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return (eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer) -> {
            System.out.println("Započinjem izračune...");
            short[] red = new short[width * height];
            short[] green = new short[width * height];
            short[] blue = new short[width * height];
            Point3D OG = view.sub(eye);
            Point3D yAxis = viewUp.sub(OG.scalarMultiply(OG.scalarProduct(viewUp))).normalize();
            Point3D xAxis = OG.vectorProduct(yAxis).normalize();
            Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0)).add(yAxis.scalarMultiply(vertical / 2.0));
            Scene scene = RayTracerViewer.createPredefinedScene();
            calculateColors(0, height, width, height, horizontal, vertical, xAxis, yAxis, screenCorner, scene, eye, red, green, blue);
            System.out.println("Izračuni gotovi...");
            observer.acceptResult(red, green, blue, requestNo);
            System.out.println("Dojava gotova...");
        };
    }

    /**
     * Helping method that calculates colors
     *
     * @param yMin         starting line
     * @param yMax         ending line
     * @param width        width of the screen(px)
     * @param height       height of the screen(px)
     * @param horizontal   horizontal value of view
     * @param vertical     vertical value of view
     * @param xAxis        xAxis
     * @param yAxis        yAxis
     * @param screenCorner screen corner
     * @param scene        scene
     * @param eye          eye position
     * @param red          array for each pixel containing amount of red
     * @param green        array for each pixel containing amount of green
     * @param blue         array for each pixel containing amount of blue
     */
    public static void calculateColors(int yMin, int yMax, int width, int height, double horizontal, double vertical,
                                       Point3D xAxis, Point3D yAxis, Point3D screenCorner, Scene scene,
                                       Point3D eye, short red[], short green[], short blue[]) {
        int offset = yMin * width;
        for (int y = yMin; y < yMax; y++) {
            for (int x = 0; x < width; x++) {
                double horizontalScale = x / (1.0 * (width - 1)) * horizontal;
                double verticalScale = y / (1.0 * (height - 1)) * vertical;
                Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(horizontalScale)).sub(yAxis.scalarMultiply(verticalScale));
                Ray ray = Ray.fromPoints(eye, screenPoint);

                short[] rgb = new short[3];
                tracer(scene, ray, rgb);

                red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
                offset++;
            }
        }
    }

    /**
     * Tracer method for given scene and ray starting going from eye to center of the view
     *
     * @param scene scene
     * @param ray   ray
     * @param rgb   array containing red green and blue
     */
    private static void tracer(Scene scene, Ray ray, short[] rgb) {
        rgb[0] = 0;
        rgb[1] = 0;
        rgb[2] = 0;
        short[] colors = new short[]{0, 0, 0};

        List<GraphicalObject> objects = scene.getObjects();
        List<LightSource> lights = scene.getLights();

        double minDist = Double.MAX_VALUE;
        RayIntersection intersection = null;
        for (GraphicalObject object : objects) {
            RayIntersection temp = object.findClosestRayIntersection(ray);
            if (temp != null) {
                if (temp.getDistance() < minDist) {
                    intersection = temp;
                    minDist = temp.getDistance();
                }
            }
        }

        if (intersection != null) {

            rgb[0] = 15;
            rgb[1] = 15;
            rgb[2] = 15;

            for (LightSource light : lights) {
                Ray lightRay = Ray.fromPoints(intersection.getPoint(), light.getPoint());
                boolean isShadowed = false;
                for (GraphicalObject object : objects) {
                    RayIntersection temp = object.findClosestRayIntersection(lightRay);
                    if (temp != null) {
                        if (temp.getDistance() < intersection.getPoint().sub(light.getPoint()).norm()) {
                            isShadowed = true;
                            break;
                        }
                    }
                }
                if (isShadowed) {
                    continue;
                }

                Point3D reflection = intersection.getNormal().scalarMultiply(2 * lightRay.direction.scalarProduct(intersection.getNormal())).sub(lightRay.direction);
                reflection = reflection.negate().normalize();

                double reflectiveCoef = Math.max(reflection.scalarProduct(ray.direction), 0);


                reflectiveCoef = Math.pow(reflectiveCoef, intersection.getKrn());

                int reflectiveRed = (int) Math.round(light.getR() * intersection.getKrr() * reflectiveCoef);
                int reflectiveGreen = (int) Math.round(light.getG() * intersection.getKrg() * reflectiveCoef);
                int reflectiveBlue = (int) Math.round(light.getB() * intersection.getKrb() * reflectiveCoef);


                double diffuseCoef = Math.max(lightRay.direction.scalarProduct(intersection.getNormal()), 0);

                int diffuseRed = (int) Math.round(light.getR() * intersection.getKdr() * diffuseCoef);
                int diffuseGreen = (int) Math.round(light.getG() * intersection.getKdg() * diffuseCoef);
                int diffuseBlue = (int) Math.round(light.getB() * intersection.getKdb() * diffuseCoef);

                colors[0] += reflectiveRed + diffuseRed;
                colors[1] += reflectiveGreen + diffuseGreen;
                colors[2] += reflectiveBlue + diffuseBlue;

            }
        }

        rgb[0] += colors[0];
        rgb[1] += colors[1];
        rgb[2] += colors[2];
    }

}
