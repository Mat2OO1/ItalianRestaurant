import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../authentication/auth/auth.service";
import {UserDto} from "../models/user-dto";
import {MatDialog} from "@angular/material/dialog";
import {PasswordDialogComponent} from "./password-dialog/password-dialog.component";
import {Profile} from "../models/profile";
import {SnackbarService} from "../shared/sncakbar.service";
import {TranslateService} from "@ngx-translate/core";
import {DeleteConfirmationDialogComponent} from "./delete-confirmation-dialog/delete-confirmation-dialog.component";
import {Role} from "../authentication/auth/user.model";

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent implements OnInit {

  accountForm: FormGroup
  processing = true;
  userData?: Profile;
  isLocalAccount = true;
  userRole?: Role;

  constructor(private authService: AuthService,
              private dialog: MatDialog,
              private snackbarService: SnackbarService,
              private translate: TranslateService
  ) {

    this.accountForm = new FormGroup({
      firstName: new FormControl({value: '', disabled: true}, [Validators.required]),
      lastName: new FormControl({value: '', disabled: true}, [Validators.required]),
      email: new FormControl({value: '', disabled: true}, [Validators.required, Validators.email]),
      phoneNumber: new FormControl({value: '', disabled: true}, [Validators.pattern('[0-9]{9}'), Validators.required]),
      newsletter: new FormControl({value: false, disabled: true}),
    })

  }

  ngOnInit() {
    this.authService.getLoggedInUser().subscribe(user => {
      this.userData = user;
      this.isLocalAccount = this.userData.provider === 'local';
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

    this.authService.user.subscribe(user => {
        this.userRole = user?.role;
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
          this.translate.get('user_updated_successfully').subscribe((message) => {
            this.snackbarService.openSnackbarSuccess(message);
          });
        }
      )
  }

  isLargeScreen() {
    const width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    return width > 770;
  }

  openPasswordDialog() {
    this.dialog.open(PasswordDialogComponent, {
        data: {provider: this.userData?.provider},
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

  openDeleteAccountDialog() {
    let dialogRef = this.dialog.open(DeleteConfirmationDialogComponent,
      {
        autoFocus: false
      });
    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(result);
    });
  }

  handleDialogResult(result: { delete: boolean, password?: string }) {
    if (result.delete && result.password) {
      this.authService.deleteUser(result.password).subscribe(() => {
        this.translate.get('user_deleted_successfully').subscribe((message) => {
          this.snackbarService.openSnackbarSuccess(message);
        });
        this.authService.logout();
      })
    }
  }

  protected readonly Role = Role;
}
