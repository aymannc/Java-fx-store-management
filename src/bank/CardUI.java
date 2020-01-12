package bank;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardUI {

    private String data;

    public void showStage() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(15);
        root.setMinWidth(200);
        root.setMinHeight(100);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        Label l = new Label("Ajouter le numero de cart ");
        TextField tf = new TextField();
        Button submit = new Button("Chercher");

        submit.setOnAction(e -> {
            data = tf.getText();
            stage.close();
        });

        root.getChildren().addAll(l, tf, submit);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public String getData() {
        return data;
    }
}
