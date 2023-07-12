import 'package:flutter/material.dart';
import '../database/db_interface.dart';

class NewConversationScreen extends StatefulWidget {
  @override
  _NewConversationScreenState createState() => _NewConversationScreenState();
}

class _NewConversationScreenState extends State<NewConversationScreen> {
  final _formKey = GlobalKey<FormState>();
  String phoneNumber = "";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('New Conversation'),
      ),
      body: Form(
        key: _formKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            TextFormField(
              decoration: InputDecoration(
                labelText: "Phone Number",
              ),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please enter a phone number';
                }
                return null;
              },
              onSaved: (value) {
                phoneNumber = value ?? "";
              },
            ),
            ElevatedButton(
              onPressed: () {
                if (_formKey.currentState!.validate()) {
                  _formKey.currentState!.save();
                  FakeDatabase.createConversation(phoneNumber);
                  Navigator.pop(context);
                }
              },
              child: Text('Create Conversation'),
            ),
          ],
        ),
      ),
    );
  }
}
