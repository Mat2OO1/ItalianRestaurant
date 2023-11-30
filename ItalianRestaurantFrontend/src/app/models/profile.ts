export class Profile {

  id: number;
  email: string;
  firstName: string;
  lastName: string;
  username: string;
  imageUrl: string;
  phoneNumber: string;
  emailVerified: boolean;
  role: string;
  provider: string;

  constructor(id: number, email: string, firstName: string, lastName: string, username: string, imageUrl: string, phoneNumber: string, emailVerified: boolean, role: string, provider: string) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.imageUrl = imageUrl;
    this.phoneNumber = phoneNumber;
    this.emailVerified = emailVerified;
    this.role = role;
    this.provider = provider;
  }

  }
