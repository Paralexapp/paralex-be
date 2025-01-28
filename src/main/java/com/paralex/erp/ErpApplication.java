package com.paralex.erp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.paralex.erp.dtos.EmailCredentialsDto;
import com.paralex.erp.dtos.LongToLocalDateTimeConverter;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.providers.EmailCredentialsProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@EnableWebSecurity
@SpringBootApplication
public class ErpApplication {
	public static final Integer DEFAULT_PAGE_SIZE = 100;
	public static final String AUTHORIZATION_COOKIE_KEY = "session";
	public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

	private final HttpServletRequest httpServletRequest;
	private final EmailCredentialsProvider emailCredentialsProvider;

	private static final Logger log = LoggerFactory.getLogger(ErpApplication.class);
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//				// https://docs.spring.io/spring-security/reference/features/exploits/http.html
//				// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
//				.authorizeHttpRequests(authorize -> authorize
//						.anyRequest()
//						.permitAll()
//				)
//				// if Spring MVC is on classpath and no CorsConfigurationSource is provided,
//				// Spring Security will use CORS configuration provided to Spring MVC
//				.csrf(AbstractHttpConfigurer::disable)
//				.cors(withDefaults());
//
//		return http.build();
//	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@RequestScope
	public UserEntity userEntity() {
		return (UserEntity) httpServletRequest.getAttribute("user");
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().findAndRegisterModules();
	}

	@Bean
	public OkHttpClient okHttpClient() {
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

		return new OkHttpClient.Builder()
				.addInterceptor(
						new DefaultContentTypeInterceptor(MediaType.APPLICATION_JSON_VALUE))
				.build();
	}

	@Bean
	public MongoCustomConversions customConversions() {
		return new MongoCustomConversions(Arrays.asList(new LongToLocalDateTimeConverter()));
	}

	@Bean
	public EmailCredentialsDto emailCredentials() {
		return EmailCredentialsDto.builder()
				.host(emailCredentialsProvider.getHost())
				.port(emailCredentialsProvider.getPort())
				.username(emailCredentialsProvider.getUsername())
				.password(emailCredentialsProvider.getPassword())
				.protocol(emailCredentialsProvider.getProtocol())
				.authentication(emailCredentialsProvider.isAuthentication())
				.tlsEnabled(emailCredentialsProvider.isTlsEnabled())
				.debug(emailCredentialsProvider.isDebug())
				.build();
	}

	@Bean
	public MustacheFactory mustacheFactory() {
		return new DefaultMustacheFactory();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@RequiredArgsConstructor
	public static class DefaultContentTypeInterceptor implements Interceptor {
		private final String contentType;

		public @NotNull Response intercept(Interceptor.Chain chain)
				throws IOException {
			final Request originalRequest = chain.request();
			String requestUrl = originalRequest.url().toString();

			// Check if the request URL matches a pattern you want to skip
			if (requestUrl.startsWith("/api/v1/auth/")) {
				return chain.proceed(originalRequest);  // Skip interceptor logic for /api/v1/auth/ routes
			}

			final Request requestWithUserAgent = originalRequest
					.newBuilder()
					.header(CONTENT_TYPE, contentType)
					.build();

			return chain.proceed(requestWithUserAgent);
		}
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ErpApplication.class, args);
	}

}
