package magazineUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import productsManagement.Product;
import productsManagement.ProductDAOIMPL;
import salesManagment.Sale;
import salesManagment.SaleItem;
import salesManagment.SaleItemDAOIMPL;

import java.util.ArrayList;

public class SaleItemUI extends BaseUI {

    SaleItemDAOIMPL saleItemDAOIMPL;
    ProductDAOIMPL productDAOIMPL;
    ObservableList<SaleItem> saleItemObservableList;
    TableView<SaleItem> saleItemTableView;
    Sale sale;
    private VBox productsContainer;
    private ObservableList<Product> productsObservableList;
    private TableView<Product> productsTableView;

    public SaleItemUI(String label, Stage parentStage, Sale sale) {
        super(label);
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
        this.sale = sale;
    }

    public SaleItemUI(Stage parentStage, Sale sale) {
        super("Gestion des lignes de commande");
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
        this.sale = sale;
        initAdditionalComponents();
    }


    public SaleItemUI(Sale sale) {
        super("Gestion des lignes de commande");
        this.sale = sale;
        initAdditionalComponents();
    }

    public static void createDataView(ObservableList<SaleItem> observableList, TableView<SaleItem> tableView) {
        TableColumn<SaleItem, Long> column1 = new TableColumn<>("N°");
        column1.setCellValueFactory(new PropertyValueFactory<>("num"));
        TableColumn<SaleItem, String> column2 = new TableColumn<>("Produit");
        column2.setCellValueFactory(new PropertyValueFactory<>("productName"));
        TableColumn<SaleItem, Double> column3 = new TableColumn<>("Quantity");
        column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<SaleItem, String> column4 = new TableColumn<>("Sub Total");
        column4.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        column1.setPrefWidth(Width / 12.0 - 10);
        column2.setPrefWidth(Width / 12.0);
        column3.setPrefWidth(Width / 12.0);
        column4.setPrefWidth(Width / 12.0);
        tableView.getColumns().addAll(column1, column2, column3, column4);
        try {
            tableView.setItems(observableList);
        } catch (NullPointerException e) {
            System.out.println("Products list empty");
        }
    }

    private void initAdditionalComponents() {
        if (saleItemDAOIMPL == null)
            saleItemDAOIMPL = new SaleItemDAOIMPL(sale);
        saleItemObservableList = saleItemDAOIMPL.getAllObsList();
        if (saleItemObservableList == null) {
            saleItemObservableList = FXCollections.observableArrayList(new ArrayList<>());
        }
        FilteredList<SaleItem> filteredList = new FilteredList<>(saleItemObservableList);
        saleItemTableView.setItems(filteredList);
        textFieldList[2].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(newValue == null || newValue.length() == 0 ? s -> true : s -> s.getProduct()
                        .getDesignation().toLowerCase().contains(newValue.toLowerCase()))
        );
        textFieldList[3].setText(String.valueOf(sale.getId()));
        textFieldList[4].setText(String.valueOf(sale.getTotal()));
    }

    @Override
    protected void createInsertContainer() {
        saleItemDAOIMPL = sale == null ? null : new SaleItemDAOIMPL(sale);
        labelList = new Label[8];
        textFieldList = new TextField[7];

        labelList[3] = new Label("Vent N° : ");
        textFieldList[3] = new TextField();
        readOnlyTextField(textFieldList[3]);
        HBox holder1 = new HBox(10, labelList[3], textFieldList[3]);

        labelList[4] = new Label("Total : ");
        textFieldList[4] = new TextField();
        readOnlyTextField(textFieldList[4]);
        HBox holder2 = new HBox(10, labelList[4], textFieldList[4]);

        insertContainer = new VBox(10);
        insertContainer.setMinWidth(Width / 2.0);
        labelList[0] = new Label("ID : ");
        textFieldList[0] = new TextField();
        readOnlyTextField(textFieldList[0]);
        labelList[5] = new Label("Products : ");
        createProductsTableView();
        labelList[1] = new Label("Quantity : ");
        textFieldList[1] = new TextField();
        labelList[2] = new Label("Sub total : ");
        textFieldList[2] = new TextField();
        readOnlyTextField(textFieldList[2]);
        insertContainer.getChildren().addAll(holder1, holder2, labelList[0], textFieldList[0],
                labelList[5], productsContainer, labelList[1], textFieldList[1], labelList[2], textFieldList[2]);

    }

    private void createProductsTableView() {
        productsContainer = new VBox(5);
        productDAOIMPL = new ProductDAOIMPL();
        productsObservableList = productDAOIMPL.getAll();
        productsTableView = new TableView<>();
        productsTableView.setMaxWidth(Width >> 1);
        HBox holder = new HBox(10);
        holder.setMinWidth(Width >> 1);
        labelList[6] = new Label("Search for :(nom de produit)");
        textFieldList[5] = new TextField();
        Button modifyButton = new Button("Modifier un produit");
        modifyButton.setOnMouseClicked(event -> {
            new ProductUI(this.mainStage);
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        holder.getChildren().addAll(labelList[6], textFieldList[5], spacer, modifyButton);

        ProductUI.createDataView(productsObservableList, productsTableView);
        productsTableView.setMaxHeight(90);
        FilteredList<Product> filteredList = new FilteredList<>(productsObservableList);
        productsTableView.setItems(filteredList);
        textFieldList[5].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true :
                        s -> s.getDesignation().toLowerCase().contains(newValue.toLowerCase())
                ));
        productsContainer.getChildren().addAll(holder, productsTableView);

    }

    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(30);
        tableContainer.setMinWidth(Width / 5.0 - 50);
        labelList[7] = new Label("Search for :(type)");
        textFieldList[6] = new TextField();
        createDataView();
        tableContainer.getChildren().addAll(labelList[7], textFieldList[6], saleItemTableView);

    }

    @Override
    protected void createDataView() {
        if (saleItemDAOIMPL == null) {
            saleItemObservableList = FXCollections.observableArrayList(new ArrayList<>());
        }
        saleItemTableView = new TableView<>();
        createDataView(saleItemObservableList, saleItemTableView);
        saleItemTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFieldList[0].setText(String.valueOf(newSelection.getId()));
                textFieldList[1].setText(String.valueOf(newSelection.getQuantity()));
//                textFieldList[2].setText(String.valueOf(newSelection.getSubTotal()));
                productsTableView.getSelectionModel().select(newSelection.getProduct());

            }

        });
    }

    @Override
    protected void addButtonClick() {

        Product p = productsTableView.getSelectionModel().getSelectedItem();
        SaleItem saleItem = null;
        for (SaleItem si : saleItemObservableList) {
            if (si.getProduct().equals(p)) {
                System.out.println("Found it");
                saleItem = si;
                break;
            }
        }

        long num = 1;
        if (saleItemObservableList.size() != 0)
            num = saleItemObservableList.get(saleItemObservableList.size() - 1).getNum() + 1;
        Long quantity = checkLong(textFieldList[1]);
        if (saleItem != null && quantity != null) {
            double old = saleItem.getQuantity();
            saleItem.setQuantity(saleItem.getQuantity() + quantity);
            if (saleItemDAOIMPL.updateQuantity(saleItem, old)) {
                System.out.println("Updaaaated");
//                saleItemTableView.refresh();
                clearButtonClick();
            }
        } else if (p != null && quantity != null) {
            saleItem = new SaleItem(null, num, quantity, p, sale);
            if (saleItemDAOIMPL.create(saleItem)) {
                saleItemObservableList.add(saleItem);
                clearButtonClick();
            }
        } else {
            System.out.println("Non valid input");
        }
        textFieldList[3].setText(String.valueOf(sale.getId()));
        textFieldList[4].setText(String.valueOf(sale.getTotal()));
    }

    @Override
    protected void updateButtonClick() {
        Product p = productsTableView.getSelectionModel().getSelectedItem();
        SaleItem saleItem = saleItemTableView.getSelectionModel().getSelectedItem();
        int index = saleItemTableView.getSelectionModel().getSelectedIndex();
        Long quantity = checkLong(textFieldList[1]);
        if (p != null && saleItem != null && quantity != null) {
            if (saleItemDAOIMPL.update(saleItem, new SaleItem(null, saleItem.getNum(), quantity, p, sale))) {
                saleItemObservableList.set(index, saleItem);
                for (int i = 0; i < textFieldList.length - 1; i++)
                    textFieldList[i].clear();
                saleItemTableView.getSelectionModel().clearSelection();
                productsTableView.getSelectionModel().clearSelection();
                textFieldList[3].setText(String.valueOf(sale.getId()));
                textFieldList[4].setText(String.valueOf(sale.getTotal()));
            }

        } else {
            System.out.println("Non valid input");
        }
    }

    @Override
    protected void deleteButtonClick() {
        SaleItem saleItem = saleItemTableView.getSelectionModel().getSelectedItem();

        if (saleItemDAOIMPL.delete(saleItem)) {
            saleItemObservableList.remove(saleItem);
            saleItemTableView.getSelectionModel().clearSelection();
            clearButtonClick();
            textFieldList[3].setText(String.valueOf(sale.getId()));
            textFieldList[4].setText(String.valueOf(sale.getTotal()));
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

