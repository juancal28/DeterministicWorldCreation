# Deterministic World Creation

A 2D tile-based dungeon exploration game in Java that procedurally generates worlds using seed-based deterministic algorithms. Enter the same seed, get the exact same world every time.

## How It Works

The world generation pipeline:

1. **Chunk layout** - The 60x45 grid is divided into a 3-row layout with varying column counts, creating 11-14 placement zones depending on the seed.
2. **Room placement** - Rooms of randomized dimensions (3x3 up to 7x8) are placed within each zone. Collision detection shrinks or skips rooms that would overlap.
3. **Hallway generation** - Rooms are connected using **Prim's Minimum Spanning Tree algorithm**, ensuring all rooms are reachable with minimal total hallway length. Hallways are L-shaped corridors with walls.
4. **Avatar spawn** - The player avatar (`@`) is placed on the first available floor tile.

Because all random generation is seeded, the entire process is fully reproducible.

## Features

- **Seed-based world generation** - Reproducible worlds from any `long` seed value
- **Line of sight** - Toggle a fog-of-war mode with a 5-tile vision radius using Bresenham's line algorithm
- **Save/Load** - Game state is persisted by storing the seed + movement inputs, then replaying them on load
- **HUD** - Displays the tile type under the mouse cursor
- **Pause menu** - Return to the main menu mid-game

## Controls

| Key | Action |
|-----|--------|
| `N` | New game (enter seed, then press `G` to start) |
| `L` | Load most recent save |
| `Q` | Quit |
| `W/A/S/D` | Move up / left / down / right |
| `V` | Toggle line-of-sight mode |
| `M` | Pause / return to menu |
| `:Q` | Save and quit |

## Tech Stack

- **Java** with Princeton's [algs4 StdDraw](https://algs4.cs.princeton.edu/code/) library for rendering
- Custom 2D tile engine (`tileengine` package)

## Project Structure

```
proj3/src/
├── core/
│   ├── Main.java         # Entry point and game loop
│   ├── World.java        # World generation (rooms, hallways, line of sight)
│   ├── Avatar.java       # Player movement and sight updates
│   ├── MainMenu.java     # Menu system
│   ├── RoomGraph.java    # Graph structure for Prim's MST
│   ├── HUD.java          # Heads-up display
│   ├── Saving.java       # Save/load persistence
│   └── Metadata.java     # Game state (seed, inputs, name)
├── tileengine/
│   ├── TETile.java       # Tile representation
│   ├── Tileset.java      # Tile type definitions
│   └── TERenderer.java   # Frame rendering
└── utils/
    └── RandomUtils.java  # Deterministic random helpers
```

## Running

Requires Java and the [algs4.jar](https://algs4.cs.princeton.edu/code/algs4.jar) library on the classpath.

```bash
# Compile
javac -cp ".:algs4.jar" proj3/src/core/*.java proj3/src/tileengine/*.java proj3/src/utils/*.java

# Run
java -cp ".:algs4.jar:proj3/src" core.Main
```

Or import the project into an IDE (IntelliJ, Eclipse) and add `algs4.jar` as a dependency.

## Algorithms

- **Prim's MST** - Connects rooms with minimal total hallway distance using a priority queue
- **Bresenham's line algorithm** - Calculates line-of-sight visibility through walls
- **Chunk-based placement** - Prevents room clustering by partitioning the grid into zones
