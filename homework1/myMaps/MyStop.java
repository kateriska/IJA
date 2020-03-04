package myMaps;
import maps.Stop;
import maps.Coordinate;
import maps.Street;

public class MyStop implements Stop
{
    String id;
    Coordinate street_coordinates;
    Street street_name;

    public MyStop(String id, Coordinate street_coordinates)
    {
        this.id = id;
        this.street_coordinates = street_coordinates;
    }

    public MyStop(String id)
    {
        this.id = id;
    }


    @java.lang.Override
    public String getId() {
        return this.id;
    }

    @java.lang.Override
    public Coordinate getCoordinate() {
        return this.street_coordinates;
    }

    @java.lang.Override
    public void setStreet(Street s) {
        this.street_name = s;
    }

    @java.lang.Override
    public Street getStreet() {
        return this.street_name;

    }



}