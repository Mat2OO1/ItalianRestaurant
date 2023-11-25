import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-email-form',
  templateUrl: './email-form.component.html',
  styleUrls: ['./email-form.component.css']
})
export class EmailFormComponent {
  emailForm: FormGroup;
  lang = ""
  constructor(private authService: AuthService) {
    this.emailForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),

    })
  }

  onResetSubmit() {
    this.lang = localStorage.getItem('lang')||'en'
    let email = this.emailForm.value['email'];
    this.authService.requestResetPassword(email, this.lang);
  }



}
