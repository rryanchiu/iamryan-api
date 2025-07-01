package me.rryan.tinyurl.model.resp;

import java.io.Serializable;

public class CreateWordListBookResp implements Serializable {

    private String url;

    private boolean success;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
