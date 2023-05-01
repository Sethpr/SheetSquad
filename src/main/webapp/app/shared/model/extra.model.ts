import { IBaseExtra } from 'app/shared/model/base-extra.model';
import { Capacity } from 'app/shared/model/enumerations/capacity.model';

export interface IExtra {
  id?: number;
  multiplier?: number;
  notes?: string | null;
  capacity?: Capacity | null;
  base?: IBaseExtra | null;
}

export const defaultValue: Readonly<IExtra> = {};
