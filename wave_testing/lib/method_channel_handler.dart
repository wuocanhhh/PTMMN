import 'package:flutter/services.dart';
import 'package:receiving_test/models/message_model.dart';
import 'models/conversation_model.dart';

class MethodChannelHandler {
  static const MethodChannel _methodChannel =
      const MethodChannel('com.example.receiving_test/method');

  // Inside MethodChannelHandler class
  Future<MessageModel> sendMessage(String message, int conversationId) async {
    try {
      final Map<String, dynamic> sms = {
        'message': message,
        'conversationId': conversationId,
      };
      final Map<String, dynamic> result = (await _methodChannel
              .invokeMethod<Map<dynamic, dynamic>>('sendSms', sms))!
          .cast<String,
              dynamic>(); // Add this line to cast the result to the correct type

      // Assuming the result map contains messageId, senderId, and timestamp
      return MessageModel(
          messageId: result['messageId'],
          conversationId: conversationId,
          senderId: result['senderId'],
          message: message,
          timestamp: result['timestamp']);
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
      throw e;
    }
  }

  Future<List<MessageModel>> getConversation(int conversationId) async {
    try {
      final List conversationMap = await _methodChannel
          .invokeMethod('getConversation', <String, dynamic>{
        'conversationId': conversationId,
      });
      var conversation = conversationMap
          .map((messageMap) => MessageModel(
                messageId: messageMap['messageId'],
                conversationId: messageMap['conversationId'],
                senderId: messageMap['senderId'],
                message: messageMap['message'],
                timestamp: messageMap['timestamp'],
              ))
          .toList();
      if (conversation.isEmpty) {
        print("There was no message loaded");
      }
      return conversation;
    } on PlatformException catch (e) {
      print("Failed to get conversation: '${e.message}'.");
      return [];
    }
  }

  Future<List<ConversationModel>?> fetchConversations() async {
    try {
      final List<dynamic> conversationsList =
          await _methodChannel.invokeMethod('getAllConversations');
      final List<ConversationModel> conversations =
          conversationsList.map((conversationMap) {
        return ConversationModel(
          conversationId: conversationMap['conversationId'],
          conversationName: conversationMap['conversationName'],
        );
      }).toList();

      for (var conversation in conversations) {
        print(
            "Conversation with id: ${conversation.conversationId} and name: ${conversation.conversationName} has been loaded.");
      }
      if (conversations.isEmpty) {
        print("No conversations were loaded");
      }
      return conversations;
    } on PlatformException catch (e) {
      print("Failed to fetch conversations: '${e.message}'.");
      return null;
    }
  }

  Future<ConversationModel?> createConversation(String phoneNumber) async {
    try {
      // Create a new user and get the userId
      final int userId =
          await _methodChannel.invokeMethod('createNewUser', <String, dynamic>{
        'phoneNumber': phoneNumber,
      });

      // Create a new conversation and get the conversationId
      final int conversationId = await _methodChannel
          .invokeMethod('createNewConversation', <String, dynamic>{
        'conversationName': phoneNumber,
      });
      final String conversationName = phoneNumber;
      final ConversationModel newConversation = ConversationModel(
          conversationId: conversationId, conversationName: conversationName);

      // Create a new participant using the userId and conversationId
      await _methodChannel
          .invokeMethod('createNewParticipant', <String, dynamic>{
        'userId': userId,
        'conversationId': conversationId,
      });
      return newConversation;
    } on PlatformException catch (e) {
      print("Failed to create conversation: '${e.message}'.");
      return null;
    }
  }
}
