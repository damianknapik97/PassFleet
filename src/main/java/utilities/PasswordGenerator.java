package utilities;

import java.security.SecureRandom;

public class PasswordGenerator {

    private final String basicAlphabet = "qwertyuiopasdfghjklzxcvbnm";
    private final String numberAlphabet = "0123456789";
    private final String upperCaseAlphabet = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private final String specialCharactersAlphabet = "@%+/'!#$^?:.(){}[]~";
    private final SecureRandom secureRandom = new SecureRandom();


    //The goal is to generate password and use at least one from every alphabet that user choose to include
    public String generateNewPassword(int numberOfChars, boolean useUpperCase, boolean useSpecialCharacters){

        //Creating alphabet desired by user
        String alphabet = basicAlphabet;
        alphabet = alphabet.concat(numberAlphabet);
        if(useUpperCase && useSpecialCharacters){
            alphabet = alphabet.concat(upperCaseAlphabet);
            alphabet = alphabet.concat(specialCharactersAlphabet);
        } else if( useUpperCase && !useSpecialCharacters){
            alphabet = alphabet.concat(upperCaseAlphabet);
        } else if(!useUpperCase && useSpecialCharacters){
            alphabet = alphabet.concat(specialCharactersAlphabet);
        }

        //Creating new password
        char[] charsTable = alphabet.toCharArray();
        char[] newPassword = new char[numberOfChars];
        for(int i =0 ; i<numberOfChars;i++){
            newPassword[i] = charsTable[secureRandom.nextInt(charsTable.length)];
        }



        return passwordChecker(newPassword,useUpperCase,useSpecialCharacters);
    }

    //Now we need to run verification to make sure that we draw character from every of the alphabets
    //If we find one char, we break the loop and return password
    //Recursion might not be the fastest solution but I got too invested to not implement it
    private String passwordChecker(char[] password, boolean useUpperCase, boolean useSpecialCharacters) {
        String verifiedPassword;

        //Checking password if every desired alphabet is included
        if(checkIfPasswordIsValid(password,basicAlphabet) && checkIfPasswordIsValid(password,numberAlphabet)
                && (checkIfPasswordIsValid(password,upperCaseAlphabet) == useUpperCase)
                && (checkIfPasswordIsValid(password,specialCharactersAlphabet) == useSpecialCharacters)) {
            //If true, return verified password
            verifiedPassword = String.valueOf(password);
        } else {
            // If false, try to repair password by inserting a random character from every desired Alphabet
            password = repairPassword(password,basicAlphabet);
            password = repairPassword(password,numberAlphabet);
            if(useUpperCase)
                password = repairPassword(password,upperCaseAlphabet);
            if(useSpecialCharacters)
                password = repairPassword(password,specialCharactersAlphabet);
            //And repeat process
            verifiedPassword = passwordChecker(password,useUpperCase,useSpecialCharacters);
        }
        //return password
        return verifiedPassword;
    }

    // Checking if alphabet is included
    // Upon finding that alphabet is included, returning true
    public boolean checkIfPasswordIsValid(char[] password, String alphabet){
        char[] tmpCharsTable;
        //Checking if there is at least one char from basicAlphabet
        tmpCharsTable = alphabet.toCharArray();
        for(int i=0; i<password.length;i++){
            for(int y =0; y<tmpCharsTable.length; y++){
                if(password[i] == tmpCharsTable[y]){
                    return true;
                }
            }
        }
        return false;
    }

    //Inserting random char from alphabet to random spot in password
    public char[] repairPassword(char[] password, String alphabet){
        password[secureRandom.nextInt(password.length)] = alphabet.charAt(secureRandom.nextInt(alphabet.length()));
        return password;
    }

}
