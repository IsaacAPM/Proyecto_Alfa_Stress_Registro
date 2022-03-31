package mx.itam;

import mx.itam.Clases.Jugador;
import mx.itam.Client.Cliente;
import mx.itam.Servidor.Deployer;

import javax.sound.midi.Soundbank;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Tests {
    private static final int[] numClientes = {50,100,150,250,350,450,500};
    //private static final int numRondas = 50;
    private static ArrayList<TestCliente> clientes = new ArrayList<TestCliente>();

    public static void main(String[] args) throws InterruptedException {
        //Crea N clientes
        System.out.println("Se crean los clientes");
        for(int numCliente : numClientes) {
            TestCSV.nombreCsv = "prueba" + numCliente + ".csv";
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < numCliente; i++) {
                    String nomCliente = "Cliente " + i;
                    TestCliente temp = new TestCliente(nomCliente);
                    System.out.println(nomCliente + " creado");
                    temp.start();
                    clientes.add(temp);
                    Thread.sleep(100);
                }
                Thread.sleep(1000*60*5);
            }
        }

        System.out.println("\n------Jugadores creados------\n");
        //Thread.sleep(1000);

        /*for(int j = 0; j < clientes.size(); j++){
            clientes.get(j).start();
        }*/


        /*for(int i = 0; i<numRondas;i++){
            int ganador = ganadorRonda();
            System.out.println("Ganador: " + ganador);
            clientes.get(ganador).ganaRonda("Cliente " + ganador);
            Thread.sleep(1000);
        }*/
    }

    public static int ganadorRonda(){
        return randomNumber(clientes.size(), 0);
    }

    private static int randomNumber(int max, int min){
        Random random = new Random();

        int value = random.nextInt(max - min) + min;
        return  value;
    }
}
