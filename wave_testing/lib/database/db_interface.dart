import '/models/conversation_model.dart';
import '/models/message_model.dart';
import '/models/user_model.dart';
import '/models/participant_model.dart';

class FakeDatabase {
  // Tables
  static List<UserModel> users = [
    UserModel(userId: _userIdCounter++, phoneNumber: "me"),
    UserModel(userId: _userIdCounter++, phoneNumber: "098-765-4321"),
  ];

  static List<ConversationModel> conversations = [
    ConversationModel(
        conversationId: _conversationIdCounter++,
        conversationName: "Work chat"),
    ConversationModel(
        conversationId: _conversationIdCounter++,
        conversationName: "Family chat"),
  ];

  static List<ParticipantModel> participants = [
    ParticipantModel(
        participantId: _participantIdCounter++, conversationId: 1, userId: 1),
    ParticipantModel(
        participantId: _participantIdCounter++, conversationId: 2, userId: 1),
    ParticipantModel(
        participantId: _participantIdCounter++, conversationId: 2, userId: 2),
  ];

  static List<MessageModel> messages = [
    MessageModel(
      messageId: _messageIdCounter++,
      conversationId: 1,
      senderId: 1,
      sender: "me",
      messageContent: "Hello",
      timestampSent: DateTime.now().toString(),
      timestampReceived: DateTime.now().toString(),
    ),
    MessageModel(
      messageId: _messageIdCounter++,
      conversationId: 2,
      senderId: 2,
      sender: "098-765-4321",
      messageContent: "Hi",
      timestampSent: DateTime.now().toString(),
      timestampReceived: DateTime.now().toString(),
    ),
  ];

  // To mimic autoincrement feature in sqlite, we use these counters
  static int _userIdCounter = 1;
  static int _conversationIdCounter = 1;
  static int _messageIdCounter = 1;
  static int _participantIdCounter = 1;

  static List<ConversationModel> getAllConversations() {
    return conversations;
  }

  static List<MessageModel> getMessagesFromConversation(int conversationId) {
    return messages
        .where((message) => message.conversationId == conversationId)
        .toList();
  }

  static createConversation(String phoneNumber) {
    var newConversation = ConversationModel(
        conversationId: _conversationIdCounter++,
        conversationName: phoneNumber);
    conversations.add(newConversation);
  }

  static List<MessageModel> getAllMessagesFromConversation(int conversationId) {
    return messages
        .where((message) => message.conversationId == conversationId)
        .toList();
  }

  static void addMessage(MessageModel newMessage) {
    // Increment the messageId counter and assign it to the new message
    newMessage.messageId = _messageIdCounter++;

    // Add the new message to the list
    messages.add(newMessage);
  }
}
