/******************************************************************************************************************
* File:MConsole.java
* Course: 17655
* Project: Assignment 3
* Copyright: RShomali Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class is the console for the museum environmental control system. This process consists of two
* threads. The MMonitor object is a thread that is started that is responsible for the monitoring and control of
* the museum environmental systems. The main thread provides a text interface for the user to change the temperature
* and humidity ranges, as well as shut down the system.
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
import TermioPackage.*;
import EventPackage.*;

public class MConsole
{
	public static void main(String args[])
	{
    	Termio UserInput = new Termio();	// Termio IO Object
		boolean Done = false;				// Main loop flag
		String Option = null;				// Menu choice from user
		Event Evt = null;					// Event object
		boolean Error = false;				// Error flag
		MMonitor Monitor = null;			// The environmental control system monitor
	

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the event manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length != 0 )
 		{
			// event manager is not on the local system
			Monitor = new MMonitor( args[0] );

		} else {

			Monitor = new MMonitor();

		} // if


		// Here we check to see if registration worked. If ef is null then the
		// event manager interface was not properly created.

		if (Monitor.IsRegistered() )
		{
			Monitor.start(); // Here we start the monitoring and control thread


			System.out.println( "\n\n\n\n" );
			System.out.println( "Maintanace Console started \n" );

			if (args.length != 0)
				System.out.println( "Using event manger at: " + args[0] + "\n" );
			else
				System.out.println( "Using local event manger \n" );


			System.out.println( "Registered Components: \n" );	
			for ( String key : MySystem.COMPONENTS.keySet() )
			{
				System.out.println( key + "\n"  );
			}
			
			while (!Done)
			{
				// Here, the main thread continues and provides the main menu
		


			} // while

		} else {

			System.out.println("\n\nUnable start the monitor.\n\n" );

		} // if

  	} // main

} // MConsole
