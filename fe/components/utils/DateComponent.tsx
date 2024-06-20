import React from "react";

interface DateComponentProps {
  dateString: string;
}
function DateComponent({ dateString }: DateComponentProps) {
  const formattedDate = formatDate(dateString);
  function formatDate(dateString: string) {
    const year = dateString.substr(0, 4);
    const month = dateString.substr(4, 2);
    const day = dateString.substr(6, 2);

    return `${year.slice(-2)}년${month}월${day}일`;
  }
  return <div>{formattedDate}</div>;
}

export default DateComponent;
