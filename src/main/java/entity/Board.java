package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = -1790261785521495991L;
    public static final int ROW = 4;
    public static final int[] _0123 = { 0, 1, 2, 3 };

    final startGame2048 host;

    private Tile[] tiles;

    public static Value GOAL = Value._2048;

    public Board(startGame2048 f) {
        host = f;
        setFocusable(true);
        initTiles();
    }


    public void initTiles() {
        tiles = new Tile[ROW * ROW];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.ZERO;
        }
        addTile();
        addTile();
        host.statusBar.setText("");
    }

    public void left() {
        boolean needAddTile = false;
        for (int i : _0123) {
            Tile[] origin = getLine(i);
            Tile[] afterMove = moveLine(origin);
            Tile[] merged = mergeLine(afterMove);
            setLine(i, merged);
            if (!needAddTile && !Arrays.equals(origin, merged)) {
                needAddTile = true;
            }
        }
        if (needAddTile) {
            addTile();
        }
    }


    public void right() {
        tiles = rotate(180);
        left();
        tiles = rotate(180);
    }

    public void up() {
        tiles = rotate(270);
        left();
        tiles = rotate(90);
    }

    public void down() {
        tiles = rotate(90);
        left();
        tiles = rotate(270);
    }

    private Tile tileAt(int x, int y) {
        return tiles[x + y * ROW];
    }

    private void addTile() {
        List<Integer> list = availableIndex();
        int idx = list.get((int) (Math.random() * list.size()));
        tiles[idx] = Tile.randomTile();
    }

    private List<Integer> availableIndex() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].empty())
                list.add(i);
        }
        return list;
    }

    private boolean isFull() {
        return availableIndex().size() == 0;
    }

    boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x : _0123) {
            for (int y : _0123) {
                Tile t = tileAt(x, y);
                if ((x < ROW - 1 && t.equals(tileAt(x + 1, y)))
                        || (y < ROW - 1 && t.equals(tileAt(x, y + 1)))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Tile[] rotate(int dgr) {
        Tile[] newTiles = new Tile[ROW * ROW];
        int offsetX = 3, offsetY = 3;
        if (dgr == 90) {
            offsetY = 0;
        } else if (dgr == 180) {
        } else if (dgr == 270) {
            offsetX = 0;
        } else {
            throw new IllegalArgumentException(
                    "Only can rotate 90, 180, 270 degree");
        }
        double radians = Math.toRadians(dgr);
        int cos = (int) Math.cos(radians);
        int sin = (int) Math.sin(radians);
        for (int x : _0123) {
            for (int y : _0123) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * ROW] = tileAt(x, y);
            }
        }
        return newTiles;
    }
    private Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<>();
        for (int i : _0123) {
            if (!oldLine[i].empty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i : _0123) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < ROW; i++) {
            if (i < ROW - 1 && !oldLine[i].empty()
                    && oldLine[i].equals(oldLine[i + 1])) {
                Tile merged = oldLine[i].getDouble();
                i++;
                list.add(merged);
                if (merged.value() == GOAL) {
                    host.win();
                }
            } else {
                list.add(oldLine[i]);
            }
        }
        ensureSize(list, 4);
        return list.toArray(new Tile[4]);
    }

    private static void ensureSize(List<Tile> l, int s) {
        while (l.size() < s) {
            l.add(Tile.ZERO);
        }
    }

    private Tile[] getLine(int idx) {
        Tile[] result = new Tile[4];
        for (int i : _0123) {
            result[i] = tileAt(i, idx);
        }
        return result;
    }

    private void setLine(int idx, Tile[] re) {
        for (int i : _0123) {
            tiles[i + idx * ROW] = re[i];
        }
    }

    private static final Color BG_COLOR = new Color(0xbbada0);

    private static final Font STR_FONT = new Font(Font.SANS_SERIF,
            Font.BOLD, 17);

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.setFont(STR_FONT);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        for (int y : _0123) {
            for (int x : _0123) {
                drawTile(g, tiles[x + y * ROW], x, y);
            }
        }
    }

    private static final int SIDE = 64;
    private static final int MARGIN = 16;


    private void drawTile(Graphics g, Tile tile, int x, int y) {
        Value val = tile.value();
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(val.color());
        g.fillRect(xOffset, yOffset, SIDE, SIDE);
        g.setColor(val.fontColor());
        if (val.score() != 0)
            g.drawString(tile.toString(), xOffset
                    + (SIDE >> 1) - MARGIN, yOffset + (SIDE >> 1));
    }

    private static int offsetCoors(int arg) {
        return arg * (MARGIN + SIDE) + MARGIN;
    }

}
