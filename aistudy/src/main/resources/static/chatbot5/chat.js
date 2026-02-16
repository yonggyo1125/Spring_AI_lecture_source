async function sendMessage() {
    const input = document.getElementById('user-input');
    const chatWindow = document.getElementById('chat-window');
    const message = input.value.trim();

    if (!message) return;

    // 사용자 메시지 화면에 추가
    addMessage(message, 'user');
    input.value = '';

    try {
        // 백엔드 LittlePrinceController로 요청
        // Controller가 @RequestBody String으로 받으므로 JSON.stringify 없이 문자열 자체를 전송
        const response = await fetch('/littleprince5', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'text/plain'
            },
            body: message
        });

        if (response.ok) {
            const botReply = await response.text();
            addMessage(botReply, 'bot');
        } else {
            addMessage("어른들은 참 이상해... 대답을 들을 수가 없네.", 'bot');
        }
    } catch (error) {
        console.error("Error:", error);
        addMessage("소행성 B-612와 연결이 끊겼어.", 'bot');
    }
}

function addMessage(text, sender) {
    const chatWindow = document.getElementById('chat-window');
    const msgDiv = document.createElement('div');
    msgDiv.classList.add('message', sender);

    msgDiv.innerHTML = `<div class="bubble">${text}</div>`;

    chatWindow.appendChild(msgDiv);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}