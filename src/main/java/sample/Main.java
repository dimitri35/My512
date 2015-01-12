package sample;

import javafx.application.Application;

import javafx.fxml.FXML ;
import javafx.scene.layout.GridPane ;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	/*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        */
    	//Chargement du FXML
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
    	Parent root = (Parent)loader.load();
    	//obtention du controleur
    	Controller controller = loader.getController();
    	
    	//titre de la fenêtre
        primaryStage.setTitle("My 512");
        //Ajout d'une scène
        Scene scene = new Scene(root, 300, 275) ;
        //Ajout d'un écouteur d'évènement clic dans la scène
        scene.setOnKeyPressed(controller.keyEventHandler);
        //On ajoute la scène au primary stage
        primaryStage.setScene(scene);
        //On affiche le primary stage
        primaryStage.show() ;
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
