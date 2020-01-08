package magazineUI;

import categoriesManagement.Category;
import categoriesManagement.CategoryDAOIMPL;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
        Long id = checkLong(textFieldList[0]);
        String name = textFieldList[1].getText();
        if (id != null && name.length() > 0) {
            Category c = new Category(id, name);
            if (categoryDAOIMPL.create(c)) {
                observableList.add(c);
                for (int i = 0; i < textFieldList.length - 1; i++)
                    textFieldList[i].clear();
            }
        } else {
            System.out.println("Non valid input");
        }

    }

    @Override
    protected void updateButtonClick() {
        Category c = (Category) tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        Long id = checkLong(textFieldList[0]);
        String name = textFieldList[1].getText();
        if (id != null && name != null && c != null) {
            if (categoryDAOIMPL.update(c, new Category(id, name))) {
                observableList.set(index, c);
                for (int i = 0; i < textFieldList.length - 1; i++)
                    textFieldList[i].clear();
                tableView.getSelectionModel().clearSelection();
            }

        } else {
            System.out.println("Non valid input");
        }
    }

    @Override
    protected void deleteButtonClick() {
        Category c = (Category) tableView.getSelectionModel().getSelectedItem();
        if (categoryDAOIMPL.delete(c)) {
            observableList.remove(c);
            tableView.getSelectionModel().clearSelection();
        }
    }

    @Override
    void clearButtonClick() {

        for (int i = 0; i < textFieldList.length; i++)
            textFieldList[i].clear();
        try {
            tableView.getSelectionModel().clearSelection();
        } catch (NullPointerException e) {
        }
//        additionalClears();
    }
}