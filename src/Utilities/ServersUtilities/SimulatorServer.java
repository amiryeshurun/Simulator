package Utilities.ServersUtilities;

import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;
import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimulatorServer {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private SimulatorServer() {}

    private static class ServerHolder {
        private static final SimulatorServer server = new SimulatorServer();
    }
    public static SimulatorServer getServer(){
        return ServerHolder.server;
    }

    public void open(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }
    public void stop(){
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {}
    }
    public void setVariable(String varPath, double value) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println("set " + varPath + " " + value);
        out.flush();
    }
    public double getVariable(String varPath) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println("get " + varPath);
        out.flush();
        try {
            return Double.valueOf(in.readLine());
        } catch (IOException e) {}
        return 0;
    }
    public void sendString(String str) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println(str);
        out.flush();
    }
}
