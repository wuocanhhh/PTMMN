import 'package:flutter/material.dart';
import '../widgets/conversation_list_widget.dart';
import '../screens/new_conversation_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('SMS Conversations'),
      ),
      body: ConversationListWidget(),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => NewConversationScreen()),
          ).then((_) {
            setState(() {
              // This will refresh the state of the HomeScreen widget and call the build method again.
            });
          });
        },
        child: Icon(Icons.add),
        backgroundColor: Colors.blue,
      ),
    );
  }
}
