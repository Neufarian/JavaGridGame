# Java Grid Game

A simple grid-based 2D Java game featuring a player, ghosts, coins, and walls. The goal is to collect all the coins while avoiding ghosts. The game uses multithreading, basic AWT UI dialogs, and sound effects to enhance gameplay.

## 🕹️ Features

- Player movement with keyboard input
- Two types of ghosts (strong and weak) with autonomous movement
- Coins to collect and win the game
- Walls and passages for obstacles and navigation
- Sound effect when a coin is collected
- Win and loss dialogs
- Thread-safe game logic

## 📁 Project Structure

src/
├── figures/ # Game elements like Player, Ghosts, Coin
├── logic/ # Core logic: Terrain, Field, Position, etc.
├── ui/ # User interface: Game window, Dialogs, Sound

## 🚀 How to Run

1. **Requirements**:  
   - Java 17 or later  
   - Any Java IDE (e.g. IntelliJ IDEA, VSCode)

2. **Build & Run**:  
   - Open the project in your IDE  
   - Compile all classes  
   - Run the `Game` class from the `ui` package  

3. **Controls**:  
   - W-A-S-D to move the player

## 🔊 Assets

- `coinCollected.wav` (must be located in the correct relative path)

## 🧠 Technical Highlights

- Custom game loop using threads
- Use of packages to separate concerns (`logic`, `ui`, `figures`)
- Dialog-driven UI with Java AWT
- Clean OOP design with base `Figure` class

## 📝 Author

- **Dejan Pavlović** – github.com/Neufarian

## 📄 License

This project is for educational/demo purposes and is not licensed for commercial use.
