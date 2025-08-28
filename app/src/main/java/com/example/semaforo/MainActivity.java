package com.example.semaforo;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Thread worker;
    private volatile boolean running = false;
    private int counter = 0;
    private static final long PERIODO_MS = 5000;
    private ImageView img_led_one, img_led_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        img_led_one  = findViewById(R.id.img_led_one);
        img_led_two  = findViewById(R.id.img_led_two);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (worker != null && worker.isAlive()) return;
        running = true;
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    final int step = counter;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean topOn = (step % 2 == 0);
                            img_led_one.setSelected(topOn);
                            img_led_two.setSelected(!topOn);
                        }
                    });
                    counter++;
                    try {
                        Thread.sleep(PERIODO_MS);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }, "SemaforoThread");

        worker.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        if (worker != null) {
            worker.interrupt();
            worker = null;
        }
    }
}