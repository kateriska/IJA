package maps;

import maps.Stop;
import maps.Street;
import maps.Coordinate;

public class Line {
    public boolean addStop(Stop stop)
    {
        return true;
    }

    public boolean addStreet(Street street)
    {
        return true;
    }

    public static Line defaultLine(java.lang.String id)
    {
        return null;
    }

    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>> getRoute()
    {
        return null;
    }


}
