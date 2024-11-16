package QuizGame;

import java.io.*; // use for input, output class
import java.net.*; // use for socket connecting
import java.util.*; // use for quiz list array

public class QuizClient {
    public static void main(String[] args) {
      //use BufferedReader to read server_info.dat file
        try (BufferedReader configReader = new BufferedReader(new FileReader("server_info.dat"))) {
            String serverIP = configReader.readLine(); // read server IP address
            int serverPort = Integer.parseInt(configReader.readLine()); // read server port number

            Socket socket = new Socket(serverIP, serverPort); // make socket connection with server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // set input stream to read server's data
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true); // output stream for send data to server
            Scanner scanner = new Scanner(System.in); //user input

            String serverMessage; // save message to send server
            while ((serverMessage = input.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.startsWith("QUESTION: ")) { // message from server start with question
                    System.out.print("Your answer: "); 
                    String answer = scanner.nextLine(); // read answer user input
                    output.println(answer); // send answer to server
                }
            }
            socket.close(); // close the socket connection after communication is done
        } catch (IOException ex) {
            ex.printStackTrace(); //check the error and tell user what is the matter
        }
    }
}