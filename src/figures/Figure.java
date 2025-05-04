/**
 * Abstract base class for all figures on the field, such as the player and ghosts.
 * Encapsulates shared properties like equals and paint method.
 */

package figures;
import java.awt.Graphics;

import logic.Position;
import logic.Terrain;

public abstract class Figure {
    
    protected Terrain terrain;
    protected Position position;

    public Figure(Terrain terrain, Position position) {
        this.terrain = terrain;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void paint(Graphics g);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Figure)) return false;
        Figure other = (Figure)obj;
        return this.position.equals(other.position);
    }

}
