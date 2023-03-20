package VyrobniProces;

import DoubleList.AbstrDoubleList;
import DoubleList.LinkedListException;
import DoubleList.IAbstrDoubleList;
import Model.Proces;
import Model.ProcesManualni;
import Model.ProcesRoboticky;
import Zasobnik.AbstrLifo;
import Zasobnik.IAbstrLifo;
import typy.enumPozice;
import typy.enumReorg;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

public class VyrobniProces implements IVyrobniProces{
    private final IAbstrDoubleList<Proces> linkedList;

    public VyrobniProces() {
        linkedList = new AbstrDoubleList<>();
    }
    @Override
    public int importDat(String soubor) {
        int pocet = 0;
        try {
            Scanner scanner = new Scanner(new File(soubor));
            scanner.next();
            while (scanner.hasNext()) {
                String[] result = scanner.next().split(";");
                Proces proces;
                if (result[0].startsWith("R")) proces = new ProcesRoboticky(result[0], Integer.parseInt(result[2]));
                else proces = new ProcesManualni(result[0], Integer.parseInt(result[1]), Integer.parseInt(result[2]));
                pocet++;
                linkedList.vlozPosledni(proces);
            }
            } catch (FileNotFoundException e) {
                chyba("File was not found");
            }
        return pocet;
    }

    @Override
    public void exportDat(String soubor) {
        File file = new File(soubor);
        try(PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write("IdProc;persons;time\n");
            for (Proces proces : linkedList) {
                String result=proces.getId();
                if(proces.getId().startsWith("O")) result+=";"+((ProcesManualni)proces).getPocetLide();
                else result+=";0";
                result+=";"+proces.getCas();
                printWriter.write(result+"\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
    }


    @Override
    public void vlozProces(Proces proces, enumPozice pozice) {
        try {
            switch (pozice) {
                case PRVNI -> linkedList.vlozPrvni(proces);
                case POSLEDNI -> linkedList.vlozPosledni(proces);
                case NASLEDNIK -> linkedList.vlozNaslednika(proces);
                case PREDCHUDCE -> linkedList.vlozPredchudce(proces);
            }
        }catch (LinkedListException e){
            chyba(e.getMessage());
        }
    }

    @Override
    public Proces zpristupniProces(enumPozice pozice) {
        Proces result = null;
        try {
            switch (pozice) {
                case PRVNI -> result = linkedList.zpristupniPrvni();
                case POSLEDNI -> result = linkedList.zpristupniPosledni();
                case NASLEDNIK -> result = linkedList.zpristupniNaslednika();
                case PREDCHUDCE -> result = linkedList.zpristupniPredchudce();
                case AKTUALNI -> result = linkedList.zpristupniAktualni();
            }
        }catch (LinkedListException e){
            chyba(e.getMessage());
        }
        return result;
    }

    @Override
    public Proces odeberProces(enumPozice pozice) {
        Proces result = null;
        try {
            switch (pozice) {
                case PRVNI -> result = linkedList.odeberPrvni();
                case POSLEDNI -> result = linkedList.odeberPosledni();
                case NASLEDNIK -> result = linkedList.odeberNaslednika();
                case PREDCHUDCE -> result = linkedList.odeberPredchudce();
                case AKTUALNI -> result = linkedList.odeberAktualni();
            }
        }
        catch (LinkedListException e){
            chyba(e.getMessage());
        }
        return result;
    }

    @Override
    public Iterator<Proces> iterator() {
        return linkedList.iterator();
    }

    @Override
    public IAbstrLifo<Proces> vytipujKandidatiReorg(int cas, enumReorg reorgan) {
        switch (reorgan){
            case AGREGACE -> {
                IAbstrLifo<Proces> newList = new AbstrLifo<>();
                for (Proces proces : linkedList) {
                    if (proces.getId().startsWith("O") && proces.getCas()<=cas) {
                        newList.vloz(proces);
                    }
                }
                return newList;
            }
            case DEKOMPOZICE -> {
                IAbstrLifo<Proces> newList = new AbstrLifo<>();
                for (Proces proces : linkedList) {
                    if (proces.getId().startsWith("O") && proces.getCas()>=cas) {
                        newList.vloz(proces);
                    }
                }
                return newList;
            }
            default -> {
                chyba("Neco se stalo xD");
                return null;
            }
        }
    }

    @Override
    public void reorganizace(enumReorg reorgan, IAbstrLifo<Proces> zasobnik) {
        try {
            switch (reorgan) {
                case AGREGACE -> {
                    IAbstrLifo<Proces> dublicate = new AbstrLifo<>();
                    Proces prechudce = null;
                    while (!zasobnik.jePrazdny()) {
                        if (prechudce == null) {
                            prechudce = zasobnik.odeber();
                            if (zasobnik.jePrazdny()) {
                                Proces proces = dublicate.odeber();
                                proces = new ProcesManualni(
                                        prechudce.getId(),
                                        ((ProcesManualni) proces).getPocetLide() + ((ProcesManualni) prechudce).getPocetLide(),
                                        proces.getCas() + prechudce.getCas());
                                dublicate.vloz(proces);
                            }
                        } else {
                            Proces now = zasobnik.odeber();
                            Proces proces = new ProcesManualni(
                                    now.getId(),
                                    ((ProcesManualni) prechudce).getPocetLide() + ((ProcesManualni) now).getPocetLide(),
                                    prechudce.getCas() + now.getCas());
                            dublicate.vloz(proces);
                            prechudce = null;
                        }
                    }
                    linkedList.zrus();
                    while (!dublicate.jePrazdny()) {
                        linkedList.vlozPosledni(dublicate.odeber());
                    }
                }
                case DEKOMPOZICE -> {
                    IAbstrLifo<Proces> lifo = new AbstrLifo<>();
                    while (!zasobnik.jePrazdny()) {
                        Proces proces = zasobnik.odeber();
                        Proces novyProces = new ProcesManualni(proces.getId() + "_novy", round(((ProcesManualni) proces).getPocetLide()),
                                round(proces.getCas()));
                        Proces staryProces = new ProcesManualni(proces.getId() + "_stary", ceil(((ProcesManualni) proces).getPocetLide()),
                                ceil(proces.getCas()));
                        lifo.vloz(novyProces);
                        lifo.vloz(staryProces);
                    }
                    linkedList.zrus();
                    while (!lifo.jePrazdny()) {
                        linkedList.vlozPosledni(lifo.odeber());
                    }
                }
            }
        }catch (LinkedListException e) {
            chyba(e.getMessage());}
    }

    @Override
    public void zrus() {
        linkedList.zrus();
    }
    private void chyba(String message) {
        new Alert(Alert.AlertType.ERROR,message, ButtonType.OK).show();
    }
    private static int round(int cas){
        return (int) (cas/2.0);
    }
    private static int ceil(int cas){
        return (int) (Math.ceil(cas/2.0));
    }

}
