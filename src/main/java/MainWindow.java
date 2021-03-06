import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindow extends Application {
	private final static int circleRadius = 20;
	private static int windowHeight = 1024;
	private static int windowsWidth = 1280;
	private TablePane<MyCircle> ballsTable;
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

		ballsTable = new TablePane<>(200, (int) (primaryStage.getHeight() - toolPane.getHeight()));
		root.getChildren().addAll(ballsTable);
		myManager.setTablePane(ballsTable);

		ObservableList<MyCircle> tableData = FXCollections.observableArrayList();
		ballsTable.setItems(tableData);

		Canvas canvas = new Canvas(windowsWidth, windowHeight);
		myManager.movingObjects.getChildren().add(canvas);


		root.getChildren().add(myManager.movingObjects);
		myManager.movingObjects.toBack();

		primaryStage.setScene(scene);
		primaryStage.show();

		myManager.myRun();

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
					boolean restore = myManager.getIsPlaying();
					myManager.pause();
					for (MyCircle i : myManager.figures) {
						i.translate((x - mouseStartX.get()), (y - mouseStartY.get()));
					}
					mouseStartX.set(x);
					mouseStartY.set(y);
					if (restore) {
						myManager.play();
					}
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
				if (event.getButton().toString().equals("PRIMARY") && !newCircleWindow.get()) {
					System.out.println("Click");
                    /*
                    * Check if clicked on existing wheel
                    * */
					for (MyCircle i : myManager.figures) {
						if (!myManager.addingMovable && !i.isMovable()) {
							continue;
						}
						if (i.inCircle(event.getSceneX(), event.getSceneY())) {
							System.out.println("Clicked on circle");
							boolean restore = myManager.getIsPlaying();
							myManager.pause();
							ModifyCircleDialog modifyCircleDialog = new ModifyCircleDialog(primaryStage, i);
							modifyCircleDialog.saveChanges.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									i.setVecX(modifyCircleDialog.getXValue());
									i.setVecY(modifyCircleDialog.getYValue());
									i.setMass(modifyCircleDialog.getMassValue());
									modifyCircleDialog.hide();
									if (restore) {
										myManager.play();
									}
								}
							});
							modifyCircleDialog.show();
							return;
						}
					}
					boolean collision = false;
					for (MyCircle i : myManager.figures) {
						if (!myManager.addingMovable && !i.isMovable()) {
							continue;
						}
						if (i.inCollisionProximity(event.getSceneX(), event.getSceneY())) {
							System.out.println("Too close to create new circle");
							collision = true;
						}
					}

					if (!collision) {
                        /*
                        * Init new Dialong window with gridPane and add all labels, buttons and textFields
                        * */


                        /*
                        * Create new Circle with proper x, y position
                        * */
						MyCircle circle = new MyCircle(event.getSceneX(), event.getSceneY(), 0, circleRadius);

                        /*
                        * Set action for clicking ADD button
                        * */


						boolean restore = myManager.getIsPlaying();
						myManager.pause();
						if (myManager.addingMovable) {
							newCircleWindow.set(true);
							NewCircleDialog newCircleDialog = new NewCircleDialog(primaryStage);
							newCircleDialog.addCircleButton.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									circle.setVecX(newCircleDialog.getXValue());
									circle.setVecY(newCircleDialog.getYValue());
									circle.setMass(newCircleDialog.getMassValue());
									myManager.movingObjects.getChildren().add(circle);
									myManager.figures.add(circle);
									myManager.operations.add(circle);
									tableData.add(circle);
									System.out.println("Circle with ID: " + circle.getCircleId() + " added.");
									//System.out.println(tableData.size());
									newCircleDialog.hide();
									newCircleWindow.set(false);
									if (restore) {
										myManager.play();
									}
								}
							});
							newCircleDialog.show();
						} else {
							circle.setVecX(0.0);
							circle.setVecY(0.0);
							circle.setMass(100);
							circle.setUnmovable();
							myManager.movingObjects.getChildren().add(circle);
							myManager.figures.add(circle);
							myManager.operations.add(circle);
							//tableData.add(circle);
							System.out.println("Circle with ID: " + circle.getCircleId() + " added.");
							//System.out.println(tableData.size());

							if (restore) {
								myManager.play();
							}
						}
					}
				}
			}
		});
		toolPane.startButton.setOnAction(new EventHandler<ActionEvent>() {

			boolean paused = true;

			@Override
			public void handle(ActionEvent event) {
				if (paused) {
					paused = false;
					myManager.play();
					toolPane.startButton.setText("Stop");
				} else {
					myManager.pause();
					paused = true;
					toolPane.startButton.setText("Continue");
				}
			}
		});
		toolPane.newSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
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
				synchronized (myManager.scrollValue) {
					myManager.scrollValue = toolPane.timeScroll.getValue();
				}
			}
		});
		toolPane.newSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				myManager.pause();
				toolPane.startButton.setText("Start");
				myManager.movingObjects.getChildren().removeAll(myManager.figures);
				myManager.figures.clear();
				myManager.myRun();
				MyCircle.clearId();
				tableData.clear();
			}
		});
		/*
		Button to add both static and nonstatic cirles

		 */
		toolPane.movablilityOfCircleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				myManager.addingMovable = !myManager.addingMovable;
				if(toolPane.movablilityOfCircleButton.getText().equals("Unmovable Circles")) {
					toolPane.movablilityOfCircleButton.setText("Movable Circles");
				}
				else {
					toolPane.movablilityOfCircleButton.setText("Unmovable Circles");
				}
			}
		});

		toolPane.undoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (myManager.operations.isEmpty()) {
					return;
				}
				boolean restore = myManager.getIsPlaying();
				myManager.pause();
				myManager.figures.remove(myManager.operations.peek());
				myManager.movingObjects.getChildren().remove(myManager.operations.peek());
				if(myManager.operations.peek().isMovable()) tableData.remove(tableData.size() - 1);
				myManager.operations.pop();
				if (restore) {
					myManager.play();
				}
			}
		});


		toolPane.saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				boolean restore = myManager.getIsPlaying();
				myManager.pause();
				ArrayList<SerializableCircle> toSave = new ArrayList<>();
				for (MyCircle i : myManager.figures) {
					toSave.add(new SerializableCircle(i));
				}
				try {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Select file to save");
					File selectedFile = fileChooser.showSaveDialog(primaryStage);
					if(selectedFile == null){
						System.out.println("Select file to save");
						return;
					}
					OutputStream file = new FileOutputStream(selectedFile);
					OutputStream buffer = new BufferedOutputStream(file);
					ObjectOutput output = new ObjectOutputStream(buffer);
					output.writeObject(toSave);
					output.close();
					buffer.close();
					file.close();
				} catch (IOException e) {
					System.out.println(e);
				}
				if (restore) {
					myManager.play();
				}
				System.out.println("Saved to file");
			}
		});
		toolPane.loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				myManager.pause();
				myManager.movingObjects.getChildren().removeAll(myManager.figures);
				myManager.figures.clear();
				myManager.operations.clear();
				tableData.clear();
				ArrayList<SerializableCircle> loaded = null;
				try {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Select file to load");
					File selectedFile = fileChooser.showOpenDialog(primaryStage);
					if(selectedFile == null){
						System.out.println("Select file to load");
						return;
					}
					InputStream file = new FileInputStream(selectedFile);
					InputStream buffer = new BufferedInputStream(file);
					ObjectInput input = new ObjectInputStream(buffer);
					loaded = (ArrayList<SerializableCircle>) input.readObject();
					for (SerializableCircle i : loaded) {
						MyCircle temp = i.getMyCircle();
						temp.setVisible(true);
						temp.setRadius(20);
						temp.setFill(Color.BLACK);

						//System.out.print(i.id + " " );

						if(temp.isMovable()) tableData.add(temp);
						myManager.movingObjects.getChildren().add(temp);
						myManager.figures.add(temp);
						myManager.operations.add(temp);
					}
					System.out.println();
					input.close();
					buffer.close();
					file.close();
					myManager.myRun();

				} catch (Exception e) {
					System.out.println("Exception while loading from file");
				}
				System.out.println("Loaded from file");
			}
		});
	}
}