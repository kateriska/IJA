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


    public MyStreet(String street_id, Coordinate coordinate1, Coordinate coordinate2)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;

    }
    @Override
    public String getId() {
        return street_id;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return null;
    }

    @Override
    public List<Stop> getStops() {
        return all_stops_list;
    }

    @Override
    public void addStop(Stop stop) {
        if (stop != null) {
            all_stops_list.add(stop);
        }
        return;
    }
}
