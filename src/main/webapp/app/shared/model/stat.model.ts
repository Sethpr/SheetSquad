import { IPool } from 'app/shared/model/pool.model';
import { IRefrence } from 'app/shared/model/refrence.model';
import { IExtra } from 'app/shared/model/extra.model';
import { ICharacter } from 'app/shared/model/character.model';
import { StatType } from 'app/shared/model/enumerations/stat-type.model';

export interface IStat {
  id?: number;
  type?: StatType;
  pool?: IPool | null;
  refrence?: IRefrence | null;
  extras?: IExtra[] | null;
  owners?: ICharacter[] | null;
}

export const defaultValue: Readonly<IStat> = {};
