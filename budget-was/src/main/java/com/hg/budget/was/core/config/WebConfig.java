package com.hg.budget.was.core.config;

import com.hg.budget.was.core.annotation.AccountId;
import com.hg.budget.was.core.security.AuthenticationInterceptor;
import com.hg.budget.was.core.security.UserDetails;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    /*
     * HandlerMethodArgumentResolver 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AccountIdArgumentResolver());
    }

    /*
     * CORS 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods(HttpMethod.GET.name(), HttpMethod.OPTIONS.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
            .allowCredentials(true)
            .maxAge(10);
    }

    /*
     * Interceptor 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        H2ConsoleRequestMatcher h2ConsoleRequestMatcher = PathRequest.toH2Console();
//        StaticResourceRequestMatcher staticResourceRequestMatcher = PathRequest
//            .toStaticResources()
//            .atCommonLocations();
        // TODO excludePathPatterns 에 위의 2개 RequestMatcher 등록 할 수 있는지 방법 찾아보기.(string 은 type safety 하지 않음)
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/static/**",
                "/h2-console/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/webjars/**",
                "/favicon.*",
                "/*/icon-*"
            );
    }

    /*
     * @AccountId annotation 기반 ArgumentResolver
     */
    private static class AccountIdArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterAnnotation(AccountId.class) != null;
        }

        @Override
        public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
        ) throws Exception {
            UserDetails context = UserDetailsContextHolder.getContext();
            if (context == null) {
                // TODO context 가 null 일 때 어떻게 처리 할 건지? (인증 에러? 로그 후 null 반환?)
                return null;
            }
            return context.getId();
        }
    }
}
