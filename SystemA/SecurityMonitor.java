/******************************************************************************************************************
* File:ECSMonitor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class monitors the environmental control systems that control museum temperature and humidity. In addition to
* monitoring the temperature and humidity, the ECSMonitor also allows a user to set the humidity and temperature
* ranges to be maintained. If temperatures exceed those limits over/under alarm indicators are triggered.
*
* Parameters: IP address of the event manager (on command line). If blank, it is assumed that the event manager is
* on the local machine.
*
* Internal Methods:
*	static private void Heater(EventManagerInterface ei, boolean ON )
*	static private void Chiller(EventManagerInterface ei, boolean ON )
*	static private void Humidifier(EventManagerInterface ei, boolean ON )
*	static private void Dehumidifier(EventManagerInterface ei, boolean ON )
*
******************************************************************************************************************/
import EventPackage.*;
import java.util.*;

class SecurityMonitor extends Thread
{
	private EventManagerInterface em = null;// Interface object to the event manager
	private String EvtMgrIP = null;			// Event Manager IP address
	boolean armed = true;					// The system is armed
	boolean Registered = true;				// Signifies that this class is registered with an event manager.
	MessageWindow mw = null;				// This is the message window

	public SecurityMonitor()
	{
		// event manager is on the local system

		try
		{
			// Here we create an event manager interface object. This assumes
			// that the event manager is on the local machine

			em = new EventManagerInterface();

		}

		catch (Exception e)
		{
			System.out.println("SecurityMonitor::Error instantiating event manager interface: " + e);
			Registered = false;

		} // catch

	} //Constructor

	public SecurityMonitor( String EvmIpAddress )
	{
		// event manager is not on the local system

		EvtMgrIP = EvmIpAddress;

		try
		{
			// Here we create an event manager interface object. This assumes
			// that the event manager is NOT on the local machine

			em = new EventManagerInterface( EvtMgrIP );
		}

		catch (Exception e)
		{
			System.out.println("SecurityMonitor::Error instantiating event manager interface: " + e);
			Registered = false;

		} // catch

	} // Constructor

	public void run()
	{
		Event Evt0 = null;				// Event object
		EventQueue eq = null;			// Message Queue
		int EvtId = 0;					// User specified event ID
		Event evt = null;
		int	Delay = 1000;				// The loop delay (1 second)
		boolean Done = false;			// Loop termination flag

		if (em != null)
		{
			// Now we create the ECS status and message panel
			// Note that we set up two indicators that are initially yellow. This is
			// because we do not know if the temperature/humidity is high/low.
			// This panel is placed in the upper left hand corner and the status 
			// indicators are placed directly to the right, one on top of the other

			mw = new MessageWindow("SecurityMonitor Monitoring Console", 0, 0);
	
			mw.WriteMessage( "Registered with the event manager." );

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
				// Here we get our event queue from the event manager

				try
				{
					eq = em.GetEventQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting event queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for EventIDs = 1 or 2. Event IDs of 1 are temperature
				// readings from the temperature sensor; event IDs of 2 are humidity sensor
				// readings. Note that we get all the messages at once... there is a 1
				// second delay between samples,.. so the assumption is that there should
				// only be a message at most. If there are more, it is the last message
				// that will effect the status of the temperature and humidity controllers
				// as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Evt0 = eq.GetEvent();
					switch(Evt0.GetEventId())
					{
					
					case EventIDs.DOOR_ID : // door broken
					
						if(!armed)
							break;
						//We forward the event to the console
						evt = new Event( EventIDs.SECURITY_MONITOR_ID, Events.DOOR_BROKEN );

						// Here we send the event to the event manager.

						try
						{
							em.SendEvent( evt );
							//mw.WriteMessage( "Sent Humidity Event" );

						} // try

						catch (Exception e)
						{
							System.out.println( "Error Posting Door Break:: " + e );

						} // catch
					break;
					
					case EventIDs.WINDOW_ID : // window broken
						
						if(!armed)
							break;
						//We forward the event to the console
						evt = new Event( EventIDs.SECURITY_MONITOR_ID, Events.WINDOW_BROKEN );

						// Here we send the event to the event manager.

						try
						{
							em.SendEvent( evt );

						} // try

						catch (Exception e)
						{
							System.out.println( "Error Posting Window Break:: " + e );

						} // catch
					break;

					case EventIDs.MOTION_ID : // motion detected
					
						if(!armed)
							break;
						//We forward the event to the console
						evt = new Event( EventIDs.SECURITY_MONITOR_ID, Events.MOTION_DETECTED );

						// Here we send the event to the event manager.

						try
						{
							em.SendEvent( evt );

						} // try

						catch (Exception e)
						{
							System.out.println( "Error Posting Motion Detection:: " + e );
						} // catch
					break;
					
					case EventIDs.FIRE_ID : // fire detected
					
						if(!armed)
							break;
						//We forward the event to the console
						evt = new Event( EventIDs.SECURITY_MONITOR_ID, Events.FIRE_ALARM );

						// Here we send the event to the event manager.

						try
						{
							em.SendEvent( evt );

						} // try

						catch (Exception e)
						{
							System.out.println( "Error Posting Fire Alarm:: " + e );
						} // catch
					break;
				
					case EventIDs.CONSOLE_ID :
					
						if(Evt0.GetMessage().equals(Events.ARM_SYSTEM))
							armed = true;
						else if(Evt0.GetMessage().equals(Events.DISARM_SYSTEM))
							armed = false;
						else if(Evt0.GetMessage().equals(Events.SPRINKLER_OFF))
						{

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( new Event( EventIDs.SECURITY_MONITOR_ID, Events.SPRINKLER_OFF ));
							} // try

							catch (Exception e)
							{
								System.out.println( "Error Posting Sprinkler Turn Off:: " + e );
							} // catch
						}
					break;
					}

				} // for

				

				// This delay slows down the sample rate to Delay milliseconds

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

	/***************************************************************************
	* CONCRETE METHOD:: IsRegistered
	* Purpose: This method returns the registered status
	*
	* Arguments: none
	*
	* Returns: boolean true if registered, false if not registered
	*
	* Exceptions: None
	*
	***************************************************************************/

	public boolean IsRegistered()
	{
		return( Registered );

	} // SetTemperatureRange

	/***************************************************************************
	* CONCRETE METHOD:: Halt
	* Purpose: This method posts an event that stops the environmental control
	*		   system.
	*
	* Arguments: none
	*
	* Returns: none
	*
	* Exceptions: Posting to event manager exception
	*
	***************************************************************************/

	public void Halt()
	{
		mw.WriteMessage( "***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***" );

		// Here we create the stop event.

		Event evt;

		evt = new Event( (int) 99, "XXX" );

		// Here we send the event to the event manager.

		try
		{
			em.SendEvent( evt );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending halt message:: " + e);

		} // catch

	} // Halt

	/***************************************************************************
	* CONCRETE METHOD:: Heater
	* Purpose: This method posts events that will signal the temperature
	*		   controller to turn on/off the heater
	*
	* Arguments: boolean ON(true)/OFF(false) - indicates whether to turn the
	*			 heater on or off.
	*
	* Returns: none
	*
	* Exceptions: Posting to event manager exception
	*
	***************************************************************************/

} // ECSMonitor