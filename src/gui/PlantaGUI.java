// Nivelul de interfață grafică: GUI
package gui;

import domain.PlantaMedicinala;
import repository.PlantaRepository;
import service.PlantaService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PlantaGUI {
    private final PlantaService service;
    private JFrame frame;
    private JTable table;
    private JTextField denumireField, cantitateField, pretField, cumparaDenumireField, cumparaCantitateField;

    public PlantaGUI(PlantaService service) {
        this.service = service;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Magazin Plafar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Tabel pentru afișarea plantelor
        table = new JTable();
        updateTable(service.getAllPlante()); // Actualizăm tabelul cu lista curentă de plante
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panou pentru adăugare
        JPanel addPanel = new JPanel(new GridLayout(4, 2));
        addPanel.add(new JLabel("Denumire:"));
        denumireField = new JTextField();
        addPanel.add(denumireField);

        addPanel.add(new JLabel("Cantitate:"));
        cantitateField = new JTextField();
        addPanel.add(cantitateField);

        addPanel.add(new JLabel("Preț:"));
        pretField = new JTextField();
        addPanel.add(pretField);

        JButton addButton = new JButton("Adaugă");
        addButton.addActionListener(_ -> {
            addPlanta();
            updateTable(service.getAllPlante()); // Actualizăm tabelul după adăugare
        });
        addPanel.add(addButton);

        panel.add(addPanel, BorderLayout.NORTH);

        // Panou pentru cumpărare
        JPanel buyPanel = new JPanel(new GridLayout(3, 2));
        buyPanel.add(new JLabel("Denumire (cumpărare):"));
        cumparaDenumireField = new JTextField();
        buyPanel.add(cumparaDenumireField);

        buyPanel.add(new JLabel("Cantitate:"));
        cumparaCantitateField = new JTextField();
        buyPanel.add(cumparaCantitateField);

        JButton buyButton = new JButton("Cumpără");
        buyButton.addActionListener(_ -> {
            cumparaPlanta();
            updateTable(service.getAllPlante()); // Actualizăm tabelul după cumpărare
        });
        buyPanel.add(buyButton);

        panel.add(buyPanel, BorderLayout.SOUTH);

        // Panou pentru sortare
        JPanel sortPanel = new JPanel(new GridLayout(2, 2));

        JButton sortByNameButton = new JButton("Sortează după Nume");
        sortByNameButton.addActionListener(_ -> updateTable(service.sortByName()));
        sortPanel.add(sortByNameButton);

        JButton sortByPriceButton = new JButton("Sortează după Preț");
        sortByPriceButton.addActionListener(_ -> updateTable(service.sortByPrice()));
        sortPanel.add(sortByPriceButton);

        JButton sortByQuantityButton = new JButton("Sortează după Cantitate");
        sortByQuantityButton.addActionListener(_ -> updateTable(service.sortByQuantity()));
        sortPanel.add(sortByQuantityButton);

        JButton sortByDateButton = new JButton("Sortează după Dată");
        sortByDateButton.addActionListener(_ -> updateTable(service.sortByDate()));
        sortPanel.add(sortByDateButton);

        panel.add(sortPanel, BorderLayout.WEST);

        frame.add(panel);
        frame.setVisible(true);
    }

    public void updateTable(List<PlantaMedicinala> plante) {
        String[] columnNames = {"Denumire", "Cantitate", "Preț", "Data"};
        Object[][] data = new Object[plante.size()][4];

        // Pregătim datele pentru tabel
        for (int i = 0; i < plante.size(); i++) {
            PlantaMedicinala planta = plante.get(i);
            data[i][0] = planta.getDenumire();
            data[i][1] = planta.getCantitate();
            data[i][2] = planta.getPret();
            data[i][3] = planta.getDataInregistrare();
        }

        // Creăm un model nou pentru tabel
        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void addPlanta() {
        try {
            String denumire = capitalizeFirstLetter(denumireField.getText());
            int cantitate = Integer.parseInt(cantitateField.getText());
            double pret = Double.parseDouble(pretField.getText());


            // Adăugăm planta în serviciu
            service.addPlanta(denumire, cantitate, pret);

            // Actualizăm tabelul
            updateTable(service.getAllPlante());

            JOptionPane.showMessageDialog(frame, "Planta a fost adăugată cu succes!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Datele introduse sunt invalide!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void cumparaPlanta() {
        String denumire = capitalizeFirstLetter(cumparaDenumireField.getText());
        String cantitateText = cumparaCantitateField.getText();

        if (denumire.isEmpty() || cantitateText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Introduceți atât denumirea plantei, cât și cantitatea!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantitate = Integer.parseInt(cantitateText);

            // Găsim planta și calculăm prețul
            List<PlantaMedicinala> plante = service.getAllPlante();
            PlantaMedicinala plantaGasita = null;

            for (PlantaMedicinala planta : plante) {
                if (planta.getDenumire().equalsIgnoreCase(denumire)) {
                    plantaGasita = planta;
                    break;
                }
            }

            if (plantaGasita == null) {
                JOptionPane.showMessageDialog(frame, "Planta cu denumirea introdusă nu există!", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (plantaGasita.getCantitate() < cantitate) {
                JOptionPane.showMessageDialog(frame, "Cantitate insuficientă în stoc pentru planta selectată!", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double pretTotal = cantitate * plantaGasita.getPret();

            // Afișăm prețul total și cerem confirmarea
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Preț total: " + pretTotal + " lei.\nDoriți să continuați cu cumpărarea?",
                    "Confirmare Cumpărare",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Finalizăm cumpărarea
                service.cumparaPlanta(denumire, cantitate, "PlanteVandute.txt");
                JOptionPane.showMessageDialog(frame, "Cumpărarea a fost realizată cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

                // Dacă planta rămâne fără stoc, o eliminăm din listă
                if (plantaGasita.getCantitate() - cantitate <= 0) {
                    service.removePlanta(denumire); // Metodă de eliminare din serviciu
                }
                updateTable(service.getAllPlante());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Introduceți o cantitate validă (număr întreg)!", "Eroare", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Fereastra de autentificare
        LoginGUI loginGUI = new LoginGUI();

        // Așteaptă autentificarea
        while (!loginGUI.isAuthenticated()) {
            try {
                Thread.sleep(100); // Așteaptă până se autentifică utilizatorul
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // După autentificare, lansează aplicația principală
        PlantaRepository repository = new PlantaRepository("PlanteDisponibile.txt");
        PlantaService service = new PlantaService(repository);
        new PlantaGUI(service);
    }
}
