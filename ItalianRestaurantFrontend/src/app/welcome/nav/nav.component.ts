import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../authentication/auth/auth.service";
import {Role, User} from "../../authentication/auth/user.model";
import {TranslateService} from "@ngx-translate/core";
import {CartService} from "../../shared/cart.service";

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
  constructor(private authService: AuthService,
              private translateService:TranslateService,
              private cartService: CartService) {
    this.authService.user.subscribe(
      (user) => {
        this.currentUser = user;
      }
    )
  }

  onLogout() {
    this.authService.logout();
  }

  changeLang(selectedLanguage: string){
    setTimeout(() => {
      this.lang = selectedLanguage;
      localStorage.setItem('lang', selectedLanguage);
      this.translateService.use(selectedLanguage);
    },200)
  }

  get cartItemsCount(){
    return this.cartService.getCartItemsCount();
  }
}
