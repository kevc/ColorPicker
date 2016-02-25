package me.kevcar;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Plumb {

    public static Subscription join(Object o1, Object o2) {
        List<Field> clz1sourcesAndSinks = Arrays.asList(o1.getClass().getDeclaredFields());
        List<Field> clz2sourcesAndSinks = Arrays.asList(o2.getClass().getDeclaredFields());

        Map<String, Observable> sources = new HashMap<>();
        Map<String, Observer> sinks = new HashMap<>();

        addSourcesAndSinks(o1, clz1sourcesAndSinks, sources, sinks);
        addSourcesAndSinks(o2, clz2sourcesAndSinks, sources, sinks);
        return connect(sources, sinks);
    }

    // Takes sources and sinks found in the Object and places them in the correct maps
    // for further processing.
    private static void addSourcesAndSinks(Object o, List<Field> clzSourcesAndSinks,
                                           Map<String, Observable> sourcesMap, Map<String, Observer> sinksMap) {
        for (Field f : clzSourcesAndSinks) {
            boolean isSource = f.isAnnotationPresent(Source.class);
            boolean isSink = f.isAnnotationPresent(Sink.class);

            if (isSource && isSink) {
                throw new IllegalStateException("Field " + f.getName() +
                        " is annotated with both @Source and @Sink");
            }

            if (isSink) {
                String value = f.getAnnotation(Sink.class).value();
                try {
                    Observer observer = (Observer) f.get(o);
                    sinksMap.put(value, observer);
                } catch (IllegalAccessException e) {
                    System.out.println("Something bad happened. Sinks\n" + e);
                }
            }
            else if (isSource) {
                String value = f.getAnnotation(Source.class).value();
                try {
                    Observable observable = (Observable) f.get(o);
                    sourcesMap.put(value, observable);
                } catch (IllegalAccessException e) {
                    System.out.println("Something bad happened. Sources\n" + e);
                }
            }
        }
    }

    // Takes all sources and sinks, and subscribes to the sinks to the sources changes when their
    // provided values match.
    // Not typesafe nor efficient, but it works.
    private static Subscription connect(Map<String, Observable> sources, Map<String, Observer> sinks) {
        CompositeSubscription subscriptions = new CompositeSubscription();
        for (Map.Entry<String, Observable> sourceEntry : sources.entrySet()) {
            String sourceName = sourceEntry.getKey();
            Observable sourceObservable = sourceEntry.getValue();

            for (Map.Entry<String, Observer> sinkEntry : sinks.entrySet()) {
                if (sinkEntry.getKey().equals(sourceName)) {
                    Observer sink = sinkEntry.getValue();
                    subscriptions.add(sourceObservable.subscribe(sink));
                }
            }
        }
        return subscriptions;
    }
}
