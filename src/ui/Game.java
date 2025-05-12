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
import java.awt.Label;
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
import logic.Timer;
import logic.Wall;
 
 public class Game extends Frame {
     
    private static final int GAME_DURATION = 1;
     private static final Dimension MENU_SIZE = new Dimension(500, 500);
     private static final Dimension GAME_SIZE = new Dimension(800, 800);
     private static final Point MENU_LOCATION = new Point(600, 300);
     private static final Point GAME_LOCATION = new Point(600, 100);
     private static Terrain terrain = new Terrain(14, 14);;
     private static boolean started = false;
     private static Panel controlPanel = new Panel(new GridLayout(0, 1, 0, 50));
     private static Panel scorePanel = new Panel();
     private static Label scoreLabel = new Label();
     private static Label timeLabel = new Label();
     private static Timer timer;
 
     public static boolean getStarted() {
         return started;
     }
 
     public static void setStarted(boolean started) {
         Game.started = started;
     }

     public static Terrain getTerrain() {
        return terrain;
     }

     public static Timer getTimer() {
        return timer;
     }
 
     public Game() {

         setLocation(MENU_LOCATION);
         setResizable(false);
 
         populateWindow();
         pack();
 
         addWindowListener(new WindowAdapter() {
             @Override
             public void windowClosing(WindowEvent e) {
                 terrain.endGame();
                 timer.stopTime();
                 dispose();
             }
         });
 
         setVisible(true);
 
     }
 
     private void populateWindow() {
         
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
         Label coinCountLabel = new Label();
 
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
             scoreLabel.setText("0");
             timeLabel.setText(String.format("%02d:%02d", GAME_DURATION, 0));
             timer = new Timer(timeLabel, GAME_DURATION, 0);
             coinCountLabel.setText(Integer.toString(terrain.getCoins().size()));
             add(scorePanel, BorderLayout.NORTH);
             revalidate();
             pack();
             started = true;
             if (terrain.getSelectedField() != null) terrain.getSelectedField().select(false);
             terrain.startGame();
         });

         scorePanel.add(new Label("Time: "));
         scorePanel.add(timeLabel);
         scorePanel.add(new Label("Score: "));
         scorePanel.add(scoreLabel);
         scorePanel.add(new Label("/"));
         scorePanel.add(coinCountLabel);
 
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

     public static void updateScore() {
        scoreLabel.setText(Integer.toString(Integer.parseInt(scoreLabel.getText()) + 1));
     }
 
     public static void returnToMenu(Frame frame) {
         frame.add(controlPanel, BorderLayout.EAST);
         frame.remove(scorePanel);
         frame.setLocation(MENU_LOCATION);
        timer.stopTime();
         terrain.setPreferredSize(MENU_SIZE);
         frame.revalidate();
         frame.pack();
     }
 
     public static void main(String[] args) {
         new Game();
     }
 
 }