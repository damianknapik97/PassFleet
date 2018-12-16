package utilities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repeat.Repeat;


public class PasswordGeneratorTest {

    PasswordGenerator passwordGenerator;
    @Before
    public void initialize(){
        passwordGenerator = new PasswordGenerator();
    }

    @Test
    @Repeat(times = 1000)
    public void checkIfPasswordsAreUnique(){
        Assert.assertNotEquals(passwordGenerator.generateNewPassword(5,false,false),
                passwordGenerator.generateNewPassword(5,false,false));
        Assert.assertNotEquals(passwordGenerator.generateNewPassword(5,true,false),
                passwordGenerator.generateNewPassword(5,true,false));
        Assert.assertNotEquals(passwordGenerator.generateNewPassword(5,true,true),
                passwordGenerator.generateNewPassword(5,true,true));
    }

    //Checking if passwords contain only desired chars from the alphabet
    @Test
    @Repeat(times = 1000)
    public void testFirstCaseOfPassword(){
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,false,false).toCharArray(), "qwertyuiopasdfghjklzxcvbnm"));
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,false,false).toCharArray(), "1234567890"));
        Assert.assertFalse(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(50,false,false).toCharArray(), "QWERTYUIOPASDFGHJKLZXCVBNM"));
        Assert.assertFalse(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(50,false,false).toCharArray(), "@%+/'!#$^?:.(){}[]~"));
    }

    @Test
    @Repeat(times = 1000)
    public void testSecondCaseOfPassword(){
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,false).toCharArray(), "qwertyuiopasdfghjklzxcvbnm"));
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,false).toCharArray(), "1234567890"));
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,false).toCharArray(), "QWERTYUIOPASDFGHJKLZXCVBNM"));
        Assert.assertFalse(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(50,true,false).toCharArray(), "@%+/'!#$^?:.(){}[]~"));
    }

    @Test
    @Repeat(times = 1000)
    public void testThirdCaseOfPassword(){
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,true).toCharArray(), "qwertyuiopasdfghjklzxcvbnm"));
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,true).toCharArray(), "1234567890"));
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,true).toCharArray(), "QWERTYUIOPASDFGHJKLZXCVBNM"));
        Assert.assertTrue(checkIfPasswordIsValid(passwordGenerator.generateNewPassword(5,true,true).toCharArray(), "@%+/'!#$^?:.(){}[]~"));
    }


    private boolean checkIfPasswordIsValid(char[] password, String alphabet){
        char[] tmpCharsTable;
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

}