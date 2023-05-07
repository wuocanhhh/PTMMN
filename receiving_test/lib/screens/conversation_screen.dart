import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:receiving_test/event_channel_handler.dart';
import 'package:receiving_test/method_channel_handler.dart';
import 'package:receiving_test/models/conversation_model.dart';
import 'package:receiving_test/models/message_model.dart';

class ConversationScreen extends StatefulWidget {
  final ConversationModel conversation;

  ConversationScreen({required this.conversation});

  @override
  _ConversationScreenState createState() => _ConversationScreenState();
}

class _ConversationScreenState extends State<ConversationScreen> {
  final List<MessageModel> _messages = [];
  final TextEditingController _messageController = TextEditingController();
  late ConversationModel conversation;
  final MethodChannelHandler _methodChannelHandler = MethodChannelHandler();
  final EventChannelHandler _eventChannelHandler = EventChannelHandler();
  final ScrollController _scrollController = ScrollController();

  void _onMessageReceived(MessageModel message) {
    setState(() {
      _messages.insert(0, message);
    });
    _scrollController.animateTo(
      0.0,
      curve: Curves.easeOut,
      duration: const Duration(milliseconds: 300),
    );
  }

  Future<void> _sendMessage(String message) async {
    try {
      final MessageModel sentMessage = await _methodChannelHandler.sendMessage(
          message, conversation.conversationId);

      // Update the _conversations list with the sent message
      setState(() {
        _messages.insert(0, sentMessage);
      });
      _scrollController.animateTo(
        0.0,
        curve: Curves.easeOut,
        duration: const Duration(milliseconds: 300),
      );
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
    }
  }

  Widget _buildMessageList() {
    return ListView.builder(
      reverse: true,
      controller: _scrollController,
      itemCount: _messages.length,
      itemBuilder: (BuildContext context, int index) {
        final MessageModel message = _messages[index];

        // Convert the timestamp to a DateTime object
        DateTime timestamp =
            DateTime.fromMillisecondsSinceEpoch(int.parse(message.timestamp));
        String formattedTimestamp =
            DateFormat('MMM d, y h:mm a').format(timestamp);

        return ListTile(
          title: Text(
            message.message,
            style: const TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.w400,
              color: Colors.white,
            ),
          ),
          subtitle: Text(
            formattedTimestamp,
            style: const TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w300,
              color: Colors.white70,
            ),
          ),
        );
      },
    );
  }

  @override
  void initState() {
    super.initState();

    _eventChannelHandler.setUpMessageEventListener(
      onMessageReceived: _onMessageReceived,
    );

    () async {
      final List<MessageModel> messageList = await _methodChannelHandler
          .getConversation(widget.conversation.conversationId);
      setState(() {
        _messages.addAll(messageList);
      });
    }();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        FocusScope.of(context).unfocus();
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text(
            widget.conversation.conversationName.toString(),
            style: const TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.w500,
            ),
          ),
        ),
        backgroundColor: const Color(0xFF2C2C2E), // Set the background color
        body: Column(
          children: <Widget>[
            Expanded(child: _buildMessageList()),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  Expanded(
                    child: TextField(
                      controller: _messageController,
                      decoration: const InputDecoration(
                        hintText: 'Type a message',
                        hintStyle: TextStyle(color: Colors.white),
                      ),
                      style: const TextStyle(color: Colors.white),
                      onTap: () {
                        FocusScope.of(context).requestFocus();
                      },
                    ),
                  ),
                  IconButton(
                    icon: const Icon(
                      Icons.send,
                      color: Colors.blue,
                    ),
                    onPressed: () {
                      if (_messageController.text.isNotEmpty) {
                        _sendMessage(_messageController.text);
                        _messageController.clear();
                      }
                    },
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
