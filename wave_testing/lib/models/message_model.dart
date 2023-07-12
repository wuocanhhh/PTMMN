class MessageModel {
  int? messageId;
  int? conversationId;
  int? senderId;
  String? sender;
  String messageContent;
  String timestampSent;
  String timestampReceived;

  MessageModel({
    this.messageId,
    this.conversationId,
    this.senderId,
    required this.sender,
    required this.messageContent,
    required this.timestampSent,
    required this.timestampReceived,
  });
}
