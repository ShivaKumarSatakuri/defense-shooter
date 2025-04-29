# Defense Shooter
<i><h3>Defense shooter is my personal project to understand, learn and gain experience of creating 2D games using nothing but in-built java libraries like swing and awt.</h3></i>

# Game concept

- A two-dimensional shooting game, where the player has a gun with unlimited bullets.

- We get to see the game from a bird's eye view. (Just like tetris)

- Player is inside a closed room. Walls keep rotating in clockwise and anti-clockwise direction with in their positions. Direction of the movement keeps changing every 30 seconds.

- Game starts when the player fires the first bullet (by clicking CTRL key).

- When the bullet is fired, it'll start with a little ripple. Ripple must disappear in a second or two, but the bullet must continue to move.

- The concept of the game is such that when the first bullet is fired, it'll hit the moving wall and bounce back at a random angle depending on the direction in which the wall is moving.

- Player must hit the bouncing bullet(s) by aiming and firing another bullet. If the fired bullet or it's ripple touches a bouncing bullet, then both bullets will disappear.

- If the fired bullet or it's ripple does not hit the bouncing bullet, then the ripple will turn into a bullet and travel straight until it hits the walls and bounces back by changing its direction.

- Player must escape from the bouncing bullets using control arrows. Game ends if the bullet hits the player.

- If the fired bullet doesn't hit the bouncing bullet, then the user will now have to shoot two bullets which are bouncing within the room randomly.

- The game only ends if the player gets hit by the bullet, only highest score will be recorded. Highest score is nothing but the number of bouncing bullets hit by the player.

# Future enhancements
- Add a jet as an image for the player
- Add game sounds like firing a bullet and bullets hitting each other