package me.kevcar.colorpicker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.widget.RxSeekBar;

import me.kevcar.colorpicker.R;
import me.kevcar.model.RGB;
import me.kevcar.viewmodel.ColorPickerViewModel;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class ColorPickerView extends RelativeLayout {

    // ViewModel

    private ColorPickerViewModel viewModel = new ColorPickerViewModel();

    // Views

    private ImageView target;
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;

    // Subscriptions

    private CompositeSubscription subscriptions = new CompositeSubscription();

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
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Sources
        subscriptions.add(RxSeekBar.changes(red).subscribe(viewModel.redSubscriber));
        subscriptions.add(RxSeekBar.changes(green).subscribe(viewModel.greenSubscriber));
        subscriptions.add(RxSeekBar.changes(blue).subscribe(viewModel.blueSubscriber));

        // Sinks
        subscriptions.add(viewModel.rgbSubject.subscribe(rgbOnNext));
    }

    @Override
    public void onDetachedFromWindow() {
        subscriptions.clear();
        super.onDetachedFromWindow();
    }

    // Sink implementations

    private Action1<RGB> rgbOnNext = new Action1<RGB>() {
        @Override
        public void call(RGB rgb) {
            int color = Color.rgb(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
            target.setBackgroundColor(color);
        }
    };
}
