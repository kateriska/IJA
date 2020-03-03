package myMaps;
import maps.Stop;
import maps.Coordinate;
import maps.Street;

public class MyStop implements Stop
{
    private String id;
    private Coordinate street_coordinates;
    Street street_name;

    public MyStop(String id, Coordinate street_coordinates)
    {
        this.id = id;
        this.street_coordinates = street_coordinates;
    }


    @java.lang.Override
    public String getId() {
        return id;
    }

    @java.lang.Override
    public Coordinate getCoordinate() {
        return street_coordinates;
    }

    @java.lang.Override
    public Street getStreet() {

        return street_name;

    }

    @java.lang.Override
    public void setStreet(Street s) {
        this.street_name = s;
    }


}