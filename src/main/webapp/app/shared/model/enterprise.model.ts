export interface IEnterprise {
  id?: number;
  name?: string | null;
  areaCode?: string | null;
}

export class Enterprise implements IEnterprise {
  constructor(public id?: number, public name?: string | null, public areaCode?: string | null) {}
}
