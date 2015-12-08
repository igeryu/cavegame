/**
 * Cave.java
 * 
 * CMSC 335
 * Project 4
 * 
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <p>A <code>Cave</code> object owns an <code>ArrayList</code> of <code>Party</code>
 * objects and a pointer to its owner (a <code>Game</code> instance).</p>
 * 
 * <p>The owning <code>Game</code> object passes new <code>Party</code>,
 * <code>Creature</code>, <code>Artifact</code>, and <code>Treasure</code>
 * objects to the <code>Cave</code> as they are added.  The <code>Cave</code> is
 * then responsible for handing those objects to their appropriate owners.</p>
 * 
 * @author      Alan Johnson
 * @version     1.3
 */
public class Cave {
    
    //                       ----  Instance Variables  ----
    
    private final ArrayList<Party> partyList;
    private Game owner;
    
    //                       ----  Constructor  ----
    
    /**
     * @param _owner      <code>Game</code> object that will own this <code>Cave</code>
     */
    public Cave (Game _owner) {
        partyList = new ArrayList<>();
        setOwner(_owner);
    }
    
    
    
    //                       ----  Other Methods  ----
    
    /**
     * Attempts to add supplied <code>Artifact</code> to a <code>Creature</code>
     * within this <code>Cave</code>.  If the appropriate <code>Creature</code>
     * cannot be found (index mismatch), the method returns <code>false</code>.  Otherwise,
     * the <code>Artifact</code> is added and the method returns true.
     * 
     * @param artifact      <code>Artifact</code> object to be added
     * @return              <code>Boolean</code> value to indicate whether the
     *                      <code>Artifact</code> was successfully added to a
     *                      <code>Creature</code> object.
     */
    public boolean addArtifact(Artifact artifact) {
        
        for (Party party : partyList) {
            
            if (party.addArtifact(artifact)) { return true; }
            
        }  //  for each party in partyList
        
        return false;
        
    }  //  end addArtifact() method
    
    
    
    /**
     * Attempts to add supplied <code>Creature</code> to a <code>Party</code> in
     * <code>partyList</code>.  If the appropriate <code>Party</code> cannot be
     * found (index mismatch), the method returns <code>false</code>.  Otherwise, the
     * <code>Creature</code> is added and the method returns <code>true</code>.
     * 
     * @param creature      <code>Creature</code> object to be added
     * 
     * @return              <code>Boolean</code> value to indicate whether the
     *                      <code>Creature</code> was successfully added to a
     *                      <code>Party</code> object.
     */
    public boolean addCreature(Creature creature) {
        
        
        
        for (Party party : partyList) {
            
            if (party.getIndex() == creature.getPartyIndex()) {
                
                party.addCreature(creature);
                return true;
            }  //  if (matching indexes)
        }  //  for each party in partyList
        
        return false;
    }  //  end addCreature() method
    
    
    
    /**
     * Attempts to add supplied <code>Job</code> to a <code>Creature</code>
     * within this <code>Cave</code>.  If the appropriate <code>Creature</code>
     * cannot be found (index mismatch), the method returns <code>false</code>.
     * Otherwise, the <code>Job</code> is added and the method returns <code>true</code>.
     * 
     * @param job     <code>Job</code> object to be added
     * @return        <code>Boolean</code> value to indicate whether the
     *                <code>Job</code> was successfully added.
     */
    public boolean addJob(Job job) {
        
        for (Party party : partyList) {
            
            if (party.addJob(job)) { return true; }
        }
        
        return false;
    }  //  end method addJob()
    
    
    
    /**
     * Adds the given <code>Party</code> to <code>partyList</code>.
     * 
     * @param party      <code>Party</code> object to be added
     */
    public void addParty(Party party) {
        
        if (party != null) { partyList.add(party); }
        else System.out.println("\nError adding party...\n");
        
    }  //  end addParty() method
    
    
    
    /**
     * Attempts to add supplied <code>Treasure</code> to a <code>Creature</code>
     * within this <code>Cave</code>.  If the appropriate <code>Creature</code>
     * cannot be found (index mismatch), the method returns <code>false</code>.  
     * Otherwise, the <code>Treasure</code> is added and the method returns <code>true</code>.
     * 
     * @param treasure      <code>Treasure</code> object to be added
     * 
     * @return              <code>Boolean</code> value to indicate whether the <code>Treasure</code> was
     *                      successfully added to a <code>Creature</code> object.
     */
    public boolean addTreasure(Treasure treasure) {
        
        for (Party party : partyList) {
            
            if (party.addTreasure(treasure)) { return true; }
        }  //  for each party in partyList
        
        return false;
    }  //  end addTreasure() method
    
    
    
    /**
     * Builds a sub-<code>JTree</code> by calling <code>buildTree()</code> on each
     * <code>Party</code> in <code>partyList</code>.
     * 
     * @return      Sub-<code>JTree</code> of all elements contained in this
     *              <code>Cave</code>
     */
    public DefaultMutableTreeNode buildTree() {
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
        
        for (Party party : partyList) { node.add(party.buildTree()); }
        
        return node;
    }  //  end method buildTree()
    
    
    
    /**
     * 
     */
    public void createSemaphores() {
        
        for (Party p : partyList)
            p.createSemaphores();
        
    }  //  end method createSemaphores()
    
    
    
    /**
     * <p>Checks the <code>partyList</code> for a <code>Party</code> object with
     * the given <code>index</code> parameter.</p>
     * <p>If found, it returns that <code>Party</code> object, otherwise returns <code>null</code>.</p>
     * 
     * @param index      Index of the <code>Party</code> object to be found
     * 
     * @return           The <code>Party</code> with the given index, or <code>null</code>.
     */
    public Party getParty(int index) {
        
        for (Party party : partyList) {
            
            if (party.getIndex() == index) { return party; }
        }  // for each party in partyList
        
        return null;
    }  //  end getParty() method
    
    
    
    /**
     * Sets the sort mode for all <code>Creature</code> objects in <code>partyList</code>
     * to the supplied <code>sort</code> parameter.
     * 
     * @param sort     Must be "<code>age</code>", "<code>height</code>",
     *                 "<code>name</code>", or "<code>weight</code>"
     */
    public void setCreatureSortMode(String sort) {
        
        for (Party party : partyList) { party.setCreatureSortMode(sort); }
    }  //  end setCreatureSortMode()
    
    
    
    /**
     * Set the sort mode for all <code>Artifact</code> and <code>Treasure</code>
     * objects in <code>partyList</code> to the supplied <code>sort</code> parameter.
     * 
     * @param sort     Must be "<code>weight</code>" or "<code>value</code>"
     */
    public void setItemSortMode(String sort) {
        
        for (Party party : partyList) { party.setItemSortMode(sort); }
    }  //  end setCreatureSortMode()
    
    
    
    /**
     * Sets <code>owner</code> to point towards the supplied <code>Game</code> object.
     * 
     * @param game      <code>Game</code> object to own this <code>Cave</code>
     */
    public final void setOwner(Game game) {
        
        if (game != null) { owner = game; }
        
    }  //  end setOwner() method
    
    
    
    /**
     * 
     * @return     <code>String</code> representation
     */
    @Override public String toString() { return "Cave"; }  //  end toString() method
    
    
    
}  //  end class Cave
