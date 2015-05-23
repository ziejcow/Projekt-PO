


//Imports are listed in full to show what's being used

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;import javafx.scene.control.Label;import javafx.scene.control.ScrollBar;import javafx.scene.control.TextField;import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import java.lang.*;import java.lang.Integer;import java.lang.Override;import java.lang.String;import java.lang.System;import java.lang.Thread;import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {


    private final static int circleRadius = 20;
    private static int windowHeight = 1024;
    private static int windowsWidth = 1280;
    //JavaFX applicatoin still use the main method.
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
                double width = scene.getWidth();
                double height = scene.getHeight();
                if(event.getButton().toString().equals("SECONDARY")){
                    int x = (int)event.getSceneX();
                    int y = (int) event.getSceneY();
                    myManager.stop();
                    for (Thread t : myManager.myThreads) {
                        t.stop();
                    }
                    for (MyCircle i : myManager.figures) {
                       //System.out.println( (x-mouseStartX.get()) + " " + (y-mouseStartY.get()) );
                       i.translate((x-mouseStartX.get()), (y-mouseStartY.get()));
                       // i.translate( (event.getSceneX() - width/2) / 100, (event.getSceneY() - height/2) / 100);
                    }
                    mouseStartX.set(x);
                    mouseStartY.set(y);
                    myManager.myRun();
                }
            }
        });
        myManager.movingObjects.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().toString().equals("PRIMARY")){
                    MyCircle temp = new MyCircle();
                    temp.setCenterX(event.getSceneX());
                    temp.setCenterY(event.getSceneY());
                    temp.setRadius(circleRadius);
                    System.out.println("dziala");
                    myManager.movingObjects.getChildren().add(temp);

                    final Stage dialog = new Stage();

                    dialog.initOwner(primaryStage);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("Set movement vector"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);

                    MyButton next = new MyButton("X");
                    next.newCircle = temp;
                    TextField scanVector = new TextField("10");
                    next.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if(next.getText().equals("X")) {
                                next.setText("Y");
                                next.newCircle.vecX = java.lang.Integer.parseInt(scanVector.getText());
                                scanVector.setText("10");
                            }
                            else if(next.getText().equals("Y")) {
                                next.setText("MASS");

                                next.newCircle.vecY = Integer.parseInt(scanVector.getText());
                            }
                            else if(next.getText().equals("MASS")){
                                next.newCircle.mass = Integer.parseInt(scanVector.getText());
                                dialog.hide();
                            }
                        }
                    });
                    myManager.figures.add(temp);
                    dialogVbox.getChildren().add(scanVector);
                    dialogVbox.getChildren().add(next);
                    dialog.setScene(dialogScene);
                    dialog.show();
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

        /*scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("W: " + scene.getWidth() + ", H: " + scene.getHeight());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("W: " + scene.getWidth() + ", H: " + scene.getHeight());
            }
        });*/
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}