
# Compiling routes
SERVIDOR = src/server/Server3ER.java src/server/Jugador.java
CLIENT = src/client/Client3ER.java 
ADDICIONAL = src/addicional/Tauler.java

# Packages
SERVER_PACK = server.Server3ER
CLIENT_PACK = client.Client3ER



server: $(SERVIDOR) $(ADDICIONAL)
	javac -d out $(SERVIDOR) $(ADDICIONAL)

client: $(CLIENT) $(ADDICIONAL)
	javac -d out $(CLIENT) $(ADDICIONAL)

runs:
	java -cp out $(SERVER_PACK) $(PORT)

runc:
	java -cp out $(CLIENT_PACK) $(IP) $(PORT)

clean:
	rm -r out