package jshooter.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationInfo {

	private String name;
	private String version;
	private String buildTimestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildTimestamp() {
		return buildTimestamp;
	}

	public Date getBuildTimestampDate() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(getBuildTimestamp());
		} catch (ParseException e) {
			return null;
		}
	}

	public void setBuildTimestamp(String buildTimestamp) {
		this.buildTimestamp = buildTimestamp;
	}

}
