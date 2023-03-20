package VyrobniProces;

import Model.Proces;
import Zasobnik.IAbstrLifo;
import typy.enumPozice;
import typy.enumReorg;

import java.util.Iterator;

public interface IVyrobniProces {

    int importDat(String soubor);

    void exportDat(String soubor);

    void vlozProces(Proces proces, enumPozice pozice);

    Proces zpristupniProces(enumPozice pozice);

    Proces odeberProces(enumPozice pozice);

    Iterator<Proces> iterator();

    IAbstrLifo<Proces> vytipujKandidatiReorg(int cas, enumReorg reorgan);

    void reorganizace(enumReorg reorgan, IAbstrLifo<Proces> zasobnik);

    void zrus();
}
