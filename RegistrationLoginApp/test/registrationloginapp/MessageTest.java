 package registrationloginapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testCheckMessageID_Valid() {
        Message m = new Message();
        m.messageID = "1234567890";
        assertTrue(m.checkMessageID());
    }

    @Test
    public void testCheckMessageID_TooLong() {
        Message m = new Message();
        m.messageID = "123456789012";
        assertFalse(m.checkMessageID());
    }

    @Test
    public void testCreateMessageHash() {
        Message m = new Message();
        m.messageID = "0012345678";
        m.messageText = "Hi Mike, can you join us for dinner tonight";
        // 00 = first 2 of ID, 0 = message number, HI + TONIGHT
        String hash = m.createMessageHash(0);
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testCreateMessageHash_SecondExample() {
        Message m = new Message();
        m.messageID = "1234567890";
        m.messageText = "Did you get the cake?";
        String hash = m.createMessageHash(0);
        assertEquals("12:0:DIDCAKE?", hash);
    }
}