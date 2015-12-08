/**
 * Creature.java
 * 
 * CMSC 335
 * Project 4
 * 
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>A <code>Creature</code> object belongs to a <code>Party</code> object.  It
 * also owns <code>ArrayList</code>s of <code>Artifact</code> and <code>Treasure</code>
 * objects and an <code>index</code> value that identifies its owner (a
 * <code>Party</code> object).  It also contains an integer static value that
 * represents the index to be used for the next <code>Party</code> object created,
 * in the case that the constructor gets an erroneous index.</p>
 * 
 * <p><code>Creature</code> objects also implement methods to add
 * <code>Treasure</code>/<code>Artifact</code> objects to itself.</p>
 * 
 * @author      Alan Johnson
 * @version     1.3
 */
public class Creature implements GameElement{
    
    //                       ----  Class Variable  ----
    
    private static int nextIndex = 20_000;
    
    //                       ----  Instance Variables  ----
    
    private int index, partyIndex, empathy, fear, capacity;
    private float age, height, weight;
    public boolean isBusy = false;
    private String type, name, sortMode = "name";
    
    private ArrayList<Treasure> treasureList;
    private ArrayList<Artifact> artifactList;
    private ArrayList<Job> jobList;
    private Party owner;
    
    
    //                       ----  Constructors  ----
    
    
    
    /**
     * This is only to be used by the <code>newCreatureFromTokens()</code> method,
     * to prevent array size issues
     * 
     * @param tokens    An array of <code>String[8]</code> objects, beginning with "<code>c</code>"
     */
    private Creature (ArrayList<String> tokens) {
        
        this(Integer.parseInt(tokens.get(1)), tokens.get(2), tokens.get(3), 
                Integer.parseInt(tokens.get(4)), Integer.parseInt(tokens.get(5)),
                Integer.parseInt(tokens.get(6)), Integer.parseInt(tokens.get(7)),
                Float.parseFloat(tokens.get(8)), Float.parseFloat(tokens.get(9)),
                Float.parseFloat(tokens.get(10)));
        
    }  //  end constructor
    
    
    
    /**
     * @param _index      Creature index
     * @param _type       Creature type
     * @param _name       Creature name
     * @param _party      Index of owning Party object
     * @param _empathy    Creature empathy
     * @param _fear       Creature fear
     * @param _capacity   Creature capacity
     * @param _age        Creature age
     * @param _height     Creature age
     * @param _weight     Creature weight
     */
    public Creature(int _index, String _type, String _name, int _party,
            int _empathy, int _fear, int _capacity, float _age, float _height,
            float _weight) {
        
        
        //    Index
        if (_index >= 20_000 && _index < 30_000 && _index >= nextIndex) 
            index = nextIndex = _index;
        
        else
            index = nextIndex++;
        
        
        
        //    Type
        if (_type != null)
            type = _type;
        
        
        
        //    Name
        if (_name != null)
            name = _name;
        
        else
            name = "no_name";
        
        
        
        //    Party Index
        if (_party >= 10_000 && _party < 20_000)
            partyIndex = _party;
        
        else
            partyIndex = 0;
        
        
        
        //    Empathy
        if (_empathy >= 0)
            empathy = _empathy;
        
        else
            empathy = 0;
        
        
        
        //    Fear
        if (_fear >= 0)
            fear = _fear;
        
        else
            fear = 0;
        
        
        
        //    Capacity
        if (_capacity >= 0)
            capacity = _capacity;
        else
            capacity = 0;
        
        
        
        //    Age
        if (_age >= 0)
            age = _age;
        else
            age = 0;
        
        
        
        //    Weight
        if (_weight >= 0)
            weight = _weight;
        else
            weight = 0;
        
        
        
        //    Height
        if (_height >= 0)
            height = _height;
        else
            height = 0;
        
        
        treasureList = new ArrayList<>();
        
        artifactList = new ArrayList<>();
        
        jobList = new ArrayList<>();
        
        System.out.printf("Creature[%d] (%s) created.\n", index, name);
        
    }
 
    
    //                       ----  Other Methods  ----
    
    
    
    /**
     * Adds the given <code>Artifact</code> object to <code>treasureList</code>.
     * 
     * @param artifact      <code>Artifact</code> object to be added to <code>Creature</code>
     */
    public void addArtifact(Artifact artifact) { artifactList.add(artifact); }  //  end method addArtifact()
    
    
    
    /**
     * Adds the given <code>Job</code> object to <code>jobList</code>.
     * 
     * @param job      <code>Job</code> object to be added to <code>Creature</code>
     */
    public void addJob(Job job) {
        jobList.add(job);
        job.setOwner(this);
    }  //  end method addJob()
    
    
    
    /**
     * Adds the given <code>Treasure</code> object to <code>treasureList</code>
     * 
     * @param treasure      <code>Treasure</code> object to be added to <code>Creature</code>
     */
    public void addTreasure(Treasure treasure) { treasureList.add(treasure); }  //  end method addTreasure()
    
    
    
    /**
     * Returns the value of <code>index</code>.
     * 
     * @return      the value of the <code>index</code>
     */
    @Override public int getIndex() { return index; }  //  end getIndex() method
    
    
    
    /**
     * Returns the value of <code>name</code>.
     * 
     * @return      the value of <code>name</code>
     */
    @Override
    public String getName(){ return name; }
    
    
    
    /**
     * 
     * @return 
     */
    public Party getOwner() { return owner; }  //  end method getOwner()
    
    
    
    /**
     * Returns the value of <code>type</code>
     * 
     * @return     the value of <code>type</code>
     */
    @Override public String getType() { return type; }  //  end method toString()
    
    
    
    /**
     * Returns the value of <code>weight</code>
     * 
     * @return     value of <code>weight</code>
     */
    public float getWeight() { return weight; }  //  end method getWeight()
    
    
    
     /**
     * Provides error-checking on the <code>tokens</code> parameter before they
     * are given to the <code>Creature</code> constructor.
     * 
     * @param tokens    Raw list of tokens
     * 
     * @return          new <code>Creature</code> object
     */
    public static Creature newCreatureFromTokens(ArrayList<String> tokens) {
        
        int missing = 11 - tokens.size();
        
        //  fix missing elements in tokens
        for (int i = 0; i < missing; i++) { tokens.add("0"); }
        
        return new Creature(tokens);
    }  //  end newCreatureFromTokens() method
    
    
    
    /**
     * Returns the value of <code>age</code>.
     * 
     * @return      value of <code>age</code>
     */
    public float getAge() { return age; }  //  end method getAge()
    
    
    
    /**
     * Returns the value of <code>height</code>
     * 
     * @return     value of <code>height</code>
     */
    public float getHeight() { return height; }  //  end method getHeight()
    
    
    
    /**
     * Builds a sub-<code>JTree</code> by adding appropriate nodes for each
     * <code>Artifact</code>, <code>Treasure</code>, and <code>Job</code> in
     * <code>artifactList</code>, <code>treasureList</code>, and <code>jobList</code>,
     * respectively.
     * 
     * @return      Sub-<code>JTree</code> of all elements contained in this
     *              <code>Creature</code>
     */
    public DefaultMutableTreeNode buildTree() {
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
        DefaultMutableTreeNode subNode;
        String string;
        
        //  Age:
        string = String.format("Age : %.1f", age);
        subNode = new DefaultMutableTreeNode(string);
        node.add(subNode);
        
        //  Height:
        string = String.format("Height : %.1f", height);
        subNode = new DefaultMutableTreeNode(string);
        node.add(subNode);
        
        //  Weight:
        string = String.format("Weight : %.1f", weight);
        subNode = new DefaultMutableTreeNode(string);
        node.add(subNode);
        
        if (artifactList.size() > 0) {
            subNode = new DefaultMutableTreeNode("Artifacts");
            node.add(subNode);
        
            for (Artifact artifact : artifactList) {
            
                subNode.add(new DefaultMutableTreeNode(artifact));
            }  //  end for each artifact in artifactList
        }  // if Creature has artifacts
        
        if (treasureList.size() > 0) {
            subNode = new DefaultMutableTreeNode("Treasures");
            node.add(subNode);
            for (Treasure treasure : treasureList) {
            
                DefaultMutableTreeNode temp = new DefaultMutableTreeNode(treasure);
                subNode.add(temp);
                
                string = String.format("Weight : %.1f", treasure.getWeight());
                temp.add(new DefaultMutableTreeNode(string));
                
                string = String.format("Value : %.1f", treasure.getValue());
                temp.add(new DefaultMutableTreeNode(string));
            }  //  end for each treasure in treasureList
        }  //  if Creature has treasures
        
        if (jobList.size() > 0) {
            subNode = new DefaultMutableTreeNode("Jobs");
            node.add(subNode);
            for (Job job : jobList) {
            
                subNode.add(new DefaultMutableTreeNode(job));
            }  //  end for each job in jobList
        }  //  if Creature has jobs
        
        return node;
        
    }  //  end method buildTree()
    
    
    
    /**
     * Returns the index that identifies the <code>Party</code> object that owns this
     * <code>Creature</code>.
     * 
     * @return     the index of the <code>Party</code> owner
     */
    public int getPartyIndex() { return partyIndex; }  //  end method getPartyIndex()
    
    
    
    /**
     * Sets the mode for sorting <code>Treasure</code> and <code>Artifact</code>
     * objects in <code>treasureList</code> and <code>artifactList</code>, 
     * respectively.
     * 
     * @param sort     Must be either "<code>weight</code>" or "<code>value</code>"
     */
    public void setItemSortMode(String sort) {
        
        if (sort.equals("weight") || sort.equals("value")) {
        
            sortMode = sort;
            sortItems();
        }  //  if sort is a valid sort parameter
    }  //  end method setItemSortMode()
    
    
    
    /**
     * 
     * @param p 
     */
    public void setOwner(Party p) {
        if (p != null) owner = p;
    }  //  end method setOwner()
    
    
    
    /**
     * Sorts the <code>Artifact</code> and <code>Treasure</code> objects of this
     * <code>Creature</code> based on current value of <code>sortMode</code>.
     */
    public void sortItems() {
        
        //  Sort Artifact objects before switch(), since there is only one field
        ArrayList <Artifact> a = new ArrayList<>(artifactList);
        
        Collections.sort(a, new Comparator<Artifact>() {
            
                    @Override public int compare(Artifact a1, Artifact a2) {
                
                        return a1.getName().compareTo(a2.getName());
                    }
        });
        
        ArrayList <Treasure> t = new ArrayList<>(treasureList);
        
        switch (sortMode) {
            
            case "weight":
                Collections.sort(t, new Comparator<Treasure>() {
            
                    @Override public int compare(Treasure t1, Treasure t2) {
                
                        return Double.compare(t1.getWeight(), t2.getWeight());
                    }
                });
            break;
        
        
            case "value":
                Collections.sort(t, new Comparator<Treasure>() {
            
                    @Override public int compare(Treasure t1, Treasure t2) {
                
                        return Double.compare(t1.getValue(), t2.getValue());
                    }
                });
            break;
                
        }  //  switch
        
        if (!a.isEmpty()) {
            //  Rebuild artifactList with new sorted order:
            artifactList = new ArrayList<>();
        
            for (Artifact artifact : a) { artifactList.add(artifact); }
        }
        
        if (!t.isEmpty()) {
            //  Rebuild treasureList with new sorted order:
            treasureList = new ArrayList<>();
        
            for (Treasure treasure : t) { treasureList.add(treasure); }
        }
    }  //  end method sortItems()
    
    
    
    /**
     * 
     * @return     <code>String</code> representation
     */
    @Override public String toString() { return name; }  //  end method toString()
    
    
    
}  // end class Creature