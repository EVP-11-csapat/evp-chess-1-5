package chess15;

import java.util.Objects;

/**
 * A 2D vector for representing the coordinates and moves of board elements.
 */
public class Vector2 {
    public int x;
    public int y;

    /**
     * Creates a new vector with the given coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new vector which is the sum of the given vectors.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The sum of the given vectors.
     */
    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    /**
     * Creates a new vector which is multiplyed by the given scalar.
     *
     * @param c The scalar.
     * @return The multiplied vector.
     */
    public Vector2 scaleBy(int c) {
        return new Vector2(x * c, y * c);
    }

    /**
     * Returns a new vector which is the flipped version of the original.
     * Flipping is done by reversing the y coordinate
     *
     * @return The flipped vector
     */
    public Vector2 flip() {
        return new Vector2(this.x, -this.y);
    }

    /**
     * Returns a new vector which is the inverted version of the original.
     * Inverting is done by reversing the coordinates
     *
     * @return The inverted vector
     */
    public Vector2 invert() {
        return new Vector2(-this.x, -this.y);
    }

    /**
     * Returns whether two vectors have the same absolute direction.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return True if the two vectors have the same absolute direction.
     */
    public static boolean sameDirection(Vector2 a, Vector2 b) {

        Vector2 normA = a.normalize();
        Vector2 normB = b.normalize();

        return (normA.equals(normB) || normA.equals(normB.invert()));
    }

    /**
     * Returns a new vector which is the normalized version of the original.
     * If the normalization resulted in non-integer values it returns the original vector.
     *
     * @return The normalized vector
     */
    public Vector2 normalize() {
        int max = Math.max(Math.abs(x), Math.abs(y));
        float normX = x / (float) max;
        float normY = y / (float) max;
        if (normX % 1 != 0 || normY % 1 != 0) return this;
        return new Vector2((int) normX, (int) normY);
    }

    /**
     * Checks if the given vector is outside the chess board coordinates.
     *
     * @return True if the vector is outside the specified range
     */
    public boolean outOfBounds() {
        return x < 0 || y < 0 || x > 7 || y > 7;
    }

    /**
     * A method to print the vector to the console. Used for debugging.
     *
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return ("( " + this.x + ", " + this.y + " )");
    }

    /**
     * Checks if the given object is equal to this vector.
     *
     * @param o The object to compare.
     * @return True if the given object is equal to this vector.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    /**
     * Returns the hashcode of the vector so the same vector coordinates always have the same hash.
     *
     * @return The hashcode of the vector.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
