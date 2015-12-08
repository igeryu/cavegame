/**
 * Game.java
 *
 * CMSC 335
 * Project 4
 *
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;



/**
 * A <code>Game</code> object owns a <code>Cave</code>, an extension of
 * <code>JFrame</code>, and two <code>HashMap</code>s, one of unowned
 * <code>GameElement</code> objects and of of all <code>GameElement</code>
 * objects associated with the <code>Game</code> object.
 *
 * The <code>Game</code> is responsible for opening the <code>File</code> given
 * to it in its constructor and parsing <code>File</code>'s data into
 * <code>Party</code>, <code>Creature</code>, <code>Artifact</code>, and
 * <code>Treasure</code> objects.
 *
 * The <code>Game</code> also allows the user to search for an instance(s) of the above
 * object types, by name/type/index.
 *
 * @author     Alan Johnson
 * @version    1.3
 */
public class Game {

    //                       ----  Instance Variables  ----

    private Cave cave;
    private final String name;

    private final HashMap<Integer, GameElement> unownedElements, allElements;
    private final ArrayList<Job> jobList;
    private final ArrayList<Party> partyList;

    public enum CreatureSortBy { age, height, name, weight }

    //                      ----  GUI Components ----

    private final GameFrame gameFrame;
    
    //                       ----  Constructors  ----

    /**
     * @param file      <code>File</code> object that contains data to be parsed
     */
    public Game(File file) {
        jobList = new ArrayList<>();
        partyList = new ArrayList<>();
        unownedElements = new HashMap<>();
        allElements = new HashMap<>();
        
        name = "Alan Johnson - Project 4: " + file.getName();
        System.out.printf("Game File: \"%s\"\n\n", file.getName());

        readFile(file);

        cave.setOwner(this);
        gameFrame = new GameFrame();
        
        System.out.printf("\nGame File: \"%s\"\n", file.getName());
        
    }  //  end Game(File) constructor



    /**
     * @param _name      Name of new <code>Game</code> object
     */
    public Game(String _name) {
        jobList = new ArrayList<>();
        partyList = new ArrayList<>();
        unownedElements = new HashMap<>();
        allElements = new HashMap<>();

        if (_name != null)
            name = _name;
        else
            name = "No_Name";

        cave = new Cave(this);
        gameFrame = new GameFrame();

    }



    /**
     * Adds the given <code>GameElement</code> to <code>unownedItems</code>.
     *
     * @param element      <code>GameElement</code> object to be added to
     *                     <code>unownedElements HashMap</code>
     */
    private void addUnownedElement(GameElement element) {
        unownedElements.put(element.getIndex(), element);
    }  //  end method addUnownedElement()
    


    /**
     * Is called by <code>readFile()</code>  This method creates a
     * <code>Scanner</code> object from the <code>File</code> parameter and
     * returns that <code>Scanner</code> object.
     *
     * @param file      The <code>File</code> to be opened
     * @return          The <code>Scanner</code> object containing the file's data.
     */
    private Scanner openFile(File file) {
        Scanner scanner;

        try { scanner = new Scanner(file); }
        
        catch (FileNotFoundException e) {
            System.out.println("File input fail!");
            return null; }  //  catch

        return scanner;
    }  //  end method openFile()



    /**
     * Is called by <code>parseInput()</code>.  Takes a single <code>String</code>
     * (one line from the input file) and parses it into an <code>ArrayList</code>
     * of <code>String</code> objects.
     *
     * @param nextLine      The <code>String</code> to be parsed
     * @return              <code>ArrayList</code> of <code>String</code> objects,
     *                      representing tokens from the <code>nextLine</code> parameter.
     */
    private ArrayList<String> parseLine (String nextLine) {
        String delim = ":";
        String[] tokens = nextLine.split(delim);

        //  Try again with spaces for delim if previous line does not work:
        if (tokens.length <= 1) { tokens = nextLine.split("\\s+"); }

        //  Trim all the tokens:
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        //  Convert tokens Array into currentTokens ArrayList
        ArrayList<String> currentTokens = new ArrayList<>(Arrays.asList(tokens));
        return currentTokens;
    }  //  end parseLine() method



    /**
     * Is called by <code>readFile()</code>.  Calls <code>parseLine()</code>.  
     * Takes the <code>Scanner</code> parameter and uses the <code>parseLine()</code>
     * method (which returns an <code>ArrayList&lt;String&gt;</code>) to create an
     * <code>ArrayList&lt;ArrayList&lt;String&gt;&gt;</code>.
     *
     * @param scanner      Contains the data from the <code>File</code> object
     *                     supplied to <code>Game</code> constructor.
     * @return             An <code>ArrayList&lt;ArrayList&lt;String&gt;&gt;</code>,
     *                     the inner <code>ArrayList</code> each  representing
     *                     all tokens from a single line of the file input.
     */
    private ArrayList<ArrayList<String>> parseInput (Scanner scanner) {

        ArrayList<ArrayList<String>> tokens = new ArrayList<>();

        while (scanner.hasNextLine()) {      //  Grab tokens from the input

            String nextLine = scanner.nextLine();

            if (!(nextLine.startsWith("/") || nextLine.isEmpty())) {

                ArrayList<String> currentTokens = parseLine(nextLine);
                tokens.add(currentTokens);

            }  //  if the token is not a comment

        }  //  Stepping through the input file

        return tokens;

    }  //  end parseInput() method



    /**
     * <p>Called by <code>readFile()</code>.  Steps through each line in the
     * <code>ArrayList&lt;ArrayLists&lt;String&gt;&gt;</code> objects (the input
     * parameter) and creates the appropriate <code>GameElements</code>.  Those
     * elements are then added to their appropriate owners, if possible.
     * Otherwise, they are added to <code>unownedElements</code>.</p>
     *
     * <p>All <code>GameElement</code> objects are added to the
     * <code>allElements HashMap</code>, whether their owner assignment is successful or not.</p>
     *
     * @param tokens      List of tokens, grouped by line
     */
    private void parseTokens(ArrayList<ArrayList<String>> tokens) {

        for (ArrayList<String> line : tokens) {

            String process = line.get(0);

            switch (process) {  //  select the appropriate object to instantiate

                case "p":
                    if (line.size() >= 3) {

                        //  Party's own index
                        Integer index = Integer.parseInt(line.get(1));
                        Party party = new Party(line);
                        cave.addParty(party);
                        partyList.add(party);
                        allElements.put(party.getIndex(), party);
                    }
                    break;

                case "c":
                    if (line.size() >= 8) {

                        //  Index of owning Party:
                        Integer index = Integer.parseInt(line.get(4));
                        Creature creature = Creature.newCreatureFromTokens(line);

                        // if creature can't be added to any Party objects, add
                        // it to unownedElements:
                        if(!cave.addCreature(creature))
                            unownedElements.put(creature.getIndex(), creature);
                        
                        allElements.put(creature.getIndex(), creature);
                    }
                    break;

                case "t":
                    if (line.size() >= 6) {

                        //  Index of owning Creature:
                        Integer index = Integer.parseInt(line.get(3));
                        Treasure treasure = new Treasure(line);

                        // if treasure can't be added to any Creature objects,
                        // add it to unownedElements:
                        if(!cave.addTreasure(treasure))
                            unownedElements.put(treasure.getIndex(), treasure);

                        allElements.put(treasure.getIndex(), treasure);

                    } else System.out.println("Error adding Treasure");
                    break;

                case "a":
                    if (line.size() >= 4) {

                        //  Index of owning Creature:
                        Integer index = Integer.parseInt(line.get(3));
                        Artifact artifact = new Artifact(line);

                        // if artifact can't be added to any Creature objects,
                        // add it to unownedElements:
                        if(!cave.addArtifact(artifact))
                            unownedElements.put(artifact.getIndex(), artifact);

                        allElements.put(artifact.getIndex(), artifact);

                    }  else System.out.println("Error adding Artifact");
                    break;



                    case "j":
                    if (line.size() >= 5) {

                        //  Index of owning Creature:
                        Job job = Job.newJob(line, allElements);

                        // if job can't be added to any Creature objects,
                        // add it to unownedElements:
                        if (job.getOwner() != null) {
                            cave.addJob(job);
                            jobList.add(job);
                        } else {
                            unownedElements.put(job.getIndex(), job);
                            System.out.printf("Job[%d] (%s) has no Creature.\n", job.getIndex(), job);
                        }
                    
                    }  else System.out.println("Error adding Artifact");
                    break;

                default:
                    System.out.println("Error at line " + line);

            }  // switch

        }  //  for each String in tokens

    }  //  end method parseTokens()


    /**
     * <p>Is called by the <code>Game</code> constructor.  Calls the <code>openFile()</code>,
     * <code>parseInput()</code>, and <code>parseTokens()</code> methods.</p>
     *
     * <p>Passes the <code>File</code> parameter to the <code>openFile()</code>
     * method.  The returned <code>Scanner</code> object is then passed to the
     * <code>parseInput()</code> method.  The <code>ArrayList&lt;ArrayList&lt;String&gt;&gt;</code>
     * returned by <code>parseInput()</code> is then passed to the <code>parseTokens()</code>
     * method, which creates and adds all the objects to their appropriate owners.</p>
     *
     * @param file      The <code>File</code> to be opened
     *
     */
    private void readFile(File file) {

        Scanner scanner = openFile(file);
        cave = new Cave(this);
        
        if (scanner != null) {  // Use input file to setup game

            ArrayList<ArrayList<String>> tokens = parseInput(scanner);
            parseTokens(tokens);
            
            cave.createSemaphores();

        }  //  if scanner isnt null

    }  // method parseInput()



    /**
     * <p>Is called when <code>gameFrame<code> has received a search <code>String</code>
     * from the user.</p>
     *
     * <p>Searches all <code>GameElement</code> objects (<code>Party</code>,
     * <code>Creature</code>, <code>Artifact</code>, and <code>Treasure</code>)
     * for the search <code>String</code> within their names, indexes, and types.</p>
     *
     * <p>Displays a modal <code>JOptionPane</code> to indicate whether the search found
     * anything (and displays the findings).</p>
     *
     * @param input      Search <code>String</code> to find within <code>GameElement</code> objects
     */
    private void search(String input) {

        if (input != null) {

            ArrayList<GameElement> results = new ArrayList<>();
            input = input.toLowerCase();

            for (Map.Entry<Integer, GameElement> e : allElements.entrySet()) {

                String elemName = e.getValue().getName().toLowerCase();
                String elemIndex = String.valueOf(e.getValue().getIndex());
                String elemType = e.getValue().getType().toLowerCase();

                if (elemName.contains(input) || elemIndex.contains(input)
                        || elemType.contains(input)) {

                        results.add(e.getValue());

                }  //  if element name matches input string

            }  //  for each GameElement in allElements

            if (!results.isEmpty()) {

                String plural = "";

                if (results.size() > 1)
                    plural = "s";

                String message = String.format("Search string \"%s\" found %d result%s:",
                    input, results.size(), plural);

                for (GameElement e : results) {

                    message += String.format("\n%s (%d):  %s", e.getType(), e.getIndex(), e.getName());

                }  //  for each GameElement in results

                JOptionPane.showMessageDialog(gameFrame, message);

            } else JOptionPane.showMessageDialog(gameFrame, String.format("Search string \"%s\" found no results.", input));

        }  //  if input isn't null

    }  //  end search() method



    /**
     *
     * @return     <code>String</code> representation
     */
    @Override public String toString() { return "Game"; }  //  end toString() method



    /**
     * <p>Visually represents the owning Game object's data in a tree format.</p>
     * <p>Contains a "Search" button to allow the user to search all of the
     * <code>Game</code> object's <code>GameElement</code> objects in order to
     * find any that contain the search <code>String</code> given by the user.</p>
     *
     * @author      Alan Johnson
     * @version     1.2
     */
    private class GameFrame extends JFrame {
        private static final long serialVersionUID = 1L;

        private final JTree tree;
        private final JScrollPane artifactDetailScrollPane, jobProgressScrollPane, treeScrollPane;
        private ButtonGroup creatureSortGroup, itemSortGroup;
        private ArrayList<JPanel> artifactPanels;



        /**
         * 
         */
        public GameFrame() {

            this.setTitle(name);

            //             -----  Header -----

            add(new JLabel("Game"));

            //             -----  Data -----
            //             -----  Data : Tree -----
            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new GridLayout(1, 2));
            add(dataPanel, BorderLayout.CENTER);
            
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new GridLayout(2, 1));
            tree = buildTree();
            treeScrollPane = new JScrollPane(tree);
            treeScrollPane.setBorder(BorderFactory.createTitledBorder("Data Tree"));
            leftPanel.add(treeScrollPane);
            
            //dataPanel.add(treeScrollPane);
            artifactDetailScrollPane = buildArtifactDetailList();
            leftPanel.add(artifactDetailScrollPane);
            
            
            dataPanel.add(leftPanel);
            
            //             -----  Data : Set up jobProgressScrollPane -----
            jobProgressScrollPane = buildJobProgressList();
            jobProgressScrollPane.setBorder(BorderFactory.createTitledBorder("Jobs"));
            
            dataPanel.add(jobProgressScrollPane);
            
            //             -----  Footer -----

            JPanel buttonPanel = new JPanel();
            add(buttonPanel, BorderLayout.PAGE_END);
            buttonPanel.setLayout(new GridLayout(3, 1));

            //             -----  Footer : Search Button -----

            JButton searchButton = new JButton("Search");

            searchButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog("Input search string: ");
                    Game.this.search(input);
                }
            });

            JPanel panel = new JPanel();
            panel.add(searchButton, BorderLayout.CENTER);
            buttonPanel.add(panel);

            //             -----  Footer : Sort Options -----

            JPanel sortOptionsPanel = new JPanel();
            sortOptionsPanel.setLayout(new GridLayout(1, 2));
            buttonPanel.add(sortOptionsPanel);

            //             -----  Sort Options : Creature -----

            JPanel creatureSortPanel = new JPanel();
            sortOptionsPanel.add(creatureSortPanel);

            creatureSortGroup = new ButtonGroup();
            creatureSortPanel.setBorder(BorderFactory.createTitledBorder("Sort Creatures By..."));

            String actionString = "Age";
            JRadioButton radioButton = new JRadioButton(actionString);
            radioButton.setActionCommand(actionString);
            creatureSortGroup.add(radioButton);
            creatureSortPanel.add(radioButton);

            actionString = "Height";
            radioButton = new JRadioButton(actionString);
            radioButton.setActionCommand(actionString);
            creatureSortGroup.add(radioButton);
            creatureSortPanel.add(radioButton);

            actionString = "Name";
            radioButton = new JRadioButton(actionString);
            radioButton.setActionCommand(actionString);
            creatureSortGroup.add(radioButton);
            creatureSortPanel.add(radioButton);
            radioButton.setSelected(true);

            actionString = "Weight";
            radioButton = new JRadioButton(actionString);
            radioButton.setActionCommand(actionString);
            creatureSortGroup.add(radioButton);
            creatureSortPanel.add(radioButton);



            //             -----  Sort Options : Items -----

            JPanel itemSortPanel = new JPanel();
            sortOptionsPanel.add(itemSortPanel);

            itemSortGroup = new ButtonGroup();
            itemSortPanel.setBorder(BorderFactory.createTitledBorder("Sort Items By..."));

            actionString = "Weight";
            radioButton = new JRadioButton(actionString);
            radioButton.setActionCommand(actionString);
            itemSortGroup.add(radioButton);
            itemSortPanel.add(radioButton);
            radioButton.setSelected(true);

            actionString = "Value";
            radioButton = new JRadioButton(actionString);
            radioButton.setActionCommand(actionString);
            itemSortGroup.add(radioButton);
            itemSortPanel.add(radioButton);


            //             -----  Footer : Sort -----

            JButton sortButton = new JButton("Sort");

            sortButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {

                    if (creatureSortGroup.getSelection() != null &&
                            itemSortGroup.getSelection() != null) {

                        String creatureSortString = creatureSortGroup.getSelection().getActionCommand();
                        String itemSortString = itemSortGroup.getSelection().getActionCommand();

                        if (creatureSortString != null && itemSortString != null) {
                            cave.setCreatureSortMode(creatureSortString);
                            cave.setItemSortMode(itemSortString);
                        }  //  if both sort strings aren't null

                        gameFrame.refreshTree();
                    }  //  if both sort selections aren't null
                }  //  end method actionPerformed()
            });

            panel = new JPanel();
            panel.add(sortButton, BorderLayout.CENTER);
            buttonPanel.add(panel);
            

            //                 ----  Finalize Window  ----
            int width = 800; int height = 800;
            setMinimumSize(new Dimension(width, height));
            setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);

        }  //  end GameFrame constructor
        
        
        
        private JScrollPane buildArtifactDetailList() {
            
            gatherPanels();
            
            JList<JPanel> list = new JList<JPanel>() {
                private static final long serialVersionUID = 1L;
                @Override public Dimension getPreferredSize() {
                    int width = 350;
                    int rows = artifactPanels.size();
                    int height = 37 * rows;
                    return new Dimension(width, height);
                }
            };
            
            list.setLayout(new FlowLayout());
            list.setLayoutOrientation(JList.VERTICAL);
            
            JScrollPane pane = new JScrollPane(list);
            pane.getVerticalScrollBar().setUnitIncrement(100);
            pane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
            
            for (JPanel p : artifactPanels) {
                list.add(p);
            }
            
            
            
            list.validate();
            pane.validate();
            
            return pane;
            
        }
        
        
        
        /**
         * Creates a <code>JScrollPane</code> that contains a <code>JList</code>
         * with each <code>Job</code> object's <code>JPanel</code>.  The <code>JList</code>
         * overrides <code>getPreferredSize()</code> so that the returned size is
         * based on the number of <code>Job</code> panels to be displayed.
         * 
         * @return      A <code>JScrollPane</code> containing a <code>JList</code>
         *              with every <code>Job</code> panel
         */
        private JScrollPane buildJobProgressList() {
            
            JList<JPanel> list = new JList<JPanel>() {
                private static final long serialVersionUID = 1L;
                @Override public Dimension getPreferredSize() {
                    int width = 350;
                    int rows = jobList.size();
                    int height = 68 * rows;
                    return new Dimension(width, height);
                }
            };
            
            list.setLayout(new FlowLayout());
            list.setLayoutOrientation(JList.VERTICAL);
            
            JScrollPane pane = new JScrollPane(list);
            pane.getVerticalScrollBar().setUnitIncrement(100);
            pane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
            
            //  iterate through all GameElements, find Job objects and add their panes:
            
            for (Party p : partyList) {
                for (Job j : p.jobList) {
                    list.add(j.getPanel());
                }
            }  //  for each Party in partyList
            
            list.validate();
            pane.validate();
            
            return pane;
            
        }  // end method buildJobProgressList()



        /**
         * Creates a <code>JTree</code> that contains a hierarchal view of all
         * <code>GameElement</code> objects.
         * 
         * @return      A <code>JTree</code> that represents all <code>GameElement</code>
         *              objects and their hierarchal structure
         */
        private JTree buildTree() {

            DefaultMutableTreeNode root;
            DefaultMutableTreeNode node = root = new DefaultMutableTreeNode(Game.this);

            JTree newTree = new JTree(root);

            newTree.setCellRenderer(new DefaultTreeCellRenderer() {
                private static final long serialVersionUID = 1L;

                //  Image found at http://www.clipartbest.com/cliparts/yTo/7rX/yTo7rX4TE.png
                private final Icon unownedIcon = new ImageIcon(getClass().getResource("yTo7rX4TE.png"));

                @Override public Component getTreeCellRendererComponent(JTree tree, Object value,
                        boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
                    
                    Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
                    
                    Object userObject = ((DefaultMutableTreeNode) value).getUserObject();



                    if (userObject instanceof Game) {
                        
                        //  Image found at http://icons.iconseeker.com/png/fullsize/crystal-clear-actions/star-10.png
                        Icon gameIcon = new ImageIcon(getClass().getResource("star-10.png"));

                        if (gameIcon != null) { setIcon(gameIcon); }

                    } //  end if a Game is chosen
                    
                    
                    else if (userObject instanceof Cave) {
                        
                        //  Image found at http://www.icon2s.com/wp-content/uploads/2013/07/black-white-metro-cave-icon.png
                        Icon caveIcon = new ImageIcon(getClass().getResource("black-white-metro-cave-icon.png"));

                        if (caveIcon != null) { setIcon(caveIcon); }

                    } //  end if a Cave is chosen
                    
                    
                    else if (userObject instanceof Party) {
                        
                        //  Image found at http://png-1.findicons.com/files/icons/560/fast_icon_users/128/user_group.png
                        Icon partyIcon = new ImageIcon(getClass().getResource("user_group.png"));

                        if (partyIcon != null) { setIcon(partyIcon); }

                    } //  end if a Party is chosen
                    
                    
                    else if (userObject instanceof Creature) {
                        
                        //  Image found at http://yellowicons.com/wp-content/uploads/Person-Icon-4.png
                        Icon creatureIcon = new ImageIcon(getClass().getResource("Person-Icon-4.png"));

                        if (creatureIcon != null) { setIcon(creatureIcon); }

                    } //  end if a Creature is chosen
                    
                    
                    else if (userObject instanceof Artifact || userObject.equals("Artifacts")) {
                        
                        //  Image found at http://www.veryicon.com/icon/png/Movie%20%26%20TV/300/300%20sword.png
                        Icon artifactIcon = new ImageIcon(getClass().getResource("300 sword.png"));

                        if (artifactIcon != null) { setIcon(artifactIcon); }

                    } //  end if a Artifact is chosen
                    
                    
                    else if (userObject instanceof Treasure || userObject.equals("Treasures")) {
                        
                        //  Image found at http://png-3.findicons.com/files/icons/1307/jolly_roger/128/pirate_treasure.png
                        Icon treasureIcon = new ImageIcon(getClass().getResource("pirate_treasure.png"));

                        if (treasureIcon != null) { setIcon(treasureIcon); }

                    } //  end if a Treasure is chosen
                    else if (userObject instanceof Job || userObject.equals("Jobs")) {
                        
                        //  Image found at http://vieclam.dtvc.edu.vn/hinhuser/vieclam/iab-job-task.png
                        Icon jobIcon = new ImageIcon(getClass().getResource("iab-job-task.png"));

                        if (jobIcon != null) { setIcon(jobIcon); }

                    } //  end if a Job is chosen
                    
                    
                    else if (unownedIcon != null) {

                        setIcon(unownedIcon);

                    }  //  end if-else

                    return c;

                }  //  end method getTreeCellRendererComponent()

            });

            node.add(cave.buildTree());

            node = new DefaultMutableTreeNode("Unowned Objects");

            for (Map.Entry<Integer, GameElement> element : unownedElements.entrySet()) {
                node.add(new DefaultMutableTreeNode(element.getValue()));
            }

            root.add(node);

            return newTree;

        }  //  end method buildTree()
        
        
        
        private void gatherPanels() {
            artifactPanels = new ArrayList<>();
            
            //  iterate through all GameElements, find Job objects and add their panes:
            for (Party p : partyList) {
                
                if (p.jobList.size() < 1) continue;
                
                ArrayList<JPanel> panelList = p.getPanels();
                
                for (JPanel panel : panelList) {
                        artifactPanels.add(panel);
                    }  //  for each JPanel in panelList
                
            }  //  for each Party
        }



        /**
         * Builds a new <code>JTree</code> using <code>buildTree()</code>, and
         * then replaces <code>tree</code>'s model with that of the new <code>JTree</code>
         */
        public void refreshTree() {
            JTree temp = buildTree();
            tree.setModel(temp.getModel());
        }  //  end method refreshTree()
        
        
        
    }  //  end class GameFrame



}  //  end class Game
