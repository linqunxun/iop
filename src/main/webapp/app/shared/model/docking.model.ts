export interface IDocking {
  id?: number;
  name?: string | null;
  variable?: string | null;
}

export class Docking implements IDocking {
  constructor(public id?: number, public name?: string | null, public variable?: string | null) {}
}
