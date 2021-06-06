export interface IBrand {
  id?: number;
  name?: string | null;
  cover?: string | null;
}

export class Brand implements IBrand {
  constructor(public id?: number, public name?: string | null, public cover?: string | null) {}
}
