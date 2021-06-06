import { IModel } from '@/shared/model/model.model';

export interface IScenes {
  id?: number;
  name?: string | null;
  cover?: string | null;
  desc?: string | null;
  dataSpec?: string | null;
  models?: IModel[] | null;
}

export class Scenes implements IScenes {
  constructor(
    public id?: number,
    public name?: string | null,
    public cover?: string | null,
    public desc?: string | null,
    public dataSpec?: string | null,
    public models?: IModel[] | null
  ) {}
}
