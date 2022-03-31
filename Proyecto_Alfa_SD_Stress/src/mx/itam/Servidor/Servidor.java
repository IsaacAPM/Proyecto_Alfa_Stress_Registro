package mx.itam.Servidor;

import mx.itam.Clases.Jugador;
import mx.itam.Interfaces.Registro;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class Servidor implements Registro {
    public static ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    private static int playersCounter = 0;
    private ServerSocket listenSocket;
    public static int N;
    public static boolean encuentraGanador = false;
    public static boolean recibeTCP;
    private static MulticastSocket socketUDP = null;
    private static InetAddress group = null;
    public static String nomGanador;
    private static final int portTCP = 49200;
    private static final int portUDP = 49159;
    private static final String inetA = "228.5.6.7";
    public static int ronda = 1;

    public Servidor() throws RemoteException{
        super();
    }

    public Servidor(int N){
        super();
        Servidor.N = N;
    }

    public void deploy(String name){
        System.setProperty("java.security.policy", "src/mx/itam/Servidor/server.policy");
        //En versiones recientes de la maquina virutal de java marca error
        /*if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }*/
        try {
            Servidor engine = new Servidor();
            Registro stub = (Registro) UnicastRemoteObject.exportObject(engine, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Servicio de registro desplegado\n");

            //Se genera el servidor Multicast UDP
            group = InetAddress.getByName(inetA); // destination multicast group
            socketUDP = new MulticastSocket(portUDP);
            socketUDP.joinGroup(group);

            //Espera a que haya al menos un jugador
            System.out.println("Esperando jugadores");
            while (jugadores.size()==0){
                Thread.sleep(1000);
            }

            System.out.println("Jugador encontrado");

            Thread.sleep(2000);

            System.out.println("\n------Arranca el juego------\n");
            //Arranca el juego
            listenSocket = new ServerSocket(portTCP);

            while (true){
                loopJuego();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loopJuego() {
        try {
            while (!encuentraGanador){
                //System.out.println("Envia UDP");
                this.recibeTCP = false;
                int posMonstruo = randomNumber(9,1);
                enviaMensajeUDP(ronda + ";" +posMonstruo + ";null");

                //System.out.println("Genera TCP");
                //System.out.println(recibeTCP);

                while (!recibeTCP){
                    //System.out.println("Escucho");
                    Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                    //System.out.println("Recibo TCP");
                    Connection c = new Connection(clientSocket);
                    c.start();
                    Thread.sleep(100);
                    //System.out.println(recibeTCP);
                }
            }
            enviaMensajeUDP(ronda + ";0;" + nomGanador);
            System.out.println("GANADOR: " + nomGanador);
            for(int i = 0; i < jugadores.size();i++){
                jugadores.get(i).resetWinCount();
            }
            ronda = 1;
            encuentraGanador = false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String registro(String id) throws RemoteException{
        playersCounter++;
        String IP = "localhost";
        String resp = IP + ";" + portTCP + ";" + portUDP + ";" + inetA;
        Jugador aux = new Jugador(id,N);

        if(!jugadores.contains(aux)){
           jugadores.add(aux);
        }

        System.out.println(this);
        return  resp;
    }

    private int randomNumber(int max, int min){
        Random random = new Random();

        int value = random.nextInt(max - min) + min;
        return  value;
    }

    private void enviaMensajeUDP(String mensaje) {
        try {
            System.out.println(mensaje);
            byte[] m = mensaje.getBytes();
            DatagramPacket messageOut =
                    new DatagramPacket(m, m.length, group, portUDP);
            socketUDP.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Jugador jugador : jugadores){
            sb.append(jugador.toString());
        }
        return sb.toString();
    }
}

class Connection extends Thread {
    private DataInputStream in;
    private Socket clientSocket;

    public Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            int length = in.readInt();
            byte[] array = new byte[length];
            in.readFully(array);
            String[] mensaje = new String(array).split(";");
            int rondaJugador = Integer.parseInt(mensaje[0]);

            if(rondaJugador == Servidor.ronda) {
                String nombreUsuario = mensaje[1];
                System.out.println("Ronda " + Servidor.ronda + ": " + nombreUsuario);
                int i = 0;
                boolean encuentraJugador = false;

                while (i < Servidor.jugadores.size() && !encuentraJugador) {
                    encuentraJugador = Servidor.jugadores.get(i).getId().equals(nombreUsuario);
                    i++;
                }

                if (encuentraJugador) {
                    Servidor.encuentraGanador = Servidor.jugadores.get(i - 1).incWinCount();
                    System.out.println("Puntos: " + Servidor.jugadores.get(i - 1).getWinCount());
                    Servidor.nomGanador = Servidor.jugadores.get(i - 1).getId();
                    Servidor.recibeTCP = true;
                }

                Servidor.ronda = Servidor.ronda + 1;
            }
        } catch (SocketException e) {
            Servidor.recibeTCP = true;
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException se){
            se.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
