package kevcar.me.colorpicker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import kevcar.me.colorpicker.R;
import me.kevcar.RGB;

public class ColorPickerView extends LinearLayout {

    // Cached views

    private ImageView target;
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;

    // Members

    private SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            RGB.Builder builder = new RGB.Builder()
                                        .setRed(red.getProgress())
                                        .setGreen(green.getProgress())
                                        .setBlue(blue.getProgress());
            setColor(builder.build());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // no-op
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // no-op
        }
    };

    // Constructor

    public ColorPickerView(Context context) {
        super(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Lifecycle

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        target = (ImageView) findViewById(R.id.target);
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);

        // Set listener
        red.setOnSeekBarChangeListener(listener);
        green.setOnSeekBarChangeListener(listener);
        blue.setOnSeekBarChangeListener(listener);

        // Set initial progress
        red.setProgress(0);
        green.setProgress(0);
        blue.setProgress(0);
    }

    // Private

    private void setColor(RGB rgb) {
        int color = Color.rgb(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        target.setBackgroundColor(color);
    }
}
