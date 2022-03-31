package mx.itam.Tablero;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class Tablero extends JFrame{
    private static int portTCP;
    private static String nombreJugador;
    private static String IP;
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;

    Icon img = new ImageIcon("src/mx/itam/Tablero/monstruo.png");

    public void conectar(String nombreJugador, String IP, int portTCP){
        this.portTCP = portTCP;
        this.nombreJugador = nombreJugador;
        this.IP = IP;
    }

    public void limpiar(){
        button1.setIcon(null);
        button2.setIcon(null);
        button3.setIcon(null);
        button4.setIcon(null);
        button5.setIcon(null);
        button6.setIcon(null);
        button7.setIcon(null);
        button8.setIcon(null);
        button9.setIcon(null);
    }

    public void muestra(int posMonstruo, boolean ganador){
        /*System.out.println(posMonstruo);*/
        Timer tiempo;
        tiempo = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    switch (posMonstruo){
                        case 1:
                            limpiar();
                            button1.setIcon(img);
                            break;
                        case 2:
                            limpiar();
                            button2.setIcon(img);
                            break;
                        case 3:
                            limpiar();
                            button3.setIcon(img);
                            break;
                        case 4:
                            limpiar();
                            button4.setIcon(img);
                            break;
                        case 5:
                            limpiar();
                            button5.setIcon(img);
                            break;
                        case 6:
                            limpiar();
                            button6.setIcon(img);
                            break;
                        case 7:
                            limpiar();
                            button7.setIcon(img);
                            break;
                        case 8:
                            limpiar();
                            button8.setIcon(img);
                            break;
                        case 9:
                            limpiar();
                            button9.setIcon(img);
                            break;
                        default:
                            limpiar();
                            if (ganador) JOptionPane.showMessageDialog(null,"Ganaste");
                            else JOptionPane.showMessageDialog(null,"Mejor suerte la próxima");
                            break;
                    }
                }
        });
        tiempo.start();
    }

    public Tablero() {
        this.setContentPane(this.panel1);
        this.setTitle("Tablero");
        this.setSize(700,800);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button1.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button2.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button3.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button4.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button5.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button6.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button7.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button8.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
        button9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(button9.getIcon() != null){
                    mensajeTCP(nombreJugador);
                }
            }
        });
    }

    public void mensajeTCP(String mensaje) {
        Socket socket = null;
        try {
            socket = new Socket(IP, portTCP);
            System.out.println(mensaje);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            byte[] data = mensaje.getBytes();
            out.writeInt(data.length);
            out.write(data);
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: -" + e.getMessage());
        } finally {
            if (socket != null) try {
                socket.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
