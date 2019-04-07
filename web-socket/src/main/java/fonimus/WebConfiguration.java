package fonimus;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by francois on 06/03/2017.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void configurePathMatch(PathMatchConfigurer matcher) {
		matcher.setUseRegisteredSuffixPatternMatch(true);
	}

}
