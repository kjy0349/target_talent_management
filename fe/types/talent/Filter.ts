export default interface Filters {
  [key: string]: string[];
}

export interface FilterCount {
  data: string,
  count: number,
}