import useFilterStore from '@/stores/filter';
import React, { useEffect, useMemo, useState } from 'react';
import {
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, TooltipProps, ResponsiveContainer, ReferenceLine, Label, LabelList, ReferenceArea, Legend
} from 'recharts';

interface GrapPollSizeata {
  data: string;
  count: number;
}

interface EnhancedAreaChartProps {
  identifier: string,
}

interface CustomTooltipProps extends TooltipProps<number, string> {
  active?: boolean;
  payload?: { color: string; value: number; }[];
  label?: string;
}

const CustomTooltip: React.FC<CustomTooltipProps> = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div className="custom-tooltip" style={{ backgroundColor: '#ffffff', padding: '5px', border: '1px solid #cccccc' }}>
        <p>{label}</p>
        <p style={{ color: payload[0].color }}>{payload[0].value}</p>
      </div>
    );
  }
  return null;
};

const EnhancedAreaChart = ({ identifier }: EnhancedAreaChartProps) => {
  const store = useFilterStore();
  const [data, setData] = useState<GrapPollSizeata[]>([]);

  useEffect(() => {
    if (identifier === "career") {
      setData(store.careerGrapPollSizeata);
    }
    else {
      setData(store.graduateGrapPollSizeata);
    }
  }, [identifier, store.careerGrapPollSizeata, store.graduateGrapPollSizeata]);

  const sortedData = useMemo(() => {
    const transformedData: GrapPollSizeata[] = data.map(a => a.data === "none" ? { data: "0", count: a.count } : a);
    const combinedData: GrapPollSizeata[] = transformedData.reduce((acc, curr) => {
      const existing = acc.find(item => item.data === curr.data);
      if (existing) {
        existing.count += curr.count;
      } else {
        acc.push(curr);
      }
      return acc;
    }, [] as GrapPollSizeata[]);
    return combinedData.sort((a, b) => parseInt(a.data, 10) - parseInt(b.data, 10));
  }, [data]);

  return (
    <div style={{ width: '100%', height: '250px' }}>
      <ResponsiveContainer>
        <AreaChart data={sortedData} margin={{ top: 30, right: 0, left: -10, bottom: 20 }}>
          <defs>
            <linearGradient id="colorGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#8884d8" stopOpacity={0} />
            </linearGradient>
          </defs>
          <XAxis dataKey="data">
            <Label value="연도(연차)" offset={0} position="bottom" />
          </XAxis>
          <YAxis>
            <Label value="누적 인원" offset={0} angle={-90} position='top' />
          </YAxis>
          <Tooltip content={<CustomTooltip />} />
          <Area type="monotone" dataKey="count" stroke="#8884d8" fillOpacity={0.5} fill="url(#colorGrad)" />
          <ReferenceArea x1={identifier === "career" ? store.careerMinYear : store.graduateMinYear} x2={identifier === "career" ? store.careerMaxYear : store.graduateMaxYear} stroke="red" strokeOpacity={0.3} fill="red" fillOpacity={0.1} />
        </AreaChart>
      </ResponsiveContainer>
    </div >
  );
};

export default EnhancedAreaChart;
