♞ Knight Pathfinder Visualizer
A modern, interactive JavaFX application designed to visualize and compare different pathfinding algorithms. The goal is to find the path for a Knight (Chess piece) to reach the Coin, while avoiding Bombs placed on the board.

✨ Features
Interactive Grid: Click to place the Start Node (Knight), Goal Node (Coin), and Obstacles (Bombs).

Modern UI: A sleek, dark-themed interface inspired by modern IDE aesthetics (Dracula/One Dark), featuring neon glow effects and smooth visuals.

Algorithm Comparison: Run multiple pathfinding algorithms simultaneously and compare them based on:

Execution Time (ms & ns)

Total Steps taken

Path Found status

Visual Path Tracing: Click on any result in the table to visualize the specific path taken by that algorithm directly on the board.

Dynamic Obstacles: Toggle bombs on/off by clicking on the grid.

🧠 Algorithms Implemented
The project implements the following search algorithms to solve the navigation problem:

Breadth-First Search (BFS): Explores all neighbors at the present depth prior to moving on to the nodes at the next depth level. Guarantees the shortest path in an unweighted grid.

Depth-First Search (DFS): Explores as far as possible along each branch before backtracking. Does not guarantee the shortest path.

*A Search (A-Star):** Uses heuristics to find the shortest path faster than BFS by prioritizing nodes closer to the goal.

Greedy Best-First Search: Explores the node that is estimated to be closest to the goal. Fast, but not always optimal.

Iterative Deepening DFS (IDDFS): Combines the space-efficiency of DFS with the completeness of BFS.

🛠️ Tech Stack
Language: Java 21

GUI Framework: JavaFX

Build Tool: Maven

IDE: Visual Studio Code

🚀 Getting Started
Prerequisites
Java Development Kit (JDK) 21

Apache Maven

Installation
Clone the repository:

Bash

git clone -b Knight-UI https://github.com/cemkagba/SENG465.git
cd knight-pathfinder
Build the project using Maven:

Bash

mvn clean install
Run the application:

Bash

mvn clean javafx:run
🎮 How to Use
Select a Tool: Use the radio buttons on the left panel to choose what to place:

Move Knight: Set the starting position.

Move Coin: Set the target destination.

Place Bomb: Add or remove obstacles.

Edit the Board: Click on the grid cells to apply your selection.

Run Comparison: Click the "RUN ALGORITHMS" button. The application will execute all algorithms.

Analyze Results: Check the bottom table for performance metrics.

Visualize: Click on a row in the results table to see the path calculated by that specific algorithm drawn on the board with a neon glow effect.

Reset: Use "CLEAR BOMBS" to remove obstacles or manually adjust positions.

📂 Project Structure
src
└── main
    └── java
        └── com
            └── knightpathfinder
                ├── ModernUI.java       # Main JavaFX Application & UI Logic
                ├── Board.java          # Grid and State Management
                ├── Node.java           # Graph Node Structure
                ├── PathFinder.java     # Interface for Algorithms
                ├── algorithms          # (Logic classes)
                │   ├── AStarSolver.java
                │   ├── BFSSolver.java
                │   ├── DFSSolver.java
                │   ├── IDDFS.java
                │   └── GreedyBestFirstSearch.java

                
👨‍💻 Author

Yiğit Tacir

Cem Kağba

Selin Samray

Semih Utku Canverdi
