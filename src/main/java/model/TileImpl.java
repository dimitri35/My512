package model;
import javafx.beans.InvalidationListener;
import javafx.beans.value.* ;
import javafx.beans.property.* ;
/**
 * Created by plouzeau on 2014-10-09.
 */
public class TileImpl implements Tile {

    //private  int rank;
    private IntegerProperty propriete ;
   

    public TileImpl(int rank) {
        //this.rank = rank;
    	propriete = new SimpleIntegerProperty() ;
        propriete.set(rank);
    }

    public TileImpl()
    {
    	propriete = new SimpleIntegerProperty() ;
    	propriete.set(NONDEFINI);
    }
    
    public int getRank() {
        return propriete.get();
    }

    public void incrementRank() {
        //this.rank++;
    	propriete.set(getRank()+1);
    }
    
    public IntegerProperty getPropriete() {
		return propriete;
	}
    
    public void  setValeurTuile(Tile valeur)
    {
    	if(valeur==null)
    		propriete.setValue(Tile.NONDEFINI) ;
    	else
    		propriete.setValue(valeur.getRank());
    }
    
    @Override
    public String toString() {
    	
    	return propriete.getValue().toString();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj instanceof TileImpl)
    	{
    		TileImpl tuile2  = (TileImpl) obj ;
    		return getRank()==tuile2.getRank() ;
    	}
    	else
    	{
    		return false ;
    	}
    		
    }
}
