import {AfterViewInit, Component} from '@angular/core';
import * as AOS from "aos";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit{
  title = 'ItalianRestaurant';

  ngAfterViewInit(){
    AOS.init();
  }
}
