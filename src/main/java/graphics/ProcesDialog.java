package graphics;

import Model.Proces;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public abstract class ProcesDialog extends Stage {
    private TextField id;
    private TextField cas;
    private final Proces proces;
    protected Proces finalProces;
    protected int pocet;
    private GridPane gridPane;

    public ProcesDialog(Proces proces, String nazev) {
        this.proces = proces;
        this.pocet=0;
        this.finalProces = proces;
        setTitle(nazev);
        setResizable(false);
        setScene(start());
    }
    public Scene start(){

        VBox vBox = new VBox(12);
        vBox.setAlignment(Pos.CENTER);
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(7);
        gridPane.setVgap(20);
        vBox.getChildren().addAll(gridPane);
        id = addRow("Id:", pocet++, "");
        cas = addRow("Cas:", pocet++, "0");
        Button ok = new Button("OK");
        Button cancel = new Button("CANCEL");
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setPrefHeight(50);
        hBox.setSpacing(12);
        cancel.setPrefSize(75,25);
        ok.setPrefSize(75,25);
        hBox.getChildren().addAll(ok,cancel);
        cancel.setOnAction(actionEvent -> hide());
        ok.setOnAction(actionEvent -> {
            if(obnov())hide();
        });
        vBox.getChildren().add(hBox);
        return new Scene(vBox);
    }

    protected TextField addRow(String nazev, int radek,String atribut) {
        gridPane.add(new Label(nazev), 0, radek);
        TextField textField = new TextField(atribut);
        textField.setEditable(true);
        gridPane.add(textField, 1, radek);
        return textField;
    }

    protected boolean obnov() {
        if(!(id.getText().equals("") ||  cas.getText().equals(""))){
            try {
                finalProces.setCas(Integer.parseInt(cas.getText()));
                finalProces.setId(id.getText());
                return true;
            } catch (NumberFormatException e){
                new Alert(Alert.AlertType.WARNING,"Zadejte cislo", ButtonType.OK).show();
                return false;
            }

        }
        else {
            new Alert(Alert.AlertType.WARNING,"Vyplňte všechna pole", ButtonType.OK).show();
            return false;
        }
    }

    protected abstract Proces vratit();
}
