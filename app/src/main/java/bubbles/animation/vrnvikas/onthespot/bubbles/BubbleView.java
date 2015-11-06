package bubbles.animation.vrnvikas.onthespot.bubbles;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

/**
 * Created by Vikas Kumar on 05-11-2015.
 */
class BubblesView extends SurfaceView implements SurfaceHolder.Callback {

    private LinkedList<Bubble> bubbles = new LinkedList<Bubble>();
    private SurfaceHolder surfaceHolder;
    private float BUBBLE_FREQUENCY = 0.3f;
    private GameLoop gameLoop;
    private Paint backgroundPaint = new Paint();

    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        backgroundPaint.setColor(Color.BLUE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        surfaceHolder = holder;
        startAnimation();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopAnimation();

    }

    private void drawScreen(Canvas c) {
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), backgroundPaint);
        for (Bubble bubble : bubbles) {
            bubble.draw(c);
        }
    }


    private void calculateDisplay(Canvas c) {
        randomlyAddBubbles(c.getWidth(), c.getHeight());
        LinkedList<Bubble> bubblesToRemove = new
                LinkedList<Bubble>();
        for (Bubble bubble : bubbles) {
            bubble.move();
            if (bubble.outOfRange())
                bubblesToRemove.add(bubble);
        }
        for (Bubble bubble : bubblesToRemove) {
            bubbles.remove(bubble);
        }
    }

    public void randomlyAddBubbles(
            int screenWidth,
            int screenHeight) {
        if (Math.random() > BUBBLE_FREQUENCY) return;
        bubbles.add(
                new Bubble(
                        (int) (screenWidth * Math.random()),
                        screenHeight + Bubble.RADIUS,
                        (int) (Bubble.MAX_SPEED * Math.random())));
    }

    public void startAnimation() {
        synchronized (this) {
            if (gameLoop == null) {
                gameLoop = new GameLoop();
                gameLoop.start();
            }
        }
    }

    public void stopAnimation() {
        synchronized (this) {
            boolean retry = true;
            if (gameLoop != null) {
                gameLoop.running = false;
                while (retry) {
                    try {
                        gameLoop.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            gameLoop = null;
        }
    }


    public class GameLoop extends Thread {

        private long msPerFrame = 1000 / 25;
        public boolean running = true;
        long frameTime = 0;
        //BubblesView view = new BubblesView();

        public void run() {
            Canvas canvas = null;
            frameTime = System.currentTimeMillis();
            final SurfaceHolder surfaceHolder =
                    BubblesView.this.surfaceHolder;
            while (running) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        calculateDisplay(canvas);
                        drawScreen(canvas);
                    }
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                waitTillNextFrame();
            }
        }

        private void waitTillNextFrame() {
            long nextSleep = 0;
            frameTime += msPerFrame;
            nextSleep = frameTime - System.currentTimeMillis();
            if (nextSleep > 0) {
                try {
                    sleep(nextSleep);
                } catch (InterruptedException e) {
                }
            }
        }

    }

}
