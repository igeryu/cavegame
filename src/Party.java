/**
 * Party.java
 * 
 * CMSC 335
 * Project 4
 * 
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */



import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>A <code>Party</code> object owns an <code>ArrayList&lt;Creature&gt;</code>.  
 * It also contains an <code>int</code> static value that represents the index
 * to be used for the next <code>Party</code> object created, in the case that
 * the constructor gets an erroneous index.</p>
 * 
 * <p><code>Party</code> objects also implement methods to add <code>Creature</code>
 * objects and <code>Treasure</code>/<code>Artifact</code> objects to them.</p
 * 
 * @author      Alan Johnson
 * @version     1.3
 */
public class Party implements GameElement{
    
    //                       ----  Class Variable  ----
    
    private static int nextIndex = 10_000;
    
    //                       ----  Instance Variables  ----
    
    private int index;
    private String name;
    private ArrayList<Creature> creatureList;
    private String sortMode = "name"; // set default vaule
    private Map<String, Integer> artifactList;
    private HashMap<String, Semaphore> semaphoreList;
    public ArrayList<Job> jobList;
    private ArrayList<JPanel> artifactPanels;
    
    
    //                       ----  Constructors  ----
    
    
    
    /**
     * @param _index      Party index
     * @param _name       Party name
     */
    public Party (int _index, String _name) {
        
        if (_index >= 10_000 && _index < 20_000 && _index >= nextIndex) 
            index = nextIndex = _index;
        
        else index = nextIndex++;
        
        if (_name != null)
            name = _name;
        
        else
            name = "no_name";
        
        System.out.printf("Cave[%d] (%s) created.\n", index, name);
        creatureList = new ArrayList<>();
        jobList = new ArrayList<>();
        artifactList = new HashMap<>();
        semaphoreList = new HashMap<>();
    }
    
    
    
    /**
     * 
     * @param tokens    An array of <code>String[3]</code> objects, beginning
     *                  with "<code>p</code>"
     */
    public Party (ArrayList<String> tokens) {
        
        this(Integer.parseInt(tokens.get(1)), tokens.get(2));
        creatureList = new ArrayList<>();
    }  //  end Party(ArrayList<String>) constructor
    
    
    //                       ----  Other Methods  ----
    
    
    
    /**
     * Adds the given <code>Artifact</code> object to a <code>Creature</code>
     * object contained in <code>creatureList</code>.
     * 
     * @param artifact      <code>Artifact</code> object to be added to a <code>Creature</code>
     * @return              <code>Boolean</code> value to indicate whether the
     *                      <code>Artifact</code> was successfully added to a
     *                      <code>Creature</code> object.
     */
    public boolean addArtifact(Artifact artifact) {
        
        for (Creature creature : creatureList) {
            
            if (creature.getIndex() == artifact.getCreatureIndex()) {
                
                creature.addArtifact(artifact);
                
                //  Add Artifact to this Party's artifactList:
                if (artifactList.containsKey(artifact.getName())) {
                    Integer newValue = artifactList.get(artifact.getName()) + 1;
                    artifactList.put(artifact.getName(), newValue);
                }else artifactList.put(artifact.getName(), 1);
                
                return true;
            }  // if (matching indexes)
        }  //  for each creature in creatureList
        
        return false;
    }  //   end addArtifact()
    
    
    
    /**
     * Adds the given <code>Creature</code> object to <code>creatureList</code>.
     * 
     * @param creature      <code>Creature</code> to be added
     */
    public void addCreature(Creature creature) {
        creature.setOwner(this);
        creatureList.add(creature);
    }  //  end method addCreature()
    
    
    
    /**
     * Adds the given <code>Job</code> object to a <code>Creature</code> in
     * <code>creatureList</code>.
     * 
     * @param job     <code>Job</code> object to be added to a <code>Creature</code>
     * @return        <code>Boolean</code> value to indicate whether the
     *                      <code>Job</code> was successfully added to a
     *                      <code>Creature</code> object.
     */
    public boolean addJob(Job job) {
        
        Integer jobIndex = job.getCreatureIndex();
        
        for (Creature creature : creatureList) {
            
            Integer creatureIndex = creature.getIndex();
            
            if (jobIndex.equals(creatureIndex)) {
                
                creature.addJob(job);
                jobList.add(job);
                return true;
            }  //  if creature could add a job
        }  //  for each creature in creatureList
        
        return false;
    }  //  end method addJob()
    
    
    
    /**
     * Adds the given <code>Treasure</code> object to a <code>Creature</code>
     * object contained in <code>creatureList</code>.
     * 
     * @param treasure      <code>Treasure</code> object to be added to a <code>Creature</code>
     * @return               <code>Boolean</code> value to indicate whether the
     *                      <code>Treasure</code> was successfully added to a
     *                      <code>Creature</code> object.
     */
    public boolean addTreasure(Treasure treasure) {
        
        for (Creature creature : creatureList) {
            
            if (creature.getIndex() == treasure.getCreatureIndex()) {
                
                creature.addTreasure(treasure);
                return true;
            }  // if (matching indexes)
        }  //  for each creature in creatureList
        
        return false;
    }  //   end addTreasure()
    
    
    
    /**
     * Builds a sub-<code>JTree</code> by calling <code>buildTree()</code> on each
     * <code>Creature</code> in <code>creatureList</code>.
     * 
     * @return      Sub-<code>JTree</code> of all elements contained in this
     *              <code>Party</code>
     */
    public DefaultMutableTreeNode buildTree() {
        
        sortCreatures();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
        
        for (Creature creature : creatureList) {
            
            creature.sortItems();
            node.add(creature.buildTree());
        }
        
        return node;
    }  //  end method buildTree()
    
    
    
    /**
     * 
     */
    public void createSemaphores() {
        
        for (Entry<String, Integer> entry : artifactList.entrySet()) {
            String s = entry.getKey();
            
            Semaphore semaphore = new Semaphore(entry.getValue()) {
                private static final long serialVersionUID = 1L;
                
                public int max;
                {
                    max = availablePermits();
                }
                
                @Override public void release() {
                    int current = availablePermits();
                    
                    if (current + 1 > max) throw new IllegalArgumentException();
                    
                    super.release();
                }
                
                 @Override public void release(int permits) {
                    int current = availablePermits();
                    
                    if (current + permits > max) throw new IllegalArgumentException();
                    
                    super.release(permits);
                }
            };
            
            
            semaphoreList.put(s, semaphore);
            
        }  //  for each artifact, create a corresponding semaphore
        
        startJobs();
        
    }  //  end method createSemaphores()
    
    
    
    /**
     * Checks <code>creatureList</code> for a <code>Creature</code> object with
     * the given <code>index</code> parameter.  If found, it returns that
     * <code>Creature</code> object, otherwise returns <code>null</code>.
     * 
     * @param index      Index of the <code>Creature</code> object to be found
     * 
     * @return           <code>Creature</code> with the given index, or <code>null</code>.
     */
    public Creature getCreature(int index) {
        
        for (Creature creature : creatureList) {
            
            if (creature.getIndex() == index) { return creature; }
        }  // for each creature in creatureList
        
        return null;
    }  //  end getCreature() method
    
    
    
    /**
     * Returns the value of <code>index</code>.
     * 
     * @return      the value of the <code>index</code>
     */
    @Override
    public int getIndex() { return index; }  //  end method getIndex()
    
    
    
    /**
     * Returns the value of <code>name</code>.
     * 
     * @return      the value of <code>name</code>
     */
    @Override
    public String getName() { return name; }  //  end method getName()
    
    
    
    public ArrayList<JPanel> getPanels() {
        
        if (artifactPanels != null) return artifactPanels;
        
        artifactPanels = new ArrayList<>();
        
        for (Entry<String, Semaphore> entry : semaphoreList.entrySet()) {
            artifactPanels.add(new ArtifactPanel(entry.getKey(), entry.getValue(), artifactList.get(entry.getKey())));
        }
        
        return artifactPanels;
    }
    
    
    
    /**
     * Returns the value of <code>type</code>
     * 
     * @return     the value of <code>type</code>
     */
    @Override
    public String getType() { return "Party"; }  //  end method getType()
    
    
    
    /**
     * 
     */
    private void startJobs() {
        
        for (Job j : jobList) {
            j.startJob(semaphoreList);
        }
        
    }
    
    
    
    /**
     * 
     * @return     <code>String</code> representation
     */
    @Override public String toString() { return name; }  //  end toString() method
    
    
    
    public void updateArtifactPanels() {
        getPanels();
        
        synchronized (artifactPanels) {

            try {
            for (JPanel p : artifactPanels) {
                ArtifactPanel panel = (ArtifactPanel) p;

                if (panel != null) {
                    panel.update();
                }

            }  //  for each panel in artifactPanels
            
            } catch (Exception e) { }

        }  //  end synchronized
        
        
    }  //  end method updateArtifactPanels()
    
    
    
    /**
     * Sets <code>sortMode</code> and then sorts the <code>creatureList</code>
     * 
     * @param sort     Must be either "<code>age</code>", "<code>height</code>", "<code>name</code>", or "<code>weight</code>"
     */
    public void setCreatureSortMode(String sort) {
        
        sort = sort.toLowerCase();
        
        if (sort.equals("age") || sort.equals("height") || sort.equals("name") || sort.equals("weight")) {
        
            sortMode = sort;
            sortCreatures();
        }
    }  //  end method setCreatureSortMode()
    
    
    
    /**
     * Sets the sort mode for <code>Artifact</code> and <code>Treasure</code> objects
     * in <code>artifactList</code> and <code>treasureList</code>, respectively.
     * 
     * @param sort     must be either "<code>value</code>" or "<code>weight</code>"
     */
    public void setItemSortMode(String sort) {
        
        for (Creature creature : creatureList) { creature.setItemSortMode(sort); }
        
    }  //  end setItemSortMode() method
    
    
    
    /**
     * Sorts <code>creatureList</code> based on <code>sortMode</code>
     */
    public void sortCreatures() {
        
        ArrayList <Creature> c = new ArrayList<>(creatureList);
        
        switch (sortMode) {
            
            case "age":
                Collections.sort(c, new Comparator<Creature>() {
                    @Override public int compare(Creature c1, Creature c2) {
                        return Float.compare(c1.getAge(), c2.getAge());
                    }
                });
            break;
        
                
            case "height":
                Collections.sort(c, new Comparator<Creature>() {
                    @Override public int compare(Creature c1, Creature c2) {
                        return Float.compare(c1.getHeight(), c2.getHeight());
                    }
                });
            break;
            
                
            case "name":
                Collections.sort(c, new Comparator<Creature>() {
                    @Override public int compare(Creature c1, Creature c2) {
                        return c1.getName().compareTo(c2.getName());
                    }
                });
            break;
                
                
            case "weight":
                Collections.sort(c, new Comparator<Creature>() {
                    @Override public int compare(Creature c1, Creature c2) {
                        return Float.compare(c1.getWeight(), c2.getWeight());
                    }
                });
            break;
        
        }  //  switch
        
        //  Rebuild creatureList with new sorted order:
        
        creatureList = new ArrayList<>();
        
        for (Creature creature : c) { creatureList.add(creature); }
        
    }  //  end method sortCreatures()
    
    
    
    
    
     /**
     * 
     */
    private class ArtifactPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        private final JLabel nameLabel;
        private JLabel statusLabel;
        
        private String name;
        
        private final int max;
        private int current;
        
        private final Semaphore semaphore;
        
        /**
         * 
         * @param o 
         */
        public ArtifactPanel (String str, Semaphore sema, int m) {
            super();
            if (str != null) name = str;
            current = max = m;
            semaphore = sema;
            FlowLayout layout = new FlowLayout();
            setSize(500, 20);
            
            setLayout(layout);
            JPanel panel = new JPanel();
            nameLabel = new JLabel();
            update();
            nameLabel.getInsets().set(1, 1, 1, 1);
            nameLabel.setPreferredSize(new Dimension(280, 15));
            panel.add(nameLabel);
            panel.setPreferredSize(new Dimension(290, 20));
            add(panel);
            
            
            
            
            
        }  //  end Job constructor
        
        
        
        public final void update() {
            current = semaphore.availablePermits();
            
            nameLabel.setText(String.format("%s: %s (%d/%d)", Party.this.name, name, current, max));
            
            if (current == max) setBackground(Color.green);
            else if (current == 0) setBackground(Color.red);
            else setBackground(Color.orange);
            
        }
        
        
        
    }  //  end class ArtifactPanel
    
    
}  // end class Party
