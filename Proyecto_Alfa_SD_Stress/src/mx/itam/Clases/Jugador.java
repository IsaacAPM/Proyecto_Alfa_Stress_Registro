package mx.itam.Clases;

public class Jugador implements Comparable<Jugador>{
    private String id; //identificador del jugador
    private int winCount; //contador de victorias
    private int N; //victorias para ganar

    public Jugador(String  id, int N){
        this.id = id;
        this.N = N;
        this.winCount = 0;
    }

    public  Jugador(){

    }

    public boolean incWinCount(){
        /*Esta fución incrementa en 1 las victorias de este jugador
        * si ya alcanzó las victias para ganar regresa True, sino regresa False.
        * */
        this.winCount++;
        return winCount==N;
    }

    public String getId(){
        return this.id;
    }

    public int getWinCount(){
        return this.winCount;
    }

    public void resetWinCount(){
        this.winCount=0;
    }

    public boolean equals(Jugador otroJugador){
        return this.id.equals(otroJugador.getId());
    }

    @Override
    public int compareTo(Jugador otroJugador){
        int resp = 0;
        if(this.winCount > otroJugador.getWinCount()){
            resp = 1;
        }else if(this.winCount < otroJugador.getWinCount()){
            resp = -1;
        }
        return resp;
    }

    public String toString(){
        StringBuilder bd = new StringBuilder();
        bd.append("ID: " + this.id + " ");
        bd.append("Puntos: " + this.winCount + "\n");
        return bd.toString();
    }


}
