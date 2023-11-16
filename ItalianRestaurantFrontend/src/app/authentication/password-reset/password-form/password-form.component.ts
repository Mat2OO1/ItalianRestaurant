import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {ActivatedRoute} from "@angular/router";
import {ToastService} from "../../../shared/toast.service";

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
              private toastService: ToastService) {
    this.token = this.route.snapshot.queryParamMap.get('token');
    this.passwordForm = new FormGroup({
      password: new FormControl('', [Validators.required]),
      repeatPassword: new FormControl('', [Validators.required]),
    })
  }

  onResetSubmit() {
    if (this.arePasswordsDifferent) {
      this.toastService.showErrorToast("Reset error", "Passwords are different");
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
