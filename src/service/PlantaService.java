// Nivelul logic: Service
package service;

import domain.PlantaMedicinala;
import repository.PlantaRepository;

import java.util.Date;
import java.util.List;

public class PlantaService {
    private final PlantaRepository repository;

    // Flag-uri pentru direcția sortării
    private boolean sortByNameAscending = true;
    private boolean sortByPriceAscending = true;
    private boolean sortByQuantityAscending = true;
    private boolean sortByDateAscending = true;

    public PlantaService(PlantaRepository repository) {
        this.repository = repository;
    }

    // Metodă pentru a obține toate plantele
    public List<PlantaMedicinala> getAllPlante() {
        return repository.readPlante();
    }

    // Metodă pentru a adăuga o plantă nouă
    public void addPlanta(String denumire, int cantitate, double pret) {
        // Transformăm numele să aibă prima literă mare
        String denumireFormata = denumire.substring(0, 1).toUpperCase() + denumire.substring(1).toLowerCase();

        List<PlantaMedicinala> plante = repository.readPlante();
        plante.add(new PlantaMedicinala(denumireFormata, cantitate, pret));
        repository.writePlante(plante);
    }

    // Metodă pentru a cumpăra o plantă
    public void cumparaPlanta(String denumire, int cantitateCeruta, String fileVanzari) throws IllegalArgumentException {
        List<PlantaMedicinala> plante = repository.readPlante();
        for (PlantaMedicinala planta : plante) {
            if (planta.getDenumire().equalsIgnoreCase(denumire)) {
                if (planta.getCantitate() >= cantitateCeruta) {
                    planta.setCantitate(planta.getCantitate() - cantitateCeruta);
                    repository.writePlante(plante);
                    repository.logVanzare(fileVanzari, planta, cantitateCeruta);
                    return;
                } else {
                    throw new IllegalArgumentException("Cantitate insuficientă pentru planta: " + denumire);
                }
            }
        }
        throw new IllegalArgumentException("Planta cu denumirea " + denumire + " nu a fost găsită.");
    }

    public void removePlanta(String denumire) {
        List<PlantaMedicinala> plante = repository.readPlante(); // Citim lista de plante din fișier
        PlantaMedicinala plantaDeEliminat = null;

        for (PlantaMedicinala planta : plante) {
            if (planta.getDenumire().equalsIgnoreCase(denumire)) {
                plantaDeEliminat = planta;
                break;
            }
        }

        if (plantaDeEliminat != null) {
            plante.remove(plantaDeEliminat); // Eliminăm planta din listă
            repository.writePlante(plante); // Salvăm lista actualizată în fișier
        }
    }

    // Sortare după nume
    public List<PlantaMedicinala> sortByName() {
        List<PlantaMedicinala> plante = getAllPlante();

        if (sortByNameAscending) {
            plante.sort((p1, p2) -> p1.getDenumire().compareToIgnoreCase(p2.getDenumire()));
        } else {
            plante.sort((p1, p2) -> p2.getDenumire().compareToIgnoreCase(p1.getDenumire()));
        }

        sortByNameAscending = !sortByNameAscending; // Schimbăm direcția pentru următoarea sortare
        return plante;
    }

    // Sortare după preț
    public List<PlantaMedicinala> sortByPrice() {
        List<PlantaMedicinala> plante = getAllPlante();

        if (sortByPriceAscending) {
            plante.sort((p1, p2) -> Double.compare(p1.getPret(), p2.getPret()));
        } else {
            plante.sort((p1, p2) -> Double.compare(p2.getPret(), p1.getPret()));
        }

        sortByPriceAscending = !sortByPriceAscending; // Schimbăm direcția pentru următoarea sortare
        return plante;
    }

    // Sortare după cantitate
    public List<PlantaMedicinala> sortByQuantity() {
        List<PlantaMedicinala> plante = getAllPlante();

        if (sortByQuantityAscending) {
            plante.sort((p1, p2) -> Integer.compare(p1.getCantitate(), p2.getCantitate()));
        } else {
            plante.sort((p1, p2) -> Integer.compare(p2.getCantitate(), p1.getCantitate()));
        }

        sortByQuantityAscending = !sortByQuantityAscending; // Schimbăm direcția pentru următoarea sortare
        return plante;
    }

    // Sortare după data înregistrării
    public List<PlantaMedicinala> sortByDate() {
        List<PlantaMedicinala> plante = getAllPlante();

        if (sortByDateAscending) {
            plante.sort((p1, p2) -> p1.getDataInregistrare().compareTo(p2.getDataInregistrare()));
        } else {
            plante.sort((p1, p2) -> p2.getDataInregistrare().compareTo(p1.getDataInregistrare()));
        }

        sortByDateAscending = !sortByDateAscending; // Schimbăm direcția pentru următoarea sortare
        return plante;
    }
}