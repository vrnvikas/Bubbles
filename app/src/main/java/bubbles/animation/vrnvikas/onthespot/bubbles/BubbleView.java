package bubbles.animation.vrnvikas.onthespot.bubbles;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private float xStart, yStart, xStop, yStop;
    private LinkedList<Bubble> bubbles = new LinkedList<Bubble>();
    private LinkedList<Line> lines = new LinkedList<>();
    private SurfaceHolder surfaceHolder;
    private float BUBBLE_FREQUENCY = 0.2f;
    private GameLoop gameLoop;
    private Bitmap bubbleBitmap;
    public float currentDistance = 0;
    public static final float MIN_DIST = 130;
    private Paint backgroundPaint = new Paint();

    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        backgroundPaint.setColor(Color.WHITE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bubbleBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.football);
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

        for (Line line : lines) {
            line.draw(c);

        }
    }

    /*
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
    */

    private void calculateDisplay(Canvas c, float numberOfFrames) {

        randomlyAddBubbles(c.getWidth(), c.getHeight(), numberOfFrames);
        LinkedList<Bubble> bubblesToRemove = new LinkedList<Bubble>();
        lines.clear();

        for (Bubble bubble : bubbles) {
            bubble.move(numberOfFrames);
            if (bubble.outOfRange())
                bubblesToRemove.add(bubble);

            for (Bubble nextBubble : bubbles) {

                if (inRange(bubble, nextBubble)) {
                    joinBubble(bubble, nextBubble);
                }
            }
        }

        for (Bubble bubble : bubblesToRemove) {
            bubbles.remove(bubble);
        }
    }

    private void joinBubble(Bubble bubble, Bubble bubbleNext) {

        synchronized (lines) {
            Line line = new Line(bubble.x, bubble.y, bubbleNext.x, bubbleNext.y);
            lines.add(line);
        }
    }

    public boolean inRange(Bubble bubble, Bubble bubbleNext) {

        xStart = bubble.x;
        yStart = bubble.y;
        xStop = bubbleNext.x;
        yStop = bubbleNext.y;

        currentDistance = (float) Math.sqrt((Math.abs(xStop - xStart)) * (Math.abs(xStop - xStart))
                + (Math.abs(yStop - yStart)) * (Math.abs(yStop - yStart)));
        if (currentDistance < MIN_DIST) {
            return true;
        }

        return false;
    }


    public void randomlyAddBubbles(int screenWidth, int screenHeight, float numFrames) {

        if (Math.random() > BUBBLE_FREQUENCY * numFrames) return;
        synchronized (bubbles) {
            bubbles.add(new Bubble((int) (screenWidth * Math.random()), (int) (screenHeight * Math.random()),
                    (int) (Bubble.MAX_SPEED * Math.random()), (int) (Bubble.MAX_SPEED * Math.random()), bubbleBitmap));
        }
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

        /*
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
        */
        public void run() {
            Canvas canvas = null;
            long thisFrameTime;
            long lastFrameTime = System.currentTimeMillis();
            float framesSinceLastFrame = 0;
            final SurfaceHolder surfaceHolder = BubblesView.this.surfaceHolder;
            while (running) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        if (canvas != null) {
                            drawScreen(canvas);
                            calculateDisplay(canvas, framesSinceLastFrame);
                        }
                    }
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                thisFrameTime = System.currentTimeMillis();
                framesSinceLastFrame = (float) (thisFrameTime - lastFrameTime) / msPerFrame;
                lastFrameTime = thisFrameTime;
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
