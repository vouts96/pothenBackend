export interface IPosition {
  id: number;
  name?: string | null;
}

export type NewPosition = Omit<IPosition, 'id'> & { id: null };
