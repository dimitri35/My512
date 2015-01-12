package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;

/**
 * Created by plouzeau on 2014-10-09.
 */
public class BoardImpl implements Board {


    private final int sideSizeInSquares;
    private Direction directionToPackInto;

    public BoardImpl(int sideSizeInSquares) {
        if (sideSizeInSquares <= 1) {
            throw new IllegalArgumentException("sideSizeInSquares");
        }
        this.sideSizeInSquares = sideSizeInSquares;
        currentBoard = new Tile[sideSizeInSquares][sideSizeInSquares];
        nextBoard = new Tile[sideSizeInSquares][sideSizeInSquares];
        tuileARemplir = new Tile[sideSizeInSquares][sideSizeInSquares] ;
        
        for(int i=0;i<sideSizeInSquares;i++)
        {
        	for(int j=0;j<sideSizeInSquares;j++)
        	{
        		currentBoard[i][j] = new TileImpl(Tile.NONDEFINI) ;
        	}
        }
        
        for(int i=0;i<sideSizeInSquares;i++)
        {
        	for(int j=0;j<sideSizeInSquares;j++)
        	{
        		nextBoard[i][j] = new TileImpl(Tile.NONDEFINI) ;
        	}
        }
        
        
        for(int i=0;i<sideSizeInSquares;i++)
        {
        	for(int j=0;j<sideSizeInSquares;j++)
        	{
        		tuileARemplir[i][j] = new TileImpl(Tile.NONDEFINI) ;
        	}
        }
        
    }

    public int getSideSizeInSquares() {
        return this.sideSizeInSquares;
    }


    private Tile[][] currentBoard;
    private Tile[][] nextBoard;
    private Tile[][] tuileARemplir ;

    /**
     * Return the tile at a given coordinate, or null if none exists there.
     *
     * @param lineNumber   must be >=1 and <= getSideSizeInSquares()
     * @param columnNumber must be >=1 and <= getSideSizeInSquares()
     * @return a tile or null if none
     * @throws IllegalArgumentException if parameters are out of board's bounds
     */
    public Tile getTile(int lineNumber, int columnNumber) {
        return currentBoard[lineNumber - 1][columnNumber - 1];
    }

    /**
     * Apply the only game action: packing tiles
     * @param direction  where to push the tiles
     */
    public void packIntoDirection(Direction direction) {
        this.directionToPackInto = direction;
        for (int i = 1; i <= sideSizeInSquares; i++) {
            packLine(i);
        }

    }

    /**
     * Validate the step effects
     * NOTE: do we need this in the interface?
     */
    public void commit() {

        //currentBoard = nextBoard;
        for(int i=0;i<sideSizeInSquares;i++)
        {
        	for(int j=0;j<sideSizeInSquares;j++)
        	{
        		if(currentBoard[i][j] != null)
        			currentBoard[i][j].setValeurTuile(nextBoard[i][j]) ;
        		/*
        		else
        		{
        			Tile tuile = tuileARemplir[i][j] ;
        			tuile.setValeurTuile(nextBoard[i][j]);
        			currentBoard[i][j] = tuile ;
        		}
        		*/
        	}
        }
        nextBoard = new Tile[sideSizeInSquares][sideSizeInSquares];
        for(int i=0;i<sideSizeInSquares;i++)
        {
        	for(int j=0;j<sideSizeInSquares;j++)
        	{
        		nextBoard[i][j] = new TileImpl(Tile.NONDEFINI) ;
        	}
        }
        
    }


    private void packLine(int lineNumber) {
      /*
      * Scan the current board line looking for two consecutive tiles
      * with the same rank
      * When this case is encountered, write a single tile with rank+1
      * Otherwise just copy the tile (in practice packing it in the next board)
      * Remember that indices are 1-based in this code
      * Conversion to Java arrays indices is done in computeLineIndex and computeColumnIndex
      */
        int readIndex = 1; // Position of the tile to be read
        int writeIndex = 0; // Position of the last tile written

        while (readIndex <= sideSizeInSquares) {
            // Find next tile
            while ((readIndex <= sideSizeInSquares)
                    && (readTile(currentBoard, lineNumber, readIndex) == null)) {
                readIndex++;
            }
            if (readIndex > sideSizeInSquares) {
                break; // Done with the line
            }
            // Try to merge with previous tile
            if ((writeIndex > 0) &&
                    (readTile(nextBoard, lineNumber, writeIndex).getRank()
                            == readTile(currentBoard, lineNumber, readIndex).getRank())) {
                // Merge previously written tile and currently read one
                readTile(nextBoard, lineNumber, writeIndex).incrementRank();
            } else {
                // Advance write index and copy currently read tile
                writeIndex++;
                writeTile(nextBoard, readTile(currentBoard, lineNumber, readIndex), lineNumber, writeIndex);
            }
            // Done with the current tile read, move forward
            readIndex++;
        }


    }

    /**
     * Writes a tile into a matrix (board) using indices transformation
     * @param board       destination
     * @param tile        what to write at the given coordinates
     * @param lineIndex   coordinate
     * @param columnIndex  coordinate
     */
    private void writeTile(Tile[][] board, Tile tile, int lineIndex, int columnIndex) {
    	Tile tuile = board[computeLineIndex(lineIndex, columnIndex)][computeColumnIndex(lineIndex, columnIndex)] ;
    	tuile.setValeurTuile(tile);
    	/*
    	if(tuile != null)
    		tuile.setValeurTuile(tile);
    	else
    		tuile = tuileARemplir[computeLineIndex(lineIndex, columnIndex)][computeColumnIndex(lineIndex, columnIndex)];
    		tuile.setValeurTuile(tile);  	
    	*/
    }

    /**
     * Returns a tile  from a matrix (board) using indices transformation
     * @param board      origin
     * @param lineIndex   coordinate
     * @param columnIndex  coordinate
     * @return    tile at the given coordinates or null if no tile there
     */
    private Tile readTile(Tile[][] board, int lineIndex, int columnIndex) {
        int boardLineIndex = computeLineIndex(lineIndex, columnIndex);
        int boardColumnIndex = computeColumnIndex(lineIndex, columnIndex);
        Tile currentTile = board[boardLineIndex][boardColumnIndex];
        
        if(currentTile.getRank()==Tile.NONDEFINI)
        	return null ;
        
        return currentTile;
    }

    /**
     * Adds a level of indirection in the index computation
     * In practice provides a rotation/symmetry so that we need
     * to deal with one packing directionToPackInto only.
     * This operation also takes care of the conversion from (1..N) board
     * coordinates to the (0..N-1) Java array coordinates.
     *
     * NOTE: <b>NO CHECKS are made on parameter bounds.</b>
     *
     * @param lineIndex   must be in [1..sideSizeInSquares]
     * @param columnIndex must be in [1..sideSizeInSquares]
     * @return the columnIndex after rotation/symmetry
     */
    private int computeColumnIndex(int lineIndex, int columnIndex) {
        switch (directionToPackInto) {
            case RIGHT:
                return sideSizeInSquares - columnIndex;     //Symmetry on a vertical axis
            case LEFT:
                return columnIndex - 1;      //
            case TOP:
                return lineIndex - 1;
            case BOTTOM:
                return lineIndex - 1;
        }
        return 0; // NOT REACHED
    }

    /**
     * Adds a level of indirection in the index computation
     * In practice provides a rotation/symmetry so that we need
     * to deal with one packing directionToPackInto only.
     * This operation also takes care of the conversion from (1..N) board
     * coordinates to the (0..N-1) Java array coordinates.
     *
     * NOTE: <b>NO CHECKS are made on parameter bounds.</b>
     *
     * @param lineIndex   must be in [1..sideSizeInSquares]
     * @param columnIndex must be in [1..sideSizeInSquares]
     * @return the lineIndex after rotation/symmetry
     */
    private int computeLineIndex(int lineIndex, int columnIndex) {
        switch (directionToPackInto) {
            case LEFT:
                return lineIndex - 1;
            case RIGHT:
                return lineIndex - 1;
            case BOTTOM:
                return sideSizeInSquares - columnIndex;
            case TOP:
                return columnIndex - 1;
        }
        return 0; // NOT REACHED
    }

    /**
     * For testing purposes only.
     * Creates a board configuration using a matrix of ranks
     *
     * @param rankMatrix a non null matrix reference, must match board size
     */
    public void loadBoard(int[][] rankMatrix) {
        for (int i = 0; i < sideSizeInSquares; i++) {
            for (int j = 0; j < sideSizeInSquares; j++) {
                if (rankMatrix[i][j] > 0) {
                    nextBoard[i][j] = new TileImpl(rankMatrix[i][j]);
                }
            }
        }
    }

    /**
     * For testing purposes only.
     * Writes the ranks of contents of the matrix into a logger
     *
     * @param logger  where to write into
     * @param message the message to write first before writing the contents of the board
     */
    public void printBoard(Logger logger, String message) {

        logger.info(message);
        for (int i = 0; i < sideSizeInSquares; i++) {
            StringBuffer outputBuffer = new StringBuffer();
            outputBuffer.append(i + 1);
            outputBuffer.append(":{");
            for (int j = 0; j < sideSizeInSquares; j++) {
                if (currentBoard[i][j] != null) {
                    outputBuffer.append(currentBoard[i][j].getRank());
                } else {
                    outputBuffer.append("0");
                }
            }
            outputBuffer.append("}");
            logger.info(outputBuffer.toString());
        }
    }

	public List<IntegerProperty> getProprietes() {
		// TODO Auto-generated method stub
		List<IntegerProperty> listeProprietes  = new ArrayList<IntegerProperty>() ;
		for(int i=0;i<sideSizeInSquares;i++)
		{
			for(int j=0;j<sideSizeInSquares;j++)
			{
				
				IntegerProperty entier = null;
				/*
				if(currentBoard[i][j] == null)
				{
					Tile tuile = tuileARemplir[i][j] ;
					entier = tuile.getPropriete() ;
				}
				*/
				if(currentBoard[i][j] != null)
				{
					entier = currentBoard[i][j].getPropriete() ;
				}
				listeProprietes.add(entier) ;
			}
		}
		return listeProprietes ;
	}

	public int nombrePropriete() {
		return sideSizeInSquares*sideSizeInSquares ;
	}
    
	public void ajouterTuile()
	{
		int i = (int) (Math.floor(Math.random()*sideSizeInSquares*sideSizeInSquares) );
		System.out.println("nouvelleLigne:");
		Tile tuile ;
		int rang ; 
		boolean tuileVide = false ;
		do
		{
			int y = i/sideSizeInSquares ;
			int x = i%sideSizeInSquares ;
			tuile = nextBoard[y][x] ;
			
			if(tuile.getRank()==Tile.NONDEFINI)
			{
				tuileVide = true ;
				//tuile = tuileARemplir[y][x] ;
				System.out.println("incrementation");
				tuile.incrementRank();
			}
			//System.out.println("rank:"+tuile.getRank());
			//System.out.println("x:"+x+",y:"+y);
			i++ ;
		}while(!tuileVide && i<(sideSizeInSquares*sideSizeInSquares)) ;
	}

	public Etat verifierGagnePerdu() {
		Etat etatCourant = Etat.PERDU ;
		for(int i=0;i<sideSizeInSquares;i++)
		{
			for(int j=0;j<sideSizeInSquares;j++)
			{
				Tile tuile = currentBoard[i][j] ;
				if(tuile.getRank()==Tile.NONDEFINI)
				{
					etatCourant = Etat.GAGNE ;
				}
				else
				{
					int rang = tuile.getRank() ;
					if(rang>=9)
					{
						return Etat.GAGNE ;
					}
				}
				/*
				if(rang==0)
				{
					etatCourant = Etat.NONTERM ;
				}
				*/
			}
		}	
		
		Direction[] directions = Direction.values() ;
		for(int i=0;i<directions.length;i++)
		{
			packIntoDirection(directions[i]) ;
			if(!boardEquals())
			{
				reInit() ;
				return Etat.NONTERM ;
			}
		}
		
		return etatCourant; 
	}
	
	private boolean boardEquals() {
		for(int i=0;i<sideSizeInSquares;i++)
		{
			for(int j=0;j<sideSizeInSquares;j++)
			{
				if(!currentBoard[i][j].equals(nextBoard[i][j]))
				{
					return false ;
				}
			}
		}
		return true;
	}

	private void reInit()
	{
		nextBoard = new Tile[sideSizeInSquares][sideSizeInSquares];
        for(int i=0;i<sideSizeInSquares;i++)
        {
        	for(int j=0;j<sideSizeInSquares;j++)
        	{
        		nextBoard[i][j] = new TileImpl(Tile.NONDEFINI) ;
        	}
        }
	}
	
}