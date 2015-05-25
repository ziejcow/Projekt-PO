


//Imports are listed in full to show what's being used

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;import javafx.scene.control.Label;import javafx.scene.control.ScrollBar;import javafx.scene.control.TextField;import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import java.lang.*;import java.lang.Integer;import java.lang.Override;import java.lang.String;import java.lang.System;import java.lang.Thread;import java.util.Vector;
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

        AnchorPane toolPane = new AnchorPane();
        toolPane.setPrefSize(1024, 40);
        root.getChildren().addAll(toolPane);

        Button startButton = new Button("Start");
        startButton.setPrefSize(100, 40);
        toolPane.getChildren().addAll(startButton);

        Button restartButton = new Button("Restart");
        restartButton.setPrefSize(100, 40);
        restartButton.setTranslateX(100);
        toolPane.getChildren().addAll(restartButton);

        Label timeScrollLabel = new Label("Time: ");
        timeScrollLabel.setPrefSize(100, 20);
        timeScrollLabel.setTranslateX(200);
        toolPane.getChildren().addAll(timeScrollLabel);

        ScrollBar timeScroll = new ScrollBar();
        timeScroll.setMax(100);
        timeScroll.setMin(-100);
        timeScroll.setValue(0);
        timeScroll.setPrefSize(200, 20);
        timeScroll.setTranslateY(20);
        timeScroll.setTranslateX(200);
        toolPane.getChildren().addAll(timeScroll);

        TableView ballsTable = new TableView();
        ballsTable.setEditable(true);
        ballsTable.setTranslateY(40);
        ballsTable.setPrefSize(200, primaryStage.getHeight()-toolPane.getHeight());
        root.getChildren().addAll(ballsTable);

        TableColumn id = new TableColumn("ID");
        id.setPrefWidth(100);
        TableColumn vector = new TableColumn("Vector");
        vector.setPrefWidth(100);
        TableColumn vecX = new TableColumn("X");
        vecX.setPrefWidth(50);
        TableColumn vecY = new TableColumn("Y");
        vecY.setPrefWidth(50);
        vector.getColumns().addAll(vecX, vecY);
        ballsTable.getColumns().addAll(id, vector);


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
                        final Stage settingsDialog = new Stage();
                        settingsDialog.initOwner(primaryStage);
                        settingsDialog.setTitle("Adding new circle");
                        GridPane gridPane = new GridPane();
                        Scene dialogScene = new Scene(gridPane, 250, 120);

                        Label xLabel = new Label(" X: ");
                        TextField xTextField = new TextField("10");
                        gridPane.add(xLabel, 0, 0);
                        gridPane.add(xTextField, 1, 0);

                        Label yLabel = new Label(" Y: ");
                        TextField yTextField = new TextField("10");
                        gridPane.add(yLabel, 0, 1);
                        gridPane.add(yTextField, 1, 1);

                        Label massLabel = new Label(" Mass: ");
                        TextField massTextField = new TextField("10");
                        gridPane.add(massLabel, 0, 2);
                        gridPane.add(massTextField, 1, 2);

                        Button addCircleButton = new Button("ADD");
                        gridPane.add(addCircleButton, 1, 3);


                        /*
                        * Create new Circle with proper x, y position
                        * */
                        MyCircle circle = new MyCircle(event.getSceneX(), event.getSceneY(), 0, circleRadius);


                        /*
                        * Set action for clicking ADD button
                        * */

                        addCircleButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                circle.vecX = Integer.parseInt(xTextField.getText());
                                circle.vecY = Integer.parseInt(yTextField.getText());
                                circle.mass = Integer.parseInt(massTextField.getText());
                                myManager.movingObjects.getChildren().add(circle);
                                myManager.figures.add(circle);
                                System.out.println("Circle added");
                                settingsDialog.hide();
                                newCircleWindow.set(false);
                            }
                        });
                        settingsDialog.setScene(dialogScene);
                        settingsDialog.show();
                    }
                }
            }
        });

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!myManager.isGoing()) {
                    myManager.myRun();
                    startButton.setText("Stop");
                } else {
                    myManager.stop();
                    for (Thread t : myManager.myThreads) {
                        t.stop();
                    }
                    startButton.setText("Continue");
                }
            }
        });
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myManager.restart();
                startButton.setText("Start");
                myManager.movingObjects.getChildren().clear();
            }
        });

        timeScroll.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                synchronized (myManager.scrollValue){
                    myManager.scrollValue = timeScroll.getValue();
                }
            }
        });
    }
}