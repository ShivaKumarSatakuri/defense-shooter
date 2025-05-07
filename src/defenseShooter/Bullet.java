package defenseShooter;

import java.awt.*;

public class Bullet {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private boolean rippleActive;
    private int rippleTimer;

    /**
     * Creates a new bullet
     * 1. Sets co-ordinates and direction
     * 2. Enables ripple
     * 3. Sets ripple timer
     *
     * @param x  sets x co-ordinate
     * @param y  sets y co-ordinate
     * @param dx sets direction of x
     * @param dy sets direction of y
     */
    public Bullet(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;

        this.dx = dx; // No need to normalize if firing (0, -1)
        this.dy = dy;

        this.rippleActive = true;
        this.rippleTimer = AppConstants.RIPPLE_TIMER;
    }


    /**
     * This method keeps the bullet moving until it hits the wall(s). When hit, it'll invoke the bounce method
     */
    public void moveBulletAhead() {

        // Setting the direction of the bullet
        x += dx * AppConstants.BULLET_SPEED;    // x co-ordinate remains constant. Bullet doesn't change direction in x axis
        y += dy * AppConstants.BULLET_SPEED;    // Bullet keeps going up, so y co-ordinate keeps decreasing until reaches top wall

        // Ripple display decider and enabler
        if (rippleTimer > 0) rippleTimer--;
        else rippleActive = false;

        boolean bounced = false;

        // If bullet touches any of the walls, reverse the direction
        if (x <= AppConstants.LEFT_WALL || x >= AppConstants.RIGHT_WALL) {
            dx = -dx;
            bounced = true;
        }
        if (y <= AppConstants.TOP_WALL || y >= AppConstants.BOTTOM_WALL) {
            dy = -dy;
            bounced = true;
        }

        if (bounced) {
            applyRandomBounce();
        }
    }

    /**
     * Reverse the direction of the bullet by change the angle upon hitting the wall
     */
    private void applyRandomBounce() {
        double randomAngle = Math.toRadians((Math.random() * 30) - 15); // random between -15 to 15 degrees

        double newDx = dx * Math.cos(randomAngle) - dy * Math.sin(randomAngle);
        double newDy = dx * Math.sin(randomAngle) + dy * Math.cos(randomAngle);

        double length = Math.sqrt(newDx * newDx + newDy * newDy);

        dx = newDx / length;
        dy = newDy / length;
    }


    /**
     * Merge given two bullets and return a new bullet
     *
     * @param b1 : bullet1
     * @param b2 : bullet2
     * @return new bullet
     */
    public static Bullet merge(Bullet b1, Bullet b2) {
        // Create a new bullet at the average position
        double newX = (b1.x + b2.x) / 2;
        double newY = (b1.y + b2.y) / 2;

        // Option 1: Keep one direction (say b1's direction)
        double newDx = b1.dx;
        double newDy = b1.dy;

        return new Bullet(newX, newY, newDx, newDy);
    }

    /**
     * To check whether a bouncing bullet is touching the fired bullet or not
     *
     * @param other : bouncing bullet
     * @return boolean status
     */
    public boolean isTouchingRipple(Bullet other) {
        int rippleRadius = (30 - rippleTimer) * 2 * 3 / 2; // approx outer ripple radius

        double distance = Math.hypot(this.x - other.x, this.y - other.y);
        return distance < rippleRadius;
    }


    /**
     * 1. Check if ripple is active and draw ripples
     * 2. Draw bullet
     *
     * @param graphics : Set the graphics object with newly drawn bullet
     */
    public void drawBullet(Graphics graphics) {

        /*>>>>>> Creating ripple <<<<<*/
        if (rippleActive) {
            graphics.setColor(Color.BLACK);

            // Determines size of every ripple
            int rippleSize = (AppConstants.RIPPLE_TIMER - rippleTimer) * 2;

            // This loop takes care of displaying ripples(semi-circles) with calculated radius and ripple growth
            for (int i = 1; i <= 3; i++) {          // 3 Wi-Fi arcs
                int radius = rippleSize * i;        //determines size of each ripple
                graphics.drawArc((int) (x - (double) radius / 2), (int) (y - (double) radius / 2), radius, radius, 0, 180);
            }
        }

        /*>>>>>> Drawing bullet (dot) <<<<<*/
        graphics.setColor(Color.BLACK);
        graphics.fillOval((int) x, (int) y, AppConstants.BULLET_SIZE, AppConstants.BULLET_SIZE);
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public boolean isRippleActive() {
        return rippleActive;
    }
}