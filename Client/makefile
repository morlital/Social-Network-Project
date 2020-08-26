# define some Makefile variables for the compiler and compiler flags
# to use Makefile variables later in the Makefile: $()
CC = g++
CFLAGS  = -g -Wall -Weffc++
LFLAGS  = -L/usr/lib -lboost_system -lboost_locale -pthread


# All Targets
all: Client

# Tool invocations
Client: bin/Client.o bin/connectionHandler.o bin/EncoderDecoder.o
	$(CC) -o bin/BGSClient bin/Client.o bin/connectionHandler.o bin/EncoderDecoder.o $(LFLAGS)


# Depends on the source and header files
bin/Client.o: src/Client.cpp
	$(CC) $(CFLAGS) -c -Linclude -o bin/Client.o src/Client.cpp

	
# Depends on the source and header files 
bin/connectionHandler.o: src/connectionHandler.cpp
	$(CC) $(CFLAGS) -c -Linclude -o bin/connectionHandler.o src/connectionHandler.cpp


# Depends on the source and header files
bin/EncoderDecoder.o: src/EncoderDecoder.cpp
	$(CC) $(CFLAGS) -c -Linclude -o bin/EncoderDecoder.o src/EncoderDecoder.cpp



#Clean the build directory
clean:
	rm -f bin/*
