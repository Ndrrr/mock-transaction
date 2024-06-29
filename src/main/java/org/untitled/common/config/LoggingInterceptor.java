package org.untitled.common.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.untitled.common.config.properties.LoggingProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor extends OncePerRequestFilter {

    private final LoggingProperties loggingProperties;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        final long start = System.currentTimeMillis();
        final String requestURI = request.getRequestURI();
        writeLog(enterMessage(request), requestURI);
        try {
            filterChain.doFilter(request, response);
        } finally {
            writeLog(exitMessage(request, start), requestURI);
        }
    }

    private String exitMessage(final HttpServletRequest request, final double start) {
        var builder = buildMessage(request);
        builder.append(" elapsedTime=").append(System.currentTimeMillis() - start);

        return builder.toString();
    }

    private String enterMessage(final HttpServletRequest request) {
        return buildMessage(request).toString();
    }

    private StringBuilder buildMessage(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append("method=").append(request.getMethod() + ",")
                .append(" uri=").append(request.getRequestURI());

        if (request.getQueryString() != null) {
            builder.append('?').append(request.getQueryString());
        }

        return builder;
    }

    private void writeLog(String request, String requestURI) {
        if (isLoggingEnabled() && isUriAllowed(requestURI)) {
            log.info(request);
        }
    }

    private boolean isLoggingEnabled() {
        return loggingProperties.getEnableLogInterceptor();
    }

    private boolean isUriAllowed(String requestURI) {
        return loggingProperties.getIgnorePaths().stream()
                .noneMatch(requestURI::startsWith);
    }

}