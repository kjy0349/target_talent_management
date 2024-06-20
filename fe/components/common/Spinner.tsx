import React from "react";

interface SpinnerProps {
  size?: number; // 크기를 지정할 수 있는 옵션
  color?: string; // 색상을 지정할 수 있는 옵션
}

const Spinner: React.FC<SpinnerProps> = ({ size = 50, color = "#3498db" }) => {
  return (
    <div className="relative flex h-screen items-center justify-center">
      <div
        className="animate-spin rounded-full border-4 border-solid border-current border-t-transparent"
        style={{
          width: size,
          height: size,
          borderColor: `${color} transparent ${color} transparent`,
        }}
      ></div>
    </div>
  );
};

export default Spinner;
