import 'package:flutter/services.dart';

import 'models/conversation_model.dart';

class ChannelHandler {
  static const MethodChannel _methodChannel =
      const MethodChannel('com.example.receiving_test/method');

  static const EventChannel _eventChannel =
      const EventChannel('com.example.receiving_test/event');

  void setUpEventListener(Function(ConversationModel) onEventReceived) {
    _eventChannel.receiveBroadcastStream().listen((dynamic event) {
      if (event is Map) {
        final Map<String, dynamic> conversationMap =
            event.cast<String, dynamic>();
        final ConversationModel conversation = ConversationModel(
          conversationId: conversationMap['conversationId'],
          conversationName: conversationMap['conversationName'],
        );
        print('Received conversation from Kotlin: $conversation');
        onEventReceived(
            conversation); // Invoke the callback with the ConversationModel
      } else {
        print('Error: received event is not a Map');
      }
    }, onError: (dynamic error) {
      print('Error receiving conversation from Kotlin: ${error.message}');
    });
  }

  Future<void> sendMessage(String message, int conversationId) async {
    try {
      final Map<String, dynamic> sms = {
        'message': message,
        'conversationId': conversationId,
      };
      await _methodChannel.invokeMethod('sendSms', sms);
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
    }
  }

  Future<List<Map>> getConversation(int conversationId) async {
    try {
      final List conversation = await _methodChannel
          .invokeMethod('getConversation', <String, dynamic>{
        'conversationId': conversationId,
      });
      if (conversation.isEmpty) {
        print("There was no message loaded");
      }
      return List<Map>.from(conversation);
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
