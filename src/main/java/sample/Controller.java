package sample;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import model.Board;
import model.Board.Direction;
import model.Board.Etat;
import model.BoardImpl;
import model.Tile;
import javafx.event.EventHandler;
import javafx.fxml.FXML ;
import javafx.scene.layout.GridPane ;
import javafx.scene.control.Button ;
import javafx.fxml.Initializable ;
import javafx.scene.* ;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint ;
import javafx.scene.paint.Color;
import javafx.scene.control.Label ;
import javafx.beans.property.Property ;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode ;

public class Controller implements Initializable  {
	
	@FXML private GridPane grille ;
	private Rectangle[][] rectangle ;
	private Board board ;

	private Etat etat = Etat.NONTERM ;
	private final int tailleDuCarre = 3;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//création d'un board de 4*4 (modèle)
		board = new BoardImpl(tailleDuCarre) ;

		/*
		int i = (int) (Math.floor(Math.random()*16) );
		int y = i/4 ;
		int x = i%4 ;
		Tile tuile = board.getTile(y+1, x+1) ;
		tuile.incrementRank();
		*/
		
		//Initialisation de la première case
		board.ajouterTuile();
		board.commit();
		BoardImpl boardTest = (BoardImpl) board ;
		boardTest.printBoard(Logger.global, "bonjour");
		//boardTest.loadBoard(rankMatrix);
		//boardTest.loadBoard(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}) ;
		//boardTest.loadBoard(new int[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}}) ;
		//boardTest.loadBoard(new int[][]{{1, 1, 1}, {1, 9, 1}, {1, 1, 1}}) ;
		//boardTest.commit();
		/*
		rectangle = new Rectangle[4][4] ;
		
		rect.setFill(Color.BLUE) ;
		rect.setWidth(50);
		rect.setHeight(50);
		rect.setArcWidth(20);
		rect.setArcHeight(20);
		*/
		//grille.getChildren().add(rect) ;
		//grille.setOnKeyReleased(keyEventHandler);

		//création de la vue 
		VueGrille vueGrille = new VueGrille(grille,board) ;
		vueGrille.initialiser(tailleDuCarre,tailleDuCarre);
	}
	
	//permet de déplacer la grille dans une direction
	private void deplacerDirection(Direction direction)
	{
		
		if(etat==Etat.GAGNE|| etat==Etat.PERDU)
			return ;
		
		BoardImpl  board2 = (BoardImpl) board ;
		board2.printBoard(Logger.getGlobal(),"test");
		board.packIntoDirection(direction);
		board.ajouterTuile();
		board.commit(); 
		
		Etat etatCourant = board.verifierGagnePerdu() ;
		
		switch(etatCourant)
		{
			case NONTERM :
				board.ajouterTuile(); 
			break ;
			case  GAGNE :
				Dialogue.afficherGagne();
			break;
			
			case PERDU :
				Dialogue.afficherPerdu();
			break ;
		}
		this.etat = etatCourant ;
		
		
		
	}
	
	final EventHandler<KeyEvent> keyEventHandler =
            new EventHandler<KeyEvent>() {
                public void handle(final KeyEvent keyEvent) {
                	KeyCode code = keyEvent.getCode() ;
                	//évenement clavier bouton bas
                	if(code== KeyCode.DOWN)
                	{
                		deplacerDirection(Direction.BOTTOM) ;
                	}
                	//évènement clavier bouton haut
                	else if(code == KeyCode.UP)
                	{
                		deplacerDirection(Direction.TOP) ;
                	}
                	//évènement clavier bouton gauche
                	else if(code == KeyCode.LEFT)
                	{
                		deplacerDirection(Direction.LEFT) ;
                	}
                	//évènement clavier bouton droit
                	else if(code==KeyCode.RIGHT)
                	{
                		deplacerDirection(Direction.RIGHT);
                	}
                	/*
                    if (keyEvent.getCode() == KeyCode.) {
                        //setPressed(keyEvent.getEventType()== KeyEvent.KEY_PRESSED);
                        
                        keyEvent.consume();
                    }
                    */
                }
        };
	
}
