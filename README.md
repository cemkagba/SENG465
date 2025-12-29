# 🏇 Knight Path Finder - Algorithm Visualizer

A beautiful JavaFX-based visualization tool for comparing pathfinding algorithms on a chess board with obstacles.

## ✨ Features

- **Interactive Chess Board**: Click to place bombs, select start/target positions
- **5 Search Algorithms**: IDDFS, BFS, DFS, A*, and Greedy Best-First Search
- **Beautiful UI**: Modern gradient design with smooth animations
- **Path Visualization**: Watch algorithms find paths step-by-step with animation
- **Performance Comparison**: Compare execution time and path length across all algorithms
- **Real-time Results**: See detailed statistics in formatted tables

## 🚀 Running the Application

### Prerequisites
- Java 21 or higher
- JavaFX SDK

### Option 1: Run the Animated UI (Recommended)
```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls src/KnightPathFinderUIAnimated.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls KnightPathFinderUIAnimated
```

### Option 2: Run the Standard UI
```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls src/KnightPathFinderUI.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls KnightPathFinderUI
```

### Option 3: Run the Console Version
```bash
cd src
javac Main.java
java Main
```

## 🎮 How to Use

1. **Set Start Position**: Select the knight's starting position from dropdown
2. **Set Target Position**: Choose where the knight should reach
3. **Place Bombs**: Click on chess board cells to place/remove bombs
4. **Choose Algorithm**: Select a single algorithm to visualize
5. **Visualize**: Click "🎬 Visualize Path" to see the algorithm in action
6. **Compare All**: Click "🚀 Compare All Algorithms" to run all algorithms and see performance comparison
7. **Reset**: Click "🔄 Reset Board" to start fresh

## 🎨 UI Components

### Main Features:
- **Visual Chess Board**: 8x8 interactive board with coordinates (a1-h8)
- **Control Panel**: Configure start, target, bombs, and algorithm selection
- **Animation Controls**: Toggle path animation on/off
- **Results Panel**: View detailed performance metrics and comparisons
- **Legend**: Visual guide for all board symbols

### Symbols:
- 🏇 Knight (Start Position)
- 🎯 Target (Goal)
- 💣 Bomb (Obstacle)
- 🟢 Path Step
- 🔵 Explored Cell

## 📊 Algorithms Implemented

1. **IDDFS** (Iterative Deepening DFS): Combines benefits of DFS and BFS
2. **BFS** (Breadth-First Search): Guarantees shortest path
3. **DFS** (Depth-First Search): Memory efficient but may not find shortest
4. **A*** (A-Star): Optimal and complete with heuristic guidance
5. **Greedy Best-First**: Fast but not always optimal

## 🏗️ Project Structure

```
src/
├── KnightPathFinderUIAnimated.java  # Main UI with animations
├── KnightPathFinderUI.java          # Standard UI version
├── Main.java                        # Console version
├── Board.java                       # Chess board representation
├── Node.java                        # Path node structure
├── IDDFS.java                       # IDDFS implementation
├── BFSSolver.java                   # BFS implementation
├── DFSSolver.java                   # DFS implementation
├── AStarSolver.java                 # A* implementation
└── GreedyBestFirstSearch.java       # Greedy implementation
```

## 🛠️ Technical Details

- **Language**: Java 21
- **UI Framework**: JavaFX
- **Board Size**: 8x8 chess board
- **Knight Moves**: Standard L-shaped chess knight movements
- **Visualization**: Animated path rendering with Timeline API

## 📝 Development Notes

- Each algorithm implements its own search logic
- Board class handles obstacle detection and validation
- UI runs algorithms in background threads to prevent freezing
- Path animation uses JavaFX Timeline for smooth transitions

## 🎓 Educational Purpose

This project demonstrates:
- Various search algorithm implementations
- Algorithm performance comparison
- JavaFX UI development
- Thread-safe UI updates
- Animation and visualization techniques

---

**Author**: Sen465 Project Team  
**Course**: SENG 465  
**Year**: 2025
