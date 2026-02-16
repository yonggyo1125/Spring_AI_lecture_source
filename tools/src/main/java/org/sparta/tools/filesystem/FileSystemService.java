package org.sparta.tools.filesystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileSystemService {

    private final ChatClient chatClient;

    private final FileSystemTools fileSystemTools;

    public FileSystemService(ChatClient.Builder builder, ChatMemory chatMemory, FileSystemTools fileSystemTools) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem("""
                        당신은 'Full-Stack 코드 생성 에이전트'입니다.
                        사용자가 [기획서 양식]을 제출하면 다음 단계를 엄격히 따르세요:
                        
                        1. **해석**: 프로그램 명과 설명을 분석하여 필요한 클래스와 리소스를 구상합니다.
                        2. **설계**: [기획서 양식]에 명시된 개발 플랫폼의 구조에 맞게 경로를 결정합니다.
                        3. **실행**: `createFile` 도구를 호출하여 각 소스 코드(Java, HTML, CSS 등)를 실제로 생성합니다.
                        4. **보고**: 생성된 파일 목록과 주요 로직 설명을 <div> 태그를 사용하여 사용자에게 출력하세요.
                        
                        반드시 파일을 실제로 생성한 후에 답변을 마쳐야 합니다.
                        HTML 답변 내에서 <h1>, <h2>, <h3>는 절대 사용하지 마세요.
                 """)
                .build();

        this.fileSystemTools = fileSystemTools;
    }

    // LLM과 대화하는 메서드
    public String chat(String question, String conversationId) {
        return chatClient.prompt()
                .user(question)
                .advisors(advisorSpec ->  advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .tools(fileSystemTools)
                .call()
                .content();
    }
}
