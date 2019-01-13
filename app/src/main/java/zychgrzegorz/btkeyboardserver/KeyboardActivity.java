package zychgrzegorz.btkeyboardserver;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import java.util.HashSet;

public class KeyboardActivity extends Activity {

    private HashSet<Short> pressedButtons;
    private boolean shiftPressed = false;
    private boolean altPressed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pressedButtons = new HashSet<>();
        setContentView(R.layout.activity_keyboard);


        KeyboardView keyboardView = findViewById(R.id.keyboardView);
        keyboardView.setKeyboard(new Keyboard(this,R.xml.qwerty_layout_sc));
        keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {

                switch(i){
                    case 0x2A:
                        pressShift();
                        break;
                    case 0x38:
                        pressAlt();
                        break;
                    default:
                        buttonPressed((short)i);
                        break;
                }
            }

            @Override
            public void onRelease(int i) {
                if(i!=0x2A&&i!=0x38)
                buttonReleased((short)i);
            }

            @Override
            public void onKey(int i, int[] ints) {

            }

            @Override
            public void onText(CharSequence charSequence) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });
    }

    private void pressAlt() {
        if(shiftPressed){
            pressedButtons.remove((short)0x38);
            altPressed =false;
        }
        else{
            pressedButtons.add((short)0x38);
            altPressed =true;
        }
    }

    private void pressShift() {
        if(shiftPressed){
            pressedButtons.remove((short)0x2A);
            shiftPressed =false;
        }
        else{
            pressedButtons.add((short)0x2A);
            shiftPressed =true;
        }
    }

    private void buttonPressed(short i) {
        pressedButtons.add(i);
        ConnectActivity.getThisActivity().changeKeyboardCharacteristicValue(pressedButtons);
    }

    private void buttonReleased(short i) {
        pressedButtons.remove(i);
        ConnectActivity.getThisActivity().changeKeyboardCharacteristicValue(pressedButtons);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
