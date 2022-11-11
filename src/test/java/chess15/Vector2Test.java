package chess15;

import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2Test {

    @Test
    public void add() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = new Vector2(3, 4);
        Vector2 c = Vector2.add(a, b);
        assertEquals(c.x, 4);
        assertEquals(c.y, 6);
    }

    @Test
    public void inverse() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = a.inverse();
        assertEquals(b.x, 1);
        assertEquals(b.y, -2);
    }

    @Test
    public void testToString() {
        Vector2 a = new Vector2(1, 2);
        assertEquals(a.toString(), "( 1, 2 )");
    }

    @Test
    public void scaleBy() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = a.scaleBy(3);
        assertEquals(b.x, 3);
        assertEquals(b.y, 6);
    }

    @Test
    public void outOfBounds() {
        Vector2 a = new Vector2(1, 2);
        assertFalse(a.outOfBounds());
        Vector2 b = new Vector2(-1, 2);
        assertTrue(b.outOfBounds());
        Vector2 c = new Vector2(1, -2);
        assertTrue(c.outOfBounds());
        Vector2 d = new Vector2(1, 8);
        assertTrue(d.outOfBounds());
        Vector2 e = new Vector2(8, 2);
        assertTrue(e.outOfBounds());
    }
}