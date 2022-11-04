package chess15;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(int x, int  y){this.x = x; this.y = y;}

    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public Vector2 inverse(){
        return new Vector2(-this.x, -this.y);
    }

    @Override
    public String toString() {
        return ("( "+this.x+", "+this.y+" )");
    }
}
