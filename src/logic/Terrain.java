/**
 * Core logic controller for the game board.
 * Handles player movement, collision detection, game state changes, and figure interactions.
 */

package logic;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import figures.Coin;
import figures.Figure;
import figures.Player;
import figures.StrongGhost;
import figures.WeakGhost;
import ui.DialogManager;
import ui.FigureManager;
import ui.Game;
import ui.SoundManager;

public class Terrain extends Panel {
    
    private static final Dimension TERRAIN_SIZE = new Dimension(500, 500);
    private int rows;
    private int columns;
    private Field[][] matrix;
    private Field selectedField;
    private ArrayList<Coin> coins;
    private ArrayList<Figure> ghosts;
    private Player player;
    private boolean paused = false;

    public Field getSelectedField() {
        return selectedField;
    }

    public Field getField(Position position) {
        return matrix[position.getRow()][position.getColumn()];
    }

    public int getRows() {
        return rows;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public Player getPlayer() {
        return player;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    public Terrain(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        setLayout(new GridLayout(rows, columns, 2, 2));
        setPreferredSize(TERRAIN_SIZE);
        setFocusable(true);
        matrix = new Field[rows][columns];
        coins = new ArrayList<>();
        ghosts = new ArrayList<>();

        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new Passage(this, new Position(i, j));
                add(matrix[i][j]);
            }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!Game.getStarted()) return;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) 
                    DialogManager.showPauseDialog((Game)Terrain.this.getParent(), Terrain.this);
                if (player != null && !paused) movePlayer(e);
            }
        });
            
    }
    
    private void movePlayer(KeyEvent e) {
        Position currPosition = player.getPosition();
        Position nextPosition;
        Field currField = getField(player.getPosition());
        Field nextField;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                nextPosition = currPosition.getPosition(Position.Direction.UP);
                if (nextPosition.getRow() < 0) return;
                player.setFacing(Position.Direction.UP);
                break;
            case KeyEvent.VK_A:
                nextPosition = currPosition.getPosition(Position.Direction.LEFT);
                if (nextPosition.getColumn() < 0) return;
                player.setFacing(Position.Direction.LEFT);
                break;
            case KeyEvent.VK_S:
                nextPosition = currPosition.getPosition(Position.Direction.DOWN);
                if (nextPosition.getRow() >= rows) return;
                player.setFacing(Position.Direction.DOWN);
                break;
            case KeyEvent.VK_D:
                nextPosition = currPosition.getPosition(Position.Direction.RIGHT);
                if (nextPosition.getColumn() >= columns) return;
                player.setFacing(Position.Direction.RIGHT);
                break;
            default:
                return;
        }

        player.setMoved(true);
        nextField = getField(nextPosition);
        if (nextField.accessible()) {
            if (nextField.getFigure() instanceof WeakGhost || 
                nextField.getFigure() instanceof StrongGhost) { 
                DialogManager.showWinDialog((Game)getParent(), this, false);
                currField.setFigure(null);
                currField.repaint();
                return; 
            }
            if (nextField.getFigure() instanceof Coin) {
                coins.remove(nextField.getFigure());
                SoundManager.playSound("coinCollected.wav");
            }
            currField.setFigure(null);
            nextField.setFigure(player);
            player.setPosition(nextPosition);
            currField.repaint();
            nextField.repaint();
            
            if (coins.isEmpty()) DialogManager.showWinDialog((Game)getParent(), this, true);
        }

    }
    
    public void selected(Field field) {
        if (selectedField == null) { 
            field.select(true);
            selectedField = field;
            return;
        }
        if (field.getPosition().equals(selectedField.getPosition())) {
            field.select(false);
            selectedField = null;
            return;
        }
        field.select(true);
        selectedField.select(false);
        selectedField = field;
    }
    
    public void switchSelected(Field field, Figure figure) {
        if (selectedField.getFigure() != null) return;

        if (field == null) field = selectedField;
        if (figure != null) {
            if (figure instanceof Player) addPlayer((Player)figure);
            if (figure instanceof Coin) addCoin((Coin)figure);
            if (figure instanceof WeakGhost) addGhost((WeakGhost)figure);
            if (figure instanceof StrongGhost) addGhost((StrongGhost)figure);
        }

        Position pos = selectedField.getPosition();
        int row = pos.getRow();
        int column = pos.getColumn();

        remove(row * columns + column);
        matrix[row][column] = field;
        add(matrix[row][column], row * columns + column);
        selectedField = matrix[row][column];
        selectedField.select(true);
        revalidate();
        repaint();
    }
    
    public void generate() {
        removeAll();
        matrix = new Field[rows][columns];
        coins.clear();
        ghosts.clear();

        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++) {
                Position pos = new Position(i, j);
                matrix[i][j] = (Math.random() <= 0.3) ? new Wall(this, pos) : new Passage(this, pos);
                if (Math.random() <= 0.1 && matrix[i][j].accessible()) addCoin(new Coin(this, pos));
                add(matrix[i][j]);
            }
        revalidate();
        repaint();

        addPlayer(new Player(this, getRandomPosition(this)));
        addGhost(new WeakGhost(this, getRandomPosition(this)));
        addGhost(new WeakGhost(this, getRandomPosition(this)));
        addGhost(new StrongGhost(this, getRandomPosition(this)));
        addGhost(new StrongGhost(this, getRandomPosition(this)));
    }
    
    public void reset() {
        removeAll();
        matrix = new Field[rows][columns];
        coins.clear();
        ghosts.clear();

        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++) {
                Position pos = new Position(i, j);
                matrix[i][j] = new Passage(this, pos);
                add(matrix[i][j]);
            }
        revalidate();
        repaint();
    }
    
    public void addPlayer(Player player) {
        if (this.player == null)    
            this.player = player;
        else {
            getField(this.player.getPosition()).setFigure(null);
            getField(this.player.getPosition()).repaint();
            this.player.setPosition(getSelectedField().getPosition());
        }
        getField(this.player.getPosition()).setFigure(this.player);
        getField(this.player.getPosition()).repaint();
    }

    public void addCoin(Coin coin) {
        if (coins.contains(coin)) return;
        coins.add(coin);
        getField(coin.getPosition()).setFigure(coin);
        getField(coin.getPosition()).repaint();
    }

    public void addGhost(Figure ghost) {
        if (ghosts.contains(ghost)) return;
        ghosts.add(ghost);
        getField(ghost.getPosition()).setFigure(ghost);
        getField(ghost.getPosition()).repaint();
    }
    
    public Position getRandomPosition(Terrain terrain) {
        Position rndPos;
        do {
            int posX = (int)(Math.random() * terrain.getRows());
            int posY = (int)(Math.random() * terrain.getColumns());
            rndPos = new Position(posX, posY);
        } while(!terrain.getField(rndPos).accessible() || 
                terrain.getField(rndPos).getFigure() != null);
        return rndPos;
    }
    
    public boolean playable() {
        return (player != null && coins.size() > 0) ? true : false;
    }
    
    public void startGame() {
        FigureManager.startMovement(ghosts);
        paused = false;
        requestFocus();
    }
    
    public void pauseGame() {
        FigureManager.pauseMovement(ghosts);
        paused = true;
    }
    
    public void endGame() {
        FigureManager.stopMovement(ghosts);
        player = null;
    }

}
