# Defense Shooter
<i><h3>Defense shooter is my personal project to understand, learn and gain experience of creating 2D games using nothing but in-built java libraries like swing and awt.</h3></i>

# Game concept

- A two-dimensional shooting game, where the player has a gun with unlimited bullets to fire.

- We see the game from bird's eyes view. (Just like tetris)

- Player is inside a closed room. There's no way for the player and the fired bullets to escape from the room.

- Game starts when the player fires the first bullet (by clicking CTRL key).

- When the bullet is fired, it'll start moving ahead with a little ripple. Ripple will disappear in a second or two, but the bullet will continue to move.

- Upon hitting the wall, the moving bullet(s) will bounce back in a random directions.

- Player must hit the bouncing bullet(s) by aiming and firing another bullet. If a fired bullet or it's ripple touches the bouncing bullets, they all merge and become a single bullet.

- If the fired bullet or it's ripple does not hit the bouncing bullet(s), then the ripple will disappear and the bullet will continue to travel straight until it hits the walls and bounces back.

- Player must escape from the bouncing bullets using control arrows. Game ends if a bouncing bullet hits the player.

- Only the highest score will be recorded. Highest score is nothing but the number of bouncing bullets hit by the player.

# Future enhancements
- Add a jet as an image for the player
- Add game sounds like firing a bullet and bullets hitting each other
- Possible refactoring of code if need be