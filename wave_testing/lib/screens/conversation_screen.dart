import 'package:flutter/material.dart';
import '../models/message_model.dart';
import '../widgets/message_list_widget.dart';
import '../database/db_interface.dart';

class ConversationScreen extends StatefulWidget {
  final int conversationId;

  ConversationScreen({required this.conversationId});

  @override
  _ConversationScreenState createState() => _ConversationScreenState();
}

class _ConversationScreenState extends State<ConversationScreen> {
  final TextEditingController _textController = TextEditingController();
  List<MessageModel> _messageList = [];

  @override
  void initState() {
    super.initState();
    _fetchData();
  }

  void _fetchData() {
    _messageList =
        FakeDatabase.getAllMessagesFromConversation(widget.conversationId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Conversation ${widget.conversationId}'),
      ),
      body: Column(
        children: <Widget>[
          Expanded(child: MessageListWidget(messageList: _messageList)),
          Container(
            padding: EdgeInsets.symmetric(horizontal: 8.0),
            child: Row(
              children: <Widget>[
                Expanded(
                  child: TextField(
                    controller: _textController,
                    decoration: InputDecoration(
                      hintText: "Enter Message",
                    ),
                  ),
                ),
                IconButton(
                  icon: Icon(Icons.send),
                  onPressed: () async {
                    // Create the new message
                    MessageModel newMessage = MessageModel(
                      conversationId: widget.conversationId,
                      senderId: 1,
                      sender: 'me',
                      messageContent: _textController.text,
                      timestampSent: DateTime.now().toIso8601String(),
                      timestampReceived: DateTime.now().toIso8601String(),
                    );

                    // Add the message to the database
                    FakeDatabase.addMessage(newMessage);

                    // Add the message to the list
                    setState(() {
                      _messageList.add(newMessage);
                    });

                    // Clear the text field
                    _textController.clear();
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
