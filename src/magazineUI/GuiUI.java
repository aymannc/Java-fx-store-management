package magazineUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import salesManagment.SaleDAOIMPL;

public class GuiUI {
    protected static int Heigth = 300;
    protected static int Width = 400;
    protected Scene mainScene;
    protected Stage mainStage;
    protected VBox container;
    ProductUI productUI;
    ClientUI clientUI;
    CategoryUI categoryUI;
    SaleUI saleUI;
    SaleItemUI saleItemUI;
    PaymentUI paymentUI;
    SaleDAOIMPL saleDAOIMPL = new SaleDAOIMPL();
    Button[] buttonsList = new Button[4];

    public GuiUI() {
        container = new VBox(Heigth / 10.0);
        container.setAlignment(Pos.CENTER);
        buttonsList[0] = new Button("Gestion des produits");
        buttonsList[1] = new Button("Gestion des catégories");
        buttonsList[2] = new Button("Gestion des clients");
        buttonsList[3] = new Button("Gestion des ventes");
        for (Button button : buttonsList) {
            button.setPrefWidth(2 * Width / 3.0);
        }
        container.getChildren().addAll(buttonsList);
        mainScene = new Scene(container, Width, Heigth);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.show();
//        saleUI = new SaleUI(mainStage);
//        saleItemUI = new SaleItemUI(saleDAOIMPL.findAll().get(0), false);
//        Sale s = saleDAOIMPL.findAll().get(1);
//        paymentUI = new PaymentUI(s);
        buttonsList[0].setOnMouseClicked(mouseEvent -> {
            mainStage.hide();
            productUI = new ProductUI(buttonsList[0].getText(), mainStage);
        });
        buttonsList[1].setOnMouseClicked(mouseEvent -> {
            mainStage.hide();
            categoryUI = new CategoryUI(buttonsList[1].getText(), mainStage);
        });
        buttonsList[2].setOnMouseClicked(mouseEvent -> {
            mainStage.hide();
            clientUI = new ClientUI(buttonsList[2].getText(), mainStage);
        });
        buttonsList[3].setOnMouseClicked(mouseEvent -> {
            saleUI = new SaleUI(buttonsList[3].getText());
            mainStage.setIconified(true);
        });
    }
}
