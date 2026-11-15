package registrationloginapp;

import java.util.Scanner;

public class RegistrationLogin {

    public String savedUsername;
    public String savedPassword;
    public String savedFirstName;
    public String savedLastName;
    public String savedCell;

    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cell) {
        return cell != null && cell.startsWith("+27") && cell.length() == 12;
    }

    public String registerUser(String first, String last, String user, String pass, String cell) {
        if (!checkUserName(user)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(pass)) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number and a special character.";
        }
        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        savedFirstName = first;
        savedLastName = last;
        savedUsername = user;
        savedPassword = pass;
        savedCell = cell;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public boolean loginUser(String user, String pass) {
        if (savedUsername == null) return false;
        return user != null && user.equals(savedUsername) && pass != null && pass.equals(savedPassword);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + savedFirstName + ", " + savedLastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RegistrationLogin login = new RegistrationLogin();

        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter your first name: ");
        String first = sc.nextLine();
        System.out.print("Enter your second name: ");
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
        }
        sc.close();
    }
}