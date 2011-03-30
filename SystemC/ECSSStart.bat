%ECHO OFF
%ECHO Starting Security System
PAUSE
%ECHO Security Monitoring Console
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM SECURITY CONSOLE" /NORMAL java SecurityConsole %1
%ECHO Starting Security Controller Console
START "SECURITY CONTROLLER CONSOLE" /MIN /NORMAL java SecurityController %1


%ECHO Starting Door Sensor Console
START "Door SENSOR CONSOLE" /MIN /NORMAL java DoorSensor %1

%ECHO Starting Window Sensor Console
START "WINDOW SENSOR CONSOLE" /MIN /NORMAL java WindowSensor %1

%ECHO Starting Motion Sensor Console
START "Motion SENSOR CONSOLE" /MIN /NORMAL java MotionSensor %1

%ECHO Starting Fire Sensor Console
START "Fire SENSOR CONSOLE" /MIN /NORMAL java FireSensor %1

%ECHO Starting Sprinkler Sensor Console
START "Sprinkler SENSOR CONSOLE" /MIN /NORMAL java SprinklerSensor %1