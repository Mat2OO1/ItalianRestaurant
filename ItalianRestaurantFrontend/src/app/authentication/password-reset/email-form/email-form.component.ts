import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {TranslateService} from "@ngx-translate/core";
import {SnackbarService} from "../../../shared/sncakbar.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-email-form',
  templateUrl: './email-form.component.html',
  styleUrls: ['./email-form.component.css']
})
export class EmailFormComponent {
  emailForm: FormGroup;
  lang = ""
  processing = false;
  constructor(private authService: AuthService, private router: Router, private snackbarService: SnackbarService, private translate: TranslateService) {
    this.emailForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),

    })
  }

  onResetSubmit() {
    this.processing = true;
    this.lang = localStorage.getItem('lang')||'en'
    let email = this.emailForm.value['email'];
    this.authService.requestResetPassword(email, this.lang).subscribe(
      (res) => {
        this.translate.get('user_check_email').subscribe((message) => {
          this.snackbarService.openSnackbarSuccess(message);
          this.processing = false;
        });
        this.router.navigate(['']);
      },
      (err) => {
        this.translate.get('user_email_not_found').subscribe((message) => {
          this.snackbarService.openSnackbarError(message); // Corrected method
          this.processing = false;
        });
      }
    );
  }



}
