/**
 * Artifact.java
 * 
 * CMSC 335
 * Project 4
 * 
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */



import java.util.ArrayList;


/**
 * Represents an artifact owned by a Creature object.  It also contains an
 * integer static value that represents the index to be used for the next Party
 * object created, in the case that the constructor gets an erroneous index.
 * 
 * @author      Alan Johnson
 * @version     1.3
 */
public class Artifact implements GameElement {
    
    //                       ----  Class Variable  ----
    
    private static int nextIndex = 40_000;
    
    //                       ----  Instance Variables  ----
    
    private int index, creatureIndex;
    
    private String type;
    private Party owner;
    
    
    //                       ----  Constructors  ----
    
    
    
    /**
     * 
     * @param tokens    An array of String[4] objects, beginning with "a"
     */
     public Artifact(ArrayList<String> tokens) {
        
        this(Integer.parseInt(tokens.get(1)), tokens.get(2), 
                Integer.parseInt(tokens.get(3)));
        
    }  //  end Artifact constructor
     
     
     
     /**
     * @param _index    Artifact index
     * @param _type     Artifact type
     * @param creature  index of the owning Creature object
     */
     public Artifact(int _index, String _type, int creature) {
         
         if (_index >= 40_000 && _index < 50_000 && _index >= nextIndex) 
            index = nextIndex = _index;
        
        else
            index = nextIndex++;
         
         
         
         if (_type != null) {
             type = _type;
             
             if (type.charAt(type.length() - 1) == 's') {
                 type = type.substring(0, type.length() - 2);
             }
         }
         
         else
             type = "no_name";
         
         
         
         if (creature >= 20_000 && creature < 30_000)
             creatureIndex = creature;
         
         else
             creatureIndex = 0;
         
         System.out.printf("Artifact[%d] (%s) created.\n", index, type);
         
     }
     
     
     //                       ----  Other Methods  ----
     
     
     
     /**
     * Returns the index that identifies the Creature object that owns this
     * Artifact.
     * 
     * @return     the index of the Creature owner
     */
     public int getCreatureIndex() {
         return creatureIndex;
     }
     
     
     
     /**
     * Returns the value of index.
     * 
     * @return      the value of the index
     */
     @Override
     public int getIndex() {
        return index;
    }
     
     
     
     /**
     * Returns the value of type.  It mirrors getType(), because the GameElement
     * interface requires both.
     * 
     * @return      the value of type
     */
     @Override public String getName() {
         
         return type;
         
     }
     
     
     
     
     /**
     * Returns the value of type.  It mirrors getName(), because the GameElement
     * interface requires both.
     * 
     * @return      the value of type
     */
     @Override
     public String getType() {
         return "Artifact";
     }
     
     
     
     
     
     
     
     /**
      * 
      * @return     String representation
      */
     @Override public String toString() {
         
         return type;
         
     }  //  end toString() method
     
     
     
    
     
     
     
}  //  end class Artifact
