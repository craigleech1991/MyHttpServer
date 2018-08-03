# MyHttpServer
Java http server

 This program is a simple HTTPServer that serves multiple files. Server is started via the command
 prompt. Files can then be accessed through a web browser. Server has full logging capabilities, a requests
 log and errors log are created and stored in a logs directory.
 
 This program uses Java(TM) SE Runtime Environment (build 9.0.4+11)
 
 Compiling/Running Instructions:
 1) Navigate to directory containing source code.
 2) Ensure a directory "webroot" exists inside current directory
 3) Compile: javac MyHttpServer.java
 4) Run: java MyHttpServer webroot (port)
 5) To view in browser: localhost:port/file
 6) Documentation: javadoc MyHttpServer.java
