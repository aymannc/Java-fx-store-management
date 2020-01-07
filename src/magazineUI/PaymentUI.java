package magazineUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import paymentsManagment.Payment;
import paymentsManagment.PaymentDAOIMPL;
import salesManagment.Sale;
import salesManagment.SaleDAOIMPL;

import java.util.ArrayList;

public class PaymentUI extends BaseUI {
    PaymentDAOIMPL paymentDAOIMPL;
    SaleDAOIMPL saleDAOIMPL;
    ObservableList<Payment> paymentObservableList;
    TableView<Payment> paymentTableView;
    DatePicker datePicker;
    ComboBox comboBox;

    Sale sale;

    public PaymentUI(String label, Stage parentStage, Sale sale) {
        super(label);
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
        this.sale = sale;
    }

    public PaymentUI(Stage parentStage, Sale sale) {
        super("Gestion des payments");
        mainStage.setOnHiding(event -> {
            parentStage.show();
        });
        this.sale = sale;
        initTableViewData();
    }

    public PaymentUI(Sale sale) {
        super("Gestion des payments");
        this.sale = sale;
        initTableViewData();
    }

    public PaymentUI(String label, Sale sale) {
        super(label);
        this.sale = sale;
        initTableViewData();
    }

    public static void createDataView(ObservableList<Payment> observableList,
                                      TableView<Payment> tableView) {
        TableColumn<Payment, Long> column0 = new TableColumn<>("ID vent");
        column0.setCellValueFactory(new PropertyValueFactory<>("saleID"));
        TableColumn<Payment, Long> column1 = new TableColumn<>("N°");
        column1.setCellValueFactory(new PropertyValueFactory<>("num"));
        TableColumn<Payment, Double> column2 = new TableColumn<>("Montatnt");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Payment, String> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Payment, String> column4 = new TableColumn<>("Date");
        column4.setCellValueFactory(new PropertyValueFactory<>("date"));

        column0.setPrefWidth(Width / 10.0 - 10);
        column1.setPrefWidth(Width / 10.0 - 10);
        column2.setPrefWidth(Width / 10.0);
        column3.setPrefWidth(Width / 10.0);
        column4.setPrefWidth(Width / 10.0 + 10);
        tableView.getColumns().addAll(column1, column0, column2, column3, column4);
        try {
            tableView.setItems(observableList);
        } catch (NullPointerException e) {
            System.out.println("Products list empty");
        }
        tableView.setMaxHeight(3 * Heigth / 5);
    }

    private void initTableViewData() {
        paymentObservableList = paymentDAOIMPL.getAll(sale);
        if (paymentObservableList == null) {
            paymentObservableList = FXCollections.observableArrayList(new ArrayList<>());
        }
        FilteredList<Payment> filteredList = new FilteredList<>(paymentObservableList);
        paymentTableView.setItems(filteredList);
        textFieldList[2].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(newValue == null || newValue.length() == 0 ? s -> true : s -> s.getType().
                        toLowerCase().contains(newValue.toLowerCase()))
        );
    }

    @Override
    protected void createInsertContainer() {
        saleDAOIMPL = new SaleDAOIMPL();
        labelList = new Label[8];
        textFieldList = new TextField[5];
        insertContainer = new VBox(10);
        insertContainer.setMinWidth(Width / 3.0 - 20);
        labelList[0] = new Label("Total : ");
        textFieldList[0] = new TextField("2");
        readOnlyTextField(textFieldList[0]);
        HBox holder1 = new HBox(10, labelList[0], textFieldList[0]);
        labelList[1] = new Label("Reste : ");
        textFieldList[1] = new TextField("2");
        readOnlyTextField(textFieldList[1]);
        HBox holder2 = new HBox(10, labelList[1], textFieldList[1]);
        labelList[7] = new Label("Total payé:");
        textFieldList[4] = new TextField("2");
        readOnlyTextField(textFieldList[4]);
        HBox holder3 = new HBox(10, labelList[7], textFieldList[4]);
        labelList[2] = new Label("N :");
        textFieldList[2] = new TextField();
        readOnlyTextField(textFieldList[2]);
        labelList[3] = new Label("Date :");
        datePicker = new DatePicker();
        labelList[4] = new Label("Montant :");
        textFieldList[3] = new TextField();
        labelList[5] = new Label("Type :");
        comboBox = new ComboBox(FXCollections.observableArrayList(SaleDAOIMPL.paymentTypes));


        insertContainer.getChildren().addAll(holder1, holder2, holder3, labelList[2], textFieldList[2], labelList[3],
                datePicker, labelList[4], textFieldList[3], labelList[5], comboBox);

    }

    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(30);
        tableContainer.setMinWidth(Width >> 1);
        labelList[6] = new Label("Search for :(type)");
        textFieldList[2] = new TextField();
        createDataView();
        tableContainer.getChildren().addAll(labelList[6], textFieldList[2], paymentTableView);

    }

    @Override
    protected void createDataView() {
        paymentDAOIMPL = new PaymentDAOIMPL();
        paymentObservableList = paymentDAOIMPL.getAll(sale);
        paymentTableView = new TableView<>();
        createDataView(null, paymentTableView);
        setTotalPayed(textFieldList[4]);
        paymentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

            }

        });

    }

    private void setTotalPayed(TextField label) {
        double total;
        try {
            total = paymentObservableList.stream().mapToDouble(Payment::getAmount).sum();
        } catch (NullPointerException e) {
            total = 0;
        }

        label.setText(String.valueOf(total));

    }

    @Override
    protected void addButtonClick() {

    }

    @Override
    protected void updateButtonClick() {

    }

    @Override
    protected void deleteButtonClick() {

    }

}
