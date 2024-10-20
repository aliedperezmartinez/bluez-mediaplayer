package com.javadruid.bluez.mediaplayer.lib;

import com.javadruid.bluez.mediaplayer.lib.interfaces.MediaPlayer1;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.freedesktop.dbus.DBusMap;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.ObjectPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.ObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaPlayerManager implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(MediaPlayerManager.class);

    private final DBusConnection conn;
    private final ObjectManager remoteObject;

    public MediaPlayerManager() {
        this(getConnection());
    }

    MediaPlayerManager(DBusConnection conn) {
        try {
            this.conn = conn;
            remoteObject = conn.getRemoteObject(MediaPlayer1.BUS_NAME, "/", ObjectManager.class);
        } catch (DBusException ex) {
            logger.error("Error initialising Media Player Manager", ex);
            throw new RuntimeException(ex);
        }
    }

    public Stream<MediaPlayer> getMediaPlayers() {
        try {
            return getManagedObjects()
                .map(o -> (DBusMap<ObjectPath, DBusMap>)o)
                .map(DBusMap::entrySet)
                .flatMap(Set::stream)
                .filter(MediaPlayerManager::hasMediaPlayer)
                .map(Map.Entry::getKey)
                .map(DBusPath::getPath)
                .map(this::mediaPlayer)
                .filter(Objects::nonNull);
        } catch (DBusException ex) {
            logger.error("Error retrieving objects", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        conn.close();
    }

    private static DBusConnection getConnection() {
        try {
            return DBusConnection.getConnection(DBusConnection.DBusBusType.SYSTEM);
        } catch (DBusException ex) {
            logger.error("Error creating connection", ex);
            throw new RuntimeException(ex);
        }
    }

    private Stream<Object> getManagedObjects() throws DBusException {
    return Arrays.stream(
        conn.callMethodAsync(remoteObject, "GetManagedObjects")
            .getCall()
            .getReply()
            .getParameters());
    }

    private static boolean hasMediaPlayer(Map.Entry<ObjectPath, DBusMap> e) {
        return e.getValue().containsKey(MediaPlayer1.DBUS_INTERFACE_NAME);
    }

    private MediaPlayer mediaPlayer(String path) {
        try {
            return new MediaPlayer(conn, path);
        } catch (DBusException ex) {
            return null;
        }
    }

}
