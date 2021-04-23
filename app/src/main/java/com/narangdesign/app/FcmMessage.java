package com.narangdesign.app;

public class FcmMessage {
    private String image;
    private Boolean is_background;
    private String payload;
    private String title;
    private String message;
    private String timestamp;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIs_background() {
        return is_background;
    }

    public void setIs_background(Boolean is_background) {
        this.is_background = is_background;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FcmMessage{" +
                "image='" + image + '\'' +
                ", is_background=" + is_background +
                ", payload='" + payload + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
