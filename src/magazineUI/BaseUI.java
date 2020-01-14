package magazineUI;

import categoriesManagement.Category;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

public abstract class BaseUI {
    protected static int Heigth;
    protected static int Width;

    protected VBox container;
    protected HBox header;
    protected HBox body;
    protected VBox buttonsContainer;
    protected VBox insertContainer;
    protected VBox tableContainer;
    protected Scene mainScene;
    protected Stage mainStage;
    TableView tableView;
    Button[] buttonsList;
    TextField[] textFieldList;
    Label[] labelList;

    public BaseUI(int w, int h, String label) {
        Width = w;
        Heigth = h;
        createHeader(label);
        createBody();
        addButtonsFunctionality();
        container = new VBox(6);
        container.getChildren().addAll(header, body);
        mainScene = new Scene(container, Width, Heigth);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    public BaseUI(String label) {
        this(1000, 550, label);
    }

    private void createHeader(String label) {
        header = new HBox(6);
        Label title = new Label(label);
        title.setTextFill(Color.RED);
        title.setFont(new Font(28));
        header.setAlignment(Pos.CENTER);
        header.getChildren().add(title);
    }

    private void createBody() {
        body = new HBox(5);
        body.setMinWidth(Width);
        createButtonsContainer();
        createInsertContainer();
        createTableContainer();
        body.getChildren().addAll(buttonsContainer, insertContainer, tableContainer);
    }

    private void createButtonsContainer() {
        buttonsList = new Button[4];
        buttonsContainer = new VBox(30);
        buttonsContainer.setMinWidth(Width / 6.0);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsList[0] = new Button("Create");
        buttonsList[1] = new Button("Update");
        buttonsList[2] = new Button("Delete");
        buttonsList[3] = new Button("Clear");
        for (Button button : buttonsList) {
            button.setPrefWidth((Width / 6.0) - 20);
        }
        buttonsContainer.getChildren().addAll(buttonsList[0], buttonsList[1], buttonsList[2], buttonsList[3]);
    }

    protected abstract void createInsertContainer();

    protected abstract void createTableContainer();

    protected abstract void createDataView();

    protected void addButtonsFunctionality() {
        buttonsList[0].setOnMouseClicked(event -> addButtonClick());
        buttonsList[1].setOnMouseClicked(event -> updateButtonClick());
        buttonsList[2].setOnMouseClicked(event -> deleteButtonClick());
        buttonsList[3].setOnMouseClicked(event -> clearButtonClick());
    }

    protected abstract void addButtonClick();

    protected abstract void updateButtonClick();

    protected abstract void deleteButtonClick();


    abstract void clearButtonClick();

//    protected abstract void additionalClears();

    protected Category getCategoryComboBoxInput(ComboBox<Category> comboBox) {
        if (comboBox.getValue() == null) {
            comboBox.setStyle("-fx-border-color: red ;-fx-focus-color: red ;");
        } else {
            comboBox.setStyle("");
        }
        return comboBox.getValue();

    }

    protected String getStringComboBoxInput(ComboBox<String> comboBox) {
        if (comboBox.getValue() == null) {
            comboBox.setStyle("-fx-border-color: red ;-fx-focus-color: red ;");
            return null;
        } else {
            comboBox.setStyle("");
        }
        return comboBox.getValue();

    }

    protected Long checkLong(TextField textField) {
        try {
            long n = Long.parseLong(textField.getText());
            textField.setStyle("");
            return n;
        } catch (NumberFormatException e) {
            textField.setStyle("-fx-text-box-border: red ;-fx-focus-color: red ;");
            System.out.println(textField.getText() + " is not a Long");
        }
        return null;
    }

    protected void setAsError(TextField textField) {
        textField.setStyle("-fx-text-box-border: red ;-fx-focus-color: red ;");
    }

    protected Double checkDouble(TextField textField) {
        try {
            double n = Double.parseDouble(textField.getText());
            textField.setStyle("");
            return n;
        } catch (NumberFormatException e) {
            setAsError(textField);
            System.out.println(textField.getText() + " is not a Double");
        }
        return null;
    }

    protected void readOnlyTextField(TextField textField) {
        textField.setMaxWidth(70);
        textField.setDisable(true);
        textField.setStyle("-fx-opacity: 0.8;");
    }

    protected LocalDate getDateDatepicker(DatePicker datePicker) {
        LocalDate datePickerValue = datePicker.getValue();
        if (datePickerValue == null) {
            datePicker.setStyle("-fx-border-color: red ;-fx-focus-color: red ;");
            return null;
        } else {
            datePicker.setStyle("");
        }
        return datePickerValue;
    }

    protected ButtonType showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType, s, ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        return alert.getResult();
    }

}

