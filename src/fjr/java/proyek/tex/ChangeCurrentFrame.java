package fjr.java.proyek.tex;

//import java.awt.TextField;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class ChangeCurrentFrame extends Application {

    double width = 600;
    double height = 400;

    FileHandler fileHandler;

    Stage mainStage;

    FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(
            "Tex Files", "*.tex");

    File initialDirectory = new File("E:/");

    StackPane stack;

    TableView<FrameLabel> tableView;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Group root = new Group();
        stage.setScene(new Scene(root,
                width,
                height));

        stack = new StackPane();
        stack.getChildren().add(getTable());

        fileHandler = new FileHandler();

        VBox boxButton = new VBox();
        boxButton.setSpacing(10);

        Button buttonOpen = new Button();
        buttonOpen.setText("OPEN");
        buttonOpen.setPrefWidth(80);
        buttonOpen.setOnAction(event -> {
            try {
                openFile();
            } catch (IOException ex) {
                Logger.getLogger(ChangeCurrentFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Button buttonSet = new Button();
        buttonSet.setText("SET DATA");
        buttonSet.setPrefWidth(80);
        buttonSet.setOnAction(event -> {
            if (fileAvailable) {
                convertMapToTableClass();
                tableView.setItems(listFrameLabels);
            }
        });

        Button buttonClear = new Button();
        buttonClear.setPrefWidth(80);
        buttonClear.setText("CLEAR");
        buttonClear.setOnAction(event -> {
            if (listFrameLabels != null) {
                listFrameLabels.clear();
            }
        });

        Button buttonSave = new Button();
        buttonSave.setPrefWidth(80);
        buttonSave.setText("SAVE");
        buttonSave.setOnAction(event -> {
            try {
                save();
            } catch (IOException ex) {
                Logger.getLogger(ChangeCurrentFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        Button buttonReload = new Button(); 
        buttonReload.setText("RELOAD");
        buttonReload.setPrefWidth(80);
        buttonReload.setOnAction(event-> {
            try {
                fileHandler.reload();
            } catch (IOException ex) {
                Logger.getLogger(ChangeCurrentFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        boxButton.getChildren().addAll(buttonOpen, buttonSet, buttonClear, buttonSave);

        HBox box = new HBox();
        box.setSpacing(10);
        box.getChildren().addAll(stack, boxButton);

        root.getChildren().add(box);
        stage.show();
    }

    boolean fileAvailable = false;

    public void save() throws IOException {
        fileHandler.writeFile(listFrameLabels);
    }

    public void openFile() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(filter1);
        chooser.setInitialDirectory(initialDirectory);
        File ff = chooser.showOpenDialog(mainStage);

        if (ff != null) {
            fileHandler.read(ff);
            fileAvailable = true;

        }
    }

    public TableView<FrameLabel> getTable() {
        TableView<FrameLabel> tabel = new TableView<>();

        tabel.setPrefWidth(.6 * width);

        TableColumn<FrameLabel, Boolean> stateColumn = new TableColumn<>();
        stateColumn.setText("Is Compile");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("compile"));

        stateColumn.setCellFactory((
                TableColumn<FrameLabel, Boolean> t) -> {
                    return new TableCell<FrameLabel, Boolean>() {
                        private CheckBox checkBox;
                        private ObservableValue<Boolean> ov;

                        {
                            checkBox = new CheckBox();
                            checkBox.setAlignment(
                                    Pos.CENTER);
                            setAlignment(Pos.CENTER);
                            setGraphic(checkBox);
                        }

                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                setGraphic(checkBox);
                                if (ov instanceof BooleanProperty) { // biar g ilang pas discroll 
                                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                                }
                                ov = getTableColumn().getCellObservableValue(getIndex());
                                if (ov instanceof BooleanProperty) {
                                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                                }
                            }
                        }
                    ;
                };
        });

        TableColumn<FrameLabel, String> nameColumn = new TableColumn<>();
        nameColumn.setText("Label");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("label"));

        nameColumn.setCellFactory((TableColumn<FrameLabel, String> t) -> {
            return new TableCell<FrameLabel, String>() {
                @Override
                public void updateItem(String item,
                        boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(getItem());
                        setGraphic(null);
                    }
                }
            };
        });

        nameColumn.setOnEditCommit(t -> {
            ((FrameLabel) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).changeLabel(t.getNewValue());
        });

        TableColumn<FrameLabel, Integer> positionColumn = new TableColumn<>();
        positionColumn.setText("Posisi");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        positionColumn.setCellFactory((TableColumn<FrameLabel, Integer> label) -> {
            return new TableCell<FrameLabel, Integer>() {
                private Label label;

                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(getItem().toString());
                        setGraphic(null);
                    }
                }
            };
        });

        tabel.setEditable(true);
        tabel.getColumns().addAll(stateColumn, nameColumn, positionColumn);
        tabel.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        nameColumn.prefWidthProperty().bind(tabel.widthProperty().divide(tabel.getColumns().size()));

        stateColumn.prefWidthProperty().bind(tabel.widthProperty().divide(tabel.getColumns().size()));

        positionColumn.prefWidthProperty().bind(tabel.widthProperty().divide(tabel.getColumns().size()));
        
        tableView = tabel;
        
        return tabel;
    }
    
    public void refresh() {
        if (fileAvailable) {
            tableView.setItems(listFrameLabels);
        }
    }

    ObservableList<FrameLabel> listFrameLabels;

    public void convertMapToTableClass() {
        listFrameLabels = FXCollections.observableArrayList(fileHandler.getListFrameLabel());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
