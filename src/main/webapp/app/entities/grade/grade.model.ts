export interface IGrade {
  id: number;
  name?: string | null;
}

export type NewGrade = Omit<IGrade, 'id'> & { id: null };
