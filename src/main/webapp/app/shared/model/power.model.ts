import { IPowerCategory } from 'app/shared/model/power-category.model';
import { IPool } from 'app/shared/model/pool.model';

export interface IPower {
  id?: number;
  name?: string;
  cost?: number;
  notes?: string | null;
  owner?: IPowerCategory | null;
  pool?: IPool | null;
}

export const defaultValue: Readonly<IPower> = {};
