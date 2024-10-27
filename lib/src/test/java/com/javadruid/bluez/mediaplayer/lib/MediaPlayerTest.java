package com.javadruid.bluez.mediaplayer.lib;

import com.javadruid.bluez.mediaplayer.lib.interfaces.MediaPlayer1;
import java.time.Duration;
import java.util.Optional;
import org.freedesktop.dbus.DBusAsyncReply;
import org.freedesktop.dbus.DBusMap;
import org.freedesktop.dbus.ObjectPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.errors.Error;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusSigHandler;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.interfaces.Properties.PropertiesChanged;
import org.freedesktop.dbus.messages.Message;
import org.freedesktop.dbus.messages.MethodCall;
import org.freedesktop.dbus.types.DBusMapType;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaPlayerTest {

    private static final String path = "path";

    @Mock
    private DBusConnection connection;
    @Mock
    private MediaPlayer1 remoteObject;
    @Mock
    private Properties properties;
    @Mock
    private DBusAsyncReply reply;
    @Mock
    private MethodCall call;
    @Mock
    private Message message;

    @Test
    public void testConstructor() throws DBusException {
        new MediaPlayer(connection, path);

        verify(connection).getRemoteObject(MediaPlayer1.BUS_NAME, path, MediaPlayer1.class);
        verify(connection).getRemoteObject(MediaPlayer1.BUS_NAME, path, Properties.class);
    }

    @Test
    public void testFastForward() throws DBusException {
        mockObjectCall(MediaPlayer.FAST_FORWARD);

        final MediaPlayer instance = newTestInstance();
        instance.fastForward();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.FAST_FORWARD);
    }

    @Test
    public void testHold() throws DBusException {
        when(connection.callMethodAsync(remoteObject, MediaPlayer.HOLD, (byte)0))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);

        final MediaPlayer instance = newTestInstance();
        instance.hold((byte)0);

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.HOLD, (byte)0);
    }

    @Test
    public void testNext() throws DBusException {
        mockObjectCall(MediaPlayer.NEXT);

        final MediaPlayer instance = newTestInstance();
        instance.next();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.NEXT);
    }

    @Test
    public void testPause() throws DBusException {
        mockObjectCall(MediaPlayer.PAUSE);

        final MediaPlayer instance = newTestInstance();
        instance.pause();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.PAUSE);
    }

    @Test
    public void testPlay() throws DBusException {
        mockObjectCall(MediaPlayer.PLAY);

        final MediaPlayer instance = newTestInstance();
        instance.play();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.PLAY);
    }

    @Test
    public void testPress() throws DBusException {
        when(connection.callMethodAsync(remoteObject, MediaPlayer.PRESS, (byte)0))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);

        final MediaPlayer instance = newTestInstance();
        instance.press((byte)0);

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.PRESS, (byte)0);
    }

    @Test
    public void testPrevious() throws DBusException {
        mockObjectCall(MediaPlayer.PREVIOUS);

        final MediaPlayer instance = newTestInstance();
        instance.previous();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.PREVIOUS);
    }

    @Test
    public void testRelease() throws DBusException {
        mockObjectCall(MediaPlayer.RELEASE);

        final MediaPlayer instance = newTestInstance();
        instance.release();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.RELEASE);
    }

    @Test
    public void testRewind() throws DBusException {
        mockObjectCall(MediaPlayer.REWIND);

        final MediaPlayer instance = newTestInstance();
        instance.rewind();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.REWIND);
    }

    @Test
    public void testStop() throws DBusException {
        mockObjectCall(MediaPlayer.STOP);

        final MediaPlayer instance = newTestInstance();
        instance.stop();

        verify(connection).callMethodAsync(remoteObject, MediaPlayer.STOP);
    }

    @Test
    public void testIsBrowsable() throws DBusException {
        mockProperty(MediaPlayer.BROWSABLE, Boolean.TRUE);

        final MediaPlayer instance = newTestInstance();
        final Optional<Boolean> result = instance.isBrowsable();

        assertTrue(result.get());
    }

    @Test
    public void testGetDevice() throws IllegalArgumentException, DBusException {
        mockProperty(MediaPlayer.DEVICE, new ObjectPath("source", path));

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getDevice();

        assertEquals(path, result.get());
    }

    @Test
    public void testGetEqualizer() throws IllegalArgumentException, DBusException {
        final String equalizer = "equalizer";
        mockProperty(MediaPlayer.EQUALIZER, equalizer);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getEqualizer();

        assertEquals(equalizer, result.get());
    }

    @Test
    public void testSetEqualizer() throws DBusException {
        final String equalizer = "equalizer";
        when(connection.callMethodAsync(properties, "Set", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.EQUALIZER, equalizer))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);

        final MediaPlayer instance = newTestInstance();
        instance.setEqualizer(equalizer);

        verify(connection).callMethodAsync(properties, "Set", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.EQUALIZER, equalizer);
    }

    @Test
    public void testGetName() throws IllegalArgumentException, DBusException {
        final String name = "name";
        mockProperty(MediaPlayer.NAME, name);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getName();

        assertEquals(name, result.get());
    }

    @Test
    public void testGetPlaylist() throws IllegalArgumentException, DBusException {
        final String playlist = "playlist";
        mockProperty(MediaPlayer.PLAYLIST, new ObjectPath("source", playlist));

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getPlaylist();

        assertEquals(playlist, result.get());
    }

    @Test
    public void testGetPosition() throws IllegalArgumentException, DBusException {
        final int position = 42;
        mockProperty(MediaPlayer.POSITION, new UInt32(position));

        final MediaPlayer instance = newTestInstance();
        final Optional<Integer> result = instance.getPosition();

        assertEquals(position, result.get());
    }

    @Test
    public void testGetRepeat() throws IllegalArgumentException, DBusException {
        final String repeat = "repeat";
        mockProperty(MediaPlayer.REPEAT, repeat);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getRepeat();

        assertEquals(repeat, result.get());
    }

    @Test
    public void testGetScan() throws IllegalArgumentException, DBusException {
        final String scan = "scan";
        mockProperty(MediaPlayer.SCAN, scan);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getScan();

        assertEquals(scan, result.get());
    }

    @Test
    public void testIsSearchable() throws IllegalArgumentException, DBusException {
        mockProperty(MediaPlayer.SEARCHABLE, Boolean.TRUE);

        final MediaPlayer instance = newTestInstance();
        final Optional<Boolean> result = instance.isSearchable();

        assertEquals(Boolean.TRUE, result.get());
    }

    @Test
    public void testGetShuffle() throws IllegalArgumentException, DBusException {
        final String shuffle = "shuffle";
        mockProperty(MediaPlayer.SHUFFLE, shuffle);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getShuffle();

        assertEquals(shuffle, result.get());
    }

    @Test
    public void testGetStatus() throws IllegalArgumentException, DBusException {
        final String status = "status";
        mockProperty(MediaPlayer.STATUS, status);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getStatus();

        assertEquals(status, result.get());
    }

    @Test
    public void testGetSubtype() throws IllegalArgumentException, DBusException {
        final String subtype = "subtype";
        mockProperty(MediaPlayer.SUBTYPE, subtype);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getSubtype();

        assertEquals(subtype, result.get());
    }

    @Test
    public void testGetTrack() throws IllegalArgumentException, DBusException {
        final String artist = "artist";
        final int numberOfTracks = 42;
        final String title = "title";
        final String album = "album";
        final Duration duration = Duration.ofMinutes(3);
        final String genre = "rock";
        final int trackNumber = 1;

        final DBusMap<String, Variant<?>> track = new DBusMap<>(
            new Object[][]{
                {"Artist", new Variant<>(artist)},
                {"NumberOfTracks", new Variant<>(new UInt32(numberOfTracks))},
                {"Title", new Variant<>(title)},
                {"Album", new Variant<>(album)},
                {"Duration", new Variant<>(new UInt32(duration.toMillis()))},
                {"Genre", new Variant<>(genre)},
                {"TrackNumber", new Variant<>(new UInt32(trackNumber))}
            });
        when(connection.callMethodAsync(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.TRACK))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);
        when(call.getReply()).thenReturn(message);
        when(message.getParameters()).thenReturn(new Object[]{new Variant<>(track, new DBusMapType(String.class, Variant.class))});
        final Track expectedResult = new Track(artist, numberOfTracks, title, album, duration, genre, trackNumber);

        final MediaPlayer instance = newTestInstance();
        final Optional<Track> result = instance.getTrack();

        assertEquals(expectedResult, result.get());
    }

    @Test
    public void testGetType() throws IllegalArgumentException, DBusException {
        final String type = "type";
        mockProperty(MediaPlayer.TYPE, type);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getType();

        assertEquals(type, result.get());
    }

    @Test
    public void testToString() throws IllegalArgumentException, DBusException {
        mockProperty(MediaPlayer.DEVICE, new ObjectPath("source", path));

        final MediaPlayer instance = newTestInstance();
        final String result = instance.toString();

        assertEquals(path, result);
    }

    @Test
    public void testToStringDefault() throws IllegalArgumentException, DBusException {
        when(connection.callMethodAsync(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.DEVICE))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);

        final MediaPlayer instance = newTestInstance();
        final String result = instance.toString();

        assertEquals("Player " + instance.hashCode(), result);
    }

    @Test
    public void testOnPropertyChange() throws Exception {
        final MediaPlayer instance = newTestInstance();
        instance.onPropertyChange(System.out::println);

        verify(connection).addSigHandler(same(PropertiesChanged.class), eq(properties), any(DBusSigHandler.class));
        verify(connection, never()).removeSigHandler(same(PropertiesChanged.class), eq(properties), any(DBusSigHandler.class));
    }

    @Test
    public void testRemovePropertyChange() throws Exception {
        InOrder inOrder = inOrder(connection);
        final MediaPlayer instance = newTestInstance();
        instance.onPropertyChange(System.out::println);
        instance.removePropertyChange();

        inOrder.verify(connection).addSigHandler(same(PropertiesChanged.class), eq(properties), any(DBusSigHandler.class));
        inOrder.verify(connection).removeSigHandler(same(PropertiesChanged.class), eq(properties), any(DBusSigHandler.class));
    }

    @Test
    public void testRemovePropertyChangeExistingListener() throws Exception {
        InOrder inOrder = inOrder(connection);
        final MediaPlayer instance = newTestInstance();
        instance.onPropertyChange(System.out::println);
        instance.onPropertyChange(System.out::println);

        inOrder.verify(connection).removeSigHandler(same(PropertiesChanged.class), eq(properties), any(DBusSigHandler.class));
        inOrder.verify(connection).addSigHandler(same(PropertiesChanged.class), eq(properties), any(DBusSigHandler.class));
    }

    @Test
    public void testGetPropertyError() throws IllegalArgumentException, DBusException {
        when(connection.callMethodAsync(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.TYPE))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);
        when(call.getReply()).thenReturn(new Error());

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getType();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPropertyParametersException() throws IllegalArgumentException, DBusException {
        when(connection.callMethodAsync(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.TYPE))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);
        when(call.getReply()).thenReturn(message);
        when(message.getParameters()).thenThrow(DBusException.class);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getType();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPropertyNoProperty() throws IllegalArgumentException, DBusException {
        when(connection.callMethodAsync(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, MediaPlayer.TYPE))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);

        final MediaPlayer instance = newTestInstance();
        final Optional<String> result = instance.getType();

        assertTrue(result.isEmpty());
    }

    private MediaPlayer newTestInstance() throws DBusException {
        when(connection.getRemoteObject(MediaPlayer1.BUS_NAME, path, MediaPlayer1.class))
            .thenReturn(remoteObject);
        when(connection.getRemoteObject(MediaPlayer1.BUS_NAME, path, Properties.class))
            .thenReturn(properties);
        return new MediaPlayer(connection, path);
    }

    private void mockObjectCall(final String methodName) {
        when(connection.callMethodAsync(remoteObject, methodName))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);
    }

    private void mockProperty(final String propertyName, final Object result) throws IllegalArgumentException, DBusException {
        when(connection.callMethodAsync(properties, "Get", MediaPlayer1.DBUS_INTERFACE_NAME, propertyName))
            .thenReturn(reply);
        when(reply.getCall()).thenReturn(call);
        when(call.getReply()).thenReturn(message);
        when(message.getParameters()).thenReturn(new Object[]{new Variant<>(result)});
    }

}
