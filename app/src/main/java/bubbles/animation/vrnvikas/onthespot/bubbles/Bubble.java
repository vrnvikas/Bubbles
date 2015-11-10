package bubbles.animation.vrnvikas.onthespot.bubbles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Vikas Kumar on 05-11-2015.
 */
public class Bubble {

    public float x, y, speedy, speedx, xStart, yStart, xStop, yStop;
    public Bitmap bubbleBitmap;
    private float amountOfWobble = 0;
    public static final float WOBBLE_RATE = 1 / 40;
    public static final int WOBBLE_AMOUNT = 3;
    private static final Paint bubblePaint = new Paint();
    public static final int RADIUS = 20;
    public static final int MAX_SPEED = 5;
    public static final int MIN_SPEED = 1;
    public static final float MIN_DIST = 150;
    public float currentDistance = 0;

    static {
        bubblePaint.setStyle(Paint.Style.FILL);
        bubblePaint.setColor(Color.rgb(40,203,155));
        //bubblePaint.setAlpha(80);
        bubblePaint.setAntiAlias(true);
    }

    public Bubble(float x, float y, float speedy, float speedx, Bitmap bubbleBitmap) {
        this.x = x;
        this.y = y;
        this.bubbleBitmap = bubbleBitmap;
        this.speedx = Math.max(speedx, MIN_SPEED);
        this.speedy = Math.max(speedy, MIN_SPEED);
    }

    public void draw(Canvas c) {
        //c.drawCircle(x, y, RADIUS, bubblePaint);
        //c.drawBitmap(bubbleBitmap, x-RADIUS, y-RADIUS, bubblePaint);

        c.drawOval(new RectF(
                        x - RADIUS - WOBBLE_AMOUNT * amountOfWobble,
                        y - RADIUS + WOBBLE_AMOUNT * amountOfWobble,
                        x + RADIUS + WOBBLE_AMOUNT * amountOfWobble,
                        y + RADIUS - WOBBLE_AMOUNT * amountOfWobble),
                bubblePaint);


        //c.drawRect(x, y, x + MIN_DIST, y + MIN_DIST, bubblePaint);
        //c.drawLine(x, y, x + MIN_DIST, y + MIN_DIST, bubblePaint);

    }

    public void move(float numFrames) {
        y -= speedy * numFrames;
        x -= speedx * numFrames;
        amountOfWobble = (float) Math.sin(y * WOBBLE_RATE);
    }

    public boolean outOfRange() {

        if (y + RADIUS < 0 || x + RADIUS < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inRange(float xStart, float yStart, float xStop, float yStop) {

        currentDistance = (float) Math.sqrt((Math.abs(xStop - xStart)) * (Math.abs(xStop - xStart))
                + (Math.abs(yStop - yStart)) * (Math.abs(yStop - yStart)));
        if (currentDistance < MIN_DIST) {
            return true;
        }

        return false;
    }

}
