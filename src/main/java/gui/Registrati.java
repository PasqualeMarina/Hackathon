package gui;

import controller.Controller;
import model.*;

import javax.swing.*;

/**
 * Classe che implementa l'interfaccia grafica per la registrazione degli utenti.
 * Permette la creazione di nuovi account per:
 * - Organizzatori
 * - Giudici
 * - Partecipanti
 * <p>
 * La form richiede l'inserimento di:
 * - Nome
 * - Cognome
 * - Codice fiscale
 * - Email
 * - Password
 * - Selezione del ruolo
 */
public class Registrati extends JFrame{
    private JPanel registratiPanel;
    private JTextField nomeField1;
    private JTextField cognomeField1;
    private JTextField ssnField1;
    private JTextField emailField1;
    private JPasswordField passwordField1;
    private JRadioButton partecipanteRadioButton;
    private JRadioButton giudiceRadioButton;
    private JRadioButton organizzatoreRadioButton;
    private JButton accediButton;
    private JButton creaAccountButton;
    private JLabel ruoloField1;

    public Registrati(Controller controller) {
        setTitle("Registrazione");
        setResizable(false);
        setContentPane(registratiPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // chiude solo questa finestra
        setSize(600, 300);
        setVisible(true);
        ruoloField1.setText("Seleziona un ruolo");

        accediButton.addActionListener(e -> {
            dispose();
            new Login(controller);
        });

        creaAccountButton.addActionListener(e -> {
            if(emailField1.getText().isEmpty() || passwordField1.getPassword().length==0)
                JOptionPane.showMessageDialog(this, "Riempi i campi obbligatori!", "Errore", JOptionPane.WARNING_MESSAGE);
            else{
                if(organizzatoreRadioButton.isSelected()){
                    Organizzatore organizzatore = new Organizzatore(ssnField1.getText(), nomeField1.getText(), cognomeField1.getText(), emailField1.getText(), new String(passwordField1.getPassword()), "Organizzatore");
                    controller.aggiungiUtente(organizzatore);
                    dispose();
                    new GuiOrganizzatore(controller, organizzatore);
                }
                else if(giudiceRadioButton.isSelected()){
                    Giudice giudice = new Giudice(ssnField1.getText(), nomeField1.getText(), cognomeField1.getText(), emailField1.getText(), new String(passwordField1.getPassword()), "Giudice");
                    controller.aggiungiUtente(giudice);
                    dispose();
                    new GuiGiudice(controller, giudice);
                }
                else if(partecipanteRadioButton.isSelected()){
                    Partecipante partecipante = new Partecipante(ssnField1.getText(), nomeField1.getText(), cognomeField1.getText(), emailField1.getText(), new String(passwordField1.getPassword()), "Partecipante");
                    controller.aggiungiUtente(partecipante);
                    dispose();
                    new GuiPartecipante(controller, partecipante);
                }
            }
        });
    }
}
