package sample;

//Imports are listed in full to show what's being used

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
public class Main extends Application {

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
        GridPane grid = new GridPane();
        Button start = new Button("Start");
        Button restart = new Button("Restart");
        Button addFigure = new Button("Add Figure");
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!myManager.isGoing()) {
                    myManager.run();
                    start.setText("Stop");
                }
                else {
                    myManager.stop();
                    start.setText("Continue");
                }
            }
        });
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myManager.restart();
                start.setText("Start");
            }
        });

        ScrollBar scroll = new ScrollBar();
        scroll.setMax(100);
        scroll.setMin(0);
        scroll.setValue(50);
        myManager.setScroll(scroll);
        grid.add(start, 0, 0);
        grid.add(restart, 0, 1);
        grid.add(addFigure, 0, 2);
        grid.add(new Label("Speed"), 2, 0);
        grid.add(scroll, 2, 1);
        Scene scene = new Scene(grid, 200, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}