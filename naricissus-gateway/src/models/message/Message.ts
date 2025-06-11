export enum MessageEnum {REQUEST, RESPONSE}

export interface Message {
    messageType: MessageEnum;
    source: String;
    uri: String;
    payload: Object
}

export default Message;