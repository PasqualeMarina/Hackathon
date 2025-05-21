package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class GuiGiudice extends JFrame{
    private JTextArea descrizioneTextArea1;
    private JComboBox eventiDescrizioneComboBox1;
    private JPanel inserisciDescrizioneJPanel;
    private JButton INSERISCIButton;
    private JPanel HomePanel;
    private JComboBox eventiVotiComboBox1;
    private JComboBox teamComboBox2;
    private JComboBox votoComboBox3;
    private JScrollPane documentiPubblicatiPanel;
    private JTextArea documentiPubblicatiTextArea1;
    private JPanel invitiJPanel;
    private JTextArea invitiTextArea1;
    private JButton INSERISCIButton1;
    private JButton ACCETTAButton;
    private JButton RIFIUTAButton;
    private JComboBox invitiComboBox1;
    private JButton ESCIButton;

    public GuiGiudice(Controller controller, Giudice giudice){
        setTitle("Home Organizzatore");
        setResizable(false);
        setContentPane(HomePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(850, 400);
        setVisible(true);
        descrizioneTextArea1.setLineWrap(true);           // Fa andare a capo automaticamente
        descrizioneTextArea1.setWrapStyleWord(true);
        documentiPubblicatiTextArea1.setEditable(false);
        invitiTextArea1.setEditable(false);

        aggiornaPagina(controller, giudice);

        // reagisce al cambio di selezione in eventiVotiComboBox1
        eventiVotiComboBox1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof Hackathon h) {
                // 1) svuota area documenti
                documentiPubblicatiTextArea1.setText("");
                // 2) svuota combo team
                teamComboBox2.removeAllItems();
                // 3) ricarica dati per l’evento selezionato
                recuperaDocumentiCompetizione(controller, h);
                riempiComboBoxTeam(controller, h);
            }
        });


        INSERISCIButton.addActionListener(e->{
            String descrizione = descrizioneTextArea1.getText();
            if(descrizione.isBlank())
                JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Inserisci una descrizione!", "Errore", JOptionPane.WARNING_MESSAGE);
            else if(eventiDescrizioneComboBox1.getSelectedItem().equals("Seleziona evento"))
                JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Seleziona un evento per modificarne la descrizione", "Errore", JOptionPane.WARNING_MESSAGE);
            else{
                if(controller.pubblicaProblema(giudice, (Hackathon) eventiDescrizioneComboBox1.getSelectedItem(), descrizione))
                    JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Descrizione pubblicata con successo!", "Descrizione", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(inserisciDescrizioneJPanel, "Errore nella pubblicazione della descrizione!", "Descrizione", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        INSERISCIButton1.addActionListener(e->{
            if(controller.daiVoto(giudice, (Team)teamComboBox2.getSelectedItem(), Integer.parseInt(votoComboBox3.getSelectedItem().toString())))
                JOptionPane.showMessageDialog(invitiJPanel, "Voto consegnato con successo!", "Voto", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(invitiJPanel, "Hai già votato per questo team!", "Voto", JOptionPane.WARNING_MESSAGE);
        });

        ACCETTAButton.addActionListener(e->{
            if(invitiComboBox1.getSelectedItem().equals("Seleziona evento"))
                JOptionPane.showMessageDialog(invitiJPanel, "Seleziona un evento per accettare l'invito!", "Errore", JOptionPane.WARNING_MESSAGE);
            else{
                if(controller.accettaInvito(giudice, (Hackathon)invitiComboBox1.getSelectedItem()))
                    JOptionPane.showMessageDialog(invitiJPanel, "Invito accettato con successo!", "Invito", JOptionPane.INFORMATION_MESSAGE);
            }
            aggiornaPagina(controller, giudice);
        });

        RIFIUTAButton.addActionListener(e->{
            controller.rifiutaInvito(giudice, (Hackathon)invitiComboBox1.getSelectedItem());
            JOptionPane.showMessageDialog(invitiJPanel, "Invito rifiutato!", "Invito", JOptionPane.INFORMATION_MESSAGE);
            aggiornaPagina(controller, giudice);
        });

        ESCIButton.addActionListener(e->{
            dispose();
            new Login(controller);
        });
    }

    void riempiComboboxEvento(Controller controller, Giudice giudice) {
        ArrayList<Hackathon> eventi = controller.getEventi();
        if (!eventi.isEmpty()) {
            for (Hackathon e : eventi) {
                if (e.getListaGiudici().contains(giudice)) {
                    eventiDescrizioneComboBox1.addItem(e); // aggiungi l'oggetto Hackathon a entrambe le combo box
                    eventiVotiComboBox1.addItem(e);
                }
            }
        }
    }

    void riempiComboBoxTeam(Controller controller, Hackathon evento){
        for(Team t : controller.getTeams(evento)){
            teamComboBox2.addItem(t);
        }
    }

    void recuperaDocumentiCompetizione(Controller controller, Hackathon evento){
        for(Team t : controller.getTeams(evento))
            documentiPubblicatiTextArea1.append(controller.getDocumenti(t) + "\n\n" );
    }

    void aggiornaPagina(Controller controller, Giudice giudice){
        //svuota per aggiornare
        descrizioneTextArea1.setText("");
        eventiDescrizioneComboBox1.removeAllItems();
        eventiVotiComboBox1.removeAllItems();
        teamComboBox2.removeAllItems();
        invitiComboBox1.removeAllItems();

        //inserisci informazioni aggiornate
        invitiComboBox1.addItem("Seleziona evento");
        eventiDescrizioneComboBox1.addItem("Seleziona evento");
        eventiVotiComboBox1.addItem("Seleziona evento");
        riempiComboBoxInviti(controller, giudice);
        riempiTextAreaInviti(controller, giudice);
        riempiComboboxEvento(controller, giudice);

        Object selectedItem = eventiVotiComboBox1.getSelectedItem();
        if(selectedItem instanceof Hackathon selectedEvento) {
            riempiComboBoxTeam(controller, selectedEvento);
            recuperaDocumentiCompetizione(controller, selectedEvento);
        }
    }

    void riempiTextAreaInviti(Controller controller, Giudice giudice){
        invitiTextArea1.setText(controller.getInviti(giudice));
    }

    void riempiComboBoxInviti(Controller controller, Giudice giudice){
        for(Hackathon h : controller.getInvitiGiudice(giudice))
            invitiComboBox1.addItem(h);
    }
}
