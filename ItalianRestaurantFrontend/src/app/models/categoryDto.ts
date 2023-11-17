export class CategoryDto {
  name: string;
  name_pl?: string;
  image?: string;

  constructor(name: string, name_pl?: string, image? : string) {
    this.name = name;
    this.name_pl = name_pl;
    this.image = image;
  }
}
