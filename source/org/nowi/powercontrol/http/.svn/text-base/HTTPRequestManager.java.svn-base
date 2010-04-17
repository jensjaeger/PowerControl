package org.nowi.powercontrol.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;

public class HTTPRequestManager
{
    private HTTPRequestManager()
    {
        // do not allow to instantiate utility class
    }

    /**
     * Sends an HTTP GET request to a url
     * 
     * @throws Exception
     * 
     */
    public static HTTPResult sendGetRequest(String endpoint, String requestParameters, int timeOutInMs, String login,
                    String password) throws Exception
    {
        try
        {
            StringBuilder urlBuffer = new StringBuilder(endpoint);

            if (requestParameters != null && requestParameters.length() > 0)
            {
                urlBuffer.append("?");
                urlBuffer.append(requestParameters);
            }

            setBasicHttpAuthentication(login, password);

            URL url = new URL(urlBuffer.toString());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(timeOutInMs);
            urlConnection.setReadTimeout(timeOutInMs);
            urlConnection.setInstanceFollowRedirects(false);

            int httpResponseCode = urlConnection.getResponseCode();

            if (httpResponseCode == HttpURLConnection.HTTP_OK)
            {
                // Get the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder responseBuffer = new StringBuilder();

                try
                {
                    for (String line = null; (line = reader.readLine()) != null;)
                    {
                        responseBuffer.append(line);
                    }
                }

                finally
                {
                    reader.close();
                }

                return new HTTPResult(responseBuffer.toString(), httpResponseCode);
            }
            else
            {
                return new HTTPResult(null, httpResponseCode);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static HTTPResult sendPostRequest(String endpoint, String requestParameters, int timeoutInMs, String login,
                    String password) throws Exception
    {
        try
        {
            setBasicHttpAuthentication(login, password);

            URL url = new URL(endpoint);
            Reader reader = new StringReader(requestParameters);

            try
            {
                StringWriter writer = new StringWriter();

                int httpResponseCode = postData(url, reader, writer, timeoutInMs, login, password);

                if (httpResponseCode == HttpURLConnection.HTTP_OK)
                {
                    return new HTTPResult(writer.getBuffer().toString(), httpResponseCode);
                }
                else
                {
                    return new HTTPResult(null, httpResponseCode);
                }
            }

            finally
            {
                reader.close();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Reads data from the data reader and posts it to a server via POST
     * 
     * @throws Exception
     * 
     */
    private static int postData(URL endpoint, Reader data, Writer output, int timeoutInMs, final String login,
                    final String password) throws Exception
    {
        int httpResponseCode = HttpURLConnection.HTTP_OK;
        HttpURLConnection urlConnection = null;

        try
        {
            urlConnection = (HttpURLConnection) endpoint.openConnection();

            try
            {
                setConnectionProperties(timeoutInMs, urlConnection);

                writePostData(data, urlConnection);

                httpResponseCode = readHttpResponse(output, urlConnection);
            }

            catch (IOException e)
            {
                e.printStackTrace();
                throw e;
            }

            finally
            {
                urlConnection.disconnect();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return httpResponseCode;
    }

    private static int readHttpResponse(Writer output, HttpURLConnection urlConnection) throws IOException
    {
        int httpResponseCode = urlConnection.getResponseCode();

        if (httpResponseCode == HttpURLConnection.HTTP_OK)
        {
            try
            {
                InputStream in = urlConnection.getInputStream();

                try
                {
                    Reader reader = new InputStreamReader(in);

                    try
                    {
                        pipeToOutput(reader, output);
                    }

                    finally
                    {
                        reader.close();
                    }
                }

                catch (UnknownServiceException ignored)
                {
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                    throw e;
                }

                finally
                {
                    in.close();
                }
            }

            catch (SocketTimeoutException e)
            {
                e.printStackTrace();
                throw e;
            }
        }

        return httpResponseCode;
    }

    private static void writePostData(Reader data, HttpURLConnection urlConnection) throws IOException
    {
        OutputStream out = urlConnection.getOutputStream();

        try
        {
            Writer writer = new OutputStreamWriter(out, "UTF-8");

            try
            {
                pipeToOutput(data, writer);
            }

            finally
            {
                writer.close();
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }

        finally
        {
            out.close();
        }
    }

    private static void setConnectionProperties(int timeoutInMs, HttpURLConnection urlConnection)
                    throws ProtocolException
    {
        urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(timeoutInMs);
        urlConnection.setReadTimeout(timeoutInMs);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
    }

    private static void setBasicHttpAuthentication(final String login, final String password)
    {
        Authenticator.setDefault(new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(login, password.toCharArray());
            }
        });
    }

    /**
     * Pipes everything from the reader to the writer via a buffer
     */
    private static void pipeToOutput(Reader reader, Writer writer) throws IOException
    {
        char[] buf = new char[8192];

        for (int numberOfBytesRead = 0; (numberOfBytesRead = reader.read(buf)) >= 0;)
        {
            writer.write(buf, 0, numberOfBytesRead);
        }

        writer.flush();
    }
}
