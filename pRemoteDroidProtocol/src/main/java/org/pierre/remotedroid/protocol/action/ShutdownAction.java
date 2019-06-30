package org.pierre.remotedroid.protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Sourav Adhikary on 24-02-2016.
 */
public class ShutdownAction extends PRemoteDroidAction {

    public static final byte SHUTDOWN = 0;
    public static final byte REBOOT = 1;
    public static final byte HIBERNATE = 2;

    public byte button;
    public ShutdownAction(byte button){
        this.button = button;

    }
    public static ShutdownAction parse(DataInputStream dis)throws IOException{
        byte button = dis.readByte();
        return new ShutdownAction(button);
    }
    @Override
    public void toDataOutputStream(DataOutputStream dos) throws IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");

        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        }
        else if ("Windows".equals(operatingSystem)) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }
        dos.writeByte(SHUTDOWN);
        dos.writeByte(this.button);
        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }
}
