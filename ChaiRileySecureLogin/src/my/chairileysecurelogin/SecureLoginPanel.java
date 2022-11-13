/*
 * Author: Riley Chai
 * Class: ICS4U
 * Program: Secure Login
 */
package my.chairileysecurelogin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 335480661
 */
public class SecureLoginPanel extends javax.swing.JPanel {

    /**
     * Creates new form SecureLoginPanel
     *
     * @throws java.io.FileNotFoundException
     */
    private static File listOfUsers = new File("Users.txt");//Creates the file object
    private static List<ArrayList<String>> userList;//A 2D array list that stores the user's username, email and password.
    private static int curUser;//Keeps track of the current user.

    public SecureLoginPanel() {
        initComponents();
        try {
            readFile();//Reads the file when the program starts.          
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecureLoginPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads the file contents into array lists.
     *
     * @throws FileNotFoundException
     */
    static public void readFile() throws FileNotFoundException {
        userList = new ArrayList<>();//A 2D array list that stores the users username, email and password.
        curUser = 0;//Keeps track of the current user.
        Scanner input = new Scanner(listOfUsers);//Initializes the scanner for the file.

        //Stores the information from the file into array lists.
        while (input.hasNext()) {//Loops while there are still more users in the file to check.
            StringTokenizer curUserInfo = new StringTokenizer(input.nextLine(), ",");//Tokenizes the current line of user info with a comma delimiter.
            userList.add(new ArrayList());//Creates a new array list to store the current user info from the file.
            while (curUserInfo.hasMoreElements()) {//Loops while there are still more tokens to add to the array list.
                userList.get(curUser).add(curUserInfo.nextToken());//Adds the current token to the current user's array list.
            }
            curUser++;//Continues to the next user in the file.
        }
    }

    /**
     * Writes from the arrays to the file.
     *
     * @throws FileNotFoundException
     */
    static public void writeFile() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(listOfUsers);//Creates the writing object.
        //Loops through every array list and writes the data to the file.
        userList.forEach((user) -> {
            user.forEach((info) -> {
                output.print(info + ",");//Writes the current user info and adds a comma to act as the delimiter.
            });
            output.println("");//Moves to the next line.
        });
        output.close();//Closes the print writer and updates the file with the new information.
    }

    /**
     * Checks if the username and password matches an existing user in the file.
     *
     * @param username A string retrieved from the username text field.
     * @param password A string retrieved from the password text field.
     * @return correctLogin - A boolean denoting if the login information
     * matches an existing user(true) or not(false).
     * @throws java.io.FileNotFoundException
     * @throws java.security.NoSuchAlgorithmException
     */
    static public boolean checkLogin(String username, String password) throws FileNotFoundException, NoSuchAlgorithmException {
        boolean correctLogin = false;//If both the username and password match, set to true. Otherwise, set to false.

        //Reads through the array lists and check if the username and password match.
        for (int i = 0; i < userList.size(); i++) {//Loops through every user in the array list.
            if (userList.get(i).get(0).equals(username)) {//Checks if the username matches.
                if (userList.get(i).get(2).equals(digestPassword(password))) {//Checks if the password matches.
                    correctLogin = true;//If both match, login is successful.
                }
            }
        }
        return correctLogin;
    }

    /**
     * Ensures the username entered is valid.
     *
     * @param Username A string representing the user's username.
     * @return isValid - A boolean representing if the username is valid(true)
     * or not(false).
     */
    static public boolean verifyUsername(String Username) {
        boolean isValid = false;
        if (Username.length() > 0) {//Ensures the username is not empty
            isValid = true;
        }
        return isValid;
    }

    /**
     * Ensures the email entered is valid.
     *
     * @param Email A string representing the user's email.
     * @return isValid - A boolean representing if the email is valid(true) or
     * not(false).
     */
    static public boolean verifyEmail(String Email) {
        boolean isValid = false;
        if (Email.indexOf("@") > 0 && Email.indexOf(" ") == -1) {//Checks if there is an @ sign with non space characters preceding it.
            //Ensures there is a "." with a two or three letter domain following it.
            if (Email.indexOf(".") == (Email.length() - 3) || Email.indexOf(".") == (Email.length() - 4)) {
                if (Email.indexOf("@") < Email.indexOf(".") - 1) {//Ensures there is a mail server(text between the @ and domain). 
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    /**
     * Ensures the password entered is valid.
     *
     * @param Password A string representing the user's password.
     * @return isValid - A boolean representing if the password is valid(true)
     * or not(false).
     * @throws FileNotFoundException
     * @throws NoSuchElementException
     */
    static public boolean verifyPassword(String Password) throws FileNotFoundException, NoSuchElementException {
        boolean isValid = true;
        File badPasswords = new File("badPasswords.txt");//Creates the file object.
        Scanner input = new Scanner(badPasswords);//Initializes the scanner for the file.

        while (input.hasNext() && isValid == true) {//Loops through all the bad passwords in the file.
            String curBadPassword = input.nextLine();//Stores the current bad password in a string.
            if (Password.length() > 3) {//Ensures the password is more than 4 characters long.
                if (curBadPassword.equals(Password)) {//Checks if the user's password matches the bad password.
                    isValid = false;//If the new password matches a bad password, set isValid to false.
                }
            } else {//If the password is less than 4 characters.
                isValid = false;
            }
        }
        input.close();//Closes the scanner.
        return isValid;
    }

    /**
     * Adds a new user to the file.
     *
     * @param newUsername A string representing the user's username.
     * @param newEmail A string representing the user's email.
     * @param newPassword A string representing the user's password.
     * @throws FileNotFoundException
     * @throws NoSuchElementException
     * @throws NoSuchAlgorithmException
     */
    static public void newUser(String newUsername, String newEmail, String newPassword) throws FileNotFoundException, NoSuchElementException, NoSuchAlgorithmException {
        userList.add(new ArrayList<>());//Creates a new array list to store the information
        userList.get(curUser).add(newUsername);//Adds their username.
        userList.get(curUser).add(newEmail);//Adds their email.
        userList.get(curUser).add(digestPassword(newPassword));//Adds their password after it is encrypted using SHA-256.
        curUser++;//Increases the current index for the next time a user is registered.

        writeFile();//Re-writes the user data file.
    }

    /**
     * Encrypts the user's password using SHA-256.
     *
     * @param password A string representing the user's password.
     * @return digestedPassword - A string representing the user's encrypted
     * password.
     * @throws NoSuchAlgorithmException
     */
    static public String digestPassword(String password) throws NoSuchAlgorithmException {
        String digestedPassword = "";//Stores the encrypted password.
        //Creates the SHA-256 encryption object.
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        //Takes the password as bytes and updates the digest.
        md.update(password.getBytes());
        //Takes the digest and stores it as a byte array.
        byte byteData[] = md.digest();
        //Builds a new string of the digested password.
        for (int i = 0; i < byteData.length; i++) {
            digestedPassword += (Integer.toHexString(byteData[i] & 0xFF | 0x100).substring(1, 3));
        }
        return digestedPassword;//Returns the encrypted password.
    }

    /**
     * Checks if the username and email match an existing user on file. If both
     * match, updates the user's password on file.
     *
     * @param username A string representing the user's username.
     * @param email A string representing the user's email.
     * @param newPassword - A String representing the user's new password.
     * @return isValid - A boolean representing if the username and email
     * entered are valid(true) or not(false).
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException
     */
    static public boolean resetPassword(String username, String email, String newPassword) throws FileNotFoundException, NoSuchAlgorithmException {
        boolean isValid = false;
        for (int i = 0; i < userList.size(); i++) {//Loops through every user on file.
            if (userList.get(i).get(0).equals(username)) {//Checks if the username matches.
                if (userList.get(i).get(1).equals(email)) {//Checks if the email matches.
                    //If they both match, replace the current password with the new encrypted one.
                    userList.get(i).set(2, digestPassword(newPassword));
                    writeFile();//Re-writes the user data file.
                    isValid = true;//Password was successfully reset.
                }
            }
        }
        return isValid;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
