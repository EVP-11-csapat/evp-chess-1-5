package chess15;

public class Coords {
    private int x;
    private int y;

    public Coords(int x,int  y){this.x = x; this.y = y;}
    public Coords(Coords a){this.x = a.getX(); this.y = a.getY();}

    public int getY(){return this.y;}
    public int getX(){return this.x;}
    public void setX(int a){this.x = a;}
    public void setY(int a){this.y = a;}

    public Coords add(Coords a)
    {
        this.x += a.getX();
        this.y += a.getY();
        return this;
    }
    public Coords subtract(Coords a)
    {
        this.x -= a.getX();
        this.y -= a.getY();
        return this;
    }
    public static  Coords add(Coords a, Coords b)
    {
        return new Coords(a.getY()+b.getY(), a.getX() + b.getX());
    }
    public static Coords subtract(Coords a, Coords b)
    {
        return new Coords(a.getY()-b.getY(), a.getX() - b.getX());
    }

    @Override
    public String toString() {
        return ("(x: "+this.x+", y: "+this.y+")");
    }
}
