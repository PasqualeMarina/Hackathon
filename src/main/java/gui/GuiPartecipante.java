package gui;

import controller.Controller;
import model.*;
import model.Hackathon;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class GuiPartecipante extends JFrame {
    private JPanel HomePanel;
    private JList<Team> listaInviti;
    private JPanel HackatonPanel;
    private JPanel TeamPanel;
    private JTextField textTeam;
    private JComboBox<Hackathon> comboBoxTeam;
    private JButton createTeamButton;
    private JPanel InvitesPanel;
    private JButton accettaInvitoButton;
    private JButton inviteButton;
    private JLabel hackatonlabel;
    private JLabel title;
    private JList<Hackathon> List1;
    private JButton selectButton;
    private JLabel creaTeam;
    private JLabel InvitiRicevuti;
    private JLabel TeamName;
    private JTextField textInviti;
    private JList<Hackathon> listevents;
    private JPanel EventsPanel;
    private JLabel teamiscritto;
    private JButton esciButton;
    private Organizzatore organizzatore;

    public GuiPartecipante(Controller controller, Partecipante partecipante) {
        setTitle("Benvenuto " + partecipante.getNome() + " " + partecipante.getCognome() + "!");
        setResizable(true);
        setContentPane(HomePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 500);
        setVisible(true);

        title.setText("Benvenuto " + partecipante.getNome() + " " + partecipante.getCognome() + "!");
        creaTeam.setText("Crea Team: ");
        InvitiRicevuti.setText("Inviti Ricevuti: ");
        TeamName.setText("Eventi a cui sei iscritto");

        DefaultListModel<Hackathon> modelEventi = new DefaultListModel<>();
        ArrayList<Hackathon> listaHackathon = controller.getEventi();
        ArrayList<Hackathon> nonIscritti = controller.getHackathonNonIscritti(listaHackathon, partecipante);

        for (Hackathon h : nonIscritti) {
            modelEventi.addElement(h);
        }
        List1.setModel(modelEventi);

        selectButton.addActionListener(e -> {
            Hackathon selected = List1.getSelectedValue();
            if (selected != null) {
                if(controller.effettuaIscrizione(partecipante, selected)){
                    modelEventi.removeElement(selected);
                    JOptionPane.showMessageDialog(this, "Ti sei iscritto ad un evento!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un Hackathon dalla lista!");
            }
            aggiornaPagina(controller, partecipante);
        });

        createTeamButton.addActionListener(e -> {
            if(controller.getTeam(partecipante) != null && !controller.getTeam(partecipante).isBlank())
                JOptionPane.showMessageDialog(this, "Fai già parte di un team!");
            else{
                    if (textTeam.getText().trim().isEmpty() || comboBoxTeam.getSelectedItem() == null)
                        JOptionPane.showMessageDialog(this, "Seleziona un nome per il team e un evento!");
                    else {
                        String nomeTeam = textTeam.getText().trim();
                        Hackathon selezionato = (Hackathon) comboBoxTeam.getSelectedItem();
                        Team team = controller.creaTeam(nomeTeam, selezionato, partecipante);
                        JOptionPane.showMessageDialog(this, "Team creato!");
                        aggiornaPagina(controller, partecipante);
                    }
            }

        });

        accettaInvitoButton.addActionListener(e -> {
            Team selected = listaInviti.getSelectedValue();

            String teamCorrente = controller.getTeam(partecipante);

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Seleziona un invito!");
            } else if (teamCorrente != null && !teamCorrente.isBlank()) {
                JOptionPane.showMessageDialog(this, "Fai già parte di un team!");
            } else {
                controller.accettaInvito(partecipante, selected);
                JOptionPane.showMessageDialog(this, "Ora fai parte di un team!");
            }
            aggiornaPagina(controller, partecipante);
        });

        textInviti.setPreferredSize(new Dimension(500, 30));
        inviteButton.addActionListener(e -> {
            String email = textInviti.getText().trim();
            String team = controller.getTeam(partecipante);

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci un'email valida!");
            } else if (team != null && !team.isBlank()) {
                if(controller.inviaInvito(partecipante, email))
                    JOptionPane.showMessageDialog(this, "Invito inviato!");
                else
                    JOptionPane.showMessageDialog(this, "Hai già invitato questo partecipante!");
            } else {
                JOptionPane.showMessageDialog(this, "Devi prima far parte di un team per invitare altri partecipanti!");
            }
            aggiornaPagina(controller, partecipante);
        });

        esciButton.addActionListener(e -> {
            dispose();
            new Login(controller);
        });

        aggiornaPagina(controller, partecipante);
    }

    public void aggiornaListaEventi(Controller controller, Partecipante partecipante) {
        DefaultListModel<Hackathon> modello2 = new DefaultListModel<>();
        for (Hackathon h : controller.getIscrizioni(partecipante)) {
            modello2.addElement(h);
        }
        listevents.setModel(modello2);
        listevents.setPreferredSize(new Dimension(400, 100));
    }

    public void aggiornaCreateTeam(Controller controller, Partecipante partecipante) {
        textTeam.setText("");
    }

    public void aggiornaComboBox(Controller controller, Partecipante partecipante) {
        comboBoxTeam.removeAllItems();
        for (Hackathon h : controller.getIscrizioni(partecipante)) {
            comboBoxTeam.addItem(h);
        }
    }

    public void aggiornaListaInviti(Controller controller, Partecipante partecipante) {
        DefaultListModel<Team> modelTeam = new DefaultListModel<>();
        for (Team t : controller.getInviti(partecipante)) {
            modelTeam.addElement(t);
        }
        listaInviti.setModel(modelTeam);
    }

    public void aggiornaPagina(Controller controller, Partecipante partecipante) {
        aggiornaComboBox(controller, partecipante);
        aggiornaListaEventi(controller, partecipante);
        aggiornaListaInviti(controller, partecipante);

        String team = controller.getTeam(partecipante);
        if (team != null && !team.isBlank()) {
            teamiscritto.setText(team);
        } else {
            teamiscritto.setText("Nessun team associato");
        }
    }
}