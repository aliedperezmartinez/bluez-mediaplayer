package com.javadruid.bluez.mediaplayer.lib;

import com.javadruid.bluez.mediaplayer.lib.interfaces.MediaPlayer1;
import java.util.List;
import org.freedesktop.dbus.DBusAsyncReply;
import org.freedesktop.dbus.DBusMap;
import org.freedesktop.dbus.ObjectPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.ObjectManager;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.messages.Message;
import org.freedesktop.dbus.messages.MethodCall;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaPlayerManagerTest {

    @Mock
    private DBusConnection connection;
    @Mock
    private ObjectManager objectManager;

    @Test
    public void testGetMediaPlayers() throws DBusException {
        when(connection.getRemoteObject(MediaPlayer1.BUS_NAME, "/", ObjectManager.class))
            .thenReturn(objectManager);
        final DBusAsyncReply reply = Mockito.mock(DBusAsyncReply.class);
        when(connection.callMethodAsync(objectManager, "GetManagedObjects"))
            .thenReturn(reply);
        final MethodCall call = Mockito.mock(MethodCall.class);
        when(reply.getCall()).thenReturn(call);
        final Message message = Mockito.mock(Message.class);
        when(call.getReply()).thenReturn(message);
        final String path_01 = "path_01";
        final String path_02 = "path_02";
        when(message.getParameters()).thenReturn(new DBusMap[]{
            new DBusMap(new Object[][]{}),
            new DBusMap(new Object[][]{
                {
                    new ObjectPath("source", path_01), new DBusMap(new Object[][]{
                        {MediaPlayer1.DBUS_INTERFACE_NAME},
                        {new Object()}
                    })
                },
                {
                    new ObjectPath("source", path_02), new DBusMap(new Object[][]{
                        {MediaPlayer1.DBUS_INTERFACE_NAME},
                        {new Object()}
                    })
                }
            })
        });

        final MediaPlayerManager instance = new MediaPlayerManager(connection);
        final List<MediaPlayer> result = instance.getMediaPlayers().toList();

        assertEquals(2, result.size());
        verify(connection).getRemoteObject(MediaPlayer1.BUS_NAME, path_01, MediaPlayer1.class);
        verify(connection).getRemoteObject(MediaPlayer1.BUS_NAME, path_01, Properties.class);
        verify(connection).getRemoteObject(MediaPlayer1.BUS_NAME, path_02, MediaPlayer1.class);
        verify(connection).getRemoteObject(MediaPlayer1.BUS_NAME, path_02, Properties.class);
    }

    @Test
    public void testClose() throws Exception {
        when(connection.getRemoteObject(MediaPlayer1.BUS_NAME, "/", ObjectManager.class))
            .thenReturn(objectManager);
        final MediaPlayerManager instance = new MediaPlayerManager(connection);

        instance.close();

        verify(connection).close();
    }


    @Test
    public void testConstructorDBusException() throws DBusException {
        when(connection.getRemoteObject(MediaPlayer1.BUS_NAME, "/", ObjectManager.class))
            .thenThrow(DBusException.class);

        assertThrows(RuntimeException.class, () -> new MediaPlayerManager(connection));
    }

    @Test
    public void testGetMediaPlayersDBusException() throws DBusException {
        when(connection.getRemoteObject(MediaPlayer1.BUS_NAME, "/", ObjectManager.class))
            .thenReturn(objectManager);
        final DBusAsyncReply reply = Mockito.mock(DBusAsyncReply.class);
        when(connection.callMethodAsync(objectManager, "GetManagedObjects"))
            .thenReturn(reply);
        final MethodCall call = Mockito.mock(MethodCall.class);
        when(reply.getCall()).thenReturn(call);
        final Message message = Mockito.mock(Message.class);
        when(call.getReply()).thenReturn(message);
        when(message.getParameters()).thenThrow(DBusException.class);

        final MediaPlayerManager instance = new MediaPlayerManager(connection);
        assertThrows(RuntimeException.class, () -> instance.getMediaPlayers());
    }

}
