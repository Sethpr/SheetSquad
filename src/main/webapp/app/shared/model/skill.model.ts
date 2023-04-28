import { IPool } from 'app/shared/model/pool.model';
import { IRefrence } from 'app/shared/model/refrence.model';
import { IExtra } from 'app/shared/model/extra.model';
import { ICharacter } from 'app/shared/model/character.model';
import { SkillType } from 'app/shared/model/enumerations/skill-type.model';
import { StatType } from 'app/shared/model/enumerations/stat-type.model';

export interface ISkill {
  id?: number;
  type?: SkillType;
  under?: StatType;
  pool?: IPool | null;
  refrence?: IRefrence | null;
  extras?: IExtra[] | null;
  owners?: ICharacter[] | null;
}

export const defaultValue: Readonly<ISkill> = {};
