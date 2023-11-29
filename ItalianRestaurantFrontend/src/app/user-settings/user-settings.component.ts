import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../authentication/auth/auth.service";
import {UserDto} from "../models/user-dto";

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent {

  accountForm: FormGroup
  processing = true;

  constructor(private router: Router,
              private authService: AuthService) {
    this.accountForm = new FormGroup({
      firstname: new FormControl('', [Validators.required]),
      lastname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      phoneNumber: new FormControl('', [Validators.pattern('[0-9]{9}'), Validators.required]),
    })

    this.authService.getLoggedInUser().subscribe(user => {
      this.accountForm.setValue({
        firstname: user.firstName,
        lastname: user.lastName,
        email: user.email,
        phoneNumber: user.phoneNumber
      })
      this.processing = false;
    })
  }

  onSubmit() {
    this.processing = true;
    let user: UserDto = {
      firstname: this.accountForm.value['firstname'],
      lastname: this.accountForm.value['lastname'],
      email: this.accountForm.value['email'],
      phoneNumber: this.accountForm.value['phoneNumber']
    }
    console.log(user)
    this.authService.updateUser(user)
      .subscribe(
        () => {
          this.processing = false;
        },
        errorMessage => {
          this.accountForm.reset()
          this.processing = false;
        }
      )
  }

  isLargeScreen() {
    const width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    return width > 770;
  }

}
