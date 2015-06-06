import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by mateusz on 06.06.15.
 */
public class TablePane<T> extends TableView<T> {
    TablePane(int width, int height){
        this.setEditable(true);
        this.setTranslateY(40);
        this.setPrefSize(width, height);

        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory<MyCircle, String>("idProperty"));
        id.setPrefWidth(100);

        TableColumn vecX = new TableColumn("X");
        vecX.setCellValueFactory(new PropertyValueFactory<MyCircle, Integer>("vecXProperty"));
        vecX.setPrefWidth(50);

        TableColumn vecY = new TableColumn("Y");
        vecY.setCellValueFactory(new PropertyValueFactory<MyCircle, Integer>("vecYProperty"));
        vecY.setPrefWidth(50);

        this.getColumns().addAll(id, vecX, vecY);

    }
    public void refresh(){
        /*
        * Hack from stackoverflow, should refresh table in gui
        * */
        this.getColumns().get(0).setVisible(false);
        this.getColumns().get(0).setVisible(true);
    }
}
