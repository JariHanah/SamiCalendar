/*
 * Shredzone Commons - suncalc
 *
 *   http://commons.shredzone.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.shredzone.commons.suncalc.util;

import com.codename1.util.MathUtil;
import static java.lang.Math.*;
import static org.shredzone.commons.suncalc.util.ExtendedMath.PI2;
import static org.shredzone.commons.suncalc.util.ExtendedMath.isZero;


/**
 * A three dimensional vector.
 * <p>
 * Objects are is immutable and threadsafe.
 */
public class Vector {

    private final double x;
    private final double y;
    private final double z;
    private final Polar polar = new Polar();

    /**
     * Creates a new {@link Vector} of the given cartesian coordinates.
     *
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @param z
     *            Z coordinate
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new {@link Vector} of the given cartesian coordinates.
     *
     * @param d
     *            Array of coordinates, must have 3 elements
     */
    public Vector(double[] d) {
        if (d.length != 3) {
            throw new IllegalArgumentException("invalid vector length");
        }
        this.x = d[0];
        this.y = d[1];
        this.z = d[2];
    }

    /**
     * Creates a new {@link Vector} of the given polar coordinates, with a radial distance
     * of 1.
     *
     * @param thetaY
     *            Azimuthal Angle
     * @param thetaO
     *            Polar Angle
     * @return Created {@link Vector}
     */
    public static Vector ofPolar(double thetaY, double thetaO) {
        return ofPolar(thetaY, thetaO, 1.0);
    }

    /**
     * Creates a new {@link Vector} of the given polar coordinates.
     *
     * @param thetaY
     *            Azimuthal Angle
     * @param thetaO
     *            Polar Angle
     * @param r
     *            Radial Distance
     * @return Created {@link Vector}
     */
    public static Vector ofPolar(double thetaY, double thetaO, double r) {
        double cosO = cos(thetaO);
        Vector result = new Vector(
            r * cos(thetaY) * cosO,
            r * sin(thetaY) * cosO,
            r *          sin(thetaO)
        );
        result.polar.setPolar(thetaY, thetaO, r);
        return result;
    }

    /**
     * Returns the cartesian X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the cartesian Y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the cartesian Z coordinate.
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns the azimuthal angle (?) in radians.
     */
    public double getPhi() {
        return polar.getPhi();
    }

    /**
     * Returns the polar angle (?) in radians.
     */
    public double getTheta() {
        return polar.getTheta();
    }

    /**
     * Returns the polar radial distance (r).
     */
    public double getR() {
        return polar.getR();
    }

    /**
     * Returns a {@link Vector} that is the sum of this {@link Vector} and the given
     * {@link Vector}.
     *
     * @param vec
     *            {@link Vector} to add
     * @return Resulting {@link Vector}
     */
    public Vector add(Vector vec) {
        return new Vector(
            x + vec.x,
            y + vec.y,
            z + vec.z
        );
    }

    /**
     * Returns a {@link Vector} that is the difference of this {@link Vector} and the
     * given {@link Vector}.
     *
     * @param vec
     *            {@link Vector} to subtract
     * @return Resulting {@link Vector}
     */
    public Vector subtract(Vector vec) {
        return new Vector(
            x - vec.x,
            y - vec.y,
            z - vec.z
        );
    }

    /**
     * Returns a {@link Vector} that is the scalar product of this {@link Vector} and the
     * given scalar.
     *
     * @param scalar
     *            Scalar to multiply
     * @return Resulting {@link Vector}
     */
    public Vector multiply(double scalar) {
        return new Vector(
            x * scalar,
            y * scalar,
            z * scalar
        );
    }

    /**
     * Returns the negation of this {@link Vector}.
     *
     * @return Resulting {@link Vector}
     */
    public Vector negate() {
        return new Vector(
            -x,
            -y,
            -z
        );
    }

    /**
     * Returns a {@link Vector} that is the cross product of this {@link Vector} and the
     * given {@link Vector}.
     *
     * @param right
     *            {@link Vector} to multiply
     * @return Resulting {@link Vector}
     */
    public Vector cross(Vector right) {
        return new Vector(
            y * right.z - z * right.y,
            z * right.x - x * right.z,
            x * right.y - y * right.x
        );
    }

    /**
     * Returns the dot product of this {@link Vector} and the given {@link Vector}.
     *
     * @param right
     *            {@link Vector} to multiply
     * @return Resulting dot product
     */
    public double dot(Vector right) {
        return x * right.x + y * right.y + z * right.z;
    }

    /**
     * Returns the norm of this {@link Vector}.
     *
     * @return Norm of this vector
     */
    public double norm() {
        return sqrt(dot(this));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vector)) {
            return false;
        }

        Vector vec = (Vector) obj;
        return Double.compare(x, vec.x) == 0
            && Double.compare(y, vec.y) == 0
            && Double.compare(z, vec.z) == 0;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(x).hashCode()
            ^  Double.valueOf(y).hashCode()
            ^  Double.valueOf(z).hashCode();
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ", z=" + z + ")";
    }

    /**
     * Helper class for lazily computing the polar coordinates in an immutable Vector
     * object.
     */
    private class Polar {
        private  Double thetaY = null;
        private  Double thetaO = null;
        private  Double r = null;

        /**
         * Sets polar coordinates.
         *
         * @param thetaY
         *            Phi
         * @param thetaO
         *            Theta
         * @param r
         *            R
         */
        public synchronized void setPolar(double thetaY, double thetaO, double r) {
            this.thetaY = thetaY;
            this.thetaO = thetaO;
            this.r = r;
        }

        public synchronized double getPhi() {
            if (thetaY == null) {
                if (isZero(x) && isZero(y)) {
                    thetaY = 0.0;
                } else {
                    thetaY = MathUtil.atan2(y, x);
                }

                if (thetaY < 0.0) {
                    thetaY += PI2;
                }
            }
            return thetaY;
        }

        public synchronized double getTheta() {
            if (thetaO == null) {
                double pSqr = x * x + y * y;

                if (isZero(z) && isZero(pSqr)) {
                    thetaO = 0.0;
                } else {
                    thetaO = MathUtil.atan2(z, sqrt(pSqr));
                }
            }
            return thetaO;
        }

        public synchronized double getR() {
            if (r == null) {
                r = sqrt(x * x + y * y + z * z);
            }
            return r;
        }
    }

}

