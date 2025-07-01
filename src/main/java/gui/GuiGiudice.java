package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;

/**
 * Classe che implementa l'interfaccia grafica per il giudice.
 * Permette al giudice di:
 * - Visualizzare e valutare i documenti dei team
 * - Inserire descrizioni per gli hackathon
 * - Gestire gli inviti ricevuti dagli organizzatori
 * - Visualizzare gli hackathon a cui partecipa
 */
public class GuiGiudice extends JFrame{
    private JTextArea descrizioneTextArea1;
    private JComboBox<Hackathon> eventiDescrizioneComboBox1;
    private JPanel inserisciDescrizioneJPanel;
    private JButton inserisciButton;
    private JPanel homePanel;
    private JComboBox<Hackathon> eventiVotiComboBox1;
    private JComboBox<Team> teamComboBox2;
    private JComboBox<Integer> votoComboBox3;
    private JTextArea documentiPubblicatiTextArea1;
    private JPanel invitiJPanel;
    private JTextArea invitiTextArea1;
    private JButton inserisciButton1;
    private JButton accettaButton;
    private JButton rifiutaButton;
    private JComboBox<Hackathon> invitiComboBox1;
    private JButton esciButton;
    private static final String ERRORE = "ERRORE";
    private JScrollPane documentiPubblicatiPanel;

    
    public GuiGiudice(Controller controller, Giudice giudice){
        setTitle("Home Organizzatore");
        setResizable(false);
        setContentPane(homePanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(850, 400);
        setVisible(true);
        descrizioneTextArea1.setLineWrap(true);           // Fa andare a capo automaticamente
        descrizioneTextArea1.setWrapStyleWord(true);
        documentiPubblicatiTextArea1.setEditable(false);
        invitiTextArea1.setEditable(false);
        documentiPubblicatiPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        aggiornaPagina(controller, giudice);

        initListeners(controller, giudice);
    }

    private void riempiComboboxEvento(Controller controller, Giudice giudice) {
        List<Hackathon> eventi = controller.getEventi();
        if (!eventi.isEmpty()) {
            for (Hackathon e : eventi) {
                if (e.getListaGiudici().contains(giudice)) {
                    eventiDescrizioneComboBox1.addItem(e); // aggiungi l'oggetto Hackathon a entrambe le combo box
                    eventiVotiComboBox1.addItem(e);
                }
            }
        }
    }

    private void riempiComboBoxTeam(Controller controller, Hackathon evento){
        for(Team t : controller.getTeams(evento)){
            teamComboBox2.addItem(t);
        }
    }

    private void recuperaDocumentiCompetizione(Controller controller, Hackathon evento){
        for(Team t : controller.getTeams(evento))
            documentiPubblicatiTextArea1.append(controller.getDocumentiTeam(t) + "\n\n" );
    }

    private void riempiTextAreaInviti(Controller controller, Giudice giudice){
        invitiTextArea1.setText(controller.getInvitiGiudice(giudice).toString() + "\n\n");
    }

    private void riempiComboBoxInviti(Controller controller, Giudice giudice){
        for(Hackathon h : controller.getInvitiGiudice(giudice))
            invitiComboBox1.addItem(h);
    }

    private void aggiornaPagina(Controller controller, Giudice giudice){
        //svuota per aggiornare
        descrizioneTextArea1.setText("");
        eventiDescrizioneComboBox1.removeAllItems();
        eventiVotiComboBox1.removeAllItems();
        teamComboBox2.removeAllItems();
        invitiComboBox1.removeAllItems();

        //inserisci informazioni aggiornate
        riempiComboBoxInviti(controller, giudice);
        riempiTextAreaInviti(controller, giudice);
        riempiComboboxEvento(controller, giudice);

        Object selectedItem = eventiVotiComboBox1.getSelectedItem();
        if(selectedItem instanceof Hackathon selectedEvento) {
            riempiComboBoxTeam(controller, selectedEvento);
            recuperaDocumentiCompetizione(controller, selectedEvento);
        }
    }

    private void initListeners(Controller controller, Giudice giudice) {
        initEventiVotiListener(controller);
        initInserisciDescrizioneListener(controller);
        initVotoListener(controller, giudice);
        initAccettaInvitoListener(controller, giudice);
        initRifiutaInvitoListener(controller, giudice);
        initEsciListener(controller);
    }


    private void initEventiVotiListener(Controller controller) {
        eventiVotiComboBox1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof Hackathon h) {
                documentiPubblicatiTextArea1.setText("");
                teamComboBox2.removeAllItems();
                recuperaDocumentiCompetizione(controller, h);
                riempiComboBoxTeam(controller, h);
            }
        });
    }

    private void initInserisciDescrizioneListener(Controller controller) {
        inserisciButton.addActionListener(e -> {
            String descrizione = descrizioneTextArea1.getText();
            if (descrizione.isBlank())
                JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Inserisci una descrizione!", ERRORE, JOptionPane.WARNING_MESSAGE);
            else {
                if (controller.pubblicaProblema( (Hackathon) eventiDescrizioneComboBox1.getSelectedItem(), descrizione))
                    JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Descrizione pubblicata con successo!", "Descrizione", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Errore nella pubblicazione della descrizione!", "Descrizione", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void initVotoListener(Controller controller, Giudice giudice) {
        inserisciButton1.addActionListener(e -> {
            if(!(teamComboBox2.getSelectedItem() instanceof Team team))
                JOptionPane.showMessageDialog(invitiJPanel, "Seleziona un team!", ERRORE, JOptionPane.WARNING_MESSAGE);
            else if (controller.assegnaVoto(giudice, team, Integer.parseInt(votoComboBox3.getSelectedItem().toString())))
                JOptionPane.showMessageDialog(invitiJPanel, "Voto consegnato con successo!", "Voto", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(invitiJPanel, "Hai già votato per questo team!", "Voto", JOptionPane.WARNING_MESSAGE);
        });
    }

    private void initAccettaInvitoListener(Controller controller, Giudice giudice) {
        accettaButton.addActionListener(e -> {

            if(!(invitiComboBox1.getSelectedItem() instanceof Hackathon evento))
                JOptionPane.showMessageDialog(invitiJPanel, "Seleziona un evento!", ERRORE, JOptionPane.WARNING_MESSAGE);
            else if (controller.accettaInvito(giudice, evento))
                JOptionPane.showMessageDialog(invitiJPanel, "Invito accettato con successo!", "Invito", JOptionPane.INFORMATION_MESSAGE);

            aggiornaPagina(controller, giudice);
        });
    }

    private void initRifiutaInvitoListener(Controller controller, Giudice giudice) {
        rifiutaButton.addActionListener(e -> {
            controller.rifiutaInvito(giudice, (Hackathon) invitiComboBox1.getSelectedItem());
            JOptionPane.showMessageDialog(invitiJPanel, "Invito rifiutato!", "Invito", JOptionPane.INFORMATION_MESSAGE);
            aggiornaPagina(controller, giudice);
        });
    }

    private void initEsciListener(Controller controller) {
        esciButton.addActionListener(e -> {
            dispose();
            new Login(controller);
        });
    }
}
