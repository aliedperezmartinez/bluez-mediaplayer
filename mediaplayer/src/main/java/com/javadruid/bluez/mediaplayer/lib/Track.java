package com.javadruid.bluez.mediaplayer.lib;

import java.time.Duration;

public record Track (
    String artist,
    int numberOfTracks,
    String title,
    String album,
    Duration duration,
    String genre,
    int trackNumber
) {

    @Override
    public String toString() {
        return "Artist: " + artist +
            ", Title: " + title +
            ", Album: " + album +
            ", TrackNumber: " + trackNumber +
            ", NumberOfTracks: " + numberOfTracks +
            ", Duration: " + durationToString() +
            ", Genre: " + genre;
    }

    private String durationToString() {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

}
