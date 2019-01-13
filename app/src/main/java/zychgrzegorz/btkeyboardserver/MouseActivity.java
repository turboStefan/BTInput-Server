package zychgrzegorz.btkeyboardserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashMap;

public class MouseActivity extends Activity {

    private float Y = 0;
    private float X = 0;

    private enum BUTTONS{
        LMB_DOWN(2),LMB_UP(4),RMB_DOWN(8),RMB_UP(16);

        private int code;
        private BUTTONS(int i){
            this.code = i;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        findViewById(R.id.touchpadView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        X = motionEvent.getRawX();
                        Y = motionEvent.getRawY();
                        break;
                    case(MotionEvent.ACTION_MOVE):
                        setDeltaX((int)(motionEvent.getRawX()-X));
                        setDeltaY((int)(motionEvent.getRawY()-Y));
                        X = motionEvent.getRawX();
                        Y = motionEvent.getRawY();
                        break;
                    case(MotionEvent.ACTION_UP):
                        setDeltaX(0);
                        setDeltaY(0);
                        break;

                }
                return false;
            }
        });

        findViewById(R.id.lmb).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity
                                .MOUSE_CHARACTERISTICS_UUIDS.MouseOptions,
                                String.valueOf(BUTTONS.LMB_DOWN.code));
                        break;
                    case(MotionEvent.ACTION_UP):
                        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity
                                .MOUSE_CHARACTERISTICS_UUIDS.MouseOptions,
                                String.valueOf(BUTTONS.LMB_UP.code));
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.rmb).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case(MotionEvent.ACTION_DOWN):
                        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity
                                        .MOUSE_CHARACTERISTICS_UUIDS.MouseOptions,
                                String.valueOf(BUTTONS.RMB_DOWN.code));
                        break;
                    case(MotionEvent.ACTION_UP):
                        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity
                                        .MOUSE_CHARACTERISTICS_UUIDS.MouseOptions,
                                String.valueOf(BUTTONS.RMB_UP.code));
                        break;
                }
                return false;
            }
        });
    }

    private void setDeltaY(int v) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity
        .MOUSE_CHARACTERISTICS_UUIDS.DeltaY,String.valueOf(v));
    }

    private void setDeltaX(int v) {
        ConnectActivity.getThisActivity().changeCharacteristicValue(ConnectActivity
                .MOUSE_CHARACTERISTICS_UUIDS.DeltaX,String.valueOf(v));
    }


}
