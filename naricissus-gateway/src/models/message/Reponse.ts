import { MessageEnum } from "./Message";

export interface Response{
    messageType: MessageEnum;
    errorMessage: String;
    payload: Object
}

export default Response;