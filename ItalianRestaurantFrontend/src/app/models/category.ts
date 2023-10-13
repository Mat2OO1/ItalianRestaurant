export class Category {
  id: number;
  name: string;
  imgPath: string;

  constructor(id: number, name: string, imgPath : string) {
    this.id = id;
    this.name = name;
    this.imgPath = imgPath;
  }
}
