package defenseShooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DefenseShooter extends JPanel implements ActionListener, KeyListener {
    private final Timer timer;

    // Player
    private transient Image jetImage;                               // Player image - Load jet image from constructor

    // playerX and playerY are the X and Y axis co-ordinates of the player's position. Assigning default values here.
    private int playerX = AppConstants.DEFAULT_PLAYER_POSITION_X;
    private int playerY = AppConstants.DEFAULT_PLAYER_POSITION_Y;

    /*>>>>> Bullets <<<<<*/
    private final transient ArrayList<Bullet> bullets = new ArrayList<>();
    private final transient ArrayList<Bullet> bouncingBullets = new ArrayList<>();

    /*>>>>> Game status params <<<<<*/
    private boolean gameOver = false;

    /*>>>>> Tracking score and highScore <<<<<*/
    private int score = 0;
    private int highScore = 0;

    /*>>>>> Restart button <<<<<*/
    private JButton restartButton;

    /*>>>>> Control keys <<<<<*/
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean ctrlPressed;

    /*>>>>> Bullet firing status <<<<<*/
    private boolean canFire = true;


    /**
     * 1. Creating JFrame and assigning name - Defense Shooter
     * 2. Setting frame width and height
     * 3. Setting default close operation
     * 4. Setting frame display to centre of the screen
     * 5. Setting jet flight image to display as player
     * 6. Setting CTRL click detection
     * 7. Setting key controls
     * 8. Setting timer
     */
    public DefenseShooter() {

        /*>>>>> Setting name of the game/frame <<<<<*/
        // Window
        JFrame frame = new JFrame("Defense Shooter");
        frame.add(this);

        /*>>>>> Setting size of the game window/frame <<<<<*/
        frame.setSize(AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT);

        /*>>>>> Setting default close operation <<<<<*/
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*>>>>> Setting frame display to centre of the screen <<<<<*/
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        /*>>>>> Setting jet flight image to display as player <<<<<*/
        try {
            jetImage = new ImageIcon("jet.png").getImage();
        } catch (Exception e) {
            System.out.println("Error loading jet image: " + e.getMessage());
        }

        /*>>>>> To make sure CTRL click is working <<<<<*/
        setFocusable(true);
        requestFocusInWindow();

        /*>>>>> Setting key controls <<<<<*/
        frame.addKeyListener(this);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> upPressed = true;
                    case KeyEvent.VK_DOWN -> downPressed = true;
                    case KeyEvent.VK_LEFT -> leftPressed = true;
                    case KeyEvent.VK_RIGHT -> rightPressed = true;
                    case KeyEvent.VK_CONTROL -> ctrlPressed = true;
                    default -> System.out.println("Incorrect key pressed");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> upPressed = false;
                    case KeyEvent.VK_DOWN -> downPressed = false;
                    case KeyEvent.VK_LEFT -> leftPressed = false;
                    case KeyEvent.VK_RIGHT -> rightPressed = false;
                    case KeyEvent.VK_CONTROL -> ctrlPressed = false;
                    default -> System.out.println("Incorrect key");
                }
            }
        });


        /*>>>>> Setting timer <<<<<*/
        timer = new Timer(AppConstants.TIMER_CONSTANT, this);
        timer.start();
    }

    /**
     * 1. Set background color to gray
     * 2. Display Score and High Score in color black
     * 3. Create walls with boundaries and width
     * 4. Draw player using the jet plane image
     * 5. Draw bullets
     * 6. Draw bouncing bullets
     * 7. Restart logic
     * 8. Stopping the timer
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*>>>>>> Set background color in the frame <<<<<*/
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        /*>>>>>> Always show Score and High Score <<<<<*/
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("High Score: " + highScore, 10, 40);

        /*>>>>>> Creating walls <<<<<*/
        g.setColor(Color.BLACK);
        g.drawRect(AppConstants.LEFT_WALL_BOUNDARY, AppConstants.TOP_WALL_BOUNDARY, AppConstants.WALL_WIDTH, AppConstants.WALL_HEIGHT);

        /*>>>>>> Creating player <<<<<*/
        // Setting jet image for player
        g.drawImage(jetImage, playerX, playerY, AppConstants.PLAYER_SIZE, AppConstants.PLAYER_SIZE, this);


        /*>>>>>> Bullets <<<<<*/
        //Drawing bullets which are just fired but not bounced yet with timer refresh
        for (Bullet b : bullets) {
            b.drawBullet(g);
        }

        //Drawing bouncing bullets on screen with timer refresh
        for (Bullet b : bouncingBullets) {
            b.drawBullet(g);
        }


        /*>>>>>> Restart logic <<<<<*/
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2 - 20);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, getWidth() / 2 - 30, getHeight() / 2 + 20);

            if (restartButton == null) {
                restartButton = new JButton("Restart");
                restartButton.setBounds(getWidth() / 2 - 50, getHeight() / 2 + 50, 100, 30);
                restartButton.addActionListener(e -> restartGame());
                this.setLayout(null);
                this.add(restartButton);
                this.repaint();
            }

            /*>>>>>> Stopping timer. If the timer is not stopped, bullets will keep moving after game is over <<<<<*/
            timer.stop();
        }
    }

    /**
     * This method takes care of all movements of player and bullets after a click happens.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Update bullets
        for (Bullet bullet : bullets) {
            bullet.moveBulletAhead();
            checkAndMergeBullets();

            // Skip the flow for bullets without ripple
            if (!bullet.isRippleActive()) continue;

            for (Bullet floatingBullet : bullets) {

                /*
                 * - Don't check self
                 * - Only check non-ripple bullets
                 * - check is bullet is touching floating bullet
                 */
                if (bullet != floatingBullet && !floatingBullet.isRippleActive() && bullet.isTouchingRipple(floatingBullet)) {

                    // Merge the floating bullet into ripple bullet, so removing floating bullet
                    bullets.remove(floatingBullet);

                    break; // Restart loop since list changed
                }
            }
        }

        for (Bullet b : bouncingBullets) {
            b.moveBulletAhead();
            checkAndMergeBullets();
        }

        checkCollisions();

        repaint();

        /*>>>>>> Player movements <<<<<*/
        controlPlayerMovements();

        /*>>>>>> Keep player inside the walls <<<<<*/
        keepPlayerInsideTheWalls();

        fireNewBulletWhenCTRLIsPressed();
    }

    /**
     * 1. Firing a new bullet when CTRL is pressed.
     * 2. When CTRL is pressed, create new bullet, add it to list, play bullet sound.
     * Since CTRL is still pressed, set canFire to false.
     * 3. When CTRL is released, set canFire to true.
     */
    private void fireNewBulletWhenCTRLIsPressed() {
        // Check if CTRL is pressed and can fire the bullet
        if (ctrlPressed && canFire) {
            bullets.add(new Bullet(playerX + (double) AppConstants.PLAYER_SIZE / 2, playerY + (double) AppConstants.PLAYER_SIZE / 2, AppConstants.FIXED_BULLET_DIRECTION_X, AppConstants.FIXED_BULLET_DIRECTION_Y));
            SoundManager.playSound("shoot.wav"); // Play shoot sound
            canFire = false; // prevent continuous firing
        }

        // Enable firing next bullet when CTRL is released
        if (!ctrlPressed) {
            canFire = true; // allow firing again when ctrl released
        }
    }

    private void controlPlayerMovements() {
        if (upPressed) playerY -= 5;
        if (downPressed) playerY += 5;
        if (leftPressed) playerX -= 5;
        if (rightPressed) playerX += 5;
    }

    private void keepPlayerInsideTheWalls() {
        // Math.clamp will compare given value with given min and max values. if value is less than min, returns min, if greater than max, return max
        playerX = Math.clamp(playerX, AppConstants.LEFT_WALL_BOUNDARY, AppConstants.WALL_WIDTH + (AppConstants.LEFT_WALL_BOUNDARY - AppConstants.PLAYER_SIZE));
        playerY = Math.clamp(playerY, AppConstants.TOP_WALL_BOUNDARY, AppConstants.WALL_HEIGHT + (AppConstants.TOP_WALL_BOUNDARY - AppConstants.PLAYER_SIZE));
    }


    /**
     * This method will identify rippling and bouncing bullets. Merge them both when they collide
     * 1. Find rippling bullet, else keep skipping the flow
     * 2. Find a bouncing bullet
     * 3. Check if both bullets are colliding
     * 4. Remove bouncing bullet from list
     */
    private void checkAndMergeBullets() {
        for (Bullet rippleBullet : bullets) {

            if (!rippleBullet.isRippleActive()) continue; // Skip the flow if bullet is not rippling

            for (Bullet bouncingBullet : bullets) {

                /*
                 * - Check to make sure that rippling bullet and bouncing bullet are not same
                 * - Ripple of bouncing bullet is not active
                 * - Ripple bullet is touching the bouncing bullet
                 */
                if (rippleBullet != bouncingBullet && !bouncingBullet.isRippleActive() && rippleBullet.isTouchingRipple(bouncingBullet)) {

                    // Merge bullets, just remove bouncing bullet so that new bullet shows up as merged bullet
                    bullets.remove(bouncingBullet);
                    break;
                }

            }
        }
    }


    private void checkCollisions() {
        // Bullet vs Bouncing Bullet
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            boolean hit = false;

            Iterator<Bullet> bounceIterator = bouncingBullets.iterator();
            while (bounceIterator.hasNext()) {
                Bullet bounce = bounceIterator.next();
                double dist = Math.hypot(bullet.getX() - bounce.getX(), bullet.getY() - bounce.getY());
                if (dist < AppConstants.COLLISION_RANGE) { // Ripple or bullet hit
                    // Remove the bouncing bullet
                    bounceIterator.remove();
                    score++;

                    SoundManager.playSound("hit.wav"); // Play hit sound

                    // Bounce the fired bullet
                    bullet.setDx(-bullet.getDx() + (Math.random() - 0.5) * 2);
                    bullet.setDy(-bullet.getDy() + (Math.random() - 0.5) * 2);

                    hit = true;
                    break;
                }
            }

            if (!hit && !bullet.isRippleActive()) {
                // Turn fired bullet into bouncing bullet if missed
                bouncingBullets.add(new Bullet((int) bullet.getX(), (int) bullet.getY(), AppConstants.FIXED_BULLET_DIRECTION_X, AppConstants.FIXED_BULLET_DIRECTION_Y));
                bulletIterator.remove(); // Remove original
            }
        }

        // Bouncing bullet vs Player
        for (Bullet bounce : bouncingBullets) {
            double dist = Math.hypot(bounce.getX() - (playerX + (double) AppConstants.PLAYER_SIZE / 2), bounce.getY() - (playerY + (double) AppConstants.PLAYER_SIZE / 2));
            if (dist < (double) AppConstants.PLAYER_SIZE / 2) {
                gameOver = true;

                //tracks high score
                if (score > highScore) {
                    highScore = score;
                }
            }
        }
    }


    /**
     * Controls to move the player within the walls.
     * Valid keys - arrow keys
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) playerX -= AppConstants.PLAYER_SPEED;
        if (key == KeyEvent.VK_RIGHT) playerX += AppConstants.PLAYER_SPEED;
        if (key == KeyEvent.VK_UP) playerY -= AppConstants.PLAYER_SPEED;
        if (key == KeyEvent.VK_DOWN) playerY += AppConstants.PLAYER_SPEED;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Implementation not needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Implementation not needed
    }


    /**
     * 1. Resetting all components except for highScore to re-start the game
     */
    private void restartGame() {
        // Reset everything
        playerX = 300;
        playerY = 300;
        bullets.clear();
        bouncingBullets.clear();
        score = 0;
        gameOver = false;
        restartButton.setVisible(false);
        this.remove(restartButton);
        restartButton = null;
        timer.start();
    }

    public static void main(String[] args) {
        new DefenseShooter();
    }
}