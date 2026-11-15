package registrationloginapp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RegistrationLoginTest {

    private RegistrationLogin login;

    @Before
    public void setUp() {
        login = new RegistrationLogin();
    }

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08968976"));
    }

    @Test
    public void testRegistrationSuccessMessage() {
        String result = login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.", result);
    }

    @Test
    public void testLoginSuccessful() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongpass"));
    }

    @Test
    public void testSuccessfulLoginMessage() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus(true));
    }

    @Test
    public void testFailedLoginMessage() {
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }
}