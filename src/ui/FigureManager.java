/**
 * Manages the movement of figures like ghosts in separate threads.
 * Ensures safe concurrent updates to the game field.
 */

package ui;
import java.util.ArrayList;

import figures.Figure;
import figures.StrongGhost;
import figures.WeakGhost;

public class FigureManager {

    public static void startMovement(ArrayList<Figure> figures) {
        for (Figure f : figures) {
            if (f instanceof WeakGhost) {
                ((WeakGhost) f).startMovement();
            } else if (f instanceof StrongGhost) {
                ((StrongGhost) f).startMovement();
            }
        }
    }

    public static void pauseMovement(ArrayList<Figure> figures) {
        for (Figure f : figures) {
            if (f instanceof WeakGhost) {
                ((WeakGhost) f).pauseMovement();
            } else if (f instanceof StrongGhost) {
                ((StrongGhost) f).pauseMovement();
            }
        }
    }

    public static void stopMovement(ArrayList<Figure> figures) {
        for (Figure f : figures) {
            if (f instanceof WeakGhost) {
                ((WeakGhost) f).stopMovement();
            } else if (f instanceof StrongGhost) {
                ((StrongGhost) f).stopMovement();
            }
        }
    }
    
}
