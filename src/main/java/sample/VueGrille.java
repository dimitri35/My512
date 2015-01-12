package sample;

import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;

import model.Board;
import javafx.scene.layout.GridPane ;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint ;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.beans.property.* ;
import javafx.util.StringConverter ;

public class VueGrille implements IVueGrille{
	
	private int tailleX, tailleY ;
	private GridPane grille ;
	private Rectangle[][] rectangle ;
	private StackPane[][] stacks ;
	private Text[][] texts ;
	
	private Board modele ;
	
	public VueGrille(GridPane grille,Board modele) {
		this.grille = grille ;
		this.modele = modele ;
	}

	public void initialiser(int tailleX, int tailleY) {
		//Ensemble des cases du jeu 
		rectangle = new Rectangle[tailleX][tailleY] ;
		stacks = new StackPane[tailleX][tailleY] ;
		//textes dans les cases avec les valeurs
		texts = new Text[tailleX][tailleY] ;
		
		this.tailleX = tailleX ;
		this.tailleY = tailleY ;
		
		//Pour chaque case 
		for(int i=0;i<tailleX;i++)
		{
			for(int j=0;j<tailleY;j++)
			{
				//On crée l'ensemble des cases avec le texte
				StackPane stackPane = new StackPane() ;
				stacks[i][j] = stackPane ;
				Text text = new Text("0") ;	
				texts[i][j] = text ;
				
				//Rectangles d'arrière plan
				Rectangle rect = new Rectangle() ;
				rect.setFill(Color.BLUE) ;
				rect.setWidth(50);
				rect.setHeight(50);
				rect.setArcWidth(20);
				rect.setArcHeight(20);
				rectangle[i][j] = rect ;
				
				//Ajout du rectangle et du texte
				stackPane.getChildren().addAll(rect,text) ;		
				
				//Ajout de la case à la grille
				grille.add(stackPane, i,j);
			}
		}
		bind() ;
	}
	
	private void bind() 
	{
		//On récupère l'ensemble des propriétés du modèle
		List<IntegerProperty> listePropriete = modele.getProprietes() ;
		//Pour chaque case 
		for(int i=0;i<texts.length;i++)
		{
			for(int j=0;j<texts.length;j++)
			{
				//On tranforme la position de la case en indice dans une liste
				int index = j*modele.getSideSizeInSquares() + i ;
				//Si il reste des propriétés 
				if(modele.nombrePropriete()>index)
				{
					//On applique un binding bidirectionnelle entre la la propriété qui est dans la tuile et le texte
					StringConverter converter = new NombreConverter() ;
					texts[i][j].textProperty().bindBidirectional(listePropriete.get(index), converter); 
				}
			}
		}
	}
}
