export class UserDto {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  newsletter: boolean;

  constructor(firstName: string, lastName: string, email: string, phoneNumber: string, newsletter: boolean) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.newsletter = newsletter;
  }
}
