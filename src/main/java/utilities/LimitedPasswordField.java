package utilities;

import javafx.scene.control.PasswordField;

public class LimitedPasswordField extends PasswordField {
    private int textLimit;


    public LimitedPasswordField(){
        super();
        this.textLimit = 16;
    }

    public void setTextLimit(int limit){
        this.textLimit = limit;
    }

    @Override
    public void replaceText(int start, int end,String text){
        super.replaceText(start,end,text);
        verify();
    }

    @Override
    public void replaceSelection(String text){
        super.replaceSelection(text);
        verify();
    }

    private void verify(){
        if(getText().length() > textLimit){
            setText(getText().substring(0,textLimit));
            positionCaret(textLimit);
        }
    }

}
