export class PasswordDto {
  currentPassword: string;
  newPassword: string;
  email: string;

  constructor(currentPassword: string, newPassword: string, email: string) {
    this.currentPassword = currentPassword;
    this.newPassword = newPassword;
    this.email = email;
  }
}
