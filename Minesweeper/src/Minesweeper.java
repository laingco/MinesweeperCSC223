/**
 * Version - 1.1
 * This is a simple minesweeper game made in java.
 *
 * Author - Cooper Laing
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 
 class gui implements MouseListener{
     private int difficulty = 2;
     private int gridSize[] = {8, 10, 14, 18, 20, 24};
     private int flagSize[] = {10, 40, 99};
     private int easyGrid[][] = new int[gridSize[0]][gridSize[1]];
     private int mediumGrid[][] = new int[gridSize[2]][gridSize[3]];
     private int hardGrid[][] = new int[gridSize[4]][gridSize[5]];
     private JFrame jframe = new JFrame("Minesweeper test");   //create JFrame object
     private JButton tiles[][] = new JButton[20][24]; // -2 = uncovered -1 = bomb 0 = safe to click 1-7 = no. of mines nearby
     private boolean flagged[][] = new boolean[20][24];
     private int flags = 0;
     private JLabel flagsLabel = new JLabel("Flags left: " + flags);
 
     ImageIcon temp1 = new ImageIcon("src/783503.png");
     Image image = temp1.getImage();
     Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
     ImageIcon resetImage = new ImageIcon(newimg); 
 
     ImageIcon temp2 = new ImageIcon("src/7628490.png");
     Image image2 = temp2.getImage();
     Image newimg2 = image2.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
     ImageIcon flagImage = new ImageIcon(newimg2); 
 
     ImageIcon temp3 = new ImageIcon("src/Eo_circle_green_checkmark.svg.png");
     Image image3 = temp3.getImage();
     Image newimg3 = image3.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
     ImageIcon uncoveredButton = new ImageIcon(newimg3); 
 
     JButton reset = new JButton("Reset", resetImage);
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
 
     public String difficultyPicker(int choice){
         if (choice == 0){
             switch (difficulty) {
                 case 1:
                     return("Easy");
                 case 2:
                     return("Medium");
                 case 3:
                     return("Hard");
                 default:
                     return("N/A");
             }
         } else{
             return(null);
         }
     }
 
     public int[][] gridPicker(){
         switch (difficulty) {
             case 1:
                 return(easyGrid);
             case 2:
                 return(mediumGrid);
             case 3:
                 return(hardGrid);
             default:
                 return(null);
         }
     }
     
     public void guiMethod(){
         jframe.getContentPane().removeAll();
 
         JPanel score = new JPanel();
         JPanel playArea = new JPanel();
         playArea.setLayout(new GridLayout(gridSize[2*difficulty-2], gridSize[2*difficulty-1]));
         jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         jframe.setSize(1050,900);         //set size of GUI screen
         jframe.setVisible(true);
 
         elapsedTime = 0;
         time.setText("Time: " + elapsedTime + "s");
         JMenuBar menu = new JMenuBar();
         JMenu difficultyDropdown = new JMenu("Difficulty");
         JMenu colours = new JMenu("Colours");
         JLabel difficultyLabel = new JLabel("Difficulty: " + difficultyPicker(0));
 
         flags = flagSize[difficulty-1];
         flagsLabel.setText("Flags left: " + flags);
 
         //JButton tiles[][] = new JButton[gridSize[2*difficulty-2]][gridSize[2*difficulty-1]];
         for (int y = 0; y < gridSize[2*difficulty-2]; y++){
             for (int x = 0; x < gridSize[2*difficulty-1]; x++){
                 int currentGrid[][] = new int[gridSize[2*difficulty-2]][gridSize[2*difficulty-1]];
                 currentGrid = gridPicker();
                 if(currentGrid[y][x] == 0){
                     //tiles[y][x] = new JButton(uncoveredButton);
                     tiles[y][x] = new JButton(Integer.toString(currentGrid[y][x]));
                 } else if(currentGrid[y][x] == -2){
                     tiles[y][x] = new JButton();
                 } else if (flagged[y][x]){
                     tiles[y][x] = new JButton(flagImage);
                 } else{
                     tiles[y][x] = new JButton(Integer.toString(currentGrid[y][x]));
                 }
                 tiles[y][x].addMouseListener(this);
                 tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 8));
                 tiles[y][x].setOpaque(true);
                 tiles[y][x].setBorderPainted(false);
                 if(y%2==0){
                     if(x%2==0){
                         tiles[y][x].setBackground(Color.WHITE);
                     } else{
                         tiles[y][x].setBackground(Color.LIGHT_GRAY);
                     }
                 } else if (!(x%2==0)){
                     tiles[y][x].setBackground(Color.WHITE);
                 } else{
                     tiles[y][x].setBackground(Color.LIGHT_GRAY);
                 }
                 playArea.add(tiles[y][x]);
             }
         }
 
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

     public void updateScreen(){
        for (int y = 0; y < gridSize[2*difficulty-2]; y++){
            for (int x = 0; x < gridSize[2*difficulty-1]; x++){
                int currentGrid[][] = new int[gridSize[2*difficulty-2]][gridSize[2*difficulty-1]];
                currentGrid = gridPicker();
                if(currentGrid[y][x] == 0){
                    tiles[y][x].setText("y");
                } else if(currentGrid[y][x] == -2){
                    tiles[y][x].setIcon(uncoveredButton);
                } else if (flagged[y][x]){
                    tiles[y][x].setIcon(flagImage);
                } else if (!flagged[y][x]){
                    tiles[y][x].setIcon(null);
                } else if (currentGrid[y][x] > 0){
                    tiles[y][x].setText(Integer.toString(currentGrid[y][x]));
                } else if (currentGrid[y][x] == -1){

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
         while (z < flagSize[difficulty - 1] && !plantedMines) {
             int randX = (int) Math.floor(Math.random() * gridSize[2 * difficulty - 1]);
             int randY = (int) Math.floor(Math.random() * gridSize[2 * difficulty - 2]);
             if (!(grid[randY][randX] == -1) && !(randX == xx && randY == yy)) {
                 grid[randY][randX] = -1;
                 System.out.print(randX + randY + "b ");
                 z++;
             }
         }
 
         for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
             for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                 int nearbyMines = 0;
                 if (!(grid[y][x] == -1)) {
                     if (x > 0) {
                         if (grid[y][x - 1] == -1) {
                             nearbyMines++;
                         }
                         if (y > 0) {
                             if (grid[y - 1][x - 1] == -1) {
                                 nearbyMines++;
                             }
                         }
                         if (y + 1 < gridSize[2 * difficulty - 2]) {
                             if (grid[y + 1][x - 1] == -1) {
                                 nearbyMines++;
                             }
                         }
                     }
                     if (x + 1 < gridSize[2 * difficulty - 1]) {
                         if (grid[y][x + 1] == -1) {
                             nearbyMines++;
                         }
                         if (y > 0) {
                             if (grid[y - 1][x + 1] == -1) {
                                 nearbyMines++;
                             }
                         }
                         if (y + 1 < gridSize[2 * difficulty - 2]) {
                             if (grid[y + 1][x + 1] == -1) {
                                 nearbyMines++;
                             }
                         }
                     }
                     if (y > 0) {
                         if (grid[y - 1][x] == -1) {
                             nearbyMines++;
                         }
                     }
                     if (y + 1 < gridSize[2 * difficulty - 2]) {
                         if (grid[y + 1][x] == -1) {
                             nearbyMines++;
                         }
                     }
                 }
                 grid[y][x] = nearbyMines;
             }
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
         System.out.println();
         for (int y = 0; y < gridSize[2 * difficulty - 2]; y++) {
             for (int x = 0; x < gridSize[2 * difficulty - 1]; x++) {
                 System.out.print(grid[y][x]);
             }
             System.out.println();
         }
     }
 
     public void resetGrids(){
         for (int y = 0; y < gridSize[0]; y++){
             for (int x = 0; x < gridSize[1]; x++){
                 easyGrid[y][x] = -2;
             }
         }
 
         for (int y = 0; y < gridSize[2]; y++){
             for (int x = 0; x < gridSize[3]; x++){
                 mediumGrid[y][x] = -2;
             }
         }
 
         for (int y = 0; y < gridSize[4]; y++){
             for (int x = 0; x < gridSize[5]; x++){
                 hardGrid[y][x] = -2;
                 flagged[y][x] = false;
             }
         }
         flags = flagSize[difficulty-1];
         plantedMines = false;
         timer.restart();
         timer.stop();
     }
 
     public void domainExpansion(int y, int x){
         int grid[][] = gridPicker();
         if (!(grid[y][x] == -1)) {
             if (x > 0) {
                 if (grid[y][x - 1] == -1) {
                     
                 }
                 if (y > 0) {
                     if (grid[y - 1][x - 1] == -1) {
                         
                     }
                 }
                 if (y + 1 < gridSize[2 * difficulty - 2]) {
                     if (grid[y + 1][x - 1] == -1) {
                         
                     }
                 }
             }
             if (x + 1 < gridSize[2 * difficulty - 1]) {
                 if (grid[y][x + 1] == -1) {
                     
                 }
                 if (y > 0) {
                     if (grid[y - 1][x + 1] == -1) {
                         
                     }
                 }
                 if (y + 1 < gridSize[2 * difficulty - 2]) {
                     if (grid[y + 1][x + 1] == -1) {
                         
                     }
                 }
             }
             if (y > 0) {
                 if (grid[y - 1][x] == -1) {
                     
                 }
             }
             if (y + 1 < gridSize[2 * difficulty - 2]) {
                 if (grid[y + 1][x] == -1) {
                     
                 }
             }
         }
     }
 
     public void mousePressed(MouseEvent e) {
         int currentGrid[][] = gridPicker();
         for (int y = 0; y < gridSize[2*difficulty-2]; y++) {
             for (int x = 0; x < gridSize[2*difficulty-1]; x++) {
                 if (tiles[y][x] == e.getSource() && e.getButton() == MouseEvent.BUTTON1) {
                     System.out.println("Clicked button at (x, y): (" + (x + 1) + ", " + (y + 1) + ")");
                     if (!(tiles[y][x].getIcon() == flagImage)){
                         tiles[y][x].setText("O");
                         guiMethod();
                     } else if (currentGrid[y][x] == 0){
                         domainExpansion(y, x);
                         guiMethod();
                     }
                     if (!plantedMines){
                         plantMines(x, y);
                         //System.out.println("past planted mines");
                     }
                     if (!timer.isRunning()) {
                         timer.start();
                         //System.out.println("past timer start");
                     }
                     updateScreen();
                 } else if (tiles[y][x] == e.getSource() && e.getButton() == MouseEvent.BUTTON3) {
                     System.out.println("Flagged button at (x, y): (" + (x + 1) + ", " + (y + 1) + ")");
                     if (tiles[y][x].getIcon() == flagImage){
                         tiles[y][x].setIcon(null);
                         flags++;
                         flagsLabel.setText("Flags left: " + flags);
                         tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 8));
                         flagged[y][x] = false;
                     } else /*if(currentGrid[y][x] < 0)*/{   
                         //tiles[y][x].setText("");
                         tiles[y][x].setFont(new Font("Serif", Font.PLAIN, 0));
                         tiles[y][x].setIcon(flagImage);
                         flags--;
                         flagsLabel.setText("Flags left: " + flags);
                         flagged[y][x] = true;
                     }
                     updateScreen();
                 } else if (e.getSource() == whiteAndGrey){
                     if(y%2==0){
                         if(x%2==0){
                             tiles[y][x].setBackground(Color.WHITE);
                             tiles[y][x].setOpaque(true);
                             tiles[y][x].setBorderPainted(false);
                         } else{
                             tiles[y][x].setBackground(Color.LIGHT_GRAY);
                             tiles[y][x].setOpaque(true);
                             tiles[y][x].setBorderPainted(false);
                         }
                     } else if (!(x%2==0)){
                         tiles[y][x].setBackground(Color.WHITE);
                         tiles[y][x].setOpaque(true);
                         tiles[y][x].setBorderPainted(false);
                     } else{
                         tiles[y][x].setBackground(Color.LIGHT_GRAY);
                         tiles[y][x].setOpaque(true);
                         tiles[y][x].setBorderPainted(false);
                     }
                 }
                 
             }
         }
 
         if (e.getSource() == easy){
             difficulty = 1;
             resetGrids();
             guiMethod();
         } else if (e.getSource() == medium){
             difficulty = 2;
             resetGrids();
             guiMethod();
         } else if (e.getSource() == hard){
             difficulty = 3;
             resetGrids();
             guiMethod();
         } else if (e.getSource() == reset){
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
 
     public static void main(String args[]){
         gui sm=new gui();
         sm.resetGrids();
         sm.guiMethod();
     }
 }