import {Component} from '@angular/core';
import {AuthService} from "../../authentication/auth/auth.service";
import {Role, User} from "../../authentication/auth/user.model";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {

  protected readonly Role = Role;
  currentUser: User | null = null;

  constructor(private authService: AuthService) {
    this.authService.user.subscribe(
      (user) => {
        this.currentUser = user;
      }
    )
  }

  onLogout() {
    this.authService.logout();
  }

}
