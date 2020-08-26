# Social-Network-Project
A project that implements a simple social network server and client. The communication between the server and the client(s) will be performed using a binary communication protocol. A registered user will be able to follow other users and post messages.

The implementation of the server will be based on the Thread-Per-Client (TPC) and Reactor servers. The servers only support pull
notifications. Any time the server receives a message from a client it can replay back to the client itself. The server support push notifications for brodcasting and sending direct messages. In addition, a new protocol was implemented to emulate a simple social netwok. The social network supports registration and login/logout of users. In the social network a user can post messages and follow other users.

For the full assignment instructions: https://www.cs.bgu.ac.il/~spl191/wiki.files/assignment3[2].pdf
