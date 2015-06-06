import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by mateusz on 23.05.15.
 */
public class NewCircleDialog extends Stage {
	public Button addCircleButton;
	private TextField xTextField, yTextField, massTextField;

	NewCircleDialog(Stage owner) {
		this.initOwner(owner);
		this.setTitle("Adding new circle");
		GridPane gridPane = new GridPane();
		Scene dialogScene = new Scene(gridPane, 250, 120);

		Label xLabel = new Label(" X: ");
		xTextField = new TextField("10");
		gridPane.add(xLabel, 0, 0);
		gridPane.add(xTextField, 1, 0);

		Label yLabel = new Label(" Y: ");
		yTextField = new TextField("10");
		gridPane.add(yLabel, 0, 1);
		gridPane.add(yTextField, 1, 1);

		Label massLabel = new Label(" Mass: ");
		massTextField = new TextField("10");
		gridPane.add(massLabel, 0, 2);
		gridPane.add(massTextField, 1, 2);

		addCircleButton = new Button("ADD");
		gridPane.add(addCircleButton, 1, 3);
		this.setScene(dialogScene);
	}

	public int getXValue() {
		return Integer.parseInt(xTextField.getText());
	}

	public int getYValue() {
		return Integer.parseInt(yTextField.getText());
	}

	public int getMassValue() {
		return Integer.parseInt(massTextField.getText());
	}
}
