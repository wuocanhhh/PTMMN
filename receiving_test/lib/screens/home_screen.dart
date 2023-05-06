import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:receiving_test/screens/conversation_screen.dart';

import '../channels_handler.dart';
import '../models/conversation_model.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final List<ConversationModel> _conversations = [];
  final ChannelHandler _channelHandler = ChannelHandler();
  final TextEditingController _textEditingController = TextEditingController();

  void _setUpEventListener() {
    _channelHandler.setUpEventListener(_onEventReceived);
  }

  void _onEventReceived(ConversationModel conversation) {
    print("A new conversation was created, is the UI updating?"); //! temporary
    setState(() {
      _conversations.add(conversation);
    });
  }

  Future<void> _fetchConversations() async {
    final List<ConversationModel>? conversations =
        await _channelHandler.fetchConversations();

    if (conversations != null) {
      setState(() {
        _conversations.clear();
        _conversations.addAll(conversations);
      });

      for (var conversation in conversations) {
        print(
            "Conversation with id: ${conversation.conversationId} and name: ${conversation.conversationName} has been loaded.");
      }
      if (conversations.isEmpty) {
        print("No conversations were loaded");
      }
    } else {
      print("Failed to fetch conversations.");
    }
  }

  Future<void> _createConversation(String phoneNumber) async {
    try {
      final ConversationModel? newConversation =
          await _channelHandler.createConversation(phoneNumber);
      if (newConversation != null) {
        // Add the conversationId to the _conversationIds list and trigger a rebuild
        setState(() {
          _conversations.add(newConversation);
        });
      } else {
        print("Failed to create conversation.");
      }
    } on PlatformException catch (e) {
      print("Failed to create conversation: '${e.message}'.");
    }
  }

  @override
  void initState() {
    super.initState();
    _setUpEventListener();
    _fetchConversations();
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
            'SMS Conversations',
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.w500,
            ),
          ),
        ),
        backgroundColor: Color(0xFF2C2C2E), // Set the background color
        body: Column(
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: TextField(
                controller: _textEditingController,
                decoration: InputDecoration(
                  labelText: 'Enter phone number',
                  labelStyle: TextStyle(color: Colors.white),
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.white),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.blue),
                  ),
                ),
                style: TextStyle(color: Colors.white),
                keyboardType: TextInputType.phone,
                onTap: () {
                  FocusScope.of(context).requestFocus();
                },
              ),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: _conversations.length,
                itemBuilder: (BuildContext context, int index) {
                  return ListTile(
                    title: Text(
                      '${_conversations[index].conversationName}',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w400,
                        color: Colors.white,
                      ),
                    ),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => ConversationScreen(
                              conversationId:
                                  _conversations[index].conversationId),
                        ),
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            if (_textEditingController.text.isNotEmpty) {
              _createConversation(_textEditingController.text);
              _textEditingController.clear();
            }
          },
          child: Icon(Icons.add),
          backgroundColor: Colors.blue,
        ),
      ),
    );
  }
}
