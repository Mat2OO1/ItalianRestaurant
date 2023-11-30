import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup
  error: string = '';
  hide = true;
  processing = false;

  constructor(private router: Router,
              private authService: AuthService) {
    this.registerForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        this.passwordValidator()
      ]),
    })
  }

  onRegisterSubmit() {
    this.processing = true;
    let firstName = this.registerForm.value['firstName'];
    let lastName = this.registerForm.value['lastName'];
    let email = this.registerForm.value['email'];
    let password = this.registerForm.value['password'];
    this.authService.signup(firstName, lastName, email, password)
      .subscribe(
        resData => {
          this.router.navigate(['./menu'])
        },
        errorMessage => {
          this.registerForm.reset()
          this.error = errorMessage;
          this.processing = false;
        }
      )
  }
  passwordValidator() {
      return (control: FormControl): { [key: string]: any } | null => {
          const valid = /[^a-zA-Z0-9]/.test(control.value);
          return valid ? null : { 'specialCharacterRequired': true };
      };
  }
}
