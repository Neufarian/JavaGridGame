/**
 * Represents a collectible coin in the game.
 */

package figures;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import logic.Field;
import logic.Position;
import logic.Terrain;

public class Coin extends Figure {

    public Coin(Terrain terrain, Position position) {
        super(terrain, position);
    }

    @Override
    public void paint(Graphics g) {
        Color prevColor = g.getColor();
        Font prevFont = g.getFont();

        Field field = terrain.getField(position);
        int width = field.getWidth();
        int height = field.getWidth();
        g.setColor(Color.ORANGE);
        g.fillOval(width / 4, height / 4, width / 2, height / 2);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Dialog", Font.BOLD, 15));
        
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth("$");
        int textHeight = fm.getAscent();
        int textX = (field.getWidth() - textWidth) / 2;
        int textY = (field.getHeight() + textHeight) / 2 - 2;
        g.drawString("$", textX, textY);

        g.setFont(prevFont);
        g.setColor(prevColor);
    }
    
}
