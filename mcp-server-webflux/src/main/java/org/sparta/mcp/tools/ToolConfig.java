package org.sparta.mcp.tools;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {
    private final DateTimeTools dateTimeTools;
    private final FileSystemTools fileSystemTools;
    private final InternetSearchTools internetSearchTools;

    public ToolConfig(DateTimeTools dateTimeTools, FileSystemTools fileSystemTools, InternetSearchTools internetSearchTools) {
        this.dateTimeTools = dateTimeTools;
        this.fileSystemTools = fileSystemTools;
        this.internetSearchTools = internetSearchTools;
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTimeTools, fileSystemTools, internetSearchTools)
                .build();
    }
}
