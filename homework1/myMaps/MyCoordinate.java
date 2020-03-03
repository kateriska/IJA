package myMaps;

import maps.Coordinate;


public class MyCoordinate implements Coordinate
{
    //private Coordinate[][] coordinates;
    int x;
    int y;

    public MyCoordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
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
        return x;
    }

    @java.lang.Override
    public int getY() {
        return y;
    }




}