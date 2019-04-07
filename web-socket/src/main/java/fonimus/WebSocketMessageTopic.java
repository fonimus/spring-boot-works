package fonimus;

import fonimus.jpa.Message;

/**
 * Created by francois on 04/03/2017.
 */
public class WebSocketMessageTopic extends WebSocketMessage {

	public WebSocketMessageTopic(WebSocketMessageType type, Message message, String content, Object resourceId, Object resourceName) {
		super(type, content, resourceId, resourceName);
		this.message = message;
	}

	private Message message;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
