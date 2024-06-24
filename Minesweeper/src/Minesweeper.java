
/**
 * Version - 1.18
 * This is a simple minesweeper game made in java.
 *
 * Author - Cooper Laing
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class gui implements MouseListener {
    private static int SCREEN_WIDTH = 1050;
    private static int SCREEN_HEIGHT = 900;
    private int difficulty = 2; //The overall difficulty variable which any difficulty dependent value uses
    private int gridSize[] = { 8, 10, 14, 18, 20, 24 }; //Array to store grid sizes and uses the difficulty variable to retrieve correct size
    private int flagSize[] = { 10, 40, 99 }; //The flag count for each difficulty
    private int easyGrid[][] = new int[gridSize[0]][gridSize[1]]; //Array to store nearby mine counter for easy mode
    private int mediumGrid[][] = new int[gridSize[2]][gridSize[3]]; //Array to store nearby mine counter for medium mode
    private int hardGrid[][] = new int[gridSize[4]][gridSize[5]]; //Array to store nearby mine counter for hard mode
    private JFrame jframe = new JFrame("Minesweeper game"); // create JFrame objects
    private JButton tiles[][] = new JButton[20][24]; //The array that stores all of the clickable game tiles
    private boolean flagged[][] = new boolean[20][24]; //Stores whether each tile is flagged or not
    private boolean bombs[][] = new boolean[20][24]; //Stores whether each tile is a bomb or not
    private boolean visible[][] = new boolean[20][24]; //Stores whether each tile is visible or not
    private int coloursInt = 1; //Stores what colour scheme is active
    private int flags = 0; //Stores how many flags are remaining
    private JLabel flagsLabel = new JLabel("Flags left: " + flags); //Flag count label
    private boolean gameRunning = true; //Stores whether the game is running or not

    ImageIcon temp1 = new ImageIcon("MinesweeperCSC223/Minesweeper/assets/resetButtonImage.png"); //Defines the source image as an icon
    Image image = temp1.getImage(); //Converts to an standard image for resizing
    Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH); //Resizes the image
    ImageIcon resetImage = new ImageIcon(newimg); //Converts the image back to an image icon.

    //See above
    ImageIcon temp2 = new ImageIcon("MinesweeperCSC223/Minesweeper/assets/flagImage.png");
    Image image2 = temp2.getImage();
    Image newimg2 = image2.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    ImageIcon flagImage = new ImageIcon(newimg2);

    //See above
    ImageIcon temp4 = new ImageIcon("MinesweeperCSC223/Minesweeper/assets/bombImage.png");
    Image image4 = temp4.getImage();
    Image newimg4 = image4.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    ImageIcon bombImage = new ImageIcon(newimg4);

    JButton reset = new JButton(resetImage); //Reset button
    JMenuItem easy = new JMenuItem("Easy"); //Easy button
    JMenuItem medium = new JMenuItem("Medium"); //Medium button
    JMenuItem hard = new JMenuItem("Hard"); //Hard button
    JMenuItem white = new JMenuItem("White"); //White button
    JMenuItem green = new JMenuItem("Green (default)"); //Green button
    JMenuItem blue = new JMenuItem("Blue"); //Blue button
    JMenuItem blueAndRed = new JMenuItem("Blue and Red"); //Blue and red button
    JMenuItem purpleAndGreen = new JMenuItem("Purple and Green"); //Purple and green button
    int elapsedTime = 0; //Stores how long the curren t game has been going
    JLabel time = new JLabel("Time: " + elapsedTime + "s"); //Time label
    boolean plantedMines = false; //Stores whether the mines hae been planted yet

    Timer timer = new Timer(1000, new ActionListener() { //Timer which is used for timing the game.
        public void actionPerformed(ActionEvent e) {
            updateTimeLabel();
        }
    });

    /*
     * This method is used once in the guiMethod method.
     * All this method does is take the golbal difficulty variable and 
     * output the difficulty in string form for use in labels.
     */

    public String difficultyPicker() {
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
    }

    /* 
     * This method is used when I need to access a difficulty sepcific grid.
     * All this method does is take the golbal difficulty variable and 
     * then outputs the appropriate grid based on it.
    */

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

    /* 
     * This method is used when you first start the game, 
     * when you are changing difficulties and when you reset the game.
     * This method starts with removing all of the mouse listeners to remove a buildup of lag if it is called multiple times.
     * It then defines the two jpanels used in the game and sets up the jframe.
     * It then sets up the score panel.
     * Then it fills the tiles array with jbuttons and sets them all up appropriately.
     * After that it then adds all of the j components for the game to their appropriately.
    */

    public void guiMethod() {
        //Remove previous listeners/panels
        jframe.getContentPane().removeAll();
        easy.removeMouseListener(this);
        medium.removeMouseListener(this);
        hard.removeMouseListener(this);
        reset.removeMouseListener(this);
        green.removeMouseListener(this);
        white.removeMouseListener(this);
        blue.removeMouseListener(this);
        blueAndRed.removeMouseListener(this);
        purpleAndGreen.removeMouseListener(this);

        //Sets up jpanels and jframe
        JPanel score = new JPanel();
        JPanel playArea = new JPanel();
        playArea.setLayout(new GridLayout(gridSize[2 * difficulty - 2], gridSize[2 * difficulty - 1])); //Sets up a gridlayout for the main play area
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(SCREEN_WIDTH, SCREEN_HEIGHT); //Set size of GUI screen
        jframe.setVisible(true);

        //Sets up labels
        elapsedTime = 0; //Resets the timer count
        time.setText("Time: " + elapsedTime + "s"); //Sets the timer text
        JMenuBar menu = new JMenuBar(); //Defines the menu bar
        JMenu difficultyDropdown = new JMenu("Difficulty"); //Adds difficulty menu
        JMenu colours = new JMenu("Colours"); //Adds colour menu
        JLabel difficultyLabel = new JLabel("Difficulty: " + difficultyPicker()); //Sets the difficulty label
        flags = flagSize[difficulty - 1]; //Sets the flag count
        flagsLabel.setText("Flags left: " + flags); //Updated flag label

        //Fills tiles array
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                tiles[y][x] = new JButton();
                tiles[y][x].addMouseListener(this); //Makes each tile listen for a mouse click
                tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 20)); 
                tiles[y][x].setBorderPainted(false); //Removes button border
                tiles[y][x].setMargin(new Insets(0, 0, 0, 0)); //Allows for larger text
                playArea.add(tiles[y][x]);
            }
        }
        drawPattern(); //Correctly sets colour scheme

        //Adds components and their listeners
        menu.add(difficultyDropdown);
        menu.add(colours);
        difficultyDropdown.add(easy);
        difficultyDropdown.add(medium);
        difficultyDropdown.add(hard);
        colours.add(green);
        colours.add(white);
        colours.add(blue);
        colours.add(blueAndRed);
        colours.add(purpleAndGreen);
        easy.addMouseListener(this);
        medium.addMouseListener(this);
        hard.addMouseListener(this);
        reset.addMouseListener(this);
        green.addMouseListener(this);
        white.addMouseListener(this);
        blue.addMouseListener(this);
        blueAndRed.addMouseListener(this);
        purpleAndGreen.addMouseListener(this);
        score.add(reset);
        score.add(flagsLabel);
        score.add(time);
        score.add(menu);
        score.add(difficultyLabel);

        //Assign jpanel positions and set visible
        jframe.getContentPane().add(BorderLayout.NORTH, score);
        jframe.getContentPane().add(BorderLayout.CENTER, playArea);
        jframe.setVisible(true);
    }

    /*
     * This method is used when you click or change colour schemes.
     * This method updates the tiles and colour schemes when called.
     */

    public void updateScreen() {
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                int currentGrid[][] = gridPicker();
                if (flagged[y][x]) {
                    tiles[y][x].setIcon(flagImage);
                    tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 0));
                } else if (currentGrid[y][x] > 0 && visible[y][x]) {
                    tiles[y][x].setText(Integer.toString(currentGrid[y][x]));
                } else if (!flagged[y][x]) {
                    tiles[y][x].setIcon(null);
                    tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 20));
                }
            }
        }
        drawPattern();
        System.out.println("Screen updated");
    }

    /*
     * This method is called when you first start the game and when you update the screen.
     * In this method it defines all of the nessiscary colours for the game.
     * It then loops through the grid first checking whether the tile is visible and 
     * then if it isn't visible then in a checkered pattern, colouring two colours determined by the colorsInt variable.
     * If the tile is visible it does the same process with a different two colours also based off of the colorsInt variable.
     */

    public void drawPattern() {
        //Define the required colours
        Color greenLight = new Color(167, 217, 72);
        Color greenDark = new Color(142, 204, 57);
        Color greenDarkBackground = new Color(215, 184, 153);
        Color greenLightBackground = new Color(229, 194, 159);
        Color whiteLight = new Color(255, 255, 255);
        Color whiteDark = new Color(200, 200, 200);
        Color whiteLightBackground = new Color(130, 130, 130);
        Color whiteDarkBackground = new Color(110, 110, 110);
        Color blueLight = new Color(123, 191, 205);
        Color blueDark = new Color(78, 181, 180);
        Color blueLightBackground = new Color(51, 120, 156);
        Color blueDarkBackground = new Color(53, 87, 128);
        Color blueAndRedLight = new Color(58, 147, 195);
        Color blueAndRedDark = new Color(16, 101, 171);
        Color blueAndRedLightBackground = new Color(215, 95, 76);
        Color blueAndRedDarkBackground = new Color(179, 21, 41);
        Color purpleAndGreenLight = new Color(92, 174, 99);
        Color purpleAndGreenDark = new Color(27, 121, 57);
        Color purpleAndGreenLightBackground = new Color(152, 110, 172);
        Color purpleAndGreenDarkBackground = new Color(116, 40, 129);

        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                //Check for covered cells
                if (!visible[y][x]) {
                    if (y % 2 == 0) {
                        if (x % 2 == 0) {
                            switch (coloursInt) { //Pick a colour using the colorsInt variable
                                case 0:
                                    tiles[y][x].setBackground(whiteLight);
                                    break;
                                case 1:
                                    tiles[y][x].setBackground(greenLight);
                                    break;
                                case 2:
                                    tiles[y][x].setBackground(blueLight);
                                    break;
                                case 3:
                                    tiles[y][x].setBackground(blueAndRedLight);
                                    break;
                                case 4:
                                    tiles[y][x].setBackground(purpleAndGreenLight);
                                    break;
                                default:
                                    tiles[y][x].setBackground(greenLight);
                                    break;
                            }
                        } else {
                            switch (coloursInt) {
                                case 0:
                                    tiles[y][x].setBackground(whiteDark);
                                    break;
                                case 1:
                                    tiles[y][x].setBackground(greenDark);
                                    break;
                                case 2:
                                    tiles[y][x].setBackground(blueDark);
                                    break;
                                case 3:
                                    tiles[y][x].setBackground(blueAndRedDark);
                                    break;
                                case 4:
                                    tiles[y][x].setBackground(purpleAndGreenDark);
                                    break;
                                default:
                                    tiles[y][x].setBackground(greenDark);
                                    break;
                            }
                        }
                    } else if (!(x % 2 == 0)) {
                        switch (coloursInt) {
                            case 0:
                                tiles[y][x].setBackground(whiteLight);
                                break;
                            case 1:
                                tiles[y][x].setBackground(greenLight);
                                break;
                            case 2:
                                tiles[y][x].setBackground(blueLight);
                                break;
                            case 3:
                                tiles[y][x].setBackground(blueAndRedLight);
                                break;
                            case 4:
                                tiles[y][x].setBackground(purpleAndGreenLight);
                                break;
                            default:
                                tiles[y][x].setBackground(greenLight);
                                break;
                        }
                    } else {
                        switch (coloursInt) {
                            case 0:
                                tiles[y][x].setBackground(whiteDark);
                                break;
                            case 1:
                                tiles[y][x].setBackground(greenDark);
                                break;
                            case 2:
                                tiles[y][x].setBackground(blueDark);
                                break;
                            case 3:
                                tiles[y][x].setBackground(blueAndRedDark);
                                break;
                            case 4:
                                tiles[y][x].setBackground(purpleAndGreenDark);
                                break;
                            default:
                                tiles[y][x].setBackground(greenDark);
                                break;
                        }
                    }
                } else {
                    if (y % 2 == 0) {
                        if (x % 2 == 0) {

                            switch (coloursInt) {
                                case 0:
                                    tiles[y][x].setBackground(whiteLightBackground);
                                    break;
                                case 1:
                                    tiles[y][x].setBackground(greenLightBackground);
                                    break;
                                case 2:
                                    tiles[y][x].setBackground(blueLightBackground);
                                    break;
                                case 3:
                                    tiles[y][x].setBackground(blueAndRedLightBackground);
                                    break;
                                case 4:
                                    tiles[y][x].setBackground(purpleAndGreenLightBackground);
                                    break;
                                default:
                                    tiles[y][x].setBackground(greenLightBackground);
                                    break;
                            }
                        } else {

                            switch (coloursInt) {
                                case 0:
                                    tiles[y][x].setBackground(whiteDarkBackground);
                                    break;
                                case 1:
                                    tiles[y][x].setBackground(greenDarkBackground);
                                    break;
                                case 2:
                                    tiles[y][x].setBackground(blueDarkBackground);
                                    break;
                                case 3:
                                    tiles[y][x].setBackground(blueAndRedDarkBackground);
                                    break;
                                case 4:
                                    tiles[y][x].setBackground(purpleAndGreenDarkBackground);
                                    break;
                                default:
                                    tiles[y][x].setBackground(greenDarkBackground);
                                    break;
                            }
                        }
                    } else if (!(x % 2 == 0)) {

                        switch (coloursInt) {
                            case 0:
                                tiles[y][x].setBackground(whiteLightBackground);
                                break;
                            case 1:
                                tiles[y][x].setBackground(greenLightBackground);
                                break;
                            case 2:
                                tiles[y][x].setBackground(blueLightBackground);
                                break;
                            case 3:
                                tiles[y][x].setBackground(blueAndRedLightBackground);
                                break;
                            case 4:
                                tiles[y][x].setBackground(purpleAndGreenLightBackground);
                                break;
                            default:
                                tiles[y][x].setBackground(greenLightBackground);
                                break;
                        }
                    } else {

                        switch (coloursInt) {
                            case 0:
                                tiles[y][x].setBackground(whiteDarkBackground);
                                break;
                            case 1:
                                tiles[y][x].setBackground(greenDarkBackground);
                                break;
                            case 2:
                                tiles[y][x].setBackground(blueDarkBackground);
                                break;
                            case 3:
                                tiles[y][x].setBackground(blueAndRedDarkBackground);
                                break;
                            case 4:
                                tiles[y][x].setBackground(purpleAndGreenDarkBackground);
                                break;
                            default:
                                tiles[y][x].setBackground(greenDarkBackground);
                                break;
                        }
                    }
                }
            }
        }
    }

    /*
     * This method is called every second because of the timer earlier defined.
     * All this method does is update the time count and then the time label.
     */

    public void updateTimeLabel() {
        elapsedTime++;
        time.setText("Time: " + elapsedTime + "s");
    }

    /*
     * This method is called once per game when the player clicks and the minesPlanted variable is false and the gameRunning variable is true.
     * In this method It first plants the mines in random positions on the board whilst not planting any withing the 3x3 around the player click.
     * It then resets the temporary nearbyMines array 
     */

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
                    }
                }
            }
            gameRunning = false;
            timer.stop();
            Timer delayTimer = new Timer(100, e -> gameOver(0));
            delayTimer.setRepeats(false);
            delayTimer.start();
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
            Timer delayTimer2 = new Timer(100, e -> gameOver(1));
            delayTimer2.setRepeats(false);
            delayTimer2.start();
        }
    }

    public void stopListeners() {
        jframe.getContentPane().removeAll();
        easy.removeMouseListener(this);
        medium.removeMouseListener(this);
        hard.removeMouseListener(this);
        reset.removeMouseListener(this);
        green.removeMouseListener(this);
        white.removeMouseListener(this);
        blue.removeMouseListener(this);
        blueAndRed.removeMouseListener(this);
        purpleAndGreen.removeMouseListener(this);
    }

    public void gameOver(int state) {
        if (state == 0) {
            try{
                Thread.sleep(2500);
            }catch(InterruptedException e){
                System.out.println(e);
            }
            stopListeners();
            JButton endScreen = new JButton("<html>Game Over! You Lost!<br>Press the button to try again.</html>");
            endScreen.setBounds(SCREEN_WIDTH / 5, SCREEN_HEIGHT / 5, (SCREEN_WIDTH / 5) * 3, (SCREEN_HEIGHT / 5) * 3);
            endScreen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetGrids();
                    guiMethod();
                }
            });
            endScreen.setMargin(new Insets(0, 0, 0, 0));
            endScreen.setFont(new Font("Serif", Font.PLAIN, 50));
            jframe.add(endScreen);
            endScreen.repaint();
        } else {
            stopListeners();
            JButton endScreen = new JButton(
                    "<html>You won in " + elapsedTime + " seconds!<br>Press the button to play again.</html>");
            endScreen.setBounds(SCREEN_WIDTH / 5, SCREEN_HEIGHT / 5, (SCREEN_WIDTH / 5) * 3, (SCREEN_HEIGHT / 5) * 3);
            endScreen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetGrids();
                    guiMethod();
                }
            });
            endScreen.setMargin(new Insets(0, 0, 0, 0));
            endScreen.setFont(new Font("Serif", Font.PLAIN, 50));
            jframe.add(endScreen);
        }
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
                        } else if (!visible[y][x]) {
                            flags--;
                            flagsLabel.setText("Flags left: " + flags);
                            flagged[y][x] = true;
                        }
                        updateScreen();
                    }
                }

            }
        }

        if (e.getSource() == white) {
            coloursInt = 0;
            System.out.println("white");
            updateScreen();
        } else if (e.getSource() == green) {
            coloursInt = 1;
            System.out.println("green");
            updateScreen();
        } else if (e.getSource() == blue) {
            coloursInt = 2;
            System.out.println("blue");
            updateScreen();
        } else if (e.getSource() == blueAndRed) {
            coloursInt = 3;
            System.out.println("blue and red");
            updateScreen();
        } else if (e.getSource() == purpleAndGreen) {
            coloursInt = 4;
            System.out.println("purple and green");
            updateScreen();
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