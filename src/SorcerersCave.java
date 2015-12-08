/**
 * SorcerersCave.java
 * 
 * CMSC 335
 * Project 4
 * 
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */



import javax.swing.*;


/**
 * Tests the Game class by providing it with a <code>File</code> object or
 * prompting for a new session.
 * 
 * @author      Alan Johnson
 * @version     1.2
 */
public class SorcerersCave {
    
    
    
    /**
     * @param args      Command-line arguments (not used)
     */
    public static void main (String[] args) {
        
        //                       ----  Setup  ----
        
        System.out.println("Alan Johnson, CMSC 335, Project 4\n");
        
        Game game = startOptions();
        
    }
    
    
    
    /**
     * Displays to the user the option to either load an existing file or to
     * start a new game.  Until either option is fully chosen (successfully
     * choosing an input file, simply clicking "New File", or closing the
     * Load/New dialog), the options are shown again and again.
     * 
     * @return     <code>Game</code> object representing input data chosen by user
     */
    private static Game startOptions() {
        
        JFileChooser fc = new JFileChooser(".");
        fc.setFileHidingEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        boolean optionChosen = false;
        
        Object[] options = {"Load File", "New File"};
        
        do {
        int n = JOptionPane.showOptionDialog(null, "Would you like to load"
                + "\n or start a new file?", null, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        switch (n) {
            
            case 0:  //  User chose to load a file
                int retVal = fc.showOpenDialog(null);
                
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    return new Game(fc.getSelectedFile());
                }  // if a file was chosen
                
                break;
                
            case 1:  //  User chose to start a new file
                String name = JOptionPane.showInputDialog("Please input game name:", "New Game");
                return new Game(name);
                
            case -1:  //  User cancelled:
                System.exit(0);
                        
        }  //  switch
        
        } while (!optionChosen);
     
        return null;
        
    }  // end startOptions() method
    
    
}  //  end class SorcerersCave
