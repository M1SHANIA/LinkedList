package graphics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ControlPanelVbox extends VBox {

    public ControlPanelVbox() {
        setSpacing(25);

        alignmentProperty();
        setAlignment(Pos.CENTER);
        setPrefWidth(100);

    }
    public void newField(String nazev,EventHandler<ActionEvent> handler){
        TextField field = new TextField();
        field.setPromptText(nazev);
        field.setPrefSize(80,25);
        field.setOnAction(handler);
        getChildren().add(field);
    }

    public void newButton(String nazev, EventHandler<ActionEvent> handler) {
        Button btn = new Button(nazev);
        btn.setPrefSize(80,25);
        btn.setOnAction(handler);
        getChildren().add(btn);
    }
    public void newLabel(String nazev){
        Label label = new Label(nazev);
        getChildren().add(label);


    }
}
