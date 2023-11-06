import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as AOS from "aos";
import {AuthService} from "./authentication/auth/auth.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  constructor(private authService: AuthService, private translateService:TranslateService) {
    this.translateService.setDefaultLang('en');
    this.translateService.use(localStorage.getItem('lang')||'en');
  }
  ngAfterViewInit() {
    AOS.init();
  }

  ngOnInit() {
    this.authService.autoLogin();
  }
}
