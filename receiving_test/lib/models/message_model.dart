class MessageModel {
  late int messageId;
  late int conversationId;
  late int senderId;
  late String message;
  late String timestamp;

  MessageModel(
      {required this.messageId,
      required this.conversationId,
      required this.senderId,
      required this.message,
      required this.timestamp});
}
