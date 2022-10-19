package org.jnius;

import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import java.util.Random;


public class TaskRunner {
    private final Executor executor = Executors.newFixedThreadPool(1);
    private int counter = 0;

    public class TestObject {
        private byte[] name;
        TestObject() {
            this.name = null;
        }
        byte[] getValue() {
            return this.name;
        }
        void setValue(byte[] name) {
            this.name = name;
        }

    }

    public interface Callback<R> {
        void onComplete(TestObject test);
    }

    public <R> void executeAsync(Callback<R> callback) {
        executor.execute(() -> {
            try {
                if(this.counter == 0){
                    TestObject test = new TestObject();
                    byte[] test_array = new byte[200000];
                    Random rd = new Random();
                    rd.nextBytes(test_array);
                    test.setValue(test_array);
                    callback.onComplete(test);
                } else {
                    callback.onComplete(null);
                }
                this.counter += 1;
                Thread.sleep(1);
                this.executeAsync(callback);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}