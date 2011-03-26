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

import TermioPackage.Termio;

import EventPackage.*;

public class SecurityConsole extends Thread
{
	private static Timer timer;
	private static EventManagerInterface em = null;// Interface object to the event manager
	private static Event evt = null;		// Event object
	private String EvtMgrIP = null;			// Event Manager IP address
	boolean Registered = true;				// Signifies that this class is registered with an event manager.
	
	private static boolean fireAlarm = false;
	private static boolean sprinklerOn = false;
	
	public SecurityConsole()
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
	
	public SecurityConsole( String EvmIpAddress )
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
		
		String Option = null;				// Menu choice from user
		
		@SuppressWarnings("unused")
		
		Event Evt0 = null;				// Event object
		EventQueue eq = null;			// Message Queue
		boolean Error = false;				// Error flag
		SecurityMonitor Monitor = null;			// The environmental control system monitor
		SecurityConsole console = null;
		
		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the event manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length != 0 )
 		{
			// event manager is not on the local system
 			// event manager is not on the local system
			Monitor = new SecurityMonitor( args[0] );
			console = new SecurityConsole(args[0]);
		} else {
			Monitor = new SecurityMonitor();
			console = new SecurityConsole();
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
				System.out.println( "1: Arm the system? (yes/no)" );
				System.out.println( "2: Disarm the system? (yes/no)" );
				System.out.println( "X: Stop System\n" );
				System.out.print( "\n>>>> " );
				Option = UserInput.KeyboardReadString();

				
				//////////// option 1 ////////////////////////////////////
				if ( Option.equals( "1" ) )
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
					}//while
					
					
					System.out.println( "a: Simulate door broken? (yes/no)" );
					System.out.println( "b: Simulate window broken? (yes/no)" );
					System.out.println( "c: Simulate motion detection? (yes/no)" );
					System.out.println( "d: Simulate fire alarm? (yes/no)" );
					if(fireAlarm == true)
					{
						System.out.println( "e: Turn on sprinkler? (yes/no)" );
						fireAlarm=true;
					}
					if(sprinklerOn == true)
					{
						System.out.println( "f: Turn off sprinkler? (yes/no)" );
						sprinklerOn=true;
					}
				
					
					System.out.print( "\n>>>> " );
					Option = UserInput.KeyboardReadString();
					
					if(Option.equals( "a" ))
					{
						//////////// option a ////////////
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
					}
						
					if(Option.equals( "b" ))
					{
						//////////// option b ////////////
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
					}	
					
					if(Option.equals( "c" ))
					{
						//////////// option c ////////////
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
					}			
					
					if(Option.equals( "d" ))
					{
						//////////// option d ////////////
						Error = true;
						while (Error)
						{	
							System.out.print( "\nSimulate fire alarm? (yes/no)>>> " );
							Option = UserInput.KeyboardReadString();
							if (Option.equals("yes"))
							{
								Error = false;
								fireAlarm = true;
								timer = new Timer();
								timer.schedule(new TimerTestTask(), 15*1000);
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
					}		
					
					if(Option.equals( "e" ) && fireAlarm==true)
					{
						//////////// option e ////////////
						Error = true;
						while (Error)
						{	
							System.out.print( "\ne: Turn on sprinkler? (yes)>>> " );
							Option = UserInput.KeyboardReadString();
							if (Option.equals("yes"))
							{
								Error = false;
								sprinklerOn=true;
								fireAlarm=false;
								evt = new Event( EventIDs.CONSOLE_ID, Events.SPRINKLER_ON );
								timer.cancel();
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
							else
							{
								System.out.println( "Not a yes/no, please try again..." );
							} // if
						} // while
					}		
					
					if(Option.equals( "f" ) && sprinklerOn == true)
					{
						//////////// option f ////////////
						Error = true;
						while (Error)
						{	
							System.out.print( "\nf: Turn off sprinkler? (yes/no)>>> " );
							Option = UserInput.KeyboardReadString();
							if (Option.equals("yes"))
							{
								Error = false;
								sprinklerOn = false;
								evt = new Event( EventIDs.CONSOLE_ID, Events.SPRINKLER_OFF );
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
							else
							{
								System.out.println( "Not a yes/no, please try again..." );
							} // if
						} // while
					}			
				} // if(option ==1)
				
				
	//////////// option 2 ////////////////////////////////////////////
				if ( Option.equals( "2" ) )
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
					
					System.out.println( "a: Simulate door broken? (yes/no)" );
					System.out.println( "b: Simulate window broken? (yes/no)" );
					System.out.println( "c: Simulate motion detection? (yes/no)" );
					System.out.println( "d: Simulate fire alarm? (yes/no)" );
					if(fireAlarm == true)
					{
						System.out.println( "e: Turn on sprinkler? (yes/no)" );
						fireAlarm=true;
					}
					if(sprinklerOn == true)
					{
						System.out.println( "f: Turn off sprinkler? (yes/no)" );
						sprinklerOn=true;
					}
				
					
					System.out.print( "\n>>>> " );
					Option = UserInput.KeyboardReadString();
					
					if(Option.equals( "a" ))
					{
						//////////// option a ////////////
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
					}
						
					if(Option.equals( "b" ))
					{
						//////////// option b ////////////
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
					}	
					
					if(Option.equals( "c" ))
					{
						//////////// option c ////////////
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
					}			
					
					if(Option.equals( "d" ))
					{
						//////////// option d ////////////
						Error = true;
						while (Error)
						{	
							System.out.print( "\nSimulate fire alarm? (yes/no)>>> " );
							Option = UserInput.KeyboardReadString();
							if (Option.equals("yes"))
							{
								Error = false;
								fireAlarm = true;
								timer = new Timer();
								timer.schedule(new TimerTestTask(), 15*1000);
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
					}		
					
					if(Option.equals( "e" ) && fireAlarm==true)
					{
						//////////// option e ////////////
						Error = true;
						while (Error)
						{	
							System.out.print( "\ne: Turn on sprinkler? (yes)>>> " );
							Option = UserInput.KeyboardReadString();
							if (Option.equals("yes"))
							{
								Error = false;
								sprinklerOn=true;
								fireAlarm=false;
								evt = new Event( EventIDs.CONSOLE_ID, Events.SPRINKLER_ON );
								timer.cancel();
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
							else
							{
								System.out.println( "Not a yes/no, please try again..." );
							} // if
						} // while
					}		
					
					if(Option.equals( "f" ) && sprinklerOn == true)
					{
						//////////// option f ////////////
						Error = true;
						while (Error)
						{	
							System.out.print( "\nf: Turn off sprinkler? (yes/no)>>> " );
							Option = UserInput.KeyboardReadString();
							if (Option.equals("yes"))
							{
								Error = false;
								sprinklerOn = false;
								evt = new Event( EventIDs.CONSOLE_ID, Events.SPRINKLER_OFF );
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
							else
							{
								System.out.println( "Not a yes/no, please try again..." );
							} // if
						} // while
					}			
				} // if (option == 2)
				
				
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
				
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception e)
				{
				}
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
				System.out.println("sprinkler automatically turn on ...");
				em.SendEvent( evt );
			} // try
			catch (Exception e)
			{
				System.out.println( "Error Posting Turn On Sprinkler:: " + e );
			} // catch
			timer.cancel();
			fireAlarm = false;
			sprinklerOn = true; 
		}	
	}
	
} // ECSConsole
