
/**
 * Version - 1.21
 * This is a simple minesweeper game made in java.
 *
 * Author - Cooper Laing
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

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
    private File gamesPlayed = new File("MinesweeperCSC223/Minesweeper/assets/gamesPlayed.txt");
    private int games = 0;
    private JLabel gameCounter = new JLabel("|  Games played (all time): " + games);

    private ImageIcon temp1 = new ImageIcon("MinesweeperCSC223/Minesweeper/assets/resetButtonImage.png"); //Defines the source image as an icon
    private Image image1 = temp1.getImage(); //Converts to an standard image for resizing
    private Image newimg1 = image1.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH); //Resizes the image
    private ImageIcon resetImage = new ImageIcon(newimg1); //Converts the image back to an image icon.

    //See above
    private ImageIcon temp2 = new ImageIcon("MinesweeperCSC223/Minesweeper/assets/flagImage.png");
    private Image image2 = temp2.getImage();
    private Image newimg2 = image2.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon flagImage = new ImageIcon(newimg2);

    //See above
    private ImageIcon temp3 = new ImageIcon("MinesweeperCSC223/Minesweeper/assets/bombImage.png");
    private Image image3 = temp3.getImage();
    private Image newimg3 = image3.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon bombImage = new ImageIcon(newimg3);

    //See above
    private ImageIcon temp4 = new ImageIcon("Minesweeper/assets/dropdownArrow.png");
    private Image image4 = temp4.getImage();
    private Image newimg4 = image4.getScaledInstance(12, 12, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon dropdownImage = new ImageIcon(newimg4);

    private JButton reset = new JButton(resetImage); //Reset button
    private JMenuItem infoPanel = new JMenuItem("<html><h1>How to Play Minesweeper</h1>\n" + //
                "    <ol>\n" + //
                "        <li><strong>Objective:</strong> The objective of Minesweeper is to clear a board containing hidden mines without detonating any of them.</li>\n" + //
                "        <li><strong>Game Board:</strong> The game board consists of a grid of squares. Some squares contain mines (bombs), and others are safe.</li>\n" + //
                "        <li><strong>Starting:</strong> Click on any square to start the game. This square and its neighboring squares will be revealed. If the square you click on is a mine, you lose.</li>\n" + //
                "        <li><strong>Numbers:</strong> If a square does not contain a mine, it will show a number indicating how many mines are adjacent to it (including diagonally adjacent squares).</li>\n" + //
                "        <li><strong>Using Numbers:</strong> Use the numbers revealed to deduce which squares are safe to click. For example, if a square shows '1', it means there is 1 mine in one of its adjacent squares.</li>\n" + //
                "        <li><strong>Flagging Mines:</strong> If you suspect a square contains a mine, you can right-click (or long-press on touch devices) to flag it. This helps you keep track of where the mines are.</li>\n" + //
                "        <li><strong>Winning:</strong> To win, you need to successfully uncover all the squares that do not contain mines. This requires careful deduction and a bit of luck.</li>\n" + //
                "        <li><strong>Strategy:</strong> Start with the squares that are least likely to contain mines based on adjacent numbers. Use logical deduction to progress through the board.</li>\n" + //
                "        <li><strong>Restart:</strong> If you hit a mine, the game ends. You can start a new game by clicking the restart button or choosing a new difficulty level.</li>\n" + //
                "        <li><strong>Difficulty Levels:</strong> Minesweeper typically offers different difficulty levels, changing the size of the grid and the number of mines, to provide varied challenges.</li>\n" + //
                "    </ol></html>");
    private JMenuItem easy = new JMenuItem("Easy"); //Easy button
    private JMenuItem medium = new JMenuItem("Medium"); //Medium button
    private JMenuItem hard = new JMenuItem("Hard"); //Hard button
    private JMenuItem white = new JMenuItem("White"); //White button
    private JMenuItem green = new JMenuItem("Green (default)"); //Green button
    private JMenuItem blue = new JMenuItem("Blue"); //Blue button
    private JMenuItem blueAndRed = new JMenuItem("Blue and Red"); //Blue and red button
    private JMenuItem purpleAndGreen = new JMenuItem("Purple and Green"); //Purple and green button
    private int elapsedTime = 0; //Stores how long the curren t game has been going
    private JLabel time = new JLabel("|  Time: " + elapsedTime + "s"); //Time label
    private boolean plantedMines = false; //Stores whether the mines hae been planted yet
 
    private Timer timer = new Timer(1000, new ActionListener() { //Timer which is used for timing the game.
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
        time.setText("|  Time: " + elapsedTime + "s"); //Sets the timer text
        JMenuBar menu = new JMenuBar(); //Defines the menu bar
        JMenu difficultyDropdown = new JMenu("Difficulty"); //Adds difficulty menu
        JLabel dropdownArrow1 = new JLabel(dropdownImage); //Adds dropdown arrow
        JMenu colours = new JMenu("|  Colours"); //Adds colour menu
        JLabel dropdownArrow2 = new JLabel(dropdownImage); //Adds dropdown arrow
        JLabel difficultyLabel = new JLabel("Difficulty: " + difficultyPicker()); //Sets the difficulty label
        flags = flagSize[difficulty - 1]; //Sets the flag count
        flagsLabel.setText("Flags left: " + flags); //Updated flag label
        JMenu infoMenu = new JMenu("|  How to play");
        updateGamesPlayed(false);

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
        menu.add(dropdownArrow1);
        menu.add(colours);
        menu.add(dropdownArrow2);
        menu.add(infoMenu);
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
        score.add(gameCounter);
        infoMenu.add(infoPanel);

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
        int grid[][] = gridPicker(); //Retrieves the approriate difficulty grid
        int[][] nearbyMines = new int[gridSize[2 * difficulty - 2]][gridSize[2 * difficulty - 1]];
        
        //Plants mines
        while (z < flagSize[difficulty - 1] && !plantedMines) { //Loops intil all mines are planted
            int randX = (int) Math.floor(Math.random() * gridSize[2 * difficulty - 1]); //Random x value withing the correct range based on the difficulty variable
            int randY = (int) Math.floor(Math.random() * gridSize[2 * difficulty - 2]); //Random y value withing the correct range based on the difficulty variable
            if (!(grid[randY][randX] == -1) //Ensures the random number is not already a bomb
                    && !(randX == xx - 1 && randY == yy - 1) && !(randX == xx && randY == yy - 1)
                    && !(randX == xx + 1 && randY == yy - 1)
                    && !(randX == xx - 1 && randY == yy) && !(randX == xx && randY == yy)
                    && !(randX == xx + 1 && randY == yy)
                    && !(randX == xx - 1 && randY == yy + 1) && !(randX == xx && randY == yy + 1)
                    && !(randX == xx + 1 && randY == yy + 1)) {
                grid[randY][randX] = -1; //Temporarily fills the grid with -1 where the mines are
                System.out.print(randX);
                System.out.print(randY + "b ");
                bombs[randY][randX] = true; //Updates the bombs array to reflect ehere the mines are placed
                z++; //Increases count if mine planted
            }
        }

        //Resets the nearbyMines array
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                nearbyMines[y][x] = 0; 
            }
        }
        
        //Diagnostic text
        System.out.println();
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }

        //Fills the nearbyMines array
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                if (x > 0) { //Makes sure the if statements don't check out of bounds to the left
                    if (grid[y][x - 1] == -1) {
                        nearbyMines[y][x]++;
                    }
                    if (y > 0) { //Makes sure the if statements don't check out of bounds upwards
                        if (grid[y - 1][x - 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                    if (y + 1 < gridSize[2 * difficulty - 2]) { //Makes sure the if statements don't check out of bounds downwards
                        if (grid[y + 1][x - 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                }
                if (x + 1 < gridSize[2 * difficulty - 1]) { //Makes sure the if statements don't check out of bounds to the right
                    if (grid[y][x + 1] == -1) {
                        nearbyMines[y][x]++;
                    }
                    if (y > 0) { //Makes sure the if statements don't check out of bounds upwards
                        if (grid[y - 1][x + 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                    if (y + 1 < gridSize[2 * difficulty - 2]) { //Makes sure the if statements don't check out of bounds downwards
                        if (grid[y + 1][x + 1] == -1) {
                            nearbyMines[y][x]++;
                        }
                    }
                }
                if (y > 0) { //Makes sure the if statements don't check out of bounds upwards
                    if (grid[y - 1][x] == -1) {
                        nearbyMines[y][x]++;
                    }
                }
                if (y + 1 < gridSize[2 * difficulty - 2]) { //Makes sure the if statements don't check out of bounds downwards
                    if (grid[y + 1][x] == -1) {
                        nearbyMines[y][x]++;
                    }
                }
                if (grid[y][x] == -1) {
                    nearbyMines[y][x]++;
                }

            }
        }
        grid = nearbyMines; //Copies the contents of the temporary nearbyMines array to the grid array

        //Prints out the grid array for diagnostic purposes
        System.out.println();
        for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
            for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }

        //Copies the temporary grid array into the appropriate difficulty array based on the difficulty variable
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
        guiMethod(); //Resets the screen
        plantedMines = true;
    }

    /*
     * This method is used whenever you change the difficulty, press the reset button and when you win/lose the game.
     * This method resets all relevant arrays oto their default states to allow for the game to be played again.
     */

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

    /*
     * This method is called every time the player clicks and also under certain conditions inside the method itself.
     * In this method it first gets the grid for the current difficulty and then checks whether the inputed value is safe (0) and
     * then it loops through the 3x3 around the player click, for each square in the 3x3 it checks whever it is out of bounds and 
     * if its is not out of bounds it chechs whether that square is alrady not visible and not flagged if so it sets the square to visible and
     * reruns the method again.
     * If the original inputted value is not a 0 it checks if the square is not yet visible and not flagged and then makes it visible and 
     * runs the method again.
     * What this ends up doing is recursively expanding safe squares stopping when it reaches an unknown square.
     */

    public void domainExpansion(int y, int x) {
        int grid[][] = gridPicker(); //Grabs the appropriate difficulty grid

        if (grid[y][x] == 0) { //Runs when the clicked square is a 0 (completely safe)
            for (int xx = x - 1; xx <= x + 1; xx++) { //Loops through the surrounding 3x3 in columns
                for (int yy = y - 1; yy <= y + 1; yy++) { //Loops through the surrounding 3x3 in rows
                    if (xx >= 0 && yy >= 0 && xx + 1 <= gridSize[2 * difficulty - 1] //Checks whether current square in 3x3 is within game bounds to ensure game doesn't break
                            && yy + 1 <= gridSize[2 * difficulty - 2]) {
                        if (!visible[yy][xx] && !flagged[yy][xx]) { //Checks whether the current square in the 3x3 is already visible and/or flagged
                            visible[yy][xx] = true; //Makes the covered square uncovered
                            domainExpansion(yy, xx); //Reruns the method to expand recursively
                        }
                    }
                }
            }
        }
        if (grid[y][x] > 0) { //Runs when the clicked square is not completely safe (has surrounding mines)
            //Reveals only the current square (not a 3x3)
            if (!visible[y][x] && !flagged[y][x]) { 
                visible[y][x] = true;
                domainExpansion(y, x);
            }
        }
    }

    /*
     * This method is called on every left mouse click and nowhere else.
     * This method is used to determine whether the game has been either won or lost.
     * It does this by checking whether the clicked square is both visible and a bomb.
     * If it is, it then loops through the bombs array and making all of the bombs visible
     * by checking whether there is a bomb there or not.
     * After this it stops the timer and gameRunning variable and starts a new timer which allows
     * for the bombs to all be revealed before the gameOver method is called.
     * To check whether the game has been won or not it loops throught the visible array and 
     * adds to a count for every visible square.
     * Then with this count, it is checked whether the count is equal to the number of total squares on the board
     * minus the flags.
     * It then does similar actions a losing to end the game.
     */

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
            gameOver(1);

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
        updateGamesPlayed(true);
    }

    public void updateGamesPlayed(boolean played){
        try{
            Scanner fileScanner = new Scanner(gamesPlayed);
            while (fileScanner.hasNextInt()){
                games = fileScanner.nextInt();
            }
            if (played){games++;};
            FileWriter fileWriter = new FileWriter(gamesPlayed);
            fileWriter.write(String.valueOf(games));
            fileWriter.flush();
            fileWriter.close();
            fileScanner.close();
            System.out.println("Total games played: " + (games));
        }catch(IOException e){
            System.out.println(e);
        }
        gameCounter.setText("|  Games played (all time): " + games);;
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