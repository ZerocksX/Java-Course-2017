package hr.fer.zemris.java.math;

/**
 * 3D vector representation
 *
 * @author Pavao Jerebić
 */
public class Vector3 {

    /**
     * X
     */
    private double x;
    /**
     * Y
     */
    private double y;
    /**
     * Z
     */
    private double z;

    /**
     * Basic constructor
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Norm of the vector
     *
     * @return norm
     */
    public double norm() {
        return Math.sqrt((x * x + y * y + z * z));
    }

    /**
     * Normalized version of this vector
     *
     * @return normalized version of this
     */
    public Vector3 normalized() {
        double norm = norm();
        if (norm == 0) {
            throw new UnsupportedOperationException("Can not normalize a 0 vector");
        }
        return new Vector3(x / norm, y / norm, z / norm);

    }

    /**
     * Result of this + other
     *
     * @param other other
     * @return this + other
     */
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Result of this - other
     *
     * @param other other
     * @return this - other
     */
    public Vector3 sub(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Result of this dot product other
     *
     * @param other other
     * @return this . other
     */
    public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Result of this vector product other
     *
     * @param other other
     * @return this x other
     */
    public Vector3 cross(Vector3 other) {
        return new Vector3(
                y * other.z - other.y * z,
                other.x * z - x * other.z,
                x * other.y - other.x * y
        );
    }

    /**
     * Result of this vector scaled by s
     *
     * @param s s
     * @return s * this
     */
    public Vector3 scale(double s) {
        return new Vector3(x * s, y * s, z * s);
    }

    /**
     * Cosine of angle between this and other
     *
     * @param other other
     * @return cos(angle this, other)
     * @throws UnsupportedOperationException if at least 1 is null vector
     */
    public double cosAngle(Vector3 other) {
        double thisNorm = norm();
        double otherNorm = other.norm();
        if (thisNorm == 0 || otherNorm == 0) {
            throw new UnsupportedOperationException("Can not calculate cosine value of the angle between 2 vectors when at least one is 0 vector");
        }
        return dot(other) / (norm() * other.norm());
    }

    /**
     * Getter for x
     *
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for y
     *
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for z
     *
     * @return z
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns this as array of 3 elements x,y,z
     *
     * @return [x, y, z]
     */
    public double[] toArray() {
        return new double[]{x, y, z};

    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3)) return false;

        Vector3 vector3 = (Vector3) o;

        if (Double.compare(vector3.x, x) != 0) return false;
        if (Double.compare(vector3.y, y) != 0) return false;
        return Double.compare(vector3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
