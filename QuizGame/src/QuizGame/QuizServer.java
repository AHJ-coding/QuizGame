package QuizGame;

import java.io.*; // use for input, output class
import java.net.*; // use for socket connecting
import java.util.*; // use for quiz list array

//main class, Server processing client requests
public class QuizServer {
 public static List<String[]> questions = Arrays.asList( // make array static list to store question and answer value
         new String[]{"Who is the most handsome man in Gachon university? Hint : His first name is Choi, Last name is Jaehyuk", "JaehyukChoi"}, // first value is question, second is answer
         new String[]{"What is the capital of South Korea?", "Seoul"},
         new String[]{"Who is the world's best soccer player? Hint : not Ronaldo", "Messi"},
         new String[]{"What is 20 + 80?", "100"},
         new String[]{"What is the gender of IU (korean famous singer)? Male or Female", "Female"}
 );

//start server program
 public static void main(String[] args) {
     try (ServerSocket serverSocket = new ServerSocket(2000)) { //open server socket in 5000 port number
         System.out.println("Server is waiting on port 2000... Enter 'exit' to shut down server"); // check either server is waiting

         // Create a thread for listening to server shutdown command
         Thread shutdownThread = new Thread(() -> {
             try (Scanner scanner = new Scanner(System.in)) {
                 while (true) {
                     String command = scanner.nextLine();
                     if (command.equalsIgnoreCase("exit")) { //user enter exit, server shutdown
                         System.out.println("Shutting down the server...");
                         System.exit(0); // Terminate the program
                     }
                 }
             }
         });
         shutdownThread.start();

       
         while (true) { //infinite loop to make server always waiting for client
             Socket socket = serverSocket.accept(); // if client request to server, accept it and make new socket object
             new QuizThread(socket).start(); // make new thread to process client request name QuizThread
         }
     } catch (IOException ex) {
         ex.printStackTrace(); //check the error and tell user what is the matter
     }
 }
}
//thread
class QuizThread extends Thread { // processing communication with clients independently
 private Socket socket; //variable to store socket connected client

 public QuizThread(Socket socket) { // manage each connection with client
     this.socket = socket;
 }

//method when thread is running
 public void run() {
     try (
         BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //input stream for receive data from client, use BufferReader to read it in line unit
         PrintWriter output = new PrintWriter(socket.getOutputStream(), true) // output stream setting, use true to send data to client immediately
     ) {
         int score = 0; // initialize total score variable to 0
         for (String[] question : QuizServer.questions) { // ask all question in server turn by turn
             output.println("QUESTION: " + question[0]); // use "QUESTION: " format to recognize it is question
             String response = input.readLine(); // read answer from client
             if (response != null && response.equalsIgnoreCase(question[1])) { //check answer is same using Case-insensitive comparison
                 output.println("Correct!"); // send message to client it is correct
                 score++; // plus the score
             } else {
                 output.println("Incorrect!"); // send message it is incorrect
             }
         }
         output.println("Your final score is: " + score); // after all question end, check the total score and send it to client
     } catch (IOException ex) {
         ex.printStackTrace(); //check the error and tell user what is the matter
     } finally {
         try {
             socket.close(); // close the socket connection with client
         } catch (IOException e) {
             e.printStackTrace(); //check the error and tell user what is the matter
         }
     }
 }
}