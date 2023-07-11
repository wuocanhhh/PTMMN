import 'package:flutter/services.dart';
import 'package:receiving_test/models/message_model.dart';
import 'models/conversation_model.dart';

class MethodChannelHandler {
  static const MethodChannel _methodChannel =
      const MethodChannel('com.example.receiving_test/method');

  Future<MessageModel> getNewMessages(String message, int conversationId) async {
    try {
      
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
      throw e;
    }
  }

  // Inside MethodChannelHandler class
  Future<MessageModel> sendSms(String message, int conversationId) async {
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

  Future<MessageModel> deleteAllTemporaryMessages(String message, int conversationId) async {
    try {
      
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
      throw e;
    }
  }
}
