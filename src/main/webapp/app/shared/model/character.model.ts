import { IArchetype } from 'app/shared/model/archetype.model';
import { IUser } from 'app/shared/model/user.model';

export interface ICharacter {
  id?: number;
  name?: string;
  talentName?: string | null;
  loyalty?: string | null;
  passion?: string | null;
  inventory?: string | null;
  pointTotal?: number;
  spentPoints?: number;
  archetype?: IArchetype | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<ICharacter> = {};
