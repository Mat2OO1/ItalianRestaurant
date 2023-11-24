import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {ActivatedRoute} from "@angular/router";
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

  constructor(private authService: AuthService,
              private route: ActivatedRoute,
              private snackbarService: SnackbarService,
              private translate: TranslateService) {
    this.token = this.route.snapshot.queryParamMap.get('token');
    this.passwordForm = new FormGroup({
      password: new FormControl('', [Validators.required]),
      repeatPassword: new FormControl('', [Validators.required]),
    })
  }

  onResetSubmit() {
    if (this.arePasswordsDifferent) {
      this.translate.get('password_same_error').subscribe((message) => {
        this.snackbarService.openSnackbarError(message);
      });
      return;
    }
    if (this.passwordForm.invalid || this.token === null) return;
    let password = this.passwordForm.value['repeatPassword'];
    this.authService.resetPassword(password, this.token)
  }

  get arePasswordsDifferent() {
    return this.passwordForm.get('password')?.value !== this.passwordForm.get("repeatPassword")?.value
  }
}
