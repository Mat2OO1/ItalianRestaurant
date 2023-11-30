export class UserDto {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;

  constructor(firstName: string, lastName: string, email: string, phoneNumber: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}
