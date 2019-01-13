package zychgrzegorz.btkeyboardserver;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class KeyboardButton extends AppCompatButton {
    public int codes;
    private int priv;
    public KeyboardButton(Context context) {
        super(context);
    }

    public KeyboardButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public KeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
