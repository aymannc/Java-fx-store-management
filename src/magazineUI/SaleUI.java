package magazineUI;

import clientsManagement.Client;
import clientsManagement.ClientDAOIMPL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import paymentsManagment.Payment;
import paymentsManagment.PaymentDAOIMPL;
import salesManagment.Sale;
import salesManagment.SaleDAOIMPL;
import salesManagment.SaleItem;
import salesManagment.SaleItemDAOIMPL;

import java.util.ArrayList;
import java.util.List;

public class SaleUI extends BaseUI {
    PaymentDAOIMPL paymentDAOIMPL;
    ClientDAOIMPL clientDAOIMPL;
    SaleItemDAOIMPL saleItemDAOIMPL;
    SaleDAOIMPL saleDAOIMPL;
    ObservableList<Sale> salesObservableList;
    ObservableList<Client> clientsObservableList;
    ObservableList<SaleItem> saleItemObservableList;
    ObservableList<Payment> paymentObservableList;

    VBox clientsContainer;
    VBox saleItemsContainer;
    VBox paymentsContainer;

    TableView<Client> clientTableView;
    TableView<Sale> saleTableView;
    TableView<SaleItem> saleItemTableView;
    TableView<Payment> paymentTableView;
    ComboBox<String> comboBox2;

    public SaleUI() {
        this("Gestion des vents");
    }

    public SaleUI(String label) {
        super(1200, 600, label);
    }

    @Override
    protected void createInsertContainer() {
        saleDAOIMPL = new SaleDAOIMPL();
        paymentDAOIMPL = new PaymentDAOIMPL();

        clientsContainer = new VBox(5);
        saleItemsContainer = new VBox(5);
        paymentsContainer = new VBox(5);

        labelList = new Label[9];
        textFieldList = new TextField[5];

        insertContainer = new VBox(15);
        insertContainer.setMinWidth((Width >> 1));
        labelList[0] = new Label("ID :");
        textFieldList[0] = new TextField();
        readOnlyTextField(textFieldList[0]);
        labelList[1] = new Label("Client :");
        createClientsTableView();
        labelList[2] = new Label("Ligne de commande :");
        createSaleItemsTableView();
        labelList[4] = new Label("Paiements :");
        createPaymentsTableView();
        HBox holder = new HBox(10);
        holder.setMinWidth(Width >> 1);
        labelList[3] = new Label("Total :");
        textFieldList[1] = new TextField();
        readOnlyTextField(textFieldList[1]);
        holder.getChildren().addAll(labelList[3], textFieldList[1]);

        insertContainer.getChildren().addAll(labelList[0], textFieldList[0], clientsContainer, saleItemsContainer, paymentsContainer, holder);
    }

    private void createClientsTableView() {
        clientsContainer = new VBox(5);
        clientDAOIMPL = new ClientDAOIMPL();
        clientsObservableList = clientDAOIMPL.getAll();
        clientTableView = new TableView<>();
        clientTableView.setMaxWidth(Width >> 1);
        HBox holder = new HBox(10);
        holder.setMinWidth(Width >> 1);
        labelList[6] = new Label("Search for :(nom de client)");
        textFieldList[3] = new TextField();
        Button modifyButton = new Button("Modifier un client");
        modifyButton.setOnMouseClicked(event -> {
            new ClientUI(this.mainStage);
            mainStage.close();
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        holder.getChildren().addAll(labelList[6], textFieldList[3], spacer, modifyButton);

        ClientUI.createDataView(clientsObservableList, clientTableView);
        clientTableView.setMaxHeight(75);

        FilteredList<Client> filteredList = new FilteredList<>(clientsObservableList);
        clientTableView.setItems(filteredList);
        textFieldList[3].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true :
                        s -> s.getName().toLowerCase().contains(newValue.toLowerCase())
                ));
        clientsContainer.getChildren().addAll(holder, clientTableView);

    }

    private void createSaleItemsTableView() {
        saleItemsContainer = new VBox(5);
        saleItemDAOIMPL = new SaleItemDAOIMPL(null);
        saleItemObservableList = FXCollections.observableList(new ArrayList<>());
        saleItemTableView = new TableView<>();
        saleItemTableView.setMaxWidth(Width >> 1);
        HBox holder = new HBox(10);
        holder.setMinWidth(Width >> 1);
        labelList[7] = new Label("Search for :(produit)");
        textFieldList[4] = new TextField();
        Button modifyButton = new Button("Modifier LC");
        modifyButton.setOnMouseClicked(event -> {
            Sale sale = saleTableView.getSelectionModel().getSelectedItem();
            if (sale != null) {
                this.mainStage.close();
                new SaleItemUI(sale, false);
            }
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        holder.getChildren().addAll(labelList[7], textFieldList[4], spacer, modifyButton);

        SaleItemUI.createDataView(saleItemObservableList, saleItemTableView);
        saleItemTableView.setMaxHeight(75);

        FilteredList<SaleItem> filteredList = new FilteredList<>(saleItemObservableList);
        saleItemTableView.setItems(filteredList);
        textFieldList[4].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true : s -> s.getProduct().getDesignation().toLowerCase().contains(newValue.toLowerCase())
                ));
        saleItemsContainer.getChildren().addAll(holder, saleItemTableView);
    }

    private void createPaymentsTableView() {
        paymentsContainer = new VBox(5);
        paymentDAOIMPL = new PaymentDAOIMPL();
        paymentObservableList = FXCollections.observableList(new ArrayList<>());
        paymentTableView = new TableView<>();
        paymentTableView.setMaxWidth(Width >> 1);
        HBox holder = new HBox(10);
        holder.setMinWidth(Width >> 1);
        labelList[8] = new Label("Search for :(Type)");
        Button modifyButton = new Button("Modifier les payment");
        modifyButton.setOnMouseClicked(event -> {
            Sale sale = saleTableView.getSelectionModel().getSelectedItem();
            if (sale != null) {
                this.mainStage.close();
                new PaymentUI(sale);
            }

        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ArrayList<String> cl = new ArrayList<>(List.of(SaleDAOIMPL.paymentTypes));
        cl.add(0, "All");
        comboBox2 = new ComboBox<>(FXCollections.observableArrayList(cl));
        comboBox2.getSelectionModel().selectFirst();
        holder.getChildren().addAll(labelList[8], comboBox2, spacer, modifyButton);

        PaymentUI.createDataView(null, paymentTableView);
        paymentTableView.setMaxHeight(75);
        FilteredList<Payment> filteredList = new FilteredList<>(paymentObservableList);

        paymentTableView.setItems(filteredList);
        comboBox2.valueProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(newValue.equals("All") ? s -> true : s -> s.getType().
                        toLowerCase().contains(newValue.toLowerCase()))
        );
        paymentsContainer.getChildren().addAll(holder, paymentTableView);
    }


    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(5);
        tableContainer.setMinWidth((Width / 3.0) - 20);
        labelList[5] = new Label("Search for :(nom de client)");
        textFieldList[2] = new TextField();

        createDataView();

        FilteredList<Sale> filteredList = new FilteredList<>(salesObservableList);
        saleTableView.setItems(filteredList);
        textFieldList[2].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true :
                        s -> s.getClient().getName().toLowerCase().contains(newValue.toLowerCase())
                ));

        tableContainer.getChildren().addAll(labelList[5], textFieldList[2], saleTableView);

    }

    @Override
    protected void createDataView() {
        saleDAOIMPL = new SaleDAOIMPL();
        salesObservableList = saleDAOIMPL.getAll();
        saleTableView = new TableView<>();
        TableColumn<Sale, Integer> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Sale, String> column2 = new TableColumn<>("Date d'ajoute");
        column2.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        TableColumn<Sale, String> column3 = new TableColumn<>("Nome de client");
        column3.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        TableColumn<Sale, Integer> column4 = new TableColumn<>("Total");
        column4.setCellValueFactory(new PropertyValueFactory<>("total"));
        column1.setPrefWidth(Width / 15.0 - 20);
        column2.setPrefWidth(1.5 * Width / 15.0);
        column3.setPrefWidth(1.5 * Width / 15.0);
        column4.setPrefWidth(Width / 15.0);
        try {
            saleTableView.setItems(salesObservableList);
        } catch (NullPointerException e) {
            System.out.println("Sales list is empty");
        }
        saleTableView.getColumns().addAll(column1, column3, column2, column4);
        saleTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFieldList[0].setText(String.valueOf(newSelection.getId()));
                textFieldList[1].setText(String.valueOf(newSelection.getTotal()));
                clientTableView.getSelectionModel().select(newSelection.getClient());
                paymentObservableList.clear();
                saleItemObservableList.clear();
                try {
                    paymentObservableList.addAll(paymentDAOIMPL.getAll(newSelection));
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    saleItemDAOIMPL.setSale(newSelection);
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    saleItemObservableList.addAll(saleItemDAOIMPL.getAllObsList());
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    paymentTableView.setItems(paymentObservableList);
                } catch (Exception e) {
                    System.out.println(e);
                }
                textFieldList[1].setText(String.valueOf(newSelection.getTotal()));
            }

        });
    }


    @Override
    protected void addButtonClick() {
        Client c = clientTableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            Sale s = new Sale();
            s.setClient(c);
            mainStage.close();
            new SaleItemUI(s, true);
        }
    }

    @Override
    protected void updateButtonClick() {
        Client c = clientTableView.getSelectionModel().getSelectedItem();
        Sale sale = saleTableView.getSelectionModel().getSelectedItem();
        int index = saleTableView.getSelectionModel().getSelectedIndex();
        if (c != null) {
            Sale s = new Sale(null, c, 0, null, null, null);
            if (saleDAOIMPL.update(sale, s)) {
                salesObservableList.set(index, sale);
                clearButtonClick();
            }
        }
    }

    @Override
    protected void deleteButtonClick() {
        Sale s = saleTableView.getSelectionModel().getSelectedItem();
        if (saleDAOIMPL.delete(s)) {
            salesObservableList.remove(s);
            clearButtonClick();
        }
    }

    @Override
    void clearButtonClick() {
        for (TextField textField : textFieldList) textField.clear();
        try {
            saleTableView.getSelectionModel().clearSelection();
        } catch (NullPointerException ignored) {
            System.out.println(1);
        }
        try {
            saleItemObservableList.clear();
        } catch (NullPointerException ignored) {
            System.out.println(2);
        }
        try {
            saleItemObservableList.clear();
        } catch (NullPointerException ignored) {

            System.out.println(3);
        }
    }
}

