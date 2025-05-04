/**
 * Represents a traversable field element through which the player and other figures can move.
 */

package logic;
import java.awt.Color;

public class Passage extends Field {

    public Passage(Terrain terrain, Position position) {
        super(Color.LIGHT_GRAY, terrain, position);
    }

    @Override
    public boolean accessible() {
        return true;
    }
    
}
