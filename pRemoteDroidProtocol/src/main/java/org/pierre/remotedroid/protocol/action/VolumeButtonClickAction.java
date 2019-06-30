package org.pierre.remotedroid.protocol.action;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VolumeButtonClickAction extends PRemoteDroidAction
{
	public static final byte BUTTON_NONE = 0;
	public static final byte BUTTON_UP = 1;
	public static final byte BUTTON_DOWN = 2;
	
	public byte volumebutton;
	public boolean state;
	public int command;

	public VolumeButtonClickAction(byte volumebutton, boolean state)
	{
		// TODO Auto-generated constructor stub
		this.volumebutton = volumebutton;
		this.state = state;

		processCommand();
	}



	public static VolumeButtonClickAction parse(DataInputStream dis) throws IOException
	{
		// TODO Auto-generated constructor stub
		byte button = dis.readByte();
		boolean state = dis.readBoolean();

		return new VolumeButtonClickAction(button,state);
		
	}
	
	public void toDataOutputStream(DataOutputStream dos) throws IOException
	{
		dos.writeByte(VOLUME_BUTTON);
		dos.writeByte(this.volumebutton);
		dos.writeBoolean(this.state);


	}

	private void processCommand()
	{
		try
		{
			Robot robot = new Robot();
			switch (command)
			{
				case BUTTON_UP:
					robot.keyPress(KeyEvent.VK_RIGHT);
					System.out.println("Right");
					break;
				case BUTTON_DOWN:
					robot.keyPress(KeyEvent.VK_LEFT);
					System.out.println("Left");
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
