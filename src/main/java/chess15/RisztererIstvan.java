package chess15;

import java.util.*;

public class RisztererIstvan
{
    public static void run()
    {
        //testCoords();
        testQueen();
        testKnight();
        testKing();    
    }
    public static void testCoords()
    {
        Coords c1 = new Coords(0,0);
        
        Coords c2 = new Coords(1,0);
        Coords c3 = new Coords(0,1);
        Coords c4 = new Coords(1,1);
        Coords c5 = new Coords(-1,0);
        Coords c6 = new Coords(0,-1);
        Coords c7 = new Coords(-1,-1);

        System.out.println(Coords.subtract(c1, c2).toString());
        System.out.println(Coords.subtract(c2, c3).toString());
        System.out.println(Coords.subtract(c3, c4).toString());
        System.out.println(Coords.subtract(c4, c5).toString());
        System.out.println(Coords.add(c5, c6).toString());
        System.out.println(Coords.add(c6, c7).toString());
        System.out.println(Coords.add(c7, c5).toString());
        System.out.println(Coords.add(c7, c1).toString());
        System.out.println("non static func");
        System.out.println(c1.add(c2).toString());
        System.out.println(c1.add(c3).toString());
        System.out.println(c1.add(c4).toString());
        System.out.println(c1.add(c5).toString());
        System.out.println(c1.add(c6).toString());
        System.out.println(c1.add(c7).toString());
        System.out.println(c1.subtract(c7).toString());
        System.out.println(c1.subtract(c6).toString());
        System.out.println(c1.subtract(c5).toString());
        System.out.println(c1.subtract(c4).toString());
        System.out.println(c1.subtract(c3).toString());
        System.out.println(c1.subtract(c2).toString());
    }

    public static void testQueen()
    {
        ArrayList<Coords> result1=Queen.getQueenSingleton().possibleMoves(new Coords(0,0));
        System.out.println("A királynő a A1-es mezőről (0,0), az alábbi mezőkre tud lépni: ");
        System.out.println("lehetséges lépések száma: "+ result1.size());
        for(int i=0; i<result1.size(); i++)
        {
            System.out.println(result1.get(i).toString());

        }
        ArrayList<Coords> result2=Queen.getQueenSingleton().possibleMoves(new Coords(3,3));
        System.out.println("A királynő a D4-es mezőről (3,3), az alábbi mezőkre tud lépni: ");
        System.out.println("lehetséges lépések száma : "+result2.size());
        for(int i=0; i<result2.size(); i++)
        {
            System.out.println(result2.get(i).toString() );
        }
    }
    public static void testKing()
    {
        ArrayList<Coords> result1=King.getKingSingleton().possibleMoves(new Coords(0,0));
        System.out.println("A király a A1-es mezőről (0,0), az alábbi mezőkre tud lépni: ");
        System.out.println("lehetséges lépések száma: "+ result1.size());
        for(int i=0; i<result1.size(); i++)
        {
            System.out.println(result1.get(i).toString());

        }
        ArrayList<Coords> result2=King.getKingSingleton().possibleMoves(new Coords(3,3));
        System.out.println("A király a D4-es mezőről (3,3), az alábbi mezőkre tud lépni: ");
        System.out.println("lehetséges lépések száma : "+result2.size());
        for(int i=0; i<result2.size(); i++)
        {
            System.out.println(result2.get(i).toString() );
        }
    }
    public static void testKnight()
    {
        ArrayList<Coords> result1=Knight.getKnightSingleton().possibleMoves(new Coords(0,0));
        System.out.println("A ló a A1-es mezőről (0,0), az alábbi mezőkre tud lépni: ");
        System.out.println("lehetséges lépések száma: "+ result1.size());
        for(int i=0; i<result1.size(); i++)
        {
            System.out.println(result1.get(i).toString());

        }
        ArrayList<Coords> result2=Knight.getKnightSingleton().possibleMoves(new Coords(3,3));
        System.out.println("A ló a D4-es mezőről (3,3), az alábbi mezőkre tud lépni: ");
        System.out.println("lehetséges lépések száma : "+result2.size());
        for(int i=0; i<result2.size(); i++)
        {
            System.out.println(result2.get(i).toString() );
        }
    }
}