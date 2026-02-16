package org.sparta.mcp.tools;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfig {
    private final FileSystemTools fileSystemTools;

    public ToolsConfig(FileSystemTools fileSystemTools) {
        this.fileSystemTools = fileSystemTools;
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(fileSystemTools)
                .build();
    }
}
