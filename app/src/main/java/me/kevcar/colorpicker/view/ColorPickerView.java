package me.kevcar.colorpicker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.widget.RxSeekBar;

import me.kevcar.Plumb;
import me.kevcar.Sink;
import me.kevcar.Source;
import me.kevcar.colorpicker.R;
import me.kevcar.model.RGB;
import me.kevcar.viewmodel.ColorPickerViewModel;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

public class ColorPickerView extends RelativeLayout {

    // ViewModel

    private ColorPickerViewModel viewModel = new ColorPickerViewModel();

    // Views

    private ImageView target;
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;

    @Source("RED")
    Observable<Integer> redChanges;

    @Source("GREEN")
    Observable<Integer> greenChanges;

    @Source("BLUE")
    Observable<Integer> blueChanges;

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

        redChanges = RxSeekBar.changes(red);
        greenChanges = RxSeekBar.changes(green);
        blueChanges = RxSeekBar.changes(blue);

        subscriptions.add(Plumb.join(this, viewModel));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscriptions.clear();
    }

    // Sink implementations
    @Sink("RGB")
    Observer<RGB> rgbOnNext = new Observer<RGB>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(RGB rgb) {
            int color = Color.rgb(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
            target.setBackgroundColor(color);
        }
    };
}
