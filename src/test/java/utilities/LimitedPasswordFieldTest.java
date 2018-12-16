package utilities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testingResources.JfxTestRunner;


@RunWith(JfxTestRunner.class)
public class LimitedPasswordFieldTest {

    LimitedPasswordField testField;
    @Before
    public void initialize(){
         testField = new LimitedPasswordField();
         testField.setTextLimit(12);
    }

    @Test
    public void checkIfTooLongInsertIsTrimmed(){
        testField.clear();
        testField.insertText(0,"01234567890 01234567890");
        Assert.assertNotEquals(testField.getText(),"01234567890 01234567890");
    }

    @Test
    public void checkIfTooShortInsertIsNotTrimmed(){
        testField.clear();
        testField.insertText(0,"0123456789");
        Assert.assertEquals("0123456789",testField.getText());
    }

}