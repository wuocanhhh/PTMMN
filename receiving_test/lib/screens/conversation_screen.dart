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
  final MethodChannelHandler _channelHandler = MethodChannelHandler();
  final EventChannelHandler _eventChannelHandler = EventChannelHandler();
  final ScrollController _scrollController = ScrollController();

  void _setUpEventListener() {
    _eventChannelHandler.setUpMessageEventListener(
      onMessageReceived: _onMessageReceived,
    );
  }

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
      final MessageModel sentMessage = await _channelHandler.sendMessage(
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

  Future<List<Map>> _getConversation(int conversationId) async {
    try {
      final List conversation =
          await _channelHandler.getConversation(conversationId);
      if (conversation.isEmpty) {
        print("There was no message loaded");
      }
      return List<Map>.from(conversation);
    } on PlatformException catch (e) {
      print("Failed to get conversation: '${e.message}'.");
      return [];
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
    conversation = widget.conversation;
    _getConversation(widget.conversation.conversationId).then((conversation) {
      if (conversation.isNotEmpty) {
        setState(() {
          // Map the fetched messages to the MessageModel and add them to the _messages list
          _messages.addAll(conversation.map((messageMap) => MessageModel(
                messageId: messageMap['messageId'],
                conversationId: messageMap['conversationId'],
                senderId: messageMap['senderId'],
                message: messageMap['message'],
                timestamp: messageMap['timestamp'],
              )));
        });
      }
    });
    _setUpEventListener();
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
