package me.kevcar.viewmodel;

import me.kevcar.Sink;
import me.kevcar.Source;
import me.kevcar.model.RGB;
import rx.Subscriber;
import rx.subjects.BehaviorSubject;

public class ColorPickerViewModel {

    // Sources

    @Source("RGB")
    public BehaviorSubject<RGB> rgbSubject = BehaviorSubject.create(new RGB(0, 0, 0));

    // Sinks (write)

    @Sink("RED")
    public Subscriber<Integer> redSubscriber = new BaseSubscriber() {
        @Override
        public void onNext(Integer integer) {
            RGB updated = rgbSubject.getValue()
                    .toBuilder()
                    .setRed(integer)
                    .build();
            rgbSubject.onNext(updated);
        }
    };

    @Sink("GREEN")
    public Subscriber<Integer> greenSubscriber = new BaseSubscriber() {
        @Override
        public void onNext(Integer integer) {
            RGB updated = rgbSubject.getValue()
                    .toBuilder()
                    .setGreen(integer)
                    .build();
            rgbSubject.onNext(updated);
        }
    };

    @Sink("BLUE")
    public Subscriber<Integer> blueSubscriber = new BaseSubscriber() {
        @Override
        public void onNext(Integer integer) {
            RGB updated = rgbSubject.getValue()
                    .toBuilder()
                    .setBlue(integer)
                    .build();
            rgbSubject.onNext(updated);
        }
    };

    // Private

    private class BaseSubscriber extends Subscriber<Integer> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Integer integer) {
        }
    }

}
