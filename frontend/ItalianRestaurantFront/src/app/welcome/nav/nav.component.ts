import { Component } from '@angular/core';
import {AuthService} from "../../authentication/auth/auth.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {
  constructor(private authService: AuthService) {
    this.authService.user.subscribe(
      (user) => {
        this.isLoggedIn = !!user.token;
      }
    )
  }
  isLoggedIn = false;

  onLogout(){
    this.isLoggedIn = false;
    this.authService.logout();
  }
}
