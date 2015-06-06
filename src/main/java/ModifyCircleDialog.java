import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by mateusz on 05.06.15.
 */
public class ModifyCircleDialog extends Stage {
	public Button saveChanges;
	private TextField xTextField, yTextField, massTextField;

	ModifyCircleDialog(Stage owner, MyCircle circle) {
		this.initOwner(owner);
		this.setTitle("Modify");
		GridPane gridPane = new GridPane();
		Scene dialogScene = new Scene(gridPane, 250, 120);

		Label xLabel = new Label(" X: ");
		xTextField = new TextField(circle.getVecXProperty().toString());
		gridPane.add(xLabel, 0, 0);
		gridPane.add(xTextField, 1, 0);

		Label yLabel = new Label(" Y: ");
		yTextField = new TextField(circle.getVecYProperty().toString());
		gridPane.add(yLabel, 0, 1);
		gridPane.add(yTextField, 1, 1);

		Label massLabel = new Label(" Mass: ");
		massTextField = new TextField(circle.getMassProperty().toString());
		gridPane.add(massLabel, 0, 2);
		gridPane.add(massTextField, 1, 2);

		saveChanges = new Button("Save");
		gridPane.add(saveChanges, 1, 3);
		this.setScene(dialogScene);
	}

	public double getXValue() {
		return Double.parseDouble(xTextField.getText());
	}

	public double getYValue() {
		return Double.parseDouble(yTextField.getText());
	}

	public double getMassValue() {
		return Double.parseDouble(massTextField.getText());
	}
}
