<template>
  <vue-advanced-chat
      :current-user-id="currentUserId"
      :rooms="JSON.stringify(rooms)"
      :messages="JSON.stringify(messages)"
      :room-actions="JSON.stringify(roomActions)"
      :rooms-loaded="true"
      :messages-loaded="this.messagesLoaded"
      @add-room="onAddRoom"
      @send-message="onSendMessage"
  />
</template>

<script>
import { register } from 'vue-advanced-chat'
register()

export default {
  data() {
    return {
      currentUserId: '1234',
      rooms: [],
      messages: [],
      roomActions: [
        { name: 'inviteUser', title: 'Invite User' },
        { name: 'removeUser', title: 'Remove User' },
        { name: 'deleteRoom', title: 'Delete Room' }
      ],
      messagesLoaded: true
    }
  },
  methods: {
    onAddRoom() {
      this.messages = [];
      const roomId = crypto.randomUUID();
      const room = {
        roomId: roomId,
        roomName: roomId,
        users: [
          {
            _id: 'user1',
            username: 'user1'
          }
        ],
        messages: [],
      }
      // Keep only one room
      this.rooms = [room]
      this.initConversation();
    },
    async onSendMessage(event) {
      const msg = event.detail.at(0).content;
      this.messages.push({
        _id: Math.random().toString(36).substr(2, 9),
        content: msg,
        senderId: this.currentUserId,
        timestamp: new Date().toISOString()
      });

      // request the answer
      this.messagesLoaded = true;
      const answer = await this.askQuestion("Question")
      this.messages.push({
        _id: Math.random().toString(36).substr(2, 9),
        content: answer,
        senderId: 'user1',
        timestamp: new Date().toISOString()
      });
      this.messagesLoaded = true;
    },
    async initConversation() {
      const answer = await this.askQuestion('');
      this.displayMessage('user1', answer)
    },
    async askQuestion(question) {
      try {
        const response = await fetch(`http://localhost:8081/session/${this.rooms.at(0).roomId}/question`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            text: question
          }),
        });
        if (!response.ok) {
          throw new Error('Error: ', response);
        }
        const result = await response.json();
        return result.text;
      } finally {
        this.messagesLoaded = true;
      }
    },
    displayMessage(userId, message) {
      try {
        this.messagesLoaded = true;
        this.messages.push({
          _id: Math.random().toString(36).substr(2, 9),
          content: message,
          senderId: userId,
          timestamp: new Date().toISOString()
        });
      } finally {
        this.messagesLoaded = true;
      }
    }
  }
}
</script>
