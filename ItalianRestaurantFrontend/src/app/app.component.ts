import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as AOS from "aos";
import {TranslateService} from "@ngx-translate/core";
import {AuthService} from "./authentication/auth/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  constructor(private translateService: TranslateService, private authService: AuthService) {
    this.translateService.setDefaultLang('en');
    this.translateService.use(localStorage.getItem('lang') || 'en');
  }

  ngAfterViewInit() {
    AOS.init();
  }

  ngOnInit() {
    this.authService.autoLogin();
  }
}
