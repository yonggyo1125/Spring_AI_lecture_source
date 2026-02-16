async function sendStreamMessage() {
    const input = document.getElementById('user-input');
    const chatWindow = document.getElementById('chat-window');
    const message = input.value.trim();

    if (!message) return;

    // 사용자 메시지 추가
    addMessage(message, 'user');
    input.value = '';

    // 어린 왕자의 답변을 담을 빈 말풍선 미리 생성
    const botMessageDiv = document.createElement('div');
    botMessageDiv.classList.add('message', 'bot');
    const botBubble = document.createElement('div');
    botBubble.classList.add('bubble');
    botMessageDiv.appendChild(botBubble);
    chatWindow.appendChild(botMessageDiv);

    try {
        // 컨트롤러의 /littleprince/stream 엔드포인트로 요청
        const response = await fetch('/littleprince/stream', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: message
        });

        if (!response.ok) throw new Error("연결에 실패했어요.");

        // 스트림 데이터를 읽기 위한 리더 설정
        const reader = response.body.getReader();
        const decoder = new TextDecoder();

        while (true) {
            const { done, value } = await reader.read();
            if (done) break;

            // 받아온 데이터 조각을 텍스트로 변환하여 말풍선에 추가
            const chunk = decoder.decode(value, { stream: true });
            botBubble.innerText += chunk;

            // 새로운 내용이 들어올 때마다 자동 스크롤
            chatWindow.scrollTop = chatWindow.scrollHeight;
        }
    } catch (error) {
        console.error("Stream Error:", error);
        botBubble.innerText = "어른들은 참 이상해... 대답을 하다가 별로 돌아가 버렸나 봐.";
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