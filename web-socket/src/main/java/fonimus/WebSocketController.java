package fonimus;

import fonimus.jpa.Message;
import fonimus.jpa.Topic;
import fonimus.jpa.TopicRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.codahale.metrics.annotation.Timed;

@Controller
public class WebSocketController
		implements ApplicationListener<SessionDisconnectEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private TopicRepository topicRepository;

	@PostConstruct
	public void init() {
		LOGGER.info("Initialize topic repo");
		Topic websocket = new Topic();
		websocket.setName("websocket");
		websocket.setLastUpdate(new Date());
		topicRepository.save(websocket);
		Topic gradle = new Topic();
		gradle.setName("gradle");
		gradle.setLastUpdate(new Date());
		topicRepository.save(gradle);
	}

	@RequestMapping(value = "/api/topic")
	@Timed(name = "topic.list")
	public ResponseEntity<List<Topic>> topics() {
		List<Topic> target = new ArrayList<>();
		topicRepository.findAll().forEach(target::add);
		return ResponseEntity.ok(target);
	}

	@RequestMapping(value = "/api/topic/{topicId}", method = RequestMethod.PUT)
	@Timed(name = "topic.put")
	public ResponseEntity<Void> addMessageInTopic(@RequestBody Message message, @PathVariable(name = "topicId") Long topicId) {
		LOGGER.info("Adding new message in topic : {} ({})", topicId, message);
		Topic topic = topicRepository.findOne(topicId);
		if (topic == null) {
			return ResponseEntity.notFound().build();
		}
		topic.getMessages().add(message);
		topic.setLastUpdate(new Date());
		topicRepository.save(topic);
		template.convertAndSend("/topic/notification",
				new WebSocketMessageTopic(WebSocketMessageType.update, message, "Topic " + topic.getName() + " has been updated", topic.getId(),
						topic.getName()));
		return ResponseEntity.ok().build();
	}

	@MessageMapping("/refresh")
	@SendTo("/topic/notification")
	public WebSocketMessage refresh(RefreshMessage refreshMessage) throws Exception {
		LOGGER.info("Refreshing topic : {}", refreshMessage);
		Long topicId = refreshMessage.getTopicId();
		Topic topic = topicRepository.findOne(topicId);
		if (topic == null) {
			throw new IllegalArgumentException("Cannot refresh unknown topic with id : " + topicId);
		}
		if (topic.getLastUpdate().after(refreshMessage.getLastUpdate())) {
			return new WebSocketMessage(WebSocketMessageType.update, "Topic " + topic.getName() + " has been updated", topic.getId(), topic.getName());
		} else {
			return new WebSocketMessage(WebSocketMessageType.same, "Topic " + topic.getName() + " has not been updated", topic.getId(), topic.getName());
		}
	}

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		LOGGER.info("Session disconnected");
		template.convertAndSend("/topic/notification", new WebSocketMessage(WebSocketMessageType.disconnect, "Session over !"));
	}
}
