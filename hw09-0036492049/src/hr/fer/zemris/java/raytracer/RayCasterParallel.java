package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Parallel variant of {@link RayCaster}
 *
 * @author Pavao Jerebić
 */
public class RayCasterParallel {
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
     * Ray tracer producer
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
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new RayCasterCalculator(0, height, width, height, horizontal, vertical, xAxis, yAxis, screenCorner, scene, eye, red, green, blue));
            pool.shutdown();
            System.out.println("Izračuni gotovi...");
            observer.acceptResult(red, green, blue, requestNo);
            System.out.println("Dojava gotova...");
        };
    }

    /**
     * Class that calculates color on each pixel with recursive actions with threshold = 8 * available processors horizontal lines
     */
    private static class RayCasterCalculator extends RecursiveAction {

        /**
         * Threshold
         */
        private int threshold = 8 * Runtime.getRuntime().availableProcessors();
        /**
         * Starting line
         */
        private int yMin;
        /**
         * Last line
         */
        private int yMax;
        /**
         * Width
         */
        private int width;
        /**
         * Height
         */
        private int height;
        /**
         * Horizontal value of screen
         */
        private double horizontal;
        /**
         * Vertical value of screen
         */
        private double vertical;
        /**
         * X axis
         */
        private Point3D xAxis;
        /**
         * Y axis
         */
        private Point3D yAxis;
        /**
         * Screen corner position
         */
        private Point3D screenCorner;
        /**
         * Scene
         */
        private Scene scene;
        /**
         * Eye position
         */
        private Point3D eye;
        /**
         * Red buffer
         */
        private short[] red;
        /**
         * Green buffer
         */
        private short[] green;
        /**
         * Blue buffer
         */
        private short[] blue;

        /**
         * Constructor
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
        public RayCasterCalculator(int yMin, int yMax, int width, int height, double horizontal, double vertical,
                                   Point3D xAxis, Point3D yAxis, Point3D screenCorner, Scene scene, Point3D eye,
                                   short[] red, short[] green, short[] blue) {
            this.yMin = yMin;
            this.yMax = yMax;
            this.width = width;
            this.height = height;
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            this.screenCorner = screenCorner;
            this.scene = scene;
            this.eye = eye;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @Override
        public void compute() {
            if (yMax - yMin + 1 <= threshold) {
                RayCaster.calculateColors(yMin, yMax, width, height, horizontal, vertical, xAxis, yAxis, screenCorner, scene, eye, red, green, blue);
                return;
            }
            invokeAll(
                    new RayCasterCalculator(yMin, yMin + (yMax - yMin) / 2, width, height, horizontal, vertical, xAxis, yAxis, screenCorner, scene, eye, red, green, blue),
                    new RayCasterCalculator(yMin + (yMax - yMin) / 2, yMax, width, height, horizontal, vertical, xAxis, yAxis, screenCorner, scene, eye, red, green, blue)
            );
        }
    }
}
