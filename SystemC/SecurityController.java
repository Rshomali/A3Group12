/******************************************************************************************************************
* File:SecurityController.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2011 Rami Shomali Carnegie Mellon University
* Versions:
*	1.0 March 2011 initial 
*
* Description:
*
* This class simulates a device that controls a door, window, and motion alram sensor. It polls the event manager for event
* ids = 4 and reacts to them by ..... The following command are valid
* strings for controlling the humidifier and dehumidifier:
*
*	DB  = door alarm on
*	DNB = door alarm off
*	WB  = window alaram on
*	WNB = window alaram off
*   MD  = motion alaram on
*   MND = motion alaram off
*
*
* Parameters: IP address of the event manager (on command line). If blank, it is assumed that the event manager is
* on the local machine.
*
* Internal Methods:
*	static private void ConfirmMessage(EventManagerInterface ei, String m )
*
******************************************************************************************************************/
import EventPackage.*;
import java.util.*;
import InstrumentationPackage.*;

class SecurityController
{
	public static void main(String args[])
	{
		String EvtMgrIP;					// Event Manager IP address
		Event Evt = null;					// Event object
		EventQueue eq = null;				// Message Queue
		int EvtId = 0;						// User specified event ID
		EventManagerInterface em = null;	// Interface object to the event manager
		boolean DoorState = false;	
		boolean WindowState = false;
		boolean MotionState = false;
		int	Delay = 2500;					// The loop delay (2.5 seconds)
		boolean Done = false;				// Loop termination flag

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the event manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length == 0 )
 		{
			// event manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine..." );

			try
			{
				// Here we create an event manager interface object. This assumes
				// that the event manager is on the local machine

				em = new EventManagerInterface();
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating event manager interface: " + e);

			} // catch

		} else {

			// event manager is not on the local system

			EvtMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + EvtMgrIP );

			try
			{
				// Here we create an event manager interface object. This assumes
				// that the event manager is NOT on the local machine

				em = new EventManagerInterface( EvtMgrIP );
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating event manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If em is null then the
		// event manager interface was not properly created.

		if (em != null)
		{
			System.out.println("Registered with the event manager." );

			/* Now we create the humidity control status and message panel
			** We put this panel about 2/3s the way down the terminal, aligned to the left
			** of the terminal. The status indicators are placed directly under this panel
			*/

			float WinPosX = 0.0f; 	//This is the X position of the message window in terms 
									//of a percentage of the screen height
			float WinPosY = 0.30f;	//This is the Y position of the message window in terms 
								 	//of a percentage of the screen height 
			
			MessageWindow mw = new MessageWindow("Security Controller Status Console", WinPosX, WinPosY);

			// Now we put the indicators directly under the humitity status and control panel
						
			mw.WriteMessage("Registered with the event manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				System.out.println("Error:: " + e);

			} // catch

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			while ( !Done )
			{
				try
				{
					eq = em.GetEventQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting event queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for EventIDs = DOOR_ID, WINDOW_ID, and MOTION_ID, 
				// at once... there is a 2.5 second delay between samples,.. so
				// the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output.
				
				DoorState = false;	
				WindowState = false;
				MotionState = false;

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Evt = eq.GetEvent();

					if ( Evt.GetEventId() == EventIDs.ALARM_ID )
					{

						if (Evt.GetMessage().equalsIgnoreCase(Events.DOOR_BROKEN)) 
						{
							DoorState = true;
							mw.WriteMessage("Received "+ Events.DOOR_BROKEN +" event" );


						} // if

						if (Evt.GetMessage().equalsIgnoreCase(Events.WINDOW_BROKEN)) 
						{
							WindowState = true;
							mw.WriteMessage("Received "+ Events.WINDOW_BROKEN +" event" );


						} // if

						if (Evt.GetMessage().equalsIgnoreCase(Events.MOTION_DETECTED)) 
						{
							MotionState = true;
							mw.WriteMessage("Received " +Events.MOTION_DETECTED+ " event" );

						} // if

					} // if

					// If the event ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the event manager.

					if ( Evt.GetEventId() == 99 )
					{
						Done = true;

						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage( "\n\nSimulation Stopped. \n");

						// Get rid of the indicators. The message panel is left for the
						// user to exit so they can see the last message posted.

					} // if

				} // for

				// Update status
				
				// Here we create the event.

				Event evt = null;
				
				if (DoorState)
				{
					// Send event to monitor
					evt = new Event( EventIDs.ALARM_ID, Events.DOOR_BROKEN );
				

				} else {

					// 
					

				} // if
				
				if (WindowState)
				{
					// Send event to monitor
					evt = new Event( EventIDs.ALARM_ID, Events.WINDOW_BROKEN );
				

				} else {

					// 
					

				} // if

				if (MotionState)
				{
					// Send event to monitor
					evt = new Event( EventIDs.ALARM_ID, Events.MOTION_DETECTED );

				} else {

					// 

				

				} // if
				
				try
				{
					em.SendEvent( evt );

				} // try

				catch (Exception e)
				{
					System.out.println("Error sending event:: " + e);

				} // catch

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					System.out.println( "Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the event manager.\n\n" );

		} // if

	} // main

} // SecurityControllers