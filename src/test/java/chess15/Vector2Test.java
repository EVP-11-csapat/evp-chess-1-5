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
}