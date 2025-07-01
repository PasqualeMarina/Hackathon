package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.util.List;

/**
 * Classe che implementa l'interfaccia grafica per il partecipante.
 * Permette al partecipante di:
 * - Visualizzare e iscriversi agli hackathon disponibili
 * - Creare e gestire team per gli eventi
 * - Inviare e accettare inviti per i team
 * - Visualizzare gli hackathon a cui è iscritto
 */
public class GuiPartecipante extends JFrame {
    private JPanel homePanel;
    private JList<Team> listaInviti;
    private JTextField textTeam;
    private JComboBox<Team> comboBoxTeam;
    private JButton createTeamButton;
    private JButton accettaInvitoButton;
    private JButton inviteButton;
    private JLabel namePage;
    private JList<Hackathon> list1;
    private JButton selectButton;
    private JLabel creaTeam;
    private JLabel invitiRicevuti;
    private JLabel teamName;
    private JTextField textInviti;
    private JList<Hackathon> listevents;
    private JLabel iscrizioneTeams;
    private JButton esciButton;
    private JComboBox<Hackathon> comboBoxEventi;
    private JLabel hackathonlabel;
    private JPanel hackathonPanel;
    private JPanel teamPanel;
    private JPanel invitesPanel;
    private JPanel eventsPanel;
    private JComboBox<Team> comboBoxTeam2;
    private JTextField linkDocumento;
    private JButton creaDocumentoButton;

    public GuiPartecipante(Controller controller, Partecipante partecipante) {
        setTitle("Benvenuto " + partecipante.getNome() + " " + partecipante.getCognome() + "!");
        setResizable(true);
        setContentPane(homePanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1100, 500);
        setVisible(true);

        utilizzaPannelli();
        hackathonlabel.setText("Scegli Hackaton a cui partecipare:");
        namePage.setText("Benvenuto " + partecipante.getNome() + " " + partecipante.getCognome() + "!");
        creaTeam.setText("Crea Team:");
        invitiRicevuti.setText("Inviti Ricevuti:");
        teamName.setText("Eventi a cui sei iscritto:");

        aggiornaPagina(controller, partecipante);
        initListeners(controller, partecipante);
    }

    private void utilizzaPannelli() {
        hackathonPanel.setVisible(true);
        teamPanel.setVisible(true);
        invitesPanel.setVisible(true);
        eventsPanel.setVisible(true);
    }


    // 1. Lista hackathon non iscritti
    public void aggiornaListaNonIscritti(Controller controller, Partecipante p) {
        DefaultListModel<Hackathon> model = new DefaultListModel<>();
        List<Hackathon> nonIscritti = controller.getHackathonNonIscritti(p);
        for (Hackathon h : nonIscritti) {
            model.addElement(h);
        }
        list1.setModel(model);
    }

    // 2. Lista eventi a cui sei iscritto
    public void aggiornaListaEventi(Controller controller, Partecipante p) {
        DefaultListModel<Hackathon> model = new DefaultListModel<>();
        for (Hackathon h : controller.getIscrizioni(p)) {
            model.addElement(h);
        }
        listevents.setModel(model);
    }
    // 3. ComboBox per invitare (team dell'utente)

    private void aggiornaComboBoxTeam(Controller controller, Partecipante p) {
        comboBoxTeam.removeAllItems();
        for (Team t : controller.getTeamsPartecipante(p)) {
            comboBoxTeam.addItem(t);
            comboBoxTeam2.addItem(t);
        }
    }

    // 4. ComboBox per creare un team (eventi iscritti)
    private void aggiornaComboBoxEventi(Controller controller, Partecipante p) {
        comboBoxEventi.removeAllItems();
        for (Hackathon h : controller.getIscrizioni(p)) {
            comboBoxEventi.addItem(h);
        }
    }

    // 5. Lista inviti ricevuti
    private void aggiornaListaInviti(Controller controller, Partecipante p) {
        DefaultListModel<Team> model = new DefaultListModel<>();
        for (Team t : controller.getInviti(p)) {
            model.addElement(t);
        }
        listaInviti.setModel(model);
    }

    // 6. Reset del campo nome team
    private void aggiornaCreateTeam() {
        textTeam.setText("");
    }

    // 7. Reset del campo email invito
    private void aggiornaTextInviti() {
        textInviti.setText("");
    }

    // 8. Aggiorna riepilogo team per evento
    private void aggiornaListaTeam(Controller controller, Partecipante p) {
        StringBuilder sb = new StringBuilder("<html>");
        for (Team t : controller.getTeamsPartecipante(p)) {
            sb.append(controller.getHackathon(t))
                    .append(": ")
                    .append(controller.getNomeTeam(t))
                    .append("<br>");
        }
        sb.append("</html>");
        iscrizioneTeams.setText(sb.length() > 13 ? sb.toString() : "Nessun team associato");
    }


    // Chiamata completa per aggiornare la pagina
    private void aggiornaPagina(Controller controller, Partecipante partecipante) {
        aggiornaCreateTeam();
        aggiornaTextInviti();
        aggiornaListaNonIscritti(controller, partecipante);
        aggiornaListaEventi(controller, partecipante);
        aggiornaComboBoxTeam(controller, partecipante);
        aggiornaComboBoxEventi(controller, partecipante);
        aggiornaListaInviti(controller, partecipante);
        aggiornaListaTeam(controller, partecipante);
    }

    private void initSelectButtonListener(Controller controller, Partecipante partecipante) {
        selectButton.addActionListener(e -> {
            Hackathon selected = list1.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Seleziona un Hackathon dalla lista!");
            } else {
                if (controller.effettuaIscrizione(partecipante, selected)) {
                    JOptionPane.showMessageDialog(this, "Ti sei iscritto ad un evento!");
                }
            }
            aggiornaPagina(controller, partecipante);
        });
    }

    private void initCreateTeamButtonListener(Controller controller, Partecipante partecipante) {
        createTeamButton.addActionListener(e -> {
            Object sel = comboBoxEventi.getSelectedItem();
            String nomeTeam = textTeam.getText().trim();

            if(!(sel instanceof Hackathon evento))
                JOptionPane.showMessageDialog(this, "Seleziona un evento!");
            else if (nomeTeam.isEmpty())
                JOptionPane.showMessageDialog(this, "Inserisci un nome per il team!");
            else if (controller.parteDiTeam(partecipante, evento))
                JOptionPane.showMessageDialog(this, "Già fai parte di un team per questo evento!");
            else if(controller.creaTeam(nomeTeam, partecipante, evento)){
                JOptionPane.showMessageDialog(this, "Team creato!");
            }
            aggiornaPagina(controller, partecipante);
        });
    }

    private void initAccettaInvitoButtonListener(Controller controller, Partecipante partecipante) {
        accettaInvitoButton.addActionListener(e -> {
            Team selected = listaInviti.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Seleziona un invito!");
            } else if (controller.parteDiTeam(partecipante, selected.getEvento())) {
                JOptionPane.showMessageDialog(this, "Hai già un team per questo evento!");
            } else {
                controller.accettaInvito(partecipante, selected);
                JOptionPane.showMessageDialog(this, "Invito accettato!");
            }
            aggiornaPagina(controller, partecipante);
        });
    }

    private void initInviteButtonListener(Controller controller, Partecipante partecipante) {
        inviteButton.addActionListener(e -> {
            String email = textInviti.getText().trim();
            Object sel = comboBoxTeam.getSelectedItem();

            if (email.isEmpty())
                JOptionPane.showMessageDialog(this, "Inserisci un'email valida!");
            else if (!(sel instanceof Team team))
                JOptionPane.showMessageDialog(this, "Seleziona un team per invitare!");
            else {
                if (controller.inviaInvito(partecipante, email, team))
                    JOptionPane.showMessageDialog(this, "Invito inviato!");
                else
                    JOptionPane.showMessageDialog(this, "Impossibile inviare invito!");
            }

            aggiornaPagina(controller, partecipante);
        });
    }

    private void initEsciButtonListener(Controller controller) {
        esciButton.addActionListener(e -> {
            dispose();
            new Login(controller);
        });
    }

    private void initCreaDocumento(Controller controller, Partecipante partecipante){
        creaDocumentoButton.addActionListener(e -> {
            Object t = comboBoxTeam2.getSelectedItem();
            String link = linkDocumento.getText().trim();
            if(!(t instanceof Team team))
                JOptionPane.showMessageDialog(this, "Seleziona un team!");
            else if(controller.aggiungiDocumento(team, new Documento(link, team)))
                JOptionPane.showMessageDialog(this, "Documento aggiunto!");
            else
                JOptionPane.showMessageDialog(this, "Errore nella creazione del documento!");
            aggiornaPagina(controller, partecipante);
        });
    }

    private void initListeners(Controller controller, Partecipante partecipante) {
        initSelectButtonListener(controller, partecipante);
        initCreateTeamButtonListener(controller, partecipante);
        initAccettaInvitoButtonListener(controller, partecipante);
        initInviteButtonListener(controller, partecipante);
        initEsciButtonListener(controller);
        initCreaDocumento(controller, partecipante);
    }

}
