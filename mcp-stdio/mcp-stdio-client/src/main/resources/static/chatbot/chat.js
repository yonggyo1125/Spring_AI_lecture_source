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

        // 2. 로딩 표시 시작
        loadingTrigger.classList.remove('hidden');
        chatWindow.scrollTop = chatWindow.scrollHeight;

        try {
            // Controller가 @RequestBody String으로 받으므로 일반 문자열 전송
            const response = await fetch('/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: question
            });

            if (!response.ok) throw new Error('서버 응답 오류');

            const data = await response.text();

            // 3. AI 메시지 추가 (HTML 포함 가능)
            addMessage(data, 'bot-message', true);

        } catch (error) {
            addMessage('오류가 발생했습니다: ' + error.message, 'bot-message');
        } finally {
            // 4. 로딩 표시 종료
            loadingTrigger.classList.add('hidden');
            chatWindow.scrollTop = chatWindow.scrollHeight;
        }
    });

    function addMessage(text, className, isHTML = false) {
        const msgDiv = document.createElement('div');
        msgDiv.classList.add('message', className);

        if (isHTML) {
            msgDiv.innerHTML = text; // Service에서 HTML 태그를 보내주므로 처리
        } else {
            msgDiv.textContent = text;
        }

        chatWindow.appendChild(msgDiv);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }
});