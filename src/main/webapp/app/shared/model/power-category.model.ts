import { ICharacter } from 'app/shared/model/character.model';

export interface IPowerCategory {
  id?: number;
  name?: string;
  priority?: number | null;
  cost?: number;
  owner?: ICharacter | null;
}

export const defaultValue: Readonly<IPowerCategory> = {};
