GAME_VERSION="b1.8.1"

if ! [ -f "RetroMCP.jar" ]; then
  wget -O RetroMCP.jar https://github.com/MCPHackers/RetroMCP-Java/releases/download/v1.0/RetroMCP-Java-CLI.jar
fi;

mkdir -p minecraft_server/source

java -jar RetroMCP.jar setup $GAME_VERSION

unzip -oq jars/minecraft_server.jar -d jars/minecraft_server

if [ -f "jars/minecraft_server/minecraft-server.jar" ]; then
  rm -rf jars/minecraft_server.jar
  cp jars/minecraft_server/minecraft-server.jar jars/minecraft_server.jar
fi;

rm -rf jars/minecraft_server

java -jar RetroMCP.jar decompile