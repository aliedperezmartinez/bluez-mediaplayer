package com.javadruid.bluez.mediaplayer.lib.interfaces;

import java.util.Map;
import org.freedesktop.dbus.ObjectPath;
import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.interfaces.DBusInterface;

@DBusInterfaceName(MediaPlayer1.DBUS_INTERFACE_NAME)
public interface MediaPlayer1 extends DBusInterface {

    static final String DBUS_INTERFACE_NAME = "org.bluez.MediaPlayer1";
    static final String BUS_NAME = "org.bluez";

    // Methods
    void FastForward();

    void Hold(byte avc_key);

    void Next();

    void Pause();

    void Play();

    void Press(byte avc_key);

    void Previous();

    void Release();

    void Rewind();

    void Stop();

    // Properties
    boolean isBrowsable();

    ObjectPath getDevice();

    String getEqualizer();

    void setEqualizer(String equalizer);

    String getName();

    ObjectPath getPlaylist();

    int getPosition();

    String getRepeat();

    String getScan();

    boolean isSearchable();

    String getShuffle();

    String getStatus();

    String getSubtype();

    Map<String, Object> getTrack();

    String getType();
}
