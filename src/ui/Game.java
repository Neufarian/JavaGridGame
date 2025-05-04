/**
 * Main UI class responsible for initializing and displaying the game window.
 * Handles user input and interfaces with the terrain and other managers.
 */

package ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import figures.Coin;
import figures.Player;
import figures.StrongGhost;
import figures.WeakGhost;
import logic.Passage;
import logic.Terrain;
import logic.Wall;

public class Game extends Frame {
    
    private static final Dimension MENU_SIZE = new Dimension(500, 500);
    private static final Dimension GAME_SIZE = new Dimension(800, 800);
    private static final Point MENU_LOCATION = new Point(600, 300);
    private static final Point GAME_LOCATION = new Point(600, 100);
    private static Terrain terrain;
    private static boolean started = false;
    private static Panel controlPanel;

    public static boolean getStarted() {
        return started;
    }

    public static void setStarted(boolean started) {
        Game.started = started;
    }

    public Game() {
        terrain = new Terrain(14, 14);

        setLocation(MENU_LOCATION);
        setResizable(false);

        populateWindow();
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                terrain.endGame();
                dispose();
            }
        });

        setVisible(true);

    }

    private void populateWindow() {
        
        controlPanel = new Panel(new GridLayout(0, 1, 0, 50));
        Panel optionPanel = new Panel(new GridLayout(0, 1));
        Panel fieldPanel = new Panel();
        Panel figurePanel = new Panel();
        CheckboxGroup optionsGroup = new CheckboxGroup();
        Checkbox wallCheckbox = new Checkbox("Wall", true, optionsGroup);
        Checkbox passageCheckbox = new Checkbox("Passage", false, optionsGroup);
        Checkbox coinCheckbox = new Checkbox("Coin", false, optionsGroup);
        Checkbox playerCheckbox = new Checkbox("Player", false, optionsGroup);
        Checkbox weakGhostCheckbox = new Checkbox("Weak Ghost", false, optionsGroup);
        Checkbox strongGhostCheckbox = new Checkbox("Strong Ghost", false, optionsGroup);
        Button setButton = new Button("Set");
        Button randomButton = new Button("Random");
        Button resetButton = new Button("Reset");
        Button startButton = new Button("Start");

        startButton.setEnabled(false);

        setButton.addActionListener((ae) -> {
            if (terrain.getSelectedField() == null) return;
            String option = optionsGroup.getSelectedCheckbox().getLabel();
            if (option.equals("Wall"))
                terrain.switchSelected(new Wall(terrain, terrain.getSelectedField().getPosition()), null);
            else if (option.equals("Passage"))
                terrain.switchSelected(new Passage(terrain, terrain.getSelectedField().getPosition()), null);
            else if (option.equals("Coin"))
            terrain.switchSelected(null, new Coin(terrain, terrain.getSelectedField().getPosition()));
            else if (option.equals("Player"))
                terrain.switchSelected(null, new Player(terrain, terrain.getSelectedField().getPosition()));
            else if (option.equals("Weak Ghost"))
                terrain.switchSelected(null, new WeakGhost(terrain, terrain.getSelectedField().getPosition()));
            else if (option.equals("Strong Ghost"))
                terrain.switchSelected(null, new StrongGhost(terrain, terrain.getSelectedField().getPosition()));

            if (terrain.playable()) startButton.setEnabled(true);
            terrain.requestFocus();
        });

        randomButton.addActionListener((ae) -> {
            startButton.setEnabled(true);
            terrain.endGame();
            terrain.generate();
        });

        resetButton.addActionListener((ae) -> {
            startButton.setEnabled(false);
            terrain.endGame();
            terrain.reset();
        });

        startButton.addActionListener((ae) -> {
            remove(controlPanel);
            setLocation(GAME_LOCATION);
            terrain.setPreferredSize(GAME_SIZE);
            revalidate();
            pack();
            started = true;
            if (terrain.getSelectedField() != null) terrain.getSelectedField().select(false);
            terrain.startGame();
        });

        fieldPanel.add(wallCheckbox);
        fieldPanel.add(passageCheckbox);
        fieldPanel.add(coinCheckbox);

        figurePanel.add(playerCheckbox);
        figurePanel.add(weakGhostCheckbox);
        figurePanel.add(strongGhostCheckbox);

        optionPanel.add(fieldPanel);
        optionPanel.add(figurePanel);
        optionPanel.add(setButton);

        controlPanel.add(optionPanel);
        controlPanel.add(randomButton);
        controlPanel.add(resetButton);
        controlPanel.add(startButton);

        add(controlPanel, BorderLayout.EAST);
        add(terrain, BorderLayout.CENTER);
        
    }

    public static void returnToMenu(Frame frame) {
        frame.add(controlPanel, BorderLayout.EAST);
        frame.setLocation(MENU_LOCATION);
        terrain.setPreferredSize(MENU_SIZE);
        frame.revalidate();
        frame.pack();
    }

    public static void main(String[] args) {
        new Game();
    }

}
