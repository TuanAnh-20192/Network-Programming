package com;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server {
    public static void main(String[] args) throws Exception {
        String requestMessageLine;
        String fileName;
        ServerSocket listeningSocket = new ServerSocket(6789);
        Socket socketConnection = listeningSocket.accept();
        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
        DataOutputStream outputToClient = new DataOutputStream(socketConnection.getOutputStream());

        requestMessageLine = inputFromClient.readLine();
        StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);

        if (tokenizedLine.nextToken().equals("GET")) {
            fileName = tokenizedLine.nextToken();
            if (fileName.startsWith("/") == true) {
                fileName = fileName.substring(1);
            }
            File file = new File(fileName);
            int numOfBytes = (int) file.length();
            FileInputStream inFile = new FileInputStream(fileName);
            byte[] fileInBytes = new byte[numOfBytes];
            inFile.read(fileInBytes);
            outputToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
            if (fileName.endsWith(".jpg")) {
                outputToClient.writeBytes("Content-Type: image/jpeg\r\n");
            }
            if (fileName.endsWith(".gif")) {
                outputToClient.writeBytes("Content-Type: image/gif\r\n");
            }
            outputToClient.writeBytes("Content-length: " + numOfBytes + "\r\n");
            outputToClient.writeBytes("\r\n");
            outputToClient.write(fileInBytes, 0, numOfBytes);
            socketConnection.close();
        } else {
            System.out.println("Bad request message");
        }
    }
}
