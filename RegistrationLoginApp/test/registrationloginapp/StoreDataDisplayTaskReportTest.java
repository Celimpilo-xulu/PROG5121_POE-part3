package registrationloginapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class StoreDataDisplayTaskReportTest {

    // Helper to make a message quickly
    public Message makeMsg(String id, String hash, String recip, String text) {
        Message m = new Message();
        m.messageID = id;
        m.messageHash = hash;
        m.recipient = recip;
        m.messageText = text;
        return m;
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        StoreDataDisplayTaskReport.clearAll();

        Message m1 = makeMsg("1234567890", "12:0:DIDCAKE?", "+27834557896", "Did you get the cake?");
        StoreDataDisplayTaskReport.addMessage(m1, "Sent");

        Message m4 = makeMsg("0838884567", "08:1:ITTIME!", "0838884567", "It is dinner time !");
        StoreDataDisplayTaskReport.addMessage(m4, "Sent");

        assertEquals(2, StoreDataDisplayTaskReport.sentMessages.size());
        assertEquals("Did you get the cake?", StoreDataDisplayTaskReport.sentMessages.get(0).messageText);
        assertEquals("It is dinner time !", StoreDataDisplayTaskReport.sentMessages.get(1).messageText);
    }

    @Test
    public void testLongestMessage() {
        StoreDataDisplayTaskReport.clearAll();

        Message m2 = makeMsg("1111111111", "11:0:WHERETIME.", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        StoreDataDisplayTaskReport.addMessage(m2, "Stored");

        Message m5 = makeMsg("2222222222", "22:0:OKYOU.", "+27838884567", "Ok, I am leaving without you.");
        StoreDataDisplayTaskReport.addMessage(m5, "Stored");

        assertEquals("Where are you? You are late! I have asked you to be on time.", StoreDataDisplayTaskReport.displayLongestMessage());
    }

    @Test
    public void testSearchByMessageID() {
        StoreDataDisplayTaskReport.clearAll();

        Message m4 = makeMsg("0838884567", "08:1:ITTIME!", "0838884567", "It is dinner time !");
        StoreDataDisplayTaskReport.addMessage(m4, "Sent");

        assertEquals("It is dinner time !", StoreDataDisplayTaskReport.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchByRecipient() {
        StoreDataDisplayTaskReport.clearAll();

        Message m2 = makeMsg("1111111111", "11:0:WHERETIME.", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        StoreDataDisplayTaskReport.addMessage(m2, "Stored");

        Message m5 = makeMsg("2222222222", "22:0:OKYOU.", "+27838884567", "Ok, I am leaving without you.");
        StoreDataDisplayTaskReport.addMessage(m5, "Stored");

        String result = StoreDataDisplayTaskReport.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByHash() {
        StoreDataDisplayTaskReport.clearAll();

        Message m2 = makeMsg("1111111111", "11:0:WHERETIME.", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        StoreDataDisplayTaskReport.addMessage(m2, "Stored");

        String result = StoreDataDisplayTaskReport.deleteByHash("11:0:WHERETIME.");
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
    }

    @Test
    public void testDisplayReport() {
        StoreDataDisplayTaskReport.clearAll();

        Message m1 = makeMsg("1234567890", "12:0:DIDCAKE?", "+27834557896", "Did you get the cake?");
        StoreDataDisplayTaskReport.addMessage(m1, "Sent");

        String report = StoreDataDisplayTaskReport.displayReport();
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
        assertTrue(report.contains("Did you get the cake?"));
    }
}