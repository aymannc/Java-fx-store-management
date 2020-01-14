package bank;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
        Label l = new Label("Card number ");
        TextField tf = new TextField();

        Label l2 = new Label("Experation Date ");
        HBox holder = new HBox(5);
        TextField tf2 = new TextField();
        Label l3 = new Label(" / ");
        TextField tf3 = new TextField();
        holder.getChildren().addAll(tf2, l3, tf3);
        holder.setAlignment(Pos.CENTER);
        VBox vb = new VBox(15);
        VBox vb2 = new VBox(15);
        vb.getChildren().addAll(l2, holder);
        vb.setAlignment(Pos.CENTER);
        Label l4 = new Label("CVC ");
        TextField tf4 = new TextField();

        vb2.getChildren().addAll(l4, tf4);
        vb2.setAlignment(Pos.CENTER);
        Button submit = new Button("Pay");

        submit.setOnAction(e -> {
            data = tf.getText();
            stage.close();
        });
        HBox holder2 = new HBox(10);
        holder2.getChildren().addAll(vb, vb2);
        root.getChildren().addAll(l, tf, holder2, submit);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public String getData() {
        return data;
    }
}
