import 'package:flutter/services.dart';
import 'package:receiving_test/models/message_model.dart';
import 'models/conversation_model.dart';

class EventChannelHandler {
  static const EventChannel _eventChannel =
      const EventChannel('com.example.receiving_test/event');

  void setUpMessageEventListener({
    required Function(MessageModel) onMessageReceived,
  }) {
    _eventChannel.receiveBroadcastStream().listen((dynamic event) {
      if (event is Map) {
        final Map<String, dynamic> eventData = event.cast<String, dynamic>();

        if (eventData.containsKey('messageId') &&
            eventData.containsKey('conversationId') &&
            eventData.containsKey('senderId') &&
            eventData.containsKey('message') &&
            eventData.containsKey('timestamp')) {
          final MessageModel message = MessageModel(
            messageId: eventData['messageId'],
            conversationId: eventData['conversationId'],
            senderId: eventData['senderId'],
            message: eventData['message'],
            timestamp: eventData['timestamp'],
          );
          print('Received message from Kotlin: $message');
          onMessageReceived(message);
        } else {
          print('Ignoring conversation event data');
        }
      } else {
        print('Error: received event is not a Map');
      }
    }, onError: (dynamic error) {
      print('Error receiving data from Kotlin: ${error.message}');
    });
  }
}
