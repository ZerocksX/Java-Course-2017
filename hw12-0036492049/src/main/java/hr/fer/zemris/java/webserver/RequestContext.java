package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class representing a request context<br/>
 * Contains outputStream, status code, mime type, cookies, parameters, and encoding<br/>
 * If {@link #write(String)} or {@link #write(byte[])} have already been called no more changes can be made to header data
 *
 * @author Pavao Jerebić
 */
public class RequestContext {

    /**
     * Output stream
     */
    private OutputStream outputStream;
    /**
     * Charset
     */
    private Charset charset;
    /**
     * Encoding, default is 'UTF-8'
     */
    private String encoding = "UTF-8";
    /**
     * Status code, default is '200'
     */
    private int statusCode = 200;
    /**
     * Status text, default is 'OK'
     */
    private String statusText = "OK";
    /**
     * Mime type, default is 'text/html'
     */
    private String mimeType = "text/html";
    /**
     * Parameters
     */
    private Map<String, String> parameters;
    /**
     * Temporary parameters
     */
    private Map<String, String> temporaryParameters;
    /**
     * Persistent parameters
     */
    private Map<String, String> persistentParameters;
    /**
     * Output cookies
     */
    private List<RCCookie> outputCookies;
    /**
     * Header generated flag
     */
    private boolean headerGenerated = false;
    /**
     * Dispatcher
     */
    private IDispatcher dispatcher;

    /**
     * Constructor that sets output stream and all collections without temporary parameters
     *
     * @param outputStream         output stream
     * @param parameters           parameters
     * @param persistentParameters persistent parameters
     * @param outputCookies        output cookies
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        this(outputStream, parameters, persistentParameters, outputCookies, null, null);
    }

    /**
     * Constructor that setts all colections, output stream and dispatcher
     *
     * @param outputStream         output stream
     * @param parameters           parameters
     * @param persistentParameters persistent parameters
     * @param outputCookies        cookies
     * @param temporaryParameters  temporary parameters
     * @param dispatcher           dispatcher
     * @throws IllegalArgumentException if output stream is null
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies, Map<String, String> temporaryParameters, IDispatcher dispatcher) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream must not be null");
        }
        this.outputStream = outputStream;
        this.parameters = parameters == null ? new HashMap<>() : parameters;
        this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
        this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
        this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
        this.outputCookies = outputCookies;
        this.dispatcher = dispatcher;
    }

    /**
     * Getter for output stream
     *
     * @return output stream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Getter for charset
     *
     * @return charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Getter for unmodifiable parameters
     *
     * @return unmodifiable parameters
     */
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * Getter for temporary parameters
     *
     * @return temporary parameters
     */
    public Map<String, String> getTemporaryParameters() {
        return temporaryParameters;
    }

    /**
     * Getter for persistent parameters
     *
     * @return persistent parameters
     */
    public Map<String, String> getPersistentParameters() {
        return persistentParameters;
    }

    /**
     * Getter for output cookies
     *
     * @return output cookies
     */
    public List<RCCookie> getOutputCookies() {
        return outputCookies;
    }

    /**
     * Getter for header generated flag
     *
     * @return is header generated
     */
    public boolean isHeaderGenerated() {
        return headerGenerated;
    }

    /**
     * Getter for parameter
     *
     * @param name name
     * @return name or null if it does not exist
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Getter for all parameter names
     *
     * @return unmodifiable set of parameter names
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Getter for persistent parameter
     *
     * @param name persistent parameter name
     * @return persistent parameter or null if it does not exist
     */
    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    /**
     * Getter for all persistent parameter names
     *
     * @return unmodifiable set of all persitent parameter names
     */
    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * adds a new persistent parameter
     *
     * @param name  name
     * @param value value
     * @throws IllegalArgumentException if name or value is null
     */
    public void setPersistentParameter(String name, String value) {
        if (name == null
                || value == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        persistentParameters.put(name, value);
    }

    /**
     * Removes parameter from persistent parameters if it exist
     *
     * @param name persistent parameter name
     */
    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    /**
     * Getter for temporary parameter if it exist
     *
     * @param name name
     * @return persistent parameter or null if it does not exist
     */
    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    /**
     * Getter for dispatcher
     *
     * @return dispatcher
     */
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Getter for temporary parameter names
     *
     * @return unmodifiable set of all temporary parameter names
     */
    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * adds new temporary parameter
     *
     * @param name  name
     * @param value value
     * @throws IllegalArgumentException if name or value is null
     */
    public void setTemporaryParameter(String name, String value) {
        if (name == null
                || value == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        temporaryParameters.put(name, value);
    }

    /**
     * Removes temporary parameter if it exist
     *
     * @param name name
     */
    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    /**
     * Sets encoding
     *
     * @param encoding encoding
     * @throws RuntimeException         if header was already generated
     * @throws IllegalArgumentException if encoding is null
     */
    public void setEncoding(String encoding) {
        if (headerGenerated) {
            throw new RuntimeException("Header was already generated");
        }
        if (encoding == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.encoding = encoding;
    }

    /**
     * Sets status code
     *
     * @param statusCode status code
     * @throws RuntimeException if header was already generated
     */
    public void setStatusCode(int statusCode) {
        if (headerGenerated) {
            throw new RuntimeException("Header was already generated");
        }
        this.statusCode = statusCode;
    }

    /**
     * Sets status text
     *
     * @param statusText status text
     * @throws RuntimeException         if header was already generated
     * @throws IllegalArgumentException if status text is null
     */
    public void setStatusText(String statusText) {
        if (headerGenerated) {
            throw new RuntimeException("Header was already generated");
        }
        if (statusText == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.statusText = statusText;
    }

    /**
     * Sets mime type
     *
     * @param mimeType mime type
     * @throws RuntimeException         if header was already generated
     * @throws IllegalArgumentException if mime type is null
     */
    public void setMimeType(String mimeType) {
        if (headerGenerated) {
            throw new RuntimeException("Header was already generated");
        }
        if (mimeType == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.mimeType = mimeType;
    }

    /**
     * Adds new cookie
     *
     * @param cookie cookie
     * @throws RuntimeException         if header was already generated
     * @throws IllegalArgumentException if cookie is null
     */
    public void addRCCookie(RCCookie cookie) {
        if (headerGenerated) {
            throw new RuntimeException("Header was already generated");
        }
        if (cookie == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.outputCookies.add(cookie);
    }

    /**
     * Removes a cookie
     *
     * @param cookie cookie
     * @throws RuntimeException         if header was already generated
     * @throws IllegalArgumentException if cookie is null
     */
    public void removeRCCookie(RCCookie cookie) {
        if (headerGenerated) {
            throw new RuntimeException("Header was already generated");
        }
        if (cookie == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.outputCookies.remove(cookie);
    }

    /**
     * Writes data to outputStream<br/>
     * If this is the first time writing anything header is first generated then written before data is written
     *
     * @param data data to output
     * @return this
     * @throws IOException      if writing fails
     * @throws RuntimeException if charset is malformed
     */
    public RequestContext write(byte[] data) throws IOException {
        byte[] preparedData = data;
        if (!headerGenerated) {
            try {
                charset = Charset.forName(encoding);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            byte[] header = generateHeader();
            preparedData = new byte[header.length + data.length];
            System.arraycopy(header, 0, preparedData, 0, header.length);
            System.arraycopy(data, 0, preparedData, header.length, data.length);
            headerGenerated = true;
        }
        outputStream.write(preparedData);
        return this;
    }

    /**
     * Writes text to output stream<br/>
     * If this is the first time writing anything header is first generated then written before data is written
     *
     * @param text text
     * @return this
     * @throws IOException      if writing fails
     * @throws RuntimeException if charset is nmalformed
     */
    public RequestContext write(String text) throws IOException {
        try {
            charset = Charset.forName(encoding);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return write(text.getBytes(charset));
    }

    /**
     * Helping method that generates header bytes using {@link StandardCharsets#ISO_8859_1}
     *
     * @return header transformed into bytes
     */
    private byte[] generateHeader() {
        return String.format("HTTP/1.1 %s %s\r\n" +
                        "Content-Type: %s" + (mimeType.startsWith("text/") ? "; charset=" + charset : "") + "\r\n" +
                        generateCookiesForHeader() +
                        "\r\n",
                statusCode, statusText,
                mimeType
        ).getBytes(StandardCharsets.ISO_8859_1);
    }

    /**
     * Returns outputCookies as string for header creation
     *
     * @return outputCookies as string
     */
    private String generateCookiesForHeader() {
        StringBuilder sb = new StringBuilder();
        for (RCCookie cookie : outputCookies) {
            sb.append("Set-Cookie: ").append(cookie.toString()).append("; HttpOnly\r\n");
        }
        return sb.toString();
    }


    /**
     * Class representing a cookie, always HttpOnly
     */
    public static class RCCookie {
        /**
         * Name
         */
        private String name;
        /**
         * Value
         */
        private String value;
        /**
         * Domain
         */
        private String domain;
        /**
         * Path
         */
        private String path;
        /**
         * Max-Age
         */
        private Integer maxAge;

        /**
         * Constructor that sets all fields
         *
         * @param name   name
         * @param value  value
         * @param maxAge Max-Age
         * @param domain domain
         * @param path   path
         * @throws IllegalArgumentException if name or value is null
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
            if (name == null
                    || value == null) {
                throw new IllegalArgumentException("Name and value must not be null");
            }
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
        }

        /**
         * Getter for name
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Getter for value
         *
         * @return value
         */
        public String getValue() {
            return value;
        }

        /**
         * Getter for domain
         *
         * @return domain
         */
        public String getDomain() {
            return domain;
        }

        /**
         * Getter for path
         *
         * @return path
         */
        public String getPath() {
            return path;
        }

        /**
         * Getter for Max-Age
         *
         * @return Max-Age
         */
        public Integer getMaxAge() {
            return maxAge;
        }

        @Override
        public String toString() {
            return name + "=\"" + value + "\""
                    + (domain == null ? "" : ("; Domain=" + domain))
                    + (path == null ? "" : ("; Path=" + path))
                    + (maxAge == null ? "" : ("; Max-Age=" + maxAge));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RCCookie)) return false;

            RCCookie cookie = (RCCookie) o;

            if (!name.equals(cookie.name)) return false;
            if (!value.equals(cookie.value)) return false;
            if (domain != null ? !domain.equals(cookie.domain) : cookie.domain != null) return false;
            if (path != null ? !path.equals(cookie.path) : cookie.path != null) return false;
            return maxAge != null ? maxAge.equals(cookie.maxAge) : cookie.maxAge == null;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + value.hashCode();
            result = 31 * result + (domain != null ? domain.hashCode() : 0);
            result = 31 * result + (path != null ? path.hashCode() : 0);
            result = 31 * result + (maxAge != null ? maxAge.hashCode() : 0);
            return result;
        }
    }
}
