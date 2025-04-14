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
      this.fetchMessages({room});
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
      this.messagesLoaded = false;
      try {
        const response = await fetch(`http://localhost:8081/session/${this.rooms.at(0).roomId}/question`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            text: msg
          }),
        });

        if (!response.ok) {
          throw new Error('Error: ', response);
        }

        const result = await response.json();
        this.messages.push({
          _id: Math.random().toString(36).substr(2, 9),
          content: result.text,
          senderId: 'user1',
          timestamp: new Date().toISOString()
        });
      } catch (error) {
        console.error('Error sending message:', error);
      } finally {
        this.messagesLoaded = true;
      }
    },

    fetchMessages({room})
    {
      console.log('loading messages in room: ' + room.roomId)
      this.messagesLoaded = false;

      // bot message
      setTimeout(() => {
        this.messages.push({
          _id: Math.random().toString(36).substr(2, 9),
          content: 'Hello! How can I help you today?',
          senderId: this.currentUserId,
          timestamp: new Date().toISOString()
        });
        this.messagesLoaded = true
      }, 1000)

      // user message
      setTimeout(() => {
        this.messages.push({
          _id: Math.random().toString(36).substr(2, 9),
          content: 'Hi! I have a question about your product.',
          senderId: 'user1',
          timestamp: new Date().toISOString()
        });
      }, 2000)
    }
  }
}
</script>
