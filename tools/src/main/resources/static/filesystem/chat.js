document.addEventListener('DOMContentLoaded', () => {
    const sendBtn = document.getElementById('send-btn');
    const userInput = document.getElementById('user-input');
    const chatWindow = document.getElementById('chat-window');

    const appendMessage = (content, sender) => {
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message', sender);

        // AI 응답은 백엔드에서 <div>로 감싸진 HTML을 주므로 그대로 렌더링
        if (sender === 'ai') {
            messageDiv.innerHTML = `<i class="fa-solid fa-robot"></i> Architect: ${content}`;
        } else {
            // 유저 입력은 개행 처리
            messageDiv.innerText = `> USER: ${content}`;
        }

        chatWindow.appendChild(messageDiv);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    };

    const handleChat = async () => {
        const question = userInput.value.trim();
        if (!question) return;

        // 화면에 유저 메시지 표시
        appendMessage(question, 'user');
        userInput.value = ''; // 입력창 초기화

        try {
            const response = await fetch('/tools/filesystem', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(question)
            });

            if (response.ok) {
                const data = await response.text();
                appendMessage(data, 'ai');
            } else {
                appendMessage('에러가 발생했습니다: ' + response.statusText, 'system');
            }
        } catch (error) {
            appendMessage('서버와 통신할 수 없습니다: ' + error.message, 'system');
        }
    };

    sendBtn.addEventListener('click', handleChat);

    // Ctrl + Enter로 전송 가능하게 설정
    userInput.addEventListener('keydown', (e) => {
        if (e.ctrlKey && e.key === 'Enter') {
            handleChat();
        }
    });
});