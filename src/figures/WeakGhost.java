/**
 * Represents a weaker variant of the ghost enemy.
 * Has random movement logic and has less aggressive behavior compared to strong ghosts.
 */

package figures;
import java.awt.Color;
import java.awt.Graphics;

import logic.Field;
import logic.Position;
import logic.Terrain;
import ui.DialogManager;
import ui.Game;

public class WeakGhost extends Figure implements Runnable {

    private static final int sleepTime = 500;
    private Thread thread = new Thread(this);
    private boolean work = false;

    public WeakGhost(Terrain terrain, Position position) {
        super(terrain, position);
        thread.start();
    }

    @Override
    public void run() {
        boolean wasCoin = false;
        Coin savedCoin = null;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                
                synchronized (this) {
                    while(!work) {
                        wait();
                    }
                }
            
                Position newPos;
                do {
                    int ind = (int)(Math.random() * 4);
                    Position.Direction direction = Position.Direction.UP;
                    switch (ind) {
                        case 0: direction = Position.Direction.UP; break;
                        case 1: direction = Position.Direction.LEFT; break;
                        case 2: direction = Position.Direction.DOWN; break;
                        case 3: direction = Position.Direction.RIGHT; break;
                    }
                    newPos = position.getPosition(direction);
                } while(newPos.getRow() < 0 || newPos.getRow() >= terrain.getRows() ||
                        newPos.getColumn() < 0 || newPos.getColumn() >= terrain.getColumns() ||
                        !terrain.getField(newPos).accessible() ||
                        terrain.getField(newPos).getFigure() instanceof StrongGhost ||
                        terrain.getField(newPos).getFigure() instanceof WeakGhost);
                
                Field oldField = terrain.getField(position);
                position = newPos;
                Field newField = terrain.getField(position);

                if (newField.getFigure() instanceof Player) {
                    DialogManager.showWinDialog((Game)terrain.getParent(), terrain, false);
                    break;
                }

                if (wasCoin) {
                    wasCoin = false;
                    oldField.setFigure(savedCoin);
                } else oldField.setFigure(null);
                if (newField.getFigure() instanceof Coin) { 
                    wasCoin = true; 
                    savedCoin = (Coin)newField.getFigure();
                } 
                newField.setFigure(this);
                oldField.repaint();
                newField.repaint();
                Thread.sleep(sleepTime);

            }
        } catch(InterruptedException e) {}
    }

    public synchronized void startMovement() {
        work = true;
        notify();
    }

    public synchronized void pauseMovement() {
        work = false;
    }

    public synchronized void stopMovement() {
        thread.interrupt();
    }

    @Override
    public void paint(Graphics g) {
        Color prevColor = g.getColor();
    
        Field field = terrain.getField(position);
        int width = field.getWidth() - 1;
        int height = field.getHeight() - 1;
    
        g.setColor(Color.CYAN);
        g.fillArc(1, 1, width - 1, height, 0, 180);
    
        int scallopHeight = height / 3;
        g.fillRect(1, height / 2, width - 1, height / 2 - scallopHeight / 2);
    
        int scallopCount = 3;
        int scallopWidth = width / scallopCount;
        for (int i = 0; i < scallopCount; i++) {
            g.fillOval(i * scallopWidth + 1, height - scallopHeight, scallopWidth - 1, scallopHeight);
        }
    
        g.setColor(Color.WHITE);
        int eyeWidth = width / 6;
        int eyeHeight = height / 4;
        int eyeY = height / 4;
        int eyeSpacing = width / 10;
        int eyeXLeft = width / 2 - eyeSpacing - eyeWidth;
        int eyeXRight = width / 2 + eyeSpacing;
        g.fillOval(eyeXLeft, eyeY, eyeWidth, eyeHeight); 
        g.fillOval(eyeXRight, eyeY, eyeWidth, eyeHeight);

        g.setColor(Color.BLACK);
        int pupilWidth = eyeWidth / 2;
        int pupilHeight = eyeHeight / 2;
        int pupilY = eyeY + eyeHeight / 3;
        g.fillOval(eyeXLeft + eyeWidth / 3, pupilY, pupilWidth, pupilHeight);
        g.fillOval(eyeXRight + eyeWidth / 3, pupilY, pupilWidth, pupilHeight);

        g.setColor(prevColor);
    }

}
