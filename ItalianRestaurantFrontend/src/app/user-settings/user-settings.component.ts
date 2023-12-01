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
      firstName: new FormControl({value: '', disabled: true}, [Validators.required]),
      lastName: new FormControl({value: '', disabled: true}, [Validators.required]),
      email: new FormControl({value: '', disabled: true}, [Validators.required, Validators.email]),
      phoneNumber: new FormControl({value: '', disabled: true}, [Validators.pattern('[0-9]{9}'), Validators.required]),
      newsletter: new FormControl({value: false, disabled: true}),
    })

    this.authService.getLoggedInUser().subscribe(user => {
      this.user = user;
      this.isLocalAccount = this.user.provider === 'local';
      this.enableFormInputs();
      this.accountForm.setValue({
        firstName: this.isLocalAccount ? user.firstName : user.username.split(' ')[0],
        lastName: this.isLocalAccount ? user.lastName : user.username.split(' ')[1],
        email: user.email,
        phoneNumber: user.phoneNumber,
        newsletter: user.newsletter
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
      phoneNumber: this.accountForm.value['phoneNumber'],
      newsletter: this.accountForm.value['newsletter'],
    }
    this.authService.updateUser(user)
      .subscribe(
        (resData) => {
          this.processing = false;
          this.authService.handleAuthentication(resData.token, new Date(resData.expiration).getTime(), resData.role);
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
        data: {email: this.user?.email},
        autoFocus: false
      }
    );
  }

  private enableFormInputs() {
    if (this.isLocalAccount) {
      this.accountForm.get('firstName')?.enable();
      this.accountForm.get('lastName')?.enable();
      this.accountForm.get('email')?.enable();
    }
    this.accountForm.get('phoneNumber')?.enable();
    this.accountForm.get('newsletter')?.enable();
  }
}
