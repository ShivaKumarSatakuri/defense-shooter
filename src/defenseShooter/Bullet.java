package defenseShooter;

import java.awt.*;

public class Bullet {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private boolean rippleActive;
    private int rippleTimer;
    private double radius;

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
    public Bullet(double x, double y, double dx, double dy, boolean rippleActive) {
        this.x = x;
        this.y = y;

        this.dx = dx; // No need to normalize if firing (0, -1)
        this.dy = dy;

        this.rippleActive = rippleActive;
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
     * To check whether a bouncing bullet is touching the fired bullet or not
     *
     * @param bouncingBullet : bouncing bullet
     * @return boolean status
     */
    public boolean isTouchingRipple(Bullet bouncingBullet) {
        int rippleRadius = (AppConstants.RIPPLE_TIMER - rippleTimer) * 2 * 3 / 2; // approx outer ripple radius

        double distance = Math.hypot(this.x - bouncingBullet.getX(), this.y - bouncingBullet.getY());
        return distance < rippleRadius;
    }

    /**
     * @param bouncingBullet to get its co-ordinates
     * @param leftEnd        : The left beginning point of the ripple
     * @param rightEnd       : The right beginning point of the ripple
     * @param width          : Same as radius of ripple
     * @param height         : Same as radius of ripple
     * @param startAngle     : Set this to zero
     * @param arcAngle       : Set this to 180
     * @return boolean status
     */
    public boolean isTouchingRipple(Bullet bouncingBullet, double leftEnd, double rightEnd, double width, double height, double startAngle, double arcAngle) {
        // 1. Find the center and radius
        double centerX = leftEnd + width / 2.0;
        double centerY = rightEnd + height / 2.0;
        radius = width / 2.0; // Assuming width == height

        // 2. Check if point is within the radius (circle check)
        double distanceOfX = bouncingBullet.x - centerX;
        double distanceOfY = bouncingBullet.y - centerY;
        double distanceSquared = distanceOfX * distanceOfX + distanceOfY * distanceOfY;
        if (distanceSquared > radius * radius) {
            return false;
        }

        // 3. Calculate angle of point relative to center
        double angle = Math.toDegrees(Math.atan2(-distanceOfY, distanceOfX)); // Note: y axis is inverted in screen coordinates
        if (angle < 0) angle += 180;

        // 4. Normalize start and end angle
        int endAngle = (int) (startAngle + arcAngle);
        int normalizedStart = (int) (((startAngle % 180) + 180) % 180);
        int normalizedEnd = ((endAngle % 180) + 180) % 180;

        // 5. Check if angle is within arc sweep
        if (arcAngle >= 0) {
            if (normalizedStart <= normalizedEnd) {
                return angle >= normalizedStart && angle <= normalizedEnd;
            } else {
                return angle >= normalizedStart || angle <= normalizedEnd;
            }
        } else {
            if (normalizedEnd <= normalizedStart) {
                return angle <= normalizedStart && angle >= normalizedEnd;
            } else {
                return angle <= normalizedStart || angle >= normalizedEnd;
            }
        }
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
                radius = (double) rippleSize * i;        //determines size of each ripple
                graphics.drawArc((int) (x - radius / 2), (int) (y - radius / 2), (int) radius, (int) radius, 0, 180);
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

    public boolean isRippleNotActive() {
        return !rippleActive;
    }

    public void setRippleTimer(int rippleTimer) {
        this.rippleTimer = rippleTimer;
    }

    public double getRadius() {
        return radius;
    }
}