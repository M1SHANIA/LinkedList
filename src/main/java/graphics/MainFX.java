package graphics;


import Model.Proces;
import Model.ProcesManualni;
import Model.ProcesRoboticky;
import VyrobniProces.IVyrobniProces;
import VyrobniProces.VyrobniProces;
import Zasobnik.IAbstrLifo;
import typy.TypyProcesu;
import typy.enumPozice;
import typy.enumReorg;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Optional;

public class MainFX extends Application {

    private IVyrobniProces vyrobniProces;
    private ListView<Proces> langsListView;
    private enumPozice actual;


    public static void main(String[] args) {
        launch(args);
    }


        private ControlPanelVbox comandsV(){
            var controlV = new ControlPanelVbox();
            controlV.newButton("FIRST", nastavPrvni());
            controlV.newButton("NEXT", nastavDalsi());
            controlV.newButton("PREVIOUS", nastavPredchozi());
            controlV.newButton("LAST", nastavPosledni());
            controlV.newButton("DELETE ACTUAL", vyjmi());
            controlV.newButton("AGREGACE", agregace());
            controlV.newButton("DEKOMPOZICE", dekompozice());
            return controlV;
        }


    private EventHandler<ActionEvent> dekompozice() {
        return actionEvent -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("DEKOMPOZICE");
            dialog.setHeaderText("Zadejte cas");
            dialog.setContentText("Cas:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int cas = Integer.parseInt(result.get());
                IAbstrLifo<Proces> procesAbstLifo = vyrobniProces.vytipujKandidatiReorg(cas, enumReorg.DEKOMPOZICE);
                vyrobniProces.reorganizace(enumReorg.DEKOMPOZICE, procesAbstLifo);
                obnovList();
            }
        };
    }

    private EventHandler<ActionEvent> agregace() {
        return actionEvent -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("AGREGACE");
            dialog.setHeaderText("Zadejte cas");
            dialog.setContentText("Cas:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int cas = Integer.parseInt(result.get());
                IAbstrLifo<Proces> procesAbstLifo = vyrobniProces.vytipujKandidatiReorg(cas, enumReorg.AGREGACE);
                vyrobniProces.reorganizace(enumReorg.AGREGACE, procesAbstLifo);
                obnovList();
            }
        };
    }

    private ControlPanelHBox commandsH() {
            var controlH = new ControlPanelHBox();
            controlH.newLabel("");
            controlH.newButton("IMPORT", nacist());
            controlH.newButton("EXPORT", ulozit());
            controlH.newButton("CANCEL", zrusit());
            controlH.newLabel("NEW DATA:");
            controlH.newComboBox("typ",novyA());
            controlH.newComboBoxPozice("position",pozice());
            return controlH;
        }

    private EventHandler<ActionEvent> pozice() {
        return actionEvent -> {
            actual = ((ComboBox<enumPozice>)actionEvent.getSource()).getValue();
        };
    }


    @Override
        public void start(Stage stage) throws Exception {
            actual = enumPozice.PRVNI;
            vyrobniProces = new VyrobniProces();
            FlowPane root = new FlowPane(creatList(),comandsV(),commandsH());
            Scene scene = new Scene(root, 600,570);
            stage.setScene(scene);
            stage.setMaxWidth(600);
            stage.setMaxHeight(600);
            stage.setResizable(false);
            stage.setTitle("Procesy");
            stage.show();
    }


    private EventHandler<ActionEvent> novyA() {
        return event ->{
            TypyProcesu typ = ((ComboBox<TypyProcesu>)event.getSource()).getValue();
            Proces proces = null;
            switch (typ){
                case MANUALNI -> {
                    proces = new ProcesManualni("",0,0);

                }
                case ROBOTICKY -> proces = new ProcesRoboticky("",0);
            }
            proces = dialog(typ,"Dialog novy",proces);
            if(proces!=null) {
                vyrobniProces.vlozProces(proces,actual);
                obnovList(proces);
            }
        };
    }

    private void obnovList() {
        langsListView.getItems().clear();
        Iterator<Proces> iterator = vyrobniProces.iterator();
        while (iterator.hasNext()){
            langsListView.getItems().add(iterator.next());
        }
    }
    private void obnovList(Proces proces) {
        langsListView.getItems().clear();
        Iterator<Proces> iterator = vyrobniProces.iterator();
        while (iterator.hasNext()){
            langsListView.getItems().add(iterator.next());
        }
        langsListView.getSelectionModel().select(proces);
    }

    private EventHandler<ActionEvent> zrusit() {
        return event ->{
            langsListView.getSelectionModel().select(null);
            vyrobniProces.zrus();
            obnovList();
        };

    }

    private EventHandler<ActionEvent> nacist() {
        return actionEvent -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Soubor");
            dialog.setHeaderText("Zadejte nazev souboru");
            dialog.setContentText("Nazev:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int i = vyrobniProces.importDat(result.get());
                if(i>0)obnovList();
                else new Alert(Alert.AlertType.INFORMATION,"Chyba cteni dat",ButtonType.OK).show();
            }
        };

    }
    private EventHandler<ActionEvent> ulozit() {
        return actionEvent -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Soubor");
            dialog.setHeaderText("Zadejte nazev souboru");
            dialog.setContentText("Nazev:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                vyrobniProces.exportDat(result.get());

            }
        };

    }


    private EventHandler<ActionEvent> vyjmi() {
        return event -> {
          vyrobniProces.odeberProces(enumPozice.AKTUALNI);
          obnovList();
        };
    }

    private EventHandler<ActionEvent> nastavPosledni() {
        return event -> {
            Proces proces = vyrobniProces.zpristupniProces(enumPozice.POSLEDNI);
            obnovList(proces);
        };
    }

    private EventHandler<ActionEvent> nastavDalsi() {
        return event ->{
            Proces proces = vyrobniProces.zpristupniProces(enumPozice.NASLEDNIK);
            obnovList(proces);
        };
    }

    private EventHandler<ActionEvent> nastavPredchozi() {
        return event ->{
            Proces proces = vyrobniProces.zpristupniProces(enumPozice.PREDCHUDCE);
            obnovList(proces);
        };
    }

    private EventHandler<ActionEvent> nastavPrvni() {
       return event -> {
           Proces proces = vyrobniProces.zpristupniProces(enumPozice.PRVNI);
           obnovList(proces);
       };
    }


    private ListView<Proces> creatList(){
        ObservableList<Proces> langs = FXCollections.observableArrayList();
        langsListView = new ListView<>(langs);
        langsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        langsListView.setPrefSize(500, 500);
        return langsListView;
    }

    private Proces dialog(TypyProcesu typ,String nazev, Proces proces){
        ProcesDialog procesDialog = null;
        Proces back = null;
        switch (typ){
            case MANUALNI -> procesDialog = new ManualniDialog((ProcesManualni) proces,nazev);
            case ROBOTICKY -> procesDialog = new RobotickyDialog((ProcesRoboticky) proces,nazev);
        }
        Stage stage = procesDialog;
        stage.showAndWait();
        back=procesDialog.vratit();
        return back;
    }

}
