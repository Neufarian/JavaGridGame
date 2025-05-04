/**
 * Represents a single cell or tile on the game board.
 * May contain a figure and determines if movement through it is allowed.
 * Can be selected.
 */

package logic;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import figures.Figure;
import ui.Game;

public abstract class Field extends Canvas {

    private Color color;
    private Position position;
    private boolean selected = false;
    private Figure figure;

    public Field(Color color, Terrain terrain, Position position) {
        this.color = color;
        this.position = position;

        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!Game.getStarted())
                    terrain.selected(Field.this);
                terrain.requestFocus();
            }
        });
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public abstract boolean accessible();
    
    public void select(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(color);
        g.setColor((selected) ? Color.RED : Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        if (figure != null) figure.paint(g);
    }

}
