package magazineUI;

import categoriesManagement.Category;
import categoriesManagement.CategoryDAOIMPL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import productsManagement.Product;
import productsManagement.ProductDAOIMPL;

public class ProductUI extends BaseUI {

    ProductDAOIMPL productDataAccess;
    CategoryDAOIMPL categoryDAOIMPL;
    ObservableList<Product> observableList;
    ComboBox<Category> comboBox;

    public ProductUI(String label, Stage parentStage) {
        super(label);
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
    }

    public ProductUI(Stage parentStage) {
        super("Gestion des produits");
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
    }

    public static void createDataView(ObservableList<Product> productsObservableList, TableView<Product> productsTableView) {
        TableColumn<Product, Integer> column1 = new TableColumn<>("Code");
        column1.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Product, String> column2 = new TableColumn<>("Designation");
        column2.setCellValueFactory(new PropertyValueFactory<>("designation"));
        TableColumn<Product, String> column3 = new TableColumn<>("Prix");
        column3.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> column4 = new TableColumn<>("Catégorie");
        column4.setCellValueFactory(new PropertyValueFactory<>("category"));
        column1.setPrefWidth(Width / 12.0);
        column2.setPrefWidth(Width / 4.0);
        column3.setPrefWidth(Width / 12.0);
        column4.setPrefWidth(Width / 12.0);
        try {
            productsTableView.setItems(productsObservableList);
        } catch (NullPointerException e) {
            System.out.println("Products list empty");
        }
        productsTableView.getColumns().addAll(column1, column2, column3, column4);
    }


    @Override
    protected void createInsertContainer() {
        categoryDAOIMPL = new CategoryDAOIMPL();
        labelList = new Label[5];
        textFieldList = new TextField[4];
        insertContainer = new VBox(30);
        insertContainer.setMinWidth((Width / 3) - 20);
        labelList[0] = new Label("Code :");
        textFieldList[0] = new TextField();
        readOnlyTextField(textFieldList[0]);
        labelList[1] = new Label("Designation :");
        textFieldList[1] = new TextField();
        labelList[2] = new Label("Price :");
        textFieldList[2] = new TextField();
        labelList[3] = new Label("Catégorie :");
        comboBox = new ComboBox();
        ObservableList list = FXCollections.observableArrayList(categoryDAOIMPL.getAll());
        if (list != null) {
            comboBox.setItems(list);
        }
        insertContainer.getChildren().addAll(labelList[0], textFieldList[0], labelList[1], textFieldList[1],
                labelList[2], textFieldList[2], labelList[3], comboBox);

    }

    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(30);
        tableContainer.setMinWidth(Width / 2);
        labelList[4] = new Label("Search for :");
        textFieldList[3] = new TextField();

        createDataView();

        FilteredList<Product> filteredList = new FilteredList<>(observableList);
        tableView.setItems(filteredList);
        textFieldList[3].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true : s -> s.getDesignation().toLowerCase().contains(newValue.toLowerCase()))
        );

        tableContainer.getChildren().addAll(labelList[4], textFieldList[3], tableView);

    }

    @Override
    protected void createDataView() {
        productDataAccess = new ProductDAOIMPL();
        observableList = productDataAccess.getAll();
        tableView = new TableView<Product>();
        createDataView(observableList, tableView);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFieldList[0].setText(String.valueOf(((Product) newSelection).getCode()));
                textFieldList[1].setText(((Product) newSelection).getDesignation());
                textFieldList[2].setText(String.valueOf(((Product) newSelection).getPrice()));
                comboBox.getSelectionModel().select(((Product) newSelection).getCategory());
            }

        });
    }

    @Override
    protected void addButtonClick() {
        Double price = checkDouble(textFieldList[2]);
        Category category = getCategoryComboBoxInput(comboBox);
        if (price != null && category != null) {
            Product p = new Product(0, textFieldList[1].getText(), price, category);
            if (productDataAccess.create(p)) {
                observableList.add(p);
                clearButtonClick();
            }
        } else {
            System.out.println("Non valid input");
        }
    }


    @Override
    protected void updateButtonClick() {
        Product p = (Product) tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        String designation = textFieldList[1].getText();
        Double price = checkDouble(textFieldList[2]);
        Category category = comboBox.getValue();
        if (price != null && category != null && p != null) {
            if (productDataAccess.update(p, new Product(p.getCode(), designation, price, category))) {
                observableList.set(index, p);
                clearButtonClick();
            }

        } else {
            System.out.println("Non valid input");
        }
    }

    @Override
    protected void deleteButtonClick() {
        Product p = (Product) tableView.getSelectionModel().getSelectedItem();
        if (productDataAccess.delete(p)) {
            observableList.remove(p);
            clearButtonClick();
        }
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



