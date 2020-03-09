package myMaps;
import maps.Stop;
import maps.Coordinate;
import maps.Street;

public class MyStop implements Stop
{
    private String id;
    private Coordinate street_coordinates;
    private Street street_name;

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
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        return true;
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