package fonimus;

import java.util.Date;
import java.util.UUID;

public class WebSocketMessage {

    private String id;

    private WebSocketMessageType type;

    private Object resourceId;

    private Object resourceName;

    private String content;

    private Date time;

    public WebSocketMessage(WebSocketMessageType type) {
        this(type, null);
    }

    public WebSocketMessage(WebSocketMessageType type, String content) {
        this(type, content, null, null);
    }

    public WebSocketMessage(WebSocketMessageType type, String content, Object resourceId, Object resourceName) {
        this.id = UUID.randomUUID().toString();
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.content = content;
        this.type = type;
        this.time = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WebSocketMessageType getType() {
        return type;
    }

    public void setType(WebSocketMessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Object getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Object getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
