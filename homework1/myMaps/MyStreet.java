package myMaps;

import maps.Coordinate;
import maps.Stop;
import maps.Street;

import java.util.*;

public class MyStreet implements Street {
    String street_id;
    int x;
    int y;
    Coordinate coordinate1;
    Coordinate coordinate2;
    List<Stop> all_stops_list = new ArrayList<Stop>();
    List<Coordinate> all_coordinates_list = new ArrayList<Coordinate>();
    HashMap<String, MyStreet> streets_map = new HashMap<String, MyStreet>();

    public MyStreet(String street_id, Coordinate coordinate1, Coordinate coordinate2)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
        streets_map.put(street_id, this);


    }

    public MyStreet()
    {



    }

    public void streetMapAccess()
    {
        for (MyStreet i : streets_map.values()) {
            System.out.println(i);
        }

        return;
    }
    @Override
    public String getId() {
        return this.street_id;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        all_coordinates_list.add(coordinate1);
        all_coordinates_list.add(coordinate2);
        return this.all_coordinates_list;
    }

    @Override
    public List<Stop> getStops() {
        return this.all_stops_list;
    }

    @Override
    public void addStop(Stop stop) {
        if (stop != null) {
            all_stops_list.add(stop);
        }
        return;
    }
}
