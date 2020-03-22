package maps;

import maps.Stop;
import maps.Street;
import maps.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Line {
    private String line_id;
    private List<Street> streets_map = new ArrayList<Street>();
    private List<Stop> stops_map = new ArrayList<Stop>();
    HashMap<Street, Stop> street_stop_map = new HashMap<Street, Stop>();

    public Line(String line_id)
    {
        this.line_id = line_id;
    }
    public boolean addStop(Stop stop)
    {
        stops_map.add(stop);
        street_stop_map.put(stop.getStreet(), stop);
        addStreet(stop.getStreet());

        if (streets_map.size() > 1)
        {
            System.out.println("Aspon dve zastavky v ulici");
            if (streets_map.get(0).follows(streets_map.get(1)) == false || streets_map.get(0).follows(streets_map.get(1)) == false)
            {
                return false;
            }
        }


        /*
        for (Street street : street_stop_map.keySet())
        {
            System.out.println(street.getId());

        }

         */
        return true;
    }

    public boolean addStreet(Street street)
    {
        streets_map.add(street);
        return true;
    }

    public static Line defaultLine(java.lang.String id)
    {
        Line new_line = new Line(id);
        return new_line;
    }

    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>> getRoute()
    {
        return null;
    }


}
