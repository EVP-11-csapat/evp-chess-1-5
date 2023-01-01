package chess15;

import chess15.board.Vector2;
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
    public void flip() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = a.flip();
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
    public void normalize(){
        Vector2 a = new Vector2(-1,1);
        Vector2 b = new Vector2(2,2);
        Vector2 c = new Vector2(2,1);
        Vector2 d = new Vector2(2,0);

        assertEquals(new Vector2(-1,1), a.normalize());
        assertEquals(new Vector2(1,1), b.normalize());
        assertEquals(new Vector2(2,1), c.normalize());
        assertEquals(new Vector2(1,0), d.normalize());
    }

    @Test
    public void sameDirection(){
        Vector2 a = new Vector2(-1,1);
        Vector2 b = new Vector2(1,-1);
        Vector2 c = new Vector2(0,1);

        assertTrue(Vector2.sameDirection(a,b));
        assertTrue(Vector2.sameDirection(b,a));
        assertFalse(Vector2.sameDirection(a,c));
        assertFalse(Vector2.sameDirection(b,c));
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