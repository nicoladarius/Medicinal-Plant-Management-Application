// Nivelul de domeniu: PlantaMedicinala
package domain;

import java.time.LocalDate;

public class PlantaMedicinala {
    private final String denumire;
    private int cantitate;
    private final double pret;
    private LocalDate dataInregistrare; // Atribut pentru data înregistrării

    // Constructor care setează automat data curentă
    public PlantaMedicinala(String denumire, int cantitate, double pret) {
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.pret = pret;
        this.dataInregistrare = LocalDate.now(); // Data curentă
    }

    // Constructor care primește explicit data înregistrării
    public PlantaMedicinala(String denumire, int cantitate, double pret, LocalDate dataInregistrare) {
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.pret = pret;
        this.dataInregistrare = dataInregistrare; // Data specificată
    }

    // Getteri și setteri
    public String getDenumire() {
        return denumire;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public double getPret() {
        return pret;
    }

    public LocalDate getDataInregistrare() {
        return dataInregistrare;
    }

    public void setDataInregistrare(LocalDate dataInregistrare) {
        this.dataInregistrare = dataInregistrare;
    }

    @Override
    public String toString() {
        return denumire + ", cantitate: " + cantitate + ", pret: " + pret + ", data inregistrare: " + dataInregistrare;
    }
}
