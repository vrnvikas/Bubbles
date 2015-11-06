package bubbles.animation.vrnvikas.onthespot.bubbles;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Vikas Kumar on 05-11-2015.
 */
public class Bubble {

    private float x, y, speed;
    private static final Paint bubblePaint = new Paint();

    static {
        bubblePaint.setColor(Color.CYAN);
        bubblePaint.setStyle(Paint.Style.STROKE);
    }

    public static final int RADIUS = 10;
    public static final int MAX_SPEED = 10;
    public static final int MIN_SPEED = 1;

    public Bubble(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = Math.max(speed, MIN_SPEED);
    }

    public void draw(Canvas c) {
        c.drawCircle(x, y, RADIUS, bubblePaint);
    }

    public void move() {
        y -= speed;
    }

    public boolean outOfRange() {
        return (y + RADIUS < 0);
    }
}
