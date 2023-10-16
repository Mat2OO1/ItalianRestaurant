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

  constructor(private router: Router,
              private authService: AuthService) {
    this.registerForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  onRegisterSubmit() {
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
        }
      )
  }

}
