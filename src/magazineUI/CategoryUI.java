package magazineUI;

import categoriesManagement.Category;
import categoriesManagement.CategoryDAOIMPL;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryUI extends BaseUI {
    CategoryDAOIMPL categoryDAOIMPL;
    ObservableList<Category> observableList;

    public CategoryUI(String label, Stage parentStage) {
        super(label);
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
    }

    @Override
    protected void createInsertContainer() {
        labelList = new Label[3];
        textFieldList = new TextField[3];
        insertContainer = new VBox(30);
        insertContainer.setMinWidth((Width / 3) - 20);
        labelList[0] = new Label("Code :");
        textFieldList[0] = new TextField();
        readOnlyTextField(textFieldList[0]);
        labelList[1] = new Label("Nom :");
        textFieldList[1] = new TextField();
        insertContainer.getChildren().addAll(labelList[0], textFieldList[0], labelList[1], textFieldList[1]);
    }

    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(30);
        tableContainer.setMinWidth(Width / 2);
        labelList[2] = new Label("Search for :");
        textFieldList[2] = new TextField();
        createDataView();
        FilteredList<Category> filteredList = new FilteredList<>(observableList);
        tableView.setItems(filteredList);
        textFieldList[2].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true : s -> s.getName().toLowerCase().contains(newValue.toLowerCase())
                )
        );

        tableContainer.getChildren().

                addAll(labelList[2], textFieldList[2], tableView);

    }

    @Override
    protected void createDataView() {
        categoryDAOIMPL = new CategoryDAOIMPL();
        observableList = categoryDAOIMPL.getAll();
        tableView = new TableView<Category>();
        TableColumn<Category, Integer> column1 = new TableColumn<>("Code");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Category, String> column2 = new TableColumn<>("Nom");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column1.setPrefWidth(Width / 6);
        column2.setPrefWidth(Width / 3);
        try {
            tableView.setItems(observableList);
        } catch (NullPointerException e) {
            System.out.println("Category list s empty");
        }
        tableView.getColumns().addAll(column1, column2);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFieldList[0].setText(String.valueOf(((Category) newSelection).getId()));
                textFieldList[1].setText(((Category) newSelection).getName());
            }

        });

    }

    @Override
    protected void addButtonClick() {
        String name = textFieldList[1].getText();
        if (name.length() > 0) {
            Category c = new Category(0, name);
            if (categoryDAOIMPL.create(c)) {
                System.out.println(c);
                observableList.add(c);
                clearButtonClick();
            }
        } else showAlert(Alert.AlertType.ERROR, "Non valid input");

    }

    @Override
    protected void updateButtonClick() {
        Category c = (Category) tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        String name = textFieldList[1].getText();
        if (name != null && c != null) {
            if (showAlert(Alert.AlertType.CONFIRMATION, "Do you want to update ?") == ButtonType.OK)
                if (categoryDAOIMPL.update(c, new Category(c.getId(), name))) {
                    observableList.set(index, c);
                    clearButtonClick();
                }

        } else showAlert(Alert.AlertType.ERROR, "Non valid input");
    }

    @Override
    protected void deleteButtonClick() {
        Category c = (Category) tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            if (showAlert(Alert.AlertType.WARNING, "Do you want to delete ?") == ButtonType.OK)
                if (categoryDAOIMPL.delete(c)) {
                    observableList.remove(c);
                    tableView.getSelectionModel().clearSelection();
                }
        } else showAlert(Alert.AlertType.ERROR, "Nothing selected");

    }

    @Override
    void clearButtonClick() {

        for (TextField textField : textFieldList) textField.clear();
        try {
            tableView.getSelectionModel().clearSelection();
        } catch (NullPointerException ignored) {
        }
    }
}