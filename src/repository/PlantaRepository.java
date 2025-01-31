package repository;

import domain.PlantaMedicinala;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlantaRepository {
    private final String filePath;

    public PlantaRepository(String filePath) {
        this.filePath = filePath;
    }

    // Metodă pentru a citi plantele din fișier
    public List<PlantaMedicinala> readPlante() {
        List<PlantaMedicinala> plante = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { // Verificăm dacă sunt 4 părți în linie
                    String denumire = parts[0].trim();
                    int cantitate = Integer.parseInt(parts[1].trim());
                    double pret = Double.parseDouble(parts[2].trim());
                    LocalDate dataInregistrare = LocalDate.parse(parts[3].trim()); // Citim data
                    plante.add(new PlantaMedicinala(denumire, cantitate, pret, dataInregistrare));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plante;
    }

    // Metodă pentru a salva plantele în fișier
    public void writePlante(List<PlantaMedicinala> plante) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (PlantaMedicinala planta : plante) {
                // Scriem toate atributele, inclusiv data înregistrării
                bw.write(planta.getDenumire() + "," + planta.getCantitate() + "," + planta.getPret() + "," + planta.getDataInregistrare());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodă pentru a înregistra o vânzare
    public void logVanzare(String fileVanzari, PlantaMedicinala planta, int cantitateVanduta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileVanzari, true))) {
            // Adăugăm data vânzării pentru urmărire
            LocalDate dataVanzare = LocalDate.now();
            bw.write("Produs vândut: " + planta.getDenumire() +
                    ", Cantitate: " + cantitateVanduta +
                    ", Data vânzării: " + dataVanzare);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
