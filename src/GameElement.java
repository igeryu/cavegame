/**
 * GameeElement.java
 * 
 * CMSC 335
 * Project 4
 * 
 * Alan Johnson
 * 16 January 15
 * NetBeans IDE 8.0.2
 */

/**
 * Represents a game element that exist at different tiers of the game
 * structure.
 * 
 * @author      Alan Johnson
 * @version     1.3
 */
public interface GameElement {
    
    /**
     * Returns the value of index.
     * 
     * @return      the value of the index
     */
    public int getIndex();
    
    
    
    /**
     * Returns the value of name.  In the case of an element without a name 
     * (Artifact or Treasure), returns the value of type
     * 
     * @return      the value of name or type
     */
    public String getName();
    
    
    
    /**
     * Returns the value of type
     * 
     * @return     the value of type
     */
    public String getType();
    
}
