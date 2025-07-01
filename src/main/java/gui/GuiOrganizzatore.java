package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe che implementa l'interfaccia grafica per l'organizzatore.
 * Permette all'organizzatore di:
 * - Creare nuovi eventi Hackathon
 * - Visualizzare gli eventi creati
 * - Invitare giudici agli eventi
 * - Gestire il proprio profilo
 */
public class GuiOrganizzatore extends JFrame{
    static final String ERRORE = "ERRORE";
    static final String SELEZIONA = "SELEZIONA evento";
    
    private JPanel homePanel;
    private JLabel benvenutoTextField;
    private JLabel emailLabel;
    private JTextField titoloTextField1;
    private JTextField sedeTextField2;
    private JComboBox<Integer> meseComboBox1;
    private JComboBox<Integer> giornoComboBox2;
    private JComboBox<Integer> annoComboBox3;
    private JSpinner durataSpinner1;
    private JSpinner maxPerTeamSpinner2;
    private JSpinner maxIscrittiSpinner3;
    private JButton creaButton;
    private JTextArea eventiCreatiTextArea1;
    private JScrollPane eventiCreatiJScrollPane;
    private JTextField emailTextField1;
    private JButton invitaButton;
    private JComboBox<Hackathon> eventoInvitoComboBox1;
    private JButton esciButton;
    private JPanel titoloPanel;
    private JPanel creaEventoPanel;
    private JPanel eventiCreatiPanel;

    public GuiOrganizzatore(Controller controller, Organizzatore organizzatore){
        setTitle("Home Organizzatore");
        setResizable(false);
        setContentPane(homePanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(800, 400);
        setVisible(true);
        titoloPanel.setVisible(true);
        creaEventoPanel.setVisible(true);
        eventiCreatiPanel.setVisible(true);

        durataSpinner1.setValue(2);
        eventiCreatiTextArea1.setEditable(false);

        eventiCreatiJScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        eventiCreatiJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        initListeners(controller, organizzatore);

        benvenutoTextField.setText("Benvenuto " + organizzatore.getNome() + " " + organizzatore.getCognome() + "!");
        emailLabel.setText("Email: " + organizzatore.getEmail());

        aggiornaPagina(controller, organizzatore);
    }

    private void initListeners(Controller controller, Organizzatore organizzatore) {
        initInvitaButtonListener(controller);
        initCreaButtonListener(controller, organizzatore);
        initEsciButtonListener(controller);
    }

    private void initInvitaButtonListener(Controller controller) {
        invitaButton.addActionListener(e -> gestisciInvito(controller));
    }

    private void gestisciInvito(Controller controller) {
        String email = emailTextField1.getText();
        Object selectedItem = eventoInvitoComboBox1.getSelectedItem();

        if (email.isBlank()) {
            JOptionPane.showMessageDialog(this, "Inserisci la mail del giudice da invitare!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (SELEZIONA.equals(selectedItem)) {
            JOptionPane.showMessageDialog(this, SELEZIONA, ERRORE, JOptionPane.WARNING_MESSAGE);
            return;
        }

        Giudice giudice = trovaGiudiceDaEmail(email, controller);
        if (giudice == null) {
            JOptionPane.showMessageDialog(this, "Non esiste un giudice con questa mail!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return;
        }

        Hackathon evento = (Hackathon) selectedItem;
        if (controller.inviaInvito(giudice, evento)) {
            JOptionPane.showMessageDialog(this, "Invito inviato con successo!", "Invito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Hai già invitato questo giudice per questo evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
        }
    }

    private Giudice trovaGiudiceDaEmail(String email, Controller controller) {
        for (Utente u : controller.getUtenti()) {
            if (u instanceof Giudice giudice && giudice.getEmail().equals(email)) {
                return giudice;
            }
        }
        return null;
    }


    private void initCreaButtonListener(Controller controller, Organizzatore organizzatore) {
        creaButton.addActionListener(e -> {
            String sede = sedeTextField2.getText();
            String titolo = titoloTextField1.getText();
            int giorno = Integer.parseInt(giornoComboBox2.getSelectedItem().toString());
            int mese = Integer.parseInt(meseComboBox1.getSelectedItem().toString());
            int anno = Integer.parseInt(annoComboBox3.getSelectedItem().toString());
            int durata = (Integer)durataSpinner1.getValue();
            int maxIscritti = (Integer)maxIscrittiSpinner3.getValue();
            int maxperTeam = (Integer)maxPerTeamSpinner2.getValue();

            if(!controller.controllaNome(titolo))
                JOptionPane.showMessageDialog(this, "Esiste già un evento con questo nome!", "Evento", JOptionPane.INFORMATION_MESSAGE);
            else if(eventoValido(titolo, giorno, mese, anno, durata, maxIscritti, maxperTeam)){
                Hackathon evento = new Hackathon(sede, titolo, LocalDate.of(anno, mese, giorno), durata, organizzatore, maxIscritti, maxperTeam);
                controller.aggiungiEvento(evento, organizzatore);
                aggiornaPagina(controller, organizzatore);
                JOptionPane.showMessageDialog(this, "Evento creato con successo!", "Evento", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void initEsciButtonListener(Controller controller) {
        esciButton.addActionListener(e -> {
            dispose();
            new Login(controller);
        });
    }


    private boolean eventoValido(String titolo, int giorno, int mese, int anno, int durata, int maxIscritti, int maxPerTeam){
        //controllo della validità del titolo
        if(titolo.isBlank()) {
            JOptionPane.showMessageDialog(this, "Inserisci un titolo per l'evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            LocalDate data = LocalDate.of(anno, mese, giorno);
            // usa la variabile in modo innocuo per evitare warning
            if (data == null) return false;
        } catch (DateTimeException _) {
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

    private void aggiornaEventiCreati(Controller controller, Organizzatore organizzatore){
        int eventiCreati = 0;
        StringBuilder result = new StringBuilder();

        eventiCreatiTextArea1.setText("Non hai creato alcun evento");
        List<Hackathon> eventi = controller.getEventi();
        if(!eventi.isEmpty()){
            for (Hackathon e : eventi){
                if(e.getOrganizzatore().equals(organizzatore)) {
                    eventiCreati++;
                    result.append("\n").append(controller.getInfo(e)).append("\n---------------------------------------");
                }
            }

            switch(eventiCreati){
                case 0:
                    eventiCreatiTextArea1.setText("Non hai creato alcun evento");
                    break;
                case 1:
                    eventiCreatiTextArea1.setText("Hai creato " + eventiCreati + " evento: ");
                    eventiCreatiTextArea1.append(result.toString());
                    break;
                default:
                    eventiCreatiTextArea1.setText("Hai creato " + eventiCreati + " eventi: ");
                    eventiCreatiTextArea1.append(result.toString());
                    break;
            }
        }
    }


    private void riempiComboboxEvento(Controller controller, Organizzatore organizzatore) {
        List<Hackathon> eventi = controller.getEventi();
        if (!eventi.isEmpty()) {
            for (Hackathon e : eventi) {
                if (e.getOrganizzatore().equals(organizzatore)) {
                    eventoInvitoComboBox1.addItem(e); // aggiungi l'oggetto Hackathon
                }
            }
        }
    }

    private void aggiornaPagina(Controller controller, Organizzatore organizzatore){
        eventoInvitoComboBox1.removeAllItems();
        riempiComboboxEvento(controller, organizzatore);
        aggiornaEventiCreati(controller, organizzatore);
    }
}
