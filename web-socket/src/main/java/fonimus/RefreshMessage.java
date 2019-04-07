package fonimus;

import java.util.Date;

/**
 * Created by francois on 04/03/2017.
 */
public class RefreshMessage {

	private Long topicId;

	private Date lastUpdate;

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "RefreshMessage{" +
				"topicId=" + topicId +
				", lastUpdate=" + lastUpdate +
				'}';
	}
}
