package graphics;

import Model.ProcesManualni;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public final class ManualniDialog extends ProcesDialog{

    private final TextField pocetOsob;
    private ProcesManualni procesManualni;

    public ManualniDialog(ProcesManualni procesManualni, String nazev) {
        super(procesManualni, nazev);
        pocetOsob = addRow("Pocet lide: ", pocet++, "0");
    }

    @Override
    public boolean obnov() {
        if(super.obnov()) {
            if (!(pocetOsob.getText().equals(""))) {
                try {
                    procesManualni = new ProcesManualni(
                            "O"+Integer.parseInt(finalProces.getId()),
                            Integer.parseInt(pocetOsob.getText()),
                            finalProces.getCas()
                    );
                    return true;
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING, "Zadejte cisla", ButtonType.OK).show();
                    return false;
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Vyplňte všechna pole", ButtonType.OK).show();
            }

        }
        return false;
    }

    @Override
    protected ProcesManualni vratit() {
        return procesManualni;
    }
}
