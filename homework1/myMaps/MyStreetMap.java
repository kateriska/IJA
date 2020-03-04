package myMaps;

import maps.Stop;
import maps.Street;
import maps.StreetMap;

import java.util.ArrayList;
import java.util.List;

public class MyStreetMap implements StreetMap {
    List<Street> streets_map = new ArrayList<Street>();
    public MyStreetMap()
    {

    }
    @Override
    public void addStreet(Street s) {
        streets_map.add(s);
    }

    @Override
    public Street getStreet(String id) {
        Street found_street = null;
        for (Street value: streets_map)
        {
            String street_id = value.getId();

            if (street_id.equals(id))
            {
               found_street = value;
            }
        }

        return found_street;
    }
}
