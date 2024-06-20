"use client";
import {
  CartesianGrid,
  DefaultTooltipContent,
  Line,
  LineChart,
  XAxis,
} from "recharts";

export function Chart() {
  const data = [
    { name: "A", uv: 400, pv: 2400 },
    { name: "B", uv: 300, pv: 1398 },
    { name: "C", uv: 200, pv: 9800 },
    { name: "D", uv: 278, pv: 3908 },
    { name: "E", uv: 189, pv: 4800 },
    { name: "F", uv: 239, pv: 3800 },
  ];
  return (
    <LineChart
      width={300}
      height={300}
      data={data}
      margin={{ top: 5, right: 20, left: 10, bottom: 5 }}
    >
      <XAxis dataKey="name" />
      <DefaultTooltipContent />
      <CartesianGrid stroke="#f5f5f5" />
      <Line type="monotone" dataKey="uv" stroke="#ff7300" yAxisId={0} />
      <Line type="monotone" dataKey="pv" stroke="#387908" yAxisId={1} />
    </LineChart>
  );
}
