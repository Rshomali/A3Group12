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
import InstrumentationPackage.*;
import EventPackage.*;
import java.util.*;

class MMonitor extends Thread
{
	private EventManagerInterface em = null;// Interface object to the event manager
	private String EvtMgrIP = null;			// Event Manager IP address
	boolean Registered = true;				// Signifies that this class is registered with an event manager.
	MessageWindow mw = null;				// This is the message window
	Map<String, Indicator> ComponentStatus = new HashMap<String, Indicator>();
	Map<String, Long> HeartBeattime = new HashMap<String, Long>();

	public MMonitor()
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
			System.out.println("MMonitor::Error instantiating event manager interface: " + e);
			Registered = false;

		} // catch

	} //Constructor

	public MMonitor( String EvmIpAddress )
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
			System.out.println("MMonitor::Error instantiating event manager interface: " + e);
			Registered = false;

		} // catch

	} // Constructor

	public void run()
	{
		Event Evt = null;				// Event object
		EventQueue eq = null;			// Message Queue
		int EvtId = 0;					// User specified event ID
		boolean Done = false;
		int	Delay = 5*1000;				// The loop delay (5 second)


		if (em != null)
		{
			// Now we create the ECS status and message panel
			// Note that we set up two indicators that are initially yellow. This is
			// because we do not know if the temperature/humidity is high/low.
			// This panel is placed in the upper left hand corner and the status 
			// indicators are placed directly to the right, one on top of the other

			mw = new MessageWindow("Maintenance Monitoring Console", 0, 0);


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
			
			
			mw.WriteMessage( "Initializing Components: \n" );
			//ComponentStatus = new Indicator[MySystem.COMPONENTS.size()];
			int component_counter = 0;
				
			for ( String key : MySystem.COMPONENTS.keySet() )
			{
				mw.WriteMessage( "Initializing " + key + ": Status Unknown"  );
				ComponentStatus.put(key, new Indicator (key.substring(0, (key.length() >11) ? 11: key.length()), mw.GetX()+ mw.Width() + (int)((component_counter%4)*150), mw.GetY() + ((int)(component_counter/4))*(150), 2));
				component_counter++;
				//ComponentStatus[component_counter++] = new Indicator (key.substring(0, (key.length() >11) ? 11: key.length()), mw.GetX()+ mw.Width() + (int)((component_counter%4)*150), mw.GetY() + ((int)(component_counter/4))*(150), 2);
				 														   //(mw.GetY() + mw.Height()) - (int)((component_counter%4)*150), 2 );
			}
			

			Calendar TimeStamp = Calendar.getInstance();
			
			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			while ( !Done )
			{
				// This delay slows down the sample rate to Delay milliseconds

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					System.out.println( "Sleep error:: " + e );

				} // catch
				
				// Here we get our event queue from the event manager

				try
				{
					eq = em.GetEventQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting event queue::" + e );

				} // catch


				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Evt = eq.GetEvent();
					
					if (Evt.GetMessage().equalsIgnoreCase(Events.ALIVE))
					{
						for ( String key : MySystem.COMPONENTS.keySet() )
						{
							if ((int)(MySystem.COMPONENTS.get(key)) == Evt.GetEventId())
							{
								((Indicator)(ComponentStatus.get(key))).SetLampColor(1);
								mw.WriteMessage( ">>" + key + ": Status ALIVE"  );
								HeartBeattime.put(key, TimeStamp.getTimeInMillis());
							}
							
						}						
					}

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


					} // if

				} // for

				long current_time = TimeStamp.getTimeInMillis();
				
				for ( String key : MySystem.COMPONENTS.keySet() )
				{
					if(HeartBeattime.get(key) == null)
					{
						mw.WriteMessage( ">>" + key + ": Status DOWN"  );
						((Indicator)(ComponentStatus.get(key))).SetLampColor(3);
					}
					else
					{
						long last_hb = (long) HeartBeattime.get(key);
						long interval = current_time - last_hb;
						if( interval > (5*1000))
						{
							mw.WriteMessage( ">>" + key + ": Status DOWN"  );
						}
						((Indicator)(ComponentStatus.get(key))).SetLampColor(3);						
					}
				}
				

			} // while

		} else {

		//	System.out.println("Unable to register with the event manager.\n\n" );

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

	} 


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



} // ECSMonitor