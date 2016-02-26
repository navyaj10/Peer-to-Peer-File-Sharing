ReadMe File
============

Peer to Peer Application with a simple discovery Server.
---------------------------------------------------------

1.	Run the Server program: java Servermain
2.	To Run the Client program from various number of nodes: java clientmain <ipaddress>

###Quick description of the application

Details:
	
	Our project contains one server program that handles any number of client nodes that requests a connection.
	Once the Client makes a connection request, it will receive a list of available files for download.
	Any new Client joins the network, all other nodes in the network frames are updated with the new file list. 
	Node details and available files are stored in Server.