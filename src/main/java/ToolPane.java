import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;

/**
 * Created by mateusz on 05.06.15.
 */
public class ToolPane extends AnchorPane{
    //private AnchorPane toolPane;
    public final Button startButton;
    public final Button restartButton;
    public final ScrollBar timeScroll;
    ToolPane(int width, int height){
//        toolPane = new AnchorPane();

        this.setPrefSize(width, height);

        startButton = new Button("Start");
        startButton.setPrefSize(100, 40);
        this.getChildren().addAll(startButton);

        restartButton = new Button("Restart");
        restartButton.setPrefSize(100, 40);
        restartButton.setTranslateX(100);
        this.getChildren().addAll(restartButton);

        Label timeScrollLabel = new Label("Time: ");
        timeScrollLabel.setPrefSize(100, 20);
        timeScrollLabel.setTranslateX(200);
        this.getChildren().addAll(timeScrollLabel);

        timeScroll = new ScrollBar();
        timeScroll.setMax(100);
        timeScroll.setMin(-100);
        timeScroll.setValue(0);
        timeScroll.setPrefSize(200, 20);
        timeScroll.setTranslateY(20);
        timeScroll.setTranslateX(200);
        this.getChildren().addAll(timeScroll);
    }

}
