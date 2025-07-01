package dao;

import model.Giudice;
import model.Hackathon;
import model.Organizzatore;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative agli organizzatori.
 */
public interface OrganizzatoreDAO {
    /**
     * Crea un nuovo evento Hackathon associato a un organizzatore.
     *
     * @param evento        L'evento Hackathon da creare
     * @param organizzatore L'organizzatore che crea l'evento
     */
    void creaEvento(Hackathon evento, Organizzatore organizzatore);

    /**
     * Invia un invito a un giudice per partecipare a un evento Hackathon.
     *
     * @param giudice Il giudice da invitare
     * @param evento  L'evento Hackathon per cui inviare l'invito
     */
    void invitaGiudice(Giudice giudice, Hackathon evento);

    /**
     * Recupera l'organizzatore associato a un evento Hackathon.
     *
     * @param titoloHackathon Il titolo dell'evento Hackathon
     * @return L'organizzatore associato all'evento
     */
    Organizzatore getOrganizzatoreByHackathon(String titoloHackathon);
}

