package myMaps;

import maps.Coordinate;
import maps.Stop;
import maps.Street;

import java.util.*;

public class MyStreet implements Street {
    private String street_id;
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    private List<Stop> all_stops_list = new ArrayList<Stop>();
    private List<Coordinate> all_coordinates_list = new ArrayList<Coordinate>();

    public MyStreet(String street_id, Coordinate coordinate1, Coordinate coordinate2)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
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
        stop.setStreet(this);
        if (stop != null) {
            all_stops_list.add(stop);
        }
        return;
    }
}
