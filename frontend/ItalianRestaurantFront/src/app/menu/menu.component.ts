import {Component, ElementRef} from '@angular/core';
@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {


  categories = ['Pasta Dishes', 'Pizza', 'Seafood']
  images = [
    'https://www.foodandwine.com/thmb/97PY4E6Wk95IYv1_8pDZvBEi0Uw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/cream-tomato-rigatoni-FT-RECIPE1020-139fb3fa52574e8bb06f98e7fa3e4f1e.jpg',
    'https://cdn.galleries.smcloud.net/t/galleries/gf-cgdk-p5yy-aE4f_pizza-pepperoni-z-jalapeno-to-jadl-joe-biden-z-zolnierzami-w-rzeszowie-1920x1080-nocrop.jpg',
    'https://www.foodandwine.com/thmb/tjkyiJutr0DdYGtGFy_hpcN0bSQ=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/seafood-blog618-f86e8baba8834537bbffa1d55e71c999.jpg'
  ]
  meals: {[key:string]: Meal[]} = {
    'Pasta Dishes': [new Meal('Carbonara','http://kuchnia-domowa.pl/images/content/176/spaghetti-carbonara.jpg','grana padano, pasta, basil'),
      new Meal('Aglio Oglio','https://italia-by-natalia.pl/wp-content/uploads/2020/11/aglio-olio-e-peperoncino.jpg','olive oil, garlic, pasta')],
    'Pizza': [new Meal("Margherita", '', 'tomato sauce, cheese')],
    'Seafood': [new Meal('Clams', '','some description')]
  }

  scroll(category: string){
    var target = document.getElementById(category);
    if(target !== null){
      target.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}

class Meal{
  name: string
  imgPath: string
  description: string
  constructor(name: string,
              imgPath: string,
              description: string) {
    this.name = name
    this.imgPath = imgPath
    this.description = description
  }

}
