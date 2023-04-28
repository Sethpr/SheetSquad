import { IBaseExtra } from 'app/shared/model/base-extra.model';
import { IQuality } from 'app/shared/model/quality.model';
import { IStat } from 'app/shared/model/stat.model';
import { ISkill } from 'app/shared/model/skill.model';
import { Capacity } from 'app/shared/model/enumerations/capacity.model';

export interface IExtra {
  id?: number;
  multiplier?: number;
  notes?: string | null;
  capacity?: Capacity | null;
  base?: IBaseExtra | null;
  powers?: IQuality[] | null;
  stats?: IStat[] | null;
  skills?: ISkill[] | null;
}

export const defaultValue: Readonly<IExtra> = {};
