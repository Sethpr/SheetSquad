import { IPool } from 'app/shared/model/pool.model';
import { IExtra } from 'app/shared/model/extra.model';
import { ICharacter } from 'app/shared/model/character.model';
import { IRefrence } from 'app/shared/model/refrence.model';
import { StatType } from 'app/shared/model/enumerations/stat-type.model';

export interface IStat {
  id?: number;
  statType?: StatType;
  pool?: IPool | null;
  extra?: IExtra | null;
  owner?: ICharacter | null;
  refrence?: IRefrence | null;
}

export const defaultValue: Readonly<IStat> = {};
