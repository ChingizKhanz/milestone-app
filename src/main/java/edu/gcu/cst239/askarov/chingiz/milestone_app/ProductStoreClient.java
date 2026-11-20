package edu.gcu.cst239.askarov.chingiz.milestone_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Product Store client. Sends commands typed in the console to the
 * server and prints the JSON it answers with.
 */
public class ProductStoreClient {

    /**
     * Connects to the server and runs the command loop until the user types a period.
     *
     * @param args optional host and port, localhost and 6060 by default
     */
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : ProductStoreServer.PORT;

        try (Socket socket = new Socket(host, port);
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to " + host + ":" + port);
            System.out.println("Commands: ALL, GET id, FIND text, DELETE id,");
            System.out.println("SORT NAME, SORT PRICE, or . to quit");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();
                output.println(command);
                System.out.println(input.readLine());
                if (command.equals(".")) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
    }
}
