package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple server program<br/>
 * User gives path to the configuration file in the arguments<br/>
 * Some examples are given on '/index.html'
 *
 * @author Pavao Jerebić
 */
public class SmartHttpServer {
    /**
     * Address
     */
    private String address;
    /**
     * Port
     */
    private int port;
    /**
     * Number of workers
     */
    private int workerThreads;
    /**
     * Session timeout
     */
    private int sessionTimeout;
    /**
     * Mime types
     */
    private Map<String, String> mimeTypes = new HashMap<>();
    /**
     * Workers mapping and their full name
     */
    private Map<String, String> workers = new HashMap<>();
    /**
     * Workers mapping and references
     */
    private Map<String, IWebWorker> workersMap = new HashMap<>();
    /**
     * Server thread
     */
    private ServerThread serverThread;
    /**
     * Executor service
     */
    private ExecutorService threadPool;
    /**
     * Webroot
     */
    private Path documentRoot;
    /**
     * Sessions
     */
    private Map<String, SessionMapEntry> sessions = new HashMap<>();
    /**
     * Random generator
     */
    private Random sessionRandom = new SecureRandom();

    /**
     * Basic constructor
     *
     * @param configFileName path
     * @throws IllegalArgumentException if path is null or invalid
     */
    public SmartHttpServer(String configFileName) {
        if (configFileName == null) {
            throw new IllegalArgumentException("All parameters mus ton be null");
        }
        Properties properties = new Properties();
        try {
            InputStream is = Files.newInputStream(Paths.get(configFileName));
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid configFileName");
        }
        address = properties.getProperty("server.address");
        port = Integer.parseInt(properties.getProperty("server.port"));
        workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
        sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));

        initializeProperties(properties, mimeTypes, "server.mimeConfig");
        initializeProperties(properties, workers, "server.workers");
        initializeWorkersMap();

        documentRoot = Paths.get(properties.getProperty("server.documentRoot"));


        serverThread = new ServerThread();

    }

    /**
     * Helping method that initializes workers map
     */
    private void initializeWorkersMap() {
        workers.forEach((path, fqcn) -> {
            IWebWorker iww = getWorkerByName(fqcn);
            if (iww != null) {
                workersMap.put(path, iww);
            }
        });
    }

    /**
     * Helping method that initializes other properties, e.g. mime-types
     *
     * @param properties     main properties
     * @param map            map to initialize
     * @param propertiesName properties name ( must be a property of main properties)
     * @throws IllegalArgumentException if anything goes wrong
     */
    private void initializeProperties(Properties properties, Map<String, String> map, String propertiesName) {
        Properties mapProperties = new Properties();
        try {
            InputStream is = Files.newInputStream(Paths.get(properties.getProperty(propertiesName)));
            mapProperties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid properties path");
        }
        for (String name : mapProperties.stringPropertyNames()) {
            map.put(name, mapProperties.getProperty(name));
        }
    }

    /**
     * Method that starts the server and initializes session cleaner thread, executor service and server thread
     *
     * @throws IOException if writing or reading fails
     */
    protected synchronized void start() throws IOException {
        if (!serverThread.isAlive()) {
            threadPool = Executors.newFixedThreadPool(workerThreads, runnable -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Thread sessionCleaner = new Thread(() -> {
                while (!serverThread.finished) {
                    synchronized (SmartHttpServer.class) {
                        try {
                            Thread.sleep(5 * 60 * 1000);
                            System.out.printf("Logging cookies(%s),%n", new Date(System.currentTimeMillis()).toString());
                            sessions.forEach((k, e) -> System.out.printf("%s %s %s%n", k, e.sid, new Date(e.validUntil).toString()));
                            Set<String> keys = new HashSet<>(sessions.keySet());
                            for (String key : keys) {
                                if (sessions.get(key).validUntil < System.currentTimeMillis()) {
                                    sessions.remove(key);
                                }
                            }
                        } catch (InterruptedException ignored) {

                        }
                    }
                }
            });
            sessionCleaner.setDaemon(true);
            sessionCleaner.start();

            serverThread.run();
        }

    }

    /**
     * Method that signals the main thread to stop and executor service to shutdown
     */
    protected synchronized void stop() {
        serverThread.setFinished(true);
        threadPool.shutdown();

    }

    /**
     * Class representing a server thread
     */
    protected class ServerThread extends Thread {
        /**
         * Flag that signalizes the end
         */
        private volatile boolean finished = false;

        @Override
        public void run() {

            ServerSocket socket;
            try {
                socket = new ServerSocket();
                socket.bind(new InetSocketAddress(
                        address,
                        port
                ));
            } catch (IOException e) {
                System.out.println("Could not open socket");
                return;
            }

            while (!finished) {
                try {
                    Socket toClient = socket.accept();
                    serve(toClient);
                } catch (IOException ignored) {
                }

            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }

        }

        /**
         * Helping method that submits new worker to the service
         *
         * @param toClient socket to client
         */
        private void serve(Socket toClient) {
            ClientWorker worker = new ClientWorker(toClient);
            threadPool.submit(worker);
        }

        /**
         * Sets finished flag
         *
         * @param finished finised
         */
        public void setFinished(boolean finished) {
            this.finished = finished;
        }
    }

    /**
     * Class representing a worker that dispatches request response
     */
    private class ClientWorker implements Runnable, IDispatcher {
        /**
         * Socket to the client
         */
        private Socket csocket;
        /**
         * Input stream
         */
        private PushbackInputStream istream;
        /**
         * Output stream
         */
        private OutputStream ostream;
        /**
         * Version of HTTP
         */
        private String version;
        /**
         * HTTP method
         */
        private String method;
        /**
         * Parameters
         */
        private Map<String, String> params = new HashMap<>();
        /**
         * Temporary parameters
         */
        private Map<String, String> tempParams = new HashMap<>();
        /**
         * Permanent parameters, connected to the session
         */
        private Map<String, String> permPrams = new HashMap<>();
        /**
         * Output cookies ( all are HttpOnly )
         */
        private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
        /**
         * Context
         */
        private RequestContext context = null;

        /**
         * Basic constructor
         *
         * @param csocket client socket
         * @throws IllegalArgumentException if client socket is null
         */
        public ClientWorker(Socket csocket) {
            if (csocket == null) {
                throw new IllegalArgumentException("Socket must not be null");
            }
            this.csocket = csocket;
        }

        @Override
        public void run() {

            try {
                istream = new PushbackInputStream(csocket.getInputStream());
                ostream = new BufferedOutputStream(
                        csocket.getOutputStream()
                );

                byte[] request = readRequest();
                if (request == null) {
                    sendError(400, "Bad request");
                    return;
                }
                String requestStr = new String(
                        request,
                        StandardCharsets.US_ASCII
                );

                List<String> headers = extractHeaders(requestStr);
                String[] firstLine = headers.isEmpty() ?
                        null : headers.get(0).split(" ");
                if (firstLine == null || firstLine.length != 3) {
                    sendError(400, "Bad request");
                    return;
                }

                method = firstLine[0].toUpperCase();
                if (!method.equals("GET")) {
                    sendError(400, "Method Not Allowed");
                    return;
                }

                version = firstLine[2].toUpperCase();
                if (!version.equals("HTTP/1.1") || !version.equals("HTTP/1.1")) {
                    sendError(505, "HTTP Version Not Supported");
                    return;
                }

                String path = firstLine[1];
                int parameterStart = path.indexOf("?");
                if (parameterStart != -1) {
                    String parameters = path.substring(parameterStart + 1);
                    path = path.substring(0, parameterStart);
                    for (String pair : parameters.split("[&]")) {
                        String[] nameAndValue = pair.split("[=]");
                        if (nameAndValue.length != 2) {
                            sendError(500, "Invalid parameters");
                            return;
                        }
                        String name = nameAndValue[0];
                        String value = nameAndValue[1];

                        params.put(name, value);
                    }
                }

                if (path.startsWith("/..")) {
                    sendError(403, "Forbidden path");
                    return;
                }

                String host = null;
                for (String header : headers) {
                    if (header.startsWith("Host:")) {
                        host = header.split("[ ]")[1].split("[:]")[0];
                    }
                }

                if (host == null) {
                    sendError(400, "Host not found in header");
                    return;
                }


                String sidCandidate = null;
                for (String header : headers) {
                    if (header.startsWith("Cookie:")) {
                        String[] cookieData = header.split("[ ]")[1].split("[;]");
                        for (String pair : cookieData) {
                            String name = pair.split("[=]")[0];
                            String value = pair.split("[=]")[1];
                            if (name.equals("sid")) {
                                sidCandidate = value.substring(1, value.length() - 1);
                            }
                        }
                    }
                }

                synchronized (ClientWorker.class) {
                    SessionMapEntry entry = sessions.get(sidCandidate);

                    if (entry == null || entry.validUntil < System.currentTimeMillis()) {
                        entry = new SessionMapEntry(new BigInteger(100, sessionRandom).toString(32), System.currentTimeMillis() + sessionTimeout * 1000, new ConcurrentHashMap<>());
                        RequestContext.RCCookie cookie = new RequestContext.RCCookie("sid", entry.sid, null, host, "/");
                        outputCookies.add(cookie);
                    } else {
                        entry.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
                    }

                    sessions.put(entry.sid, entry);
                    permPrams = entry.map;
                }


                internalDispatchRequest(path, true);

            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            } finally {
                try {
                    csocket.close();
                } catch (IOException ignored) {
                }
            }
        }

        /**
         * Helping method that extracts header data to separate lines from the request header
         *
         * @param requestHeader request header
         * @return list of header data
         */
        private List<String> extractHeaders(String requestHeader) {
            List<String> headers = new ArrayList<>();
            String currentLine = null;
            for (String s : requestHeader.split("\n")) {
                if (s.isEmpty()) break;
                char c = s.charAt(0);
                if (c == 9 || c == 32) {
                    currentLine += s;
                } else {
                    if (currentLine != null) {
                        headers.add(currentLine);
                    }
                    currentLine = s;
                }
            }
            if (!currentLine.isEmpty()) {
                headers.add(currentLine);
            }
            return headers;

        }

        /**
         * Helping method that produces and error message
         *
         * @param statusCode status code
         * @param statusText status text
         * @throws IOException if writing fails
         */
        private void sendError(int statusCode, String statusText) throws IOException {
            ostream.write(
                    ("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                            "Server: simple java server\r\n" +
                            "Content-Type: text/plain;charset=UTF-8\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n").getBytes(StandardCharsets.US_ASCII)
            );
            ostream.flush();
        }

        /**
         * Helping method that reads request
         *
         * @return request as bytes
         * @throws IOException if reading fails
         */
        private byte[] readRequest() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int state = 0;
            l:
            while (true) {
                int b = istream.read();
                if (b == -1) return null;
                if (b != 13) {
                    bos.write(b);
                }
                switch (state) {
                    case 0:
                        if (b == 13) {
                            state = 1;
                        } else if (b == 10) state = 4;
                        break;
                    case 1:
                        if (b == 10) {
                            state = 2;
                        } else state = 0;
                        break;
                    case 2:
                        if (b == 13) {
                            state = 3;
                        } else state = 0;
                        break;
                    case 3:
                        if (b == 10) {
                            break l;
                        } else state = 0;
                        break;
                    case 4:
                        if (b == 10) {
                            break l;
                        } else state = 0;
                        break;
                }
            }
            return bos.toByteArray();
        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }

        /**
         * Helping method that processes dispatch request
         *
         * @param urlPath    url path requested
         * @param directCall is this a direct call from within this method ( for private files )
         * @throws Exception if anything goes wrong
         */
        public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {

            Path requestedPath = Paths.get(documentRoot.toAbsolutePath().toString() + urlPath);
            if (directCall) {
                if (urlPath.startsWith("/private/") || urlPath.equals("/private")) {
                    sendError(404, "Not allowed");
                    return;
                }
            }

            if (urlPath.startsWith("/ext/")) {
                String fqcn = "hr.fer.zemris.java.webserver.workers." + urlPath.substring(5);
                IWebWorker iww = getWorkerByName(fqcn);
                if (iww == null) {
                    sendError(404, "Class not found");
                    return;
                }
                if (context == null) {
                    context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
                }
                iww.processRequest(context);
                ostream.flush();
                return;
            }


            if (workersMap.containsKey(urlPath)) {
                IWebWorker worker = workersMap.get(urlPath);
                if (context == null) {
                    context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
                }
                worker.processRequest(context);
                ostream.flush();
                return;
            }

            if (!Files.isReadable(requestedPath)) {
                sendError(404, "File not found");
                return;
            }


            int extensionStart = urlPath.lastIndexOf(".");
            if (extensionStart == -1 || extensionStart == urlPath.length() - 1) {
                sendError(400, "File does not have an extension");
                return;
            }
            String extension = urlPath.substring(extensionStart + 1);

            String mimeType = mimeTypes.getOrDefault(extension, "application/octet-stream");

            if (context == null) {
                context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
            }
            context.setMimeType(mimeType);

            byte[] file = Files.readAllBytes(requestedPath);

            if (extension.equals("smscr")) {
                String script = new String(file, StandardCharsets.UTF_8);
                SmartScriptParser parser = new SmartScriptParser(script);
                SmartScriptEngine engine = new SmartScriptEngine(parser.getDocumentNode(), context);
                engine.execute();
            } else {
                context.write(file);
            }
            ostream.flush();
        }
    }

    /**
     * Helping method that returns a worker if it exist, or null
     *
     * @param fqcn fully qualified class name
     * @return worker, or null
     */
    private IWebWorker getWorkerByName(String fqcn) {
        try {
            Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
            Object newObject = referenceToClass.newInstance();
            return (IWebWorker) newObject;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignored) {
            return null;
        }
    }

    /**
     * Helping class that represents session map entry
     */
    private static class SessionMapEntry {
        /**
         * Session id
         */
        String sid;
        /**
         * Valid untill
         */
        long validUntil;
        /**
         * Map pointing to permanent parameters
         */
        Map<String, String> map;

        /**
         * Basic constructor
         *
         * @param sid        session id
         * @param validUntil valid until
         * @param map        permanent parameters
         * @throws IllegalArgumentException if any argument is null
         */
        public SessionMapEntry(String sid, long validUntil, Map<String, String> map) {
            if (sid == null
                    || map == null) {
                throw new IllegalArgumentException("All parameters must not be null");
            }
            this.sid = sid;
            this.validUntil = validUntil;
            this.map = map;
        }

    }

    /**
     * Starting method
     *
     * @param args path to the main properties file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected 1 argument: confPath");
            return;
        }
        try {
            new SmartHttpServer(args[0]).start();
        } catch (IOException e) {
            System.out.println("Failed launching the server");
        }
    }
}
