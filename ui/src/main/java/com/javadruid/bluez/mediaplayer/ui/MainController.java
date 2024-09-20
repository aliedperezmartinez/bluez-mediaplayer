package com.javadruid.bluez.mediaplayer.ui;

import com.javadruid.bluez.mediaplayer.lib.MediaPlayer;
import com.javadruid.bluez.mediaplayer.lib.MediaPlayerManager;
import java.io.Closeable;
import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class MainController implements Closeable {

    private final MediaPlayerManager mediaPlayerManager = new MediaPlayerManager();

    public ListView players;
    public CheckBox browsable;
    public CheckBox searchable;
    public TextField device;
    public TextField equaliser;
    public TextField name;
    public TextField playlist;
    public TextField position;
    public TextField repeat;
    public TextField scan;
    public TextField shuffle;
    public TextField status;
    public TextField subtype;
    public TextField track;
    public TextField type;

    public void initialize() {
        final MultipleSelectionModel selectionModel = players.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel.selectedItemProperty().addListener(this::changed);
        final ObservableList listItems = players.getItems();
        mediaPlayerManager.getMediaPlayers().forEach(listItems::add);
        if (!listItems.isEmpty()) {
            selectionModel.selectFirst();
            update((MediaPlayer) selectionModel.getSelectedItem());
        }
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        players.getSelectionModel()
            .selectedItemProperty()
            .removeListener(this::changed);
        players.getItems().clear();
        initialize();
    }

    @FXML
    public void onPrevious(ActionEvent event) {
        final MediaPlayer currentPlayer = getCurrentPlayer();
        if(currentPlayer != null) currentPlayer.previous();
    }

    @FXML
    public void onPlay(ActionEvent event) {
        MediaPlayer currentPlayer = getCurrentPlayer();
        if(currentPlayer != null) currentPlayer.play();
    }

    @FXML
    public void onPause(ActionEvent event) {
        MediaPlayer currentPlayer = getCurrentPlayer();
        if(currentPlayer != null) currentPlayer.pause();
    }

    @FXML
    public void onNext(ActionEvent event) {
        MediaPlayer currentPlayer = getCurrentPlayer();
        if(currentPlayer != null) currentPlayer.next();
    }

    @Override
    public void close() throws IOException {
        mediaPlayerManager.close();
    }

    private void update(MediaPlayer p) {
        browsable.setSelected(p.isBrowsable().orElse(Boolean.FALSE));
        searchable.setSelected(p.isSearchable().orElse(Boolean.FALSE));
        device.setText(p.getDevice().orElse(""));
        equaliser.setText(p.getEqualizer().orElse(""));
        name.setText(p.getName().orElse(""));
        playlist.setText(p.getPlaylist().orElse(""));
        position.setText(p.getPosition().map(Integer::toUnsignedString).orElse(""));
        repeat.setText(p.getRepeat().orElse(""));
        scan.setText(p.getScan().orElse(""));
        shuffle.setText(p.getShuffle().orElse(""));
        status.setText(p.getStatus().orElse(""));
        subtype.setText(p.getSubtype().orElse(""));
        track.setText(p.getTrack().map(Object::toString).orElse("<no track>"));
        type.setText(p.getType().orElse(""));
    }

    private MediaPlayer getCurrentPlayer() {
        return (MediaPlayer) players.getSelectionModel().getSelectedItem();
    }

    private void changed(ObservableValue observable, Object oldValue, Object newValue) {
        final MediaPlayer player = (MediaPlayer) newValue;
        if (player != null) {
            player.onPropertyChange(e -> {
                update(player);
            });
            update(player);
        }
        final MediaPlayer oldPlayer = (MediaPlayer) oldValue;
        if (oldPlayer != null) {
            oldPlayer.removePropertyChange();
        }
    }

}
