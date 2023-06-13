import { IPower } from 'app/shared/model/power.model';
import { IExtra } from 'app/shared/model/extra.model';
import { QualityType } from 'app/shared/model/enumerations/quality-type.model';
import { Capacity } from 'app/shared/model/enumerations/capacity.model';

export interface IQuality {
  id?: number;
  qualityType?: QualityType;
  capacity1?: Capacity;
  capacity2?: Capacity | null;
  capacity3?: Capacity | null;
  cost?: number;
  owner?: IPower | null;
  extra?: IExtra | null;
}

export const defaultValue: Readonly<IQuality> = {};
