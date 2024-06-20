export default function dateString(date?: string) {
  if (date) {
    const dateTo = new Date(date);
    return dateTo.getFullYear() + "." + dateTo.getMonth();
  }
  return null;

}