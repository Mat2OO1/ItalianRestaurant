import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth.service";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  resetPasswordForm: FormGroup;

  constructor(private authService: AuthService) {
    this.resetPasswordForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),

    })
  }

  onResetSubmit(){
    let email = this.resetPasswordForm.value['email'];
    this.authService.resetPassword(email);
  }

}
