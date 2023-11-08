import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../authentication/auth/auth.service";
import {Role, User} from "../../authentication/auth/user.model";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit{
  ngOnInit() {
    this.lang = localStorage.getItem('lang') || 'en'
  }

  protected readonly Role = Role;
  currentUser: User | null = null;
  lang ="";
  constructor(private authService: AuthService, private translateService:TranslateService) {
    this.authService.user.subscribe(
      (user) => {
        this.currentUser = user;
      }
    )
  }

  onLogout() {
    this.authService.logout();
  }

  ChangeLang(lang:any){
    const selectedLanguage = lang.target.value;
    localStorage.setItem('lang', selectedLanguage);
    this.translateService.use(selectedLanguage);
  }

}
