import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class Main extends Application {
	private final static int circleRadius = 20;
	private static int windowHeight = 600;
	private static int windowsWidth = 800;
	@Override
	public void start(Stage stage) throws Exception{
		final Group root = new Group();
		Scene scene = new Scene(root, windowsWidth, windowHeight);
		Canvas canvas = new Canvas(windowsWidth, windowHeight);
		root.getChildren().add(canvas);
		//Circle circle = new Circle(200, 150, 50, Color.BLACK);
		//root.getChildren().add(circle);

		root.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Circle temp = new Circle(event.getX(), event.getY(), circleRadius, Color.BLACK);
				System.out.println("dziala");
				root.getChildren().add(temp);
			}
		});

		stage.setTitle("Engine testing");
		stage.setScene(scene);
		stage.show();

	}


	public static void main(String[] args) {
		launch(args);
	}
}
