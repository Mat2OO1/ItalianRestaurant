export class Delivery {
  address: string
  city: string
  postalCode: string
  floor: string
  info: string
  deliveryOptions: string


  constructor(address: string, city: string, postalCode: string, floor: string, info: string, deliveryOptions: string) {
    this.address = address;
    this.city = city;
    this.postalCode = postalCode;
    this.floor = floor;
    this.info = info;
    this.deliveryOptions = deliveryOptions;
  }

}
