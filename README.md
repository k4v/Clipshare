Clipshare
=========

Utility to share clipboard text between two machines. The application copies
any text clipboard updates from either machine to the other allowing the two
machines to effectively share the system clipboard.

Working
-------

The application requires one system connect to the other, similar to a server-
client model; otherwise, the two machines are only considered peers. However,
this can change in case of parallel updates from both machines, (rare, if one
user controls both systems). In this case the "server" version is accepted on
both systems.

Usage
-----

To start the program in server mode: `java -jar Clipshare.jar -s {server-port}`

To start the program in client mode: `java -jar Clipshare.jar -s {server-host} {server-port}`
