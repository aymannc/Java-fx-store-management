package magazineUI;

import clientsManagement.Client;
import clientsManagement.ClientDAOIMPL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientUI extends BaseUI {
    ClientDAOIMPL clientDAOIMPL = null;
    ObservableList<Client> observableList = null;
    private ComboBox comboBox = null;

    public ClientUI(String label, Stage parentStage) {
        super(label);
        mainStage.setOnHiding(event -> parentStage.show());
    }

    public ClientUI(Stage parentStage) {
        super("Gestion des clients");
        mainStage.setOnHiding(event -> parentStage.show());
    }

    public static void createDataView(ObservableList<Client> observableList, TableView<Client> tableView) {
        TableColumn<Client, Long> column1 = new TableColumn<>("Code");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> column2 = new TableColumn<>("Nome");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Client, String> column3 = new TableColumn<>("Sexe");
        column3.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<Client, String> column4 = new TableColumn<>("Adresse");
        column4.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<Client, String> column5 = new TableColumn<>("Téléphone");
        column5.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Client, String> column6 = new TableColumn<>("Email");
        column6.setCellValueFactory(new PropertyValueFactory<>("email"));
        column1.setPrefWidth(Width / 24.0 - 5);
        column2.setPrefWidth(Width / 12.0 + Width / 24.0);
        column3.setPrefWidth(Width / 24.0 - 2);
        column4.setPrefWidth(Width / 12.0);
        column5.setPrefWidth(Width / 12.0);
        column6.setPrefWidth(Width / 12.0 + Width / 24.0 + 5);
        try {
            tableView.setItems(observableList);
        } catch (NullPointerException e) {
            System.out.println("Clients list empty");
        }
        tableView.getColumns().addAll(column1, column2, column3, column4, column5, column6);

    }

    @Override
    protected void createInsertContainer() {
        clientDAOIMPL = new ClientDAOIMPL();
        labelList = new Label[7];
        textFieldList = new TextField[6];
        insertContainer = new VBox(10);
        insertContainer.setMinWidth((Width / 3.0) - 20);
        labelList[0] = new Label("Code :");
        textFieldList[0] = new TextField();
        labelList[1] = new Label("Nome :");
        textFieldList[1] = new TextField();
        labelList[2] = new Label("Sexe :");
        comboBox = new ComboBox(FXCollections.observableArrayList("M", "F"));
        labelList[3] = new Label("Adresse :");
        textFieldList[2] = new TextField();
        labelList[4] = new Label("Téléphone :");
        textFieldList[3] = new TextField();
        labelList[5] = new Label("Email :");
        textFieldList[4] = new TextField();

        insertContainer.getChildren().addAll(labelList[0], textFieldList[0], labelList[1], textFieldList[1],
                labelList[2], comboBox, labelList[3], textFieldList[2], labelList[4], textFieldList[3],
                labelList[5], textFieldList[4]);
    }

    @Override
    protected void createTableContainer() {
        tableContainer = new VBox(30);
        tableContainer.setMinWidth(Width / 2.0);
        labelList[6] = new Label("Search for :");
        textFieldList[5] = new TextField();
        createDataView();
        FilteredList<Client> filteredList = new FilteredList<>(observableList);
        tableView.setItems(filteredList);
        textFieldList[5].textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate((newValue == null || newValue.length() == 0) ? s -> true : s -> s.getName().toLowerCase().contains(newValue.toLowerCase()))
        );

        tableContainer.getChildren().addAll(labelList[6], textFieldList[5], tableView);


    }

    @Override
    protected void createDataView() {
        clientDAOIMPL = new ClientDAOIMPL();
        observableList = clientDAOIMPL.getAll();
        tableView = new TableView<Client>();
        createDataView(observableList, tableView);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFieldList[0].setText(String.valueOf(((Client) newSelection).getId()));
                textFieldList[1].setText(((Client) newSelection).getName());
                comboBox.getSelectionModel().select(((Client) newSelection).getGender());
                textFieldList[2].setText(String.valueOf(((Client) newSelection).getAddress()));
                textFieldList[3].setText(String.valueOf(((Client) newSelection).getPhone()));
                textFieldList[4].setText(String.valueOf(((Client) newSelection).getEmail()));
            }

        });

    }

    private String checkEmail(TextField textField) {
        return textField.getText();
    }

    private String checkPhone(TextField textField) {
        return textField.getText();
    }

    @Override
    protected void addButtonClick() {
        Long id = checkLong(textFieldList[0]);
        String phone = checkPhone(textFieldList[2]);
        String email = checkEmail(textFieldList[2]);
        String gender = getStringComboBoxInput(comboBox);
        if (id != null && phone != null && email != null && gender != null) {
            Client c = new Client(id, textFieldList[1].getText(), gender,
                    textFieldList[2].getText(), phone, email);
            if (clientDAOIMPL.create(c)) {
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
        Client c = (Client) tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        Long id = checkLong(textFieldList[0]);
        String phone = checkPhone(textFieldList[2]);
        String email = checkEmail(textFieldList[2]);
        String gender = getStringComboBoxInput(comboBox);
        if (id != null && phone != null && email != null && gender != null) {
            if (clientDAOIMPL.update(c, new Client(id, textFieldList[1].getText(), gender,
                    textFieldList[2].getText(), phone, email))) {
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
        Client c = (Client) tableView.getSelectionModel().getSelectedItem();
        if (clientDAOIMPL.delete(c)) {
            observableList.remove(c);
            tableView.getSelectionModel().clearSelection();
        }

    }

}
