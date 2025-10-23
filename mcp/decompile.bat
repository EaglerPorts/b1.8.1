@echo off
set GAME_VERSION=b1.8.1

if not exist RetroMCP.jar bitsadmin /transfer RetroMCP https://github.com/MCPHackers/RetroMCP-Java/releases/download/v1.0/RetroMCP-Java-CLI.jar "%cd%\RetroMCP.jar"
if not exist "minecraft_server\source" mkdir "minecraft_server\source"
java -jar RetroMCP.jar setup %GAME_VERSION%
if exist "jars\minecraft_server.jar" (
  mkdir "jars\minecraft_server"
  tar -xf "jars\minecraft_server.jar" -C "jars\minecraft_server"
)
if exist "jars\minecraft_server\minecraft-server.jar" (
  del /f /q "jars\minecraft_server.jar"
  copy /y "jars\minecraft_server\minecraft-server.jar" "jars\minecraft_server.jar" >nul
)
rmdir /s /q "jars\minecraft_server"
java -jar RetroMCP.jar decompile