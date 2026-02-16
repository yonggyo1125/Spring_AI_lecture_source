document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chat-form');
    const userInput = document.getElementById('user-input');
    const chatWindow = document.getElementById('chat-window');
    const loadingTrigger = document.getElementById('loading');

    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const question = userInput.value.trim();
        if (!question) return;

        // 1. 사용자 메시지 추가
        addMessage(question, 'user-message');
        userInput.value = '';

        // 2. 로딩 표시 및 초기 봇 메시지 객체 생성
        loadingTrigger.classList.remove('hidden');

        // 스트리밍 데이터를 담을 빈 봇 메시지 요소를 먼저 생성합니다.
        const botMessageElement = createEmptyMessage('bot-message');
        chatWindow.appendChild(botMessageElement);

        try {
            const response = await fetch('/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: question
            });

            if (!response.ok) throw new Error('서버 응답 오류');

            // 3. 스트리밍 데이터 읽기
            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let done = false;

            // 로딩 인디케이터는 첫 데이터가 오면 숨기거나 바로 숨깁니다.
            loadingTrigger.classList.add('hidden');

            while (!done) {
                const { value, done: doneReading } = await reader.read();
                done = doneReading;
                const chunkValue = decoder.decode(value, { stream: !done });

                // 실시간으로 텍스트 추가 (innerHTML을 사용할 경우 조각난 태그 주의)
                botMessageElement.innerHTML += chunkValue;
                chatWindow.scrollTop = chatWindow.scrollHeight;
            }

        } catch (error) {
            loadingTrigger.classList.add('hidden');
            addMessage('오류가 발생했습니다: ' + error.message, 'bot-message');
        } finally {
            chatWindow.scrollTop = chatWindow.scrollHeight;
        }
    });

    // 메시지 생성을 위한 유틸리티 함수들
    function addMessage(text, className) {
        const msgDiv = createEmptyMessage(className);
        msgDiv.textContent = text;
        chatWindow.appendChild(msgDiv);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    function createEmptyMessage(className) {
        const msgDiv = document.createElement('div');
        msgDiv.classList.add('message', className);
        return msgDiv;
    }
});