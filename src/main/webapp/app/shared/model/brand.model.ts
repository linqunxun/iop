import { IEnterprise } from '@/shared/model/enterprise.model';

export interface IBrand {
  id?: number;
  name?: string | null;
  cover?: string | null;
  enterprise?: IEnterprise | null;
}

export class Brand implements IBrand {
  constructor(public id?: number, public name?: string | null, public cover?: string | null, public enterprise?: IEnterprise | null) {}
}
