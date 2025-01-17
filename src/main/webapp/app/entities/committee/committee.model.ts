export interface ICommittee {
  id: number;
  name?: string | null;
}

export type NewCommittee = Omit<ICommittee, 'id'> & { id: null };
