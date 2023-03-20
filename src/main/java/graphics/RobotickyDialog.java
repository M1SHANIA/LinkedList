package graphics;

import Model.ProcesRoboticky;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class RobotickyDialog extends ProcesDialog{

    private ProcesRoboticky procesRoboticky;

    public RobotickyDialog(ProcesRoboticky procesRoboticky, String nazev) {
        super(procesRoboticky, nazev);
    }

    @Override
    public boolean obnov() {
        if(super.obnov()) {
                try {
                    procesRoboticky = new ProcesRoboticky(
                            "R"+Integer.parseInt(finalProces.getId()),
                            finalProces.getCas()
                    );
                    return true;
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING, "Zadejte cisla", ButtonType.OK).show();
                    return false;
                }
            }
        return false;
    }

    @Override
    protected ProcesRoboticky vratit() {
        return procesRoboticky;
    }
}
