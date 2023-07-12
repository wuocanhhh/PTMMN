import 'package:flutter/material.dart';
import '/database/db_interface.dart';
import '/models/conversation_model.dart';
import '/screens/conversation_screen.dart';

class ConversationListWidget extends StatefulWidget {
  @override
  _ConversationListWidgetState createState() => _ConversationListWidgetState();
}

class _ConversationListWidgetState extends State<ConversationListWidget> {
  List<ConversationModel> conversations = [];

  @override
  void initState() {
    super.initState();
    fetchData();
  }

  Future<void> fetchData() async {
    List<ConversationModel> data = await FakeDatabase.getAllConversations();
    setState(() {
      conversations = data;
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: conversations.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(conversations[index].conversationName),
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => ConversationScreen(conversationId: conversations[index].conversationId)),
            );
          },
        );
      },
    );
  }
}
