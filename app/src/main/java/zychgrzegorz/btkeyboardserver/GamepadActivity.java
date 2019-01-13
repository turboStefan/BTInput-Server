package zychgrzegorz.btkeyboardserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class GamepadActivity extends Activity {

    private Integer pressedButtons = BUTTON_CODES.NONE.code;
    private final int STICK_THRESHOLD = 100;
    private float leftStartX,rightStartY,leftStartY,rightStartX;
    private float leftStickX=0,leftStickY=0,rightStickX=0,rightStickY=0;

    public void setPressedButtons(Integer pressedButtons) {
        this.pressedButtons = pressedButtons;
        ConnectActivity.getThisActivity().changeCharacteristicValue(
                ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS.Buttons,
                String.valueOf(pressedButtons));
    }

    public enum BUTTON_CODES{
        A(4),B(8),X(16),Y(32),MENU(1),VIEW(2),DPadDown(128),DPadLeft(256),DPadRight(512),DPadUp(64),
        LB(1024),RB(2048),LeftThumbstick(4096),RightThumbstick(8192),NONE(0)
        ;

        private int code;
        private BUTTON_CODES(int i){
            this.code = i;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepad);

        setupListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        ((Button)findViewById(R.id.button1)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.Y.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.Y.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.button2)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.B.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.B.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.button3)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.A.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.A.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.button4)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.X.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.X.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.povUp)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.DPadUp.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.DPadUp.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.povRight)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.DPadRight.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.DPadRight.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.povDown)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.DPadDown.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.DPadDown.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.povLeft)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.DPadLeft.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.DPadLeft.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.lbButton)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.LB.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.LB.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.rbButton)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.RB.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.RB.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.selectButton)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.VIEW.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.VIEW.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.startButton)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setPressedButtons(pressedButtons | BUTTON_CODES.MENU.code);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setPressedButtons(pressedButtons & (Integer.MAX_VALUE-BUTTON_CODES.MENU.code));
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.rtButton)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setRT(1);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setRT(0);
                        break;
                }
                return false;
            }
        });

        ((Button)findViewById(R.id.ltButton)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        setLT(1);
                        break;
                    case(MotionEvent.ACTION_UP):
                        setLT(0);
                        break;
                }
                return false;
            }
        });

        setupAnalogSticks();


    }

    private void setupAnalogSticks() {
        setupRightStick();
        setupLeftStick();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupLeftStick() {
        (findViewById(R.id.leftStick)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_UP):
                        view.setTranslationY(0);
                        view.setTranslationX(0);
                        break;
                    case (MotionEvent.ACTION_DOWN):
                        leftStartX = view.getX() + motionEvent.getX();
                        leftStartY = view.getY() + view.getHeight()/2 + motionEvent.getY();

                    case(MotionEvent.ACTION_MOVE):
                        float x = motionEvent.getRawX() - leftStartX;
                        float y = motionEvent.getRawY() - leftStartY;

                        view.setTranslationX(x);
                        view.setTranslationY(y);

                        if(view.getTranslationX()> STICK_THRESHOLD)
                            view.setTranslationX(STICK_THRESHOLD);
                        else if(view.getTranslationX()< -STICK_THRESHOLD)
                            view.setTranslationX(-STICK_THRESHOLD);
                        if(view.getTranslationY()>STICK_THRESHOLD)
                            view.setTranslationY(STICK_THRESHOLD);
                        else if(view.getTranslationY()< -STICK_THRESHOLD)
                            view.setTranslationY(-STICK_THRESHOLD);
                        break;
                }

                setLeftStickX(view.getTranslationX()/STICK_THRESHOLD);
                setLeftStickY(-view.getTranslationY()/STICK_THRESHOLD);
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupRightStick() {

        ((Button)findViewById(R.id.rightStick)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_UP):
                        view.setTranslationY(0);
                        view.setTranslationX(0);
                        break;
                    case (MotionEvent.ACTION_DOWN):
                        rightStartX = view.getX() + motionEvent.getX();
                        rightStartY = view.getY() + view.getHeight()/2 + motionEvent.getY();

                    case(MotionEvent.ACTION_MOVE):
                        float x = motionEvent.getRawX() - rightStartX;
                        float y = motionEvent.getRawY() - rightStartY;

                        view.setTranslationX(x);
                        view.setTranslationY(y);

                        if(view.getTranslationX()> STICK_THRESHOLD)
                            view.setTranslationX(STICK_THRESHOLD);
                        else if(view.getTranslationX()< -STICK_THRESHOLD)
                            view.setTranslationX(-STICK_THRESHOLD);
                        if(view.getTranslationY()>STICK_THRESHOLD)
                            view.setTranslationY(STICK_THRESHOLD);
                        else if(view.getTranslationY()< -STICK_THRESHOLD)
                            view.setTranslationY(-STICK_THRESHOLD);
                        break;
                }

                setRightStickX(view.getTranslationX()/STICK_THRESHOLD);
                setRightStickY(-view.getTranslationY()/STICK_THRESHOLD);
                return false;
            }
        });
    }

    private void setRightStickY(float v) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS
        .RightThumbstickY,String.valueOf(v));
    }

    private void setRightStickX(float v) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS
                .RightThumbstickX,String.valueOf(v));
    }

    private void setLeftStickX(float v) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS
                .LeftThumbstickX,String.valueOf(v));
    }

    private void setLeftStickY(float v) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS
                .LeftThumbstickY,String.valueOf(v));
    }

    private void setRT(int i) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS
        .RightTrigger,String.valueOf(i));
    }
    private void setLT(int i) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity.GAMEPAD_CHARACTERISTICS_UUIDS
                .LeftTrigger,String.valueOf(i));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
