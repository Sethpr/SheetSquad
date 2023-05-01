import { IPool } from 'app/shared/model/pool.model';
import { IPowerCategory } from 'app/shared/model/power-category.model';

export interface IPower {
  id?: number;
  name?: string;
  cost?: number;
  notes?: string | null;
  pool?: IPool | null;
  owner?: IPowerCategory | null;
}

export const defaultValue: Readonly<IPower> = {};
