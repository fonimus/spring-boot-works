package fonimus.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by francois on 04/03/2017.
 */
@Entity
public class Message {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String author;

	private String content;

	private Date time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", author='" + author + '\'' +
				", content='" + content + '\'' +
				", time=" + time +
				'}';
	}
}
