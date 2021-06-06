import { IModel } from '@/shared/model/model.model';

export interface IDevice {
  id?: number;
  name?: string | null;
  code?: string | null;
  model?: IModel | null;
}

export class Device implements IDevice {
  constructor(public id?: number, public name?: string | null, public code?: string | null, public model?: IModel | null) {}
}
