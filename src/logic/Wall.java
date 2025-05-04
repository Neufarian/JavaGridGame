/**
 * Represents an impassable field element.
 * Neither the player nor ghosts can move through walls.
 */

package logic;
import java.awt.Color;

public class Wall extends Field {

    public Wall(Terrain terrain, Position position) {
        super(Color.DARK_GRAY, terrain, position);
    }

    @Override
    public boolean accessible() {
        return false;
    }

}
