// import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';
// import 'package:receiving_test/screens/home_screen.dart';

// void main() {
//   runApp(MySmsApp());

//   const platform = const MethodChannel('com.example.receiving_test');
//   platform.setMethodCallHandler((MethodCall call) async {
//     switch (call.method) {
//       case "receivedSms":
//         _handleReceivedSms(call.arguments);
//         break;
//     }
//   });
// }

// void _handleReceivedSms(dynamic arguments) async {
//   // Instead of processing the received SMS here in Dart, we just print the details.
//   // The Kotlin side of the app is now responsible for inserting the SMS into the database.
//   print('Received SMS: $arguments');
// }

// class MySmsApp extends StatelessWidget {
//   @override
//   Widget build(BuildContext context) {
//     return MaterialApp(
//       title: 'My SMS App',
//       theme: ThemeData(
//         primarySwatch: Colors.blue,
//         visualDensity: VisualDensity.adaptivePlatformDensity,
//       ),
//       home: HomeScreen(),
//     );
//   }
// }
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'My App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Home Page'),
      ),
      body: Center(
        child: Text(
          'Welcome to my app!',
          style: TextStyle(fontSize: 24),
        ),
      ),
    );
  }
}
