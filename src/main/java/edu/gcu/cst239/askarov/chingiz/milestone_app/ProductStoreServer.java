package edu.gcu.cst239.askarov.chingiz.milestone_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

import edu.gcu.cst239.askarov.chingiz.milestone_app.services.FileService;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.InventoryManager;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.InventoryService;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.ProductCommandService;

/**
 * Product Store server. Serves one client at a time over TCP and
 * answers every command with one line of JSON.
 */
public class ProductStoreServer {

    /** The port the server listens on and the client connects to. */
    public static final int PORT = 6060;

    private final ProductCommandService commandService;

    /**
     * Creates the server.
     *
     * @param commandService the service that runs the client's commands
     */
    public ProductStoreServer(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Listens on the port and handles clients one after another.
     *
     * @param port the TCP port to listen on
     */
    public void start(int port) {
        System.out.println("Product Store server listening on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException ex) {
            System.err.println("Server error: " + ex.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());
        try (socket;
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals(".")) {
                    output.println("Goodbye");
                    break;
                }
                output.println(commandService.processMessage(message));
            }
        } catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
        System.out.println("Client disconnected");
    }

    /**
     * Loads the inventory from JSON and starts the server.
     *
     * @param args command line arguments, they are not used
     * @throws IOException when a JSON file cannot be read
     */
    public static void main(String[] args) throws IOException {
        InventoryService inventoryService = new InventoryManager(new FileService().loadInventory(
                Path.of("src", "main", "resources", "data", "categories.json"),
                Path.of("src", "main", "resources", "data", "products.json")));
        System.out.println("Loaded " + inventoryService.getAllInventoryItems().size() + " products from JSON.");
        new ProductStoreServer(new ProductCommandService(inventoryService)).start(PORT);
    }
}
