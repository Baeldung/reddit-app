package org.baeldung.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.reddit.classifier.RedditClassifier;
import org.baeldung.reddit.util.MyFeatures;
import org.baeldung.reddit.util.UserAgentInterceptor;
import org.baeldung.security.MyAuthorizationCodeAccessTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableScheduling
@EnableAsync
@ComponentScan({ "org.baeldung.web" })
@PropertySource("classpath:email.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/home");
        registry.addViewController("/siteForm");
        registry.addViewController("/submissionResponse");
        registry.addViewController("/profile");
    }

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> messageConverters) {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        messageConverters.add(new MappingJackson2HttpMessageConverter(builder.build()));
        super.configureMessageConverters(messageConverters);
    }

    @Bean
    public OAuth2RestTemplate schedulerRedditTemplate(final OAuth2ProtectedResourceDetails reddit) {
        final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
        list.add(new UserAgentInterceptor());
        final OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(reddit);
        restTemplate.setInterceptors(list);
        final AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider> asList(new MyAuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(), new ResourceOwnerPasswordAccessTokenProvider(),
                new ClientCredentialsAccessTokenProvider()));
        restTemplate.setAccessTokenProvider(accessTokenProvider);
        return restTemplate;
    }

    @Bean
    public RestTemplate simpleRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
        list.add(new UserAgentInterceptor());
        restTemplate.setInterceptors(list);
        return restTemplate;
    }

    @Bean
    public RedditClassifier redditClassifier() throws IOException {
        final RedditClassifier redditClassifier = new RedditClassifier();
        if (MyFeatures.PREDICTION_FEATURE.isActive()) {
            final Resource file = new ClassPathResource("data.csv");
            redditClassifier.trainClassifier(file.getFile().getAbsolutePath());
        }
        return redditClassifier;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(env.getProperty("smtp.host"));
        mailSenderImpl.setPort(env.getProperty("smtp.port", Integer.class));
        mailSenderImpl.setProtocol(env.getProperty("smtp.protocol"));
        mailSenderImpl.setUsername(env.getProperty("smtp.username"));
        mailSenderImpl.setPassword(env.getProperty("smtp.password"));
        final Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", true);
        javaMailProps.put("mail.smtp.starttls.enable", true);
        mailSenderImpl.setJavaMailProperties(javaMailProps);
        return mailSenderImpl;
    }

    @Configuration
    @EnableOAuth2Client
    @PropertySource("classpath:reddit.properties")
    protected static class ResourceConfiguration {

        @Value("${accessTokenUri}")
        private String accessTokenUri;

        @Value("${userAuthorizationUri}")
        private String userAuthorizationUri;

        @Value("${clientID}")
        private String clientID;

        @Value("${clientSecret}")
        private String clientSecret;

        @Value("${redirectUri}")
        private String redirectUri;

        @Autowired
        private PostRepository repo;

        @Bean
        public OAuth2ProtectedResourceDetails reddit() {
            final AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
            details.setId("reddit");
            details.setClientId(clientID);
            details.setClientSecret(clientSecret);
            details.setAccessTokenUri(accessTokenUri);
            details.setUserAuthorizationUri(userAuthorizationUri);
            details.setTokenName("oauth_token");
            details.setScope(Arrays.asList("identity", "read", "submit", "edit"));
            details.setGrantType("authorization_code");
            details.setPreEstablishedRedirectUri(redirectUri);
            details.setUseCurrentUri(false);
            return details;
        }

        @Bean
        public OAuth2RestTemplate redditRestTemplate(final OAuth2ClientContext clientContext) {
            final OAuth2RestTemplate template = new OAuth2RestTemplate(reddit(), clientContext);
            final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
            list.add(new UserAgentInterceptor());
            template.setInterceptors(list);
            final AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider> asList(new MyAuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(), new ResourceOwnerPasswordAccessTokenProvider(),
                    new ClientCredentialsAccessTokenProvider()));
            template.setAccessTokenProvider(accessTokenProvider);
            return template;
        }

        @PostConstruct
        public void startupCheck() {
            if (StringUtils.isBlank(accessTokenUri) || StringUtils.isBlank(userAuthorizationUri) || StringUtils.isBlank(clientID) || StringUtils.isBlank(clientSecret)) {
                throw new RuntimeException("Incomplete reddit properties");
            }
            repo.findAll();
        }

    }
}
