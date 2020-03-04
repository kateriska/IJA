/*
 * Zdrojové kódy josu součástí zadání 1. úkolu pro předmětu IJA v ak. roce 2019/2020.
 * (C) Radek Kočí
 *
 * Vytvořte třídy MyCoordinate, MyStop, MyStreet a MyStreetMap, které splňují podmínky definované touto testovací
 * třídou. Třídy implementují příslušná rozhraní, která jsou součástí zadání. Kromě metod, předepsaných rozhraními,
 * mohou třídy implementovat své další metody a konstruktory.
 */

import java.util.Arrays;
import maps.Coordinate;
import maps.Stop;
import maps.Street;
import maps.StreetMap;
import myMaps.MyCoordinate;
import myMaps.MyStop;
import myMaps.MyStreet;
import myMaps.MyStreetMap;

/**
 *
 * @author koci
 */
public class TestHomework1 {

    public static void main(String[] argv) {

        boolean assertOn = false;
        assert assertOn = true;

        if (! assertOn) {
            System.out.println(">! Spustte s prepinacem -ea");
            return ;
        }

        test();
        System.out.println("OK");
    }

    private static void test() {
        // pokusí se vytvořit souřadnice (-10,60) -- nelze!
        Coordinate c0 = MyCoordinate.create(-10,60);
        assert c0 == null
                : "Nelze vytvorit souradnice se zapornou hodnotou";

        Street str1 = new MyStreet("str1", MyCoordinate.create(10, 10), MyCoordinate.create(10, 100));
        Street str2 = new MyStreet("str2", MyCoordinate.create(10, 100), MyCoordinate.create(50, 150));
        Street str3 = new MyStreet("str3", MyCoordinate.create(10, 100), MyCoordinate.create(50, 50));

        Coordinate c1 = MyCoordinate.create(10,60);
        assert c1 != null
                : "Lze vytvorit souradnice s kladnymi hodnotami";

        Stop s1 = new MyStop("s1", c1);
        Coordinate c2 = MyCoordinate.create(25,75);
        Stop s2 = new MyStop("s1", c2);
        //System.out.println(s1.getId());
        //System.out.println(s2.getId());

        str1.addStop(s1);
        str2.addStop(s2);

        /*

        System.out.println(s1.getId());
        System.out.println(s1.getCoordinate());
        System.out.println(str1.getStops());

        System.out.println(s1.getStreet());
        System.out.println(str1);

        System.out.println(str1.getStops());
        System.out.println(str1.getId());
        System.out.println(str2.getStops());


        System.out.println(str1.getCoordinates());

        System.out.println(s1.getStreet());
        System.out.println(str1);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(str1.getStops());
        System.out.println(str2.getStops());


/*
        assert s1.getStreet().equals(str1)
                : "Zastavka s1 je umistena na ulici str1";
*/
        StreetMap sm = new MyStreetMap();
        sm.addStreet(str1);
        sm.addStreet(str2);
        sm.addStreet(str3);


        assert sm.getStreet("str2").equals(str2)
                : "Test spravne vlozene ulice str2";
        /*

        assert sm.getStreet("str1").getStops().equals(Arrays.asList(new Stop[] {new MyStop("s1")}))
                : "Test spravne vlozenych zastavek u ulice str1";
        */

        assert sm.getStreet("str3").getStops().isEmpty()
                : "Test spravne vlozenych zastavek u ulice str3";
        assert sm.getStreet("str3").getCoordinates().equals(Arrays.asList(new Coordinate[] {MyCoordinate.create(10, 100), MyCoordinate.create(50, 50)}))
                : "Test spravnych koordinatu u ulice str3";


    }

}
