package application;


/**
 * 
 * @author Nick Fasullo (nrf17)
 *
 */

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;


public class SongLibController {	
	
	@FXML Button ADD;
	@FXML Button REMOVE;
	@FXML Button EDIT;
	
	@FXML TextArea songInfo;
	
	@FXML ListView<Song> listView;                
	
	private ObservableList<Song> songList;
	private Stage stage;

	public void start(Stage mainStage, ObservableList<Song> obsList, Song selectedSong) {               
		stage = mainStage;
		songList = obsList;       
		listView.setItems(songList);
		Collections.sort(songList);
		
		//for pre-select on start of empty or imported library, if imported library, select and display 1st song
		listView.getSelectionModel().select(0);
		Song comp = listView.getSelectionModel().getSelectedItem();
		if(comp != null){
			String name = listView.getSelectionModel().getSelectedItem().getName();
			String artist = listView.getSelectionModel().getSelectedItem().getArtist();
			String album = listView.getSelectionModel().getSelectedItem().getAlbum();
			if(album == null){ album = ""; }
			String year = listView.getSelectionModel().getSelectedItem().getYear();
			if(year == null){ year = ""; }
			String display = "Name: " + name + " | Artist: " + artist + " | Album: " + album + " | Year: " + year;
			songInfo.setText(display);
		}
		
		//for add and edit, if successful select and display the song, if action canceled/unsuccessful, select song that was previously selected
		if(selectedSong != null){
			listView.getSelectionModel().select(selectedSong);
			String name = listView.getSelectionModel().getSelectedItem().getName();
			String artist = listView.getSelectionModel().getSelectedItem().getArtist();
			String album = listView.getSelectionModel().getSelectedItem().getAlbum();
			if(album == null){ album = ""; }
			String year = listView.getSelectionModel().getSelectedItem().getYear();
			if(year == null){ year = ""; }
			String display = "Name: " + name + " | Artist: " + artist + " | Album: " + album + " | Year: " + year;
			songInfo.setText(display);
		}
		
		//for click, if song is selected, it information is displayed
		listView.setOnMouseClicked((MouseEvent e) -> {
			String name = listView.getSelectionModel().getSelectedItem().getName();
			String artist = listView.getSelectionModel().getSelectedItem().getArtist();
			String album = listView.getSelectionModel().getSelectedItem().getAlbum();
			if(album == null){ album = ""; }
			String year = listView.getSelectionModel().getSelectedItem().getYear();
			if(year == null){ year = ""; }
			String display = "Name: " + name + " | Artist: " + artist + " | Album: " + album + " | Year: " + year;
			songInfo.setText(display);
		});
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public void addSong(ActionEvent e){ //add button pushed, enters scene to continue this action
		//if add action is canceled, need the song selected before action to select and display again on return to library
		Song prevSong = listView.getSelectionModel().getSelectedItem();
		Button a = (Button)e.getSource();
		if(a == ADD){
			//switch to Add scene
			try {
				FXMLLoader loader = new FXMLLoader();   
				loader.setLocation(getClass().getResource("/application/Add.fxml"));
				AnchorPane root = (AnchorPane)loader.load();
				AddController addController = loader.getController();
				addController.start(stage, songList, prevSong);
				Scene scene = new Scene(root, 700, 400);
				stage.setScene(scene);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------------------
	
	public void deleteSong(ActionEvent e) { // delete button pushed, brings pop-up to confirm/cancel action, if no song is selected displays error
		Button a = (Button)e.getSource();
		if(a == REMOVE){	
			//get the song to be deleted, including its position in the list
			Song songToDelete = listView.getSelectionModel().getSelectedItem();
			int indx = listView.getSelectionModel().getSelectedIndex();
			
			if(songToDelete != null) { //a proper song that can be deleted is selected
				//allow the user to confirm or back out of deletion action
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm Deletion");
				alert.setHeaderText("Remove song from library?");
				alert.setContentText("Are you sure you want to remove " + songToDelete.getName() + " by " + songToDelete.getArtist() + "?");
				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.get() == ButtonType.OK){ //user confirms they wish to delete the song
					songList.remove(songToDelete);
					Collections.sort(songList);
					listView.getSelectionModel().select(indx);
					Song nextSong = listView.getSelectionModel().getSelectedItem();
					if(nextSong == null){ songInfo.clear(); } //no more songs left in the list, display no song info
					else { // select and display for the song after deleted one if not the one before it is displayed
						String name = listView.getSelectionModel().getSelectedItem().getName();
						String artist = listView.getSelectionModel().getSelectedItem().getArtist();
						String album = listView.getSelectionModel().getSelectedItem().getAlbum();
						if(album == null){ album = ""; }
						String year = listView.getSelectionModel().getSelectedItem().getYear();
						if(year == null){ year = ""; }
						String display = "Name: " + name + " | Artist: " + artist + " | Album: " + album + " | Year: " + year;
						songInfo.setText(display);
					}
				} //ends ok button
			} //ends song != null
			
			//song == null, no song selected to delete, inform the user
			else{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Deletion Error");
				alert.setHeaderText("No song selected to delete.");
				alert.setContentText("You must select a song to delete, if no songs exist in the library you can't use this action.");
				alert.showAndWait();
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------
	
	
	
	
	//--------------------------------------------------------------------------------------------------------------------
	
	
	public void editSong(ActionEvent e) { // edit button pushed, enters scene to continue this action, if no song is selected displays error
		Button a = (Button)e.getSource();
		if(a == EDIT){
			//switch to Edit scene
			try {
				FXMLLoader loader = new FXMLLoader();   
				loader.setLocation(getClass().getResource("/application/Edit.fxml"));
				AnchorPane root = (AnchorPane)loader.load();
				Song songToEdit = (Song) listView.getSelectionModel().getSelectedItem();
				if(songToEdit != null) {
					EditController editController = loader.getController();
					editController.start(stage, songList, songToEdit);
					Scene scene = new Scene(root, 700, 400);
					stage.setScene(scene);
				} else{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Edit Error");
					alert.setHeaderText("No song selected to edit.");
					alert.setContentText("You must select a song to edit, if no songs exist in the library you can't use this action.");
					alert.showAndWait();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
