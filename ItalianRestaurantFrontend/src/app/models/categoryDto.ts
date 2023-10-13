export class CategoryDto {
  name: string;
  imgPath: string;

  constructor(name: string, imgPath : string) {
    this.name = name;
    this.imgPath = imgPath;
  }
}
