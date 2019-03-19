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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class EditController {	
	
	@FXML Button CONFIRM;
	@FXML Button CANCEL;
	
	@FXML TextField name;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	
	private ObservableList<Song> songList;
	private Stage stage;
	private Song songToEdit;

	public void start(Stage mainStage, ObservableList<Song> obsList, Song songToEdit) {                
		stage = mainStage;
		songList = obsList;
		this.songToEdit = songToEdit;
		//fill in text fields with the song to be edited info
		name.setText(songToEdit.getName());
		artist.setText(songToEdit.getArtist());
		album.setText(songToEdit.getAlbum());
		year.setText(songToEdit.getYear());
	}
	
	//------------------------------------------------------------------------------------------------------------------
	
	public void confirm(ActionEvent e){
		Button a = (Button)e.getSource();
		if(a == CONFIRM){
			//get new info to make the new song
			String newName = name.getText();
			String newArtist = artist.getText();
			String newAlbum = album.getText();
			String newYear = year.getText();
			
			if((!newName.isEmpty()) && (!newArtist.isEmpty())){ //required name and artist fields are filled in
				songList.remove(songToEdit);
				Song newSong = new Song(newName, newArtist);
				
				if(newAlbum != null)
					if(!newAlbum.isEmpty())
						newSong.setAlbum(newAlbum);
				
				if(newYear != null)
					if(!newYear.isEmpty())
						newSong.setYear(newYear);
				
				if(!songList.contains(newSong)) { //song not in library, so it is added and sorted
					songList.add(newSong);
					Collections.sort(songList);
					returnToListScene(newSong);
				} else {
					//let user know that the song is already in the library, puts old song back into the list and the process starts again
					songList.add(songToEdit);
					Collections.sort(songList);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Error Editing Song");
					alert.setHeaderText("Duplicate song entry");
					alert.setContentText("The song you are trying to edit, " + newName + " by " + newArtist + ", is already in the song library.");
					alert.showAndWait();
				}
			} else {
				//show error that name and artist are required
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error Editing Song");
				alert.setHeaderText("Missing required fields");
				alert.setContentText("The name and artist fields are required.");
				alert.showAndWait();
			}
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------
	
	public void cancel(ActionEvent e) { //cancel edit of the song, send back song selected before action to select and display
		Button a = (Button)e.getSource();
		if(a == CANCEL) {			
			returnToListScene(songToEdit);
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	private void returnToListScene(Song editedSong) { // return to the song library, passing in the new song or previous song before event
		try {
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/application/SongLib.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			SongLibController listController = loader.getController();
			listController.start(stage, songList, editedSong);
			Scene scene = new Scene(root, 700, 400);
			stage.setScene(scene);
			stage.show();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
