export interface IParking {
  id?: number;
  locationName?: string;
  total?: number;
  occupiedNumber?: number;
}

export const defaultValue: Readonly<IParking> = {};
