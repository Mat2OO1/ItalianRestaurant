import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as AOS from "aos";
import {AuthService} from "./authentication/auth/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit{
  constructor(private authService: AuthService) {}


  ngAfterViewInit(){
    AOS.init();
  }

  ngOnInit() {
    this.authService.autoLogin();
  }
}
