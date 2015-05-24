


//Imports are listed in full to show what's being used

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;import javafx.scene.control.Label;import javafx.scene.control.ScrollBar;import javafx.scene.control.TextField;import javafx.scene.input.MouseEvent;
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
        Group grid = new Group();
        Scene scene = new Scene(grid, 200, 300);
        myManager.myThreads = new Vector<>();
        VBox buttons = new VBox();
        Button start = new Button("Start");
        AtomicInteger mouseStartX = new AtomicInteger();
        AtomicInteger mouseStartY = new AtomicInteger();
        Button restart = new Button("Restart");
        Button addFigure = new Button("Add Figure");
        Button addBall = new Button("Add Ball");
        addBall.setVisible(false);
        myManager.figures = new Vector<>();
        Canvas canvas = new Canvas(windowsWidth, windowHeight);
        myManager.movingObjects.getChildren().add(canvas);

        /*
        * Moving plane handlers
        * */
        myManager.movingObjects.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().toString().equals("SECONDARY")){
                    mouseStartX.set(((int) event.getSceneX()));
                    mouseStartY.set(((int) event.getSceneY()));
                }
            }
        });
        myManager.movingObjects.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().toString().equals("SECONDARY")){
                    int x = (int)event.getSceneX();
                    int y = (int) event.getSceneY();
                    myManager.stop();
                    for (Thread t : myManager.myThreads) {
                        t.stop();
                    }
                    for (MyCircle i : myManager.figures) {
                       i.translate((x-mouseStartX.get()), (y-mouseStartY.get()));
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
                if(event.getButton().toString().equals("PRIMARY") && newCircleWindow.compareAndSet(false, true)){
//                    newCircleWindow.set(true);
                    // System.out.println();
                    /*
                    * Check if clicked on existing wheel
                    * */
                    boolean collision = false;
                    for(MyCircle i : myManager.figures){
                        if(i.inCircle(event.getSceneX(), event.getSceneY())){
                            System.out.println("Collision");
                            collision = true;
                        }
                    }
                    if(!collision) {
                        /*
                        * Init new Dialong window with gridPane and add all labels, buttons and textFields
                        * */
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

        ScrollBar scroll = new ScrollBar();
        scroll.setMax(100);
        scroll.setMin(-100);
        scroll.setValue(0);
        start.setMinWidth(200);
        restart.setMinWidth(200);

        addBall.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        addFigure.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addBall.setVisible(true);
            }
        });

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!myManager.isGoing()) {

                    myManager.myRun();
                    //myManager.myRun();
                    start.setText("Stop");
                } else {
                    myManager.stop();
                    for (Thread t : myManager.myThreads) {
                        t.stop();
                    }
                    start.setText("Continue");
                }
            }
        });
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myManager.restart();
                start.setText("Start");
                myManager.movingObjects.getChildren().clear();
                //myManager.movingObjects.getChildren() = new FXCollections.observableList<Circle>();
            }
        });


        myManager.setScroll(scroll);
        buttons.getChildren().add(start);
        buttons.getChildren().add(restart);
        Label startLabel = new Label("Start");
        startLabel.setMinWidth(200);
        buttons.getChildren().add(startLabel);
        buttons.getChildren().add(scroll);


        grid.getChildren().add(buttons);
        grid.getChildren().add(myManager.movingObjects);
        myManager.movingObjects.toBack();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}