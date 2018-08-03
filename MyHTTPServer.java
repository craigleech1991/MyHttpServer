
/**
* I declare that this assignment is my own work and that all 
* material previously written or published in any source by any 
* other person has been duly acknowledged in the assignment. I 
* have not submitted this work, or a significant part thereof, 
* previously as part of any academic program. In submitting this 
* assignment I give permission to copy it for assessment purposes 
* only.
* 
* title: MyHTTPServer.java
* description: An HTTP Server that will server multiple files and has full logging capabilities. 
* @author craigleech
* @version 1.0
* @since 2018-04-23
*/

/**
 * DOCUMENTATION...
 */
/**                                                                               
 *
 *<P>
 * This program is a simple HTTPServer that serves multiple files. Server is started via the command
 * prompt. Files can then be accessed through a web browser. Server has full logging capabilities, a requests
 * log and errors log are created and stored in a logs directory.
 *</P>
 *<P>
 * This program uses Java(TM) SE Runtime Environment (build 9.0.4+11)
 *</P>
 *                                                                              
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DT> Create new folder "rootDirectory" to store files you wish to server</DT>
 *<DD> Compile:    javac MyHTTPServer.java</DD>
 *<DD> Run:        java MyHTTPServer webroot port</DD>
 *<DD> To view in browser: localhost:port/file</DD>
 *<DD> Document:   javadoc MyHTTPServer.java</DD>
 *</DL>
 */

/**
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * 1. i) Create rootDirectory inside directory with source code and fill with files you with to serve.
 *    ii) Start the Server from command prompt.
 *	  iii) view files in browser
 *	  iv) logs can be found in logs folder of current directory
 *	  v) Example output of MyPooledWeblog running with output from MyHTTPServer:
 *			**NOTE: java logging includes an extra line to the log files that must be removed to be run on MyPooledWeblog.
 *					I have simply removed the extra lines for now but modifications to MyPooledWeblog could be made to handle this.
 *
 *			Output:
 *			Craigs-MacBook-Pro:COMP348 - A1 craigleech$ java MyPooledWeblog access_log2 1
 * 			option: 1. this may take a while...
 * 			0:0:0:0:0:0:0:1:62400 number of accesses: 1
 * 			0:0:0:0:0:0:0:1:62399 number of accesses: 1
 * 			0:0:0:0:0:0:0:1:62398 number of accesses: 1
 * 			0:0:0:0:0:0:0:1:62397 number of accesses: 1
 * 			0:0:0:0:0:0:0:1:62395 number of accesses: 1
 * 			0:0:0:0:0:0:0:1:62393 number of accesses: 1
 * 			Craigs-MacBook-Pro:COMP348 - A1 craigleech$ java MyPooledWeblog access_log2 2
 * 			option: 2. this may take a while...
 * 			65057
 * 			4896635
 * 			174448
 * 			65057
 * 			65057
 * 			4896635
 * 			total bytes transmitted: 10162889
 * 			Craigs-MacBook-Pro:COMP348 - A1 craigleech$ java MyPooledWeblog access_log2 3
 * 			option: 3. this may take a while...
 * 			0:0:0:0:0:0:0:1:62400  total Bytes Transmitted: 4896635
 * 			0:0:0:0:0:0:0:1:62399  total Bytes Transmitted: 65057
 * 			0:0:0:0:0:0:0:1:62398  total Bytes Transmitted: 65057
 * 			0:0:0:0:0:0:0:1:62397  total Bytes Transmitted: 174448
 * 			0:0:0:0:0:0:0:1:62395  total Bytes Transmitted: 4896635
 * 			0:0:0:0:0:0:0:1:62393  total Bytes Transmitted: 65057
 *
 * EXPECTED:
 *   Server will start if given proper rootDirectory and port number
 *</P>
 *<P>
 * 2. Good data cases:
 * EXPECTED:
 *    Start the server from command prompt with the following parameters:
 *
 *   1. java MyHTTPServer webroot 1030
 *			Output should be of the form:
 *
 * 			Apr 23, 2018 3:32:48 PM MyHTTPServer start
 *          INFO: Accepting data connections on port 1030
 *
 *		To view files in browser:
 *			open browser and navigate to: localhost:1030/"desired file"
 *
 *		To view logs navigate to logs folder within current directory
 *
 * ACTUAL:
 *    Results displayed as expected.
 *</P>
 *<P>
 * 3. Bad data cases:
 * EXPECTED:
 *    Run the following test cases by running the program with the following parameters:
 *
 *    1.  (invalid port) to run: java MyHTTPServer webroot 4
 *			output should be:
 *			SEVERE: Could not start server
 *			java.net.BindException: Permission denied (Bind failed)
 *				at java.base/java.net.PlainSocketImpl.socketBind(Native Method)
 *				at java.base/java.net.AbstractPlainSocketImpl.bind(AbstractPlainSocketImpl.java:437)
 *				at java.base/java.net.ServerSocket.bind(ServerSocket.java:381)
 *				at java.base/java.net.ServerSocket.<init>(ServerSocket.java:243)
 *				at java.base/java.net.ServerSocket.<init>(ServerSocket.java:135)
 *				at MyHTTPServer.start(MyHTTPServer.java:83)
 *				at MyHTTPServer.main(MyHTTPServer.java:305)	
 *
 *	  2. (invalid rootDirectory) to run: java MyHTTPServer webroot 1030
 *			output should be:
 *			SEVERE: Server could not start
 *				java.io.IOException: rootDirectory does not exist as a directory
 *				at MyHTTPServer.<init>(MyHTTPServer.java:70)
 *				at MyHTTPServer.main(MyHTTPServer.java:304)
 *
 * ACTUAL: 
 *    Results displayed as expected.
 *</P>
 */ 

/**
 * CODE...
 */

/* import java packages*/
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.Files.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;


/* Primary class for MyHTTPServer */
public class MyHTTPServer {
	
		/* Declare auditLogger and errorLogger*/	
		private static final Logger auditLogger = Logger.getLogger("requests");
		private static final Logger errorLogger = Logger.getLogger("errors");
		private final int port; // port
		private final int NUM_THREADS = 4; // number of threads to be used
		private File rootDirectory; // the rootDirectory/ where all files will be stored
		private static final String indexFileName = "index.html"; // declare index.html

		/* public MYHTTPServer takes input rootDirectory and port */
		public MyHTTPServer(File rootDirectory, int port) throws IOException{

			if(!rootDirectory.isDirectory()){ /// if rootDirectory is not a directory throw an exception
				throw new IOException(rootDirectory + " does not exist as a directory");
			}
			/* set rootDirectory and port*/
			this.rootDirectory = rootDirectory; 
			this.port = port;
		}

		// start the server
		public void start(){
			/* Create a new thread pool */
			ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS); 
			/* try to connect to server socket on specifiec port*/
			try(ServerSocket server = new ServerSocket(this.port)){
				auditLogger.info("Accepting data connections on port " + server.getLocalPort());
				auditLogger.info("Document root: "+ rootDirectory);

				/* while connected create a new request and submit to thread pool*/
				while(true){
					try{
						Socket request = server.accept(); // new request
						Callable r = new HTTPHandler(rootDirectory, indexFileName, request); // callable request r
						pool.submit(r); // submit r to thread pool
					}
					catch(IOException ex){ // throw exception is unable to connect
						errorLogger.log(Level.WARNING, "Exception accepting connection ", ex);
					}
				}
			}
			/* throw exception could not start server*/
			catch(IOException ex){
				errorLogger.log(Level.SEVERE, "Could not start server", ex);
			}
		}

		/* private HTTPHandler class implements callable*/
	private class HTTPHandler implements Callable<Void>{
		private final Socket connection; // socket
		private final File rootDirectory; // rootDirectory
		private String indexFileName; // indexFIleName

		/* create new HTTPHandler, inpyt rootDirectory, indexFileName, Socket */
		HTTPHandler(File rootDirectory, String indexFileName, Socket connection){
			/* if rootDirectory is not a directory throw exception*/
			if(rootDirectory.isFile()){
				throw new IllegalArgumentException("rootDirectory must be a directory not a file");
			}
			/* try to get file from rootDiretory*/
			try{
				rootDirectory = rootDirectory.getCanonicalFile();
			}
			catch(IOException ex){ // catch IOexception
			}
			this.rootDirectory = rootDirectory;

			if(indexFileName != null){
			 this.indexFileName = indexFileName;
			} 
			this.connection = connection;
		}

		@Override
		public Void call() throws IOException{
			try{

				String root = rootDirectory.getPath(); // get the path to the rootDirectory

				/* try new Outpur Stream, chained to writed out, and reader = new input stream, chained to bufferedInputStream */
				try{
					OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
					Writer out = new OutputStreamWriter(raw);
					Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()),"US-ASCII");

					// declare requestLine
					StringBuilder requestLine = new StringBuilder();
					while(true){
						int c = in.read(); // read in from c
						if(c == '\r' || c == '\n') break; // if c == \r or \n (end of line) break
						requestLine.append((char) c);	// append c to requestLine
					}

					// Declare String get = requestLine to string
					String get = requestLine.toString();

					// declare tokens to index the string
					String[] tokens = get.split("\\s+");
					String method = tokens[0]; // method will be the first index of the string
					String version = ""; 

					/* if method = get*/
					if(method.equals("GET")){
						String fileName = tokens[1]; // get the fileName
						if(fileName.endsWith("/")) fileName += indexFileName;
						String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName); // get content type
						if(tokens.length > 2){
							version = tokens[2]; // get version
						}

						// Declare a File object 'theFile'
						File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));

						// if we can read the file
						if(theFile.canRead() && theFile.getCanonicalPath().startsWith(root)){
							byte[] theData = Files.readAllBytes(theFile.toPath()); // declare the data
							// if it starts with HTTP
							if(version.startsWith("HTTP/")){// send a MIME header
								sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
							}
							// write the date
							raw.write(theData);
							raw.flush();
							// write to auditLogger
							auditLogger.info(connection.getRemoteSocketAddress() + " " + get + " "+ theData.length);

						}
						else{// cant find the file
							// construct  message for the errorLogger
							String body = new StringBuilder("<HTML>\r\n")
								.append("<HEAD><TITLE> file not found</TITLE>\r\n")
								.append("</HEAD>\r\n")
								.append("<BODY>")
								.append("<H1>HTTP Error 404: file not found</H1>\r\n")
								.append("</BODY></HTML>\r\n").toString();
							if(version.startsWith("HTTP/")){
								sendHeader(out, "HTTP/1.0 404 File not found", "text/html; charset=utf-8", body.length());
							}
							// write errorLogger
							out.write(body);
							out.flush();
							errorLogger.log(Level.WARNING, "HTTP/1.0 404 File not found");
						}
					}
					else{//method does not equal GET
						// construct message for ErrorLogger
						String body = new StringBuilder("<HTML>\r\n")
								.append("<HEAD><TITLE> file not found</TITLE>\r\n")
								.append("</HEAD>\r\n")
								.append("<BODY>")
								.append("<H1>HTTP Error 504: not implemented</H1>\r\n")
								.append("</BODY></HTML>\r\n").toString();
							if(version.startsWith("HTTP/")){
								sendHeader(out, "HTTP/1.0 504 not implemented", "text/html; charset=utf-8", body.length());
							}
							// Write to errorLogger
							out.write(body);
							out.flush();
							errorLogger.log(Level.WARNING, "HTTP/1.0 504 not implemented");
					}
				} // Catch IOException
				catch(IOException ex){
					errorLogger.log(Level.WARNING, "Error talking  to "+connection.getRemoteSocketAddress(), ex);
				}
			}

			/* finally try to close the server*/
			finally{
				try{
					connection.close();
				}
				catch(IOException ex){
				}
			}
			return null;
		}
	}

	/* Private sendHeader - Method used to send a header
	* @param out
	* @param responseCode
	* @param contentType
	* @param length
	*/
	private void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException{
		out.write(responseCode + "\r\n");
		Date now = new Date();
		out.write("Date: "+now+"\r\n");
		out.write("Server: JHTTP 2.0\r\n");
		out.write("Content-length: "+length+"\r\n");
		out.write("Content-type: "+contentType+"\r\n\r\n");
		out.flush();
	}	

	/* Main Method*/
	public static void main(String[] args) {

		// set up fileHandlers for auditLogger and errorLogger
		FileHandler fh1;
		FileHandler fh2;
		try{
			fh1 = new FileHandler("/Users/craigleech/Documents/COMP348/assignment2/logs/requests.log"); // store requests.log here
			auditLogger.addHandler(fh1);
			SimpleFormatter formatter = new SimpleFormatter();
			fh1.setFormatter(formatter); // set format of fileHandler
			fh2 = new FileHandler("/Users/craigleech/Documents/COMP348/assignment2/logs/errors.log"); // store errors.log here
			errorLogger.addHandler(fh2);
			SimpleFormatter formatter2 = new SimpleFormatter();
			fh2.setFormatter(formatter2); // set format of fileHandler
			errorLogger.setLevel(Level.INFO); // include all error above the Level INFO
		}
		catch(SecurityException e){ // catch SecurityException
			e.printStackTrace();
		}
		catch(IOException e){ // catchIOException
			e.printStackTrace();
		}
		
		// set the port to listen on
		int port;
		try{
			port = Integer.parseInt(args[1]);
			// if invalid port
			if(port < 1 || port > 65535){
				port = 80;// set to port 80
			}
		}
		catch(RuntimeException ex){// catch runtime exception
			port = 80;// set to port 80
		}
		// set up the root directory
		File docroot;
		try{
			docroot = new File(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException ex){
			System.out.println("Usage: java MyHTTPServer rootDirectory port");
			return;
		}
		/// try to start the server
		try{
			MyHTTPServer server = new MyHTTPServer(docroot, port);
			server.start();
		}
		catch(ArrayIndexOutOfBoundsException ex){
			System.out.println("Usage: java MyHTTPServer rootDirectory port");
		}
		catch(IOException ex){
			errorLogger.log(Level.SEVERE, "Server could not start", ex);
		}
	}
}