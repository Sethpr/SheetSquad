export interface IPool {
  id?: number;
  normal?: number | null;
  hard?: number | null;
  wiggle?: number | null;
  expert?: number | null;
}

export const defaultValue: Readonly<IPool> = {};
