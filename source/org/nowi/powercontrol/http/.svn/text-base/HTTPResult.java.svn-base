package org.nowi.powercontrol.http;

import java.net.HttpURLConnection;

import org.nowi.powercontrol.text.TextUtils;

public class HTTPResult
{
    private final String result;
    private final int httpResponseCode;
    private final String httpErrorText;

    public HTTPResult(String newResult, int newHttpResponseCode)
    {
        this.result = newResult;
        this.httpResponseCode = newHttpResponseCode;
        this.httpErrorText = TextUtils.getInstance().getErrorText(String.valueOf(newHttpResponseCode));
    }

    public String getResultAsString()
    {
        return result;
    }

    public int getHttpResponseCode()
    {
        return httpResponseCode;
    }

    public String getHttpErrorText()
    {
        return httpErrorText;
    }

    public boolean isError()
    {
        return HttpURLConnection.HTTP_OK != httpResponseCode;
    }
}
