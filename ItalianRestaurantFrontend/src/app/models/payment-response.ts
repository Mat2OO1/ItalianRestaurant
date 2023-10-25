export class PaymentResponse {
  sessionId: string;
  url: string;

  constructor(sessionId: string, url: string) {
    this.sessionId = sessionId;
    this.url = url;
  }
}
