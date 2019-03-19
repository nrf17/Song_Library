package application;
	
/**
 * 
 * @author Nick Fasullo (nrf17)
 *
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import application.SongLibController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import application.SongLibController;


public class SongLib extends Application {
	
	private ObservableList<Song> songList;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/application/SongLib.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			songList = FXCollections.observableArrayList();
			
			//read from text file and populate list
			File inputFile = new File("songdata.txt");
			if(inputFile.canRead()) {
				
				//scanner and delimiter
				Scanner input = new Scanner(inputFile);
				input.useDelimiter(",|\n");
				
				while(input.hasNext()) { //goes through the text file, breaking up the song info, saving it, and then inserting into the library
					String newName = input.next();
					String newArtist = input.next();
					Song newSong = new Song(newName, newArtist);
					String newAlbum = input.next();
					String newYear = input.next();
					
					if(!newAlbum.contains("##No"))
						 newSong.setAlbum(newAlbum);
					
					if(!newYear.contains("##No"))
						 newSong.setYear(newYear);
					
					songList.add(newSong);
				}
				
				input.close();
			}
			
			//loads into song library
			SongLibController listController = loader.getController();
			listController.start(primaryStage, songList, null);
			Scene scene = new Scene(root, 700, 400);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Song Library");
			primaryStage.setResizable(false);  
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public void stop(){ //upon exit of the program, the library will be saved and stored in a text file to load upon next start up
		try {
			PrintWriter out = new PrintWriter("songdata.txt");
			for(int i = 0; i < songList.size(); i++) {
				Song currentSong = songList.get(i);
				out.print(currentSong.getName() + ",");
				out.print(currentSong.getArtist() + ",");
				
				if(currentSong.getAlbum() == null)
					out.print("##No Album##,");
				else
					out.print(currentSong.getAlbum() + ",");
				
				if(currentSong.getYear() == null)
					out.print("##No Year##");
				else
					out.print(currentSong.getYear());
				
				out.println();
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		launch(args);
	}
}
