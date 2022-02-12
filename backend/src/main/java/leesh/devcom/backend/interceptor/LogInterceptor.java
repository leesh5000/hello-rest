package leesh.devcom.backend.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";
    public static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String logId = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, logId);
        request.setAttribute(START_TIME, System.currentTimeMillis());

        HandlerMethod hm = null;
        if (handler instanceof HandlerMethod) {
            hm = (HandlerMethod) handler;
        }

        log.info("request preHandle [logId = {}][requestUri = {}][Handler = {}]", logId, requestURI, hm);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {

        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        Long startTime = (Long) request.getAttribute(START_TIME);
        Long elapsedTime = System.currentTimeMillis() - startTime;

        log.info("request afterCompletion [logId = {}][requestUri = {}][Handler = {}][total elapsed milliseconds = {}]", logId, requestURI, handler, elapsedTime);
        if (ex != null) {
            log.error("afterCompletion error [logId = {}]", logId, ex);
        }
    }
}
