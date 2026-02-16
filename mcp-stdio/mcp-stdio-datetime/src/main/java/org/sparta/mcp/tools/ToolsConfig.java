package org.sparta.mcp.tools;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfig {
    private final DateTimeTools dateTimeTools;

    public ToolsConfig(DateTimeTools dateTimeTools) {
        this.dateTimeTools = dateTimeTools;
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTimeTools)
                .build();
    }
}
