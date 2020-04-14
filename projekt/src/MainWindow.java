// Import classes
import javafx.geometry.Pos;
import maps.Coordinate;
import maps.Street;
import maps.Stop;
import maps.TransportLine;
// Import java classes
import javafx.scene.image.Image;
import java.awt.geom.Point2D;
import java.io.*;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.input.*;
import javafx.event.*;
import java.util.*;
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.MouseInfo;
import javafx.scene.Node;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args); // launch app
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TASK - add sliding along map and zoom of map and other controllers of GUI
        BorderPane root = new BorderPane(); // create BorderPane as root element
        ScrollPane scroll_pane_map = new ScrollPane(); // create scroll pane for map
        VBox anchor_pane_menu = new VBox(); // create anchor pane for side menu
        ScrollPane scroll_pane_downbox = new ScrollPane(); // create down box for info about lines

        // put two components to BorderPane - scroll_pane_map for window with map and anchor_pane_menu for menu
        root.setLeft(scroll_pane_map);
        root.setRight(anchor_pane_menu);
        root.setBottom(scroll_pane_downbox);

        scroll_pane_downbox.setPrefWidth(400);
        scroll_pane_downbox.setPrefHeight(150);

        // create text field in scroll_pane_downbox for info about lines
        Text lines_info = new Text();
        scroll_pane_downbox.setContent(lines_info);

        AnchorPane anchor_pane_map = new AnchorPane(); // set anchor pane in scroll_pane_map
        scroll_pane_map.setContent(anchor_pane_map);
        anchor_pane_map.setPrefWidth(500);
        anchor_pane_map.setPrefHeight(500);
        scroll_pane_map.setPannable(true); // pannable map
        scroll_pane_map.setPrefViewportWidth(500);
        scroll_pane_map.setPrefViewportHeight(500);

        anchor_pane_menu.setPrefWidth(600);
        anchor_pane_menu.setPrefHeight(300);
        anchor_pane_menu.setSpacing(15);
        anchor_pane_menu.setAlignment(Pos.CENTER);

        Button restart_timer = new Button("Restart timer");

        Label traffic_label = new Label("Mark streets affected with traffic in map");
        Label traffic_choose = new Label("Choose higher size of traffic on marked streets (default 2):");

        TextField box_traffic = new TextField();
        box_traffic.setAlignment(Pos.CENTER);
        box_traffic.setMaxWidth(150);
        box_traffic.setPromptText("Set 2 for default");

        Button traffic_button = new Button("Show movement with traffic");
        Label closed_streets_label = new Label("Mark closed streets in map");
        Button closed_streets_button = new Button("Close street (streets)");
        Label detour_label = new Label("Mark detour for closed street");
        Button detour_streets_button = new Button("Save detour");

        anchor_pane_menu.getChildren().addAll(restart_timer, traffic_label, traffic_choose, box_traffic, traffic_button, closed_streets_label, closed_streets_button, detour_label, detour_streets_button);

        // zoom map
        anchor_pane_map.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.05;
                        double deltaY = event.getDeltaY();

                        if (deltaY < 0){
                            zoomFactor = 0.95;
                        }
                        anchor_pane_map.setScaleX(anchor_pane_map.getScaleX() * zoomFactor);
                        anchor_pane_map.setScaleY(anchor_pane_map.getScaleY() * zoomFactor);
                        event.consume();
                    }
                });

        Scene scene = new Scene(root, 1100, 700); // set width and height of window

        File file = new File("C:/Users/forto/IdeaProjects/proj/lib/new_map.png");
        BackgroundImage myBI = new BackgroundImage(new Image(file.toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        anchor_pane_map.setBackground(new Background(myBI)); // set map as background, with no repeat and also some free space for TO DO GUI components

        // beginning of coordinates [0,0] is in left upon corner of whole window and also it is beginning for image


        /*
        Point2D p = MouseInfo.getPointerInfo().getLocation();

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // show specific coordinates after mouse clicking in console - useful for positioning of streets and stops
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
            }
        });

         */



        ArrayList<Street> streets_list = setMapStreets(); // Street objects created from file

        /*
        highlight all street objects in map - for right angle streets need to create two lines instead one for this type of streets
         */
        ArrayList<Line> all_streets_lines = new ArrayList<Line>();

        for (Street s : streets_list) {
            s.highlightStreet(anchor_pane_map,all_streets_lines, Color.LIGHTGREY);
        }

        ArrayList<Stop> stops_list = setMapStops(streets_list); // objects of all Stop are created from file

        for (Stop stop : stops_list) //  highlight all stop objects in map
        {
            stop.highlightStop(anchor_pane_map, Color.LIGHTGREY);
        }

        // create ScheduleLine objects from files
        TransportLine transportLine = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule.txt");
        TransportLine transportLine2 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule2.txt");
        TransportLine transportLine3 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule3.txt");
        TransportLine transportLine4 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule4.txt");

        // each line is marked with different color
        transportLine.setTransportLineColor(Color.SKYBLUE);
        transportLine2.setTransportLineColor(Color.SANDYBROWN);
        transportLine3.setTransportLineColor(Color.PINK);
        transportLine4.setTransportLineColor(Color.VIOLET);

        transportLine.setTransportLineSelectedColor(Color.DARKBLUE);
        transportLine2.setTransportLineSelectedColor(Color.BROWN);
        transportLine3.setTransportLineSelectedColor(Color.HOTPINK);
        transportLine4.setTransportLineSelectedColor(Color.DARKVIOLET);

        ArrayList<TransportLine> all_transport_lines_list = new ArrayList<TransportLine>();
        // create list of all TransportLine objects
        all_transport_lines_list.add(transportLine);
        all_transport_lines_list.add(transportLine2);
        all_transport_lines_list.add(transportLine3);
        all_transport_lines_list.add(transportLine4);

        for (Stop stop : stops_list) // highlight stop object from transport lines with their own color
        {
            for (TransportLine t : all_transport_lines_list) {
                if (t.getStopsMap().contains(stop)) {
                    stop.highlightStop(anchor_pane_map, t.getTransportLineColor());
                }
            }
        }

        /*
        highlight the journey of lines with their own color -
        it means highlight all street from beginning to end when the line is travel through all street
        and highlight only part from stop to end coordinate of street for beginning and end street, because
        the line is not travel through all street but only part of it
         */

        for (TransportLine t : all_transport_lines_list) {
            t.highlightTransportLine(anchor_pane_map, streets_list, all_streets_lines);
        }


        //ArrayList<Circle> all_line_original_vehicles = new ArrayList<Circle>();
        /*
        create a vehicle (circle) for every TransportLine object and move along the path of TransportLine
         */
        EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getSource() instanceof Circle) {
                    Circle c = (Circle) e.getSource();

                    for (TransportLine t : all_transport_lines_list) {
                        if (t.getLineVehicles().contains(c)) {
                            if (c.getFill() == t.getTransportLineColor()) {
                                c.setFill(t.getTransportLineSelectedColor());
                            } else {
                                c.setFill(t.getTransportLineColor());
                            }

                            //System.out.println("This is line number " + t.getLineId() + " with route " + t.printRoute());
                            lines_info.setText("Line number: " + t.getLineId() + "\n");
                            lines_info.setText(lines_info.getText() + "Route: " + t.printRoute() + "\n");

                            // get actual coordinates of vehicle
                            int vehicle_actual_x = (int) Math.round(c.getCenterX());
                            int vehicle_actual_y = (int) Math.round(c.getCenterY());

                            Coordinate vehicle_actual_coordinates = new Coordinate(vehicle_actual_x, vehicle_actual_y);

                            // print next stop and previous stops of line
                            for (int i = 0; i < t.transportLinePath().size() - 1; i++) {
                                Coordinate coordinates1 = t.transportLinePath().get(i);
                                Coordinate coordinates2 = t.transportLinePath().get(i + 1);
                                String id_coordinates_2 = t.transportLinePathIDs().get(i + 1);

                                if (vehicle_actual_coordinates.isBetweenTwoCoordinates(coordinates1, coordinates2) == true) {
                                    //System.out.println("Previous stops:");
                                    lines_info.setText(lines_info.getText() + "Previous stops:" + "\n");
                                    for (int j = 0; j < t.transportLinePathIDs().size(); j++) {
                                        if (j < t.transportLinePathIDs().indexOf(id_coordinates_2) && t.transportLinePathIDs().get(j).contains("Stop")) {
                                            //System.out.println(t.transportLinePathIDs().get(j));
                                            lines_info.setText(lines_info.getText() + t.transportLinePathIDs().get(j) + " -> ");
                                        } else if (t.transportLinePathIDs().get(j).contains("Stop")) {
                                                //System.out.println("Next stop is " + t.transportLinePathIDs().get(j));
                                                lines_info.setText(lines_info.getText() + "\n" + "Next stop: " + t.transportLinePathIDs().get(j) + "\n");
                                                break;
                                            }
                                        }
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        };

        ArrayList<Coordinate> affected_points = new ArrayList<Coordinate>();
        for (TransportLine t : all_transport_lines_list) {
            Timeline timeline = t.createLineAnimation(anchor_pane_map, 2,1, affected_points, 0, 0, handler);
            timeline.play();
            t.setLineMovement(timeline);
        }


        for (Line l : all_streets_lines)
        {
            l.setOnMouseClicked(new EventHandler<MouseEvent>() { // if mouse clicked on some Street object
                @Override
                public void handle(MouseEvent event) {

                    boolean default_color = true;
                    if (l.getStroke().equals(Color.BLACK) == false)
                    {
                        l.setStroke(Color.BLACK);
                    }
                    else
                    {
                        for (TransportLine t : all_transport_lines_list) {
                            for (Street s : t.getStreetsMap())
                            {
                                if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY())
                                {
                                    l.setStroke(t.getTransportLineColor());
                                    default_color = false;
                                }
                            }
                        }

                        if (default_color == true)
                        {
                            l.setStroke(Color.LIGHTGREY);
                        }
                    }
                }
            });
        }

        restart_timer.setOnAction(event -> {
            for (TransportLine t : all_transport_lines_list)
            {
                t.getLineMovement().stop();
                t.clearLineVehicles(anchor_pane_map);

               // t.highlightTransportLine(anchor_pane_map, streets_list, all_streets_lines);
                Timeline timeline = t.createLineAnimation(anchor_pane_map, 2,1, affected_points, 0, 0, handler);
                timeline.play();
                t.setLineMovement(timeline);
            }
        });

        traffic_button.setOnAction(event -> {
            try {
                int traffic_size = Integer.parseUnsignedInt(box_traffic.getText());

                ArrayList<Line> affected_lines = new ArrayList<Line>();
                for (Line l : all_streets_lines) {
                    if (l.getStroke().equals(Color.BLACK)) {
                        affected_lines.add(l);
                    }
                }

                for (TransportLine t : all_transport_lines_list) {
                    ArrayList<Coordinate> new_affected_points = new ArrayList<Coordinate>();

                    for (Street s : t.getStreetsMap()) {
                        for (Line l : affected_lines) {
                            if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY()) {
                                System.out.println("Street is slower now from line");

                                for (int i = 0; i < t.transportLinePath().size(); i++) {
                                    if (t.transportLinePath().get(i).isBetweenTwoCoordinates(s.begin(), s.end()) || (t.transportLinePath().get(i).getX() == s.begin().getX() && t.transportLinePath().get(i).getY() == s.begin().getY()) || (t.transportLinePath().get(i).getX() == s.end().getX() && t.transportLinePath().get(i).getY() == s.end().getY())) {
                                        System.out.println("Affected points: " + t.transportLinePath().get(i).getX() + ", " + t.transportLinePath().get(i).getY());
                                        new_affected_points.add(t.transportLinePath().get(i));
                                    }
                                }
                            }
                        }
                    }

                    t.getLineMovement().stop();
                    System.out.println(t.getLineId());

                    Circle source_vehicle = t.getLineVehicles().get(t.getLineVehicles().size() - 1);

                    int actual_x = 0;
                    int actual_y = 0;

                    for (Node n : anchor_pane_map.getChildren())
                    {
                        if (n.equals(source_vehicle))
                        {
                            Circle circle = (Circle) n;
                            actual_x = (int) Math.round(circle.getCenterX());
                            actual_y = (int) Math.round(circle.getCenterY());
                            break;
                        }
                    }
                    System.out.println(source_vehicle.getCenterX());
                    System.out.println(source_vehicle.getCenterY());

                    anchor_pane_map.getChildren().remove(source_vehicle);

                    Circle affected_vehicle = new Circle(actual_x, actual_y, 10);
                    affected_vehicle.setStroke(Color.AZURE);
                    affected_vehicle.setFill(t.getTransportLineSelectedColor());
                    affected_vehicle.setStrokeWidth(5);
                    anchor_pane_map.getChildren().addAll(affected_vehicle);
                    Coordinate actual_c = new Coordinate(actual_x, actual_y);

                    ArrayList<Coordinate> line_coordinates_part = new ArrayList<Coordinate>();

                    for (int i = 0; i < t.transportLinePath().size() - 1; i++) {
                        Coordinate coordinates1 = t.transportLinePath().get(i);
                        Coordinate coordinates2 = t.transportLinePath().get(i + 1);
                        String id_coordinates_2 = t.transportLinePathIDs().get(i + 1);

                        if (actual_c.isBetweenTwoCoordinates(coordinates1, coordinates2) == true) {

                            for (int j = 0; j < t.transportLinePathIDs().size(); j++) {
                                if (j >= t.transportLinePathIDs().indexOf(id_coordinates_2)) {
                                    System.out.println(t.transportLinePathIDs().get(j));
                                    line_coordinates_part.add(t.transportLinePath().get(j));
                                }
                            }

                        }

                    }

                    Timeline affected_timeline = t.createPartLineAnimation(2, 1, new_affected_points, traffic_size, 2, affected_vehicle, line_coordinates_part, handler);
                    affected_timeline.play();

                    Timeline new_timeline = t.createLineAnimation(anchor_pane_map, 2, 1, new_affected_points, traffic_size, 2, handler);
                    t.getLineMovement().setOnFinished(e -> {
                        new_timeline.play();
                        anchor_pane_map.getChildren().remove(affected_vehicle);
                    });
                    t.setLineMovement(new_timeline); // set movement of specified line
                }


            }
            catch (Exception e)
            {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Bad parameters");
                alert.setHeaderText("Use unsigned int as values");
                alert.showAndWait();
            }
        }
       );

        closed_streets_button.setOnAction(event -> {
            for (Line l : all_streets_lines) {
                if (l.getStroke().equals(Color.BLACK))
                {
                    l.setStroke(Color.RED);
                }
                else
                {
                    for (TransportLine t : all_transport_lines_list) {
                        for (Street s : t.getStreetsMap())
                        {
                            if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY())
                            {
                                l.setStroke(t.getTransportLineColor());
                            }
                        }
                    }
                }


            }

            //anchor_pane_menu.getChildren().addAll(detour_label, detour_streets_button);

        }
        );

        detour_streets_button.setOnAction(event ->
        {
            //ArrayList<Line> closed_lines = new ArrayList<Line>();
            Line closed_line = null;
            ArrayList<Line> detour_lines = new ArrayList<Line>();
            ArrayList<Integer> closed_lines_indexes = new ArrayList<Integer>();
            ArrayList<Coordinate> detour_affected_points = new ArrayList<Coordinate>();
            for (Line l : all_streets_lines) {
                if (l.getStroke().equals(Color.RED))
                {
                    //closed_lines.add(l);
                    closed_line = l;
                }
                else if (l.getStroke().equals(Color.BLACK)) {
                    detour_lines.add(l);
                    l.setStroke(Color.GREEN);
                } else {
                    for (TransportLine t : all_transport_lines_list) {
                        for (Street s : t.getStreetsMap()) {
                            if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY()) {
                                l.setStroke(t.getTransportLineColor());
                            }
                        }
                    }
                }
            }
            /*
            for (Line l : closed_lines)
            {
                System.out.println(l);
            }

             */

            for (Line l : detour_lines)
            {
                System.out.println(l);
            }

            //System.out.println(closed_line);

            for (TransportLine t : all_transport_lines_list) {
                int closed_street_index = -1;
                for (Street s : t.getStreetsMap()) {
                        if (s.begin().getX() == closed_line.getStartX() && s.begin().getY() == closed_line.getStartY() && s.end().getX() == closed_line.getEndX() && s.end().getY() == closed_line.getEndY()) {
                            closed_street_index = t.getStreetsMap().indexOf(s);
                            System.out.println(closed_street_index);
                            //closed_lines_indexes.add(closed_street_index);
                           // t.getStreetsMap().remove(s);

                            /*
                            for (Street detour_street : t.getStreetsMap())
                            {
                                for (Line detour_line : detour_lines)
                                {
                                    if (detour_street.begin().getX() == detour_line.getStartX() && detour_street.begin().getY() == detour_line.getStartY() && detour_street.end().getX() == detour_line.getEndX() && detour_street.end().getY() == detour_line.getEndY())
                                    {
                                        t.getStreetsMap().add(closed_street_index, detour_street);
                                    }

                                }
                            }
                            */
                    }
                }

                if (closed_street_index == -1)
                {
                    continue;
                }


                t.getStreetsMap().remove(closed_street_index);

                for (Street detour_street : streets_list)
                {
                    for (Line detour_line : detour_lines)
                    {
                        if (detour_street.begin().getX() == detour_line.getStartX() && detour_street.begin().getY() == detour_line.getStartY() && detour_street.end().getX() == detour_line.getEndX() && detour_street.end().getY() == detour_line.getEndY())
                        {
                            System.out.println(detour_street.getId());
                            t.getStreetsMap().add(closed_street_index, detour_street);
                            closed_street_index++;
                        }

                    }
                }


                Timeline timeline = t.createLineAnimation(anchor_pane_map, 2,1, detour_affected_points, 0, 0, handler);
                timeline.play();
                //t.setLineMovement(timeline);
            }



        });



        stage.setScene(scene);
        stage.setResizable(false);
        stage.show(); // show GUI scene
    }

    /*
    method for loading streets IDs and their coordinates from file, and create objects of all Streets in map
    @return list of all Street objects
     */
    public ArrayList<Street> setMapStreets() throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/forto/IdeaProjects/proj/lib/streetsCoordinates.txt"));
        String line = null;
        ArrayList<Street> streets_list = new ArrayList<Street>();

        while ((line = br.readLine()) != null)
        {
            Coordinate c3 = null;
            String street_coordinates3 = null;
            int street_coordinates3_x = -1;
            int street_coordinates3_y = -1;
            boolean right_angle_street = false;

            String street_id = line.substring(0,(line.indexOf("-"))).trim();
            String street_coordinates1 = line.substring((line.indexOf("-") + 1),line.indexOf(";")).trim();
            String street_coordinates2 = line.substring((line.indexOf(";") + 1)).trim();

            if (street_coordinates2.contains(";"))
            {
                street_coordinates3 = (street_coordinates2.substring((street_coordinates2.indexOf(";") + 1)).trim());
                street_coordinates2 = (street_coordinates2.substring(0,(street_coordinates2.indexOf(";"))).trim());
                right_angle_street = true;
            }

            int street_coordinates1_x = Integer.parseInt(street_coordinates1.substring(1, (street_coordinates1.indexOf(","))));
            int street_coordinates1_y = Integer.parseInt(street_coordinates1.substring(street_coordinates1.indexOf(",")+1,street_coordinates1.length()-1));

            int street_coordinates2_x = Integer.parseInt(street_coordinates2.substring(1, (street_coordinates2.indexOf(","))));
            int street_coordinates2_y = Integer.parseInt(street_coordinates2.substring(street_coordinates2.indexOf(",")+1,street_coordinates2.length()-1));

            if (right_angle_street == true) // street with right angle - three init points
            {
                street_coordinates3_x = Integer.parseInt(street_coordinates3.substring(1, (street_coordinates3.indexOf(","))));
                street_coordinates3_y = Integer.parseInt(street_coordinates3.substring(street_coordinates3.indexOf(",")+1,street_coordinates3.length()-1));
            }

            Coordinate c1 = new Coordinate(street_coordinates1_x, street_coordinates1_y);
            Coordinate c2 = new Coordinate(street_coordinates2_x, street_coordinates2_y);

            if (right_angle_street == true)
            {
                c3 = new Coordinate(street_coordinates3_x, street_coordinates3_y);

            }

            // create street object in map
            if (right_angle_street == false)
            {
                streets_list.add(Street.defaultStreet(street_id, c1, c2));
            }
            else if (right_angle_street == true)
            {
                streets_list.add(Street.defaultStreet(street_id, c1, c2, c3));
            }
        }

        return streets_list;
    }

    /*
    method for loading stops IDs and their coordinates from file, and create objects of all Stops in map
    @arguments list of all streets in map
    @return list of all Stop objects
     */
    public ArrayList<Stop> setMapStops(ArrayList<Street> streetArrayList) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/forto/IdeaProjects/proj/lib/stopsCoordinates.txt"));
        String line = null;
        ArrayList<Stop> stops_list = new ArrayList<Stop>();

        while ((line = br.readLine()) != null)
        {
            String stop_id = line.substring(0,(line.indexOf("-"))).trim();
            String stop_coordinates = line.substring((line.indexOf("-") + 1),line.indexOf(";")).trim();

            int stop_coordinates_x = Integer.parseInt(stop_coordinates.substring(1, (stop_coordinates.indexOf(","))));
            int stop_coordinates_y = Integer.parseInt(stop_coordinates.substring(stop_coordinates.indexOf(",")+1,stop_coordinates.length()-1));

            String street_of_stop_id = line.substring(line.indexOf(";") + 1).trim();

            Coordinate c1 = new Coordinate(stop_coordinates_x, stop_coordinates_y);
            Stop stop = Stop.defaultStop(stop_id, c1);

            for (Street s : streetArrayList)
            {
                if (s.getId().equals(street_of_stop_id))
                {
                    if (s.getCoordinates().get(1) == null)
                    {
                        System.out.println("Normal street");
                        s.addStop(stop, false);
                        stops_list.add(stop);
                    }
                    else
                    {
                        System.out.println("Right angle street");
                        s.addStop(stop, true);
                        stops_list.add(stop);
                    }

                    /*
                    if (s.getId().equals("Street22"))
                    {
                        System.out.println(s.getStops());
                    }

                     */


                }

            }
        }

        return stops_list;
    }

    /*
    method for loading line ID and their stops and streets without stops from file, and create objects TransportLine (aka Line in hw2)
    @arguments list of all stops in map
    @arguments list of all Street objects
    @return TransportLine object
     */
    public TransportLine setScheduleLines(ArrayList<Street> streetArrayList, ArrayList<Stop> stopArrayList, String filename_path) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(filename_path));
        String line = null;

        line = br.readLine();
        String line_id = line.substring(line.indexOf("-")+1).trim();
        System.out.println(line_id);
        TransportLine transport_line = TransportLine.defaultLine();
        transport_line.setLineId(line_id);
        System.out.println(transport_line.getLineId());

        while ((line = br.readLine()) != null) {
            if (line.contains("Stop"))
            {
                for (Stop stop : stopArrayList)
                {
                    if (stop.getId().equals(line)) {
                        transport_line.addStop(stop);
                    }
                }
            }
            else if (line.contains("Street"))
            {
                for (Street s : streetArrayList)
                {
                    if (s.getId().equals(line))
                    {
                        transport_line.addStreet(s);
                    }
                }
            }
        }

        transport_line.printRoute();

        return transport_line;
    }
}
