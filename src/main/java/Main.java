import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Group;
import java.lang.*;
import java.lang.Override;import java.lang.String;import java.lang.System;import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {
    private final static int circleRadius = 20;
    private static int windowHeight = 1024;
    private static int windowsWidth = 1280;
    AtomicBoolean newCircleWindow = new AtomicBoolean();
    AtomicInteger mouseStartX = new AtomicInteger();
    AtomicInteger mouseStartY = new AtomicInteger();

    //JavaFX application still use the main method.
    //It should only ever contain the call to the launch method
    public static void main(String[] args) {
        launch(args);
    }
    //starting point for the application
    //this is where we put the code for the user interface
    @Override
    public void start(Stage primaryStage) {

        ExperimentManager myManager = new ExperimentManager();
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.setTitle("Physics Engine");
        primaryStage.setMaxHeight(1440);
        primaryStage.setMaxWidth(2560);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);

        ToolPane toolPane = new ToolPane(1024, 40);
        root.getChildren().addAll(toolPane);

        TableView ballsTable = new TableView();
        ballsTable.setEditable(true);
        ballsTable.setTranslateY(40);
        ballsTable.setPrefSize(200, primaryStage.getHeight()-toolPane.getHeight());
        root.getChildren().addAll(ballsTable);


        ObservableList<MyCircle> tableData = FXCollections.observableArrayList();
        ballsTable.setItems(tableData);

        TableColumn id = new TableColumn("ID");
        id.setPrefWidth(100);
        id.setCellValueFactory(new PropertyValueFactory<MyCircle, String>("idString"));

        TableColumn vector = new TableColumn("Vector");
        vector.setPrefWidth(100);

        TableColumn vecX = new TableColumn("X");
        //vecX.setCellValueFactory(new PropertyValueFactory<MyCircle, String>("vecX"));
        vecX.setPrefWidth(50);

        TableColumn vecY = new TableColumn("Y");
        vecY.setPrefWidth(50);

        vector.getColumns().addAll(vecX, vecY);
        ballsTable.getColumns().addAll(id, vector);
        ballsTable.getColumns().add(id);
        Canvas canvas = new Canvas(windowsWidth, windowHeight);
        myManager.movingObjects.getChildren().add(canvas);


        root.getChildren().add(myManager.movingObjects);
        myManager.movingObjects.toBack();

        primaryStage.setScene(scene);
        primaryStage.show();


        /*
        * Moving plane handlers
        * */
        myManager.movingObjects.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().toString().equals("SECONDARY")) {
                    mouseStartX.set(((int) event.getSceneX()));
                    mouseStartY.set(((int) event.getSceneY()));
                }
            }
        });
        myManager.movingObjects.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().toString().equals("SECONDARY")) {
                    int x = (int) event.getSceneX();
                    int y = (int) event.getSceneY();
                    myManager.stop();
                    for (Thread t : myManager.myThreads) {
                        t.stop();
                    }
                    for (MyCircle i : myManager.figures) {
                        i.translate((x - mouseStartX.get()), (y - mouseStartY.get()));
                    }
                    mouseStartX.set(x);
                    mouseStartY.set(y);
                    myManager.myRun();
                }
            }
        });

        /*
        * Adding/modifying circle handler
        * */
        myManager.movingObjects.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /*
                * Check which mouse button was used for click, PRIMARY is left
                * */
                if (event.getButton().toString().equals("PRIMARY") &&
                        !newCircleWindow.get()) {

                    /*
                    * Check if clicked on existing wheel
                    * */
                    boolean collision = false;
                    for (MyCircle i : myManager.figures) {
                        if (i.inCollisionProximity(event.getSceneX(), event.getSceneY())) {
                            System.out.println("Collision");
                            collision = true;
                        }
                    }
                    if (!collision) {
                        /*
                        * Init new Dialong window with gridPane and add all labels, buttons and textFields
                        * */
                        newCircleWindow.set(true);
                        NewCircleDialog newCircleDialog = new NewCircleDialog(primaryStage);

                        /*
                        * Create new Circle with proper x, y position
                        * */
                        MyCircle circle = new MyCircle(event.getSceneX(), event.getSceneY(), 0, circleRadius);

                        /*
                        * Set action for clicking ADD button
                        * */
                        newCircleDialog.addCircleButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                circle.vecX = newCircleDialog.getXValue();
                                circle.vecY = newCircleDialog.getYValue();
                                circle.mass = newCircleDialog.getMassValue();
                                myManager.movingObjects.getChildren().add(circle);
                                myManager.figures.add(circle);
                                //tableData.add(circle);
                                System.out.println("Circle with ID: " + circle.id + " added.");
                                //System.out.println(tableData.size());
                                newCircleDialog.hide();
                                newCircleWindow.set(false);
                            }
                        });
                        newCircleDialog.show();
                    }
                }
            }
        });

        toolPane.startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!myManager.isGoing()) {
                    myManager.myRun();
                    toolPane.startButton.setText("Stop");
                } else {
                    myManager.stop();
                    for (Thread t : myManager.myThreads) {
                        t.stop();
                    }
                    toolPane.startButton.setText("Continue");
                }
            }
        });
        toolPane.restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myManager.restart();
                toolPane.startButton.setText("Start");
                myManager.movingObjects.getChildren().clear();
            }
        });

        toolPane.timeScroll.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                synchronized (myManager.scrollValue){
                    myManager.scrollValue = toolPane.timeScroll.getValue();
                }
            }
        });
    }
}