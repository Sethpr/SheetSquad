import { IArchetype } from 'app/shared/model/archetype.model';
import { IUser } from 'app/shared/model/user.model';
import { IStat } from 'app/shared/model/stat.model';
import { ISkill } from 'app/shared/model/skill.model';

export interface ICharacter {
  id?: number;
  name?: string | null;
  talentName?: string | null;
  loyalty?: string | null;
  passion?: string | null;
  inventory?: string | null;
  pointTotal?: number;
  spentPoints?: number;
  archetype?: IArchetype | null;
  owner?: IUser | null;
  stats?: IStat[] | null;
  skills?: ISkill[] | null;
}

export const defaultValue: Readonly<ICharacter> = {};
