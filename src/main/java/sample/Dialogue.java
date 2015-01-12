package sample;

import javafx.stage.Stage;
import javafx.stage.Modality ;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.geometry.Insets ;
import javafx.scene.layout.VBoxBuilder ;
import javafx.scene.control.Button ;
import javafx.geometry.Pos ;

public class Dialogue {

	private static final String textePerdu = "Vous avez perdu" ;
	private static final String texteGagne = "Vous avez gagné" ;
	
	static void afficherDialogue(String texte){
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setScene(new Scene(VBoxBuilder.create().
		    children(new Text(texte), new Button("Ok.")).
		    alignment(Pos.CENTER).padding(new Insets(5)).build()));
		dialogStage.show();
	}
	
	static void afficherPerdu(){
		afficherDialogue(textePerdu);
	}
	
	static void afficherGagne(){
		afficherDialogue(texteGagne);
	}
}
