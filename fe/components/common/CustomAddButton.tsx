import React, {useMemo} from "react";
import {HiPlus} from "react-icons/hi";

interface CustomAddButtonProps {
	onClick?: (e: React.MouseEvent) => void;
	size?: "sm" | "md" | "lg";
	className?: string;
};

const defaultClassNames = "border-2 rounded-full border-black hover:bg-gray-100"

const CustomAddButton = (props: CustomAddButtonProps) => {
	const buttonClassNames = useMemo(() => {
		switch (props.size) {
			case "sm":
				return (props.className ? props.className + " " : "") + defaultClassNames + " p-1 text-md";
			case "lg":
				return (props.className ? props.className + " " : "") + defaultClassNames + " p-3 text-xl";
			default:
				return (props.className ? props.className + " " : "") + defaultClassNames + " p-2 text-lg";
		}
	}, [props]);

	return (
		<button className={buttonClassNames} onClick={props.onClick}>
			<HiPlus className="text-inherit" />
		</button>
	);
};

export default CustomAddButton;