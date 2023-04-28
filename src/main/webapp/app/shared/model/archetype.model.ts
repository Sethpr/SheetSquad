export interface IArchetype {
  id?: number;
  name?: string | null;
  cost?: number;
  notes?: string | null;
}

export const defaultValue: Readonly<IArchetype> = {};
