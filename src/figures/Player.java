/**
 * Represents the main controllable character in the game.
 * Handles painting the player based on the facing direction and mouth being opened or closed.
 */

package figures;
import java.awt.Color;
import java.awt.Graphics;

import logic.Field;
import logic.Position;
import logic.Terrain;

public class Player extends Figure {

    private boolean moved = false;
    private boolean mouthOpen = true;
    private Position.Direction facing = Position.Direction.RIGHT;

    public void setFacing(Position.Direction facing) {
        this.facing = facing;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public Player(Terrain terrain, Position position) {
        super(terrain, position);
    }

    @Override
    public void paint(Graphics g) {
        Color prevColor = g.getColor();

        g.setColor(Color.YELLOW);
        Field field = terrain.getField(position);
        int width = field.getWidth() - 2, height = field.getHeight() - 2;

        if (moved) mouthOpen = !mouthOpen;
        if (mouthOpen) {
            switch (facing) {
                case UP: g.fillArc(1, 1, width, height, 135, 270); break;
                case LEFT: g.fillArc(1, 1, width, height, 225, 270); break;
                case DOWN: g.fillArc(1, 1, width, height, -45, 270); break;
                case RIGHT: g.fillArc(1, 1, width, height, 45, 270); break;
            }
        } else g.fillOval(1, 1, width, height);
        moved = false;

        g.setColor(prevColor);
    }

}
