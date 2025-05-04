/**
 * Manages all game dialogs, such as win/lose notifications and pause dialog.
 */

package ui;
import java.awt.*;
import java.awt.event.*;

import logic.Terrain;

public class DialogManager {

    private static final Dimension DIALOG_SIZE = new Dimension(100, 200);

    public static void showPauseDialog(Game parent, Terrain terrain) {
        Dialog pauseDialog = new Dialog(parent, "Pause", true);
        terrain.pauseGame();

        int x = parent.getX() + (parent.getWidth() - 100) / 2;
        int y = parent.getY() + (parent.getHeight() - 200) / 2;
        pauseDialog.setLocation(x, y);
        pauseDialog.setPreferredSize(DIALOG_SIZE);
        pauseDialog.setLayout(new GridLayout(0, 1, 0, 5));
        pauseDialog.setFocusable(true);
        pauseDialog.requestFocus();

        Button resumeButton = new Button("Resume");
        Button menuButton = new Button("Menu");
        Button quitButton = new Button("Quit");

        resumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resume(terrain, pauseDialog);
            }
        });

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pauseDialog.dispose();
                terrain.pauseGame();
                Game.setStarted(false);
                Game.returnToMenu(parent);
            }
        });

        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pauseDialog.dispose();
                terrain.endGame();
                parent.dispose();
            }
        });

        pauseDialog.add(resumeButton);
        pauseDialog.add(menuButton);
        pauseDialog.add(quitButton);
        pauseDialog.pack();

        pauseDialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    resume(terrain, pauseDialog);
            }
        });

        pauseDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resume(terrain, pauseDialog);
            }
        });

        pauseDialog.setVisible(true);
    }

    private static void resume(Terrain terrain, Dialog pauseDialog) {
        terrain.startGame();
        terrain.setPaused(false);
        pauseDialog.dispose();
    }

    public static void showWinDialog(Game parent, Terrain terrain, boolean won) {
        String outcome = (won) ? "You won!" : "You lost!";
        Dialog winDialog = new Dialog(parent, outcome, true);
        terrain.endGame();

        int x = parent.getX() + (parent.getWidth() - 100) / 2;
        int y = parent.getY() + (parent.getHeight() - 200) / 2;
        winDialog.setLocation(x, y);
        winDialog.setPreferredSize(DIALOG_SIZE);
        winDialog.setLayout(new GridLayout(0, 1, 0, 5));

        Button playButton = new Button("Play again");
        Button quitButton = new Button("Quit");

        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                winDialog.dispose();
                terrain.pauseGame();
                terrain.generate();
                Game.setStarted(false);
                Game.returnToMenu(parent);
            }
        });

        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                winDialog.dispose();
                terrain.endGame();
                parent.dispose();
            }
        });

        winDialog.add(playButton);
        winDialog.add(quitButton);
        winDialog.pack();

        winDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                winDialog.dispose();
                terrain.pauseGame();
                terrain.generate();
                Game.setStarted(false);
                Game.returnToMenu(parent);
            }
        });

        winDialog.setVisible(true);
    }
}
