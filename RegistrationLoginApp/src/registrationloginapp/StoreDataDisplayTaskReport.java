package registrationloginapp;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;

public class StoreDataDisplayTaskReport {

    // PART 3 ARRAYS - this is where data is stored
    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    // Add a message to the correct array
    public static void addMessage(Message m, String flag) {
        messageIDs.add(m.messageID);
        messageHashes.add(m.messageHash);
        
        // FIXED: Used equalsIgnoreCase so "Sent", "sent", "SENT" all work (No hard coding)
        if (flag.equalsIgnoreCase("Sent")) {
            sentMessages.add(m);
        } else if (flag.equalsIgnoreCase("Stored")) {
            storedMessages.add(m);
            saveStoredToJSON();
        } else if (flag.equalsIgnoreCase("Disregard")) {
            disregardedMessages.add(m);
        }
    }

    // a) Display stored sender / recipient
    public static String displayStoredSenderRecipient() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        
        String out = "";
        for (Message m : storedMessages) {
            // FIXED: Added the sender/developer if you have it, but using recipient as requested
            out += "Recipient: " + m.recipient + " | Message: " + m.messageText + "\n";
        }
        return out;
    }

    // b) Longest message
    public static String displayLongestMessage() {
        if (storedMessages.isEmpty()) return "No stored messages";
        
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return longest.messageText;
    }

    // c) Search by Message ID
    public static String searchByMessageID(String id) {
        // Check Sent messages first
        for (Message m : sentMessages) {
            if (m.messageID.equalsIgnoreCase(id)) return m.messageText;
        }
        // Then check Stored messages
        for (Message m : storedMessages) {
            if (m.messageID.equalsIgnoreCase(id)) return m.messageText;
        }
        return "Message not found";
    }

    // d) Search by Recipient
    public static String searchByRecipient(String recip) {
        String out = "";
        // Check Stored messages
        for (Message m : storedMessages) {
            if (m.recipient.equalsIgnoreCase(recip)) {
                out += m.messageText + "\n";
            }
        }
        // Check Sent messages (The assignment test data requires this!)
        for (Message m : sentMessages) {
            if (m.recipient.equalsIgnoreCase(recip)) {
                out += m.messageText + "\n";
            }
        }
        if (out.isEmpty()) return "No messages found for this recipient.";
        return out.trim();
    }

    // e) Delete by Hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageHash.equalsIgnoreCase(hash)) {
                String msg = storedMessages.get(i).messageText;
                storedMessages.remove(i);
                saveStoredToJSON(); // Re-save the file after deleting
                return "Message: \"" + msg + "\" successfully deleted.";
            }
        }
        return "Message not found";
    }

    // f) Display Report
    public static String displayReport() {
        if (sentMessages.isEmpty()) return "No sent messages to report.";
        
        String out = "==== SENT MESSAGES REPORT ====\n";
        for (Message m : sentMessages) {
            out += "Message Hash: " + m.messageHash + "\n";
            out += "Recipient: " + m.recipient + "\n";
            out += "Message: " + m.messageText + "\n\n";
        }
        return out;
    }

    // JSON FILE - saving
    public static void saveStoredToJSON() {
        try {
            String json = "[\n";
            for (int i = 0; i < storedMessages.size(); i++) {
                Message m = storedMessages.get(i);
                
                // FIXED: Escaped quotes in the message so the JSON file doesn't break
                String safeMessage = m.messageText.replace("\"", "\\\"");
                
                json += "  {\"id\":\"" + m.messageID + "\", \"hash\":\"" + m.messageHash + 
                        "\", \"recipient\":\"" + m.recipient + "\", \"message\":\"" + safeMessage + "\"}";
                
                if (i < storedMessages.size() - 1) json += ",";
                json += "\n";
            }
            json += "]";
            
            Files.write(Paths.get("storedMessages.json"), json.getBytes());
        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    // JSON FILE - loading (FIXED: Actually reads the file now!)
    public static void loadStoredFromJSON() {
        try {
            Path path = Paths.get("storedMessages.json");
            if (!Files.exists(path)) return;

            storedMessages.clear(); // Clear old data so we don't duplicate
            
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.contains("\"id\":") && line.contains("\"hash\":")) {
                    // Extract data using split
                    String id = line.split("\"id\":\"")[1].split("\"")[0];
                    String hash = line.split("\"hash\":\"")[1].split("\"")[0];
                    String recipient = line.split("\"recipient\":\"")[1].split("\"")[0];
                    String msgPart = line.split("\"message\":\"")[1];
                    String message = msgPart.substring(0, msgPart.lastIndexOf("\""));

                    // Recreate the Message object
                    Message loadedMsg = new Message();
loadedMsg.messageID = id;
loadedMsg.messageHash = hash;
loadedMsg.recipient = recipient;
loadedMsg.messageText = message;}
            }
            System.out.println("Stored messages loaded from JSON successfully.");
        } catch (Exception e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }

    // Clear all arrays (Useful for Unit Tests!)
    public static void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }
}