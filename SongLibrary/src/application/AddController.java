package application;

/**
 * 
 * @author Nick Fasullo (nrf17)
 *
 */

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class AddController {	
	@FXML Button CONFIRM;
	@FXML Button CANCEL;
	
	@FXML TextField name;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	
	private Stage stage;
	private ObservableList<Song> songList;
	private Song prev;

	public void start(Stage mainStage, ObservableList<Song> obsList, Song prevSong) {                
		stage = mainStage;
		songList = obsList;
		prev = prevSong;
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	public void confirm(ActionEvent e){ //attempts to add song into library, will not do so if song is duplicate or has improper input fields
		Button a = (Button)e.getSource();
		if(a == CONFIRM){
			String newName = name.getText();
			String newArtist = artist.getText();
			String newAlbum = album.getText();
			String newYear = year.getText();
			
			if((!newName.isEmpty()) && (!newArtist.isEmpty())){ // required name and artist fields are filled in
				Song newSong = new Song(newName, newArtist);
				
				if(!newAlbum.isEmpty())
					newSong.setAlbum(newAlbum);
				
				if(!newYear.isEmpty())
					newSong.setYear(newYear);
				
				if(!songList.contains(newSong)) { //song not in library, so it is added and sorted
					songList.add(newSong);
					Collections.sort(songList);
					returnToListScene(newSong);
				} else {
					//let user know that the song is already in the library
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Error Adding Song");
					alert.setHeaderText("Duplicate song entry");
					alert.setContentText("The song you are trying to add, " + newName + " by " + newArtist + ", is already in the song library.");
					alert.showAndWait();
				}
				
			} else { 
				//show error that name and artist are required
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error Adding Song");
				alert.setHeaderText("Missing required fields");
				alert.setContentText("The name and artist fields are required.");
				alert.showAndWait();
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------
	
	public void cancel(ActionEvent e) { // cancel adding a new song, send back song selected before action to select and display
		Button a = (Button)e.getSource();
		if(a == CANCEL) {
			returnToListScene(prev);
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------
	
	private void returnToListScene(Song newSong) { // return to the song library, passing in the new song or previous song before event
		try {
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/application/SongLib.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			SongLibController listController = loader.getController();
			listController.start(stage, songList, newSong);
			Scene scene = new Scene(root, 700, 400);
			stage.setScene(scene);
			stage.show();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
