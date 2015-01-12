package model.test;

import model.Board.Direction;
import model.Board.Etat;
import model.BoardImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author dimitri
 *
 */
public class BoardImplTest2 {

	  BoardImpl testBoard;
	  final int defaultBoardSize = 3;

	  @Before
	  public void setUp() throws Exception {
	       testBoard = new BoardImpl(defaultBoardSize);
	  }

	  @Test
	  public void testNonTerminePlein()
	  {
		  testBoard.loadBoard(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}) ;
		  testBoard.commit(); 
		  Assert.assertEquals(testBoard.verifierGagnePerdu(),Etat.NONTERM);
	  }
	  
	  @Test
	  public void testPerduPlein()
	  {
		  testBoard.loadBoard(new int[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}}) ;
		  testBoard.commit();
		  Assert.assertEquals(testBoard.verifierGagnePerdu(),Etat.PERDU);
	  }
	  
	  @Test
	  public void testGagnePlein()
	  {
		  testBoard.loadBoard(new int[][]{{1, 2, 9}, {2, 3, 4}, {3, 4, 5}}) ;
		  testBoard.commit();
		  Assert.assertEquals(testBoard.verifierGagnePerdu(),Etat.GAGNE);
	  }
	  
}
