import 'package:flutter/material.dart';
import '../models/message_model.dart';
import '../database/db_interface.dart';

class MessageListWidget extends StatelessWidget {
  final List<MessageModel> messageList;

  MessageListWidget({required this.messageList});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: messageList.length,
      itemBuilder: (context, index) {
        return ListTile(
          leading: Text(messageList[index].sender!),
          title: Text(messageList[index].messageContent),
          subtitle: Text(messageList[index].timestampSent),
        );
      },
    );
  }
}
