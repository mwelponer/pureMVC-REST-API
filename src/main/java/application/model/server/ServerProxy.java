package application.model.server;

import org.puremvc.java.multicore.interfaces.IProxy;
import org.puremvc.java.multicore.patterns.proxy.Proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProxy extends Proxy implements IProxy, Runnable {

    public static final String NAME = "ServerProxy";

    protected boolean isStopped = false;
    protected int serverPort;
    protected ServerSocket serverSocket;
    protected Thread runningThread;

    public ServerProxy(ServerPreferencesVO prefs) {
        super(NAME, prefs);
        this.serverPort = prefs.getServerPort();
        System.out.println("ServerProxy()");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        System.out.println("  ServerProxy: stop()");
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(" ..error closing server", e);
        }
    }

    private void openServerSocket() {
        System.out.println("  ServerProxy: openServerSocket()");
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("  ..cannot open server socket on port " + serverPort, e);
        }
    }

    @Override
    public void run() {
        System.out.println("  ServerProxy: run()");

        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        System.out.println("  ..server running on port " + this.serverPort);
        while(! isStopped()){
            //System.out.print(".");
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("  ..server Stopped.");
                    return;
                }
                throw new RuntimeException("  ..error accepting client connection", e);
            }
            System.out.println("  ..message received");

            new Thread(new MessageProcessor(clientSocket, "Multithreaded Server")
            ).start();
        }

        System.out.println("  ..server Stopped.");
    }

//    public static void main(String[]args){
//        ServerPreferencesProxy prefs = new ServerPreferencesProxy();
//        prefs.setPort(9000);
//
//        ServerProxy server = new ServerProxy(prefs.getServerPrefs());
//        new Thread(server).start();
//
//        try {
//            Thread.sleep(20 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Stopping Server");
//        server.stop();
//    }
}