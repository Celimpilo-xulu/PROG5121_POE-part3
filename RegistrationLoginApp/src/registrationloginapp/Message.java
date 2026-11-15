package registrationloginapp;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Message {
    public String messageID;
    public String messageHash;
    public String recipient;
    public String messageText;
    public String flag;

    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    public Message() {
    }

    public Message(String id, String hash, String recipient, String text, String flag) {
        this.messageID = id;
        this.messageHash = hash;
        this.recipient = recipient;
        this.messageText = text;
        this.flag = flag;
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String createMessageHash(int num) {
        String[] words = messageText.trim().split(" ");
        String first = words[0];
        String last = words[words.length - 1];
        this.messageHash = messageID.substring(0, 2) + ":" + num + ":" + first.toUpperCase() + last.toUpperCase();
        return this.messageHash;
    }

    public void addToArrays() {
        if (flag == null) flag = "Sent";
        
        if (flag.equalsIgnoreCase("Sent")) {
            sentMessages.add(this);
        } else if (flag.equalsIgnoreCase("Disregard")) {
            disregardedMessages.add(this);
        } else if (flag.equalsIgnoreCase("Stored")) {
            storedMessages.add(this);
            saveStoredToJSON();
        }
        messageHashes.add(messageHash);
        messageIDs.add(messageID);
    }

    public static void saveStoredToJSON() {
        try {
            FileWriter w = new FileWriter("storedMessages.json");
            w.write("[\n");
            for (int i = 0; i < storedMessages.size(); i++) {
                Message m = storedMessages.get(i);
                // Escaping quotes to prevent breaking the JSON
                String safeMsg = m.messageText.replace("\"", "\\\"");
                w.write("  {\"id\":\"" + m.messageID + "\", \"hash\":\"" + m.messageHash + "\", \"recipient\":\"" + m.recipient + "\", \"message\":\"" + safeMsg + "\"}");
                if (i < storedMessages.size() - 1) w.write(",");
                w.write("\n");
            }
            w.write("]\n");
            w.close();
        } catch (Exception e) {
            System.out.println("Could not save JSON: " + e.getMessage());
        }
    }

    // FIXED: This actually reads the file and populates the arrays now
    public static void loadStoredFromJSON() {
        try {
            File f = new File("storedMessages.json");
            if (!f.exists()) return;
            
            storedMessages.clear(); // Clear old data before loading
            List<String> lines = Files.readAllLines(f.toPath());
            
            for (String line : lines) {
                if (line.contains("\"id\":") && line.contains("\"hash\":")) {
                    String id = line.split("\"id\":\"")[1].split("\"")[0];
                    String hash = line.split("\"hash\":\"")[1].split("\"")[0];
                    String recip = line.split("\"recipient\":\"")[1].split("\"")[0];
                    String msgPart = line.split("\"message\":\"")[1];
                    String msg = msgPart.substring(0, msgPart.lastIndexOf("\""));
                    
                    // Recreate the message using your 5-argument constructor
                    Message loaded = new Message(id, hash, recip, msg, "Stored");
                    storedMessages.add(loaded);
                    messageIDs.add(id);
                    messageHashes.add(hash);
                }
            }
            System.out.println("Successfully loaded " + storedMessages.size() + " messages from JSON.");
        } catch (Exception e) {
            System.out.println("Error loading JSON: " + e.getMessage());
        }
    }

    public static String displayStoredSenderRecipient() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        String out = "";
        for (Message m : storedMessages) {
            out += "Recipient: " + m.recipient + " | Message: " + m.messageText + "\n";
        }
        return out;
    }

    // FIXED: Changed to check storedMessages instead of sentMessages
    public static String getLongestMessage() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return "Longest: " + longest.messageText + " | To: " + longest.recipient;
    }

    // FIXED: Searches both Stored and Sent messages
    public static String searchByID(String id) {
        for (Message m : storedMessages) {
            if (m.messageID.equals(id)) return "Found: To " + m.recipient + " - " + m.messageText;
        }
        for (Message m : sentMessages) {
            if (m.messageID.equals(id)) return "Found: To " + m.recipient + " - " + m.messageText;
        }
        return "Message ID not found.";
    }

    // FIXED: Searches both Stored and Sent messages (Required by Unit Tests)
    public static String searchByRecipient(String recip) {
        String out = "";
        for (Message m : storedMessages) {
            if (m.recipient.equals(recip)) out += m.messageText + "\n";
        }
        for (Message m : sentMessages) {
            if (m.recipient.equals(recip)) out += m.messageText + "\n";
        }
        if (out.equals("")) return "No messages to this recipient.";
        return out.trim();
    }

    // FIXED: Deletes from storedMessages and updates the JSON file
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageHash.equals(hash)) {
                String deletedText = storedMessages.get(i).messageText;
                storedMessages.remove(i);
                saveStoredToJSON(); // Update JSON file!
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    // FIXED: Displays report based on Stored Messages (menu option is under Stored)
    public static String displayReport() {
        if (storedMessages.isEmpty()) return "No stored messages to report.";
        String out = "==== STORED MESSAGES REPORT ====\n";
        for (Message m : storedMessages) {
            out += "Hash: " + m.messageHash + " | To: " + m.recipient + " | Message: " + m.messageText + "\n";
        }
        return out;
    }
}