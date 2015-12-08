/**
 * Treasure.java
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
 * Represents an treasure owned by a Creature object.  It also contains an
 * integer static value that represents the index to be used for the next Party
 * object created, in the case that the constructor gets an erroneous index.
 * 
 * @author      Alan Johnson
 * @version     1.3
 */
public class Treasure implements GameElement{
    
    //                       ----  Class Variables  ----
    
    private static int nextIndex = 30_000;
    
    //                       ----  Instance Variables  ----
    
    private int index, creatureIndex;
    
    private double weight, value;
    
    private String type;
    
    
    //                       ----  Constructors  ----
    
    
    
    /**
     * 
     * @param tokens    An array of String[6] objects, beginning with "t"
     */
    public Treasure(ArrayList<String> tokens) {
        
        this(Integer.parseInt(tokens.get(1)), tokens.get(2), 
                Integer.parseInt(tokens.get(3)), Double.parseDouble(tokens.get(4)),
                Integer.parseInt(tokens.get(5)));
        
    }  //  end Treasure constructor
    
    
    
    /**
     * @param _index    Treasure index
     * @param _type     Treasure type
     * @param creature  index of the owning Creature object
     * @param _weight   Treasure weight
     * @param _value    Treasure value
     */
    public Treasure(int _index, String _type, int creature, double _weight, int _value) {
        
        if (_index >= 30_000 && _index < 40_000 && _index >= nextIndex) 
            index = nextIndex = _index;
        
        else
            index = nextIndex++;
         
         
         
         if (_type != null)
             type = _type;
         
         else
             type = "no_name";
         
         
         
         if (creature >= 20_000 && creature < 30_000)
             creatureIndex = creature;
         
         else
             creatureIndex = 0;
         
         
         
         if (_weight > 0)
             weight = _weight;
         
         else
             weight = 0;
         
         
         
         if (_value > 0)
             value = _value;
         
         else
             value = 0;
         
         System.out.printf("Treasure[%d] (%s) created.\n", index, type);
        
    }
    
    
    //                       ----  Other Methods  ----
    
    
    
    /**
     * Returns the index that identifies the Creature object that owns this
     * Treasure.
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
    @Override
    public String getName() {
        
        return type;
        
    }  //  end getName() method
    
    
    
    /**
     * Returns the value of weight.
     * @return     the value of weight
     */
    public double getWeight() {
        return weight;
    }
    
    
    
    /**
     * Returns the value of type.  It mirrors getName(), because the GameElement
     * interface requires both.
     * 
     * @return      the value of type
     */
    @Override
    public String getType() {
        return "Treasure";
    }
    
    
    
    /**
     * Returns the value of 'value'
     * 
     * @return     the value of 'value'
     */
    public double getValue() {
        return value;
    }
    
    
    
    /**
     * 
     * @return     String representation
     */
    @Override
    public String toString() {
         
         return type;
         
     }
 
}  //  end class Treasure
