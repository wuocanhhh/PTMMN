class MessageModelWithSender {
  int? messageId;
  int? conversationId;
  int? senderId;
  String sender;
  String messageContent;
  String timestampSent;
  String timestampReceived;

  MessageModelWithSender({
    this.messageId,
    this.conversationId,
    this.senderId,
    required this.sender,
    required this.messageContent,
    required this.timestampSent,
    required this.timestampReceived,
  });
}

class MessageModelWithIds {
  int messageId;
  int conversationId;
  int senderId;
  String? sender;
  String messageContent;
  String timestampSent;
  String timestampReceived;

  MessageModelWithIds({
    required this.messageId,
    required this.conversationId,
    required this.senderId,
    this.sender,
    required this.messageContent,
    required this.timestampSent,
    required this.timestampReceived,
  });
}
