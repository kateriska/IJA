package myMaps;

import maps.Coordinate;


public class MyCoordinate implements Coordinate
{
    //private Coordinate[][] coordinates;
    private int x;
    private int y;

    public MyCoordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @java.lang.Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        return true;
    }


    public static Coordinate create(int x, int y)
    {

        if (x < 0 || y < 0) {
            return null;
        }


        Coordinate coordinates = new MyCoordinate(x, y);

        return coordinates;
    }


    @java.lang.Override
    public int getX() {
        return this.x;
    }

    @java.lang.Override
    public int getY() {
        return this.y;
    }




}