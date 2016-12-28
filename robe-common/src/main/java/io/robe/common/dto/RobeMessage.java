package io.robe.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Client response message model
 */
public class RobeMessage {

    /***
     * Message log id
     */
    private String id;
    /**
     * Special error code giving by developers
     */
    private String code;
    /**
     * Http status code
     */
    private int status = 500;
    /**
     * Message detail
     */
    private String message;

    /**
     * Message description url
     */
    @JsonProperty("more_info")
    private String moreInfo;

    public RobeMessage() {
    }

    public RobeMessage(Builder builder) {
        this.id = builder.id;
        this.code = builder.code;
        this.status = builder.status;
        this.message = builder.message;
        this.moreInfo = builder.moreInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RobeMessage that = (RobeMessage) o;

        if (status != that.status) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return moreInfo != null ? moreInfo.equals(that.moreInfo) : that.moreInfo == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (moreInfo != null ? moreInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RobeMessage{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", moreInfo='" + moreInfo + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String code;
        private int status = 500;
        private String message;
        private String moreInfo;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder moreInfo(String moreInfo) {
            this.moreInfo = moreInfo;
            return this;
        }

        public int getStatus() {
            return status;
        }

        public RobeMessage build() {
            return new RobeMessage(this);
        }
    }
}
