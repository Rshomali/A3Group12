%ECHO OFF
%ECHO Starting Security System
PAUSE
%ECHO Security Monitoring Console
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM SECURITY CONSOLE" /NORMAL java SecurityConsole %1

%ECHO Starting Fire Sensor Console
START "FIRE SENSOR CONSOLE" /MIN /NORMAL java FireSensor %1

%ECHO Starting Sprinkler Controller Console
START "SPRINKLER CONTROLLER CONSOLE" /MIN /NORMAL java SprinklerController %1
