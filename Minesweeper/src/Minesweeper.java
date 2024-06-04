
/**
 * Version - 1.9
 * This is a simple minesweeper game made in java.
 *
 * Author - Cooper Laing
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class gui implements MouseListener {
    private int difficulty = 2;
    private int gridSize[] = { 8, 10, 14, 18, 20, 24 };
    private int flagSize[] = { 10, 40, 99 };
    private int easyGrid[][] = new int[gridSize[0]][gridSize[1]];
    private int mediumGrid[][] = new int[gridSize[2]][gridSize[3]];
    private int hardGrid[][] = new int[gridSize[4]][gridSize[5]];
    private JFrame jframe = new JFrame("Minesweeper game"); // create JFrame objects
    private JButton tiles[][] = new JButton[20][24]; // -1 = bomb | 0 = safe to click | 1-8 = no. of mines nearby
    private boolean flagged[][] = new boolean[20][24];
    private boolean bombs[][] = new boolean[20][24];
    private boolean visible[][] = new boolean[20][24];
    private int coloursInt = 0;
    private int flags = 0;
    private JLabel flagsLabel = new JLabel("Flags left: " + flags);
    private boolean gameRunning = true;

    ImageIcon temp1 = new ImageIcon("MinesweeperCSC223\\Minesweeper\\src\\783503.png");
    Image image = temp1.getImage();
    Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
    ImageIcon resetImage = new ImageIcon(newimg);

    ImageIcon temp2 = new ImageIcon("MinesweeperCSC223\\Minesweeper\\src\\7628490.png");
    Image image2 = temp2.getImage();
    Image newimg2 = image2.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    ImageIcon flagImage = new ImageIcon(newimg2);

    ImageIcon temp3 = new ImageIcon("MinesweeperCSC223\\Minesweeper\\src\\Eo_circle_green_checkmark.svg.png");
    Image image3 = temp3.getImage();
    Image newimg3 = image3.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    ImageIcon uncoveredButton = new ImageIcon(newimg3);

    ImageIcon temp4 = new ImageIcon("MinesweeperCSC223\\Minesweeper\\src\\ae0d1e80-6f46-11e9-96b3-b7757a65a1c7.png");
    Image image4 = temp4.getImage();
    Image newimg4 = image4.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    ImageIcon bombImage = new ImageIcon(newimg4);

    JButton reset = new JButton(resetImage);
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");
    JMenuItem whiteAndGrey = new JMenuItem("White and Grey (default)");
    int elapsedTime = 0;
    JLabel time = new JLabel("Time: " + elapsedTime + "s");
    boolean plantedMines = false;

    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            updateTimeLabel();
        }
    });

    public String difficultyPicker(int choice) {
        if (choice == 0) {
            switch (difficulty) {
                case 1:
                    return ("Easy");
                case 2:
                    return ("Medium");
                case 3:
                    return ("Hard");
                default:
                    return ("N/A");
            }
        } else {
            return (null);
        }
    }

    public int[][] gridPicker() {
        switch (difficulty) {
            case 1:
                return (easyGrid);
            case 2:
                return (mediumGrid);
            case 3:
                return (hardGrid);
            default:
                return (null);
        }
    }

    public void guiMethod() {
        jframe.getContentPane().removeAll();

        JPanel score = new JPanel();
        JPanel playArea = new JPanel();
        playArea.setLayout(new GridLayout(gridSize[2 * difficulty - 2], gridSize[2 * difficulty - 1]));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(1050, 900); // set size of GUI screen
        jframe.setVisible(true);

        elapsedTime = 0;
        time.setText("Time: " + elapsedTime + "s");
        JMenuBar menu = new JMenuBar();
        JMenu difficultyDropdown = new JMenu("Difficulty");
        JMenu colours = new JMenu("Colours");
        JLabel difficultyLabel = new JLabel("Difficulty: " + difficultyPicker(0));

        flags = flagSize[difficulty - 1];
        flagsLabel.setText("Flags left: " + flags);

        // JButton tiles[][] = new
        // JButton[gridSize[2*difficulty-2]][gridSize[2*difficulty-1]];
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                // int currentGrid[][] = new int[gridSize[2 * difficulty - 2]][gridSize[2 *
                // difficulty - 1]];
                // currentGrid = gridPicker();
                /*
                 * if (currentGrid[y][x] == 0) {
                 * // tiles[y][x] = new JButton(uncoveredButton);
                 * tiles[y][x] = new JButton(Integer.toString(currentGrid[y][x]));
                 * } else if (currentGrid[y][x] == -2) {
                 * tiles[y][x] = new JButton();
                 * } else if (flagged[y][x]) {
                 * tiles[y][x] = new JButton(flagImage);
                 * } else {
                 * tiles[y][x] = new JButton(Integer.toString(currentGrid[y][x]));
                 * }
                 */
                tiles[y][x] = new JButton();
                tiles[y][x].addMouseListener(this);
                tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 10));
                tiles[y][x].setOpaque(true);
                tiles[y][x].setBorderPainted(false);
                playArea.add(tiles[y][x]);
            }
        }
        drawPattern();

        menu.add(difficultyDropdown);
        menu.add(colours);
        difficultyDropdown.add(easy);
        difficultyDropdown.add(medium);
        difficultyDropdown.add(hard);
        colours.add(whiteAndGrey);
        easy.addMouseListener(this);
        medium.addMouseListener(this);
        hard.addMouseListener(this);
        reset.addMouseListener(this);
        whiteAndGrey.addMouseListener(this);

        score.add(reset);
        score.add(flagsLabel);
        score.add(time);
        score.add(menu);
        score.add(difficultyLabel);

        jframe.getContentPane().add(BorderLayout.NORTH, score);
        jframe.getContentPane().add(BorderLayout.CENTER, playArea);
        jframe.setVisible(true);
    }

    public void updateScreen() {
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                int currentGrid[][] = new int[gridSize[2 * difficulty - 2]][gridSize[2 * difficulty - 1]];
                currentGrid = gridPicker();
                if (currentGrid[y][x] == 0 && visible[y][x]) {
                    tiles[y][x].setIcon(uncoveredButton);
                    tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 0));
                } else if (flagged[y][x]) {
                    tiles[y][x].setIcon(flagImage);
                    tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 0));
                } else if (currentGrid[y][x] > 0 && visible[y][x]) {
                    tiles[y][x].setText(Integer.toString(currentGrid[y][x]));
                } else if (!flagged[y][x]) {
                    tiles[y][x].setIcon(null);
                    tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 10));
                }
            }
        }
        drawPattern();
        System.out.println("Screen updated");
    }

    public void drawPattern() {
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                if (!visible[y][x]) {
                    if (y % 2 == 0) {
                        if (x % 2 == 0) {
                            switch (coloursInt) {
                                case 0:
                                    tiles[y][x].setBackground(Color.WHITE);
                                case 1:

                                default:
                                    tiles[y][x].setBackground(Color.WHITE);
                            }
                        } else {
                            switch (coloursInt) {
                                case 0:
                                    tiles[y][x].setBackground(Color.LIGHT_GRAY);
                                case 1:

                                default:
                                    tiles[y][x].setBackground(Color.LIGHT_GRAY);
                            }
                        }
                    } else if (!(x % 2 == 0)) {
                        switch (coloursInt) {
                            case 0:
                                tiles[y][x].setBackground(Color.WHITE);
                            case 1:

                            default:
                                tiles[y][x].setBackground(Color.WHITE);
                        }
                    } else {
                        switch (coloursInt) {
                            case 0:
                                tiles[y][x].setBackground(Color.LIGHT_GRAY);
                            case 1:

                            default:
                                tiles[y][x].setBackground(Color.LIGHT_GRAY);
                        }
                    }
                } else{
                    if (y % 2 == 0) {
                        if (x % 2 == 0) {
                            tiles[y][x].setBackground(new Color(242, 213, 178));
                        } else {
                            tiles[y][x].setBackground(new Color(209, 184, 154));
                        }
                    } else if (!(x % 2 == 0)) {
                        tiles[y][x].setBackground(new Color(242, 213, 178));
                    } else {
                        tiles[y][x].setBackground(new Color(209, 184, 154));
                    }
                }
            }
        }
    }

    private void updateTimeLabel() {
        elapsedTime++;
        time.setText("Time: " + elapsedTime + "s");
    }

    public void plantMines(int xx, int yy) {
        int z = 0;
        int grid[][] = gridPicker();
        int[][] nearbyMines = new int[gridSize[2 * difficulty - 2]][gridSize[2 * difficulty - 1]];
        while (z < flagSize[difficulty - 1] && !plantedMines) {
            int randX = (int) Math.floor(Math.random() * gridSize[2 * difficulty - 1]);
            int randY = (int) Math.floor(Math.random() * gridSize[2 * difficulty - 2]);
            if (!(grid[randY][randX] == -1)
                    && !(randX == xx - 1 && randY == yy - 1) && !(randX == xx && randY == yy - 1)
                    && !(randX == xx + 1 && randY == yy - 1)
                    && !(randX == xx - 1 && randY == yy) && !(randX == xx && randY == yy)
                    && !(randX == xx + 1 && randY == yy)
                    && !(randX == xx - 1 && randY == yy + 1) && !(randX == xx && randY == yy + 1)
                    && !(randX == xx + 1 && randY == yy + 1)) {
                grid[randY][randX] = -1;
                System.out.print(randX);
                System.out.print(randY + "b ");
                bombs[randY][randX] = true;
                z++;
            }
        }

        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                nearbyMines[y][x] = 0/* grid[y][x] */;
            }
        }

        System.out.println();
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }

        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                if (x > 0) {
                    if (grid[y][x - 1] == -1) {
                        nearbyMines[y][x]++;
                    }
                    if (y > 0) {
                        if (grid[y - 1][x - 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                    if (y + 1 < gridSize[2 * difficulty - 2]) {
                        if (grid[y + 1][x - 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                }
                if (x + 1 < gridSize[2 * difficulty - 1]) {
                    if (grid[y][x + 1] == -1) {
                        nearbyMines[y][x]++;
                    }
                    if (y > 0) {
                        if (grid[y - 1][x + 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                    if (y + 1 < gridSize[2 * difficulty - 2]) {
                        if (grid[y + 1][x + 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                }
                if (y > 0) {
                    if (grid[y - 1][x] == -1) {
                        nearbyMines[y][x]++;
                    }
                }
                if (y + 1 < gridSize[2 * difficulty - 2]) {
                    if (grid[y + 1][x] == -1) {
                        nearbyMines[y][x]++;
                    }
                }
                if (grid[y][x] == -1) {
                    nearbyMines[y][x]++;
                }

            }
        }
        grid = nearbyMines;

        System.out.println();
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }

        switch (difficulty) {
            case 1:
                easyGrid = grid;
                break;
            case 2:
                mediumGrid = grid;
                break;
            case 3:
                hardGrid = grid;
                break;
            default:
                break;
        }
        guiMethod();
        plantedMines = true;
    }

    public void resetGrids() {
        for (int y = 0; y < gridSize[0]; y++) {
            for (int x = 0; x < gridSize[1]; x++) {
                easyGrid[y][x] = 0;
            }
        }

        for (int y = 0; y < gridSize[2]; y++) {
            for (int x = 0; x < gridSize[3]; x++) {
                mediumGrid[y][x] = 0;
            }
        }

        for (int y = 0; y < gridSize[4]; y++) {
            for (int x = 0; x < gridSize[5]; x++) {
                hardGrid[y][x] = 0;
                flagged[y][x] = false;
                bombs[y][x] = false;
                visible[y][x] = false;
            }
        }
        flags = flagSize[difficulty - 1];
        plantedMines = false;
        gameRunning = true;
        timer.restart();
        timer.stop();
    }

    public void domainExpansion(int y, int x) {
        int grid[][] = gridPicker();

        if (grid[y][x] == 0) {
            for (int xx = x - 1; xx <= x + 1; xx++) {
                for (int yy = y - 1; yy <= y + 1; yy++) {
                    if (xx >= 0 && yy >= 0 && xx + 1 <= gridSize[2 * difficulty - 1]
                            && yy + 1 <= gridSize[2 * difficulty - 2]) {
                        if (!visible[yy][xx] && !flagged[yy][xx]) {
                            visible[yy][xx] = true;
                            domainExpansion(yy, xx);
                        }
                    }
                }
            }
        }
        if (grid[y][x] > 0) {
            if (!visible[y][x] && !flagged[y][x]) {
                visible[y][x] = true;
                domainExpansion(y, x);
            }
        }
    }

    public void winCheck(int yy, int xx) {
        if (bombs[yy][xx] && visible[yy][xx]) {
            for (int yyy = 0; yyy < gridSize[2 * difficulty - 2]; yyy++) {
                for (int xxx = 0; xxx < gridSize[2 * difficulty - 1]; xxx++) {
                    if (bombs[yyy][xxx]) {
                        tiles[yyy][xxx].setIcon(bombImage);
                        tiles[yyy][xxx].setFont(new Font("Serif", Font.PLAIN, 0));
                        gameRunning = false;
                        timer.stop();
                    }
                }
            }
        }
        int count = 0;
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                if (visible[y][x]) {
                    count++;
                }
            }
        }
        if (count == (gridSize[2 * difficulty - 2] * gridSize[2 * difficulty - 1]) - flagSize[difficulty - 1]) {
            gameRunning = false;
            timer.stop();
        }
        System.out.println(gridSize[2 * difficulty - 2]);
        System.out.println(gridSize[2 * difficulty - 1]);
        System.out.println(flagSize[difficulty - 1]);
        System.out.println((gridSize[2 * difficulty - 2] * gridSize[2 * difficulty - 1]) - flagSize[difficulty - 1]);
        System.out.println(count);
    }

    public void mousePressed(MouseEvent e) {
        // int currentGrid[][] = gridPicker();
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                if (tiles[y][x] == e.getSource() && e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("Clicked button at (x, y): (" + (x + 1) + ", " + (y + 1) + ")");
                    if (gameRunning) {
                        if (!plantedMines) {
                            plantMines(x, y);
                        }
                        if (!timer.isRunning()) {
                            timer.start();
                        }
                        domainExpansion(y, x);
                        updateScreen();
                        winCheck(y, x);
                    }
                } else if (tiles[y][x] == e.getSource() && e.getButton() == MouseEvent.BUTTON3) {
                    System.out.println("Flagged button at (x, y): (" + (x + 1) + ", " + (y + 1) + ")");
                    if (gameRunning) {
                        if (tiles[y][x].getIcon() == flagImage) {
                            flags++;
                            flagsLabel.setText("Flags left: " + flags);
                            flagged[y][x] = false;
                        } else {
                            flags--;
                            flagsLabel.setText("Flags left: " + flags);
                            flagged[y][x] = true;
                        }
                        updateScreen();
                    }
                } else if (e.getSource() == whiteAndGrey) {
                    if (y % 2 == 0) {
                        if (x % 2 == 0) {
                            tiles[y][x].setBackground(Color.WHITE);
                            tiles[y][x].setOpaque(true);
                            tiles[y][x].setBorderPainted(false);
                        } else {
                            tiles[y][x].setBackground(Color.LIGHT_GRAY);
                            tiles[y][x].setOpaque(true);
                            tiles[y][x].setBorderPainted(false);
                        }
                    } else if (!(x % 2 == 0)) {
                        tiles[y][x].setBackground(Color.WHITE);
                        tiles[y][x].setOpaque(true);
                        tiles[y][x].setBorderPainted(false);
                    } else {
                        tiles[y][x].setBackground(Color.LIGHT_GRAY);
                        tiles[y][x].setOpaque(true);
                        tiles[y][x].setBorderPainted(false);
                    }
                }

            }
        }

        if (e.getSource() == easy) {
            difficulty = 1;
            resetGrids();
            guiMethod();
        } else if (e.getSource() == medium) {
            difficulty = 2;
            resetGrids();
            guiMethod();
        } else if (e.getSource() == hard) {
            difficulty = 3;
            resetGrids();
            guiMethod();
        } else if (e.getSource() == reset) {
            resetGrids();
            guiMethod();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static void main(String args[]) {
        gui sm = new gui();
        sm.resetGrids();
        sm.guiMethod();
    }
}