package model;

/**
 * Created by plouzeau on 2014-10-09.
 */
public interface Tile extends ContientPropriete{

	public static final int NONDEFINI = -1 ;
	
    int getRank();

    void incrementRank();
    
    void setValeurTuile(Tile valeur) ;
   
}
