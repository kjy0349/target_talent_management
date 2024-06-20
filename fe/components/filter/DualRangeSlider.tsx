import 'rc-slider/assets/index.css';
import Slider from 'rc-slider';
import { handleRender } from './TooltipSlider';

interface DualRangeSliderProps {
  minYear: number;
  maxYear: number;
  onMinChange: (value: number) => void;
  onMaxChange: (value: number) => void;
}

export default function DualRangeSlider({ minYear, maxYear, onMinChange, onMaxChange }: DualRangeSliderProps) {
  const handleSliderChange = (values: number[] | number) => {
    if (Array.isArray(values)) {
      onMinChange(values[0]);
      onMaxChange(values[1]);
    }
  };

  return (
    <Slider
      range
      min={minYear}
      max={maxYear}
      defaultValue={[minYear, maxYear]}
      onChange={e => handleSliderChange(e)}
      allowCross={false}
      step={1}
      handleRender={handleRender}
      marks={{
        0: 0,
        5: 5,
        10: 10,
        15: 15,
        20: 20,
        25: 25,
        30: 30,
        40: 40,
        50: 50,
        60: 60,
        1970: 1970,
        1980: 1980,
        1990: 1990,
        2000: 2000,
        2005: 2005,
        2010: 2010,
        2015: 2015,
        2020: 2020,
        2025: 2025
      }}
    />
  );
};