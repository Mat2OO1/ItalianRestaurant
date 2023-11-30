import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../authentication/auth/auth.service";
import {UserDto} from "../models/user-dto";
import {MatDialog} from "@angular/material/dialog";
import {PasswordDialogComponent} from "./password-dialog/password-dialog.component";

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent {

  accountForm: FormGroup
  processing = true;
  user?: UserDto;

  constructor(private authService: AuthService,
              private dialog: MatDialog
  ) {
    this.accountForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      phoneNumber: new FormControl('', [Validators.pattern('[0-9]{9}'), Validators.required]),
    })

    this.authService.getLoggedInUser().subscribe(user => {
      this.accountForm.setValue({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phoneNumber: user.phoneNumber
      })
      this.user = user;
      this.processing = false;
    })
  }

  onSubmit() {
    this.processing = true;
    let user: UserDto = {
      firstName: this.accountForm.value['firstName'],
      lastName: this.accountForm.value['lastName'],
      email: this.accountForm.value['email'],
      phoneNumber: this.accountForm.value['phoneNumber']
    }
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

  openPasswordDialog() {
    this.dialog.open(PasswordDialogComponent, {
        data: {email: this.user?.email}
      }
    );
  }

}
