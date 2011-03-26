import InstrumentationPackage.*;
import EventPackage.*;

import java.util.*;

class MotionSensor{
		
	static int sensorID = 8;
	static int CONSOLE_ID = 17;
	
	public static void main(String args[])
	{
		String EvtMgrIP;					// Event Manager IP address
		Event Evt = null;					// Event object
		EventQueue eq = null;				// Message Queue
		int EvtId = 0;						// User specified event ID
		EventManagerInterface em = null;	// Interface object to the event manager
		
		int MotionDetectedState = 0;			// DoorBroken State: 0 == no broken, 1 == broken.
		
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
 		
 		// Here we check to see if registration worked. If ef is null then the
		// event manager interface was not properly created.

		if (em != null)
		{
			// We create a message window. Note that we place this panel about 1/2 across 
			// and 2/3s down the screen
			
			float WinPosX = 0.5f; 	//This is the X position of the message window in terms 
									//of a percentage of the screen height
			float WinPosY = 0.60f;	//This is the Y position of the message window in terms 
								 	//of a percentage of the screen height 
			
			MessageWindow mw = new MessageWindow("Motion Sensor", WinPosX, WinPosY);

			mw.WriteMessage("Registered with the event manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				mw.WriteMessage("Error:: " + e);

			} // catch
	    	
	    	
	    	mw.WriteMessage("\nInitializing MotionDection Simulation in MotionSensor::" );

	    	
	    	/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			mw.WriteMessage("Beginning Simulation... ");

			Date previousTime = new Date();
			Date nowTime;
			//Date temp;
			while ( !Done )
			{
				// Post the current DOOR BROKEN STATUS

				//PostMotionDetected( em, MotionDetectedState );

				//mw.WriteMessage("Current Motion Detection Status:: " + MotionDetectedState + "%");

				// Get the message queue

				try
				{
					eq = em.GetEventQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting event queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for EventIDs = -4, this means the the humidify or
				// dehumidifier has been turned on/off. Note that we get all the messages
				// from the queue at once... there is a 2.5 second delay between samples,..
				// so the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output of the humidity as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Evt = eq.GetEvent();

					if ( Evt.GetEventId() == CONSOLE_ID )
					{
						if (Evt.GetMessage().equalsIgnoreCase("SMD")) // humidifier on
						{
							MotionDetectedState = 1;//It might be unnecessary, 
							PostMotionDetected( em , "MD");
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

				    	mw.WriteMessage("\n\nSimulation Stopped. \n");

					} // if

				} // for
				
				nowTime = new Date();
				long diff = nowTime.getSeconds()-previousTime.getSeconds();
				if(diff>=5){
					PostAliveSignal(em, "A"); 
					previousTime = nowTime;
				}
			}
		}
	}			
			
	static private void PostMotionDetected(EventManagerInterface ei, String event)
	{
		// Here we create the event.

		Event evt = new Event( sensorID, event );

		// Here we send the event to the event manager.

		try
		{
			ei.SendEvent( evt );
			//mw.WriteMessage( "Sent Humidity Event" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error happens during sending this"+ event + "and" + e );

		} // catch

	} // PostHumidity
	static private void PostAliveSignal(EventManagerInterface ei, String event)
	{
		// Here we create the event.

		Event evt = new Event( sensorID, event );

		// Here we send the event to the event manager.

		try
		{
			ei.SendEvent( evt );
			//mw.WriteMessage( "Sent Humidity Event" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error happens during sending this"+ event + "and" + e );

		} // catch

	} // PostHumidity
			
			
}

		