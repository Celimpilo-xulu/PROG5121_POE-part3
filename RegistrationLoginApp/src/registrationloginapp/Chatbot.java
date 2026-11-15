package registrationloginapp;

import java.util.Scanner;

public class Chatbot {

    private int totalMessagesSent = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RegistrationLogin login = new RegistrationLogin();

        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter your first name: ");
        String first = sc.nextLine();
        System.out.print("Enter your last name: ");
        String last = sc.nextLine();
        System.out.print("Enter your username: ");
        String user = sc.nextLine();
        System.out.print("Enter your password: ");
        String pass = sc.nextLine();
        System.out.print("Enter your cell phone number (+27...): ");
        String cell = sc.nextLine();

        String result = login.registerUser(first, last, user, pass, cell);
        System.out.println(result);

        if (result.contains("successfully captured")) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Enter your username: ");
            String loginUser = sc.nextLine();
            System.out.print("Enter your password: ");
            String loginPass = sc.nextLine();
            boolean loggedIn = login.loginUser(loginUser, loginPass);
            System.out.println(login.returnLoginStatus(loggedIn));

            if (loggedIn) {
                Chatbot chat = new Chatbot();
                chat.start(first);
            }
        }
    }

    public void start(String currentUser) {
        Scanner sc = new Scanner(System.in);
        StoreDataDisplayTaskReport.loadStoredFromJSON();
        System.out.println("Welcome to QuickChat, " + currentUser);

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Send Message");
            System.out.println("2. Show Sent Messages");
            System.out.println("3. Show Longest Message");
            System.out.println("4. Search by Message ID");
            System.out.println("5. Search by Recipient");
            System.out.println("6. Delete by Hash");
            System.out.println("7. Show Stored Messages");
            System.out.println("8. Quit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                sendMessage(sc, currentUser);
            } else if (choice.equals("2")) {
                System.out.println(StoreDataDisplayTaskReport.displayReport());
            } else if (choice.equals("3")) {
                System.out.println(StoreDataDisplayTaskReport.displayLongestMessage());
            } else if (choice.equals("4")) {
                System.out.print("Enter Message ID: ");
                String id = sc.nextLine();
                System.out.println(StoreDataDisplayTaskReport.searchByMessageID(id));
            } else if (choice.equals("5")) {
                System.out.print("Enter Recipient: ");
                String recip = sc.nextLine();
                System.out.println(StoreDataDisplayTaskReport.searchByRecipient(recip));
            } else if (choice.equals("6")) {
                System.out.print("Enter Hash: ");
                String hash = sc.nextLine();
                System.out.println(StoreDataDisplayTaskReport.deleteByHash(hash));
            } else if (choice.equals("7")) {
                System.out.println(StoreDataDisplayTaskReport.displayStoredSenderRecipient());
            } else if (choice.equals("8")) {
                System.out.println("Total messages sent: " + totalMessagesSent);
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void sendMessage(Scanner sc, String currentUser) {
        System.out.print("Enter recipient number (+27...): ");
        String recipient = sc.nextLine();

        System.out.print("Enter message (max 250 characters): ");
        String text = sc.nextLine();

        if (text.length() > 250 || text.trim().isEmpty()) {
            System.out.println("Message must be 1-250 characters.");
            return;
        }

        totalMessagesSent++;
        String id = "MSG" + totalMessagesSent;

        Message msg = new Message(id, "", recipient, text, "Sent");

        if (!msg.checkMessageID()) {
            System.out.println("Message ID too long.");
            return;
        }

        msg.createMessageHash(totalMessagesSent);

        System.out.println("1. Send  2. Disregard  3. Store");
        System.out.print("Choose: ");
        String opt = sc.nextLine();

        String flag = "Sent";
        if (opt.equals("2")) flag = "Disregard";
        else if (opt.equals("3")) flag = "Stored";
        msg.flag = flag;

        StoreDataDisplayTaskReport.addMessage(msg, flag);

        System.out.println("\nMessage done!");
        System.out.println("Sender: " + currentUser);
        System.out.println("ID: " + msg.messageID);
        System.out.println("Hash: " + msg.messageHash);
        System.out.println("Recipient: " + msg.recipient);
        System.out.println("Message: " + msg.messageText);
        System.out.println("Flag: " + msg.flag);
        System.out.println("Total messages sent: " + totalMessagesSent);
    }
}