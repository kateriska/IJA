package maps;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.paint.Paint;
import javafx.animation.Timeline;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.text.Text;

public class TransportLine {
    private String line_id;
    private List<Street> streets_map = new ArrayList<Street>();
    private List<Stop> stops_map = new ArrayList<Stop>();
    private List<Street> original_streets_map = new ArrayList<Street>();
    private List<Stop> original_stops_map = new ArrayList<Stop>();
    ArrayList<Circle> all_line_vehicles = new ArrayList<Circle>();
    Timeline timeline = new Timeline();
    Paint line_color = null;
    Paint selected_line_color = null;
    int delay = 0;
    Street closed_street = null;
    ArrayList<Street> detour_streets = new ArrayList<Street>();
    int closed_stop_index = 0;
    Stop closed_stop = null;

    public TransportLine()
    {

    }

    // set id of this line
    public void setLineId(String line_id)
    {
        this.line_id = line_id;
    }

    public String getLineId()
    {
        return line_id;
    }

    // add stop to line - important to check whether two streets follows each other or not, street of stop is also added
    public boolean addStop(Stop stop)
    {
        stops_map.add(stop);
        addStreet(stop.getStreet());

        if (streets_map.size() > 1)
        {
            //System.out.println("More than one street in line");
            if (streets_map.get(0).follows(streets_map.get(1)) == false || streets_map.get(0).follows(streets_map.get(1)) == false)
            {
                stops_map.remove(stop);
                streets_map.remove(stop.getStreet());
                return false;
            }
        }

        return true;
    }

    // add Street without Stop to TransportLine
    public boolean addStreet(Street street)
    {
        streets_map.add(street);
        return true;
    }

    // create TransportLine object
    public static TransportLine defaultLine()
    {
        TransportLine new_line = new TransportLine();
        return new_line;
    }

    // simulation of route of TransportLine - stop of street are in brackets, when the street is without any stop of line we set null
    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>> getRoute()
    {
        List<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>> roads_map = new ArrayList<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>>();
        java.util.AbstractMap.SimpleImmutableEntry<Street,Stop> entry = new java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>(null, null);

        for (Street s : streets_map)
        {
            if (s.getStops().isEmpty() == false)
            {
                for (int i = 0; i < s.getStops().size(); i++)
                {
                    entry = new java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>(s,s.getStops().get(i));
                }

            }
            else
            {
                entry = new java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>(s,null);
            }

            roads_map.add(entry);

        }

        return roads_map;

    }

    // prints the route of getRoute()
    // for example: Street1:stop(Stop13);Street4:stop(Stop17);Street6:stop(Stop5);Street10:null;Street17:stop(Stop1);Street20:stop(Stop2);
    public String printRoute()
    {
        String res = this.getRoute().stream()
                .map(entry -> entry.getKey().getId()
                        + ":"
                        + entry.getValue()
                        + ";")
                .collect(Collectors.joining());
        System.out.println(res);
        return res;
    }

    public String printRouteStops()
    {
        String route_output = "-> ";
        for (Stop s : stops_map)
        {
            route_output = route_output + s.getId() + " ->";
        }

        return route_output;
    }


    public List<Stop> getStopsMap()
    {
        return stops_map;
    }

    public List<Street> getStreetsMap()
    {
        return streets_map;
    }

    public void setClosedStreet(Street s)
    {
        closed_street = s;
    }

    public Street getClosedStreet()
    {
        return closed_street;
    }

    public void addDetourStreet( Street s)
    {
        detour_streets.add(s);
    }

    public ArrayList<Street> getDetourStreets()
    {
        return detour_streets;
    }

    public void clearDetourStreet()
    {
        detour_streets = null;
    }

    public void setClosedStopIndex(int i)
    {
        closed_stop_index = i;
    }

    public int getClosedStopIndex()
    {
        return closed_stop_index;
    }

    public void setClosedStop(Stop stop)
    {
        closed_stop = stop;
    }

    public Stop getClosedStop()
    {
        return closed_stop;
    }


    /*
    @return arraylist of all coordinates of specified path, they are sorted in the way how vehicle is travelling through them,
    the right order is important
     */
    public ArrayList<Coordinate> transportLinePath()
    {
        Coordinate next_street1 = null;
        Coordinate next_street2 = null;
        Street next_street = null;
        ArrayList<Coordinate> line_coordinates = new ArrayList<Coordinate>();

        for (int i = 0; i < getStreetsMap().size(); i++)
        {
            Street s = getStreetsMap().get(i);
            Coordinate this_street1 = s.getCoordinates().get(0);
            Coordinate this_street2 = s.getCoordinates().get(2);

            if (i + 1 < getStreetsMap().size())
            {
                next_street = getStreetsMap().get(i+1);
                next_street1 = next_street.getCoordinates().get(0);
                next_street2 = next_street.getCoordinates().get(2);
            }
            else
            {
                break;
            }

            for (Stop stop : getStopsMap())
            {
                if (stop.getStreet().equals(s))
                {
                    line_coordinates.add(stop.getCoordinate());
                }
            }

            if (s.getCoordinates().get(1) != null)
            {
                line_coordinates.add(s.getCoordinates().get(1));
            }

            //11
            if (this_street1.getX() == next_street1.getX() && this_street1.getY() == next_street1.getY())
            {
                line_coordinates.add(this_street1);
            }
            //12
            else if (this_street1.getX() == next_street2.getX() && this_street1.getY() == next_street2.getY())
            {
                line_coordinates.add(this_street1);
            }
            // 21
            else if (this_street2.getX() == next_street1.getX() && this_street2.getY() == next_street1.getY())
            {
                line_coordinates.add(this_street2);
            }
            //22
            else if (this_street2.getX() == next_street2.getX() && this_street2.getY() == next_street2.getY())
            {
                line_coordinates.add(this_street2);
            }



        }

        line_coordinates.add(getStopsMap().get(getStopsMap().size()-1).getCoordinate());
        return line_coordinates;
    }


    /*
    @return arraylist of IDs of Coordinates in the order which is specified path of bus, the correct order is important
     */
    public ArrayList<String> transportLinePathIDs()
    {
        Coordinate next_street1 = null;
        Coordinate next_street2 = null;
        Street next_street = null;
        ArrayList<String> line_coordinates_ids = new ArrayList<String>();

        for (int i = 0; i < getStreetsMap().size(); i++)
        {
            Street s = getStreetsMap().get(i);
            Coordinate this_street1 = s.getCoordinates().get(0);
            Coordinate this_street2 = s.getCoordinates().get(2);

            if (i + 1 < getStreetsMap().size())
            {
                next_street = getStreetsMap().get(i+1);
                next_street1 = next_street.getCoordinates().get(0);
                next_street2 = next_street.getCoordinates().get(2);
            }
            else
            {
                break;
            }

            for (Stop stop : getStopsMap())
            {
                if (stop.getStreet().equals(s))
                {
                    line_coordinates_ids.add(stop.getId());
                }
            }

            if (s.getCoordinates().get(1) != null)
            {
                line_coordinates_ids.add(s.getId());
            }

            //11
            if (this_street1.getX() == next_street1.getX() && this_street1.getY() == next_street1.getY())
            {
                line_coordinates_ids.add(s.getId());
            }
            //12
            else if (this_street1.getX() == next_street2.getX() && this_street1.getY() == next_street2.getY())
            {
                line_coordinates_ids.add(s.getId());
            }
            // 21
            else if (this_street2.getX() == next_street1.getX() && this_street2.getY() == next_street1.getY())
            {
                line_coordinates_ids.add(s.getId());
            }
            //22
            else if (this_street2.getX() == next_street2.getX() && this_street2.getY() == next_street2.getY())
            {
                line_coordinates_ids.add(s.getId());
            }


        }

        line_coordinates_ids.add(getStopsMap().get(getStopsMap().size()-1).getId());
        return line_coordinates_ids;
    }

    public ArrayList<Circle> getLineVehicles()
    {
        return this.all_line_vehicles;
    }

    public void addLineVehicles(Circle c)
    {
        this.all_line_vehicles.add(c);
        return;
    }

    public void clearLineVehicles(AnchorPane anchor_pane_map)
    {
        for (Circle c : all_line_vehicles)
        {
            anchor_pane_map.getChildren().remove(c);
        }
    }



    // set the animation of line movement for TransportLine
    public void setLineMovement(Timeline t)
    {
        timeline = t;
    }

    public Timeline getLineMovement()
    {
        return timeline;
    }

    // set color of TransportLine - for their stops, vehicles and streets
    public void setTransportLineColor(Paint p)
    {
        line_color = p;
    }

    public Paint getTransportLineColor()
    {
        return line_color;
    }

    public void setTransportLineSelectedColor(Paint p)
    {
        selected_line_color = p;
    }

    public Paint getTransportLineSelectedColor()
    {
        return selected_line_color;
    }

    /*
        highlight the journey of lines with their own color -
        it means highlight all street from beginning to end when the line is travel through all street
        and highlight only part from stop to end coordinate of street for beginning and end street, because
        the line is not travel through all street but only part of it
    */
    public void highlightTransportLine(AnchorPane anchor_pane_map, ArrayList<Street> streets_list, ArrayList<Line> all_streets_lines)
    {
        Line line1 = null;
        for (Street s : streets_list) {
            if (this.getStreetsMap().get(0).equals(s)) // highlight first street of line
            {
                int begin_stop_x = this.getStopsMap().get(0).getCoordinate().getX();
                int begin_stop_y = this.getStopsMap().get(0).getCoordinate().getY();

                Coordinate begin_street_1 = s.getCoordinates().get(0);
                Coordinate begin_street_2 = s.getCoordinates().get(2);

                Coordinate second_street_1 = this.getStreetsMap().get(1).getCoordinates().get(0);
                Coordinate second_street_2 = this.getStreetsMap().get(1).getCoordinates().get(2);

                if ((begin_street_1.getX() == second_street_1.getX() && begin_street_1.getY() == second_street_1.getY()) || (begin_street_1.getX() == second_street_2.getX() && begin_street_1.getY() == second_street_2.getY())) {
                    System.out.println("Highlight part of first street from first stop");
                    line1 = new Line(begin_stop_x, begin_stop_y, begin_street_1.getX(), begin_street_1.getY());
                    line1.setStroke(this.getTransportLineColor());
                    line1.setStrokeWidth(5);
                    anchor_pane_map.getChildren().addAll(line1);
                } else if ((begin_street_2.getX() == second_street_1.getX() && begin_street_2.getY() == second_street_1.getY()) || (begin_street_2.getX() == second_street_2.getX() && begin_street_2.getY() == second_street_2.getY())) {
                    System.out.println("Highlight part of first street from first stop");
                    line1 = new Line(begin_stop_x, begin_stop_y, begin_street_2.getX(), begin_street_2.getY());
                    line1.setStroke(this.getTransportLineColor());
                    line1.setStrokeWidth(5);
                    anchor_pane_map.getChildren().addAll(line1);
                }
            } else if (this.getStreetsMap().get(this.getStreetsMap().size() - 1).equals(s)) // end street of line
            {
                int end_stop_x = this.getStopsMap().get(this.getStopsMap().size() - 1).getCoordinate().getX();
                int end_stop_y = this.getStopsMap().get(this.getStopsMap().size() - 1).getCoordinate().getY();

                Coordinate end_street_1 = s.getCoordinates().get(0);
                Coordinate end_street_2 = s.getCoordinates().get(2);

                Coordinate nexttolast_street_1 = this.getStreetsMap().get(this.getStreetsMap().size() - 2).getCoordinates().get(0);
                Coordinate nexttolast_street_2 = this.getStreetsMap().get(this.getStreetsMap().size() - 2).getCoordinates().get(2);

                if ((end_street_1.getX() == nexttolast_street_1.getX() && end_street_1.getY() == nexttolast_street_1.getY()) || (end_street_1.getX() == nexttolast_street_2.getX() && end_street_1.getY() == nexttolast_street_2.getY())) {
                    System.out.println("Highlight last street from stop1");
                    line1 = new Line(end_stop_x, end_stop_y, end_street_1.getX(), end_street_1.getY());
                    line1.setStroke(this.getTransportLineColor());
                    line1.setStrokeWidth(5);
                    anchor_pane_map.getChildren().addAll(line1);
                } else if ((end_street_2.getX() == nexttolast_street_1.getX() && end_street_2.getY() == nexttolast_street_1.getY()) || (end_street_2.getX() == nexttolast_street_2.getX() && end_street_2.getY() == nexttolast_street_2.getY())) {
                    System.out.println("Highlight last street from stop1");
                    line1 = new Line(end_stop_x, end_stop_y, end_street_2.getX(), end_street_2.getY());
                    line1.setStroke(this.getTransportLineColor());
                    line1.setStrokeWidth(5);
                    anchor_pane_map.getChildren().addAll(line1);
                }
            } else if (this.getStreetsMap().contains(s)) { // highlight whole street from line
                s.highlightStreet(anchor_pane_map, all_streets_lines, this.getTransportLineColor());
            }
        }
    }

    // create animation of path for particular transportline
    // duration - duration between neighbour coordinates - default 2 seconds
    // stop_duration - duration of waiting in stop - default 1 second
    // affected_points - array of points affected with slowing traffic
    // slow_duration - duration between neighbour coordinates in street affected with slow traffic
    // slow_stop_duration - duration of waiting in stop in street affected with slow traffic
    public Timeline createLineAnimation(AnchorPane anchor_pane_map, int duration, int stop_duration, ArrayList<Coordinate> affected_points, int slow_duration, int slow_stop_duration, EventHandler<MouseEvent> handler, boolean detour_delay)
    {
        // coordinates of path for vehicle on transportline
        ArrayList<Coordinate> line_coordinates = this.transportLinePath();
        // ids of coordinates of path for vehicle on transportline
        ArrayList<String> line_coordinates_ids = this.transportLinePathIDs();
        // all stops for transportline
        List<Stop> line_stops = this.getStopsMap();
        // create vehicle for line (circle)
        Circle vehicle = new Circle(this.getStopsMap().get(0).getCoordinate().getX(), this.getStopsMap().get(0).getCoordinate().getY(), 10);
        vehicle.setStroke(Color.AZURE);
        vehicle.setFill(this.getTransportLineColor());
        vehicle.setStrokeWidth(5);
        addLineVehicles(vehicle);
        vehicle.setOnMouseClicked(handler);

        Timeline timeline = new Timeline();
        int original_duration = duration;
        int original_stop_duration = stop_duration;

        // add all keyframes to timeline - one keyframe means path from one coordinate to another coordinate
        // vehicle waits in stop for 1 seconds and go to another coordinate for 2 seconds (in default mode)
        int delta_time = 0;
        KeyFrame waiting_in_stop = null;
        for (int i = 0; i < line_coordinates.size() - 1; i++) {
            // if we go through street affected by slow traffic
            if (line_coordinates.get(i).isInArray(affected_points) && line_coordinates.get(i+1).isInArray(affected_points))
            {
                // duration between coordinates and duration of waiting in stop is different
                duration = slow_duration;
                stop_duration = slow_stop_duration;
            }
            else
            {
                // else use default duration
                duration = original_duration;
                stop_duration = original_stop_duration;
            }

            for (Stop s : line_stops) {
                // if we are in stop, we wait 'stop_duration' time
                if (line_coordinates.get(i).getX() == s.getCoordinate().getX() && line_coordinates.get(i).getY() == s.getCoordinate().getY()) {
                    waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + stop_duration), // this means waiting in stop for some time
                            new KeyValue(vehicle.centerXProperty(), line_coordinates.get(i).getX()),
                            new KeyValue(vehicle.centerYProperty(), line_coordinates.get(i).getY()));

                    delta_time = delta_time + stop_duration;
                    break;
                }
            }
            // we travelled for 'duration' time
            KeyFrame end = new KeyFrame(Duration.seconds(delta_time + duration), // this means that the path from one coordinate to another lasts 2 seconds
                    new KeyValue(vehicle.centerXProperty(), line_coordinates.get(i + 1).getX()),
                    new KeyValue(vehicle.centerYProperty(), line_coordinates.get(i + 1).getY()));

            if (waiting_in_stop != null) {
                timeline.getKeyFrames().addAll(end, waiting_in_stop);
            } else {
                timeline.getKeyFrames().addAll(end);
            }

            delta_time = delta_time + duration;
        }

        timeline.setCycleCount(Timeline.INDEFINITE); // infinity number of repetitions
        //timeline.setDelay(Duration.seconds(3));
        //this.setLineMovement(timeline); // set movement of specified line
        //timeline.play(); // play final animation

        anchor_pane_map.getChildren().add(vehicle);
        //this.delay = 0;
        if (detour_delay == false)
        {
            this.setDelay(duration, slow_duration, affected_points.size());
        }
        else
        {
            this.delay = this.detour_streets.size() * duration - duration;
        }


        return timeline;
    }

    // only animated part of trasnportline
    public Timeline createPartLineAnimation(int duration, int stop_duration, ArrayList<Coordinate> affected_points, int slow_duration, int slow_stop_duration, Circle vehicle, ArrayList<Coordinate> line_coordinates_part, EventHandler<MouseEvent> handler, boolean detour_delay)
    {
        Timeline affected_timeline = new Timeline();
        int original_duration = duration;
        int original_stop_duration = stop_duration;
        addLineVehicles(vehicle);
        vehicle.setOnMouseClicked(handler);

        // add all keyframes to timeline - one keyframe means path from one coordinate to another coordinate
        // vehicle waits in stop for 1 seconds and go to another coordinate for 2 seconds (in default mode)
        int delta_time = 0;
        KeyFrame waiting_in_stop = null;

        //int affected_stops_count = 0;
        for (int i = 0; i < line_coordinates_part.size() - 1; i++) {
            // if we go through street affected by slow traffic
            if (line_coordinates_part.get(i).isInArray(affected_points) && line_coordinates_part.get(i+1).isInArray(affected_points))
            {
                // duration between coordinates and duration of waiting in stop is different
                duration = slow_duration;
                stop_duration = slow_stop_duration;
            }
            else
            {
                // else use default duration
                duration = original_duration;
                stop_duration = original_stop_duration;
            }

            for (Stop s : this.getStopsMap()) {
                // if we are in stop, we wait 'stop_duration' time
                if (line_coordinates_part.get(i).getX() == s.getCoordinate().getX() && line_coordinates_part.get(i).getY() == s.getCoordinate().getY()) {
                    waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + stop_duration), // this means waiting in stop for some time
                            new KeyValue(vehicle.centerXProperty(), line_coordinates_part.get(i).getX()),
                            new KeyValue(vehicle.centerYProperty(), line_coordinates_part.get(i).getY()));

                    delta_time = delta_time + stop_duration;

                    break;
                }
            }
            // we travelled for 'duration' time
            KeyFrame end = new KeyFrame(Duration.seconds(delta_time + duration), // this means that the path from one coordinate to another lasts 2 seconds
                    new KeyValue(vehicle.centerXProperty(), line_coordinates_part.get(i + 1).getX()),
                    new KeyValue(vehicle.centerYProperty(), line_coordinates_part.get(i + 1).getY()));

            if (waiting_in_stop != null) {
                affected_timeline.getKeyFrames().addAll(end, waiting_in_stop);
            } else {
                affected_timeline.getKeyFrames().addAll(end);
            }

            delta_time = delta_time + duration;
        }

        //timeline.setCycleCount(Timeline.INDEFINITE); // infinity number of repetitions
        this.setLineMovement(affected_timeline); // set movement of specified line
        //timeline.play(); // play final animation
        //this.delay = (slow_duration - duration)*(affected_points.size()-1);
        if (detour_delay == false)
        {
            this.setDelay(duration, slow_duration, affected_points.size());
        }
        else
        {
            this.delay = this.detour_streets.size() * duration - duration;
        }


        return affected_timeline;
    }

    public int setDelay(int duration, int slow_duration, int affected_points_size)
    {
        if (slow_duration != 0)
        {
            this.delay = (slow_duration - duration)*(affected_points_size-1);
        }
        else
        {
            this.delay = 0;
        }

        return this.delay;
    }


    public int getDelay()
    {
        return this.delay;
    }

    public ArrayList<Coordinate> affectTravellingPath(Coordinate actual_c)
    {
        ArrayList<Coordinate> line_coordinates_part = new ArrayList<Coordinate>();

        for (int i = 0; i < this.transportLinePath().size() - 1; i++) {
            Coordinate coordinates1 = this.transportLinePath().get(i);
            Coordinate coordinates2 = this.transportLinePath().get(i + 1);
            String id_coordinates_2 = this.transportLinePathIDs().get(i + 1);

            if (actual_c.isBetweenTwoCoordinates(coordinates1, coordinates2) == true) {
                for (int j = 0; j < this.transportLinePathIDs().size(); j++) {
                    if (j >= this.transportLinePathIDs().indexOf(id_coordinates_2)) {
                        line_coordinates_part.add(this.transportLinePath().get(j));
                    }
                }
            }
        }

        return line_coordinates_part;
    }

    public void printInfoVehicleClick(Circle c, Text lines_info)
    {
        if (this.getLineVehicles().contains(c)) {
            if (c.getFill() == this.getTransportLineColor()) {
                c.setFill(this.getTransportLineSelectedColor());
            } else {
                c.setFill(this.getTransportLineColor());
            }

            lines_info.setText("Line number: " + this.getLineId() + "\n");
            lines_info.setText(lines_info.getText() + "Route: " + this.printRouteStops() + "\n");
            lines_info.setText(lines_info.getText() + "Line delay: +" + this.getDelay() + "\n");

            // get actual coordinates of vehicle
            int vehicle_actual_x = (int) Math.round(c.getCenterX());
            int vehicle_actual_y = (int) Math.round(c.getCenterY());

            Coordinate vehicle_actual_coordinates = new Coordinate(vehicle_actual_x, vehicle_actual_y);

            // print next stop and previous stops of line
            for (int i = 0; i < this.transportLinePath().size() - 1; i++) {
                Coordinate coordinates1 = this.transportLinePath().get(i);
                Coordinate coordinates2 = this.transportLinePath().get(i + 1);
                String id_coordinates_2 = this.transportLinePathIDs().get(i + 1);

                if (vehicle_actual_coordinates.isBetweenTwoCoordinates(coordinates1, coordinates2) == true) {
                    lines_info.setText(lines_info.getText() + "Previous stops:" + "\n");
                    for (int j = 0; j < this.transportLinePathIDs().size(); j++) {
                        if (j < this.transportLinePathIDs().indexOf(id_coordinates_2) && this.transportLinePathIDs().get(j).contains("Stop")) {
                            lines_info.setText(lines_info.getText() + this.transportLinePathIDs().get(j) + " -> ");
                        } else if (this.transportLinePathIDs().get(j).contains("Stop")) {
                            lines_info.setText(lines_info.getText() + "\n" + "Next stop: " + this.transportLinePathIDs().get(j) + "\n");
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public ArrayList<Coordinate> getAffectedPointsTraffic(ArrayList<Line> affected_lines)
    {
        ArrayList<Coordinate> new_affected_points = new ArrayList<Coordinate>();

        for (Street s : this.getStreetsMap()) {
            for (Line l : affected_lines) {
                if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY()) {
                    System.out.println("Street is slower now from line");

                    for (int i = 0; i < this.transportLinePath().size(); i++) {
                        if (this.transportLinePath().get(i).isBetweenTwoCoordinates(s.begin(), s.end()) || (this.transportLinePath().get(i).getX() == s.begin().getX() && this.transportLinePath().get(i).getY() == s.begin().getY()) || (this.transportLinePath().get(i).getX() == s.end().getX() && this.transportLinePath().get(i).getY() == s.end().getY())) {
                            System.out.println("Affected points: " + this.transportLinePath().get(i).getX() + ", " + this.transportLinePath().get(i).getY());
                            new_affected_points.add(this.transportLinePath().get(i));
                        }
                    }
                }
            }
        }

        return new_affected_points;
    }

    public void reopenClosedStreet()
    {
        int detour_index = 0;
        for (Street s : this.getStreetsMap()) {
            if (this.getDetourStreets().contains(s)) {
                detour_index = (this.getStreetsMap().indexOf(s));
                break;
            }
        }

        for (int i = 0; i < this.getDetourStreets().size(); i++)
        {
            this.getStreetsMap().remove(detour_index);
        }

        this.getStreetsMap().add(detour_index, this.getClosedStreet());
        this.getStopsMap().add(this.getClosedStopIndex(), this.getClosedStop());
        this.clearDetourStreet();
    }


}
