package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {
    private JFrame frame;
    private JTextField userField;
    private JPasswordField passwordField;
    private boolean isAuthenticated = false;

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public LoginGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Autentificare");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        frame.add(new JLabel("Utilizator:"));
        userField = new JTextField();
        frame.add(userField);

        frame.add(new JLabel("Parolă:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        frame.add(loginButton);

        frame.setVisible(true);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String user = userField.getText();
            String password = new String(passwordField.getPassword());

            // Exemplu simplu de autentificare (poți schimba cu o verificare mai complexă)
            if (user.equals("user") && password.equals("parola")) {
                isAuthenticated = true;
                JOptionPane.showMessageDialog(frame, "Autentificare reușită!");
                frame.dispose(); // Închide fereastra de login
            } else {
                JOptionPane.showMessageDialog(frame, "Utilizator sau parolă greșită!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}