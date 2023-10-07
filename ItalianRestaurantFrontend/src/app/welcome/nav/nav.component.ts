import {Component} from '@angular/core';
import {AuthService} from "../../authentication/auth/auth.service";
import {Role} from "../../authentication/auth/user.model";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {

  protected readonly Role = Role;
  isLoggedIn = false;
  role?: Role;

  constructor(private authService: AuthService) {
    this.authService.user.subscribe(
      (user) => {
        this.isLoggedIn = !!user.token;
        this.role = user.role;
      }
    )
  }
  onLogout() {
    this.isLoggedIn = false;
    this.role = undefined;
    this.authService.logout();
  }
}
