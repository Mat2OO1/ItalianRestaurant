export class Table {
    id: number;
    number: number;
    seats: number;
    status: string;

    constructor(id: number, number: number, seats: number, status: string) {
        this.id = id;
        this.number = number;
        this.seats = seats;
        this.status = status;
    }
}

