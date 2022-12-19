# evp-chess-1-5

### *WIP*

---

Chess Board coordinates

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
| 1 |   |   |   |   |   |   |   |
| 2 |   |   |   |   |   |   |   |
| 3 |   |   |   |   |   |   |   |
| 4 |   |   |   |   |   |   |   |
| 5 |   |   |   |   |   |   |   |
| 6 |   |   |   |   |   |   |   |
| 7 |   |   |   |   |   |   |   |

---

All moves are in order from top left and rotating clockwise

---

Standard piece movement coordinates

| -1;-1 | 0;-1 | 1;-1 |
|-------|------|------|
| -1;0  | 0;0  | 1;0  |
| -1;1  | 0;1  | 1;1  |

Knight coordinates

|       | -1;-2 |     | 1;-2 |      |
|-------|-------|-----|------|------|
| -2;-1 |       |     |      | 2;-1 |
|       |       | 0;0 |      |      |
| -2;1  |       |     |      | 2;1  |
|       | -1;2  |     | 1;2  |      |

---

For readability, the following will be used to represent a coordinate
`[x, y]` where `x` is the column we are in and `y` is the row we are in.

---

## Development Commands

 - `<Ctrl> + <Alt> + <R>` - Reload the chess board
 - `<Ctrl> + <Alt> + <S>` - End game with stalemate (draw)
 - `<Ctrl> + <Alt> + <C>` - End game with checkmate (white wins)
 - `<Alt> + <C>` - End game with checkmate (black wins)
 - `<Ctrl> + <Alt> + <T>` - End game with time out (white wins)
 - `<Alt> + <T>` - End game with time out (black wins)
