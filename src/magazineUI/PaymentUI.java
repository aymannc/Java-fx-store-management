package magazineUI;

import bank.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.jetbrains.annotations.NotNull;
import paymentsManagment.Payment;
import paymentsManagment.PaymentDAOIMPL;
import salesManagment.Sale;
import salesManagment.SaleDAOIMPL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class PaymentUI extends BaseUI {
    PaymentDAOIMPL paymentDAOIMPL;
    SaleDAOIMPL saleDAOIMPL;
    AccountDAOIMPL accountDAOIMPL;
    ObservableList<Payment> paymentObservableList;
    TableView<Payment> paymentTableView;
    DatePicker datePicker;
    ComboBox<String> comboBox;
    ComboBox<String> comboBox2;
    Sale sale;
    Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    private DoubleProperty total;
    private DoubleProperty payed;
    private DoubleProperty rest;

    public PaymentUI(String label, Sale sale) {
        super(label);
        mainStage.setOnHiding(event -> new SaleUI());
        this.sale = sale;
        initTableViewData();
    }

    public PaymentUI(Sale sale) {
        this("Gestion des payments", sale);
    }

    public static void createDataView(ObservableList<Payment> observableList,
                                      TableView<Payment> tableView) {

        TableColumn<Payment, Long> column1 = new TableColumn<>("N°");
        column1.setCellValueFactory(new PropertyValueFactory<>("num"));
        TableColumn<Payment, Double> column2 = new TableColumn<>("Montatnt");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Payment, String> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Payment, String> column4 = new TableColumn<>("N° Chèque");
        column4.setCellValueFactory(new PropertyValueFactory<>("checknumber"));
        TableColumn<Payment, String> column5 = new TableColumn<>("Etat");
        column5.setCellValueFactory(new PropertyValueFactory<>("state"));
        TableColumn<Payment, String> column6 = new TableColumn<>("Date");
        column6.setCellValueFactory(new PropertyValueFactory<>("date"));

        column1.setPrefWidth(Width / 12.0 - 10);
        column2.setPrefWidth(Width / 12.0);
        column3.setPrefWidth(Width / 12.0);
        column4.setPrefWidth(Width / 12.0 + 10);
        column5.setPrefWidth(Width / 12.0 - 10);
        column6.setPrefWidth(Width / 12.0);
        tableView.getColumns().addAll(column1, column2, column3, column4, column5, column6);
        try {
            tableView.setItems(observableList);
        } catch (NullPointerException e) {
            System.out.println("Payments list empty");
        }
        tableView.setMaxHeight(3 * Heigth / 5.0);
    }

    private void initTableViewData() {
        paymentObservableList = paymentDAOIMPL.getAll(sale);
        if (paymentObservableList == null) {
            paymentObservableList = FXCollections.observableArrayList(new ArrayList<>());
        }
        FilteredList<Payment> filteredList = new FilteredList<>(paymentObservableList);
        paymentTableView.setItems(filteredList);
        comboBox2.valueProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(newValue.equals("All") ? s -> true : s -> s.getType().
                        toLowerCase().contains(newValue.toLowerCase()))
        );
        total.set(sale.getTotal());
        payed.set(paymentDAOIMPL.getTotalPayed(sale));
        textFieldList[3].setText(String.valueOf(rest.get()));
    }

    @Override
    protected void createInsertContainer() {

        total = new SimpleDoubleProperty();
        payed = new SimpleDoubleProperty();
        rest = new SimpleDoubleProperty();
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
        datePicker = new DatePicker(LocalDate.now());
        labelList[4] = new Label("Montant :");
        textFieldList[3] = new TextField();
        labelList[5] = new Label("Type :");
        comboBox = new ComboBox<>(FXCollections.observableArrayList(SaleDAOIMPL.paymentTypes));

        StringConverter<? extends Number> converter = new DoubleStringConverter();
        Bindings.bindBidirectional(textFieldList[1].textProperty(), rest, (StringConverter<Number>) converter);
        Bindings.bindBidirectional(textFieldList[4].textProperty(), payed, (StringConverter<Number>) converter);
        Bindings.bindBidirectional(textFieldList[0].textProperty(), total, (StringConverter<Number>) converter);
        rest.bind(total.subtract(payed));
        insertContainer.getChildren().addAll(holder1, holder3, holder2, labelList[2], textFieldList[2], labelList[3],
                datePicker, labelList[4], textFieldList[3], labelList[5], comboBox);

    }

    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(30);
        tableContainer.setMinWidth(Width >> 1);
        labelList[6] = new Label("Search for :(type)");
        ArrayList<String> cl = new ArrayList<>(List.of(SaleDAOIMPL.paymentTypes));
        cl.add(0, "All");
        comboBox2 = new ComboBox<>(FXCollections.observableArrayList(cl));
        comboBox2.getSelectionModel().selectFirst();
        createDataView();
        tableContainer.getChildren().addAll(labelList[6], comboBox2, paymentTableView);

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
                textFieldList[2].setText(String.valueOf(newSelection.getNum()));
                datePicker.setValue(newSelection.getDate());
                textFieldList[3].setText(String.valueOf(newSelection.getAmount()));
                comboBox.getSelectionModel().select(newSelection.getType());
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
        if (rest.getValue() > 0) {
            long num = 1;
            try {
                num = paymentObservableList.get(paymentObservableList.size() - 1).getNum() + 1;
            } catch (Exception ignored) {
            }
            LocalDate date = getDateDatepicker(datePicker);
            Double amount = checkDouble(textFieldList[3]);
            String type = getStringComboBoxInput(comboBox);
            if (date != null && amount != null && type != null) {

                if (amount > rest.get()) {
                    setAsError(textFieldList[3]);
                    showAlert(Alert.AlertType.ERROR, "Reduce the amount");
                } else {
                    Payment p = new Payment(null, num, amount, date, type, Payment.paymentsStates[0], "------", sale);
                    if (paymentDAOIMPL.create(p)) {
                        if (!checkPaymentType(p))
                            paymentDAOIMPL.delete(p);
                        else {
                            paymentObservableList.add(p);
                            clearButtonClick();
                            payed.set(payed.get() + amount);
                        }
                    }
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Non valid input");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Already payed");
        }
    }

    private boolean checkPaymentType(@NotNull Payment p) {
        if (p.getType().equals(SaleDAOIMPL.paymentTypes[0])) {
            CardUI c = new CardUI();
            c.showStage();
            String account_number = c.getData();
            System.out.println(account_number);
            if (account_number == null)
                return false;
            Transaction transaction = new Transaction(p.getAmount(), new Account(account_number));
            boolean valid = sendRequest(transaction);
            if (valid) {
                valid = getResponse();
            }
            return valid;
        }
        if (p.getType().equals(SaleDAOIMPL.paymentTypes[2])) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Check number");
            dialog.setContentText("Please enter the check number:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                p.setState(Payment.paymentsStates[1]);
                return paymentDAOIMPL.update(p,
                        new Payment((long) 0, p.getNum(), p.getAmount(), p.getDate(), p.getType(), p.getState(), result.get(), p.getSale()));
            } else {
                return false;
            }
        }
        if (p.getType().equals(SaleDAOIMPL.paymentTypes[3])) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle("Number of drafts");
            dialog.setContentText("Please enter the drafts number:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int number = checkInt(result.get());
                double amount = p.getAmount() / number;
                for (int i = 0; i < number; i++) {
                    Payment payment = new Payment((long) 0, p.getNum() + i, amount, LocalDate.now().plusMonths(i),
                            SaleDAOIMPL.paymentTypes[3], Payment.paymentsStates[1], "------", p.getSale());
                    if (paymentDAOIMPL.create(payment)) {
                        paymentObservableList.add(payment);
                        clearButtonClick();
                        payed.set(payed.get() + amount);
                    }

                }
                return false;
            }
        }
        return true;
    }

    private boolean sendRequest(Object o) {
        boolean valid = true;
        try {
            socket = new Socket(BankServer.domain, BankServer.port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(o);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "500 Internal Server Error : " + e.getMessage());
            valid = false;
        }
        return valid;
    }

    private boolean getResponse() {
        boolean valid = true;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            Object o = inputStream.readObject();
            if (o instanceof Response) {
                valid = responseType((Response) o);
            }
        } catch (IOException | ClassNotFoundException e) {
            valid = false;
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }
        return valid;
    }

    boolean responseType(Response r) {

        if (r.getCode() >= 400) {
            showAlert(Alert.AlertType.ERROR, r.getCode() + " : " + r.getBody());
        } else if (r.getCode() >= 200) {
            showAlert(Alert.AlertType.INFORMATION, r.getCode() + " : " + r.getBody());
            return true;
        }
        return false;
    }

    @Override
    protected void updateButtonClick() {
        AtomicBoolean DoIt = new AtomicBoolean(false);
        Payment p = paymentTableView.getSelectionModel().getSelectedItem();
        Payment p2 = p;
        double deference = 0;
        int index = paymentTableView.getSelectionModel().getSelectedIndex();
        LocalDate date = getDateDatepicker(datePicker);

        if (p != null && date != null) {
            if (showAlert(Alert.AlertType.CONFIRMATION, "Do you want to update ?") == ButtonType.OK) {
                if (p.getType().equals(SaleDAOIMPL.paymentTypes[2])) {
                    Dialog<Payment> dialog = new Dialog<>();
                    dialog.setTitle("Check number");
                    dialog.setHeaderText("Please enter the check number and state");
                    DialogPane dialogPane = dialog.getDialogPane();
                    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                    TextField textField = new TextField(p.getChecknumber());
                    ObservableList<String> options =
                            FXCollections.observableArrayList(Payment.paymentsStates);
                    ComboBox<String> comboBox = new ComboBox<>(options);
                    comboBox.getSelectionModel().select(p.getState());
                    dialogPane.setContent(new VBox(8, textField, comboBox));
                    Platform.runLater(textField::requestFocus);
                    dialog.setResultConverter((ButtonType button) -> {
                        if (button == ButtonType.OK) {
                            String s = textField.getText().equals("") ? "------" : textField.getText();
                            return new Payment(p.getId(), p.getNum(), p.getAmount(), date, p.getType(), comboBox.getSelectionModel().getSelectedItem(), s, sale);
                        }
                        return null;
                    });
                    p2 = dialog.showAndWait().orElse(null);
                    if (p2 != null)
                        DoIt.set(showAlert(Alert.AlertType.WARNING, "Do you want to update ?") == ButtonType.OK);
                } else if (p.getType().equals(SaleDAOIMPL.paymentTypes[3])) {
                    List<String> choices = new ArrayList<>();
                    choices.add(SaleDAOIMPL.paymentTypes[1]);
                    choices.add(SaleDAOIMPL.paymentTypes[2]);
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(SaleDAOIMPL.paymentTypes[1], choices);
                    dialog.setTitle("Choose a payment type");
                    dialog.setContentText("Choose a payment:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(type -> {
                        if (type.equals(SaleDAOIMPL.paymentTypes[1]))
                            p.setState(Payment.paymentsStates[0]);
                        p.setType(type);
                        DoIt.set(showAlert(Alert.AlertType.WARNING, "Do you want to update ?") == ButtonType.OK);
                    });
                } else {
                    Double amount = checkDouble(textFieldList[3]);
                    if (amount != null) {
                        deference = amount - p.getAmount();
                        if (deference > rest.get()) {
                            setAsError(textFieldList[3]);
                            showAlert(Alert.AlertType.ERROR, "Reduce the amount");
                        } else {
                            p2 = new Payment(null, 0, amount, date, p.getType(), p.getState(), p.getChecknumber(), sale);
                            DoIt.set(showAlert(Alert.AlertType.WARNING, "Do you want to update ?") == ButtonType.OK);
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Non valid input");
                    }
                }

                if (DoIt.get())
                    if (paymentDAOIMPL.update(p, p2)) {
                        paymentObservableList.set(index, p);
                        clearButtonClick();
                        paymentTableView.getSelectionModel().clearSelection();
                        payed.set(payed.get() + deference);
                    }
            }
        }
    }

    @Override
    protected void deleteButtonClick() {
        Payment p = paymentTableView.getSelectionModel().getSelectedItem();
        if (p != null) {
            if (showAlert(Alert.AlertType.WARNING, "Do you want to delete ?") == ButtonType.OK)
                if (paymentDAOIMPL.delete(p)) {
                    payed.setValue(payed.get() - p.getAmount());
                    paymentObservableList.remove(p);
                    paymentTableView.getSelectionModel().clearSelection();
                }
        } else showAlert(Alert.AlertType.ERROR, "Nothing selected");
    }

    @Override
    void clearButtonClick() {
        textFieldList[2].clear();
        textFieldList[3].clear();
        try {
            paymentTableView.getSelectionModel().clearSelection();
        } catch (NullPointerException ignored) {
        }
        datePicker.setValue(LocalDate.now());
        comboBox.getEditor().clear();
        textFieldList[3].setText(String.valueOf(rest.get()));
    }
}

