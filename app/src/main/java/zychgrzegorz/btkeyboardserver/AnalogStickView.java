package zychgrzegorz.btkeyboardserver;

import android.content.Context;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.AppCompatButton;

public class AnalogStickView extends AppCompatButton {
    public AnalogStickView(Context context) {
        super(context);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case(MotionEvent.ACTION_DOWN):
                this.setTranslationX(event.getX());
                this.setTranslationY(event.getY());
                break;
            case(MotionEvent.ACTION_UP):
                this.setTranslationY(0);
                this.setTranslationX(0);
                break;
        }


        return super.onTouchEvent(event);
    }



}

