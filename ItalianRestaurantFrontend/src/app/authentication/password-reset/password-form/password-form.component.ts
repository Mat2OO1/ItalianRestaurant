import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {ActivatedRoute} from "@angular/router";
import {SnackbarService} from "../../../shared/sncakbar.service";

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
              private snackbarService: SnackbarService) {
    this.token = this.route.snapshot.queryParamMap.get('token');
    this.passwordForm = new FormGroup({
      password: new FormControl('', [Validators.required]),
      repeatPassword: new FormControl('', [Validators.required]),
    })
  }

  onResetSubmit() {
    if (this.arePasswordsDifferent) {
      this.snackbarService.openSnackbarError("Passwords are not the same");
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
