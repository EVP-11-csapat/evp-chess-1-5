package chess15;

public class Vector2 {
    private int x;
    private int y;

    public Vector2(int x, int  y){this.x = x; this.y = y;}
    public Vector2(Vector2 a){this.x = a.getX(); this.y = a.getY();}

    public int getY(){return this.y;}
    public int getX(){return this.x;}
    public void setX(int a){this.x = a;}
    public void setY(int a){this.y = a;}

    public Vector2 add(Vector2 a)
    {
        this.x += a.getX();
        this.y += a.getY();
        return this;
    }
    public Vector2 subtract(Vector2 a)
    {
        this.x -= a.getX();
        this.y -= a.getY();
        return this;
    }
    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.getY()+b.getY(), a.getX() + b.getX());
    }
    public static Vector2 subtract(Vector2 a, Vector2 b)
    {
        return new Vector2(a.getY()-b.getY(), a.getX() - b.getX());
    }

    @Override
    public String toString() {
        return ("(x: "+this.x+", y: "+this.y+")");
    }
}
