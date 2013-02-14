package com.wixpress.testapp.controller;

import javax.xml.bind.annotation.*;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
@SuppressWarnings("unused")
public class AjaxResult
{
    @XmlAttribute
    private boolean isOk;

    public AjaxResult(boolean isOk) {
        this.isOk = isOk;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public static AjaxResult ok() {
        return new AjaxResult(true);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AjaxResult");
        sb.append("{isOk=").append(isOk);
        sb.append('}');
        return sb.toString();
    }
}
