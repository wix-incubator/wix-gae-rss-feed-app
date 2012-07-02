package com.wixpress.testapp.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.http.HttpStatus;
import javax.xml.bind.annotation.*;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
@SuppressWarnings("unused")
public class Result<P>
{
    @XmlAttribute
    private int errorCode = 0;

    @XmlAttribute
    private String errorDescription = "OK";

    @XmlAttribute
    private boolean success;

    @XmlAnyElement(lax = true)
    private P payload;

    /**
     * The HTTP status that accompanies this result
     */
    private transient HttpStatus httpStatus = HttpStatus.OK;

    public Result()
    {
        this.success = true;
    }

    public Result(P payload)
    {
        this.payload = payload;
        this.success = true;
    }

    public Result(int errorCode, String errorDescription)
    {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.success = false;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorDescription()
    {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription)
    {
        this.errorDescription = errorDescription;
    }

    public P getPayload()
    {
        return payload;
    }

    public void setPayload(P payload)
    {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonIgnore
    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString()
    {
        return "Result{" +
                "errorCode=" + errorCode +
                ", errorDescription='" + errorDescription + '\'' +
                ", success=" + success +
                ", payload=" + payload +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
