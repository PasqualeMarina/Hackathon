package gui;

import controller.Controller;
import model.*;

import javax.swing.*;

/**
 * Classe che implementa l'interfaccia grafica per il login.
 * Permette all'utente di:
 * - Effettuare l'accesso al sistema
 * - Passare all'interfacccia per registrarsi come nuovo utente
 * - Accedere alle rispettive interfacce in base al ruolo
 */
public class Login extends JFrame {
    private JPanel loginPanel;
    private JPasswordField passwordField1;
    private JTextField emailField1;
    private JButton registratiButton;
    private JButton accediButton;
    private static final String ERRORE = "ERRORE";

    private transient Controller controller;

    public static void main(String[] args) {
        Controller controller = new Controller();
        new Login(controller);
    }

    public Login(Controller controller) {
        this.controller = controller;

        setTitle("Login");
        setResizable(false);
        setContentPane(loginPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(550, 175);
        setVisible(true);

        accediButton.addActionListener(e -> {
            String email = emailField1.getText();
            String password = new String(passwordField1.getPassword());

            if(email.isBlank() || password.isBlank()){
                JOptionPane.showMessageDialog(this, "Riempi i campi obbligatori!", ERRORE, JOptionPane.WARNING_MESSAGE);
                return;
            }

            Utente utente = controller.verificaCredenziali(email, password);
            if(utente == null) {
                JOptionPane.showMessageDialog(this, "Email o password non corretti!", ERRORE, JOptionPane.WARNING_MESSAGE);
            }
            else{
                selezionaRuolo(controller, utente);
            }
        });

        registratiButton.addActionListener(e -> {
            dispose();
            new Registrati(controller);
        });
    }

    private void selezionaRuolo(Controller controller, Utente utente){
        String ruolo = utente.getRuolo();
        if(ruolo.equals("Organizzatore") && utente instanceof Organizzatore organizzatore){
            dispose();
            new GuiOrganizzatore(controller, organizzatore);
        }
        else if(ruolo.equals("Giudice") && utente instanceof Giudice giudice){
            dispose();
            new GuiGiudice(controller, giudice);
        }
        else if(ruolo.equals("Partecipante") && utente instanceof Partecipante partecipante){
            dispose();
            new GuiPartecipante(controller, partecipante);
        } else {
            JOptionPane.showMessageDialog(this, "Ruolo utente non riconosciuto.", ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
