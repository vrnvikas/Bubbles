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

    private float x, y, speedy, speedx;
    public Bitmap bubbleBitmap;
    private float amountOfWobble = 0;
    public static final float WOBBLE_RATE = 1 / 40;
    public static final int WOBBLE_AMOUNT = 3;
    private static final Paint bubblePaint = new Paint();
    public static final int RADIUS = 10;
    public static final int MAX_SPEED = 5;
    public static final int MIN_SPEED = 1;
    public Canvas c;

    static {
        bubblePaint.setStyle(Paint.Style.FILL);
        bubblePaint.setColor(Color.GREEN);
        bubblePaint.setAlpha(66);
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
        this.c = c;
        //c.drawCircle(x, y, RADIUS, bubblePaint);
        //c.drawBitmap(bubbleBitmap, x-RADIUS, y-RADIUS, bubblePaint);
        c.drawOval(new RectF(
                        x - RADIUS - WOBBLE_AMOUNT * amountOfWobble,
                        y - RADIUS + WOBBLE_AMOUNT * amountOfWobble,
                        x + RADIUS + WOBBLE_AMOUNT * amountOfWobble,
                        y + RADIUS - WOBBLE_AMOUNT * amountOfWobble),
                bubblePaint);

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
}
