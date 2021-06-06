import { IBrand } from '@/shared/model/brand.model';
import { IDocking } from '@/shared/model/docking.model';
import { IScenes } from '@/shared/model/scenes.model';

export interface IModel {
  id?: number;
  name?: string | null;
  codeName?: string | null;
  brand?: IBrand | null;
  docking?: IDocking | null;
  scenes?: IScenes[] | null;
}

export class Model implements IModel {
  constructor(
    public id?: number,
    public name?: string | null,
    public codeName?: string | null,
    public brand?: IBrand | null,
    public docking?: IDocking | null,
    public scenes?: IScenes[] | null
  ) {}
}
