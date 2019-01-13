package zychgrzegorz.btkeyboardserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class InputChoiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_choice);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void clickKeyboard(View view){

        try {
            ConnectActivity.getThisActivity().setMode(ConnectActivity.MODES.KEYBOARD);
            startActivity(new Intent(this,KeyboardActivity.class));
        } catch (InvocationTargetException e) {
            Toast.makeText(this, "No response from desktop app!",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void clickGamepad(View view){

        try {
            ConnectActivity.getThisActivity().setMode(ConnectActivity.MODES.GAMEPAD);
            startActivity(new Intent(this,GamepadActivity.class));
        } catch (InvocationTargetException e) {
            Toast.makeText(this, "No response from desktop app!",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void clickMouse(View view){
        try {
            ConnectActivity.getThisActivity().setMode(ConnectActivity.MODES.MOUSE);
            startActivity(new Intent(this,MouseActivity.class));
        } catch (InvocationTargetException e) {
            Toast.makeText(this, "No response from desktop app!",
                    Toast.LENGTH_LONG).show();
        }

    }

}
