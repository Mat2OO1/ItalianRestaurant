export class Category {
  id?: number;
  name: string;
  name_pl?: string;


  constructor(id: number, name: string, image: string, name_pl?: string) {
    this.id = id;
    this.name = name;
    this.name_pl = name_pl;
  }
}
