package utilities;

import database.Database;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EncryptorTest {

    private String testString1;
    private String testString2;
    private String testString3;
    private String testString4;

    @Before
    public void initialize(){
        Encryptor.getInstance().setKey("test");
        testString1 = "RandomWord";
        testString2 = "a";
        testString3 = "!@##$%#%)(@#%*";
        testString4 = " ";
    }

    @Test
    public void encrypt() throws Exception {
        Assert.assertNotEquals(Encryptor.getInstance().encrypt(testString1),testString1);
        Assert.assertNotEquals(Encryptor.getInstance().encrypt(testString2),testString2);
        Assert.assertNotEquals(Encryptor.getInstance().encrypt(testString3),testString3);
        Assert.assertNotEquals(Encryptor.getInstance().encrypt(testString4),testString4);

    }

    @Test
    public void decrypt() throws Exception {

        Assert.assertEquals(Encryptor.getInstance().decrypt(Encryptor.getInstance().encrypt(testString1)),testString1);
        Assert.assertEquals(Encryptor.getInstance().decrypt(Encryptor.getInstance().encrypt(testString2)),testString2);
        Assert.assertEquals(Encryptor.getInstance().decrypt(Encryptor.getInstance().encrypt(testString3)),testString3);
        Assert.assertEquals(Encryptor.getInstance().decrypt(Encryptor.getInstance().encrypt(testString4)),testString4);
    }
}