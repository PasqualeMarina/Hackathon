package model;

public class Voto {
    private Giudice giudice;
    private Team team;
    private int voto;

    public Voto(Giudice giudice, Team team, int voto){
        this.giudice = giudice;
        this.team = team;
        this.voto = voto;
    }

    public Team getTeam(){
        return team;
    }

    public void setGiudice(Giudice giudice){
        this.giudice = giudice;
    }

    public int getVoto(){
        return voto;
    }

    public Giudice getGiudice() {
        return giudice;
    }
}
