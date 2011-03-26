/******************************************************************************************************************
* File:ECSConsole.java
* Course: 17655
* Project: Assignment 3
*
* Description: This class is the console for the museum environmental control system. This process consists of two
* threads. The ECSMonitor object is a thread that is started that is responsible for the monitoring and control of
* the museum environmental systems. The main thread provides a text interface for the user to change the temperature
* and humidity ranges, as well as shut down the system.
******************************************************************************************************************/
import java.util.Timer;
import java.util.TimerTask;
import TermioPackage.*;
import EventPackage.*;

import EventPackage.*;

public class ECSConsole
{
	private static Timer timer;
	private static EventManagerInterface em = null;// Interface object to the event manager
	private static Event evt = null;					// Event object
	private String EvtMgrIP = null;			// Event Manager IP address
	boolean Registered = true;				// Signifies that this class is registered with an event manager.
	
	public ECSConsole()
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
	}
	
	public ECSConsole( String EvmIpAddress )
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
	
	
	public static void main(String args[])
	{
		
    	Termio UserInput = new Termio();	// Termio IO Object
		boolean Done = false;				// Main loop flag
		boolean fireAlarm = false;
		boolean sprinklerOn = false;
		String Option = null;				// Menu choice from user
		
		@SuppressWarnings("unused")
		
		Event Evt0 = null;				// Event object
		EventQueue eq = null;			// Message Queue
		boolean Error = false;				// Error flag
		SecurityMonitor Monitor = null;			// The environmental control system monitor
		ECSConsole console = null;
		
		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the event manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length != 0 )
 		{
			// event manager is not on the local system
			Monitor = new SecurityMonitor( args[0] );
			console = new ECSConsole(args[0]);
		} else {
			Monitor = new SecurityMonitor();
			console = new ECSConsole();
		} // if

		// Here we check to see if registration worked. If ef is null then the
		// event manager interface was not properly created.
		if (Monitor.IsRegistered()&& em != null )
		{
			Monitor.start(); // Here we start the monitoring and control thread

			while (!Done)
			{   // Here, the main thread continues and provides the main menu
				System.out.println( "\n\n\n\n" );
				System.out.println( "Environmental Control System (ECS) Command Console: \n" );
				if (args.length != 0)
					System.out.println( "Using event manger at: " + args[0] + "\n" );
				else
					System.out.println( "Using local event manger \n" );

				System.out.println( "Allow a guard to arm or disarm the system" );
				System.out.println( "Select an Option: \n" );
				System.out.println( "1: Simulate door broken? (yes/no)" );
				System.out.println( "2: Simulate window broken? (yes/no)" );
				System.out.println( "3: Simulate motion detection? (yes/no)" );
				System.out.println( "4: Simulate fire alarm? (yes/no)" );
				System.out.println( "5: Arm the system? (yes/no)" );
				System.out.println( "6: Disarm the system? (yes/no)" );
				
				try
				{
					eq = em.GetEventQueue();
				} // try
				catch( Exception e )
				{
					System.out.println("Error getting event queue::" + e );
				} // catch
				int qlen = eq.GetSize();
				for ( int i = 0; i < qlen; i++ )
				{
					Evt0 = eq.GetEvent();
					if(Evt0.GetEventId()==EventIDs.FIRE_ID&&Evt0.GetMessage().equals(Events.FIRE_ALARM))
					{
						System.out.println( "7: Turn on sprinkler? (yes/no)" );
						timer = new Timer();
						timer.schedule(new TimerTestTask(), 15*1000);
						fireAlarm=true;
					}
					if(Evt0.GetEventId()==EventIDs.SPRINKLER_ID&&Evt0.GetMessage().equals(Events.SPRINKLER_ON))
					{
						System.out.println( "8: Turn off sprinkler? (yes/no)" );
						sprinklerOn=true;
					}
				}// for
				
				System.out.println( "X: Stop System\n" );
				System.out.print( "\n>>>> " );
				Option = UserInput.KeyboardReadString();

				
				//////////// option 1 ////////////
				if ( Option.equals( "1" ) )
				{
					// Here we get the temperature ranges
					Error = true;
					while (Error)
					{	
						System.out.print( "\nSimulate door broken? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.SIMULATE_DOOR_BROKEN );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Simulate Door Break:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						else
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if

				//////////// option 2 ////////////
				if ( Option.equals( "2" ) )
				{
					// Here we get the temperature ranges
					Error = true;
					while (Error)
					{	
						System.out.print( "\nSimulate window broken? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.SIMULATE_WINDOW_BROKEN );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Simulate Window Break:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						else
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if

				
	//////////// option 3 ////////////
				if ( Option.equals( "3" ) )
				{
					// Here we get the temperature ranges
					Error = true;
					while (Error)
					{	
						System.out.print( "\nSimulate motion dection? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.SIMULATE_MOTION_DETECTED );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Simulate Motion Detection:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						else
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if
				
				
	//////////// option 4 ////////////
				if ( Option.equals( "4" ) )
				{
					Error = true;
					while (Error)
					{	
						System.out.print( "\nSimulate fire alarm? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.SIMULATE_FIRE_ALARM );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Simulate Fire Alarm:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						else
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if
				
				
	//////////// option 5 ////////////
				if ( Option.equals( "5" ) )
				{
					Error = true;
					while (Error)
					{	
						System.out.print( "\nArm the system? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.ARM_SYSTEM );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Arm System:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						else
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if
				
				
	//////////// option 6 ////////////
				if ( Option.equals( "6" ) )
				{
					Error = true;
					while (Error)
					{	
						System.out.print( "\nDisarm the system? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.DISARM_SYSTEM );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Disarm System:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						else
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if
				
	//////////// option 7 ////////////
				if ( Option.equals( "7" )&& fireAlarm==true )
				{
					Error = true;
					while (Error)
					{	
						System.out.print( "\n7: Turn on sprinkler? (yes)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.CONSOLE_ID, Events.SPRINKLER_ON );

							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Turn On Sprintler:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if
				
				
	//////////// option 8 ////////////
				if ( Option.equals( "8" )&& sprinklerOn==true )
				{
					Error = true;
					while (Error)
					{	
						System.out.print( "\n8: Turn off sprinkler? (yes/no)>>> " );
						Option = UserInput.KeyboardReadString();
						if (Option.equals("yes"))
						{
							Error = false;
							evt = new Event( EventIDs.MOTION_ID, Events.SPRINKLER_OFF );
							timer.cancel();
							// Here we send the event to the event manager.
							try
							{
								em.SendEvent( evt );
							} // try
							catch (Exception e)
							{
								System.out.println( "Error Posting Turn Off Sprintler:: " + e );
							} // catch
						} 
						else if (Option.equals("no"))
						{
							Error = false;
						}
						{
							System.out.println( "Not a yes/no, please try again..." );
						} // if
					} // while
				} // if
				
				
				//////////// option X ////////////
				if ( Option.equalsIgnoreCase( "X" ) )
				{
					// Here the user is done, so we set the Done flag and halt
					// the environmental control system. The monitor provides a method
					// to do this. Its important to have processes release their queues
					// with the event manager. If these queues are not released these
					// become dead queues and they collect events and will eventually
					// cause problems for the event manager.
					Monitor.Halt();
					Done = true;
					System.out.println( "\nConsole Stopped... Exit monitor mindow to return to command prompt." );
					Monitor.Halt();
				} // if
				
				
				
				
				
			} // while

		} else {
			System.out.println("\n\nUnable start the monitor.\n\n" );
		} // if
		
  	} // main
	
	
	
	
	
	static class TimerTestTask extends TimerTask
	{
		public void run() {
			evt = new Event( EventIDs.CONSOLE_ID, Events.SPRINKLER_ON );
			// Here we send the event to the event manager.
			try
			{
				em.SendEvent( evt );
			} // try
			catch (Exception e)
			{
				System.out.println( "Error Posting Turn On Sprintler:: " + e );
			} // catch
			timer.cancel();
		}	
	}
	
} // ECSConsole
