import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../authentication/auth/auth.service";
import {Role, User} from "../../authentication/auth/user.model";
import {TranslateService} from "@ngx-translate/core";
import {CartService} from "../../shared/cart.service";
import {Meta} from '@angular/platform-browser';
@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit{
  ngOnInit() {
    this.meta.addTag({ name: 'description', content: 'LaDolcVita Restaurant' });
    this.meta.addTag({ name: 'viewport', content: 'width=device-width, initial-scale=1' });
    this.meta.addTag({ property: 'og:title', content: 'La dolce vita' });
    this.meta.addTag({ property: 'og:type', content: 'website' });
    this.meta.addTag({ property: 'og:url', content: 'https://ladolcevitarestaurant.live' });
    this.meta.addTag({ property: 'og:image', content: 'https://italianrestaurantimages.s3.eu-north-1.amazonaws.com/image_aNISMg' });
    this.meta.addTag({ property: 'og:description', content: 'Website for ordering food from restaurant' });
    this.meta.addTag({ property: 'og:site_name', content: 'La dolce vita' });
    this.meta.addTag({ property: 'og:locale', content: 'en_US,pl_US' });
    this.lang = localStorage.getItem('lang') || 'en'
  }

  protected readonly Role = Role;
  currentUser: User | null = null;
  lang ="";
  constructor(private authService: AuthService,
              private translateService:TranslateService,
              private cartService: CartService,
              private meta: Meta) {
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
