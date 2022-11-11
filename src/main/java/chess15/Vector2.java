package chess15;

import java.util.Objects;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(int x, int  y){this.x = x; this.y = y;}

    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }


    public Vector2 scaleBy(int c){
        return new Vector2(x*c, y*c);
    }
    public Vector2 inverse(){
        return new Vector2(this.x, -this.y);
    }

    public boolean outOfBounds(){
        return x < 0 || y < 0 || x > 7 || y > 7;
    }

    @Override
    public String toString() {
        return ("( "+this.x+", "+this.y+" )");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
