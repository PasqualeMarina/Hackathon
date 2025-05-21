package gui;

import controller.Controller;
import model.*;

import javax.swing.*;

public class Registrati extends JFrame{
    private JPanel registratiPanel;
    private JTextField nomeField1;
    private JTextField cognomeField1;
    private JTextField ssnField1;
    private JLabel ruoloField1;
    private JTextField emailField1;
    private JPasswordField passwordField1;
    private JRadioButton partecipanteRadioButton;
    private JRadioButton giudiceRadioButton;
    private JRadioButton organizzatoreRadioButton;
    private JButton accediButton;
    private JButton creaAccountButton;

    public Registrati(Controller controller) {
        setTitle("Registrazione");
        setResizable(false);
        setContentPane(registratiPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // chiude solo questa finestra
        setSize(600, 300);
        setVisible(true);

        accediButton.addActionListener(e -> {
            dispose();
            new Login(controller);
        });

        creaAccountButton.addActionListener(e -> {
            if(emailField1.getText().length() <= 0 || passwordField1.getPassword().length==0)
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
