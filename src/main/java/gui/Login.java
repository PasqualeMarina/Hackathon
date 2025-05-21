package gui;

import controller.Controller;
import model.*;

import javax.swing.*;

public class Login extends JFrame{
    private JPanel loginPanel;
    private JPasswordField passwordField1;
    private JTextField emailField1;
    private JButton registratiButton;
    private JButton accediButton;

    public static void main(String[] args) {
        Controller controller = new Controller();
        new Login(controller);
    }

    public Login(Controller controller) {
        setTitle("Login");
        setResizable(false); // impedisce il ridimensionamento
        setContentPane(loginPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(550, 175); // imposta dimensione fissa
        setVisible(true);

        // Add action listeners or other initialization code here

        accediButton.addActionListener(e -> {
            if(emailField1.getText().isBlank() || passwordField1.getPassword().length==0){
                JOptionPane.showMessageDialog(this, "Riempi i campi obbligatori!", "Errore", JOptionPane.WARNING_MESSAGE);
            }
            else {
                Utente utente = controller.verificaCredenziali(emailField1.getText(), new String(passwordField1.getPassword()));
                if(utente == null) {
                    JOptionPane.showMessageDialog(this, "Email o password non corretti!", "Errore", JOptionPane.WARNING_MESSAGE);
                }
                else{
                    String ruolo = utente.getRuolo();
                    if(utente instanceof Organizzatore && ruolo.equals("Organizzatore")){
                        dispose();
                        new GuiOrganizzatore(controller, (Organizzatore)utente);
                    }
                    else if(utente instanceof Giudice && ruolo.equals("Giudice")){
                        dispose();
                        new GuiGiudice(controller, (Giudice)utente);
                    }
                    else if(utente instanceof Partecipante && ruolo.equals("Partecipante")){
                        dispose();
                        new GuiPartecipante(controller, (Partecipante)utente);
                    }
                }
            }
        });

        registratiButton.addActionListener(e -> {
            dispose();
            new Registrati(controller);
        });
    }
}
