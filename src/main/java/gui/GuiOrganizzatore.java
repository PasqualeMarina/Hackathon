package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GuiOrganizzatore extends JFrame{
    static final String ERRORE = "ERRORE";
    static final String SELEZIONA = "SELEZIONA evento";
    
    private JPanel homePanel;
    private JLabel titoloLabel;
    private JPanel creaEventoPanel;
    private JPanel titoloPanel;
    private JPanel eventiCreatiPanel;
    private JLabel benvenutoTextField;
    private JLabel emailLabel;
    private JTextField titoloTextField1;
    private JTextField sedeTextField2;
    private JComboBox meseComboBox1;
    private JComboBox giornoComboBox2;
    private JComboBox annoComboBox3;
    private JSpinner durataSpinner1;
    private JSpinner maxPerTeamSpinner2;
    private JSpinner maxIscrittiSpinner3;
    private JButton creaButton;
    private JTextArea eventiCreatiTextArea1;
    private JScrollPane eventiCreatiJScrollPane;
    private JTextField emailTextField1;
    private JButton invitaButton;
    private JComboBox eventoInvitoComboBox1;
    private JButton esciButton;

    public GuiOrganizzatore(Controller controller, Organizzatore organizzatore){
        setTitle("Home Organizzatore");
        setResizable(false);
        setContentPane(homePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(800, 400);
        setVisible(true);

        durataSpinner1.setValue(2);
        eventiCreatiTextArea1.setEditable(false);
        eventoInvitoComboBox1.addItem(SELEZIONA);

        //modifica le politiche di scorrimento del JScrollPanel degli eventi creati
        eventiCreatiJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        eventiCreatiJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //controlla se esistono già eventi creati e aggiorna l'area eventi creati e la combo box
        aggiornaPagina(controller, organizzatore);

        invitaButton.addActionListener(e->{
            if(emailTextField1.getText().isBlank())
                JOptionPane.showMessageDialog(this, "Inserisci la mail del giudice da invitare!", ERRORE, JOptionPane.WARNING_MESSAGE);
            else if(eventoInvitoComboBox1.getSelectedItem().equals(SELEZIONA))
                JOptionPane.showMessageDialog(this, "SELEZIONA un evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
            else
            {
                String email = emailTextField1.getText();
                boolean inviato = false;
                for(Utente u : controller.getUtenti()){
                    if(u.getEmail().equals(email) && u instanceof Giudice){
                        inviato = true;
                        if(controller.inviaInvito( (Giudice) u, (Hackathon)eventoInvitoComboBox1.getSelectedItem()))
                            JOptionPane.showMessageDialog(this, "Invito inviato con successo!", "Invito", JOptionPane.INFORMATION_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(this, "Hai già invitato questo giudice per questo evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
                    }
                }
                if(!inviato)
                    JOptionPane.showMessageDialog(this, "Non esiste un giudice con questa mail!", ERRORE, JOptionPane.WARNING_MESSAGE);

            }
        });

        //ActionListener che crea un evento
        creaButton.addActionListener(e->{
           String sede = sedeTextField2.getText();
           String titolo = titoloTextField1.getText();
           int giorno = Integer.parseInt(giornoComboBox2.getSelectedItem().toString());
           int mese = Integer.parseInt(meseComboBox1.getSelectedItem().toString());
           int anno = Integer.parseInt(annoComboBox3.getSelectedItem().toString());
           int durata = (Integer)durataSpinner1.getValue();
           int maxIscritti = (Integer)maxIscrittiSpinner3.getValue();
           int maxperTeam = (Integer)maxPerTeamSpinner2.getValue();

           //funzione che controlla la validità di tutti i parametri per la creazione di un evento e l'unicità del titolo
           if(!controller.controllaNome(titolo))
               JOptionPane.showMessageDialog(this, "Esiste già un evento con questo nome!", "Evento", JOptionPane.INFORMATION_MESSAGE);
            else if(eventoValido(sede, titolo, giorno, mese, anno, durata, maxIscritti, maxperTeam)){
               Hackathon evento = new Hackathon(sede, titolo, LocalDate.of(anno, mese, giorno), durata, organizzatore, maxIscritti, maxperTeam);
               controller.aggiungiEvento(evento, organizzatore);
               aggiornaPagina(controller, organizzatore);
               JOptionPane.showMessageDialog(this, "Evento creato con successo!", "Evento", JOptionPane.INFORMATION_MESSAGE);
           }
        });

        esciButton.addActionListener(e->{
            dispose();
            new Login(controller);
        });

        benvenutoTextField.setText("Benvenuto " + organizzatore.getNome() + " " + organizzatore.getCognome() + "!");
        emailLabel.setText("Email: " + organizzatore.getEmail());
    }

    public boolean eventoValido(String sede, String titolo, int giorno, int mese, int anno, int durata, int maxIscritti, int maxPerTeam){
        //controllo della validità del titolo
        if(titolo.isBlank()) {
            JOptionPane.showMessageDialog(this, "Inserisci un titolo per l'evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        //controllo della validità della sede
        if(sede.isBlank()){
            JOptionPane.showMessageDialog(this, "Inserisci una sede per l'evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try{
            LocalDate.of(anno, mese, giorno);
        }
        catch(DateTimeException e) {
            JOptionPane.showMessageDialog(this, "Inserisci una data valida!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        //controllo validità della durata
        if(durata < 1){
            JOptionPane.showMessageDialog(this, "Inserisci una durata maggiore di 0!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }

        //controllo validità limite membri per team
        if(maxPerTeam < 2){
            JOptionPane.showMessageDialog(this, "Inserisci un numero di membri per team maggiore di 1!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }

        //controllo validità limite massimo iscritti
        if(maxIscritti < 3){
            JOptionPane.showMessageDialog(this, "Inserisci un numero di iscritti massimo maggiore di 2", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public void aggiornaEventiCreati(Controller controller, Organizzatore organizzatore){
        int eventiCreati = 0;
        String result = "";

        eventiCreatiTextArea1.setText("Non hai creato alcun evento");
        ArrayList<Hackathon> eventi = controller.getEventi();
        if(!eventi.isEmpty()){
            for (Hackathon e : eventi){
                if(e.getOrganizzatore().equals(organizzatore)) {
                    eventiCreati++;
                    result += ("\n" + controller.getInfo(e) + "\n---------------------------------------");
                }
            }

            if(eventiCreati == 0)
                eventiCreatiTextArea1.setText("Non hai creato alcun evento");
            else if(eventiCreati == 1){
                eventiCreatiTextArea1.setText("Hai creato " + eventiCreati + " evento: ");
                eventiCreatiTextArea1.append(result);
            }
            else{
                eventiCreatiTextArea1.setText("Hai creato " + eventiCreati + " eventi: ");
                eventiCreatiTextArea1.append(result);
            }
        }
    }

    void riempiComboboxEvento(Controller controller, Organizzatore organizzatore) {
        ArrayList<Hackathon> eventi = controller.getEventi();
        if (!eventi.isEmpty()) {
            for (Hackathon e : eventi) {
                if (e.getOrganizzatore().equals(organizzatore)) {
                    eventoInvitoComboBox1.addItem(e); // aggiungi l'oggetto Hackathon
                }
            }
        }
    }

    void aggiornaPagina(Controller controller, Organizzatore organizzatore){
        eventoInvitoComboBox1.removeAllItems();
        eventoInvitoComboBox1.addItem(SELEZIONA);
        riempiComboboxEvento(controller, organizzatore);
        aggiornaEventiCreati(controller, organizzatore);
    }
}
