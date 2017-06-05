package hr.fer.zemris.java.raytracer.model;

/**
 * Graphical object representing a sphere
 *
 * @author Pavao Jerebić
 */
public class Sphere extends GraphicalObject {

    /**
     * Minimum lambda value for calculating vector from ray starting point to intersection
     */
    private static final double DELTA = 1E-6;
    /**
     * Center
     */
    private Point3D center;
    /**
     * Radius
     */
    private double radius;
    /**
     * Red diffusion characteristic
     */
    private double kdr;
    /**
     * Green diffusion characteristic
     */
    private double kdg;
    /**
     * Blue diffusion characteristic
     */
    private double kdb;
    /**
     * Red reflection characteristic
     */
    private double krr;
    /**
     * Green reflection characteristic
     */
    private double krg;
    /**
     * Blue reflection characteristic
     */
    private double krb;
    /**
     * Coefficient n for reflective component
     */
    private double krn;

    /**
     * Basic constructor
     * see {@link RayIntersection} getters for coefficient explanations or {@link Sphere} private fields
     *
     * @param center center
     * @param radius radius
     * @param kdr    kdr
     * @param kdg    kdg
     * @param kdb    kdb
     * @param krr    krr
     * @param krg    krg
     * @param krb    krb
     * @param krn    krn
     */
    public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb, double krn) {
        this.center = center;
        this.radius = radius;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    @Override
    public RayIntersection findClosestRayIntersection(Ray ray) {
        Point3D start = ray.start;
        Point3D direction = ray.direction;

        double Xd = direction.x;
        double Yd = direction.y;
        double Zd = direction.z;
        double X0 = start.x;
        double Y0 = start.y;
        double Z0 = start.z;
        double A = center.x;
        double B = center.y;
        double C = center.z;

        double lambda1, lambda2;


        double a = (Xd * Xd + Yd * Yd + Zd * Zd);
        double b = (2 * Xd * (X0 - A) + 2 * Yd * (Y0 - B) + 2 * Zd * (Z0 - C));
        double c = ((X0 - A) * (X0 - A) + (Y0 - B) * (Y0 - B) + (Z0 - C) * (Z0 - C) - radius * radius);

        double D = (b * b - 4 * a * c);

        if (D < 0) {
            return null;
        }

        lambda1 = (-b + Math.sqrt(D)) / (2 * a);
        lambda2 = (-b - Math.sqrt(D)) / (2 * a);

        Point3D intersection1 = null;
        Point3D intersection2 = null;

        if (lambda1 > DELTA) {
            intersection1 = new Point3D(X0 + lambda1 * Xd, Y0 + lambda1 * Yd, Z0 + lambda1 * Zd);
        }

        if (lambda2 > DELTA) {
            intersection2 = new Point3D(X0 + lambda2 * Xd, Y0 + lambda2 * Yd, Z0 + lambda2 * Zd);
        }

        Point3D intersection = null;

        double dist = 0;

        if (intersection1 == null) {
            intersection = intersection2;
        } else if (intersection2 == null) {
            intersection = intersection1;
        } else {
            double dist1, dist2;
            dist1 = start.sub(intersection1).norm();
            dist2 = start.sub(intersection2).norm();
            if (dist1 < dist2) {
                intersection = intersection1;
                dist = dist1;
            } else {
                intersection = intersection2;
                dist = dist2;
            }
        }

        if (intersection == null) {
            return null;
        }

        Point3D finalIntersection = intersection;
        return new RayIntersection(finalIntersection, dist, ray.start.sub(center).norm() > radius) {
            @Override
            public Point3D getNormal() {
                return finalIntersection.sub(center).normalize();
            }

            @Override
            public double getKdr() {
                return kdr;
            }

            @Override
            public double getKdg() {
                return kdg;
            }

            @Override
            public double getKdb() {
                return kdb;
            }

            @Override
            public double getKrr() {
                return krr;
            }

            @Override
            public double getKrg() {
                return krg;
            }

            @Override
            public double getKrb() {
                return krb;
            }

            @Override
            public double getKrn() {
                return krn;
            }
        };
    }
}
