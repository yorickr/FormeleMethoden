package com.imegumii;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by kenny on 12-6-2017.
 */
public class BackgroundWorker implements Runnable{

    public interface Worker {
        void execute();
    };

    private Thread t;
    private boolean running;
    private Queue<Worker> workers;

    private static BackgroundWorker worker;

    public static BackgroundWorker instance()
    {
        if(worker == null)
            worker = new BackgroundWorker();

        return worker;
    }

    private BackgroundWorker()
    {
        running = true;
        workers = new ArrayDeque<Worker>();

        t = new Thread(this);
        t.start();
    }

    public void addWorker(Worker w)
    {
        workers.add(w);
    }

    public void kill()
    {
        running = false;
    }

    @Override
    public void run() {

        while(running)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if(workers.isEmpty())
                continue;

            Worker current = workers.poll();

            current.execute();
        }

    }
}
