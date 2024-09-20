package com.javadruid.bluez.mediaplayer.lib;

import com.javadruid.bluez.mediaplayer.lib.interfaces.MediaPlayer1;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.freedesktop.dbus.DBusMap;
import org.freedesktop.dbus.ObjectPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.errors.Error;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.interfaces.DBusSigHandler;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.interfaces.Properties.PropertiesChanged;
import org.freedesktop.dbus.messages.Message;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaPlayer {

    // Method names
    static final String FAST_FORWARD = "FastForward";
    static final String HOLD = "Hold";
    static final String NEXT = "Next";
    static final String PAUSE = "Pause";
    static final String PLAY = "Play";
    static final String PRESS = "Press";
    static final String PREVIOUS = "Previous";
    static final String RELEASE = "Release";
    static final String REWIND = "Rewind";
    static final String STOP = "Stop";
    // Property names
    static final String BROWSABLE = "Browsable";
    static final String DEVICE = "Device";
    static final String EQUALIZER = "Equalizer";
    static final String NAME = "Name";
    static final String PLAYLIST = "Playlist";
    static final String POSITION = "Position";
    static final String REPEAT = "Repeat";
    static final String SCAN = "Scan";
    static final String SEARCHABLE = "Searchable";
    static final String SHUFFLE = "Shuffle";
    static final String STATUS = "Status";
    static final String SUBTYPE = "Subtype";
    static final String TRACK = "Track";
    static final String TYPE = "Type";

    private static final Logger logger = LoggerFactory.getLogger(MediaPlayer.class);

    private final DBusConnection connection;
    private final MediaPlayer1 remoteObject;
    private final Properties properties;

    private DBusSigHandler<PropertiesChanged> signalhandler;

    MediaPlayer(DBusConnection connection, String objectPath) throws DBusException {
        this.connection = connection;
        this.remoteObject = connection.getRemoteObject(MediaPlayer1.BUS_NAME, objectPath, MediaPlayer1.class);
        this.properties = connection.getRemoteObject(MediaPlayer1.BUS_NAME, objectPath, Properties.class);
    }

    // Methods
    public void fastForward() {
        callObjectMethod(FAST_FORWARD);
    }

    public void hold(byte avc_key) {
        callObjectMethod(HOLD, avc_key);
    }

    public void next() {
        callObjectMethod(NEXT);
    }

    public void pause() {
        callObjectMethod(PAUSE);
    }

    public void play() {
        callObjectMethod(PLAY);
    }

    public void press(byte avc_key) {
        callObjectMethod(PRESS, avc_key);
    }

    public void previous() {
        callObjectMethod(PREVIOUS);
    }

    public void release() {
        callObjectMethod(RELEASE);
    }

    public void rewind() {
        callObjectMethod(REWIND);
    }

    public void stop() {
        callObjectMethod(STOP);
    }

    // Properties
    public Optional<Boolean> isBrowsable() {
        return Optional.ofNullable((Boolean) getProperty(BROWSABLE));
    }

    public Optional<String> getDevice() {
        return Optional.ofNullable((ObjectPath) getProperty(DEVICE))
            .map(ObjectPath::getPath);
    }

    public Optional<String> getEqualizer() {
        return Optional.ofNullable((String) getProperty(EQUALIZER));
    }

    public void setEqualizer(String equalizer) {
        setProperty(EQUALIZER, equalizer);
    }

    public Optional<String> getName() {
        return Optional.ofNullable((String) getProperty(NAME));
    }

    public Optional<String> getPlaylist() {
        return Optional.ofNullable((ObjectPath) getProperty(PLAYLIST))
            .map(ObjectPath::getPath);
    }

    public Optional<Integer> getPosition() {
        return Optional.ofNullable((UInt32)getProperty(POSITION))
            .map(UInt32::intValue);
    }

    public Optional<String> getRepeat() {
        return Optional.ofNullable((String) getProperty(REPEAT));
    }

    public Optional<String> getScan() {
        return Optional.ofNullable((String) getProperty(SCAN));
    }

    public Optional<Boolean> isSearchable() {
        return Optional.ofNullable((Boolean) getProperty(SEARCHABLE));
    }

    public Optional<String> getShuffle() {
        return Optional.ofNullable((String) getProperty(SHUFFLE));
    }

    public Optional<String> getStatus() {
        return Optional.ofNullable((String) getProperty(STATUS));
    }

    public Optional<String> getSubtype() {
        return Optional.ofNullable((String) getProperty(SUBTYPE));
    }

    public Optional<Track> getTrack() {
        final DBusMap<String, Variant<?>> result = (DBusMap) getProperty(TRACK);
        return Optional.ofNullable(result)
            .map(r -> new Track(
                (String) getValue(r.get("Artist")),
                getInt(r.get("NumberOfTracks")),
                (String) getValue(r.get("Title")),
                (String) getValue(r.get("Album")),
                Duration.ofMillis(getLong(r.get("Duration"))),
                (String) getValue(r.get("Genre")),
                getInt(r.get("TrackNumber"))));
    }

    public Optional<String> getType() {
        return Optional.ofNullable((String) getProperty(TYPE));
    }

    // Listeners
    public void onPropertyChange(Consumer<Map.Entry<String, Object>> handler) throws DBusException {
        if(signalhandler != null)
            connection.removeSigHandler(PropertiesChanged.class, properties, signalhandler);
        signalhandler = s -> {
            s.getPropertiesChanged()
                .entrySet().stream()
                .map(MediaPlayer::toObjectEntry)
                .forEach(handler::accept);
            logger.info("signal received: {}", s);
        };
        connection.addSigHandler(PropertiesChanged.class, properties, signalhandler);
    }

    public void removePropertyChange() throws DBusException {
        if (signalhandler != null) {
            connection.removeSigHandler(PropertiesChanged.class, properties, signalhandler);
        }
    }

    @Override
    public String toString() {
        return getDevice().orElseGet(this::defaultName);
    }

    private static Map.Entry<String, Object> toObjectEntry(Map.Entry<String, Variant<?>> e) {
        return Map.entry(e.getKey(), e.getValue());
    }

    private static int getInt(Variant<?> r) {
        final Object result = getValue(r);
        return (result == null)? 0: ((UInt32) result).intValue();
    }

    private static long getLong(Variant<?> r) {
        final Object result = getValue(r);
        return (result == null)? 0: ((UInt32) result).longValue();
    }

    private static Object getValue(Variant<?> r) {
        return r != null? r.getValue(): null;
    }

    private String defaultName() {
        return "Player " + hashCode();
    }

    private void callObjectMethod(final String methodName, Object... parameters) {
        callMethod(remoteObject, methodName, parameters);
    }

    private Object getProperty(final String propertyName) {
        try {
            final Message property = callMethod(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, propertyName);
            if(property != null) {
                if(property instanceof Error error) {
                    logger.warn("Error whilst getting property", error.getException());
                    return null;
                } else {
                    final Variant result = (Variant) property.getParameters()[0];
                    if(result != null)
                        return result.getValue();
                }
            }
        } catch (DBusException ex) {
            logger.warn("Could not retrieve property {}", propertyName);
            logger.warn("Could not retrieve property", ex);
            return null;
        }
        return null;
    }

    private void setProperty(final String propertyName, Object value) {
        callMethod(properties, "Set", MediaPlayer1.DBUS_INTERFACE_NAME, propertyName, value);
    }

    private Message callMethod(final DBusInterface object, final String methodName, Object... parameters) {
        return connection.callMethodAsync(object, methodName, parameters)
            .getCall()
            .getReply();
    }

}
