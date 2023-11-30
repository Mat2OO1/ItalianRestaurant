import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../authentication/auth/auth.service";
import {UserDto} from "../models/user-dto";
import {MatDialog} from "@angular/material/dialog";
import {PasswordDialogComponent} from "./password-dialog/password-dialog.component";
import {Profile} from "../models/profile";
import {SnackbarService} from "../shared/sncakbar.service";

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent {

  accountForm: FormGroup
  processing = true;
  user?: Profile;
  isLocalAccount = true;

  constructor(private authService: AuthService,
              private dialog: MatDialog,
              private snackbarService: SnackbarService,
  ) {
    this.accountForm = new FormGroup({
      firstName: new FormControl({value: '', disabled: !this.isLocalAccount}, [Validators.required]),
      lastName: new FormControl({value: '', disabled: !this.isLocalAccount}, [Validators.required]),
      email: new FormControl({value: '', disabled: !this.isLocalAccount}, [Validators.required, Validators.email]),
      phoneNumber: new FormControl('', [Validators.pattern('[0-9]{9}'), Validators.required]),
    })

    this.authService.getLoggedInUser().subscribe(user => {
      this.user = user;
      this.isLocalAccount = this.user.provider === 'local';
      this.accountForm.setValue({
        firstName: this.isLocalAccount ? user.firstName : user.username.split(' ')[0],
        lastName: this.isLocalAccount ? user.lastName : user.username.split(' ')[1],
        email: user.email,
        phoneNumber: user.phoneNumber,
      })
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
          this.snackbarService.openSnackbarSuccess('user_updated_successfully');
        },
        errorMessage => {
          this.snackbarService.openSnackbarError(errorMessage);
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
