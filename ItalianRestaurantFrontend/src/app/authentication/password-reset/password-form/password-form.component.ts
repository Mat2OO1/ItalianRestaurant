import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SnackbarService} from "../../../shared/sncakbar.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-password-form',
  templateUrl: './password-form.component.html',
  styleUrls: ['./password-form.component.css']
})
export class PasswordFormComponent {
  passwordForm: FormGroup;
  token: string | null;
  processing = false;

  constructor(private authService: AuthService,
              private route: ActivatedRoute,
              private snackbarService: SnackbarService,
              private translate: TranslateService,
              private router: Router) {
    this.token = this.route.snapshot.queryParamMap.get('token');
    this.passwordForm = new FormGroup({
      password: new FormControl('', [Validators.required, Validators.minLength(8), this.passwordValidator()]),
      repeatPassword: new FormControl('', [Validators.required, Validators.minLength(8), this.passwordValidator()]),
    })
  }
  passwordValidator() {
    return (control: FormControl): { [key: string]: any } | null => {
      const valid = /[^a-zA-Z0-9]/.test(control.value);
      return valid ? null : { 'specialCharacterRequired': true };
    };
  }

  onResetSubmit() {
    this.processing = true;
    if (this.arePasswordsDifferent) {
      this.translate.get('password_same_error').subscribe((message) => {
        this.snackbarService.openSnackbarError(message);
        this.processing = false;
      });
      return;
    }
    if (this.passwordForm.invalid || this.token === null) return;
    let password = this.passwordForm.value['repeatPassword'];
    this.authService.resetPassword(password, this.token).subscribe(
      () => {
        this.router.navigate([''])
        this.translate.get('user_password_reset').subscribe((message) => {
          this.snackbarService.openSnackbarSuccess(message);
          this.processing = false;
        });
      }
    );
  }

  get arePasswordsDifferent() {
    return this.passwordForm.get('password')?.value !== this.passwordForm.get("repeatPassword")?.value
  }
}
