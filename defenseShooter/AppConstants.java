package defenseShooter;

public class AppConstants {

    private AppConstants() {
    }

    /*********** Bullet class constants ******************/
    public static final double BULLET_SPEED = 5;
    public static final int BULLET_SIZE = 5;


    /*********** DefenseShooter class constants ******************/
    /*>>>>> Frame size parameters <<<<<*/
    public static final int FRAME_WIDTH = 620;
    public static final int FRAME_HEIGHT = 640;

    public static final int PLAYER_SIZE = 20;              // Player's pixel size (Jet plane size in our game)
    public static final int PLAYER_SPEED = 5;              // Determines no of pixels to move in a second when a control key is pressed

    /*>>>>> Wall boundaries <<<<<*/
    public static final int WALL_WIDTH = 500;
    public static final int WALL_HEIGHT = 500;
    public static final int LEFT_WALL_BOUNDARY = 50;
    public static final int TOP_WALL_BOUNDARY = 50;

    public static final int COLLISION_RANGE = 50;          // Minimum distance to detect a collision between a bouncing bullet and fired bullet

    public static final int TIMER_CONSTANT = 15;           // Timer constant to update the events and movements

    /*>>>>> Params to change the direction of the bullet at bounce <<<<<*/
    public static final double FIXED_BULLET_DIRECTION_X = 0;
    public static final double FIXED_BULLET_DIRECTION_Y = -1;

}